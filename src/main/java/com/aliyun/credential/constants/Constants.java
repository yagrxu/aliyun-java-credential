package com.aliyun.credential.constants;

public class Constants {

	public static long EXPIRATION_BUFFER = 30000; // 30 seconds

	public static String STS_CODE = "Success";

	public enum Scenario {
		AccessKey, ServiceSTS, AccessKeySTS;
	}
}
