package com.aliyun.credential.api.impl;

import javax.security.auth.login.CredentialException;

import com.aliyun.credential.api.CredentialService;
import com.aliyun.credential.config.Config;
import com.aliyun.credential.constants.Constants.Scenario;
import com.aliyun.credential.model.AccessKeyEntity;

public class CredentialServiceImpl implements CredentialService {

	private static Config serviceConfig = null;

	@Override
	public AccessKeyEntity getCredential() throws CredentialException {
		checkConfig(serviceConfig);
		switch (categorizeScenario(serviceConfig)) {
		case ServiceSTS:
			return AssumeRoleService.assumeServiceSts(serviceConfig);
		case AccessKey:
			return serviceConfig.getLonglivedAccessKey();
		case AccessKeySTS:
			return AssumeRoleService.assumRoleByAccessKey(serviceConfig);
		default:
			throw new CredentialException("not supported scenario");
		}
	}

	@Override
	public boolean isCredentialExpired() throws CredentialException {
		checkConfig(serviceConfig);
		switch (categorizeScenario(serviceConfig)) {
		case ServiceSTS:
		case AccessKeySTS:
			AccessKeyEntity tempCredential = serviceConfig.getTempAccessKey();
			return tempCredential == null ? true : tempCredential.willSoonExpire();
		case AccessKey:
			return false;
		default:
			throw new CredentialException("not supported scenario");
		}
	}

	@Override
	public void initService(Config config) throws CredentialException {
		checkConfig(config);
		serviceConfig = config;
	}

	private void checkConfig(Config config) throws CredentialException {
		if (config == null) {
			throw new CredentialException("Empty Configuration parameter is not accepted");
		}
		if (!config.isStsEnabled()) {
			validateAccessKey(config.getLonglivedAccessKey());
		}
		if (config.isStsEnabled()) {
			validateSts(config);
		}
	}

	private void validateSts(Config config) throws CredentialException {
		if (config.getRamRoleName() == null) {
			throw new CredentialException("If use STS, ram role name is required");
		}
	}

	private void validateAccessKey(AccessKeyEntity accessKey) throws CredentialException {
		if (accessKey == null || accessKey.isEmpty()) {
			throw new CredentialException(
					"If do not use STS assume role, AccessKeyId and accessKeySecret are required");
		}
	}

	private Scenario categorizeScenario(Config config) {
		if (config.isStsEnabled()) {
			AccessKeyEntity accessKey = config.getLonglivedAccessKey();
			if (accessKey == null || accessKey.isEmpty()) {
				return Scenario.ServiceSTS;
			} else {
				return Scenario.AccessKeySTS;
			}

		} else {
			return Scenario.AccessKey;
		}

	}
}