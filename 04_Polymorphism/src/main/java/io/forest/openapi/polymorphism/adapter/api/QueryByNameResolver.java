package io.forest.openapi.polymorphism.adapter.api;

import java.util.function.Function;

import io.forest.openapi.polymorphism.adapter.api.server.dto.QueryByName;
import io.forest.openapi.polymorphism.app.command.QueryByNameCommand;
import io.forest.openapi.polymorphism.port.QueryHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log4j2
public class QueryByNameResolver implements QueryResolver {

	@NonNull
	QueryHandler queryHandler;

	@Override
	public Mono<Object> resolve(Object o) {

		return Mono.justOrEmpty(o)
				.cast(QueryByName.class)
				.map(toCommand)
				.doOnNext(it -> log.info("Request parameters converted to command [command={}]", it))
				.flatMap(queryHandler::handle);
	}

	Function<QueryByName, Object> toCommand = q -> new QueryByNameCommand().setFirstName(q.getFirstName())
			.setLastName(q.getLastName());

}
