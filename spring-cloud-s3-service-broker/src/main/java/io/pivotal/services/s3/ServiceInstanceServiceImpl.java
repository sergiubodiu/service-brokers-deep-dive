package io.pivotal.services.s3;

import org.springframework.cloud.servicebroker.model.*;
import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.stereotype.Service;

@Service
public class ServiceInstanceServiceImpl implements ServiceInstanceService {

    @Override
    public CreateServiceInstanceResponse createServiceInstance(CreateServiceInstanceRequest request) {
        return null;
    }

    @Override
    public GetLastServiceOperationResponse getLastOperation(GetLastServiceOperationRequest request) {
        return null;
    }

    @Override
    public DeleteServiceInstanceResponse deleteServiceInstance(DeleteServiceInstanceRequest request) {
        return null;
    }

    @Override
    public UpdateServiceInstanceResponse updateServiceInstance(UpdateServiceInstanceRequest request) {
        return null;
    }
}