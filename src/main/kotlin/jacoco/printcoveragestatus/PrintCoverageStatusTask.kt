package jacoco.printcoveragestatus

import groovy.util.XmlSlurper
import groovy.util.slurpersupport.NodeChild
import groovy.util.slurpersupport.NodeChildren
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * Based on de.jansauer.printcoverage.PrintCoverageTask: id("de.jansauer.printcoverage") version "2.0.0"
 */
open class PrintCoverageStatusTask : DefaultTask() {

    init {
        description = "Prints code coverage status"
        group = "verification"
    }

    @TaskAction
    fun printStatus() {
        val slurper = XmlSlurper()
        slurper.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
        slurper.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
        val jacocoTestReport = File("${project.buildDir}/reports/jacoco/test/jacocoTestReport.xml")
        if (jacocoTestReport.exists()) {
            val report = slurper.parse(jacocoTestReport)
            val counter = report.getProperty("counter") as NodeChildren
            counter.forEach {
                val node = it as NodeChild
                val type = CoverageType.valueOf(node.attributes()["type"] as String)
                val missed = (node.attributes()["missed"] as String).toDouble()
                val covered = (node.attributes()["covered"] as String).toDouble()
                type.print(100 / (missed + covered) * covered)
            }
        } else {
            logger.error("Jacoco test report is missing.")
        }
    }
}

enum class CoverageType {
    INSTRUCTION, BRANCH, LINE, COMPLEXITY, METHOD, CLASS;

    fun print(coverage: Double) {
        println(String.format("%s => %.2f%%", name.padEnd(12), coverage))
    }
}
