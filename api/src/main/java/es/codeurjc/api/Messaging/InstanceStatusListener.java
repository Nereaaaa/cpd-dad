package es.codeurjc.api.Messaging;
 
 import es.codeurjc.api.Model.Instance;
 import es.codeurjc.api.Repository.InstanceRepository;
 import com.fasterxml.jackson.databind.ObjectMapper;
 import org.springframework.amqp.rabbit.annotation.RabbitListener;
 import org.springframework.stereotype.Component;
 
 @Component
 public class InstanceStatusListener {
 
     private final ObjectMapper objectMapper = new ObjectMapper();
     private final InstanceRepository instanceRepository;
 
     public InstanceStatusListener(InstanceRepository instanceRepository) {
         this.instanceRepository = instanceRepository;
     }
 
     @RabbitListener(queues = "instance-statuses")
     public void handleInstanceStatus(String message) {
         try {
             Instance instance = objectMapper.readValue(message, Instance.class);
             instanceRepository.save(instance);
             System.out.println("[API] Instancia actualizada: " + instance.getId() + " - " + instance.getStatus());
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 }
