package es.codeurjc.api.Controller;

import es.codeurjc.api.Model.Disk;
import es.codeurjc.api.Model.Instance;
import es.codeurjc.api.Service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/instances")
public class InstanceController {
	
	@Autowired
    private InstanceService instanceService;
	
	@Autowired
    private DiskService diskService;


	@GetMapping
    public List<Instance> getAllInstances() {
        return instanceService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Instance> getInstanceById(@PathVariable UUID id) {
        return instanceService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createInstance(@RequestBody Instance instance) {
        instance.setId(UUID.randomUUID());

        // Validar que el disco exista y esté en estado ASSIGNED (opcional, según negocio)
        Disk incomingDisk = instance.getDisk();
        if (incomingDisk == null || incomingDisk.getId() == null) {
            return ResponseEntity.badRequest().body("Disk ID is required.");
        }

        Optional<Disk> diskOpt = diskService.findById(incomingDisk.getId());
        if (diskOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Disk not found.");
        }

        instance.setDisk(diskOpt.get());

        Instance saved = instanceService.save(instance);
        return ResponseEntity.created(URI.create("/api/instances/" + saved.getId())).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstance(@PathVariable UUID id) {
        instanceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}