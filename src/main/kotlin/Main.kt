import java.io.File
import java.math.BigInteger


operator fun BigInteger.rangeTo(other: BigInteger) =
    BigIntegerRange(this, other)

class BigIntegerRange(
    override val start: BigInteger,
    override val endInclusive: BigInteger
) : ClosedRange<BigInteger>, Iterable<BigInteger> {
    override operator fun iterator(): Iterator<BigInteger> =
        BigIntegerRangeIterator(this)
}

class BigIntegerRangeIterator(
    private val range: ClosedRange<BigInteger>
) : Iterator<BigInteger> {
    private var current = range.start

    override fun hasNext(): Boolean =
        current <= range.endInclusive

    override fun next(): BigInteger {
        if (!hasNext()) {
            throw NoSuchElementException()
        }
        return current++
    }
}

fun main() {
    val file = File("src/main/resources/input.txt")
    val input = file.readLines()
    val times = input.first().substringAfter("Time:").split(" ").filterNot { it.isBlank() }.map { it.toInt() }
    val distances = input.last().substringAfter("Distance:").split(" ").filterNot { it.isBlank() }.map { it.toInt() }
    val races = times zip distances
    val solvePart1 = solvePart1(races)
    println("Part 1: $solvePart1")

    val singleRaceTime = times.map { it.toString() }.fold("") { acc, s -> "$acc$s" }.trim().toBigInteger()
    val singleRaceDistance = distances.map { it.toString() }.fold("") { acc, s -> "$acc$s" }.trim().toBigInteger()

    val solvePart2 = solvePart2(singleRaceTime, singleRaceDistance)
    println("Part 2: $solvePart2")
}

fun solvePart1(input: List<Pair<Int, Int>>): Int = input.map { pair ->
    (1..pair.first).map { (pair.first - it) * it }.count{ it >= pair.second}}
    .fold(1) { acc, i -> acc * i }


fun solvePart2(time: BigInteger, distance: BigInteger): Int =
    (BigInteger.ONE..time).map { (time - it) * it }.count { it >= distance }