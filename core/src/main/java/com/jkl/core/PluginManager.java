package com.jkl.core;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 플러그인 jar 파일을 로드하여 Plugin 인터페이스 구현체를 생성하는 간단한 플러그인 매니저.
 */
public class PluginManager {

    /**
     * 지정된 플러그인 jar 파일에서 플러그인을 로드.
     *
     * @param pluginJar 플러그인 jar 파일
     * @return Plugin 인터페이스 구현체 인스턴스
     * @throws Exception 로딩 실패 시 예외 발생
     */
    public Plugin loadPlugin(File pluginJar) throws Exception {
        URL[] urls = { pluginJar.toURI().toURL() };
        // 플러그인 전용 ClassLoader 생성 (코어의 ClassLoader를 부모로 지정)
        URLClassLoader pluginClassLoader = new URLClassLoader(urls, this.getClass().getClassLoader());

        // 플러그인 jar 내부에 정의된 클래스 이름을 하드코딩 (실제 환경에서는 메타데이터 기반으로 결정)
        Class<?> pluginClass = Class.forName("com.jkl.HelloWorldPlugin", true, pluginClassLoader);
        return (Plugin) pluginClass.getDeclaredConstructor().newInstance();
    }
}

