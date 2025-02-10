plugins {
    `java-library`
}

group = "com.jkl"
version = "1.0-SNAPSHOT"

dependencies {
    // 플러그인 모듈은 코어 모듈의 Plugin 인터페이스에 의존
    implementation(project(":core"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}