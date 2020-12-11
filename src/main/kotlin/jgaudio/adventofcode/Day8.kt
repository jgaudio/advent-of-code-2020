package jgaudio.adventofcode

import java.nio.file.Files
import java.nio.file.Paths

class Program(lines: List<String>) {

    private val instructions = lines.map { Instr(it) }

    fun runUntilInfiniteLoop(): Context {
        return runUntilInfiniteLoop(Context())
    }

    private fun runUntilInfiniteLoop(ctx: Context): Context {
        while (!ctx.wasNextInstructionVisited()) {
            executeNextInstruction(ctx)
        }
        return ctx
    }

    private fun executeNextInstruction(ctx: Context) {
        execute(instructions[ctx.pc], ctx)
    }

    private fun execute(instruction: Instr, ctx: Context) {
        ctx.visited.add(ctx.pc)
        when (instruction.opcode) {
            "acc" -> {
                ctx.accumulator += instruction.argument
                ctx.pc++
            }
            "jmp" -> ctx.pc += instruction.argument
            "nop" -> ctx.pc++
        }
    }

    fun fixInfiniteLoop(): Int {
        return fixInfiniteLoop(Context())
    }

    private tailrec fun fixInfiniteLoop(ctx: Context): Int {
        when {
            ctx.wasNextInstructionVisited() -> {
                return -1
            }
            ctx.pc == instructions.size -> {
                return ctx.accumulator
            }
            else -> {
                val nextInstruction = instructions[ctx.pc]
                if (!ctx.fixed && (nextInstruction.opcode == "jmp" || nextInstruction.opcode == "nop")) {
                    val fixedInstruction = Instr(if (nextInstruction.opcode == "jmp") "nop" else "jmp", nextInstruction.argument)
                    val copyContext = Context(ctx)
                    execute(fixedInstruction, copyContext)
                    val result = fixInfiniteLoop(copyContext)
                    if (result > 0) {
                        return result
                    }
                }

                execute(nextInstruction, ctx)
                return fixInfiniteLoop(ctx)
            }
        }
    }


    class Context() {
        constructor(ctx: Context) : this() {
            pc = ctx.pc
            accumulator = ctx.accumulator
            visited = ctx.visited.toMutableSet()
            fixed = true
        }

        var pc = 0
        var accumulator = 0
        var visited = mutableSetOf<Int>()
        var fixed = false

        fun wasNextInstructionVisited(): Boolean {
            return visited.contains(pc)
        }
    }

    class Instr(val opcode: String, val argument: Int) {
        constructor(line: String) : this(line.split(" ")[0].trim(),
                line.split(" ")[1].trim().replace("+", "").toInt()) {
        }
    }
}

fun day8Part1(): Int {
    return Program(input()).runUntilInfiniteLoop().accumulator
}

fun day8Part2(): Int {
    return Program(input()).fixInfiniteLoop()
}

private fun input() = Files.readAllLines(Paths.get("src/main/resources/aoc8.txt"))

fun main() {
    println("Infinite loop accum: ${day8Part1()}")
    println("Fixed accum: ${day8Part2()}")
}