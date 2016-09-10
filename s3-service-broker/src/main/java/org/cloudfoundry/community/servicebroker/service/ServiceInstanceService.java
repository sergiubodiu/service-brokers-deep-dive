package org.cloudfoundry.community.servicebroker.service;

import org.cloudfoundry.community.servicebroker.catalog.ServiceInstance;
import org.cloudfoundry.community.servicebroker.exception.ServiceBrokerException;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceExistsException;
import org.cloudfoundry.community.servicebroker.exception.ServiceInstanceUpdateNotSupportedException;
import org.cloudfoundry.community.servicebroker.model.*;

/**
 * Handles instances of service definitions.
 * 
 * @author sgreenberg@gopivotal.com
 */
public interface ServiceInstanceService {

	/**
	 * Create a new instance of a service
	 * @param createServiceInstanceRequest containing the parameters from CloudController
	 * @return The newly created ServiceInstance
	 * @throws ServiceInstanceExistsException if the service instance already exists.
	 * @throws ServiceBrokerException if something goes wrong internally
	 */
	ServiceInstance createServiceInstance(CreateServiceInstanceRequest createServiceInstanceRequest)
			throws ServiceInstanceExistsException, ServiceBrokerException;

	/**
	 * @param serviceInstanceId The id of the serviceInstance
	 * @return The ServiceInstance with the given id or null if one does not exist
	 */
	ServiceInstance getServiceInstance(String serviceInstanceId) throws ServiceInstanceDoesNotExistException;

	/**
	 * Delete and return the instance if it exists.
	 * @param deleteServiceInstanceRequest containing pertinent information for deleting the service.
	 * @return The deleted ServiceInstance or null if one did not exist.
	 * @throws ServiceBrokerException is something goes wrong internally
	 */
	ServiceInstance deleteServiceInstance(DeleteServiceInstanceRequest deleteServiceInstanceRequest)
            throws ServiceBrokerException, ServiceInstanceDoesNotExistException;

	/**
	 * Update a service instance. Only modification of service plan is supported.
	 * @param updateServiceInstanceRequest detailing the request parameters
	 *
	 * @return The updated serviceInstance
	 * @throws ServiceInstanceUpdateNotSupportedException if particular plan change is not supported
	 *         or if the request can not currently be fulfilled due to the state of the instance.
	 * @throws ServiceInstanceDoesNotExistException if the service instance does not exist
	 * @throws ServiceBrokerException if something goes wrong internally
	 *
	 */
	ServiceInstance updateServiceInstance(UpdateServiceInstanceRequest updateServiceInstanceRequest)
			throws ServiceInstanceUpdateNotSupportedException, ServiceBrokerException,
			ServiceInstanceDoesNotExistException;

}
