package com.aliyun.credential.api;

import javax.security.auth.login.CredentialException;

import com.aliyun.credential.config.Config;
import com.aliyun.credential.model.AccessKeyEntity;

public interface CredentialService {

	public void initService(Config config) throws CredentialException;

	public AccessKeyEntity getCredential() throws CredentialException;

	public boolean isCredentialExpired() throws CredentialException;
}
