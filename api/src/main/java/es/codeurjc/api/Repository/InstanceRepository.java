package es.codeurjc.api.Repository;

import es.codeurjc.api.Model.Instance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InstanceRepository extends JpaRepository<Instance, Long> {
    // Métodos personalizados si quieres buscar por estado, nombre, etc.
}
