/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.lightTree.converter

import com.intellij.lang.LighterASTNode
import com.intellij.psi.TokenType
import com.intellij.util.diff.FlyweightCapableTreeStructure
import org.jetbrains.kotlin.*
import org.jetbrains.kotlin.ElementTypeUtils.getOperationSymbol
import org.jetbrains.kotlin.ElementTypeUtils.isExpression
import org.jetbrains.kotlin.KtNodeTypes.*
import org.jetbrains.kotlin.descriptors.EffectiveVisibility
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.descriptors.Visibilities
import org.jetbrains.kotlin.fir.*
import org.jetbrains.kotlin.fir.builder.*
import org.jetbrains.kotlin.fir.declarations.FirDeclarationOrigin
import org.jetbrains.kotlin.fir.declarations.FirVariable
import org.jetbrains.kotlin.fir.declarations.builder.buildAnonymousFunction
import org.jetbrains.kotlin.fir.declarations.builder.buildProperty
import org.jetbrains.kotlin.fir.declarations.builder.buildValueParameter
import org.jetbrains.kotlin.fir.declarations.impl.FirDeclarationStatusImpl
import org.jetbrains.kotlin.fir.declarations.impl.FirResolvedDeclarationStatusImpl
import org.jetbrains.kotlin.fir.diagnostics.*
import org.jetbrains.kotlin.fir.expressions.*
import org.jetbrains.kotlin.fir.expressions.builder.*
import org.jetbrains.kotlin.fir.expressions.impl.FirSingleExpressionBlock
import org.jetbrains.kotlin.fir.expressions.impl.buildSingleExpressionBlock
import org.jetbrains.kotlin.fir.lightTree.fir.ValueParameter
import org.jetbrains.kotlin.fir.lightTree.fir.WhenEntry
import org.jetbrains.kotlin.fir.lightTree.fir.addDestructuringStatements
import org.jetbrains.kotlin.fir.references.FirNamedReference
import org.jetbrains.kotlin.fir.references.FirSuperReference
import org.jetbrains.kotlin.fir.references.builder.buildErrorNamedReference
import org.jetbrains.kotlin.fir.references.builder.buildExplicitSuperReference
import org.jetbrains.kotlin.fir.references.builder.buildExplicitThisReference
import org.jetbrains.kotlin.fir.references.builder.buildSimpleNamedReference
import org.jetbrains.kotlin.fir.symbols.impl.FirAnonymousFunctionSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirPropertySymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirValueParameterSymbol
import org.jetbrains.kotlin.fir.types.FirTypeProjection
import org.jetbrains.kotlin.fir.types.FirTypeRef
import org.jetbrains.kotlin.fir.types.builder.buildErrorTypeRef
import org.jetbrains.kotlin.lexer.KtTokens.*
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.SpecialNames
import org.jetbrains.kotlin.psi.stubs.elements.KtConstantExpressionElementType
import org.jetbrains.kotlin.psi.stubs.elements.KtNameReferenceExpressionElementType
import org.jetbrains.kotlin.types.ConstantValueKind
import org.jetbrains.kotlin.types.expressions.OperatorConventions
import org.jetbrains.kotlin.util.OperatorNameConventions
import org.jetbrains.kotlin.utils.addToStdlib.runIf

