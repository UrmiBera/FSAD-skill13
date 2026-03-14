package klu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry)
	{
		registry.addMapping("/**") //Apply to all end points
				.allowedOrigins("*") //Allow all domains
				.allowedMethods("*") //Allow all methods
				.allowedHeaders("*"); //Allow all headers
	}
}
