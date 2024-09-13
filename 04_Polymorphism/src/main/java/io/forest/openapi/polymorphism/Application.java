package io.forest.openapi.polymorphism;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import io.forest.openapi.polymorphism.conf.ApiConf;
import io.forest.openapi.polymorphism.conf.AppConf;

@SpringBootApplication
@Import({ ApiConf.class, AppConf.class })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
