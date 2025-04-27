package es.codeurjc.api.Service;

import es.codeurjc.api.Model.Disk;
import es.codeurjc.api.Repository.DiskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DiskService {

    private final DiskRepository diskRepository;

    public DiskService(DiskRepository diskRepository) {
        this.diskRepository = diskRepository;
    }

    public List<Disk> findAll() {
        return diskRepository.findAll();
    }

    public Optional<Disk> findById(UUID id) {
        return diskRepository.findById(id);
    }

    public Disk save(Disk disk) {
        return diskRepository.save(disk);
    }

    public void delete(UUID id) {
        diskRepository.deleteById(id);
    }
}