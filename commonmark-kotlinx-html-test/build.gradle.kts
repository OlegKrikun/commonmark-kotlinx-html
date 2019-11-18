plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    api("junit:junit:4.12")
    api("org.jetbrains.kotlin:kotlin-test-annotations-common:1.3.60")
    api("org.jetbrains.kotlin:kotlin-test-common:1.3.60")

    implementation("org.jetbrains.kotlin:kotlin-test-junit:1.3.60")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.6.12")
}
