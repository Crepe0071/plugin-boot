plugins {
    java
}

group = "com.jkl"
version = "1.0-SNAPSHOT"

dependencies {
    // 플러그인 모듈은 코어 모듈의 Plugin 인터페이스에 의존
    implementation(project(":core"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<Test> {
    useJUnitPlatform()
    workingDir = rootProject.projectDir
}

tasks.withType<JavaExec> {
    workingDir = rootProject.projectDir
}

tasks.jar {
    archiveFileName.set("bye-plugin.jar")
    destinationDirectory.set(file("../plugins"))
    manifest {
        attributes(
            "Implementation-Title" to "Bye Plugin",
            "Implementation-Version" to version,
            "Implementation-Vendor" to project.group,
            "Main-Class" to "${project.group}.ByeWorldPlugin"
        )
    }
}
