package ws.cogito.magic.messaging;

import static ws.cogito.magic.utilities.web.TrackingIdInterceptor.TRACKING_ID;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.core.MessageProperties;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import junit.framework.TestCase;

public class ProducerFunctionalTest extends TestCase {
	
	private static final String EXCHANGE ="test.direct";
	private static final String QUEUE_NAME = "magic.greetings";
	private static final String QUEUE_BINDING = "ws.cogito.magic.greetings";
	private static final String ROUTING_KEY = "ws.cogito.magic.greetings";
	
	private ConnectionFactory connectionFactory = null;
	private Channel channel = null;
	private Connection connection = null;
    
    protected void setUp() throws Exception {
    	
        connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        
        connection = connectionFactory.newConnection();
        
        channel = connectionFactory.newConnection().createChannel();
        
        //name, type, durable
        channel.exchangeDeclare(EXCHANGE, "direct", false);
                
        //queue, durable, exclusive, autodelete, arguments
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        
        //queue, exchange, routing key 
        channel.queueBind(QUEUE_NAME, EXCHANGE, QUEUE_BINDING);
    }

    protected void tearDown() throws Exception {          
    	
    	channel.close();
    	connection.close();
    }  
    
    public void testSendMessage() throws Exception {

    	String message = "{\"message\":\"Hello Bunny\"}";
    	
    	// Build message headers
    	Map<String, Object> headers = new HashMap<String,Object>();
    	headers.put(TRACKING_ID, UUID.randomUUID().toString());

    	// Build message properties
    	AMQP.BasicProperties.Builder basicProperties = 
    			new AMQP.BasicProperties.Builder();
    	
    	basicProperties.contentType(MessageProperties.CONTENT_TYPE_JSON)
    		.contentEncoding(StandardCharsets.UTF_8.name())
    		.headers(headers);
    	
    	//exchange, routing-key, props, body
    	channel.basicPublish(EXCHANGE, ROUTING_KEY, basicProperties.build(), 
    			message.getBytes(StandardCharsets.UTF_8.name()));
    }
}