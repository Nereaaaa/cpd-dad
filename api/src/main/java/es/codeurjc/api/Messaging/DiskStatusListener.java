package es.codeurjc.api.Messaging;
 
import es.codeurjc.api.Model.*;
import es.codeurjc.api.Repository.*;
import es.codeurjc.api.Service.*;

import com.fasterxml.jackson.databind.ObjectMapper;
 import org.springframework.amqp.rabbit.annotation.RabbitListener;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Component;
 import org.springframework.amqp.core.Message;
 import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import es.codeurjc.api.Config.RabbitConfig;

 @Component
 public class DiskStatusListener {
 
	 @Autowired
	 private InstanceService instanceService;
     @Autowired
     private DiskService diskService;
     @Autowired
 	 private MessageSender messageSender;
     
     @RabbitListener(queues = "disk-statuses")
     public void handleDiskStatus(Map<String, Object> message) {
         try {
         	
         	Object idObj = message.get("id");
         	Long id = (idObj instanceof Number) ? ((Number) idObj).longValue() : null;
         	
             int size = (int) message.get("size");
             DiskType type = DiskType.valueOf(message.get("type").toString());
             DiskStatus status = DiskStatus.valueOf(message.get("status").toString());
             
             System.out.println("[API] Received status: " + id + " -> " + status+" -> " + type+" -> " + size);
             
             Optional<Disk> diskOpt = diskService.findById(id);
             if (diskOpt.isPresent()) {
                 Disk disk = diskOpt.get();
                 
                 disk.setStatus(status);       
                 diskService.save(disk);          
                 if (status == DiskStatus.ASSIGNED) {
                 	Optional<Instance> instanceOpt = instanceService.findByDisk(disk);
                     if (instanceOpt.isPresent()) {
                         Instance instance = instanceOpt.get();

                         Map<String, Object> payload = Map.of(
                             "id", instance.getId(),
                             "name", instance.getName(),
                             "memory", instance.getMemory(),
                             "cores", instance.getCores(),
                             "diskId", disk.getId()
                         );

                         messageSender.send(RabbitConfig.INSTANCE_REQUESTS, payload);

                         System.out.println("[API] Petition send to create an Instance: " + instance.getId());
                     } else {
                         System.out.println("[API] No instance associated with the disk was found:  " + disk.getId());
                     }
                 }      
             } else {
                 throw new RuntimeException("Disk not found with ID: " + id);
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 }