package ws.cogito.magic.utilities.web;

import static ws.cogito.magic.utilities.web.TrackingIdInterceptor.TRACKING_ID;

import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ws.cogito.magic.utilities.AmqpClientProperties;

@RestController
@RequestMapping("/amqp")
public class AmqpController {
	
	@Autowired
	@Qualifier("amqpClientProperties")
	AmqpClientProperties properties;	
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	private static final Logger logger = LoggerFactory.getLogger(AmqpController.class);
	
	@RequestMapping(value = "direct/{routingKey}", method=RequestMethod.POST)
	public void sendMessage (@RequestBody String payload, @PathVariable String routingKey,
			HttpServletResponse response) throws Exception { 
		
		logger.info("Processing Message...");
		
		routingKey = routingKey.replace("_", ".");
		
		Message message = MessageBuilder.withBody(payload.getBytes())
				.setContentType(MessageProperties.CONTENT_TYPE_JSON)
				.setContentEncoding(StandardCharsets.UTF_8.name())
				.setHeader(TRACKING_ID, response.getHeader(TRACKING_ID))
				.build();
		
		rabbitTemplate.send(properties.getDirectExchange(), routingKey, message);
		
		logger.info("Sent message to Exchange " + properties.getDirectExchange() 
			+ " with Routing Key: " + routingKey);
		
		response.setStatus(HttpStatus.ACCEPTED.value());
	}
	
	@RequestMapping(value = "topic/{routingKey}", method=RequestMethod.POST)
	public void publishMessage (@RequestBody String payload, @PathVariable String routingKey,
			HttpServletResponse response) throws Exception {
		
		logger.info("Processing Message...");
		
		routingKey = routingKey.replace("_", ".");
		
		Message message = MessageBuilder.withBody(payload.getBytes())
				.setContentType(MessageProperties.CONTENT_TYPE_JSON)
				.setContentEncoding(StandardCharsets.UTF_8.name())
				.setHeader(TRACKING_ID, response.getHeader(TRACKING_ID))
				.build();
		
		rabbitTemplate.send(properties.getTopicExchange(), routingKey, message);
		
		logger.info("Published message to Exchange " + properties.getTopicExchange() 
			+ " with Routing Key: " + routingKey);	
		
		response.setStatus(HttpStatus.ACCEPTED.value());
	}
}