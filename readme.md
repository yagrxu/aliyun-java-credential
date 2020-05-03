# Aliyun Java Credential

[![Build Status](https://travis-ci.com/yagrxu/aliyun-java-credential.svg?branch=master)](https://travis-ci.com/yagrxu/aliyun-java-credential)

This jar is to make aliyun credential easier in Java code.

## Support Scenarios

1. Provide Access Key ID and Access Key Secret to access

    ``` java
    CredentialService service = new CredentialServiceImpl();
    Config  config = new Config(new AccessKeyEntity("testId", "testSecret"));
        try {
            service.initService(config);
            return service.getCredential(); // return AccessKeyEntity which  implements from com.aliyuncs.auth.AlibabaCloudCredentials
        } catch (CredentialException e) {
            System.out.println(e.getMessage());
        }
    ```

2. Provide service trusted STS which assume role from ECS meta data information or Kube2Ram

    ``` java
    CredentialService service = new CredentialServiceImpl();
    Config  config = new Config("demo"); // assume role using URL http://100.100.100.200/latest/meta-data/ram/security-credentials/demo
        try {
            service.initService(config);
            AccessKeyEntity ak = service.getCredential(); // return AccessKeyEntity which  implements from com.aliyuncs.auth.AlibabaCloudCredentials
            System.out.println("valid:" + (ak.getExpiration() - System.currentTimeMillis()) / 60000); // 3 hours
        } catch (CredentialException e) {
            System.out.println(e.getMessage());
        }
    ```

3. Provide STS assume role from AccessKey

    ``` java
    CredentialService service = new CredentialServiceImpl();
    Config  cconfig = new Config(true, "acs:ram::{account}:role/demo", new AccessKeyEntity("{access key id}", "{access key secret}"), "eu-central-1");
        try {
            service.initService(config);
            AccessKeyEntity ak = service.getCredential(); // return AccessKeyEntity which  implements from com.aliyuncs.auth.AlibabaCloudCredentials
            System.out.println("valid:" + (ak.getExpiration() - System.currentTimeMillis()) / 60000); // 1 hour
        } catch (CredentialException e) {
            System.out.println(e.getMessage());
        }
    ```
