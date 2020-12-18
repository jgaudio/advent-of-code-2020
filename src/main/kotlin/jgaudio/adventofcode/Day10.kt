package jgaudio.adventofcode

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.IllegalStateException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.xml.bind.JAXBElement
import kotlin.collections.HashMap
import kotlin.math.abs


private fun input() = Files.readAllLines(Paths.get("src/main/resources/aoc10.txt")).map { it.toInt() }.toHashSet()

/////////////////////////////////////////////////////////////////////  //
/// PART 1
///////////////////////////////////////////////////////////////////////
private data class Context(val chain: List<Int>,
                           var adaptersLeft: Set<Int>,
                           val deviceJolts: Int,
                           val diff1Count: Int,
                           val diff3Count: Int,
                           val complete: Boolean) {

    fun plug(pluggedAdapter: Int, ctx: Context): Context {
        val diff = abs(pluggedAdapter - if (ctx.chain.isNotEmpty()) ctx.chain.last() else 0)
        val adaptersLeft = ctx.adaptersLeft.minus(pluggedAdapter)
        return Context(ctx.chain.plus(pluggedAdapter),
                adaptersLeft,
                deviceJolts,
                ctx.diff1Count + if (diff == 1) 1 else 0,
                ctx.diff3Count + if (diff == 3) 1 else 0,
                adaptersLeft.isEmpty() && abs(pluggedAdapter - ctx.deviceJolts) <= 3)
    }

}

private tailrec fun makeChain(pluggedAdapter: Int, ctx: Context): Context {

    if (pluggedAdapter <= 0) {
        return ctx
    }

    val afterPlugging = ctx.plug(pluggedAdapter, ctx)

    if (afterPlugging.adaptersLeft.isEmpty()) {
        return Context(afterPlugging.chain,
                emptySet(),
                afterPlugging.deviceJolts,
                afterPlugging.diff1Count + if (abs(afterPlugging.chain.last() - afterPlugging.deviceJolts) == 1) 1 else 0,
                afterPlugging.diff3Count + if (abs(afterPlugging.chain.last() - afterPlugging.deviceJolts) == 3) 1 else 0,
                afterPlugging.complete)
    }

    for (diff in 1..3) {
        val candidateAdapter = pluggedAdapter + diff
        if (afterPlugging.adaptersLeft.contains(candidateAdapter)) {
            val result = makeChain(candidateAdapter, afterPlugging)
            if (result.complete) {
                return result
            }
        }
    }

    return makeChain(0, ctx)
}

fun day10Part1(): Int {

    val adapters = input()
    val deviceJolts = adapters.sorted()[adapters.size - 1] + 3

    for (adapter in adapters) {
        if (adapter <= 3) {
            val result = makeChain(adapter, Context(emptyList(), adapters, deviceJolts, 0, 0, false))
            if (result.complete) {
                return result.diff1Count * result.diff3Count
            }
        }
    }
    throw IllegalStateException("Unable to find solution")
}

///////////////////////////////////////////////////////////////////////
/// PART 2
///////////////////////////////////////////////////////////////////////

private val adapterPairs = HashMap<Int, MutableList<Int>>()

private var cache = mutableMapOf<Int, Long>()

private fun countChains(adapter: Int, pairs: Map<Int, List<Int>>, deviceJolts: Int): Long {
    if (adapter == deviceJolts) {
        return 1
    }
    var localAccum = 0L
    for (adapterPair in pairs[adapter]!!) {
        if (cache.containsKey(adapterPair)) {
            localAccum += cache[adapterPair]!!
        } else {
            val localResult = countChains(adapterPair, pairs, deviceJolts)
            cache[adapterPair] = localResult
            localAccum += localResult
        }
    }
    return localAccum
}

fun day10Part2(): Long {

    val adapters = input()
    val deviceJolts = adapters.sorted()[adapters.size - 1] + 3
    adapters.add(deviceJolts)
    println(deviceJolts)

    for (i in 1..3) {
        if (adapters.contains(i)) {
            adapterPairs.putIfAbsent(0, mutableListOf())
            adapterPairs[0]!!.add(i)
        }
    }

    for (adapter in adapters) {
        for (diff in 1..3) {
            val targetAdapter = adapter + diff
            if (adapters.contains(targetAdapter)) {
                adapterPairs.putIfAbsent(adapter, mutableListOf())
                adapterPairs[adapter]!!.add(targetAdapter)
            }
        }
    }
    println(adapterPairs)

    return countChains(0, adapterPairs, deviceJolts)

    //  return countChains(0, emptySet(), adapterPairs, deviceJolts, 0)
}

fun main() {
    //println("Total differences: ${day10Part1()}")
    println("Total arrangements: ${day10Part2()}")
}