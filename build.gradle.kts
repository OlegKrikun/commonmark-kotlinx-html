import com.jfrog.bintray.gradle.BintrayExtension
import java.util.Properties

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.10" apply false
    id("com.jfrog.bintray") version "1.8.5" apply false
}

group = "ru.krikun.commonmark"
version = "0.1.1"

val properties = properties("bintray.properties")
val bintrayUser: String = properties.getProperty("user")
val bintrayKey: String = properties.getProperty("key")
val bintrayDryRun = properties.getProperty("dryRun") == "true"

subprojects
    .onEach { it.repositories { jcenter() } }
    .filter { it.name != "commonmark-kotlinx-html-test" }
    .forEachAfterEvaluate { subproject ->
        subproject.group = group
        subproject.version = version

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
                setLicenses("Apache-2.0")
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

tasks.wrapper { distributionType = Wrapper.DistributionType.ALL }
