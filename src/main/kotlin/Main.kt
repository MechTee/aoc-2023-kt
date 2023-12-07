import java.io.File

fun main() {
    val allowed = Triple<Int, Int, Int>(12, 13, 14)
    val file = File("src/main/resources/input.txt")
    val games = file.readLines().fold(HashMap<Int, List<Triple<Int,Int,Int>>>()) { acc, str ->
        acc[str.split(": ")[0].substringAfter(" ").trim().toInt()] =
            str.substringAfter(": ").split(";").map {
               mapInputsToTriple(it)
            }
        acc
    }
    val result = games.filter { isGamePossible(it.value, allowed) }.keys.sum()
    println("solvePart1: $result")
    val result2 = games.map { game ->
        val maxR = game.value.maxOf { it.first }
        val maxG = game.value.maxOf { it.second }
        val maxB = game.value.maxOf { it.third }
        maxR * maxG * maxB
    }.sum()

    println("solvePart2: $result2")
}

fun mapInputsToTriple(input: String): Triple<Int, Int, Int> {
    val x = input.trim().split(",")
    var r = 0
    var g = 0
    var b = 0
    for (v in x) {
        if (v.contains("red")) {
            r = v.trim().split(" ")[0].trim().toInt()
        }
        if (v.contains("green")) {
            g = v.trim().split(" ")[0].trim().toInt()
        }
        if (v.contains("blue")) {
            b = v.trim().split(" ")[0].trim().toInt()
        }
    }
    return Triple(r, g, b)
}

fun isGamePossible(set: List<Triple<Int, Int, Int>>, allowed: Triple<Int, Int, Int>) =
    set.all { it.first <= allowed.first && it.second <= allowed.second && it.third <= allowed.third }