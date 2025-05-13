package es.codeurjc.api.Service;

import es.codeurjc.api.Model.*;
import es.codeurjc.api.Repository.InstanceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InstanceService {

    @Autowired
    private InstanceRepository instanceRepository;

    public List<Instance> findAll() {
        return instanceRepository.findAll();
    }

    public Optional<Instance> findById(Long id) {
        return instanceRepository.findById(id);
    }

    public Instance save(Instance instance) {
        return instanceRepository.save(instance);
    }

    public void delete(Long id) {
        instanceRepository.deleteById(id);
    }
    public Optional<Instance> findByDisk(Disk disk) {
        return instanceRepository.findByDisk(disk);
    }
}