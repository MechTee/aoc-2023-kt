import java.io.File

fun main() {
    val file = File("src/main/resources/input.txt")
    val lines = file.readLines()
    var sum = 0L
    val gearLocs = mutableListOf<Pair<Pair<Int, Int>, Long>>()
    for (i in lines.indices) {
        val digitIndices = mutableListOf<Int>()
        for (j in lines[i].indices) {
            if (lines[i][j].isDigit()) {
                digitIndices.add(j)
            } else {
                if (digitIndices.isNotEmpty()) {
                    // number ended here
                    val number = getViableDigitNumber(lines, digitIndices, i)
                    sum += number.first
                    gearLocs.addAll(number.second.map { Pair(it, number.first) })
                    digitIndices.clear()
                }
            }
            if (digitIndices.isNotEmpty() && j == lines[i].length - 1) {
                val number = getViableDigitNumber(lines, digitIndices, i)
                sum += number.first
                gearLocs.addAll(number.second.map { Pair(it, number.first) })
                digitIndices.clear()
            }
        }
        digitIndices.clear()
    }
    val res2 = gearLocs.groupBy { it.first }.filter { it.value.size > 1 }.values.sumOf {
        it.fold(1L) { acc, pair ->
            val res = acc * pair.second
            res
        }
    }
    println("solvePart1 $sum")
    println("solvePart2 $res2")
}

fun getViableDigitNumber(lines: List<String>, digitIndices: List<Int>, i: Int): Pair<Long, List<Pair<Int, Int>>> {
    val number = lines[i].substring(digitIndices[0], digitIndices.last + 1).toLong()
    // check if number is valid/partial number
    var upwards = ""
    var downwards = ""
    if (i > 0) {
        upwards = getNeighBoringString(lines, digitIndices, i - 1)
    }
    if (i < lines.size - 1) {
        downwards = getNeighBoringString(lines, digitIndices, i + 1)
    }
    val leftRight = when {
        digitIndices[0] - 1 >= 0 && digitIndices.last + 2 <= lines[i].length ->
            lines[i][digitIndices[0] - 1].toString() + lines[i][digitIndices.last + 1].toString()
        digitIndices[0] - 1 < 0 -> lines[i][digitIndices[0]].toString() + lines[i][digitIndices.last + 1].toString()
        digitIndices.last + 2 > lines[i].length ->
            lines[i][digitIndices[0] - 1].toString() + lines[i][digitIndices.last].toString()
        else -> lines[i][digitIndices[0]].toString() + lines[i][digitIndices.last].toString()
    }
    return createGearLocs(upwards, downwards, leftRight, digitIndices, i, number)
}

fun createGearLocs(upwards: String, downwards: String, leftRight: String, digitIndices: List<Int>, i: Int, number: Long):
        Pair<Long, List<Pair<Int, Int>>> {
    return if ((upwards + downwards + leftRight).any() { !it.isDigit() && it !='.'}) {
        val listOfGearLoc = mutableListOf<Pair<Int, Int>>()
        val upLoc = getGearLocFromNeighboringString(upwards, digitIndices, i - 1)
        if (upLoc != null) {
            listOfGearLoc.add(upLoc)
        }
        val downLoc = getGearLocFromNeighboringString(downwards, digitIndices, i + 1)
        if (downLoc != null) {
            listOfGearLoc.add(downLoc)
        }
        if (leftRight.indexOf('*') != -1) {
            val idx = leftRight.indexOf('*')
            if (idx == 0) {
                listOfGearLoc.add(Pair(i, digitIndices[0] - 1))
            } else {
                listOfGearLoc.add(Pair(i, digitIndices.last + 1))
            }
        }
        Pair(number, listOfGearLoc)
    } else {
        Pair(0, listOf())
    }
}

fun getGearLocFromNeighboringString(neighboringString: String, digitIndices: List<Int>, lineIndex:Int): Pair<Int, Int>? {
    val idx = neighboringString.indexOf('*')
    return if (idx != -1) {
        if (digitIndices[0] - 1 >= 0) {
            Pair(lineIndex, digitIndices[0] - 1 + idx)
        } else {
            Pair(lineIndex, digitIndices[0] + idx)
        }
    } else null
}

fun getNeighBoringString(lines: List<String>, digitIndices: List<Int>, lineIndex: Int): String {
    return when {
        digitIndices[0] - 1 >= 0 && digitIndices.last + 2 <= lines[lineIndex].length ->
            lines[lineIndex].substring(digitIndices[0] - 1, digitIndices.last + 2)

        digitIndices[0] - 1 < 0 -> lines[lineIndex].substring(digitIndices[0], digitIndices.last + 2)
        digitIndices.last + 2 > lines[lineIndex].length ->
            lines[lineIndex].substring(digitIndices[0] - 1, digitIndices.last + 1)

        else -> lines[lineIndex].substring(digitIndices[0], digitIndices.last + 1)
    }
}