import java.io.File
import kotlin.math.pow

fun main() {
    val file = File("src/main/resources/input.txt")
    val input = file.readLines()
    val solvePart1 = solvePart1(input)
    println("Part 1: $solvePart1")
    val solvePart2 = solvePart2(input)
    println("Part 2: $solvePart2")
}

fun solvePart1(input: List<String>) = input
    .map { it.substringAfter(": ").split(" | ")
        .map { it.split(" ").filterNot { it.isBlank() }.map{it.toInt() } }
    }.mapIndexed { i, l -> i to l.first().intersect(l.last().toSet()) }
    .sumOf { (_, l) -> 2.0.pow(l.size - 1).toInt() }

fun solvePart2(input: List<String>) = MutableList(input.size) { 1 }
    .apply {
        input
            .map { it.substringAfter(": ").split(" | ")
                .map { it.split(" ").filterNot { it.isBlank() } }
            }.forEachIndexed { i, card -> (1..card.first().intersect(card.last().toSet()).size)
                .filter { i + it < size }
                .forEach { this[i + it] += this[i] }
        }
    }.sum()