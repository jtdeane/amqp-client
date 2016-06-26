package ws.cogito.magic.messaging;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import junit.framework.TestCase;


public class ConsumerFunctionalTest extends TestCase {
	
	private static final Logger logger = LoggerFactory.getLogger(ConsumerFunctionalTest.class);
	
	private static final String EXCHANGE ="test.direct";
	private static final String QUEUE_NAME = "magic.greetings";
	private static final String QUEUE_BINDING = "ws.cogito.magic.greetings";
	
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
    
    public void testReceiveMessage() throws Exception {
    	
    	//assumes a message is on the queue...
    	GetResponse response = channel.basicGet(QUEUE_NAME, true);
    	
    	logger.info("Message Properties: " + response.toString() + "/n");
    	
    	String body = new String (response.getBody(), StandardCharsets.UTF_8.name());
    	
    	logger.info("Message Body: " + body);
    }
}   