// (C) king.com Ltd 2020
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

fun day1Part1() : Int {
    val reader = Files.newBufferedReader(Paths.get("src/main/resources/aoc1.txt"))
    var line:String? = reader.readLine()
    val winners = HashSet<Int>()
    while (line != null) {
        val n = line.toInt()
        if (winners.contains(n)) {
            print("$n x ${2020 - n} = ")
            return n * (2020 - n)
        } else {
            winners.add(2020 - n)
        }
        line = reader.readLine()
    }
    return -1
}

fun day1Part2() {
    val reader = Files.newBufferedReader(Paths.get("src/main/resources/aoc1.txt"))
    var line:String? = reader.readLine()
    val picked = LinkedList<Int>()
    val winners = HashMap<Int, Int>()
    while (line != null) {
        val n = line.toInt()
        if (winners.containsKey(n)) {
            val addend = winners[n]
            val total = n * addend!!* (2020 - n - addend!!)
            println("$n x $addend x ${2020 - n - addend!!} = $total")
        } else {
            for (i in picked) {
                winners[2020 - n - i] = n
            }
            picked.add(n)
        }

        line = reader.readLine()
    }
}

fun main() {
    day1Part2()
}