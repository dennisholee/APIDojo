package io.forest.openapi.polymorphism.conf;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;

import io.forest.openapi.polymorphism.adapter.api.QueryByNameResolver;
import io.forest.openapi.polymorphism.adapter.api.QueryFactory;
import io.forest.openapi.polymorphism.adapter.api.QueryFactory.Query;
import io.forest.openapi.polymorphism.adapter.api.QueryResolver;
import io.forest.openapi.polymorphism.adapter.api.UserApiDelegateAdapter;
import io.forest.openapi.polymorphism.adapter.api.server.UsersApiDelegate;
import io.forest.openapi.polymorphism.app.QueryByNameApp;
import io.forest.openapi.polymorphism.port.QueryHandler;
import lombok.NonNull;

public class ApiConf {

	@Bean("requestStateResponseFactory")
	FactoryBean<Object> serviceLocatorFactory() {
		ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
		factoryBean.setServiceLocatorInterface(QueryFactory.class);
		return factoryBean;
	}

	
	@Bean(Query.TypeConstant.QueryByName)
	QueryResolver queryByNameResolver(@Qualifier("QueryByNameApp") QueryHandler queryByNameResolver) {
		return new QueryByNameResolver(queryByNameResolver);
	}
	
	
	@Bean
	UsersApiDelegate apiDelegate(QueryFactory queryFactory) {
		return new UserApiDelegateAdapter(queryFactory);
	}

}
