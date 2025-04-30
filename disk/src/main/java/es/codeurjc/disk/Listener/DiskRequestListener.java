package es.codeurjc.disk.Listener;
 
 import es.codeurjc.disk.Config.RabbitConfig;
 import es.codeurjc.disk.Model.Disk;
 import es.codeurjc.disk.Model.DiskStatus;
 import com.fasterxml.jackson.databind.ObjectMapper;
 import org.springframework.amqp.rabbit.annotation.RabbitListener;
 import org.springframework.amqp.rabbit.core.RabbitTemplate;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Component;
 
 import java.util.UUID;
 import java.util.concurrent.*;
 
 @Component
 public class DiskRequestListener {
 	@Autowired
     private  RabbitTemplate rabbitTemplate;
     private final ObjectMapper objectMapper = new ObjectMapper();
 
 
     @RabbitListener(queues = RabbitConfig.DISK_REQUESTS)
     public void handleDiskRequest(String message) {
         try {
             Disk disk = objectMapper.readValue(message, Disk.class);
             UUID diskId = disk.getId();
 
             //System.out.println("[DISK] Petición recibida para crear disco: " + diskId);
 
             // Estado REQUESTED
             disk.setStatus(DiskStatus.REQUESTED);
             send(disk, 0);
 
             // Estado INITIALIZING
             disk.setStatus(DiskStatus.INITIALIZING);
             send(disk, 5);
 
             // Estado ASSIGNED
             disk.setStatus(DiskStatus.ASSIGNED);
             send(disk, 10);
 
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 
     private void send(Disk disk, int delayInSeconds) {
         ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
         executor.schedule(() -> {
             try {
                 String updated = objectMapper.writeValueAsString(disk);
                 rabbitTemplate.convertAndSend(RabbitConfig.DISK_STATUSES, updated);
                 //System.out.println("[DISK] Estado enviado: " + disk.getId() + " -> " + disk.getStatus());
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }, delayInSeconds, TimeUnit.SECONDS);
     }
 }