package es.codeurjc.api.Service;

import es.codeurjc.api.Model.Disk;
import es.codeurjc.api.Repository.DiskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DiskService {

	@Autowired
    private DiskRepository diskRepository;
	
    public List<Disk> findAll() {
        return diskRepository.findAll();
    }

    public Optional<Disk> findById(Long id) {
        return diskRepository.findById(id);
    }

    public Disk save(Disk disk) {
        return diskRepository.save(disk);
    }

    public void delete(Long id) {
        diskRepository.deleteById(id);
    }
}