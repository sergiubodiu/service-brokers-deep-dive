package org.cloudfoundry.community.servicebroker.config;

import org.cloudfoundry.community.servicebroker.catalog.Catalog;
import org.cloudfoundry.community.servicebroker.repositories.ServiceDefinitionRepository;
import org.cloudfoundry.community.servicebroker.model.BrokerApiVersion;
import org.cloudfoundry.community.servicebroker.service.BeanCatalogService;
import org.cloudfoundry.community.servicebroker.service.CatalogService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = {"org.cloudfoundry.community.servicebroker.*"})
@ConditionalOnWebApplication
@AutoConfigureAfter({WebMvcAutoConfiguration.class, ServiceDefinitionRepository.class})
public class ServiceBrokerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(BrokerApiVersion.class)
    public BrokerApiVersion brokerApiVersion() {
        return new BrokerApiVersion("2.9");
    }

    @Bean
    @ConditionalOnMissingBean(CatalogService.class)
    public CatalogService beanCatalogService(Catalog catalog, ServiceDefinitionRepository serviceDefinitionRepository) {
        return new BeanCatalogService(catalog, serviceDefinitionRepository);
    }


}