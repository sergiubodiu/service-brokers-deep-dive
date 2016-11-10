package io.pivotal.services.s3.model;

import java.util.HashMap;
import java.util.Map;

/**
 * The list of acceptable keys for the <code>credential</code> map in a service instance binding request.
 */
public enum CredentialResource {

	USER_NAME("userName"),
	ACCESS_KEY_ID("accessKeyId"),
	SECRET_ACCESS_KEY("secretAccessKey");

	private final String value;

	CredentialResource(String value) {
		this.value = value;
	}

	public static Map<String, Object> toMap(Credential credential) {
        HashMap<String, Object> credentials = new HashMap<>();
        credentials.put(CredentialResource.USER_NAME.toString(), credential.getServiceId());
        credentials.put(CredentialResource.ACCESS_KEY_ID.toString(), credential.getAccessKeyId());
        credentials.put(CredentialResource.SECRET_ACCESS_KEY.toString(), credential.getSecretAccessKey());
        return credentials;
    }

	@Override
	public String toString() {
		return value;
	}
}