package io.forest.openapi.polymorphism.conf;

import org.springframework.context.annotation.Bean;

import io.forest.openapi.polymorphism.app.QueryByNameApp;
import io.forest.openapi.polymorphism.port.QueryHandler;

public class AppConf {

	@Bean("QueryByNameApp")
	QueryHandler queryByNameApp() {
		return new QueryByNameApp();
	}
	
	@Bean("QueryByMembershipApp")
	QueryHandler queryByMembershipApp() {
		return new QueryByNameApp();
	}
}
