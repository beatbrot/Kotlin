/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.fir.analysis.diagnostics.native

import org.jetbrains.kotlin.diagnostics.KtDiagnosticFactoryToRendererMap
import org.jetbrains.kotlin.diagnostics.KtDiagnosticRenderers.TO_STRING
import org.jetbrains.kotlin.diagnostics.rendering.BaseDiagnosticRendererFactory
import org.jetbrains.kotlin.fir.analysis.diagnostics.FirDiagnosticRenderers.SYMBOL
import org.jetbrains.kotlin.fir.analysis.diagnostics.FirDiagnosticRenderers.SYMBOLS
import org.jetbrains.kotlin.fir.analysis.diagnostics.checkMissingMessages
import org.jetbrains.kotlin.fir.analysis.diagnostics.native.FirNativeErrors.INCOMPATIBLE_THROWS_INHERITED
import org.jetbrains.kotlin.fir.analysis.diagnostics.native.FirNativeErrors.INCOMPATIBLE_THROWS_OVERRIDE
import org.jetbrains.kotlin.fir.analysis.diagnostics.native.FirNativeErrors.MISSING_EXCEPTION_IN_THROWS_ON_SUSPEND
import org.jetbrains.kotlin.fir.analysis.diagnostics.native.FirNativeErrors.THROWS_LIST_EMPTY

object FirNativeErrorsDefaultMessages : BaseDiagnosticRendererFactory() {
    override val MAP = KtDiagnosticFactoryToRendererMap("FIR").also { map ->
        map.put(THROWS_LIST_EMPTY, "Throws must have non-empty class list")
        map.put(INCOMPATIBLE_THROWS_OVERRIDE, "Member overrides different @Throws filter from {0}", SYMBOL)
        map.put(INCOMPATIBLE_THROWS_INHERITED, "Member inherits different @Throws filters from {0}", SYMBOLS)
        map.put(
            MISSING_EXCEPTION_IN_THROWS_ON_SUSPEND, "@Throws on suspend declaration must have {0} (or any of its superclasses) listed",
            TO_STRING
        )

        map.checkMissingMessages(FirNativeErrors)
    }
}
