package com.mycompany.knowledge.miami.publish.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@ComponentScan("com.mycompany.knowledge.miami")
@EnableEurekaClient
@SpringBootApplication
@EnableJpaRepositories("com.mycompany.knowledge.miami.publish.repository")
@EntityScan("com.mycompany.knowledge.miami.publish.model.gongan")
//@EnableJpaAuditing
public class MiamiPublishApplication {
	public static void main(String[] args) {
		SpringApplication.run(MiamiPublishApplication.class, args);
	}
}
