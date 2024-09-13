package io.forest.openapi.polymorphism.adapter.api;

import reactor.core.publisher.Mono;

public interface QueryResolver {

	Mono<Object> resolve(Object o);

}
