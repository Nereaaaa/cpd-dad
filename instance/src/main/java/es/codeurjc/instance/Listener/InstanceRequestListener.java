package es.codeurjc.instance.Listener;
 
 import es.codeurjc.instance.Config.RabbitConfig;
 import es.codeurjc.instance.Model.Instance;
 import es.codeurjc.instance.Model.InstanceStatus;
 import com.fasterxml.jackson.databind.ObjectMapper;
 import org.springframework.amqp.rabbit.annotation.RabbitListener;
 import org.springframework.amqp.rabbit.core.RabbitTemplate;
 import org.springframework.stereotype.Component;
 import org.springframework.beans.factory.annotation.Autowired;
 import java.util.UUID;
 import java.util.concurrent.*;
 import org.springframework.amqp.core.Message;
 import java.nio.charset.StandardCharsets;
 import java.util.Map;

 @Component
 public class InstanceRequestListener {
 
	 @Autowired
	 private  RabbitTemplate rabbitTemplate;
     
 
     @RabbitListener(queues = RabbitConfig.INSTANCE_REQUESTS)
     public void handleInstanceRequest(Map<String, Object> message) {
         try {
        	 Object idObj = message.get("id");
         	Long id = (idObj instanceof Number) ? ((Number) idObj).longValue() : null;
         	
         	String name = (String) message.get("name");
             int memory = (int) message.get("memory");
             int cores = (int) message.get("cores");
             
             
             Instance instance = new Instance();
             instance.setId(id);
             instance.setName(name);
             instance.setMemory(memory);
             instance.setCores(cores);
             instance.setStatus(InstanceStatus.BUILDING_DISK);
             System.out.println("[INSTANCE] Received request to create instance: "+ id + ": " + instance.getStatus());

             sendStatus(instance, InstanceStatus.BUILDING_DISK, 0);
             sendStatus(instance, InstanceStatus.STARTING, 5);
             sendStatus(instance, InstanceStatus.INITIALIZING, 10);
             sendStatus(instance, InstanceStatus.ASSIGNING_IP, 15);
 
             ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
             executor.schedule(() -> {
                 try {
                     instance.setIp("192.168.1." + (int) (Math.random() * 253 + 2));
                     instance.setStatus(InstanceStatus.RUNNING);
                     Map<String, Object> payload = Map.of(
                             "id", instance.getId(),
                             "status",instance.getStatus(),
                             "ip",instance.getIp()
                         );
                     rabbitTemplate.convertAndSend(RabbitConfig.INSTANCE_STATUSES, payload);
                     System.out.println("[INSTANCE] Instance ready: " + id + " with IP " + instance.getIp());
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }, 20, TimeUnit.SECONDS);
 
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 
     private void sendStatus(Instance instance, InstanceStatus status, int delay) {
         ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
         executor.schedule(() -> {
             try {
                 instance.setStatus(status);
                 Map<String, Object> payload;
                 if (instance.getIp() != null) {
                     payload = Map.of(
                             "id", instance.getId(),
                             "status", instance.getStatus(),
                             "ip", instance.getIp()
                     );
                 } else {
                     payload = Map.of(
                             "id", instance.getId(),
                             "status", instance.getStatus()
                     );
                 }
                 rabbitTemplate.convertAndSend(RabbitConfig.INSTANCE_STATUSES, payload);
                 System.out.println("[INSTANCE] Estado: " + instance.getId() + " -> " + status);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }, delay, TimeUnit.SECONDS);
     }
 }