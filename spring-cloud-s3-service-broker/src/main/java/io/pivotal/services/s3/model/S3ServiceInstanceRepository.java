package io.pivotal.services.s3.model;

import org.springframework.data.jpa.repository.JpaRepository;


public interface S3ServiceInstanceRepository extends JpaRepository<S3ServiceInstance, String> {
}
