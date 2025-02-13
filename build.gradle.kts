plugins {
    // 프로젝트 레벨 플러그인 정의 (필요시 추가)
    id("java")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

// 루트 빌드 스크립트: 공통 설정
allprojects {
    repositories {
        mavenCentral()
    }
}
