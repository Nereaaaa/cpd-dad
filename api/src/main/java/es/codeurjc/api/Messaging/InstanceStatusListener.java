package es.codeurjc.api.Messaging;
 
import es.codeurjc.api.Model.*;
 import es.codeurjc.api.Repository.InstanceRepository;
 import com.fasterxml.jackson.databind.ObjectMapper;
 import org.springframework.amqp.rabbit.annotation.RabbitListener;
 import org.springframework.stereotype.Component;
 import org.springframework.amqp.core.Message;
 import java.nio.charset.StandardCharsets;
 import org.springframework.beans.factory.annotation.Autowired;
 import java.util.Map;
 import java.util.Optional;
 
 @Component
 public class InstanceStatusListener {
 
     private final ObjectMapper objectMapper = new ObjectMapper();
     @Autowired
     private InstanceRepository instanceRepository;
     
     @RabbitListener(queues = "instance-statuses")
     public void handleInstanceStatus(Map<String, Object> message) {
         try {
        	 Object idObj = message.get("id");
         	Long id = (idObj instanceof Number) ? ((Number) idObj).longValue() : null;
         	
             InstanceStatus status = InstanceStatus.valueOf(message.get("status").toString());
             
             String ip = (String) message.get("ip"); 

             System.out.println("[API] Estado recibido para instancia " + id + ": " + status);
             Optional<Instance> instanceOpt = instanceRepository.findById(id);
             if (instanceOpt.isPresent()) {
                 Instance instance = instanceOpt.get();
                 instance.setStatus(status);

                 if (ip != null && !ip.isBlank()) {
                     instance.setIp(ip);
                 }

                 instanceRepository.save(instance); // Guardar cambios
                 System.out.println("[API] Instancia actualizada: " + instance.getId() + " estado: " + instance.getStatus());
             } else {
                 System.err.println("[API] No se encontró instancia con ID: " + id);
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 }
