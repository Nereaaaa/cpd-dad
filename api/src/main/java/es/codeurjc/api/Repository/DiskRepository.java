package es.codeurjc.api.Repository;

import es.codeurjc.api.Model.Disk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DiskRepository extends JpaRepository<Disk, UUID> {
    // Puedes añadir métodos personalizados si los necesitas
}