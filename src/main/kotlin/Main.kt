import java.io.File

fun main() {
    val file = File("src/main/resources/input.txt")
    val numbers = arrayOf(Pair("one", 1), Pair("two", 2), Pair("three", 3), Pair("four", 4), Pair("five", 5), Pair
        ("six", 6),Pair("seven", 7), Pair("eight", 8), Pair("nine", 9))
    val res = file.readLines().sumOf { combineFirstAndLastDigit(it, numbers) }
    println("result is: $res")
}


fun combineFirstAndLastDigit(input: String, numbers: Array<Pair<String, Int>>): Int {
    val firstDigit = getFirstDigit(input, numbers)
    val lastDigit = getLastDigit(input, numbers)
    val asString = (firstDigit.toString() + lastDigit.toString())
    println("result of line $input with $firstDigit and $lastDigit is: $asString")
    return asString.toInt()
}

fun getFirstDigit(input: String, numbers: Array<Pair<String, Int>>): Int {
    val digit = input.first { it.isDigit() }
    val indexOfDigit = input.indexOfFirst { it.isDigit() }
    val indicesOfNumbers = numbers.map { input.indexOf(it.first) }
    val minimumIndex = indicesOfNumbers.filter { it < indexOfDigit && it != -1 }.minOrNull() ?: return digit
        .digitToInt()
    return numbers[indicesOfNumbers.mapIndexed() { index, i -> Pair(index, i) }
        .first() { it.second == minimumIndex }
        .first].second
}

fun getLastDigit(input: String, numbers: Array<Pair<String, Int>>): Int {
    val digit = input.last { it.isDigit() }
    val indexOfDigit = input.indexOfLast { it.isDigit() }
    val indicesOfNumbers = numbers.map { input.lastIndexOf (it.first) }
    val minimumIndex = indicesOfNumbers.filter { it > indexOfDigit && it != -1 }.maxOrNull() ?: return digit.digitToInt()
    return numbers[indicesOfNumbers.mapIndexed() { index, i -> Pair(index, i) }
        .first() { it.second == minimumIndex }
        .first].second
}
