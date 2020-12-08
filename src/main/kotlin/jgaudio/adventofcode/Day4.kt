package jgaudio.adventofcode

import java.nio.file.Files
import java.nio.file.Paths
import java.util.regex.Pattern
import kotlin.text.substringBefore

fun day4Part1(): Int {

    val fields = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
    var valid = 0

    val present = mutableSetOf<String>()

    for (line in Files.readAllLines(Paths.get("src/main/resources/aoc4.txt"))) {

        if (line.isBlank()) {
            if (present.size == fields.size) {
                valid++
            }
            present.clear()
        } else {
            for (pair in line.split(" ")) {
                val key = pair.split(":")[0]

                if (fields.contains(key)) {
                    present.add(key)
                }
            }
        }
    }

    if (present.size == fields.size) {
        valid++
    }

    return valid
}

fun day4Part2(): Int {

    val eyeColors = setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")

    val fields = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
    var valid = 0

    val present = mutableSetOf<String>()

    for (line in Files.readAllLines(Paths.get("src/main/resources/aoc4.txt"))) {

        if (line.isBlank()) {
            if (present.size == fields.size) {
                valid++
            }
            present.clear()
        } else {
            for (pair in line.split(" ")) {
                val key = pair.split(":")[0]
                val value = pair.split(":")[1]

                when(key) {
                    "byr" -> {
                        val birthYear = value.toInt()
                        if (birthYear in 1920..2002) {
                            present.add(key)
                        }
                    }
                    "iyr" -> {
                        val issueYear = value.toInt()
                        if (issueYear in 2010..2020) {
                            present.add(key)
                        }
                    }
                    "eyr" -> {
                        val expirationYear = value.toInt()
                        if (expirationYear in 2020..2030) {
                            present.add(key)
                        }
                    }
                    "hgt" -> {
                        if (value.endsWith("cm")) {
                            val height = value.substringBefore("cm").toInt()
                            if (height in 150..193) {
                                present.add(key)
                            }
                        } else if (value.endsWith("in")) {
                            val height = value.substringBefore("in").toInt()
                            if (height in 59..76) {
                                present.add(key)
                            }
                        }
                    }
                    "hcl" -> if (value.matches(Regex("#[0-9a-f]+"))) present.add(key)
                    "ecl" -> if (eyeColors.contains(value)) present.add(key)
                    "pid" -> if (value.matches(Regex("[\\d]{9}"))) present.add(key)
                }
            }
        }
    }

    if (present.size == fields.size) {
        valid++
    }

    return valid
}

fun main() {
    println("Valid: ${day4Part2()}")
}

