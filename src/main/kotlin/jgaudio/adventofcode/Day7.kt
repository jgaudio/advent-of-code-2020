// (C) king.com Ltd 2020
package jgaudio.adventofcode

import java.nio.file.Files
import java.nio.file.Paths

val bags = mutableMapOf<String, ColoredBag>()

class ColoredBag private constructor(builder: Builder) {

    val color: String
    private val childrenBags: Map<String, Int>

    init {
        color = builder.color!!
        childrenBags = builder.childBags
    }

    class Builder {
        var color: String? = null
            private set
        var childBags = mutableMapOf<String, Int>()

        fun color(color: String) = apply { this.color = color.replace("bags", "").replace("bag", "").trim() }
        fun addChildBag(color: String, amount: Int) = apply { this.childBags[color] = amount }
        fun build(): ColoredBag {
            return ColoredBag(this)
        }
    }

    fun deepContains(color: String): Boolean {
        return if (childrenBags.containsKey(color)) true
        else {
            childrenBags.any { bags[it.key]!!.deepContains(color) }
        }
    }

    fun deepBagCount(): Int {
        return childrenBags.map { it.value + it.value * bags[it.key]!!.deepBagCount() }.sum()
    }
}

fun day7Part1(): Int {

    for (line in Files.readAllLines(Paths.get("src/main/resources/aoc7.txt"))) {
        val bagBuilder = ColoredBag.Builder()
        bagBuilder.color(line.substringBefore("contain"))
        val childrenCsv = line.substringAfter("contain").replace(".", "").split(",")
        if (childrenCsv.isNotEmpty() && childrenCsv[0].trim() != "no other bags") {
            childrenCsv.map { it.trim() }.forEach { childrenStr ->
                val amount = childrenStr.substringBefore(" ").toInt()
                val color = childrenStr.substringAfter(" ").replace("bags", "").replace("bag", "").trim()
                bagBuilder.addChildBag(color, amount)
            }
        }
        val bag = bagBuilder.build()
        bags[bag.color] = bag
    }

    return bags.filter { it.key != "shiny gold" && it.value.deepContains("shiny gold") }.count()
}

fun day7Part2(): Int {

    for (line in Files.readAllLines(Paths.get("src/main/resources/aoc7.txt"))) {
        val bagBuilder = ColoredBag.Builder()
        bagBuilder.color(line.substringBefore("contain"))
        val childrenCsv = line.substringAfter("contain").replace(".", "").split(",")
        if (childrenCsv.isNotEmpty() && childrenCsv[0].trim() != "no other bags") {
            childrenCsv.map { it.trim() }.forEach { childrenStr ->
                val amount = childrenStr.substringBefore(" ").toInt()
                val color = childrenStr.substringAfter(" ").replace("bags", "").replace("bag", "").trim()
                bagBuilder.addChildBag(color, amount)
            }
        }
        val bag = bagBuilder.build()
        bags[bag.color] = bag
    }

    return bags["shiny gold"]!!.deepBagCount()
}

fun main() {
    println("Total individual bags: ${day7Part2()}")
}