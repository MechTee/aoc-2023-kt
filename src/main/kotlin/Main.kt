import java.io.File
import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

fun main() {
    val file = File("src/main/resources/input.txt")
    val input = file.readLines().filter { it.isNotEmpty() }
    val seeds = input.first().substringAfter("seeds:").split(" ").filterNot { it.isEmpty() }.map { it.toLong() }
    val maps = getMaps(input, false)

    val seedRanges = seeds.chunked(2).map { it[0]..it[0] + it[1] }
    val inverseMaps = getMaps(input, true)

    val solvePart1 = solvePart1(seeds, maps)
    println("Part 1: $solvePart1")

    val coroutineCount = 24
    val location = AtomicLong(0L)

    val result = runBlocking {
        (0..<coroutineCount).map {
            async {
                var current: Long
                while (true) {
                    current = location.incrementAndGet()
                    if (seedRanges.any { range ->
                            val seed = searchSeed(current, inverseMaps)
                            range.contains(seed) && searchSeed(seed, maps) == current
                    }) break
                }
                current
            }
        }.awaitAll().min()
    }
    println("solvePart2: $result")
}

fun getMaps(input: List<String>, inverse: Boolean): List<Map<LongRange, LongRange>> {
    val indexOfSeedToSoil = input.indexOfFirst { it.contains("seed-to-soil") }
    val indexOfSoilToFert = input.indexOfFirst { it.contains("soil-to-fertilizer") }
    val indexOfFertToWater = input.indexOfFirst { it.contains("fertilizer-to-water") }
    val indexOfWaterToLight = input.indexOfFirst { it.contains("water-to-light") }
    val indexOfLightToTemp = input.indexOfFirst { it.contains("light-to-temperature") }
    val indexOfTempToHumid = input.indexOfFirst { it.contains("temperature-to-humidity") }
    val indexOfHumidToLoc = input.indexOfFirst { it.contains("humidity-to-location") }

    val seedToSoilMap = createRangeMap(indexOfSeedToSoil, indexOfSoilToFert, input, inverse)
    val soilToFertMap = createRangeMap(indexOfSoilToFert, indexOfFertToWater, input, inverse)
    val fertToWaterMap = createRangeMap(indexOfFertToWater, indexOfWaterToLight, input, inverse)
    val waterToLightMap = createRangeMap(indexOfWaterToLight, indexOfLightToTemp, input, inverse)
    val lightToTempMap = createRangeMap(indexOfLightToTemp, indexOfTempToHumid, input, inverse)
    val tempToHumidMap = createRangeMap(indexOfTempToHumid, indexOfHumidToLoc, input, inverse)
    val humidToLocMap = createRangeMap(indexOfHumidToLoc, input.size, input, inverse)

    val list = listOf(
        seedToSoilMap,
        soilToFertMap,
        fertToWaterMap,
        waterToLightMap,
        lightToTempMap,
        tempToHumidMap,
        humidToLocMap)
    return if (inverse) list.reversed() else list
}

fun solvePart1(seeds: List<Long>, maps: List<Map<LongRange, LongRange>>) = seeds.minOfOrNull {
    searchSeed(it, maps)
} ?: -1

fun searchSeed(seed: Long, maps: List<Map<LongRange, LongRange>>): Long =
    maps.fold(seed) { acc, map ->
        findInMapOfRanges(acc, map)
    }

fun findInMapOfRanges (seed: Long, maps: Map<LongRange, LongRange>): Long =
    maps.firstNotNullOfOrNull {(key, value) ->
            if (key.contains(seed)) value.first + (seed - key.first) else null
        } ?: seed


fun createRangeMap(index1: Int, index2: Int, input: List<String>, inverse: Boolean): Map<LongRange, LongRange> =
    input.subList(index1 + 1, index2)
        .map { it.split(" ")
            .map { it.toLong() } }
        .fold(HashMap()) { acc, list ->
            if (inverse) {
                acc[list[0]..list[0] + list[2]] = list[1]..list[1] + list[2]
            } else {
                acc[list[1]..list[1] + list[2]] = list[0]..list[0] + list[2]
            }
            acc
        }