class LightTreeRawFirExpressionBuilder(
    session: FirSession,
    tree: FlyweightCapableTreeStructure<LighterASTNode>,
    private val declarationBuilder: LightTreeRawFirDeclarationBuilder,
    context: Context<LighterASTNode> = Context(),
) : AbstractLightTreeRawFirBuilder(session, tree, context) {

    inline fun <reified R : FirExpression> getAsFirExpression(
        expression: LighterASTNode?,
        errorReason: String = "",
        sourceWhenInvalidExpression: LighterASTNode? = expression,
        isValidExpression: (R) -> Boolean = { !it.isStatementLikeExpression },
    ): R {
        val converted = expression?.let { convertExpression(it, errorReason) }

        return when {
            converted is R -> when {
                isValidExpression(converted) -> converted
                else -> buildErrorExpression(
                    sourceWhenInvalidExpression?.toFirSourceElement(),
                    ConeSimpleDiagnostic(errorReason, DiagnosticKind.ExpressionExpected),
                    converted,
                )
            }
            else -> buildErrorExpression(
                converted?.source?.realElement() ?: expression?.toFirSourceElement(),
                if (expression == null) ConeSyntaxDiagnostic(errorReason)
                else ConeSimpleDiagnostic(errorReason, DiagnosticKind.ExpressionExpected),
                converted,
            )
        } as R
    }

    fun getAsFirStatement(expression: LighterASTNode, errorReason: String = ""): FirStatement {
        return when (val converted = convertExpression(expression, errorReason)) {
            is FirStatement -> converted
            else -> buildErrorExpression(
                expression.toFirSourceElement(),
                ConeSimpleDiagnostic(errorReason, DiagnosticKind.ExpressionExpected),
                converted,
            )
        }
    }

    /*****    EXPRESSIONS    *****/
    fun convertExpression(expression: LighterASTNode, errorReason: String): FirElement {
        return when (expression.tokenType) {
            LAMBDA_EXPRESSION -> convertLambdaExpression(expression)
            BINARY_EXPRESSION -> convertBinaryExpression(expression)
            BINARY_WITH_TYPE -> convertBinaryWithTypeRHSExpression(expression) {
                this.getOperationSymbol().toFirOperation()
            }
            IS_EXPRESSION -> convertBinaryWithTypeRHSExpression(expression) {
                if (this == "is") FirOperation.IS else FirOperation.NOT_IS
            }
            LABELED_EXPRESSION -> convertLabeledExpression(expression)
            PREFIX_EXPRESSION, POSTFIX_EXPRESSION -> convertUnaryExpression(expression)
            ANNOTATED_EXPRESSION -> convertAnnotatedExpression(expression)
            CLASS_LITERAL_EXPRESSION -> convertClassLiteralExpression(expression)
            CALLABLE_REFERENCE_EXPRESSION -> convertCallableReferenceExpression(expression)
            in QUALIFIED_ACCESS -> convertQualifiedExpression(expression)
            CALL_EXPRESSION -> convertCallExpression(expression)
            WHEN -> convertWhenExpression(expression)
            ARRAY_ACCESS_EXPRESSION -> convertArrayAccessExpression(expression)
            COLLECTION_LITERAL_EXPRESSION -> convertCollectionLiteralExpression(expression)
            STRING_TEMPLATE -> convertStringTemplate(expression)
            is KtConstantExpressionElementType -> convertConstantExpression(expression)
            REFERENCE_EXPRESSION -> convertSimpleNameExpression(expression)
            DO_WHILE -> convertDoWhile(expression)
            WHILE -> convertWhile(expression)
            FOR -> convertFor(expression)
            TRY -> convertTryExpression(expression)
            IF -> convertIfExpression(expression)
            BREAK, CONTINUE -> convertLoopJump(expression)
            RETURN -> convertReturn(expression)
            THROW -> convertThrow(expression)
            PARENTHESIZED -> {
                val content = expression.getExpressionInParentheses()
                context.forwardLabelUsagePermission(expression, content)
                getAsFirExpression(content, "Empty parentheses")
            }
            PROPERTY_DELEGATE, INDICES, CONDITION, LOOP_RANGE ->
                getAsFirExpression(expression.getChildExpression(), errorReason)
            THIS_EXPRESSION -> convertThisExpression(expression)
            SUPER_EXPRESSION -> convertSuperExpression(expression)

            OBJECT_LITERAL -> declarationBuilder.convertObjectLiteral(expression)
            FUN -> declarationBuilder.convertFunctionDeclaration(expression)
            DESTRUCTURING_DECLARATION -> declarationBuilder.convertDestructingDeclaration(expression)
                .toFirDestructingDeclaration(this, baseModuleData)
            else -> buildErrorExpression(
                expression.toFirSourceElement(KtFakeSourceElementKind.ErrorTypeRef),
                ConeSimpleDiagnostic(errorReason, DiagnosticKind.ExpressionExpected)
            )
        }
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseFunctionLiteral
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitLambdaExpression
     */
    private fun convertLambdaExpression(lambdaExpression: LighterASTNode): FirExpression {
        val valueParameterList = mutableListOf<ValueParameter>()
        var block: LighterASTNode? = null
        var hasArrow = false

        val functionSymbol = FirAnonymousFunctionSymbol()
        lambdaExpression.getChildNodesByType(FUNCTION_LITERAL).first().forEachChildren {
            when (it.tokenType) {
                VALUE_PARAMETER_LIST -> valueParameterList += declarationBuilder.convertValueParameters(
                    valueParameters = it,
                    functionSymbol,
                    ValueParameterDeclaration.LAMBDA
                )
                BLOCK -> block = it
                ARROW -> hasArrow = true
            }
        }

        val expressionSource = lambdaExpression.toFirSourceElement()
        val target: FirFunctionTarget
        val anonymousFunction = buildAnonymousFunction {
            source = expressionSource
            moduleData = baseModuleData
            origin = FirDeclarationOrigin.Source
            returnTypeRef = implicitType
            receiverParameter = expressionSource.asReceiverParameter()
            symbol = functionSymbol
            isLambda = true
            hasExplicitParameterList = hasArrow
            label = context.getLastLabel(lambdaExpression) ?: context.calleeNamesForLambda.lastOrNull()?.let {
                buildLabel {
                    source = expressionSource.fakeElement(KtFakeSourceElementKind.GeneratedLambdaLabel)
                    name = it.asString()
                }
            }
            target = FirFunctionTarget(labelName = label?.name, isLambda = true)
            context.firFunctionTargets += target
            val destructuringStatements = mutableListOf<FirStatement>()
            for (valueParameter in valueParameterList) {
                val multiDeclaration = valueParameter.destructuringDeclaration
                valueParameters += if (multiDeclaration != null) {
                    val name = SpecialNames.DESTRUCT
                    val multiParameter = buildValueParameter {
                        source = valueParameter.firValueParameter.source
                        containingFunctionSymbol = functionSymbol
                        moduleData = baseModuleData
                        origin = FirDeclarationOrigin.Source
                        returnTypeRef = valueParameter.firValueParameter.returnTypeRef
                        this.name = name
                        symbol = FirValueParameterSymbol(name)
                        defaultValue = null
                        isCrossinline = false
                        isNoinline = false
                        isVararg = false
                    }
                    addDestructuringStatements(
                        destructuringStatements,
                        baseModuleData,
                        multiDeclaration,
                        multiParameter,
                        tmpVariable = false,
                        forceLocal = true,
                    )
                    multiParameter
                } else {
                    valueParameter.firValueParameter
                }
            }

            body = withForcedLocalContext {
                if (block != null) {
                    val kind = runIf(destructuringStatements.isNotEmpty()) {
                        KtFakeSourceElementKind.LambdaDestructuringBlock
                    }
                    val bodyBlock = declarationBuilder.convertBlockExpressionWithoutBuilding(block!!, kind).apply {
                        if (statements.isEmpty()) {
                            statements.add(
                                buildReturnExpression {
                                    source = expressionSource.fakeElement(KtFakeSourceElementKind.ImplicitReturn.FromExpressionBody)
                                    this.target = target
                                    result = buildUnitExpression {
                                        source = expressionSource.fakeElement(KtFakeSourceElementKind.ImplicitUnit.ForEmptyLambda)
                                    }
                                }
                            )
                        }
                    }.build()

                    if (destructuringStatements.isNotEmpty()) {
                        // Destructured variables must be in a separate block so that they can be shadowed.
                        buildBlock {
                            source = bodyBlock.source?.realElement()
                            statements.addAll(destructuringStatements)
                            statements.add(bodyBlock)
                        }
                    } else {
                        bodyBlock
                    }
                } else {
                    buildSingleExpressionBlock(buildErrorExpression(null, ConeSyntaxDiagnostic("Lambda has no body")))
                }
            }
            context.firFunctionTargets.removeLast()
        }.also {
            target.bind(it)
        }
        return buildAnonymousFunctionExpression {
            source = expressionSource
            this.anonymousFunction = anonymousFunction
        }
    }

    private fun foldOrConvertExpression(expression: LighterASTNode, source: KtLightSourceElement): FirStatement? {
        fun LighterASTNode?.getAsFirExpressionWithFold(errorReason: String = "", sourceWhenNotNull: () -> KtLightSourceElement = { this!!.toFirSourceElement() }): FirExpression? {
            if (this == null) {
                return buildErrorExpression(
                    source = null,
                    ConeSimpleDiagnostic(errorReason, DiagnosticKind.ExpressionExpected)
                )
            }

            val exprSource = sourceWhenNotNull()
            return when (val converted = foldOrConvertExpression(this, exprSource)) {
                is FirExpression -> when {
                    !converted.isStatementLikeExpression -> converted
                    else -> buildErrorExpression(
                        exprSource,
                        ConeSimpleDiagnostic(errorReason, DiagnosticKind.ExpressionExpected),
                        converted)
                }
                null -> null
                else -> buildErrorExpression(
                    converted.source?.realElement() ?: this.toFirSourceElement(),
                    ConeSimpleDiagnostic(errorReason, DiagnosticKind.ExpressionExpected),
                    converted,
                )
            }
        }

        when (expression.tokenType) {
            STRING_TEMPLATE -> {
                val children = expression.getChildrenAsArray().filter { it != null && it.tokenType != OPEN_QUOTE && it.tokenType != CLOSING_QUOTE }
                if (children.size == 1 && children[0]!!.tokenType == LITERAL_STRING_TEMPLATE_ENTRY && context.folder.canFold()) {
                    context.folder.fold(children[0]!!.asText)
                    return null
                } else {
                    context.folder.disable()
                    return convertStringTemplate(expression)
                }
            }
            BINARY_EXPRESSION -> {
                var isLeftArgument = true
                lateinit var operationTokenName: String
                var leftArgNode: LighterASTNode? = null
                var rightArg: LighterASTNode? = null
                var operationReferenceSource: KtLightSourceElement? = null
                expression.forEachChildren {
                    when (it.tokenType) {
                        OPERATION_REFERENCE -> {
                            isLeftArgument = false
                            operationTokenName = it.asText
                            operationReferenceSource = it.toFirSourceElement()
                        }
                        else -> if (it.isExpression()) {
                            if (isLeftArgument) {
                                leftArgNode = it
                            } else {
                                rightArg = it
                            }
                        }
                    }
                }
                val operationToken = operationTokenName.getOperationSymbol()

                if (operationToken != PLUS) {
                    context.folder.disable()
                }

                if (operationToken == IDENTIFIER) {
                    context.calleeNamesForLambda += operationTokenName.nameAsSafeName()
                } else {
                    context.calleeNamesForLambda += null
                }

                var lhs: FirElement? = null
                val checkpoint: StringLiteralsFolder.StringLiteralsFolderCheckpoint? =
                    if (context.folder.canFold()) context.folder.checkpoint() else null

                lhs = leftArgNode.getAsFirExpressionWithFold("No left operand")

                val rhs = if (lhs != null) {
                    if (context.folder.canInitialize() && rightArg != null) {
                        val rightSource = rightArg!!.toFirSourceElement()
                        context.folder.initialize(rightSource)
                        rightArg.getAsFirExpressionWithFold("No right operand", { rightSource })
                            ?: getFoldedStringLiterals()
                    } else {
                        context.folder.disable()
                        rightArg.getAsFirExpressionWithFold("No right operand")
                    }
                } else {
                    rightArg.getAsFirExpressionWithFold("No right operand")
                }

                context.calleeNamesForLambda.removeLast()

                if (rhs != null && lhs == null) {
                    if (checkpoint!!.isStart()) {
                        lhs = getFoldedStringLiterals(source = leftArgNode!!.toFirSourceElement())
                    } else {
                        checkpoint.rollback(context.folder)
                        context.folder.disable()
                        lhs = leftArgNode.getAsFirExpressionWithFold("No left operand")
                    }
                }

                if (rhs == null && lhs == null) {
                    return null
                }

                return createBinaryExpression(expression, lhs as FirExpression, rhs as FirExpression, operationTokenName, operationReferenceSource, leftArgNode, rightArg, source)
            }
            PARENTHESIZED -> {
                val content = expression.getExpressionInParentheses()
                context.forwardLabelUsagePermission(expression, content)
                return content.getAsFirExpressionWithFold("Empty parentheses")
            }
            else -> {
                context.folder.disable()
                return getAsFirStatement(expression, "")
            }
        }
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseBinaryExpression
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitBinaryExpression
     */
    private fun convertBinaryExpression(binaryExpression: LighterASTNode): FirStatement {
        val source = binaryExpression.toFirSourceElement()
        if (context.folder.canInitialize())
            context.folder.initialize(source)
        return foldOrConvertExpression(binaryExpression, source) ?: getFoldedStringLiterals()
    }

    private fun createBinaryExpression(
        binaryExpression: LighterASTNode,
        leftArgument: FirExpression,
        rightArgument: FirExpression,
        operationTokenName: String,
        operationReferenceSource: KtLightSourceElement?,
        leftArgNode: LighterASTNode?,
        rightArg: LighterASTNode?,
        source: KtLightSourceElement,
    ): FirStatement {
        val operationToken = operationTokenName.getOperationSymbol()
        when (operationToken) {
            ELVIS ->
                return leftArgument.generateNotNullOrOther(rightArgument, source)
            ANDAND, OROR ->
                return leftArgument.generateLazyLogicalOperation(rightArgument, operationToken == ANDAND, source)
            in OperatorConventions.IN_OPERATIONS ->
                return rightArgument.generateContainsOperation(leftArgument, operationToken == NOT_IN, source, operationReferenceSource)
            in OperatorConventions.COMPARISON_OPERATIONS ->
                return leftArgument.generateComparisonExpression(rightArgument, operationToken, source, operationReferenceSource)
        }
        val conventionCallName = operationToken.toBinaryName()
        return if (conventionCallName != null || operationToken == IDENTIFIER) {
            buildFunctionCall {
                this.source = source
                calleeReference = buildSimpleNamedReference {
                    this.source = operationReferenceSource ?: this@buildFunctionCall.source
                    name = conventionCallName ?: operationTokenName.nameAsSafeName()
                }
                explicitReceiver = leftArgument
                argumentList = buildUnaryArgumentList(rightArgument)
                origin = if (conventionCallName != null) FirFunctionCallOrigin.Operator else FirFunctionCallOrigin.Infix
            }
        } else {
            val firOperation = operationToken.toFirOperation()
            if (firOperation in FirOperation.ASSIGNMENTS) {
                return leftArgNode.generateAssignment(
                    source,
                    leftArgNode?.toFirSourceElement(),
                    rightArgument,
                    firOperation,
                    leftArgument.annotations,
                    rightArg,
                ) {
                    getAsFirExpression<FirExpression>(
                        this,
                        "Incorrect expression in assignment: ${binaryExpression.asText}",
                        sourceWhenInvalidExpression = binaryExpression,
                        isValidExpression = { !it.isStatementLikeExpression || it.isArraySet },
                    )
                }
            } else {
                buildEqualityOperatorCall {
                    this.source = source
                    operation = firOperation
                    argumentList = buildBinaryArgumentList(leftArgument, rightArgument)
                }
            }
        }
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.Precedence.parseRightHandSide
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitBinaryWithTypeRHSExpression
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitIsExpression
     */
    private fun convertBinaryWithTypeRHSExpression(
        binaryExpression: LighterASTNode,
        toFirOperation: String.() -> FirOperation,
    ): FirTypeOperatorCall {
        lateinit var operationTokenName: String
        var leftArgAsFir: FirExpression? = null
        lateinit var firType: FirTypeRef
        binaryExpression.forEachChildren {
            when (it.tokenType) {
                OPERATION_REFERENCE -> operationTokenName = it.asText
                TYPE_REFERENCE -> firType = declarationBuilder.convertType(it)
                else -> if (it.isExpression()) leftArgAsFir = getAsFirExpression(it, "No left operand")
            }
        }

        return buildTypeOperatorCall {
            source = binaryExpression.toFirSourceElement()
            operation = operationTokenName.toFirOperation()
            conversionTypeRef = firType
            argumentList = buildUnaryArgumentList(
                leftArgAsFir ?: buildErrorExpression(null, ConeSyntaxDiagnostic("No left operand"))
            )
        }
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseLabeledExpression
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitLabeledExpression
     */
    private fun convertLabeledExpression(labeledExpression: LighterASTNode): FirElement {
        var firExpression: FirElement? = null
        var labelSource: KtSourceElement? = null
        var forbiddenLabelKind: ForbiddenLabelKind? = null

        val isRepetitiveLabel = labeledExpression.getLabeledExpression()?.tokenType == LABELED_EXPRESSION

        labeledExpression.forEachChildren {
            context.setNewLabelUserNode(it)
            when (it.tokenType) {
                LABEL_QUALIFIER -> {
                    val name = it.asText.dropLast(1)
                    labelSource = it.getChildNodesByType(LABEL).single().toFirSourceElement()
                    context.addNewLabel(buildLabel(name, labelSource!!))
                    forbiddenLabelKind = getForbiddenLabelKind(name, isRepetitiveLabel)
                }
                BLOCK -> firExpression = declarationBuilder.convertBlock(it)
                PROPERTY -> firExpression = declarationBuilder.convertPropertyDeclaration(it)
                else -> if (it.isExpression()) firExpression = getAsFirStatement(it)
            }
        }

        context.dropLastLabel()

        return buildExpressionHandlingErrors(firExpression, labeledExpression.toFirSourceElement(), forbiddenLabelKind, labelSource)
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parsePostfixExpression
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parsePrefixExpression
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitUnaryExpression
     */
    private fun convertUnaryExpression(unaryExpression: LighterASTNode): FirExpression {
        lateinit var operationTokenName: String
        var argument: LighterASTNode? = null
        var operationReference: LighterASTNode? = null
        unaryExpression.forEachChildren {
            when (it.tokenType) {
                OPERATION_REFERENCE -> {
                    operationReference = it
                    operationTokenName = it.asText
                }
                else -> if (it.isExpression()) argument = it
            }
        }

        val operationToken = operationTokenName.getOperationSymbol()
        val conventionCallName = operationToken.toUnaryName()
        return when {
            operationToken == EXCLEXCL -> {
                buildCheckNotNullCall {
                    source = unaryExpression.toFirSourceElement()
                    argumentList = buildUnaryArgumentList(getAsFirExpression<FirExpression>(argument, "No operand"))
                }

            }
            conventionCallName != null -> {
                if (operationToken in OperatorConventions.INCREMENT_OPERATIONS) {
                    return generateIncrementOrDecrementBlock(
                        unaryExpression,
                        operationReference,
                        argument,
                        callName = conventionCallName,
                        prefix = unaryExpression.tokenType == PREFIX_EXPRESSION
                    ) { getAsFirExpression(this) }
                }
                val receiver = getAsFirExpression<FirExpression>(argument, "No operand")
                convertUnaryPlusMinusCallOnIntegerLiteralIfNecessary(unaryExpression, receiver, operationToken)?.let { return it }
                buildFunctionCall {
                    source = unaryExpression.toFirSourceElement()
                    calleeReference = buildSimpleNamedReference {
                        source = operationReference?.toFirSourceElement() ?: this@buildFunctionCall.source
                        name = conventionCallName
                    }
                    explicitReceiver = receiver
                    origin = FirFunctionCallOrigin.Operator
                }
            }
            else -> throw IllegalStateException("Unexpected expression: ${unaryExpression.asText}")
        }
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parsePrefixExpression
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitAnnotatedExpression
     */
    private fun convertAnnotatedExpression(annotatedExpression: LighterASTNode): FirElement {
        var firExpression: FirElement? = null
        val firAnnotationList = mutableListOf<FirAnnotation>()
        annotatedExpression.forEachChildren {
            when (it.tokenType) {
                ANNOTATION -> firAnnotationList += declarationBuilder.convertAnnotation(it)
                ANNOTATION_ENTRY -> firAnnotationList += declarationBuilder.convertAnnotationEntry(it)
                BLOCK -> firExpression = declarationBuilder.convertBlockExpression(it)
                else -> if (it.isExpression()) {
                    context.forwardLabelUsagePermission(annotatedExpression, it)
                    firExpression = getAsFirStatement(it)
                }
            }
        }

        val result = firExpression ?: buildErrorExpression(annotatedExpression.toFirSourceElement(), ConeNotAnnotationContainer("???"))
        require(result is FirAnnotationContainer)
        result.replaceAnnotations(result.annotations.smartPlus(firAnnotationList))
        return result
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseDoubleColonSuffix
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitClassLiteralExpression
     */
    private fun convertClassLiteralExpression(classLiteralExpression: LighterASTNode): FirExpression {
        var firReceiverExpression: FirExpression? = null
        classLiteralExpression.forEachChildren {
            if (it.isExpression()) firReceiverExpression = getAsFirExpression(it, "No receiver in class literal")
        }

        val classLiteralSource = classLiteralExpression.toFirSourceElement()

        return buildGetClassCall {
            source = classLiteralSource
            argumentList = buildUnaryArgumentList(
                firReceiverExpression
                    ?: buildErrorExpression(classLiteralSource, ConeUnsupportedClassLiteralsWithEmptyLhs)
            )
        }
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseDoubleColonSuffix
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitCallableReferenceExpression
     */
    private fun convertCallableReferenceExpression(callableReferenceExpression: LighterASTNode): FirExpression {
        var isReceiver = true
        var hasQuestionMarkAtLHS = false
        var firReceiverExpression: FirExpression? = null
        lateinit var namedReference: FirNamedReference
        callableReferenceExpression.forEachChildren {
            when (it.tokenType) {
                COLONCOLON -> isReceiver = false
                QUEST -> hasQuestionMarkAtLHS = true
                else -> if (it.isExpression()) {
                    if (isReceiver) {
                        firReceiverExpression = getAsFirExpression(it, "Incorrect receiver expression")
                    } else {
                        namedReference = createSimpleNamedReference(it.toFirSourceElement(), it)
                    }
                }
            }
        }

        return buildCallableReferenceAccess {
            source = callableReferenceExpression.toFirSourceElement()
            calleeReference = namedReference
            explicitReceiver = firReceiverExpression
            this.hasQuestionMarkAtLHS = hasQuestionMarkAtLHS
        }
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parsePostfixExpression
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitQualifiedExpression
     */
    private fun convertQualifiedExpression(dotQualifiedExpression: LighterASTNode): FirExpression {
        var isSelector = false
        var isSafe = false
        var firSelector: FirExpression? = null
        var firReceiver: FirExpression? = null //before dot
        dotQualifiedExpression.forEachChildren {
            when (val tokenType = it.tokenType) {
                DOT -> isSelector = true
                SAFE_ACCESS -> {
                    isSafe = true
                    isSelector = true
                }
                else -> {
                    val isEffectiveSelector = isSelector && tokenType != TokenType.ERROR_ELEMENT
                    val firExpression =
                        getAsFirExpression<FirExpression>(it, "Incorrect ${if (isEffectiveSelector) "selector" else "receiver"} expression")
                    if (isEffectiveSelector) {
                        val callExpressionCallee = if (tokenType == CALL_EXPRESSION) it.getFirstChildExpressionUnwrapped() else null
                        firSelector =
                            if (tokenType is KtNameReferenceExpressionElementType ||
                                (tokenType == CALL_EXPRESSION && callExpressionCallee?.tokenType != LAMBDA_EXPRESSION)
                            ) {
                                firExpression
                            } else {
                                buildErrorExpression {
                                    source = callExpressionCallee?.toFirSourceElement() ?: it.toFirSourceElement()
                                    diagnostic = ConeSimpleDiagnostic(
                                        "The expression cannot be a selector (occur after a dot)",
                                        if (callExpressionCallee == null) DiagnosticKind.IllegalSelector else DiagnosticKind.NoReceiverAllowed
                                    )
                                    expression = firExpression
                                }
                            }
                    } else {
                        firReceiver = firExpression
                    }
                }
            }
        }

        var result = firSelector
        (firSelector as? FirQualifiedAccessExpression)?.let {
            if (isSafe) {
                @OptIn(FirImplementationDetail::class)
                it.replaceSource(dotQualifiedExpression.toFirSourceElement(KtFakeSourceElementKind.DesugaredSafeCallExpression))
                return it.createSafeCall(
                    firReceiver!!,
                    dotQualifiedExpression.toFirSourceElement()
                )
            }

            result = convertFirSelector(it, dotQualifiedExpression.toFirSourceElement(), firReceiver!!)
        }

        val receiver = firReceiver
        if (receiver != null) {
            (firSelector as? FirErrorExpression)?.let { errorExpression ->
                return buildQualifiedErrorAccessExpression {
                    this.receiver = receiver
                    this.selector = errorExpression
                    source = dotQualifiedExpression.toFirSourceElement()
                    diagnostic = ConeSyntaxDiagnostic("Qualified expression with unexpected selector")
                }
            }
        }

        return result ?: buildErrorExpression {
            source = dotQualifiedExpression.toFirSourceElement()
            diagnostic = ConeSyntaxDiagnostic("Qualified expression without selector")

            // if there is no selector, we still want to resolve the receiver
            expression = firReceiver
        }
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseCallSuffix
     */
    private fun convertCallExpression(callSuffix: LighterASTNode): FirExpression {
        var name: String? = null
        val firTypeArguments = mutableListOf<FirTypeProjection>()
        val valueArguments = mutableListOf<LighterASTNode>()
        var additionalArgument: FirExpression? = null
        var hasArguments = false
        var superNode: LighterASTNode? = null
        callSuffix.forEachChildren { child ->
            fun process(node: LighterASTNode) {
                when (node.tokenType) {
                    REFERENCE_EXPRESSION -> {
                        name = node.asText
                    }
                    SUPER_EXPRESSION -> {
                        superNode = node
                    }
                    PARENTHESIZED -> if (node.tokenType != TokenType.ERROR_ELEMENT) {
                        additionalArgument = getAsFirExpression(node.getExpressionInParentheses(), "Incorrect invoke receiver")
                    }
                    TYPE_ARGUMENT_LIST -> {
                        firTypeArguments += declarationBuilder.convertTypeArguments(node, allowedUnderscoredTypeArgument = true)
                    }
                    VALUE_ARGUMENT_LIST, LAMBDA_ARGUMENT -> {
                        hasArguments = true
                        valueArguments += node
                    }
                    else -> if (node.tokenType != TokenType.ERROR_ELEMENT) {
                        additionalArgument = getAsFirExpression(node, "Incorrect invoke receiver")
                    }
                }
            }

            process(child)
        }

        val source = callSuffix.toFirSourceElement()

        val (calleeReference, receiverForInvoke) = when {
            name != null -> CalleeAndReceiver(
                buildSimpleNamedReference {
                    this.source = callSuffix.getFirstChildExpressionUnwrapped()?.toFirSourceElement() ?: source
                    this.name = name.nameAsSafeName()
                }
            )

            superNode != null || (additionalArgument as? FirResolvable)?.calleeReference is FirSuperReference -> {
                CalleeAndReceiver(
                    buildErrorNamedReference {
                        this.source = superNode?.toFirSourceElement() ?: (additionalArgument as? FirResolvable)?.calleeReference?.source
                        diagnostic = ConeSimpleDiagnostic("Super cannot be a callee", DiagnosticKind.SuperNotAllowed)
                    }
                )
            }

            additionalArgument != null -> {
                CalleeAndReceiver(
                    buildSimpleNamedReference {
                        this.source = source
                        this.name = OperatorNameConventions.INVOKE
                    },
                    additionalArgument!!,
                )
            }

            else -> CalleeAndReceiver(
                buildErrorNamedReference {
                    this.source = source
                    diagnostic = ConeSyntaxDiagnostic("Call has no callee")
                }
            )
        }

        val builder: FirQualifiedAccessExpressionBuilder = if (hasArguments) {
            val builder = if (receiverForInvoke != null) FirImplicitInvokeCallBuilder() else FirFunctionCallBuilder()
            builder.apply {
                this.source = source
                this.calleeReference = calleeReference

                context.calleeNamesForLambda += calleeReference.name
                this.extractArgumentsFrom(valueArguments.flatMap { convertValueArguments(it) })
                context.calleeNamesForLambda.removeLast()
            }
        } else {
            FirPropertyAccessExpressionBuilder().apply {
                this.source = source
                this.calleeReference = calleeReference
            }
        }
        return builder.apply {
            this.explicitReceiver = receiverForInvoke
            typeArguments += firTypeArguments
        }.build().pullUpSafeCallIfNecessary()
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseStringTemplate
     */
    private fun convertStringTemplate(stringTemplate: LighterASTNode): FirExpression {
        val children = stringTemplate.getChildrenAsArray()
        return children.toInterpolatingCall(
            stringTemplate,
            convertTemplateEntry = { convertShortOrLongStringTemplate(it) },
            prefix = { children.firstOrNull { it?.tokenType == INTERPOLATION_PREFIX }?.asText ?: "" }
        )
    }

    private fun LighterASTNode?.convertShortOrLongStringTemplate(errorReason: String): Collection<FirExpression> {
        val firExpressions = mutableListOf<FirExpression>()
        this?.forEachChildren(LONG_TEMPLATE_ENTRY_START, LONG_TEMPLATE_ENTRY_END, SHORT_TEMPLATE_ENTRY_START) {
            firExpressions.add(getAsFirExpression(it, errorReason))
        }
        return firExpressions
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseLiteralConstant
     */
    private fun convertConstantExpression(constantExpression: LighterASTNode): FirExpression {
        return generateConstantExpressionByLiteral(constantExpression)
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseWhen
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitWhenExpression
     */
    private fun convertWhenExpression(whenExpression: LighterASTNode): FirExpression {
        var subjectExpression: FirExpression? = null
        var subjectVariable: FirVariable? = null
        val whenEntryNodes = mutableListOf<LighterASTNode>()
        val whenEntries = mutableListOf<WhenEntry>()
        whenExpression.forEachChildren {
            when (it.tokenType) {
                PROPERTY -> subjectVariable = (declarationBuilder.convertPropertyDeclaration(it) as FirVariable).let { variable ->
                    buildProperty {
                        source = it.toFirSourceElement()
                        origin = FirDeclarationOrigin.Source
                        moduleData = baseModuleData
                        returnTypeRef = variable.returnTypeRef
                        name = variable.name
                        initializer = variable.initializer
                        isVar = false
                        symbol = FirPropertySymbol(variable.name)
                        isLocal = true
                        status = FirDeclarationStatusImpl(Visibilities.Local, Modality.FINAL)
                        annotations += variable.annotations
                    }
                }
                DESTRUCTURING_DECLARATION -> subjectExpression =
                    getAsFirExpression(it, "Incorrect when subject expression: ${whenExpression.asText}")
                WHEN_ENTRY -> whenEntryNodes += it
                else -> if (it.isExpression()) subjectExpression =
                    getAsFirExpression(it, "Incorrect when subject expression: ${whenExpression.asText}")
            }
        }
        subjectExpression = subjectVariable?.initializer ?: subjectExpression
        val hasSubject = subjectExpression != null

        @OptIn(FirContractViolation::class)
        val subject = FirExpressionRef<FirWhenExpression>()
        var shouldBind = hasSubject
        whenEntryNodes.mapTo(whenEntries) {
            convertWhenEntry(it, subject, hasSubject)
        }
        return buildWhenExpression {
            source = whenExpression.toFirSourceElement()
            this.subject = subjectExpression
            this.subjectVariable = subjectVariable
            usedAsExpression = whenExpression.usedAsExpression
            for (entry in whenEntries) {
                shouldBind = shouldBind || entry.shouldBindSubject
                val branch = entry.firBlock
                val entrySource = entry.node.toFirSourceElement()
                branches += if (!entry.isElse) {
                    if (hasSubject) {
                        val firCondition = entry.toFirWhenCondition()
                        buildWhenBranch(hasGuard = entry.guard != null) {
                            source = entrySource
                            condition = firCondition.guardedBy(entry.guard)
                            result = branch
                        }
                    } else {
                        val firCondition = entry.toFirWhenConditionWithoutSubject()
                        buildWhenBranch(hasGuard = entry.guard != null) {
                            source = entrySource
                            condition = firCondition.guardedBy(entry.guard)
                            result = branch
                        }
                    }
                } else {
                    buildWhenBranch(hasGuard = entry.guard != null) {
                        source = entrySource
                        condition = entry.guard ?: buildElseIfTrueCondition()
                        result = branch
                    }
                }
            }
        }.also {
            if (shouldBind) {
                subject.bind(it)
            }
        }
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseWhenEntry
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseWhenEntryNotElse
     */
    private fun convertWhenEntry(
        whenEntry: LighterASTNode,
        whenRefWithSubject: FirExpressionRef<FirWhenExpression>,
        hasSubject: Boolean,
    ): WhenEntry {
        var isElse = false
        var firBlock: FirBlock = buildEmptyExpressionBlock()
        val conditions = mutableListOf<FirExpression>()
        var guard: FirExpression? = null
        var shouldBindSubject = false
        whenEntry.forEachChildren {
            when (it.tokenType) {
                WHEN_CONDITION_EXPRESSION -> conditions += convertWhenConditionExpression(it, whenRefWithSubject.takeIf { hasSubject })
                WHEN_CONDITION_IN_RANGE -> {
                    val (condition, shouldBind) = convertWhenConditionInRange(it, whenRefWithSubject, hasSubject)
                    conditions += condition
                    shouldBindSubject = shouldBindSubject || shouldBind
                }
                WHEN_CONDITION_IS_PATTERN -> {
                    val (condition, shouldBind) = convertWhenConditionIsPattern(it, whenRefWithSubject, hasSubject)
                    conditions += condition
                    shouldBindSubject = shouldBindSubject || shouldBind
                }
                WHEN_ENTRY_GUARD -> guard = getAsFirExpression(it.getFirstChildExpressionUnwrapped(), "No expression in guard")
                ELSE_KEYWORD -> isElse = true
                BLOCK -> firBlock = declarationBuilder.convertBlock(it)
                else -> if (it.isExpression()) firBlock = declarationBuilder.convertBlock(it)
            }
        }

        return WhenEntry(conditions, guard, firBlock, whenEntry, isElse, shouldBindSubject)
    }

    private fun convertWhenConditionExpression(
        whenCondition: LighterASTNode,
        whenRefWithSubject: FirExpressionRef<FirWhenExpression>?,
    ): FirExpression {
        var firExpression: FirExpression? = null
        whenCondition.forEachChildren {
            when (it.tokenType) {
                else -> if (it.isExpression()) firExpression = getAsFirExpression(it, "No expression in condition with expression")
            }
        }

        val calculatedFirExpression = firExpression ?: buildErrorExpression(
            source = null,
            ConeSyntaxDiagnostic("No expression in condition with expression")
        )

        if (whenRefWithSubject == null) {
            return calculatedFirExpression
        }

        return buildEqualityOperatorCall {
            source = whenCondition.toFirSourceElement(KtFakeSourceElementKind.WhenCondition)
            operation = FirOperation.EQ
            argumentList = buildBinaryArgumentList(
                left = buildWhenSubjectExpression {
                    source = whenCondition.toFirSourceElement()
                    whenRef = whenRefWithSubject
                },
                right = calculatedFirExpression
            )
        }
    }

    private data class WhenConditionConvertedResults(val expression: FirExpression, val shouldBindSubject: Boolean)

    private fun convertWhenConditionInRange(
        whenCondition: LighterASTNode,
        whenRefWithSubject: FirExpressionRef<FirWhenExpression>,
        hasSubject: Boolean,
    ): WhenConditionConvertedResults {
        var isNegate = false
        var firExpression: FirExpression? = null
        var conditionSource: KtLightSourceElement? = null
        whenCondition.forEachChildren {
            when {
                it.tokenType == OPERATION_REFERENCE && it.asText == NOT_IN.value -> {
                    conditionSource = it.toFirSourceElement()
                    isNegate = true
                }
                it.tokenType == OPERATION_REFERENCE -> {
                    conditionSource = it.toFirSourceElement()
                }
                else -> if (it.isExpression()) firExpression = getAsFirExpression(it, "No range in condition with range")
            }
        }

        val subjectExpression = buildWhenSubjectExpression {
            whenRef = whenRefWithSubject
            source = whenCondition.toFirSourceElement()
        }

        val calculatedFirExpression = firExpression ?: buildErrorExpression(
            null,
            ConeSyntaxDiagnostic("No range in condition with range")
        )

        val result = calculatedFirExpression.generateContainsOperation(
            subjectExpression,
            inverted = isNegate,
            baseSource = whenCondition.toFirSourceElement(),
            operationReferenceSource = conditionSource
        )
        return createWhenConditionConvertedResults(hasSubject, result, whenCondition)
    }

    private fun convertWhenConditionIsPattern(
        whenCondition: LighterASTNode,
        whenRefWithSubject: FirExpressionRef<FirWhenExpression>,
        hasSubject: Boolean,
    ): WhenConditionConvertedResults {
        lateinit var firOperation: FirOperation
        var firType: FirTypeRef? = null
        whenCondition.forEachChildren {
            when (it.tokenType) {
                TYPE_REFERENCE -> firType = declarationBuilder.convertType(it)
                IS_KEYWORD -> firOperation = FirOperation.IS
                NOT_IS -> firOperation = FirOperation.NOT_IS
            }
        }

        val subjectExpression = buildWhenSubjectExpression {
            source = whenCondition.toFirSourceElement()
            whenRef = whenRefWithSubject
        }

        val result = buildTypeOperatorCall {
            source = whenCondition.toFirSourceElement()
            operation = firOperation
            conversionTypeRef = firType ?: buildErrorTypeRef { diagnostic = ConeSyntaxDiagnostic("Incomplete code") }
            argumentList = buildUnaryArgumentList(subjectExpression)
        }

        return createWhenConditionConvertedResults(hasSubject, result, whenCondition)
    }

    private fun createWhenConditionConvertedResults(
        hasSubject: Boolean,
        result: FirExpression,
        whenCondition: LighterASTNode,
    ): WhenConditionConvertedResults {
        return if (hasSubject) {
            WhenConditionConvertedResults(result, false)
        } else {
            WhenConditionConvertedResults(
                buildErrorExpression {
                    source = whenCondition.toFirSourceElement()
                    diagnostic = ConeSimpleDiagnostic(
                        "No expression in condition with expression",
                        DiagnosticKind.ExpressionExpected
                    )
                    nonExpressionElement = result
                },
                true,
            )
        }
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseArrayAccess
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitArrayAccessExpression
     */
    private fun convertArrayAccessExpression(arrayAccess: LighterASTNode): FirExpression {
        var firExpression: FirExpression? = null
        val indices: MutableList<FirExpression> = mutableListOf()
        arrayAccess.forEachChildren {
            when (it.tokenType) {
                INDICES -> indices += convertIndices(it)
                else -> if (it.isExpression()) firExpression = getAsFirExpression(it, "No array expression")
            }
        }
        val getArgument = context.arraySetArgument.remove(arrayAccess)
        return buildFunctionCall {
            val isGet = getArgument == null
            source = (if (isGet) arrayAccess else arrayAccess.getParent()!!).toFirSourceElement()
            calleeReference = buildSimpleNamedReference {
                source = arrayAccess.toFirSourceElement().fakeElement(KtFakeSourceElementKind.ArrayAccessNameReference)
                name = if (isGet) OperatorNameConventions.GET else OperatorNameConventions.SET
            }
            explicitReceiver =
                firExpression ?: buildErrorExpression(null, ConeSyntaxDiagnostic("No array expression"))
            argumentList = buildArgumentList {
                arguments += indices
                getArgument?.let { arguments += it }
            }
            origin = FirFunctionCallOrigin.Operator
        }.pullUpSafeCallIfNecessary()
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseCollectionLiteralExpression
     */
    private fun convertCollectionLiteralExpression(expression: LighterASTNode): FirExpression {
        val firExpressionList = mutableListOf<FirExpression>()
        expression.forEachChildren {
            if (it.isExpression()) firExpressionList += getAsFirExpression<FirExpression>(it, "Incorrect collection literal argument")
        }

        return buildArrayLiteral {
            source = expression.toFirSourceElement()
            argumentList = buildArgumentList {
                arguments += firExpressionList
            }
        }
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseAsCollectionLiteralExpression
     */
    private fun convertIndices(indices: LighterASTNode): List<FirExpression> {
        val firExpressionList: MutableList<FirExpression> = mutableListOf()
        indices.forEachChildren {
            if (it.isExpression()) firExpressionList += getAsFirExpression<FirExpression>(it, "Incorrect index expression")
        }

        return firExpressionList
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseSimpleNameExpression
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitSimpleNameExpression
     */
    private fun convertSimpleNameExpression(referenceExpression: LighterASTNode): FirQualifiedAccessExpression {
        val nameSource = referenceExpression.toFirSourceElement()
        val referenceSourceElement = if (nameSource.kind is KtFakeSourceElementKind) {
            nameSource
        } else {
            nameSource.fakeElement(KtFakeSourceElementKind.ReferenceInAtomicQualifiedAccess)
        }
        return buildPropertyAccessExpression {
            val rawText = referenceExpression.asText
            if (rawText.isUnderscore) {
                nonFatalDiagnostics.add(ConeUnderscoreUsageWithoutBackticks(nameSource))
            }
            source = nameSource
            calleeReference = createSimpleNamedReference(referenceSourceElement, referenceExpression)
        }
    }

    private fun createSimpleNamedReference(
        sourceElement: KtSourceElement,
        referenceExpression: LighterASTNode,
    ): FirNamedReference {
        return buildSimpleNamedReference {
            source = sourceElement
            name = referenceExpression.asText.nameAsSafeName()
        }
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseDoWhile
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitDoWhileExpression
     */
    private fun convertDoWhile(doWhileLoop: LighterASTNode): FirElement {
        var block: LighterASTNode? = null
        var firCondition: FirExpression? = null

        val target: FirLoopTarget
        return FirDoWhileLoopBuilder().apply {
            source = doWhileLoop.toFirSourceElement()
            // For break/continue in the do-while loop condition, prepare the loop target first so that it can refer to the same loop.
            target = prepareTarget(doWhileLoop)
            doWhileLoop.forEachChildren {
                when (it.tokenType) {
                    BODY -> block = it
                    CONDITION -> firCondition = getAsFirExpression(it, "No condition in do-while loop")
                }
            }
            condition =
                firCondition ?: buildErrorExpression(null, ConeSyntaxDiagnostic("No condition in do-while loop"))
        }.configure(target) { convertLoopBody(block) }
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseWhile
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitWhileExpression
     */
    private fun convertWhile(whileLoop: LighterASTNode): FirElement {
        var block: LighterASTNode? = null
        var firCondition: FirExpression? = null
        whileLoop.forEachChildren {
            when (it.tokenType) {
                BODY -> block = it
                CONDITION -> firCondition = getAsFirExpression(it, "No condition in while loop")
            }
        }

        val target: FirLoopTarget
        return FirWhileLoopBuilder().apply {
            source = whileLoop.toFirSourceElement()
            condition =
                firCondition ?: buildErrorExpression(null, ConeSyntaxDiagnostic("No condition in while loop"))
            // break/continue in the while loop condition will refer to an outer loop if any.
            // So, prepare the loop target after building the condition.
            target = prepareTarget(whileLoop)
        }.configure(target) { convertLoopBody(block) }
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseFor
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitForExpression
     */
    private fun convertFor(forLoop: LighterASTNode): FirElement {
        var parameter: ValueParameter? = null
        var rangeExpression: FirExpression? = null
        var blockNode: LighterASTNode? = null
        forLoop.forEachChildren {
            when (it.tokenType) {
                VALUE_PARAMETER -> parameter = declarationBuilder.convertValueParameter(it, null, ValueParameterDeclaration.FOR_LOOP)
                LOOP_RANGE -> rangeExpression = getAsFirExpression(it, "No range in for loop")
                BODY -> blockNode = it
            }
        }

        val calculatedRangeExpression =
            rangeExpression ?: buildErrorExpression(null, ConeSyntaxDiagnostic("No range in for loop"))
        val fakeSource = forLoop.toFirSourceElement(KtFakeSourceElementKind.DesugaredForLoop)
        val rangeSource = calculatedRangeExpression.source?.fakeElement(KtFakeSourceElementKind.DesugaredForLoop) ?: fakeSource
        val target: FirLoopTarget
        // NB: FirForLoopChecker relies on this block existence and structure
        return buildBlock {
            source = fakeSource
            val iteratorVal = generateTemporaryVariable(
                baseModuleData,
                rangeSource,
                SpecialNames.ITERATOR,
                buildFunctionCall {
                    source = rangeSource
                    calleeReference = buildSimpleNamedReference {
                        source = rangeSource
                        name = OperatorNameConventions.ITERATOR
                    }
                    explicitReceiver = calculatedRangeExpression
                    origin = FirFunctionCallOrigin.Operator
                }
            )
            statements += iteratorVal
            statements += FirWhileLoopBuilder().apply {
                source = fakeSource
                condition = buildFunctionCall {
                    source = rangeSource
                    calleeReference = buildSimpleNamedReference {
                        source = rangeSource
                        name = OperatorNameConventions.HAS_NEXT
                    }
                    explicitReceiver = generateResolvedAccessExpression(rangeSource, iteratorVal)
                    origin = FirFunctionCallOrigin.Operator
                }
                // break/continue in the for loop condition will refer to an outer loop if any.
                // So, prepare the loop target after building the condition.
                target = prepareTarget(forLoop)
            }.configure(target) {
                buildBlock block@{
                    source = blockNode?.toFirSourceElement()
                    val valueParameter = parameter ?: return@block
                    val multiDeclaration = valueParameter.destructuringDeclaration
                    val firLoopParameter = generateTemporaryVariable(
                        baseModuleData,
                        valueParameter.source,
                        if (multiDeclaration != null) SpecialNames.DESTRUCT else valueParameter.name,
                        buildFunctionCall {
                            source = rangeSource
                            calleeReference = buildSimpleNamedReference {
                                source = rangeSource
                                name = OperatorNameConventions.NEXT
                            }
                            explicitReceiver = generateResolvedAccessExpression(rangeSource, iteratorVal)
                            origin = FirFunctionCallOrigin.Operator
                        },
                        valueParameter.returnTypeRef,
                        extractedAnnotations = valueParameter.annotations
                    )
                    if (multiDeclaration != null) {
                        addDestructuringStatements(
                            statements,
                            baseModuleData,
                            multiDeclaration,
                            firLoopParameter,
                            tmpVariable = true,
                            forceLocal = true,
                        )
                    } else {
                        statements.add(firLoopParameter)
                    }
                    statements += convertLoopBody(blockNode)
                }
            }
        }
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseLoopBody
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.toFirBlock
     */
    private fun convertLoopBody(body: LighterASTNode?): FirBlock {
        return convertLoopOrIfBody(body) ?: buildEmptyExpressionBlock()
    }

    private fun convertLoopOrIfBody(body: LighterASTNode?): FirBlock? {
        var firBlock: FirBlock? = null
        var firStatement: FirStatement? = null
        body?.forEachChildren {
            when (it.tokenType) {
                BLOCK -> firBlock = declarationBuilder.convertBlockExpression(it)
                ANNOTATED_EXPRESSION -> {
                    if (it.getChildNodeByType(BLOCK) != null) {
                        firBlock = getAsFirExpression(it)
                    } else {
                        firStatement = getAsFirStatement(it)
                    }
                }
                else -> if (it.isExpression()) firStatement = getAsFirStatement(it)
            }
        }

        return firStatement?.let { FirSingleExpressionBlock(it) } ?: firBlock
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseTry
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitTryExpression
     */
    private fun convertTryExpression(tryExpression: LighterASTNode): FirExpression {
        lateinit var tryBlock: FirBlock
        val catchClauses = mutableListOf<Triple<ValueParameter?, FirBlock, KtLightSourceElement>>()
        var finallyBlock: FirBlock? = null
        tryExpression.forEachChildren {
            when (it.tokenType) {
                BLOCK -> tryBlock = declarationBuilder.convertBlock(it)
                CATCH -> convertCatchClause(it)?.also { oneClause -> catchClauses += oneClause }
                FINALLY -> finallyBlock = convertFinally(it)
            }
        }
        return buildTryExpression {
            source = tryExpression.toFirSourceElement()
            this.tryBlock = tryBlock
            this.finallyBlock = finallyBlock
            for ((parameter, block, clauseSource) in catchClauses) {
                if (parameter == null) continue
                catches += buildCatch {
                    this.parameter = buildProperty {
                        source = parameter.source
                        moduleData = baseModuleData
                        origin = FirDeclarationOrigin.Source
                        returnTypeRef = parameter.returnTypeRef
                        isVar = false
                        status = FirResolvedDeclarationStatusImpl(Visibilities.Local, Modality.FINAL, EffectiveVisibility.Local)
                        isLocal = true
                        this.name = parameter.name
                        symbol = FirPropertySymbol(CallableId(name))
                        annotations += parameter.annotations
                    }.also {
                        it.isCatchParameter = true
                    }
                    this.block = block
                    this.source = clauseSource
                }
            }
        }
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseTry
     */
    private fun convertCatchClause(catchClause: LighterASTNode): Triple<ValueParameter?, FirBlock, KtLightSourceElement>? {
        var valueParameter: ValueParameter? = null
        var blockNode: LighterASTNode? = null
        catchClause.forEachChildren {
            when (it.tokenType) {
                VALUE_PARAMETER_LIST -> valueParameter = declarationBuilder.convertValueParameters(
                    valueParameters = it,
                    FirAnonymousFunctionSymbol(),
                    ValueParameterDeclaration.CATCH
                ).firstOrNull()
                    ?: return null

                BLOCK -> blockNode = it
            }
        }

        return Triple(valueParameter, declarationBuilder.convertBlock(blockNode), catchClause.toFirSourceElement())
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseTry
     */
    private fun convertFinally(finallyExpression: LighterASTNode): FirBlock {
        var blockNode: LighterASTNode? = null
        finallyExpression.forEachChildren {
            when (it.tokenType) {
                BLOCK -> blockNode = it
            }
        }

        return declarationBuilder.convertBlock(blockNode)
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseIf
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitIfExpression
     */
    private fun convertIfExpression(ifExpression: LighterASTNode): FirExpression {
        return buildWhenExpression {
            source = ifExpression.toFirSourceElement()
            with(parseIfExpression(ifExpression)) {
                val trueBranch = convertLoopBody(thenBlock)
                branches += buildRegularWhenBranch {
                    source = firCondition?.source
                    condition = firCondition ?: buildErrorExpression(
                        null,
                        ConeSyntaxDiagnostic("If statement should have condition")
                    )
                    result = trueBranch
                }

                if (elseBlock != null) {
                    val elseBranch = convertLoopOrIfBody(elseBlock)
                    if (elseBranch != null) {
                        branches += buildRegularWhenBranch {
                            source = elseBlock.toFirSourceElement()
                            condition = buildElseIfTrueCondition()
                            result = elseBranch
                        }
                    }
                }
            }
            usedAsExpression = ifExpression.usedAsExpression
        }
    }

    private class IfNodeComponents(val firCondition: FirExpression?, val thenBlock: LighterASTNode?, val elseBlock: LighterASTNode?)

    private fun parseIfExpression(ifExpression: LighterASTNode): IfNodeComponents {
        var firCondition: FirExpression? = null
        var thenBlock: LighterASTNode? = null
        var elseBlock: LighterASTNode? = null
        ifExpression.forEachChildren {
            when (it.tokenType) {
                CONDITION -> firCondition = getAsFirExpression(it, "If statement should have condition")
                THEN -> thenBlock = it
                ELSE -> elseBlock = it
            }
        }
        return IfNodeComponents(firCondition, thenBlock, elseBlock)
    }

    private val LighterASTNode.usedAsExpression: Boolean
        get() {
            var parent = getParent() ?: return true
            while (parent.elementType == ANNOTATED_EXPRESSION ||
                parent.elementType == LABELED_EXPRESSION
            ) {
                parent = parent.getParent() ?: return true
            }
            val parentTokenType = parent.tokenType
            return when (parentTokenType) {
                BLOCK -> parent.getLastChildExpression() == this && parent.usedAsExpression
                TRY, CATCH -> parent.usedAsExpression
                THEN, ELSE, WHEN_ENTRY -> parent.getParent()?.usedAsExpression ?: true
                CLASS_INITIALIZER, SCRIPT_INITIALIZER, SECONDARY_CONSTRUCTOR, FUNCTION_LITERAL, FINALLY -> false
                FUN, PROPERTY_ACCESSOR -> parent.getChildrenAsArray().any { it?.tokenType == EQ }
                DOT_QUALIFIED_EXPRESSION -> parent.getFirstChild() == this
                BODY -> when (parent.getParent()?.tokenType) {
                    FOR, WHILE, DO_WHILE -> false
                    else -> true
                }
                else -> true
            }
        }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseJump
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitBreakExpression
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitContinueExpression
     */
    private fun convertLoopJump(jump: LighterASTNode): FirExpression {
        var isBreak = true
        jump.forEachChildren {
            when (it.tokenType) {
                CONTINUE_KEYWORD -> isBreak = false
                //BREAK -> isBreak = true
            }
        }

        val jumpBuilder = if (isBreak) FirBreakExpressionBuilder() else FirContinueExpressionBuilder()
        val sourceElement = jump.toFirSourceElement()
        return jumpBuilder.apply {
            source = sourceElement
        }.bindLabel(jump).build()
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseReturn
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitReturnExpression
     */
    private fun convertReturn(returnExpression: LighterASTNode): FirExpression {
        var labelName: String? = null
        var firExpression: FirExpression? = null
        returnExpression.forEachChildren {
            when (it.tokenType) {
                LABEL_QUALIFIER -> labelName = it.getAsStringWithoutBacktick().replace("@", "")
                else -> if (it.isExpression()) firExpression = getAsFirExpression(it, "Incorrect return expression")
            }
        }

        val calculatedFirExpression = firExpression ?: buildUnitExpression {
            source = returnExpression.toFirSourceElement(KtFakeSourceElementKind.ImplicitUnit.Return)
        }
        return calculatedFirExpression.toReturn(
            baseSource = returnExpression.toFirSourceElement(),
            labelName = labelName,
            fromKtReturnExpression = true
        )
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseThrow
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitThrowExpression
     */
    private fun convertThrow(throwExpression: LighterASTNode): FirExpression {
        var firExpression: FirExpression? = null
        throwExpression.forEachChildren {
            if (it.isExpression()) firExpression = getAsFirExpression(it, "Nothing to throw")
        }

        return buildThrowExpression {
            source = throwExpression.toFirSourceElement()
            exception = firExpression ?: buildErrorExpression(null, ConeSyntaxDiagnostic("Nothing to throw"))
        }
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseThisExpression
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitThisExpression
     */
    private fun convertThisExpression(thisExpression: LighterASTNode): FirQualifiedAccessExpression {
        val label: String? = thisExpression.getLabelName()
        return buildThisReceiverExpression {
            val sourceElement = thisExpression.toFirSourceElement()
            source = sourceElement
            calleeReference = buildExplicitThisReference {
                labelName = label
                source = sourceElement.fakeElement(KtFakeSourceElementKind.ReferenceInAtomicQualifiedAccess)
            }
        }
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseSuperExpression
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.visitSuperExpression
     */
    private fun convertSuperExpression(superExpression: LighterASTNode): FirQualifiedAccessExpression {
        val label: String? = superExpression.getLabelName()
        var superTypeRef: FirTypeRef = implicitType
        superExpression.forEachChildren {
            when (it.tokenType) {
                TYPE_REFERENCE -> superTypeRef = declarationBuilder.convertType(it)
            }
        }

        return buildPropertyAccessExpression {
            val sourceElement = superExpression.toFirSourceElement()
            source = sourceElement
            calleeReference = buildExplicitSuperReference {
                labelName = label
                this.superTypeRef = superTypeRef
                source = sourceElement.fakeElement(KtFakeSourceElementKind.ReferenceInAtomicQualifiedAccess)
            }
        }
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseValueArgumentList
     */
    fun convertValueArguments(valueArguments: LighterASTNode): List<FirExpression> {
        return valueArguments.forEachChildrenReturnList { node, container ->
            when (node.tokenType) {
                VALUE_ARGUMENT -> container += convertValueArgument(node)
                LAMBDA_EXPRESSION,
                LABELED_EXPRESSION,
                ANNOTATED_EXPRESSION,
                -> container += getAsFirExpression<FirAnonymousFunctionExpression>(node).apply {
                    // TODO(KT-66553) remove and set in builder
                    @OptIn(RawFirApi::class)
                    replaceIsTrailingLambda(newIsTrailingLambda = true)
                }
            }
        }
    }

    /**
     * @see org.jetbrains.kotlin.parsing.KotlinExpressionParsing.parseValueArgument
     * @see org.jetbrains.kotlin.fir.builder.RawFirBuilder.Visitor.toFirExpression(org.jetbrains.kotlin.psi.ValueArgument)
     */
    private fun convertValueArgument(valueArgument: LighterASTNode): FirExpression {
        var identifier: String? = null
        var isSpread = false
        var firExpression: FirExpression? = null
        valueArgument.forEachChildren {
            when (it.tokenType) {
                VALUE_ARGUMENT_NAME -> identifier = it.asText
                MUL -> isSpread = true
                STRING_TEMPLATE -> firExpression = convertStringTemplate(it)
                is KtConstantExpressionElementType -> firExpression = convertConstantExpression(it)
                else -> if (it.isExpression()) firExpression = getAsFirExpression(it, "Argument is absent")
            }
        }
        val calculatedFirExpression =
            firExpression ?: buildErrorExpression(null, ConeSyntaxDiagnostic("Argument is absent"))
        return when {
            identifier != null -> buildNamedArgumentExpression {
                source = valueArgument.toFirSourceElement()
                expression = calculatedFirExpression
                this.isSpread = isSpread
                name = identifier.nameAsSafeName()
            }
            isSpread -> buildSpreadArgumentExpression {
                source = valueArgument.toFirSourceElement()
                expression = calculatedFirExpression
            }
            else -> calculatedFirExpression
        }
    }
}
