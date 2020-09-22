package console.commands

class EmptyCommand: CommandBase() {
    override val commandIdentifier: String = ""

    override fun isValid(arguments: List<String>): Boolean = true
    override fun run(arguments: List<String>){}
}