package ws.cogito.magic.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties(AmqpClientProperties.class)
public class AmqpDeclarationsConfiguration {
	
	@Autowired
	@Qualifier("amqpClientProperties")
	AmqpClientProperties properties;
	
	private static final Logger logger = LoggerFactory.getLogger (AmqpDeclarationsConfiguration.class);
	
    @Bean
    public ConnectionFactory connectionFactory() {
    	
    	CachingConnectionFactory connectionFactory = null;
    	
    	logger.info("Configuring Connection Factory");
    	
        connectionFactory = new CachingConnectionFactory(properties.getHost());
        connectionFactory.setUsername(properties.getUserName());
        connectionFactory.setPassword(properties.getUserPassword());
        connectionFactory.setVirtualHost(properties.getvHost());
    	
    	return connectionFactory;
    }	
	
	/**
	 * In order for all the other configurations to take effect there must
	 * be at least one decleration by the rabbitAdmin
	 * @param connectionFactory
	 * @return RabbitAdmin
	 */
    @Bean
    public RabbitAdmin admin(ConnectionFactory connectionFactory) {
    	
    	logger.info("Kicking off Declarations");
    	
    	RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
    	
    	/* 
    	 * *********************IMPORTANT********************************
    	 * 
    	 * None of the other declarations take effect unless at least one 
    	 * declaration is executed by RabbitAdmin.
    	 * 
    	 * *********************IMPORTANT******************************** 
    	 */
    	rabbitAdmin.declareExchange(new DirectExchange(properties.getDirectExchange()));
    	
        return new RabbitAdmin(connectionFactory);
    }	

	@Bean
	TopicExchange exchange() {
		return new TopicExchange(properties.getTopicExchange());
	}
}