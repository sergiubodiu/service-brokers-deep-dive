package org.cloudfoundry.community.servicebroker.model.fixture;

import org.cloudfoundry.community.servicebroker.catalog.Plan;
import org.cloudfoundry.community.servicebroker.catalog.ServiceDefinition;
import org.cloudfoundry.community.servicebroker.catalog.ServiceInstance;
import org.cloudfoundry.community.servicebroker.model.CreateServiceInstanceRequest;
import org.cloudfoundry.community.servicebroker.model.CreateServiceInstanceResponse;
import org.cloudfoundry.community.servicebroker.model.UpdateServiceInstanceRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceInstanceFixture {

	public static List<ServiceInstance> getAllServiceInstances() {
		List<ServiceInstance> instances = new ArrayList<ServiceInstance>();
		instances.add(getServiceInstance());
		instances.add(getServiceInstanceTwo());
		return instances;
	}
	
	public static ServiceInstance getServiceInstance() {
		return new ServiceInstance(new CreateServiceInstanceRequest(
				"service-one-id", 
				"plan-one-id", 
				DataFixture.getOrgOneGuid(), 
				DataFixture.getSpaceOneGuid()).withServiceInstanceId("service-instnce-one-id"))
			.withDashboardUrl("dashboard_url");
				
	}
	
	public static ServiceInstance getServiceInstanceTwo() {
		return new ServiceInstance(new CreateServiceInstanceRequest(
				"service-two-id", 
				"plan-two-id", 
				DataFixture.getOrgOneGuid(), 
				DataFixture.getSpaceOneGuid()).withServiceInstanceId("service-instnce-two-id"))
			.withDashboardUrl("dashboard_url");

	}
	
	public static String getServiceInstanceId() {
		return "service-instance-id";
	}
	
	public static CreateServiceInstanceRequest getCreateServiceInstanceRequest() {
		ServiceDefinition service = ServiceFixture.getService();
		return new CreateServiceInstanceRequest(
				service.getId(), 
                ((Plan)(service.getPlans().stream().collect(Collectors.toList()).get(0))).getId(),
				DataFixture.getOrgOneGuid(),
				DataFixture.getSpaceOneGuid(),
				ParametersFixture.getParameters()
		);
	}
	
	public static String getCreateServiceInstanceRequestJson() throws IOException {
		 return DataFixture.toJson(getCreateServiceInstanceRequest());
	}
		
	public static CreateServiceInstanceResponse getCreateServiceInstanceResponse() {
		return new CreateServiceInstanceResponse(getServiceInstance());
	}

	public static String getUpdateServiceInstanceRequestJson() throws IOException {
		return DataFixture.toJson(getUpdateServiceInstanceRequest());
	}
	
	public static UpdateServiceInstanceRequest getUpdateServiceInstanceRequest() {
		ServiceDefinition service = ServiceFixture.getService();
		return new UpdateServiceInstanceRequest(((Plan)(service.getPlans().stream().collect(Collectors.toList()).get(0))).getId());
	}


}
