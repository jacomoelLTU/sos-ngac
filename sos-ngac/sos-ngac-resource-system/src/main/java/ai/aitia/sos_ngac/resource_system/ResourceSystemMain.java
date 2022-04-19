package ai.aitia.sos_ngac.resource_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import eu.arrowhead.common.CommonConstants;

@SpringBootApplication
@ComponentScan(basePackages = {CommonConstants.BASE_PACKAGE, ResourceSystemConstants.BASE_PACKAGE})
public class ResourceSystemMain {

	public static void main(final String[] args) {
		SpringApplication.run(ResourceSystemMain.class, args);
	}	
}
