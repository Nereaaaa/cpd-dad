package es.codeurjc.api.Messaging;
 
 import es.codeurjc.api.Model.Disk;
 import es.codeurjc.api.Repository.DiskRepository;
 import com.fasterxml.jackson.databind.ObjectMapper;
 import org.springframework.amqp.rabbit.annotation.RabbitListener;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Component;
 import org.springframework.amqp.core.Message;
 import java.nio.charset.StandardCharsets;

 @Component
 public class DiskStatusListener {
 
     private final ObjectMapper objectMapper = new ObjectMapper();
     @Autowired
     private DiskRepository diskRepository;
 
     @RabbitListener(queues = "disk-statuses")
     public void handleDiskStatus(Message message) {
         try {
         	String json = new String(message.getBody(), StandardCharsets.UTF_8);
             Disk disk = objectMapper.readValue(json, Disk.class);
             diskRepository.save(disk);
             System.out.println("[API] Disco actualizado: " + disk.getId() + " - " + disk.getStatus());
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 }