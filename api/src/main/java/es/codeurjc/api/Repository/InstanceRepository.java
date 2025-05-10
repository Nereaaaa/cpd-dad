package es.codeurjc.api.Repository;

import es.codeurjc.api.Model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface InstanceRepository extends JpaRepository<Instance, Long> {
	Optional<Instance> findByDisk(Disk disk);
}
