fun main() {
    fun part1(input: List<String>): Int =
        input.map { line ->
            val eachLineNumbers = line.filter { it in '0'..'9' }
            line to "${eachLineNumbers.first()}${eachLineNumbers.last()}".toInt()
        }.sumOf { it.second }


    fun part2(input: List<String>): Int =
        part1(input.map { it.replaceFirstAndLastWrittenDigits() })


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()
    check(part1(input) == 54159)
    val testInputPart2 = readInput("Day02_test")
    check(part2(testInputPart2) == 281)
    part2(input).println()
}

private val writtenDigits = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
private fun String.replaceFirstAndLastWrittenDigits(list: List<String> = writtenDigits): String {
    var filteredString = this
    val firstNumber = filteredString.findAnyOf(list, ignoreCase = true)
    val lastNumber = filteredString.findLastAnyOf(list, ignoreCase = true)

    filteredString = firstNumber?.second.orEmpty().let { first ->
        filteredString.replaceFirst(first, first.fromWrittenDigit(), true)
    }

    filteredString = lastNumber?.second.orEmpty().let { last ->
        filteredString.reversed().replaceFirst(last.reversed(), last.fromWrittenDigit(), true).reversed()
    }

    return filteredString
}

private fun String.fromWrittenDigit(): String =
    when (this) {
        "one" -> "o1e"
        "two" -> "t2o"
        "three" -> "t3e"
        "four" -> "f4r"
        "five" -> "f5e"
        "six" -> "s6x"
        "seven" -> "s7n"
        "eight" -> "e8t"
        "nine" -> "n9e"
        else -> ""
    }