package de.maibornwolff.codecharta.parser.svnlogparser.input.metrics

import de.maibornwolff.codecharta.parser.svnlogparser.input.Modification

class NumberOfRenames : Metric {
    private var numberOfRenames = 0

    override fun description(): String {
        return "Number of Renames: The number of times a file was renamed"
    }

    override fun metricName(): String {
        return "number_of_renames"
    }

    override fun registerModification(modification: Modification) {
        if (modification.type == Modification.Type.RENAME) {
            numberOfRenames++
        }
    }

    override fun value(): Number {
        return numberOfRenames
    }
}
