val phoneAndMailBook = mutableMapOf<String, Person>()
sealed interface Command {
    fun isValid(): Boolean
}

data class AddPhone(val name: String, val phone: String) : Command {
    override fun isValid() = phone.matches(Regex("""^\+?\d+${'$'}"""))
}


data class AddEmail(val name: String, val email: String) : Command {
    override fun isValid() = email.matches(Regex("""^[A-Za-z\d](.*)(@)(.+)(\.)([A-Za-z]{2,})"""))
}

object Exit : Command {
    override fun isValid() = true
}

object Help : Command {
    override fun isValid() = true
}


// Класс Person
data class Person(
    var name: String,
    var phones: MutableList<String> = mutableListOf(),
    var emails: MutableList<String> = mutableListOf()
)


fun readCommand(input: String): Command {
    val parts = input.split(" ")
    // Распознавание команды
    return when (parts[0]) {
        "add" -> {
            if (parts.size == 4) {
                when (parts[2]) {
                    "phone" -> AddPhone(parts[1], parts[3])
                    "email" -> AddEmail(parts[1], parts[3])
                    else -> Help
                }
            } else {
                return Help
            }
        }

        "exit" -> Exit
        "help" -> Help
        
        else -> {
            println("Неизвестная команда")
            Help
        }
    }
}


fun main(){
    print("""
    Список команд:
    exit - выход
    help - справка
    add <Имя> phone <Номер телефона> - Добавить имя человека и номер телефона
    add <Имя> email <Адрес электронной почты> - Добавить имя человека и mail
    """)
   
        print("Введите команду: ")
        val command = readCommand(readLine()!!.lowercase())
        if (command.isValid()) {
            when (command) {
              
                is AddPhone -> phoneAndMailBook.getOrPut(command.name) { Person(command.name) }.also {
                    it.phones.add(command.phone)
                    println("Добавлено: Имя: ${it.name}, телефон: ${command.phone}")
                }

                is AddEmail -> phoneAndMailBook.getOrPut(command.name) { Person(command.name) }.also {
                    it.emails.add(command.email)
                    println("Добавлено: Имя: ${it.name}, email: ${command.email}")
                }

               
                is Help -> {
                    println("""Список команд:
                            exit - выход
                            help - справка
                            add <Имя> phone <Номер телефона> - Добавить имя человека и номер телефона
                            add <Имя> email <Адрес электронной почты> - Добавить имя человека и mail
                            """)
                }
                is Exit -> return
            }
        }
        else -> println("Неизвестная команда")
            
        }
        
    