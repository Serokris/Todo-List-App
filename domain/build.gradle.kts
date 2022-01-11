plugins {
    id("java-library")
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.test {
    useJUnit()
}

val jUnitVersion: String by rootProject.extra
val mockitoVersion = "4.0.0"

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.2")
    implementation("javax.inject:javax.inject:1")

    // Testing
    testImplementation("junit:junit:$jUnitVersion")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoVersion")
}