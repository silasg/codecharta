package de.maibornwolff.codecharta.tools.inspection

import de.maibornwolff.codecharta.model.Project
import de.maibornwolff.codecharta.serialization.ProjectDeserializer
import de.maibornwolff.codecharta.tools.interactiveparser.InteractiveParser
import de.maibornwolff.codecharta.tools.interactiveparser.ParserDialogInterface
import de.maibornwolff.codecharta.tools.interactiveparser.util.CodeChartaConstants
import de.maibornwolff.codecharta.util.InputHelper
import de.maibornwolff.codecharta.util.Logger
import picocli.CommandLine
import java.io.File
import java.io.InputStream
import java.io.PrintStream
import java.util.concurrent.Callable

@CommandLine.Command(
    name = InspectionTool.NAME,
    description = [InspectionTool.DESCRIPTION],
    footer = [CodeChartaConstants.General.GENERIC_FOOTER]
)
class InspectionTool(
    private val input: InputStream = System.`in`,
    private val output: PrintStream = System.out
) : Callable<Unit?>, InteractiveParser {
    @CommandLine.Option(names = ["-h", "--help"], usageHelp = true, description = ["displays this help and exits"])
    var help: Boolean = false

    @CommandLine.Parameters(arity = "0..1", paramLabel = "FILE", description = ["input project file"])
    private var source: File? = null

    @CommandLine.Option(
        names = ["-l", "--levels", "-d", "--depth"],
        description = ["prints first x levels of project hierarchy"]
    )
    private var level: Int = 1

    private lateinit var project: Project

    override val name = NAME
    override val description = DESCRIPTION

    companion object {
        const val NAME = "inspect"
        const val DESCRIPTION = "prints the structure of cc.json files"

        @JvmStatic
        fun mainWithInOut(input: InputStream, output: PrintStream, args: Array<String>) {
            CommandLine(InspectionTool(input, output)).execute(*args)
        }
    }

    override fun call(): Unit? {
        project = readProject() ?: return null

        ProjectStructurePrinter(project, output).printProjectStructure(level)

        return null
    }

    private fun readProject(): Project? {
        if (source == null) {
            return ProjectDeserializer.deserializeProject(input)
        }

        require(InputHelper.isInputValid(arrayOf(source!!), canInputContainFolders = false)) {
            "Input invalid file for InspectionTool, stopping execution..."
        }

        val input = source!!.inputStream()
        return try {
            ProjectDeserializer.deserializeProject(input)
        } catch (e: Exception) {
            val sourceName = source!!.name
            Logger.error {
                "$sourceName is not a valid project file and is therefore skipped."
            }
            null
        }
    }

    override fun getDialog(): ParserDialogInterface = ParserDialog

    override fun isApplicable(resourceToBeParsed: String): Boolean {
        return false
    }
}
