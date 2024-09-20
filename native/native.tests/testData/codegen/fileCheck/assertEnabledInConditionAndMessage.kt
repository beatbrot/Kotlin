// TARGET_BACKEND: NATIVE
// FILECHECK_STAGE: CStubs
// ASSERTIONS_MODE: always-enable
// WITH_STDLIB

@OptIn(kotlin.experimental.ExperimentalNativeApi::class)

// CHECK-LABEL: define ptr @"kfun:#box(){}kotlin.String"
// CHECK: call void @"kfun:kotlin.AssertionError#<init>(kotlin.Any?){}"
// CHECK: call void @"kfun:kotlin.AssertionError#<init>(kotlin.Any?){}"
// CHECK: call void @"kfun:kotlin.AssertionError#<init>(kotlin.Any?){}"
fun box(): String {
    assert(assert(true).toString() != "") { assert(false) }
    return "OK"
}