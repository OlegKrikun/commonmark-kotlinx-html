plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    api("junit:junit:4.13.1")
    api("org.jetbrains.kotlin:kotlin-test-annotations-common:1.4.10")
    api("org.jetbrains.kotlin:kotlin-test-common:1.4.10")

    implementation("org.jetbrains.kotlin:kotlin-test-junit:1.4.10")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
}
