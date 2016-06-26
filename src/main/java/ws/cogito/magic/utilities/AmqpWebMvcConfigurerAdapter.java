package ws.cogito.magic.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import ws.cogito.magic.utilities.web.TrackingIdInterceptor;

@Configuration
public class AmqpWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {
	
	@Autowired
	TrackingIdInterceptor trackingIdInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(trackingIdInterceptor);
		super.addInterceptors(registry);
	}
}