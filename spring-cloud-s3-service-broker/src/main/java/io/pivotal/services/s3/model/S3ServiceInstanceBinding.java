package io.pivotal.services.s3.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class S3ServiceInstanceBinding {

    @Id
    private String id;
    private String serviceInstanceId;
    private String appGuid;

    public S3ServiceInstanceBinding() {
        id = UUID.randomUUID().toString();
    }

    public S3ServiceInstanceBinding(String id,
                                    String serviceInstanceId,
                                    String appGuid) {
        this.id = id;
        this.serviceInstanceId = serviceInstanceId;
        this.appGuid = appGuid;
    }

    public String getId() {
        return id;
    }

    public String getServiceInstanceId() {
        return serviceInstanceId;
    }

    public String getAppGuid() {
        return appGuid;
    }

}