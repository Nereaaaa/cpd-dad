package es.codeurjc.disk.Listener;
 
 import es.codeurjc.disk.Config.RabbitConfig;
 import es.codeurjc.disk.Model.*;
 import com.fasterxml.jackson.databind.ObjectMapper;
 import org.springframework.amqp.rabbit.annotation.RabbitListener;
 import org.springframework.amqp.rabbit.core.RabbitTemplate;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
 import java.util.concurrent.*;
 import org.springframework.amqp.core.Message;
 import java.nio.charset.StandardCharsets;

 @Component
 public class DiskRequestListener {
 	@Autowired
    private  RabbitTemplate rabbitTemplate;
    
 
 
     @RabbitListener(queues = RabbitConfig.DISK_REQUESTS)
     public void handleDiskRequest(Map<String, Object> message) {
         try {
         	
         	Object idObj = message.get("id");
         	Long id = (idObj instanceof Number) ? ((Number) idObj).longValue() : null;
         	
             int size = (int) message.get("size");
             DiskType type = DiskType.valueOf(message.get("type").toString());
             DiskStatus status = DiskStatus.valueOf(message.get("status").toString());
             
             System.out.println("[DISK] Received status: " + id + " -> " + status+" -> " + type+" -> " + size);
            
             Disk disk = new Disk();
             disk.setId(id); 
             disk.setSize(size);
             disk.setType(type);
             disk.setStatus(status);

             disk.setStatus(DiskStatus.REQUESTED);
             send(disk, 2,"REQUESTED");

             disk.setStatus(DiskStatus.INITIALIZING);
             send(disk, 5,"INITIALIZING");

             disk.setStatus(DiskStatus.ASSIGNED);
             send(disk, 10,"ASSIGNED");

         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 
     private void send(Disk disk, int delayInSeconds, String stat) {
         ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
         executor.schedule(() -> {
             try {
             	Map<String, Object> payload = Map.of(
                         "id", disk.getId(),
                         "size", disk.getSize(),
                         "type", disk.getType().toString(),
                         "status", stat
                     );
             	System.out.println("[DISK] Sent status: " + disk.getId() + " -> " + stat +" -> "+ disk.getType() + " -> " + disk.getSize());
                 rabbitTemplate.convertAndSend(RabbitConfig.DISK_STATUSES, payload);
                 
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }, delayInSeconds, TimeUnit.SECONDS);
     }
 }