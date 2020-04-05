package com.aliyun.credential.main;

import javax.security.auth.login.CredentialException;

import com.aliyun.credential.api.CredentialService;
import com.aliyun.credential.api.impl.CredentialServiceImpl;
import com.aliyun.credential.config.Config;
import com.aliyun.credential.model.AccessKeyEntity;

public class Demo {

	public static void main(String[] args) {
		CredentialService service = new CredentialServiceImpl();
		Config config = new Config("demo");
		try {
			service.initService(config);
			AccessKeyEntity ak = service.getCredential();
			System.out.println("valid:" + (ak.getExpiration() - System.currentTimeMillis()) / 60000); // 3 hours
		} catch (CredentialException e) {
			e.printStackTrace();
		}
		config = new Config("demo1");
		try {
			service.initService(config);
			service.getCredential();
		} catch (CredentialException e) {
			System.out.println(e.getMessage());
		}

		config = new Config(new AccessKeyEntity("testId", "testSecret"));
		try {
			service.initService(config);
			service.getCredential();
		} catch (CredentialException e) {
			System.out.println(e.getMessage());
		}

		config = new Config(true, "acs:ram::{account}:role/demo",
				new AccessKeyEntity("{access key id}", "{access key secret}"), "eu-central-1");
		try {
			service.initService(config);
			AccessKeyEntity ak = service.getCredential();
			System.out.println("valid:" + (ak.getExpiration() - System.currentTimeMillis()) / 60000); // 1 hour
		} catch (CredentialException e) {
			System.out.println(e.getMessage());
		}
	}
}
