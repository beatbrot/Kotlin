// DIAGNOSTICS: -UNUSED_PARAMETER
// LATEST_LV_DIFFERENCE

@Target(AnnotationTarget.EXPRESSION)
@Retention(AnnotationRetention.SOURCE)
annotation class Ann1

@Target(AnnotationTarget.EXPRESSION)
@Retention(AnnotationRetention.SOURCE)
annotation class Ann2(val x: String)

fun bar() {}
fun bar(block: () -> Unit) {}

fun foo(y: IntArray) {
    @Ann1 bar()
    @Ann1 bar() { }
    @Ann1 bar { }

    @Ann2("") bar()
    @Ann2("") bar() { }
    @Ann2("") bar { }

    @Ann1 @Ann2("") bar()

    var x = 1

    @Ann1 ++x
    @Ann1 x++
    @Ann2("") ++x
    @Ann2("") x++
    @Ann1 @Ann2("") ++x
    @Ann1 @Ann2("") x++

    @Ann1 y[0]

    @Ann1 { x: Int -> x }
    @Ann1 { x: Int -> x }(1)
    @Ann1 object { fun foo() = 1 }
    @Ann1 object { fun foo() = 1 }.foo()

    @Ann1() (x * x)
    var z = 1
    @Ann1 x + z

    <!WRAPPED_LHS_IN_ASSIGNMENT_WARNING!>@Ann1 x = x + 2<!>
    <!WRAPPED_LHS_IN_ASSIGNMENT_WARNING!>@Ann1 x += z + 2<!>

    @Ann1 x + 6 * 2 > 0
    @Ann1 x * 6 + 2 > 0

    @Ann1 object { operator fun plus(x: Int) = 1 } + 1
    @Ann1 object { operator fun plus(x: Int) = 1 } + 1 * 4 > 0

    @Ann1 x foo z + 8

    1 + @Ann1 x
    1 + @Ann1 x * z + 8

    x foo @Ann1 z + 8
}

infix fun Int.foo(other: Int) = 1
