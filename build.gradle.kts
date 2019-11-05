import com.jfrog.bintray.gradle.BintrayExtension
import java.util.Properties

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.50" apply false
    id("com.jfrog.bintray") version "1.8.4" apply false
}

val properties = properties("bintray.properties")
val bintrayUser: String = properties.getProperty("user")
val bintrayKey: String = properties.getProperty("key")
val bintrayDryRun = properties.getProperty("dryRun") == "true"

subprojects
    .onEach { it.repositories { jcenter() } }
    .filter { it.name != "kotlinx-html-test" }
    .forEachAfterEvaluate { subproject ->
        subproject.version = "0.0.0"
        subproject.group = "ru.krikun.commonmark"

        subproject.apply(plugin = "com.jfrog.bintray")
        subproject.apply(plugin = "org.gradle.maven-publish")

        val sourcesJar = subproject.task<Jar>("sourcesJar") {
            archiveClassifier.set("sources")
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            from(subproject.sourceSets["main"].allSource)
        }

        subproject.configure<PublishingExtension> {
            publications.create<MavenPublication>("maven") {
                from(subproject.components["java"])
                artifact(sourcesJar) { classifier = "sources" }
            }
        }

        subproject.configure<BintrayExtension> {
            user = bintrayUser
            key = bintrayKey
            dryRun = bintrayDryRun

            pkg.apply {
                repo = "maven"
                name = subproject.name
                setLicenses("Apache2")
                websiteUrl = "https://github.com/OlegKrikun/commonmark-kotlinx-html"
                issueTrackerUrl = "https://github.com/OlegKrikun/commonmark-kotlinx-html/issues"
                vcsUrl = "https://github.com/OlegKrikun/commonmark-kotlinx-html.git"

                setPublications("maven")
            }
        }
    }

val Project.sourceSets get() = the<JavaPluginConvention>().sourceSets

fun Iterable<Project>.forEachAfterEvaluate(action: (Project) -> Unit) = forEach { it.afterEvaluate(action) }

fun properties(path: String) = Properties().apply { file(path).inputStream().use { load(it) } }
