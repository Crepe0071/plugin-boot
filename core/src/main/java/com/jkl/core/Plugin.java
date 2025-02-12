package com.jkl.core;

/**
 * 플러그인이 구현해야 하는 인터페이스.
 */
public interface Plugin {
	void run(BeforeContext context);
}

