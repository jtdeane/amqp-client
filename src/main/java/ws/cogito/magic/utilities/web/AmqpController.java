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
	
	@RequestMapping(value = "/{exchange}/{routingKey}", method=RequestMethod.POST)
	public void sendMessage (@RequestBody String payload, @PathVariable String routingKey,
			@PathVariable String exchange, HttpServletResponse response) throws Exception { 
		
		logger.info("Processing Message...");
		
		Message message = MessageBuilder.withBody(payload.getBytes())
				.setContentType(MessageProperties.CONTENT_TYPE_JSON)
				.setContentEncoding(StandardCharsets.UTF_8.name())
				.setHeader(TRACKING_ID, response.getHeader(TRACKING_ID))
				.build();
		
		rabbitTemplate.send(exchange, routingKey, message);
		
		logger.info("Sent message to Exchange " + exchange
			+ " with Routing Key: " + routingKey);
		
		response.setStatus(HttpStatus.ACCEPTED.value());
	}
}