package eu.anmore.hubs.samples.boot;

import eu.anmore.hubs.api.spring.config.SpringBootHubServiceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BootServiceSampleApplication extends SpringBootHubServiceConfiguration {

    public static void main(String[] args) {
        SpringApplication.run(BootServiceSampleApplication.class, args);
    }

}
