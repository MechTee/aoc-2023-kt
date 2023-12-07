import java.io.File

fun main() {
    val file = File("src/main/resources/input.txt")
    val input = file.readLines()
    val result = input.map { it.split(" ").map {it.trim()}}
    val pairs = result.map { it[0]to it[1].toLong() }
    val solvePart1 = sortPairsPt1(pairs).mapIndexed { index, pair ->  pair.second * (index + 1) }.sum()
    println("solve Part 1: $solvePart1")
    val solvePart2 = sortPairsPt2(pairs).mapIndexed { index, pair ->  pair.second * (index + 1) }.sum()
    println("solve Part 2: $solvePart2")

}

fun getCardValuePt1(char: Char) = "23456789TJQKA".indexOf(char)

fun getCardValuePt2(char: Char) = "J23456789TQKA".indexOf(char)

fun sortPairsPt1(input: List<Pair<String, Long>>) =
    input.sortedWith (compareBy({getHandValuePt1(it.first)}, {getCardValuePt1(it.first[0])},
        {getCardValuePt1(it.first[1])}, {getCardValuePt1(it.first[2])}, {getCardValuePt1(it.first[3])},
        {getCardValuePt1(it.first[4])}) )

fun sortPairsPt2(input: List<Pair<String, Long>>) =
    input.sortedWith (compareBy({getHandValuePt2(it.first)}, {getCardValuePt2(it.first[0])},
        {getCardValuePt2(it.first[1])}, {getCardValuePt2(it.first[2])}, {getCardValuePt2(it.first[3])},
        {getCardValuePt2(it.first[4])}) )

fun getHandValuePt1(input: String): Int {
    val cardCounts = input.groupingBy { it }.eachCount()
    val max = cardCounts.values.max()
    return when {
        max == 5 -> 7
        max == 4 -> 6
        max == 3 && cardCounts.values.contains(2) -> 5
        max == 3 -> 4
        max == 2 && cardCounts.values.count { it == 2 } == 2 -> 3
        max == 2 -> 2
        max == 1 -> 1
        else -> error("Missing case in when")
    }
}

fun getHandValuePt2(input: String): Int {
    var cards = input
    if (cards.contains('J')) {
        cards = if (cards == "JJJJJ") {
            "AAAAA"
        } else {
            val cardCounts = cards.replace("J", "").groupingBy { it }.eachCount()
            val cardOrder = listOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A').mapIndexed { index, c -> c to index + 1 }.toMap()
            val replaceChar =
                cardCounts.entries.filter { it.value == cardCounts.values.max() }
                    .sortedByDescending { cardOrder[it.key] }.first().key
            cards.replace('J', replaceChar)
        }
    }
    return getHandValuePt1(cards)
}