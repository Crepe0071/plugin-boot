package com.jkl.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

	private final PluginManager pluginManager;

	private static final String PLUGIN_DIR = "plugins";

	private List<String> availablePlugins = new ArrayList<>(){{add("hello-plugin.jar");}};

	public TestController(PluginManager pluginManager) {
		this.pluginManager = pluginManager;
	}

	@GetMapping
	public ResponseEntity<String> test() throws Exception {

		File pluginDir = new File(PLUGIN_DIR);
		if (!pluginDir.exists() || !pluginDir.isDirectory()) {
			throw new RuntimeException("❌ 플러그인 디렉토리를 찾을 수 없습니다: " + pluginDir.getAbsolutePath());
		}

		// 🔍 libs 폴더 내 모든 .jar 파일 찾기
		File[] jarFiles = pluginDir.listFiles((dir, name) -> name.endsWith(".jar"));
		if (jarFiles == null || jarFiles.length == 0) {
			throw new RuntimeException("❌ 플러그인 JAR 파일을 찾을 수 없습니다: " + pluginDir.getAbsolutePath());
		}

		BeforeContext v = new BeforeContext();
		for (File pluginJar : jarFiles) {
			if (availablePlugins.contains(pluginJar.getName())) {
				// 플러그인 로드 및 실행
				PluginManager pluginManager = new PluginManager();
				Plugin plugin = pluginManager.loadPlugin(pluginJar);
				System.out.println("플러그인 실행 결과:");

				plugin.run(v);
			}
		}

		return ResponseEntity.ok(v.getTest());
	}

	@GetMapping("/v2")
	public ResponseEntity<Void> testV2() {

		availablePlugins.add("bye-plugin.jar");

		return ResponseEntity.ok().build();
	}

	@GetMapping("/v3")
	public ResponseEntity<Void> testV3() {

		availablePlugins.remove("hello-plugin.jar");

		return ResponseEntity.ok().build();
	}
}
