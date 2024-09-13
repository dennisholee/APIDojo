package io.forest.openapi.polymorphism.port;

import reactor.core.publisher.Mono;

public interface QueryHandler {

	Mono<Object> handle(Object o);
}
