package jgaudio.adventofcode

import java.nio.file.Files.readAllLines
import java.nio.file.Paths
import java.util.regex.Pattern

fun day2Part1() : Int {

    val pattern = Pattern.compile("(\\d+)-(\\d+)\\s(\\w):\\s(\\w+)")
    var valid = 0

    for (line in readAllLines(Paths.get("src/main/resources/aoc2.txt"))) {

        val matcher = pattern.matcher(line)

        if (matcher.matches()) {
            val min = matcher.group(1).toInt()
            val max = matcher.group(2).toInt()
            val letter = matcher.group(3)[0]
            val sequence = matcher.group(4)

            var count = 0
            for (c in sequence) {
                if (c == letter) count++
            }
            if (count in min..max) valid++
        }
    }

    return valid
}

fun day2Part2() : Int{

    val pattern = Pattern.compile("(\\d+)-(\\d+)\\s(\\w):\\s(\\w+)")
    var valid = 0

    for (line in readAllLines(Paths.get("src/main/resources/aoc2.txt"))) {

        val matcher = pattern.matcher(line)

        if (matcher.matches()) {
            val first = matcher.group(1).toInt()
            val second = matcher.group(2).toInt()
            val letter = matcher.group(3)[0]
            val sequence = matcher.group(4)

            val firstMatch = sequence[first - 1] == letter
            val secondMatch = sequence[second - 1] == letter
            if (firstMatch xor secondMatch) valid++
        }
    }

    return valid
}

fun main() {
    print("Valid: ${day2Part1()}")
}