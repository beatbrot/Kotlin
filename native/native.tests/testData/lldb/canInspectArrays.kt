// KIND: STANDALONE_LLDB
// FIR_IDENTICAL
// INPUT_DATA_FILE: canInspectArrays.in
// OUTPUT_DATA_FILE: canInspectArrays.out
fun main(args: Array<String>) {
    val xs = IntArray(3)
    xs[0] = 1
    xs[1] = 2
    xs[2] = 3
    val ys: Array<Any?> = arrayOfNulls(2)
    ys[0] = Point(1, 2)
    return
}

data class Point(val x: Int, val y: Int)
