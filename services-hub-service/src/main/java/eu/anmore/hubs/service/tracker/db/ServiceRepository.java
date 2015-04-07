package eu.anmore.hubs.service.tracker.db;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Collection;

@Transactional
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    int countByUrl(String url);

    void deleteByUrl(String url);

    Collection<ServiceEntity> findByName(String name);

}
