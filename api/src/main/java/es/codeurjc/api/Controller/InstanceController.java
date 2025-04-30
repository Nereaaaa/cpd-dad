package es.codeurjc.api.Controller;

import es.codeurjc.api.Config.RabbitConfig;
import es.codeurjc.api.DTO.CreateInstanceRequest;
import es.codeurjc.api.Messaging.MessageSender;
import es.codeurjc.api.Model.Disk;
import es.codeurjc.api.Model.DiskStatus;
import es.codeurjc.api.Model.Instance;
import es.codeurjc.api.Model.InstanceStatus;
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
	@Autowired
 	private MessageSender messageSender;


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
    public ResponseEntity<?> createInstance(@RequestBody CreateInstanceRequest request) {
        
        Disk newDisk = new Disk();
        newDisk.setId(UUID.randomUUID());
        newDisk.setSize(request.getDiskSize());
        newDisk.setType(request.getDiskType());
        newDisk.setStatus(DiskStatus.REQUESTED);
        Disk savedDisk = diskService.save(newDisk);

        
        Instance newInstance = new Instance();
        newInstance.setId(UUID.randomUUID());
        newInstance.setName(request.getName());
        newInstance.setMemory(request.getMemory());
        newInstance.setCores(request.getCores());
        newInstance.setStatus(InstanceStatus.BUILDING_DISK);
        newInstance.setDisk(savedDisk);

        Instance savedInstance = instanceService.save(newInstance);
        
        messageSender.send(RabbitConfig.DISK_REQUESTS, savedDisk);

        return ResponseEntity.created(URI.create("/api/instances/" + savedInstance.getId()))
                .body(savedInstance);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstance(@PathVariable UUID id) {
        instanceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}