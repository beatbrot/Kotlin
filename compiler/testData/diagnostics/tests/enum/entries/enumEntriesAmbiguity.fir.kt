// !LANGUAGE: +EnumEntries
// WITH_STDLIB
// FIR_DUMP

enum class Ambiguous {
    first, entries;
}

val e = Ambiguous.entries
