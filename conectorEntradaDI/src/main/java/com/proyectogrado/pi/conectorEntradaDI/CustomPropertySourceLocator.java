package com.proyectogrado.pi.conectorEntradaDI;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;



@Configuration
public class CustomPropertySourceLocator implements PropertySourceLocator {

  private final String configServiceName;
  private final DiscoveryClient discoveryClient;

  public CustomPropertySourceLocator(
      @Value("${spring.cloud.config.discovery.service-id}") String configServiceName,
      DiscoveryClient discoveryClient){
    this.configServiceName = configServiceName;
    this.discoveryClient = discoveryClient;
    
  }


  @Override
  public PropertySource<?> locate(Environment environment) {
    List<ServiceInstance> instances = this.discoveryClient.getInstances(this.configServiceName);
    if (instances.isEmpty()) {
    	return null;
    }
    ServiceInstance serviceInstance = instances.get(0);

    return new MapPropertySource("customProperty",
      Collections.singletonMap("configserver.discovered.uri", serviceInstance.getUri()));
  }
}