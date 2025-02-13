package com.jkl.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * 플러그인 jar 파일을 로드하여 Plugin 인터페이스 구현체를 생성하는 간단한 플러그인 매니저.
 */
@Component
public class PluginManager {

	private static final String PLUGIN_DIR = "plugins";
	private static final Map<String, Plugin> PLUGIN_MAP = new HashMap<>();

	@PostConstruct
	public void init() throws Exception {
		loadPlugins();
	}

	/**
	 * 지정된 플러그인 jar 파일에서 플러그인을 로드.
	 *
	 * @throws Exception 로딩 실패 시 예외 발생
	 */
	public void loadPlugins() throws Exception {

		File pluginDir = new File(PLUGIN_DIR);

		if (!pluginDir.exists() || !pluginDir.isDirectory()) {
			throw new RuntimeException("❌ 플러그인 디렉토리를 찾을 수 없습니다: " + pluginDir.getAbsolutePath());
		}

		// 🔍 libs 폴더 내 모든 .jar 파일 찾기
		File[] jarFiles = pluginDir.listFiles((dir, name) -> name.endsWith(".jar"));
		if (jarFiles == null || jarFiles.length == 0) {
			throw new RuntimeException("❌ 플러그인 JAR 파일을 찾을 수 없습니다: " + pluginDir.getAbsolutePath());
		}

		for (File pluginJar : jarFiles) {
			loadPlugin(pluginJar);
		}
	}

	public void run(BeforeContext context) {
		PLUGIN_MAP.values().forEach(v -> v.run(context));
	}

	public void reloadPlugin(String pluginName) throws Exception {

		File pluginJar = new File(PLUGIN_DIR + FileSystems.getDefault().getSeparator() + pluginName);

		loadPlugin(pluginJar);
	}

	public void unloadPlugin(String pluginName) {

		PLUGIN_MAP.remove(pluginName);
	}

	public void loadPlugin(File pluginJar) throws Exception {
		// 1️⃣ JAR을 임시 파일로 복사 (메모리 내 파일 생성)
		Path tempJar = Files.createTempFile("plugin-", ".jar");
		Files.copy(pluginJar.toPath(), tempJar, StandardCopyOption.REPLACE_EXISTING);

		URL[] urls = {tempJar.toUri().toURL()};

		// ✅ JAR의 `META-INF/MANIFEST.MF` 파일 읽기
		try (URLClassLoader pluginClassLoader = new URLClassLoader(urls, this.getClass().getClassLoader()); JarFile jarFile = new JarFile(pluginJar)) {
			Manifest manifest = jarFile.getManifest();
			Attributes attributes = manifest.getMainAttributes();
			String pluginClassName = attributes.getValue("Main-Class");

			if (pluginClassName == null || pluginClassName.isEmpty()) {
				throw new RuntimeException("MANIFEST.MF에서 'Main-Class' 속성을 찾을 수 없습니다.");
			}

			System.out.println("🔍 플러그인 클래스 로드: " + pluginClassName);

			// ✅ JAR 내부의 해당 클래스를 로드하여 인스턴스화
			Class<?> pluginClass = pluginClassLoader.loadClass(pluginClassName);
			PLUGIN_MAP.put(pluginClassName, ((Plugin)pluginClass.getDeclaredConstructor().newInstance()));

		} finally {
			// 로딩이 끝났으면 임시 파일 삭제 시도
			try {
				Files.deleteIfExists(tempJar);
				System.out.println("✅ 임시 JAR 파일 삭제 완료: " + tempJar);
			} catch (IOException e) {
				System.err.println("❌ 임시 JAR 파일 삭제 실패: " + e.getMessage());
			}
		}
	}
}

