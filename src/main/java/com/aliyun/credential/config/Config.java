package com.aliyun.credential.config;

import com.aliyun.credential.model.AccessKeyEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * This is the main configuration class for getting the request client
 * 
 * 1. If STS is enabled, no AccessKeyId and AccessKeySecret is required.
 * 
 * 2.
 * 
 * 
 */

@Getter
@Setter
@AllArgsConstructor

public class Config {

	public Config(AccessKeyEntity accessKeyEntity) {
		this(false, null, accessKeyEntity, null, null);
	}

	public Config(String ramRoleName) {
		this(true, ramRoleName, null, null, null);
	}

	public Config(boolean stsEnabled, String ramRoleName) {
		this(stsEnabled, ramRoleName, null, null, null);
	}

	public Config(boolean stsEnabled, String ramRoleName, AccessKeyEntity accessKeyEntity, String regionId) {
		this(stsEnabled, ramRoleName, accessKeyEntity, regionId, null);
	}

	/**
	 * If stsEnabled is false, akEntity must be provided.
	 * 
	 * Otherwise it will try to assume role from ECS / Kube2RAM
	 * 
	 */

	private boolean stsEnabled = true;

	/**
	 * If stsEnabled is false, akEntity must be provided.
	 * 
	 * Otherwise it will try to assume role from ECS / Kube2RAM
	 * 
	 */

	private String ramRoleName = null;

	/**
	 * Optional, required when stsEnabled is false or using access key assume role
	 * 
	 */

	private AccessKeyEntity longlivedAccessKey = null;

	/**
	 * Optional, only required when both stsEnabled is true and long lived access
	 * key is used.
	 * 
	 */

	private String regionId = null;

	/**
	 * Optional, required when stsEnabled is false or using access key assume role
	 * 
	 */

	private AccessKeyEntity tempAccessKey = null;

}
