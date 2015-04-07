package eu.anmore.hubs.api.spring.config;


import eu.anmore.hubs.api.spring.ServiceRegisterer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.filter.CharacterEncodingFilter;

import static eu.anmore.hubs.api.spring.ServiceRegistererBuilder.serviceRegistererBuilder;

@SpringBootApplication
@EnableScheduling
public class SpringBootHubServiceConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CharacterEncodingFilter characterEncodingFilter() {
        final CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

    @Bean
    public ServiceRegisterer serviceRegisterer() {
        return serviceRegistererBuilder(applicationContext).withServiceUrl("http://localhost:8081/").withHubUrl
                ("http://localhost:8080/").get();
    }

}
