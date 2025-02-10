plugins {
    // 프로젝트 레벨 플러그인 정의 (필요시 추가)
    id("java-library")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

allprojects {
    repositories {
        mavenCentral()
    }
}