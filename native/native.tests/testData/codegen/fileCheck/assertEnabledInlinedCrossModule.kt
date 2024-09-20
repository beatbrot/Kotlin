// TARGET_BACKEND: NATIVE
// FILECHECK_STAGE: CStubs
// ASSERTIONS_MODE: always-enable
// WITH_STDLIB

// MODULE: lib
// FILE: lib.kt
@OptIn(kotlin.experimental.ExperimentalNativeApi::class)

// CHECK-LABEL: define void @"kfun:#foo(kotlin.Boolean){}"
// CHECK: call void @"kfun:kotlin.AssertionError#<init>(kotlin.Any?){}"
inline fun foo(x: Boolean) = assert(x)

// MODULE: main
// FILE: main.kt
// CHECK-LABEL: define ptr @"kfun:#box(){}kotlin.String"
// CHECK: call void @"kfun:kotlin.AssertionError#<init>(kotlin.Any?){}"
fun box(): String {
    foo(true)
    return "OK"
}