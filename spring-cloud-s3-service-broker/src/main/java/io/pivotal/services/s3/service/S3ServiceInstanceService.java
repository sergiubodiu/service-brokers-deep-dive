package io.pivotal.services.s3.service;

import io.pivotal.services.s3.model.Credential;
import io.pivotal.services.s3.model.S3ServiceInstance;
import io.pivotal.services.s3.model.S3ServiceInstanceRepository;
import io.pivotal.services.s3.model.S3User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceExistsException;
import org.springframework.cloud.servicebroker.model.*;
import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.stereotype.Service;

@Service
public class S3ServiceInstanceService implements ServiceInstanceService {

    private Logger log = LoggerFactory.getLogger(S3ServiceInstanceService.class);
    private S3ServiceInstanceRepository repository;
    private S3Service s3Service;

    @Override
    public CreateServiceInstanceResponse createServiceInstance(CreateServiceInstanceRequest request) {
        S3ServiceInstance serviceInstance = new S3ServiceInstance(request);

        if (repository.exists(serviceInstance.getServiceInstanceId()))
            throw new ServiceInstanceExistsException(request.getServiceInstanceId(), request.getServiceDefinitionId());

        try {
            S3User user = s3Service.createBucket(
                    serviceInstance.getServiceInstanceId());

            serviceInstance.setCredential(
                    new Credential(user.getCreateUserResult().getUser().getUserName(),
                            user.getAccessKeyId(), user.getAccessKeySecret()));

            serviceInstance = repository.save(serviceInstance);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            throw new ServiceBrokerException(e);
        }
        return new CreateServiceInstanceResponse();
    }

    @Override
    public GetLastServiceOperationResponse getLastOperation(GetLastServiceOperationRequest request) {
        return new GetLastServiceOperationResponse().withOperationState(OperationState.SUCCEEDED);
    }

    @Override
    public DeleteServiceInstanceResponse deleteServiceInstance(DeleteServiceInstanceRequest request) {
        if (!repository.exists(request.getServiceInstanceId())) {
            throw new ServiceInstanceDoesNotExistException(request.getServiceInstanceId());
        }
        S3ServiceInstance serviceInstance = repository.findOne(request.getServiceInstanceId());
        // Delete service broker
        if (!s3Service.deleteServiceInstanceBucket(serviceInstance.getServiceInstanceId(),
                serviceInstance.getCredential().getAccessKeyId(),
                serviceInstance.getCredential().getUserName())) {
            log.error("Could not delete the S3 bucket for the service instance");
        }

        try {
            repository.delete(request.getServiceInstanceId());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            throw new ServiceBrokerException(e);
        }

        return new DeleteServiceInstanceResponse();
    }

    @Override
    public UpdateServiceInstanceResponse updateServiceInstance(UpdateServiceInstanceRequest request) {

        if (!repository.exists(request.getServiceInstanceId())) {
            throw new ServiceInstanceDoesNotExistException(request.getServiceInstanceId());
        }
        S3ServiceInstance serviceInstance = repository.getOne(request.getServiceInstanceId());
        serviceInstance.setPlanId(request.getPlanId());
        serviceInstance = repository.save(serviceInstance);

        return new UpdateServiceInstanceResponse();
    }

}