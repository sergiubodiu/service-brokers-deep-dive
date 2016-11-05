package io.pivotal.services.s3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.servicebroker.model.Catalog;
import org.springframework.cloud.servicebroker.model.Plan;
import org.springframework.cloud.servicebroker.model.ServiceDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import java.util.Collections;
import java.util.Map;

@ComponentScan
@EnableAutoConfiguration
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}


@Configuration
class Config {

    @Bean
    public Catalog catalog() {
        return new Catalog(Collections.singletonList(
                new ServiceDefinition(
                        "00a3b868-9cf9-4ad3-a6b0-e867740cbef0",
                        "amazon-s3",
                        "Amazon S3 simple storage as a backing service",
                        true,
                        Collections.singletonList(
                                new Plan("ac8fdb55-3223-41e9-a5f5-eca6f8fd40c0",
                                        "s3-basic",
                                        "Amazon S3 bucket with unlimited storage",
                                        getPlanMetadata(),
                                        true))
                )));
    }

    private Map<String, Object> getPlanMetadata() {
        return Collections.EMPTY_MAP;
    }
}
