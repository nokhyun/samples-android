import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

abstract class DependencyOnlyPlugin : Plugin<Project> {
    abstract fun addLibraryNames(): List<String>

    override fun apply(target: Project) {
        with(target) {
            val libs = target.extensions.getByType<VersionCatalogsExtension>().named("libs")

            dependencies {
                with("implementation") {
                    addLibraryNames().forEach { alias ->
                        add(this@with, libs.findLibrary(alias).get())
                    }
                }
            }
        }
    }
}