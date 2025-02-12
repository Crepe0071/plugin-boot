package com.jkl.core;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.springframework.stereotype.Component;

/**
 * 플러그인 jar 파일을 로드하여 Plugin 인터페이스 구현체를 생성하는 간단한 플러그인 매니저.
 */
@Component
public class PluginManager {

    /**
     * 지정된 플러그인 jar 파일에서 플러그인을 로드.
     *
     * @param pluginJar 플러그인 jar 파일
     * @return Plugin 인터페이스 구현체 인스턴스
     * @throws Exception 로딩 실패 시 예외 발생
     */
    public Plugin loadPlugin(File pluginJar) throws Exception {
        URL[] urls = {pluginJar.toURI().toURL()};
               URLClassLoader pluginClassLoader = new URLClassLoader(urls, this.getClass().getClassLoader());

        // ✅ JAR의 `META-INF/MANIFEST.MF` 파일 읽기
        try (JarFile jarFile = new JarFile(pluginJar)) {
            Manifest manifest = jarFile.getManifest();
            Attributes attributes = manifest.getMainAttributes();
            String pluginClassName = attributes.getValue("Main-Class");

            if (pluginClassName == null || pluginClassName.isEmpty()) {
                throw new RuntimeException("MANIFEST.MF에서 'Main-Class' 속성을 찾을 수 없습니다.");
            }

            System.out.println("🔍 플러그인 클래스 로드: " + pluginClassName);

            // ✅ JAR 내부의 해당 클래스를 로드하여 인스턴스화
            Class<?> pluginClass = Class.forName(pluginClassName, true, pluginClassLoader);
            return (Plugin)pluginClass.getDeclaredConstructor().newInstance();
        }
    }
}

