package org.drahun

public class ChatBot {
    private val name: String = "DICT_Bot"

    private val testSystem = TestSystem()

    private fun calculateAge(r3: Int, r5: Int, r7: Int): Int {
        return (r3 * 70 + r5 * 21 + r7 * 15) % 105
    }

    public fun countTo(number: Int) {
        for (i in 0..number) {
            println("$i!")
        }
    }

    public fun step1() {
        println("Hello, my name is $name")
    }

    public fun step2() {
        val name = ConsoleInputWrapper.readString("Please, remind me your name .")
        println("What a great name you have , $name !")
    }

    public fun step3() {
        println("Let me guess your age.")
        println("Enter remainders of dividing your age by 3, 5 and 7.")

        val r3 = ConsoleInputWrapper.readInt() { it in 0..2 }
        val r5 = ConsoleInputWrapper.readInt() { it in 0..4 }
        val r7 = ConsoleInputWrapper.readInt() { it in 0..6 }

        val age = calculateAge(r3, r5, r7)

        println("Your age is $age; that's a good time to start programming!")
    }

    public fun step4() {
        val number = ConsoleInputWrapper.readInt("Now I will prove to you that I can count to any number you want.") { it >= 0 }

        countTo(number)
    }
    
    public fun step5() {
        println("Lets test your programming knowledge.")
        testSystem.runTests()
        println("Congratulations , have a nice day !")
    }
}

public class TestSystem() {
    private val questions = listOf(
        Question("Где находится ЦП?", 
        listOf(
            Variant("В компьютере", true),
            Variant("В файлах Эпштейна", true),
        )),
        Question("Что есть в компьютере любого С++ девелопера?", 
        listOf(
            Variant("Папка с фотографиями Ленина", false),
            Variant("Notepad++", false),
            Variant("Telegram", false),
            Variant("Rust", true),
        )),
        Question("Что находилось на моём столе во время написания этих вопросов?",
        listOf(
            Variant("Чашка с кофе", false),
            Variant("Кейс от наушников", false),
            Variant("Икона", true),
            Variant("Компьютерная мышь", false),
        )),
    )

    private fun askQuestion(question: Question) {
        println(question.text)
        val collection = question.variants.shuffled()
        collection.forEachIndexed { index, variant ->
            println("${index + 1}. ${variant.text}")
        }

        while (true) {
            val userChoice = ConsoleInputWrapper.readInt() { it in 1..question.variants.size }

            if (collection[userChoice - 1].isTrue) {
                println("Correct")
                break
            }
            println("Please, try again")
        }
    }

    public fun runTests() {
        questions.shuffled().forEach {askQuestion(it)}
    }
}

internal class ConsoleInputWrapper {

    companion object {

        fun readString(prompt: String? = null): String {
            if (prompt != null) {
                println(prompt)
            }
            while (true) {
                print("> ")
                val input = readlnOrNull()?.trim()

                if (input.isNullOrBlank()) {
                    println("Invalid input")
                    continue
                }

                return input
            }
        }

        fun readInt(prompt: String? = null, validator: ((Int) -> Boolean)? = null): Int {
            while (true) {
                val input = readString(prompt)
                val number = input.toIntOrNull()

                if (number == null) {
                    println("Invalid number")
                    continue
                }

                if (validator?.invoke(number) == false) {
                    println("Invalid number")
                    continue
                }

                return number
            }
        }
    }
}

public class Question(val text: String, val variants: List<Variant>)
public class Variant(val text: String, val isTrue: Boolean)

fun main() {
    val chatBot = ChatBot()
    chatBot.step1()
    chatBot.step2()
    chatBot.step3()
    chatBot.step4()
    chatBot.step5()
}