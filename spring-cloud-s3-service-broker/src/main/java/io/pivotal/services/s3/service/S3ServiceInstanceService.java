package io.pivotal.services.s3.service;

import io.pivotal.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.exception.*;
import org.springframework.cloud.servicebroker.model.*;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class S3ServiceInstanceService implements ServiceInstanceService, ServiceInstanceBindingService {

    private Logger log = LoggerFactory.getLogger(S3ServiceInstanceService.class);
    private final S3ServiceInstanceRepository repository;
    private final S3ServiceInstanceBindingRepository bindingRepository;
    private final S3Service s3Service;

    @Autowired
    public S3ServiceInstanceService(S3ServiceInstanceRepository repository, S3ServiceInstanceBindingRepository bindingRepository, S3Service s3Service) {
        this.repository = repository;
        this.bindingRepository = bindingRepository;
        this.s3Service = s3Service;
    }

    @Override
    public CreateServiceInstanceResponse createServiceInstance(CreateServiceInstanceRequest request) {
        S3ServiceInstance serviceInstance = new S3ServiceInstance(request);

        if (repository.exists(serviceInstance.getServiceInstanceId()))
            throw new ServiceInstanceExistsException(request.getServiceInstanceId(), request.getServiceDefinitionId());

        try {
            Credential credential = s3Service.createUserResult(serviceInstance.getServiceInstanceId());

            serviceInstance.setCredential(credential);

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
                serviceInstance.getCredential().getServiceId())) {
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

    /**
     * Create a new binding to a service instance.
     *
     * @param request containing parameters sent from Cloud Controller
     * @return a CreateServiceInstanceBindingResponse
     * @throws ServiceInstanceBindingExistsException if a binding with the given ID is already known to the broker
     * @throws ServiceInstanceDoesNotExistException if a service instance with the given ID is not known to the broker
     * @throws ServiceBrokerException on internal failure
     */
    @Override
    public CreateServiceInstanceBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {
        S3ServiceInstanceBinding serviceInstanceBinding = bindingRepository.findOne(request.getBindingId());

        if (serviceInstanceBinding != null)
            throw new ServiceInstanceBindingExistsException(request.getServiceInstanceId(), request.getBindingId());

        S3ServiceInstance serviceInstance = null;

        // Get service instance
        try {
            serviceInstance = repository.getOne(request.getServiceInstanceId());
        } catch (ServiceInstanceDoesNotExistException ex) {
            log.error("Could not get service instance {}", ex);
            throw new ServiceBrokerException(ex);
        }

        serviceInstanceBinding = new S3ServiceInstanceBinding(request.getBindingId(),
                request.getServiceInstanceId(),
                request.getBoundAppGuid());

        try {
            serviceInstanceBinding = bindingRepository.save(serviceInstanceBinding);
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(), ex);
            throw new ServiceBrokerException(ex);
        }

        return new CreateServiceInstanceAppBindingResponse()
                .withCredentials(CredentialResource.toMap(serviceInstance.getCredential()));
    }

    /**
     * Delete a service instance binding.
     *
     * @param request containing parameters sent from Cloud Controller
     * @throws ServiceInstanceDoesNotExistException if a service instance with the given ID is not known to the broker
     * @throws ServiceInstanceBindingDoesNotExistException if a binding with the given ID is not known to the broker
     */
    @Override
    public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request) {

        if (!bindingRepository.exists(request.getBindingId())) {
            throw new ServiceBrokerException(String.format("Service instance binding does not exist: %s", request.getBindingId()));
        }

        S3ServiceInstanceBinding serviceInstanceBinding = bindingRepository.findOne(request.getBindingId());

        try {
            bindingRepository.delete(request.getBindingId());
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage(), ex);
        }

    }

}

interface S3ServiceInstanceBindingRepository extends JpaRepository<S3ServiceInstanceBinding, String> {
}
interface S3ServiceInstanceRepository extends JpaRepository<S3ServiceInstance, String> {
}