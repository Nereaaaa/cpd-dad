package es.codeurjc.instance.Config;
 
 import org.springframework.amqp.core.Queue;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 
 @Configuration
 public class RabbitConfig {
 
     public static final String INSTANCE_REQUESTS = "instance-requests";
     public static final String INSTANCE_STATUSES = "instance-statuses";
 
     @Bean
     public Queue instanceRequestsQueue() {
         return new Queue(INSTANCE_REQUESTS, true);
     }
 
     @Bean
     public Queue instanceStatusesQueue() {
         return new Queue(INSTANCE_STATUSES, true);
     }
 }