package com.jkl;

import com.jkl.core.Plugin;

/**
 * Plugin 인터페이스를 구현한 예제 플러그인.
 * 실행 시 "Hello, World!"를 출력함.
 */
public class HelloWorldPlugin implements Plugin {

	@Override
	public void run() {
		System.out.println("Hello, World!");
	}
}