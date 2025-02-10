package com.jkl.core;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CoreApplication {

    public static void main(String[] args) throws Exception {
        // Spring Boot 애플리케이션 부트스트랩
        var context = SpringApplication.run(CoreApplication.class, args);

        // 예제: 플러그인 jar 파일 경로 (실행 시, 프로젝트 루트의 plugins 폴더에 plugin-hello.jar 파일을 두세요)
        File pluginJar = new File("plugin-hello-world/build/libs/plugin-hello-world-1.0-SNAPSHOT.jar");
        if (!pluginJar.exists()) {
            System.err.println("플러그인 jar 파일이 존재하지 않습니다: " + pluginJar.getAbsolutePath());
            return;
        }

        // 플러그인 로드 및 실행
        PluginManager pluginManager = new PluginManager();
        Plugin plugin = pluginManager.loadPlugin(pluginJar);
        System.out.println("플러그인 실행 결과:");
        plugin.run();
    }
}
