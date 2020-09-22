package console

import console.commands.CommandBase
import console.commands.EmptyCommand

class Console {

    companion object {
        var availableCommands: HashMap<String, CommandBase> = HashMap()

        fun addCommand(cmd: CommandBase) = availableCommands.putIfAbsent(cmd.commandIdentifier, cmd)
    }

    init {
        addCommand(EmptyCommand())
    }

    private fun getInput(prompt: String = ">"): String {
        print("$prompt ")
        return readLine()!!
    }

    fun launch() {
        var lastStatus: ReturnStatus = ReturnStatus.CONTINUE
        while (lastStatus != ReturnStatus.QUIT){
            val input: List<String> = getInput().split(" ")
            val arguments: List<String> = input.drop(1)
            val command: CommandBase = availableCommands.getOrDefault(input[0], EmptyCommand())

            if (command.isValid(arguments)){
                command.run(arguments)
                lastStatus = command.status
            }
            if (lastStatus == ReturnStatus.ERROR){
                print(command.statusMessage)
            }
        }

    }

    enum class ReturnStatus {
        QUIT, CONTINUE, ERROR
    }

}