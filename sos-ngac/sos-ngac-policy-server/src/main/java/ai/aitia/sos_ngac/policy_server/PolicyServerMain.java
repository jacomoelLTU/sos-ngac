package ai.aitia.sos_ngac.policy_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import eu.arrowhead.common.CommonConstants;

@SpringBootApplication
@ComponentScan(basePackages = {CommonConstants.BASE_PACKAGE, PolicyServerConstants.BASE_PACKAGE})
public class PolicyServerMain {

	public static void main(final String[] args) {
		SpringApplication.run(PolicyServerMain.class, args);
	}	
}
