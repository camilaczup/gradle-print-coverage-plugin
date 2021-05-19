package jacoco.printcoveragestatus

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.testing.jacoco.tasks.JacocoReport

class PrintCoverageStatusPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.tasks.withType(JacocoReport::class.java) {
            reports {
                xml.isEnabled = true
            }
        }
        val printStatus = target.tasks.register("jacocoPrintCoverageStatus", PrintCoverageStatusTask::class.java)
        val printStatusTask = printStatus.get()
        printStatusTask.dependsOn(target.tasks.withType(JacocoReport::class.java))
        target.tasks.getByName("check").dependsOn(printStatusTask)
    }
}
