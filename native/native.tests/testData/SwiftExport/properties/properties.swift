import Properties

func testConstants() throws {
    try assertEquals(actual: BOOLEAN_CONST, expected: true)
    try assertEquals(actual: BYTE_CONST, expected: 1)
    try assertEquals(actual: SHORT_CONST, expected: 2)
    try assertEquals(actual: INT_CONST, expected: 3)
    try assertEquals(actual: LONG_CONST, expected: 4)
    try assertEquals(actual: FLOAT_CONST, expected: 5.0)
    try assertEquals(actual: DOUBLE_CONST, expected: 6.0)
    try assertEquals(actual: UBYTE_CONST, expected: 1)
    try assertEquals(actual: USHORT_CONST, expected: 2)
    try assertEquals(actual: UINT_CONST, expected: 3)
    try assertEquals(actual: ULONG_CONST, expected: 4)
}

class PropertiesTests : TestProvider {
    var tests: [TestCase] = []

    init() {
        providers.append(self)
        tests = [
            TestCase(name: "initProducesNewObject", method: withAutorelease(testConstants))
        ]
    }
}