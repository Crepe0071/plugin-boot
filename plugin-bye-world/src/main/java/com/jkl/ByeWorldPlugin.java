package com.jkl;

import com.jkl.core.BeforeContext;
import com.jkl.core.Plugin;

/**
 * Plugin 인터페이스를 구현한 예제 플러그인.
 * 실행 시 "Hello, World!"를 출력함.
 */
public class ByeWorldPlugin implements Plugin {

	@Override
	public void run(BeforeContext context) {
		context.setTest("bye");
		System.out.println(context);
	}
}