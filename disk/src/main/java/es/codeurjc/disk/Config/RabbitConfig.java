package es.codeurjc.disk.Config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String DISK_REQUESTS = "disk-requests";
    public static final String DISK_STATUSES = "disk-statuses";

    @Bean
    public Queue diskRequestsQueue() {
        return new Queue(DISK_REQUESTS, true);
    }

    @Bean
    public Queue diskStatusesQueue() {
        return new Queue(DISK_STATUSES, true);
    }
}