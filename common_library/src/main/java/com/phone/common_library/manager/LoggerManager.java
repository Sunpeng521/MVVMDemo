package com.phone.common_library.manager;

import com.orhanobut.logger.Logger;

/**
 * 如果用于android平台，将信息记录到“LogCat”。如果用于java平台，将信息记录到“Console”
 * 使用logger封装
 */
public class LoggerManager {
	
	public static boolean DEBUG_ENABLE = false;// 是否调试模式
	
	public static void logd(String tag, String message) {
		if (DEBUG_ENABLE) {
			Logger.d(tag, message);
		}
	}
	
	public static void logd(String message) {
		if (DEBUG_ENABLE) {
			Logger.d(message);
		}
	}
	
	public static void loge(Throwable throwable, String message, Object... args) {
		if (DEBUG_ENABLE) {
			Logger.e(throwable, message, args);
		}
	}
	
	public static void loge(String message, Object... args) {
		if (DEBUG_ENABLE) {
			Logger.e(message, args);
		}
	}
	
	public static void logi(String message, Object... args) {
		if (DEBUG_ENABLE) {
			Logger.i(message, args);
		}
	}
	
	public static void logv(String message, Object... args) {
		if (DEBUG_ENABLE) {
			Logger.v(message, args);
		}
	}
	
	public static void logw(String message, Object... args) {
		if (DEBUG_ENABLE) {
			Logger.v(message, args);
		}
	}
	
	public static void logwtf(String message, Object... args) {
		if (DEBUG_ENABLE) {
			Logger.wtf(message, args);
		}
	}
	
	public static void logjson(String message) {
		if (DEBUG_ENABLE) {
			Logger.json(message);
		}
	}
	
	public static void logxml(String message) {
		if (DEBUG_ENABLE) {
			Logger.xml(message);
		}
	}
}
