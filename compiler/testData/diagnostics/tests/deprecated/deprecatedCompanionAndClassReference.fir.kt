// ISSUE: KT-54209

class A {
    @Deprecated("Deprecated companion")
    companion object
}


fun test() {
    A::class
    <!DEPRECATION!>A<!>.<!DEPRECATION!>Companion<!>::class
}
