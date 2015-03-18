package eu.anmore.hubs.service;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ServiceEntity {

    ServiceEntity() {
    }

    public ServiceEntity(String url, String name, String version, String description) {
        this.url = url;
        this.name = name;
        this.version = version;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String url;

    public String name;

    public String version;

    public String description;

}
