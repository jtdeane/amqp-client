package ws.cogito.magic.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import ws.cogito.magic.utilities.web.TrackingIdInterceptor;

@Configuration
public class AmqpWebMvcConfigurerAdapter implements WebMvcConfigurer {
	
	@Autowired
	TrackingIdInterceptor trackingIdInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(trackingIdInterceptor);
	}
	
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }
}