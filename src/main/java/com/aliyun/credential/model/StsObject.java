package com.aliyun.credential.model;

import com.aliyuncs.auth.AlibabaCloudCredentials;

import lombok.Getter;

@Getter
public class StsObject implements AlibabaCloudCredentials {

	private String AccessKeyId;

	private String AccessKeySecret;

	private String Expiration;

	private String SecurityToken;

	private String LastUpdated;

	private String Code;
}
