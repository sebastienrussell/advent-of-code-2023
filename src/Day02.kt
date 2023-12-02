fun main() {
    fun part1(input: List<String>, vararg cubes: Cube): Int =
        possibleGamesWith(input.map {
            parseLine(it)
        }, *cubes).sumOf { it.id }


    fun part2(input: List<String>): Int {
        return minimumSetOfCubesNeededPerGame(input.map {
            parseLine(it)
        }).sumOf { it.first.count * it.second.count * it.third.count }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(
        part1(
            testInput,
            Cube(Cube.Color.RED, 12),
            Cube(Cube.Color.GREEN, 13),
            Cube(Cube.Color.BLUE, 14)
        ) == 8
    )

    val input = readInput("Day02")
    part1(
        input,
        Cube(Cube.Color.RED, 12),
        Cube(Cube.Color.GREEN, 13),
        Cube(Cube.Color.BLUE, 14)
    ).println()

    check(
        part2(
            testInput,
        ) == 2286
    )


    part2(input).println()
}

data class Game(
    val id: Int,
    val setsOfCubePulled: List<List<Cube>>
)

data class Cube(
    val color: Color,
    val count: Int = 1
) {
    enum class Color { RED, GREEN, BLUE }
}

fun parseLine(line: String): Game {
    return Game(
        id = line.substringBefore(':').filter { it.isDigit() }.toInt(),
        setsOfCubePulled = line.substringAfter(":").replace(" ", "").split(";").map { set ->
            set.split(",").map { pull ->
                listOf(
                    Cube(
                        color = when (pull.filterNot { it.isDigit() }) {
                            "red" -> Cube.Color.RED
                            "blue" -> Cube.Color.BLUE
                            else -> Cube.Color.GREEN
                        },
                        count = pull.filter { it.isDigit() }.toIntOrNull() ?: 1
                    )
                )
            }.flatten()
        }
    )
}


fun possibleGamesWith(games: List<Game>, vararg cubes: Cube): List<Game> =
    games.filter { game ->
        game.setsOfCubePulled.all { setsOfCube ->
            setsOfCube.all { cube ->
                cube.count <= cubes.first { it.color == cube.color }.count
            }
        }
    }

fun minimumSetOfCubesNeededPerGame(games: List<Game>): List<Triple<Cube, Cube, Cube>> {
    return games.map { game ->
        var minRed = 0
        var minGreen = 0
        var minBlue = 0
        game.setsOfCubePulled.forEach { pull ->
            pull.forEach { cube ->
                when (cube.color) {
                    Cube.Color.RED -> if (cube.count > minRed)
                        minRed = cube.count

                    Cube.Color.BLUE -> if (cube.count > minBlue)
                        minBlue = cube.count

                    Cube.Color.GREEN -> if (cube.count > minGreen)
                        minGreen = cube.count
                }
            }
        }
        Triple(
            Cube(
                Cube.Color.RED,
                count = minRed
            ), Cube(
                Cube.Color.BLUE,
                count = minBlue
            ), Cube(
                Cube.Color.GREEN,
                count = minGreen
            )
        )
    }
}