package com.aliyun.credential.model;

import com.aliyun.credential.constants.Constants;
import com.aliyuncs.auth.AlibabaCloudCredentials;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * If plan to us AccessKeyId and AccessKeySecret to generate ascClient, use this
 * class to fill the data
 *
 */

@Setter
@AllArgsConstructor
public class AccessKeyEntity implements AlibabaCloudCredentials {

	public AccessKeyEntity(String accessKeyId, String accessKeySecret) {
		this(accessKeyId, accessKeySecret, null, -1L);
	}

	private String accessKeyId;

	private String accessKeySecret;

	@Getter
	private String sessionToken = null;

	@Getter
	private long expiration = -1L;

	public boolean isEmpty() {
		return accessKeyId == null || accessKeySecret == null;
	}

	public boolean willSoonExpire() {
		if (expiration <= 0) {
			return false;
		}
		long now = System.currentTimeMillis();

		return (expiration - now) < Constants.EXPIRATION_BUFFER;
	}

	@Override
	public String getAccessKeyId() {
		return this.accessKeyId;
	}

	@Override
	public String getAccessKeySecret() {
		return this.accessKeySecret;
	}
}
