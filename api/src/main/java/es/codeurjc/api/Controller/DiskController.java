package es.codeurjc.api.Controller;

import es.codeurjc.api.Model.Disk;
import es.codeurjc.api.Model.DiskStatus;
import es.codeurjc.api.Service.DiskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/disks")
public class DiskController {

    private final DiskService diskService;

    public DiskController(DiskService diskService) {
        this.diskService = diskService;
    }

    @GetMapping
    public List<Disk> getAllDisks() {
        return diskService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Disk> getDiskById(@PathVariable UUID id) {
        return diskService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Disk> createDisk(@RequestBody Disk disk) {
        disk.setId(UUID.randomUUID());
        Disk saved = diskService.save(disk);
        return ResponseEntity.created(URI.create("/api/disks/" + saved.getId())).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDisk(@PathVariable UUID id) {
        Optional<Disk> diskOpt = diskService.findById(id);
        if (diskOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Disk disk = diskOpt.get();
        if (!disk.getStatus().equals(DiskStatus.UNASSIGNED)) {
            return ResponseEntity.status(403).body("Disk is currently assigned and cannot be deleted.");
        }

        diskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}