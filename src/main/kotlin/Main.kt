import java.io.File
import kotlin.math.pow

fun main() {
    val file = File("src/main/resources/input.txt")
    val cards = file.readLines()
        .map {
            it.split(":")[1].split("|")
                .map { it2 ->
                    it2.split(" ")
                        .filter { it3 -> it3.isNotEmpty() }
                        .map { it4 -> it4.toInt() }
                        .toIntArray()
                }
        }.map { it2 ->
            {
                val intersection = it2[0].intersect(it2[1].toList())
                (2.0).pow((intersection.size - 1).toDouble()).toInt()
            }
        }.sumOf { it() }
    println("cards: $cards")
}
