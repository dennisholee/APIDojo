package io.forest.openapi.polymorphism.app;

import java.time.LocalDate;
import java.util.UUID;
import java.util.function.Function;

import io.forest.openapi.polymorphism.app.command.QueryByNameCommand;
import io.forest.openapi.polymorphism.app.dto.UserDTO;
import io.forest.openapi.polymorphism.port.QueryHandler;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
public class QueryByNameApp implements QueryHandler {

	@Override
	public Mono<Object> handle(Object o) {

		return Mono.justOrEmpty(o)
				.cast(QueryByNameCommand.class)
				.doOnNext(it -> log.info("Handling command request [command={}]", it))
				.map(toDTO);
	}

	Function<QueryByNameCommand, UserDTO> toDTO = dto -> UserDTO.builder()
			.firstName("John")
			.lastName("Smith")
			.dateOfBirth(LocalDate.now())
			.membershipId(UUID.randomUUID()
					.toString())
			.build();
}
