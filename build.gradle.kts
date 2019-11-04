plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.50" apply false
}

subprojects {
    repositories { jcenter() }

    afterEvaluate {
        dependencies.constraints {
            add("implementation", "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.50")
            add("implementation", "org.jetbrains.kotlinx:kotlinx-html-jvm:0.6.12")
            add("implementation", "com.atlassian.commonmark:commonmark:0.13.0")
        }
    }
}

version = "0.0.0"
