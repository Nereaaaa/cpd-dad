package es.codeurjc.api.Messaging;
 
 import es.codeurjc.api.Model.*;
 import es.codeurjc.api.Repository.DiskRepository;
import es.codeurjc.api.Service.DiskService;

import com.fasterxml.jackson.databind.ObjectMapper;
 import org.springframework.amqp.rabbit.annotation.RabbitListener;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Component;
 import org.springframework.amqp.core.Message;
 import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

 @Component
 public class DiskStatusListener {
 
     private final ObjectMapper objectMapper = new ObjectMapper();
     @Autowired
     private DiskRepository diskRepository;
     @Autowired
     private DiskService diskService;
 
     @RabbitListener(queues = "disk-statuses")
     public void handleDiskStatus(Map<String, Object> message) {
         try {
         	
         	Object idObj = message.get("id");
         	Long id = (idObj instanceof Number) ? ((Number) idObj).longValue() : null;
         	
             int size = (int) message.get("size");
             DiskType type = DiskType.valueOf(message.get("type").toString());
             DiskStatus status = DiskStatus.valueOf(message.get("status").toString());
             
             System.out.println("[API] Estado recibido: " + id + " -> " + status+" -> " + type+" -> " + size);
             
             Optional<Disk> diskOpt = diskRepository.findById(id);
             if (diskOpt.isPresent()) {
                 Disk disk = diskOpt.get();
                 disk.setStatus(status);           
                 diskRepository.save(disk);           
             } else {
                 throw new RuntimeException("Disk not found with ID: " + id);
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 }