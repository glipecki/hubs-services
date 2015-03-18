package eu.anmore.hubs.samples.boot;

import eu.anmore.hubs.api.spring.ServiceRegisterer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CharacterEncodingFilter;

import static eu.anmore.hubs.api.spring.ServiceRegistererBuilder.serviceRegistererBuilder;

@SpringBootApplication
public class BootServiceSampleApplication {

    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(BootServiceSampleApplication.class, args);
    }

    @Bean
    public ServiceRegisterer serviceRegisterer() {
        return serviceRegistererBuilder(applicationContext).withServiceUrl("http://localhost:8081/").withHubUrl
                ("http://localhost:8080/").get();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CharacterEncodingFilter characterEncodingFilter() {
        final CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

}
