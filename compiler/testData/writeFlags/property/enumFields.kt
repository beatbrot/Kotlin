// WITH_STDLIB
enum class Test {
    A
}

// TESTED_OBJECT_KIND: property
// TESTED_OBJECTS: Test, $VALUES
// FLAGS: ACC_PRIVATE, ACC_STATIC, ACC_FINAL, ACC_SYNTHETIC

// TESTED_OBJECT_KIND: property
// TESTED_OBJECTS: Test, A
// FLAGS: ACC_PUBLIC, ACC_STATIC, ACC_FINAL, ACC_ENUM
