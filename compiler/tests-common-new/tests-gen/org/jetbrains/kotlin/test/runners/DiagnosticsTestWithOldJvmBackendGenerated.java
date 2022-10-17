/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.test.runners;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.test.generators.GenerateCompilerTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("compiler/testData/diagnostics/testsWithJvmBackend")
@TestDataPath("$PROJECT_ROOT")
public class DiagnosticsTestWithOldJvmBackendGenerated extends AbstractDiagnosticsTestWithOldJvmBackend {
    @Test
    public void testAllFilesPresentInTestsWithJvmBackend() throws Exception {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/diagnostics/testsWithJvmBackend"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_OLD, true);
    }

    @Test
    @TestMetadata("indirectInlineCycle.kt")
    public void testIndirectInlineCycle() throws Exception {
        runTest("compiler/testData/diagnostics/testsWithJvmBackend/indirectInlineCycle.kt");
    }

    @Test
    @TestMetadata("inlineCycle.kt")
    public void testInlineCycle() throws Exception {
        runTest("compiler/testData/diagnostics/testsWithJvmBackend/inlineCycle.kt");
    }

    @Test
    @TestMetadata("multipleBigArityFunsImplemented.kt")
    public void testMultipleBigArityFunsImplemented() throws Exception {
        runTest("compiler/testData/diagnostics/testsWithJvmBackend/multipleBigArityFunsImplemented.kt");
    }

    @Test
    @TestMetadata("noWarningInLV1_5.kt")
    public void testNoWarningInLV1_5() throws Exception {
        runTest("compiler/testData/diagnostics/testsWithJvmBackend/noWarningInLV1_5.kt");
    }

    @Test
    @TestMetadata("propertyInlineCycle.kt")
    public void testPropertyInlineCycle() throws Exception {
        runTest("compiler/testData/diagnostics/testsWithJvmBackend/propertyInlineCycle.kt");
    }

    @Test
    @TestMetadata("suspendInlineCycle.kt")
    public void testSuspendInlineCycle() throws Exception {
        runTest("compiler/testData/diagnostics/testsWithJvmBackend/suspendInlineCycle.kt");
    }

