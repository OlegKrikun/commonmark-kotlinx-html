plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-test-common")
    api("org.jetbrains.kotlin:kotlin-test-annotations-common")
    api("junit:junit")

    implementation("org.jetbrains.kotlin:kotlin-test-junit")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm")
}
