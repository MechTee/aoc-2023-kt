import java.io.File

fun main() {
    val file = File("src/main/resources/input.txt")
    val res = file.readLines().sumOf { combineFirstAndLastDigit(it) }
    println("result is: $res")
}


fun combineFirstAndLastDigit(input: String): Int {
    val firstDigit = getFirstDigit(input)
    val lastDigit = getLastDigit(input)
    val asString = (firstDigit.toString() + lastDigit.toString())
    println("result of line $input is: $asString")
    return asString.toInt()
}

fun getFirstDigit(input: String): Int {
    var index = 0
    while (index < input.length) {
        if (input[index].isDigit()) {
            return input[index].toString().toInt()
        }
        index++
    }
    return -1
}

fun getLastDigit(input: String): Int {
    var index = input.length - 1
    while (index >= 0) {
        if (input[index].isDigit()) {
            return input[index].toString().toInt()
        }
        index--
    }
    return -1
}