    @Nested
    @TestMetadata("compiler/testData/diagnostics/testsWithJvmBackend/dataObjects")
    @TestDataPath("$PROJECT_ROOT")
    public class DataObjects {
        @Test
        public void testAllFilesPresentInDataObjects() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/diagnostics/testsWithJvmBackend/dataObjects"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_OLD, true);
        }
    }

    @Nested
    @TestMetadata("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature")
    @TestDataPath("$PROJECT_ROOT")
    public class DuplicateJvmSignature {
        @Test
        public void testAllFilesPresentInDuplicateJvmSignature() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_OLD, true);
        }

        @Test
        @TestMetadata("caseInProperties.kt")
        public void testCaseInProperties() throws Exception {
            runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/caseInProperties.kt");
        }

        @Test
        @TestMetadata("changingNullabilityOfOrdinaryClassIsBinaryCompatibleChange.kt")
        public void testChangingNullabilityOfOrdinaryClassIsBinaryCompatibleChange() throws Exception {
            runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/changingNullabilityOfOrdinaryClassIsBinaryCompatibleChange.kt");
        }

        @Test
        @TestMetadata("vararg.kt")
        public void testVararg() throws Exception {
            runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/vararg.kt");
        }

        @Nested
        @TestMetadata("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/accidentalOverrides")
        @TestDataPath("$PROJECT_ROOT")
        public class AccidentalOverrides {
            @Test
            @TestMetadata("accidentalOverrideFromGrandparent.kt")
            public void testAccidentalOverrideFromGrandparent() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/accidentalOverrides/accidentalOverrideFromGrandparent.kt");
            }

            @Test
            public void testAllFilesPresentInAccidentalOverrides() throws Exception {
                KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/accidentalOverrides"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_OLD, true);
            }

            @Test
            @TestMetadata("classFunctionOverriddenByProperty.kt")
            public void testClassFunctionOverriddenByProperty() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/accidentalOverrides/classFunctionOverriddenByProperty.kt");
            }

            @Test
            @TestMetadata("classFunctionOverriddenByPropertyInConstructor.kt")
            public void testClassFunctionOverriddenByPropertyInConstructor() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/accidentalOverrides/classFunctionOverriddenByPropertyInConstructor.kt");
            }

            @Test
            @TestMetadata("classFunctionOverriddenByPropertyNoGetter.kt")
            public void testClassFunctionOverriddenByPropertyNoGetter() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/accidentalOverrides/classFunctionOverriddenByPropertyNoGetter.kt");
            }

            @Test
            @TestMetadata("classPropertyOverriddenByFunction.kt")
            public void testClassPropertyOverriddenByFunction() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/accidentalOverrides/classPropertyOverriddenByFunction.kt");
            }

            @Test
            @TestMetadata("defaultFunction_old.kt")
            public void testDefaultFunction_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/accidentalOverrides/defaultFunction_old.kt");
            }

            @Test
            @TestMetadata("delegatedFunctionOverriddenByProperty_old.kt")
            public void testDelegatedFunctionOverriddenByProperty_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/accidentalOverrides/delegatedFunctionOverriddenByProperty_old.kt");
            }

            @Test
            @TestMetadata("genericClassFunction.kt")
            public void testGenericClassFunction() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/accidentalOverrides/genericClassFunction.kt");
            }

            @Test
            @TestMetadata("privateClassFunctionOverriddenByProperty.kt")
            public void testPrivateClassFunctionOverriddenByProperty() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/accidentalOverrides/privateClassFunctionOverriddenByProperty.kt");
            }

            @Test
            @TestMetadata("traitFunctionOverriddenByPropertyNoImpl.kt")
            public void testTraitFunctionOverriddenByPropertyNoImpl() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/accidentalOverrides/traitFunctionOverriddenByPropertyNoImpl.kt");
            }

            @Test
            @TestMetadata("traitFunctionOverriddenByProperty_old.kt")
            public void testTraitFunctionOverriddenByProperty_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/accidentalOverrides/traitFunctionOverriddenByProperty_old.kt");
            }

            @Test
            @TestMetadata("traitPropertyOverriddenByFunctionNoImpl.kt")
            public void testTraitPropertyOverriddenByFunctionNoImpl() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/accidentalOverrides/traitPropertyOverriddenByFunctionNoImpl.kt");
            }

            @Test
            @TestMetadata("traitPropertyOverriddenByFunction_old.kt")
            public void testTraitPropertyOverriddenByFunction_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/accidentalOverrides/traitPropertyOverriddenByFunction_old.kt");
            }
        }

        @Nested
        @TestMetadata("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/bridges")
        @TestDataPath("$PROJECT_ROOT")
        public class Bridges {
            @Test
            public void testAllFilesPresentInBridges() throws Exception {
                KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/bridges"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_OLD, true);
            }

            @Test
            @TestMetadata("class_old.kt")
            public void testClass_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/bridges/class_old.kt");
            }

            @Test
            @TestMetadata("fakeOverrideTrait_old.kt")
            public void testFakeOverrideTrait_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/bridges/fakeOverrideTrait_old.kt");
            }

            @Test
            @TestMetadata("trait_old.kt")
            public void testTrait_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/bridges/trait_old.kt");
            }
        }

        @Nested
        @TestMetadata("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/erasure")
        @TestDataPath("$PROJECT_ROOT")
        public class Erasure {
            @Test
            public void testAllFilesPresentInErasure() throws Exception {
                KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/erasure"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_OLD, true);
            }

            @Test
            @TestMetadata("clashFromInterfaceAndSuperClass_old.kt")
            public void testClashFromInterfaceAndSuperClass_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/erasure/clashFromInterfaceAndSuperClass_old.kt");
            }

            @Test
            @TestMetadata("collections.kt")
            public void testCollections() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/erasure/collections.kt");
            }

            @Test
            @TestMetadata("delegateToTwoTraits.kt")
            public void testDelegateToTwoTraits() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/erasure/delegateToTwoTraits.kt");
            }

            @Test
            @TestMetadata("delegationAndOwnMethod.kt")
            public void testDelegationAndOwnMethod() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/erasure/delegationAndOwnMethod.kt");
            }

            @Test
            @TestMetadata("delegationToTraitImplAndOwnMethod.kt")
            public void testDelegationToTraitImplAndOwnMethod() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/erasure/delegationToTraitImplAndOwnMethod.kt");
            }

            @Test
            @TestMetadata("extensionProperties.kt")
            public void testExtensionProperties() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/erasure/extensionProperties.kt");
            }

            @Test
            @TestMetadata("genericType.kt")
            public void testGenericType() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/erasure/genericType.kt");
            }

            @Test
            @TestMetadata("inheritFromTwoTraits_old.kt")
            public void testInheritFromTwoTraits_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/erasure/inheritFromTwoTraits_old.kt");
            }

            @Test
            @TestMetadata("kotlinAndJavaCollections.kt")
            public void testKotlinAndJavaCollections() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/erasure/kotlinAndJavaCollections.kt");
            }

            @Test
            @TestMetadata("nullableType.kt")
            public void testNullableType() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/erasure/nullableType.kt");
            }

            @Test
            @TestMetadata("superTraitAndDelegationToTraitImpl_old.kt")
            public void testSuperTraitAndDelegationToTraitImpl_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/erasure/superTraitAndDelegationToTraitImpl_old.kt");
            }

            @Test
            @TestMetadata("twoTraitsAndOwnFunction_old.kt")
            public void testTwoTraitsAndOwnFunction_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/erasure/twoTraitsAndOwnFunction_old.kt");
            }

            @Test
            @TestMetadata("typeMappedToJava.kt")
            public void testTypeMappedToJava() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/erasure/typeMappedToJava.kt");
            }

            @Test
            @TestMetadata("typeParameter.kt")
            public void testTypeParameter() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/erasure/typeParameter.kt");
            }

            @Test
            @TestMetadata("typeParameterWithBound.kt")
            public void testTypeParameterWithBound() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/erasure/typeParameterWithBound.kt");
            }

            @Test
            @TestMetadata("typeParameterWithTwoBounds.kt")
            public void testTypeParameterWithTwoBounds() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/erasure/typeParameterWithTwoBounds.kt");
            }

            @Test
            @TestMetadata("typeParameterWithTwoBoundsInWhere.kt")
            public void testTypeParameterWithTwoBoundsInWhere() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/erasure/typeParameterWithTwoBoundsInWhere.kt");
            }
        }

        @Nested
        @TestMetadata("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/finalMembersFromBuiltIns")
        @TestDataPath("$PROJECT_ROOT")
        public class FinalMembersFromBuiltIns {
            @Test
            public void testAllFilesPresentInFinalMembersFromBuiltIns() throws Exception {
                KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/finalMembersFromBuiltIns"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_OLD, true);
            }

            @Test
            @TestMetadata("enumMembers.kt")
            public void testEnumMembers() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/finalMembersFromBuiltIns/enumMembers.kt");
            }

            @Test
            @TestMetadata("waitNotifyGetClass_old.kt")
            public void testWaitNotifyGetClass_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/finalMembersFromBuiltIns/waitNotifyGetClass_old.kt");
            }
        }

        @Nested
        @TestMetadata("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/functionAndProperty")
        @TestDataPath("$PROJECT_ROOT")
        public class FunctionAndProperty {
            @Test
            public void testAllFilesPresentInFunctionAndProperty() throws Exception {
                KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/functionAndProperty"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_OLD, true);
            }

            @Test
            @TestMetadata("class.kt")
            public void testClass() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/functionAndProperty/class.kt");
            }

            @Test
            @TestMetadata("classObject.kt")
            public void testClassObject() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/functionAndProperty/classObject.kt");
            }

            @Test
            @TestMetadata("classPropertyInConstructor.kt")
            public void testClassPropertyInConstructor() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/functionAndProperty/classPropertyInConstructor.kt");
            }

            @Test
            @TestMetadata("extensionFunctionAndNormalFunction.kt")
            public void testExtensionFunctionAndNormalFunction() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/functionAndProperty/extensionFunctionAndNormalFunction.kt");
            }

            @Test
            @TestMetadata("extensionPropertyAndFunction.kt")
            public void testExtensionPropertyAndFunction() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/functionAndProperty/extensionPropertyAndFunction.kt");
            }

            @Test
            @TestMetadata("functionAndSetter.kt")
            public void testFunctionAndSetter() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/functionAndProperty/functionAndSetter.kt");
            }

            @Test
            @TestMetadata("functionAndVar.kt")
            public void testFunctionAndVar() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/functionAndProperty/functionAndVar.kt");
            }

            @Test
            @TestMetadata("localClass.kt")
            public void testLocalClass() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/functionAndProperty/localClass.kt");
            }

            @Test
            @TestMetadata("localClassInClass.kt")
            public void testLocalClassInClass() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/functionAndProperty/localClassInClass.kt");
            }

            @Test
            @TestMetadata("nestedClass.kt")
            public void testNestedClass() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/functionAndProperty/nestedClass.kt");
            }

            @Test
            @TestMetadata("object.kt")
            public void testObject() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/functionAndProperty/object.kt");
            }

            @Test
            @TestMetadata("objectExpression.kt")
            public void testObjectExpression() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/functionAndProperty/objectExpression.kt");
            }

            @Test
            @TestMetadata("objectExpressionInConstructor.kt")
            public void testObjectExpressionInConstructor() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/functionAndProperty/objectExpressionInConstructor.kt");
            }

            @Test
            @TestMetadata("privateClassPropertyNoClash.kt")
            public void testPrivateClassPropertyNoClash() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/functionAndProperty/privateClassPropertyNoClash.kt");
            }

            @Test
            @TestMetadata("topLevel.kt")
            public void testTopLevel() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/functionAndProperty/topLevel.kt");
            }

            @Test
            @TestMetadata("topLevelDifferentFiles.kt")
            public void testTopLevelDifferentFiles() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/functionAndProperty/topLevelDifferentFiles.kt");
            }

            @Test
            @TestMetadata("topLevelGetter.kt")
            public void testTopLevelGetter() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/functionAndProperty/topLevelGetter.kt");
            }

            @Test
            @TestMetadata("trait_old.kt")
            public void testTrait_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/functionAndProperty/trait_old.kt");
            }
        }

        @Nested
        @TestMetadata("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/specialNames")
        @TestDataPath("$PROJECT_ROOT")
        public class SpecialNames {
            @Test
            public void testAllFilesPresentInSpecialNames() throws Exception {
                KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/specialNames"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_OLD, true);
            }

            @Test
            @TestMetadata("classObjectCopiedField.kt")
            public void testClassObjectCopiedField() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/specialNames/classObjectCopiedField.kt");
            }

            @Test
            @TestMetadata("classObjectCopiedFieldObject_old.kt")
            public void testClassObjectCopiedFieldObject_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/specialNames/classObjectCopiedFieldObject_old.kt");
            }

            @Test
            @TestMetadata("classObject_old.kt")
            public void testClassObject_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/specialNames/classObject_old.kt");
            }

            @Test
            @TestMetadata("dataClassCopy.kt")
            public void testDataClassCopy() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/specialNames/dataClassCopy.kt");
            }

            @Test
            @TestMetadata("defaults_old.kt")
            public void testDefaults_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/specialNames/defaults_old.kt");
            }

            @Test
            @TestMetadata("delegationBy_old.kt")
            public void testDelegationBy_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/specialNames/delegationBy_old.kt");
            }

            @Test
            @TestMetadata("enum.kt")
            public void testEnum() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/specialNames/enum.kt");
            }

            @Test
            @TestMetadata("innerClassField_old.kt")
            public void testInnerClassField_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/specialNames/innerClassField_old.kt");
            }

            @Test
            @TestMetadata("instance_old.kt")
            public void testInstance_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/specialNames/instance_old.kt");
            }

            @Test
            @TestMetadata("propertyMetadataCache_old.kt")
            public void testPropertyMetadataCache_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/specialNames/propertyMetadataCache_old.kt");
            }
        }

        @Nested
        @TestMetadata("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/statics")
        @TestDataPath("$PROJECT_ROOT")
        public class Statics {
            @Test
            public void testAllFilesPresentInStatics() throws Exception {
                KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/statics"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_OLD, true);
            }

            @Test
            @TestMetadata("jkjk.kt")
            public void testJkjk() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/statics/jkjk.kt");
            }

            @Test
            @TestMetadata("kotlinClassExtendsJavaClass.kt")
            public void testKotlinClassExtendsJavaClass() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/statics/kotlinClassExtendsJavaClass.kt");
            }

            @Test
            @TestMetadata("kotlinClassExtendsJavaClassExtendsJavaClass.kt")
            public void testKotlinClassExtendsJavaClassExtendsJavaClass() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/statics/kotlinClassExtendsJavaClassExtendsJavaClass.kt");
            }

            @Test
            @TestMetadata("kotlinClassImplementsJavaInterface.kt")
            public void testKotlinClassImplementsJavaInterface() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/statics/kotlinClassImplementsJavaInterface.kt");
            }

            @Test
            @TestMetadata("kotlinClassImplementsJavaInterfaceExtendsJavaInteface.kt")
            public void testKotlinClassImplementsJavaInterfaceExtendsJavaInteface() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/statics/kotlinClassImplementsJavaInterfaceExtendsJavaInteface.kt");
            }

            @Test
            @TestMetadata("kotlinMembersVsJavaNonVisibleStatics.kt")
            public void testKotlinMembersVsJavaNonVisibleStatics() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/statics/kotlinMembersVsJavaNonVisibleStatics.kt");
            }
        }

        @Nested
        @TestMetadata("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/synthesized")
        @TestDataPath("$PROJECT_ROOT")
        public class Synthesized {
            @Test
            public void testAllFilesPresentInSynthesized() throws Exception {
                KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/synthesized"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_OLD, true);
            }

            @Test
            @TestMetadata("enumValuesValueOf.kt")
            public void testEnumValuesValueOf() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/synthesized/enumValuesValueOf.kt");
            }
        }

        @Nested
        @TestMetadata("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/traitImpl")
        @TestDataPath("$PROJECT_ROOT")
        public class TraitImpl {
            @Test
            public void testAllFilesPresentInTraitImpl() throws Exception {
                KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/traitImpl"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_OLD, true);
            }

            @Test
            @TestMetadata("defaultVsNonDefault_old.kt")
            public void testDefaultVsNonDefault_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/traitImpl/defaultVsNonDefault_old.kt");
            }

            @Test
            @TestMetadata("kt43611.kt")
            public void testKt43611() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/traitImpl/kt43611.kt");
            }

            @Test
            @TestMetadata("oneTrait_old.kt")
            public void testOneTrait_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/traitImpl/oneTrait_old.kt");
            }

            @Test
            @TestMetadata("traitFunctionOverriddenByPropertyInTrait_old.kt")
            public void testTraitFunctionOverriddenByPropertyInTrait_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/traitImpl/traitFunctionOverriddenByPropertyInTrait_old.kt");
            }

            @Test
            @TestMetadata("traitPropertyOverriddenByFunctionInTrait_old.kt")
            public void testTraitPropertyOverriddenByFunctionInTrait_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/traitImpl/traitPropertyOverriddenByFunctionInTrait_old.kt");
            }

            @Test
            @TestMetadata("twoTraits_old.kt")
            public void testTwoTraits_old() throws Exception {
                runTest("compiler/testData/diagnostics/testsWithJvmBackend/duplicateJvmSignature/traitImpl/twoTraits_old.kt");
            }
        }
    }

    @Nested
    @TestMetadata("compiler/testData/diagnostics/testsWithJvmBackend/multifileClasses")
    @TestDataPath("$PROJECT_ROOT")
    public class MultifileClasses {
        @Test
        public void testAllFilesPresentInMultifileClasses() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/diagnostics/testsWithJvmBackend/multifileClasses"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_OLD, true);
        }

        @Test
        @TestMetadata("jvmSynthetic.kt")
        public void testJvmSynthetic() throws Exception {
            runTest("compiler/testData/diagnostics/testsWithJvmBackend/multifileClasses/jvmSynthetic.kt");
        }
    }

    @Nested
    @TestMetadata("compiler/testData/diagnostics/testsWithJvmBackend/scripts")
    @TestDataPath("$PROJECT_ROOT")
    public class Scripts {
        @Test
        public void testAllFilesPresentInScripts() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/diagnostics/testsWithJvmBackend/scripts"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_OLD, true);
        }
    }

    @Nested
    @TestMetadata("compiler/testData/diagnostics/testsWithJvmBackend/typeOf")
    @TestDataPath("$PROJECT_ROOT")
    public class TypeOf {
        @Test
        public void testAllFilesPresentInTypeOf() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/diagnostics/testsWithJvmBackend/typeOf"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_OLD, true);
        }

        @Test
        @TestMetadata("nonReifiedTypeParameterWithRecursiveBound.kt")
        public void testNonReifiedTypeParameterWithRecursiveBound() throws Exception {
            runTest("compiler/testData/diagnostics/testsWithJvmBackend/typeOf/nonReifiedTypeParameterWithRecursiveBound.kt");
        }

        @Test
        @TestMetadata("suspendType.kt")
        public void testSuspendType() throws Exception {
            runTest("compiler/testData/diagnostics/testsWithJvmBackend/typeOf/suspendType.kt");
        }
    }

    @Nested
    @TestMetadata("compiler/testData/diagnostics/testsWithJvmBackend/valueClasses")
    @TestDataPath("$PROJECT_ROOT")
    public class ValueClasses {
        @Test
        public void testAllFilesPresentInValueClasses() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("compiler/testData/diagnostics/testsWithJvmBackend/valueClasses"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_OLD, true);
        }

        @Test
        @TestMetadata("cloneable.kt")
        public void testCloneable() throws Exception {
            runTest("compiler/testData/diagnostics/testsWithJvmBackend/valueClasses/cloneable.kt");
        }
    }
}
