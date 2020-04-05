package com.aliyun.credential.api.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import javax.security.auth.login.CredentialException;

import org.apache.commons.lang3.RandomStringUtils;

import com.aliyun.credential.config.Config;
import com.aliyun.credential.model.AccessKeyEntity;
import com.aliyun.credential.model.StsObject;
import com.aliyuncs.auth.AlibabaCloudCredentials;
import com.aliyuncs.auth.BasicSessionCredentials;
import com.aliyuncs.auth.STSAssumeRoleSessionCredentialsProvider;
import com.aliyuncs.exceptions.ClientException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class AssumeRoleService {

	protected static AccessKeyEntity assumeServiceSts(Config config) throws CredentialException {
		AccessKeyEntity stsCredential = config.getTempAccessKey();
		if (stsCredential != null && !stsCredential.willSoonExpire()) {
			return stsCredential;
		} else {
			return assumeEcsRole(config.getRamRoleName());
		}

	}

	protected static AccessKeyEntity assumRoleByAccessKey(Config config) throws CredentialException {
		AlibabaCloudCredentials credential = config.getLonglivedAccessKey();
		long currentTime = System.currentTimeMillis();
		STSAssumeRoleSessionCredentialsProvider stsProvider = new STSAssumeRoleSessionCredentialsProvider(
				credential.getAccessKeyId(), credential.getAccessKeySecret(), RandomStringUtils.randomAlphabetic(8),
				config.getRamRoleName(), config.getRegionId());
		try {
			BasicSessionCredentials stsCredential = (BasicSessionCredentials) stsProvider.getCredentials();
			return new AccessKeyEntity(stsCredential.getAccessKeyId(), stsCredential.getAccessKeySecret(),
					stsCredential.getSessionToken(),
					currentTime + STSAssumeRoleSessionCredentialsProvider.DEFAULT_DURATION_SECONDS * 1000);
		} catch (ClientException e) {
			throw new CredentialException("failed to get STS credential: " + e.getErrMsg());
		}
	}

	private static AccessKeyEntity assumeEcsRole(String roleName) throws CredentialException {
		URL url = null;
		StsObject temp = null;
		try {
			url = new URL("http://100.100.100.200/latest/meta-data/ram/security-credentials/" + roleName);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			int status = conn.getResponseCode();
			String responseString = null;
			switch (status) {
			case 200:
				responseString = getSuccessResponse(conn);
				JsonElement credential = JsonParser.parseString(responseString);
				Gson gson = new Gson();
				temp = gson.fromJson(credential, StsObject.class);
				break;
			case 404:
				throw new CredentialException(new StringBuilder().append("Request STS failed: RAM role ")
						.append(roleName).append(" not found").toString());
			default:
				responseString = getErrorResponse(conn);
				throw new CredentialException("Request STS failed: " + responseString);
			}

		} catch (IOException e) {
			throw new CredentialException(e.getMessage());
		}
		System.out.println(temp.getExpiration());
		return new AccessKeyEntity(temp.getAccessKeyId(), temp.getAccessKeySecret(), temp.getSecurityToken(),
				convertStr2Time(temp.getExpiration()));

	}

	private static long convertStr2Time(String formatedTime) {
		Calendar now = javax.xml.bind.DatatypeConverter.parseDateTime(formatedTime);
		return now.getTimeInMillis();
	}

	private static String getSuccessResponse(HttpURLConnection conn) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		return content.toString();
	}

	private static String getErrorResponse(HttpURLConnection conn) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		return content.toString();
	}

}
