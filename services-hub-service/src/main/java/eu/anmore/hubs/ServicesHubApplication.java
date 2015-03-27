package eu.anmore.hubs;

import eu.anmore.hubs.config.ServicesHubConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CharacterEncodingFilter;

public class ServicesHubApplication extends ServicesHubConfiguration {

    public static void main(String[] args) {
        SpringApplication.run(ServicesHubApplication.class, args);
    }

}
