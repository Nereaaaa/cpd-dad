package es.codeurjc.instance.Listener;
 
 import es.codeurjc.instance.Config.RabbitConfig;
 import es.codeurjc.instance.Model.Instance;
 import es.codeurjc.instance.Model.InstanceStatus;
 import com.fasterxml.jackson.databind.ObjectMapper;
 import org.springframework.amqp.rabbit.annotation.RabbitListener;
 import org.springframework.amqp.rabbit.core.RabbitTemplate;
 import org.springframework.stereotype.Component;
 
 import java.util.UUID;
 import java.util.concurrent.*;
 import org.springframework.amqp.core.Message;
 import java.nio.charset.StandardCharsets;

 @Component
 public class InstanceRequestListener {
 
     private final RabbitTemplate rabbitTemplate;
     private final ObjectMapper objectMapper = new ObjectMapper();
 
     public InstanceRequestListener(RabbitTemplate rabbitTemplate) {
         this.rabbitTemplate = rabbitTemplate;
     }
 
     @RabbitListener(queues = RabbitConfig.INSTANCE_REQUESTS)
     public void handleInstanceRequest(Message message) {
         try {
         	String json = new String(message.getBody(), StandardCharsets.UTF_8);
             Instance instance = objectMapper.readValue(json, Instance.class);
             UUID id = instance.getId();
 
             //System.out.println("[INSTANCE] Recibida petición para crear instancia: " + id);
 
             sendStatus(instance, InstanceStatus.BUILDING_DISK, 0);
             sendStatus(instance, InstanceStatus.STARTING, 5);
             sendStatus(instance, InstanceStatus.INITIALIZING, 10);
             sendStatus(instance, InstanceStatus.ASSIGNING_IP, 15);
 
             // Asignar IP aleatoria justo antes de "RUNNING"
             ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
             executor.schedule(() -> {
                 try {
                     instance.setIp("192.168.1." + (int) (Math.random() * 253 + 2));
                     instance.setStatus(InstanceStatus.RUNNING);
                     String msg = objectMapper.writeValueAsString(instance);
                     rabbitTemplate.convertAndSend(RabbitConfig.INSTANCE_STATUSES, msg);
                     System.out.println("[INSTANCE] Instancia lista: " + id + " con IP " + instance.getIp());
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
                 String json = objectMapper.writeValueAsString(instance);
                 rabbitTemplate.convertAndSend(RabbitConfig.INSTANCE_STATUSES, json);
                 System.out.println("[INSTANCE] Estado: " + instance.getId() + " -> " + status);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }, delay, TimeUnit.SECONDS);
     }
 }