package org.drahun

fun main() {
    val rows = ConsoleInputWrapper.readInt("Enter the number of rows:", validator = { it > 0 })
    val columns = ConsoleInputWrapper.readInt("Enter the number of columns:", validator = { it > 0 })
    val cinema = CinemaCore(rows, columns)
    println("Cinema created with $rows rows and $columns columns.")

    val ui = CinemaUi(cinema)
    ui.handleUserInput()
}


public final class CinemaCore(public val rows: Int, public val columns: Int) {
    
    private val seats: Array<BooleanArray> = Array(rows) { BooleanArray(columns) { false } }

    fun isOccupied(row: Int, column: Int): Boolean {
        return seats[row][column]
    }

    fun bookSeat(row: Int, column: Int) {
        seats[row][column] = true
    }

    fun getTicketPrice(row: Int): Int {
        val totalSeats = rows * columns

        return if (totalSeats <= 60) {
            10
        } else {
            if (row < rows / 2) 10 else 8
        }
    }
}

public final class CinemaUi(private val cinema: CinemaCore) {

    data class MenuItem(
        val title: String,
        val action: (CinemaCore) -> Unit
    )

    private val menuItems = listOf(
        MenuItem("Show the seats") { showTheSeats() },
        MenuItem("Buy a ticket") { buyTicket() },
        MenuItem("Statistics") { showStatistics() }
    )

    fun handleUserInput() {
        while (true) {
            println("\nMenu:")
            
            menuItems.forEachIndexed { index, item ->
                println("${index + 1}. ${item.title}")
            }
            println("0. Exit")

            val choice = ConsoleInputWrapper.readInt("Select an option:", validator = { it in 0..menuItems.size })
            if (choice == 0) {
                println("Exiting...")
                break
            }

            val selectedItem = menuItems[choice - 1]
            selectedItem.action(cinema)
        }
    }

    private fun showTheSeats() {
        println("\nCinema:")

        print("  ")
        for (col in 1..cinema.columns) {
            print("${col} ")
        }
        println()

        for (row in 0 until cinema.rows) {
            print("${row + 1} ")
            for (col in 0 until cinema.columns) {
                print(if (cinema.isOccupied(row, col)) "B " else "S ")
            }
            println()
        }
    }
    
    private fun buyTicket() {
        while (true) {
            val row = ConsoleInputWrapper.readInt("Enter a row number:", validator = { it in 1..cinema.rows }) - 1
            val column = ConsoleInputWrapper.readInt("Enter a seat number in that row:", validator = { it in 1..cinema.columns }) - 1

            if (cinema.isOccupied(row, column)) {
                println("That ticket has already been purchased!")
                continue
            }

            cinema.bookSeat(row, column)
            val price = cinema.getTicketPrice(row)
            println("Ticket price: $$price")
            break
        }
    }

    private fun showStatistics() {
       val totalSeats = cinema.rows * cinema.columns
        var purchasedTickets = 0
        var currentIncome = 0

        for (r in 0 until cinema.rows) {
            for (c in 0 until cinema.columns) {
                if (cinema.isOccupied(r, c)) {
                    purchasedTickets++
                    currentIncome += cinema.getTicketPrice(r)
                }
            }
        }

        val totalIncome = (0 until cinema.rows).sumOf { cinema.getTicketPrice(it) } * cinema.columns
        val percentage = if (totalSeats > 0) (purchasedTickets.toDouble() / totalSeats) * 100 else 0.0

        println("Number of purchased tickets: $purchasedTickets")
        println("Percentage: ${"%.2f".format(java.util.Locale.US, percentage)}%")
        println("Current income: $$currentIncome")
        println("Total income: $$totalIncome")
    }
}

internal final class ConsoleInputWrapper {

    companion object {

        fun readString(prompt: String? = null, validator: ((String) -> Boolean)? = null): String {
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

                if (validator?.invoke(input) == false) {
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