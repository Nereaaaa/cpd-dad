package es.codeurjc.api.Config;
 
 import org.springframework.amqp.core.Queue;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
 import org.springframework.amqp.support.converter.MessageConverter;
 import org.springframework.amqp.rabbit.core.RabbitTemplate;
 import org.springframework.amqp.rabbit.connection.ConnectionFactory;

 @Configuration
 public class RabbitConfig {
 
     public static final String DISK_REQUESTS = "disk-requests";
     public static final String DISK_STATUSES = "disk-statuses";
 
     public static final String INSTANCE_REQUESTS = "instance-requests";
     public static final String INSTANCE_STATUSES = "instance-statuses";
 
     @Bean
     public Queue diskRequestsQueue() {
         return new Queue(DISK_REQUESTS, true);
     }
 
     @Bean
     public Queue diskStatusesQueue() {
         return new Queue(DISK_STATUSES, true);
     }
 
     @Bean
     public Queue instanceRequestsQueue() {
         return new Queue(INSTANCE_REQUESTS, true);
     }
 
     @Bean
     public Queue instanceStatusesQueue() {
         return new Queue(INSTANCE_STATUSES, true);
     }
     
     
     @Bean
     public MessageConverter jsonMessageConverter() {
         return new Jackson2JsonMessageConverter();
     }
     
     @Bean
     public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
         RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
         rabbitTemplate.setMessageConverter(messageConverter);
         return rabbitTemplate;
     }
 }