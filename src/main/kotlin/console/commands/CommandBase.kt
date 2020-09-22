package console.commands

import console.Console
import java.util.*

abstract class CommandBase {
    abstract val commandIdentifier: String

    var status: Console.ReturnStatus = Console.ReturnStatus.CONTINUE
    var statusMessage: String = ""

    abstract fun isValid(arguments: List<String> = Collections.emptyList()): Boolean
    abstract fun run(arguments: List<String> = Collections.emptyList())
}