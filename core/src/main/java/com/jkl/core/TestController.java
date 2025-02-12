package com.jkl.core;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

	private final PluginManager pluginManager;

	public TestController(PluginManager pluginManager) {
		this.pluginManager = pluginManager;
	}

	@GetMapping
	public ResponseEntity<String> test() {

		BeforeContext v = new BeforeContext();
		pluginManager.run(v);
		return ResponseEntity.ok(v.getTest());
	}

	@GetMapping("/v2")
	public ResponseEntity<Void> testV2() {

		try {
			pluginManager.loadPlugins();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().build();
	}

	@GetMapping("/v3")
	public ResponseEntity<Void> testV4(@RequestParam String pluginName) throws Exception {

		pluginManager.reloadPlugin(pluginName);

		return ResponseEntity.ok().build();
	}
}
