package io.forest.openapi.polymorphism.adapter.api;

import java.util.function.Function;

import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ServerWebExchange;

import io.forest.openapi.polymorphism.adapter.api.QueryFactory.Query;
import io.forest.openapi.polymorphism.adapter.api.server.UsersApiDelegate;
import io.forest.openapi.polymorphism.adapter.api.server.dto.QueryUserRequest;
import io.forest.openapi.polymorphism.adapter.api.server.dto.QueryUserResponse;
import io.forest.openapi.polymorphism.adapter.api.server.dto.QueryUserResult;
import io.forest.openapi.polymorphism.adapter.api.server.dto.Status;
import io.forest.openapi.polymorphism.app.dto.UserDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log4j2
public class UserApiDelegateAdapter implements UsersApiDelegate {

	@NonNull
	QueryFactory queryFactory;

	@Override
	public Mono<ResponseEntity<QueryUserResponse>> queryUserProfile(Mono<QueryUserRequest> queryUserRequest,
																	ServerWebExchange exchange) {
		return queryUserRequest.flatMap(it -> {
			Query query = Query.from(it.getQueryType());
			return queryFactory.get(query.value)
					.resolve(it);
		})
				.cast(UserDTO.class)
				.map(toRsp)
				.map(r -> ResponseEntity.ok()
						.body(r))
				.switchIfEmpty(Mono.just(ResponseEntity.notFound()
						.build()))
				.onErrorResume(it -> Mono.just(it)
						.map(toErrorRsp)
						.map(r -> ResponseEntity.badRequest()
								.body(r)));

//		return null;
	}

//	public Mono<ResponseEntity<QueryUserResponse>> queryUserProfile(Mono<QueryUserProfileRequest> queryUserProfileRequest,
//																	ServerWebExchange exchange) {
//
//		return queryUserProfileRequest.log()
//				.map(QueryUserProfileRequest::getQueryType)
//				.doOnNext(it -> log.info("Query type detected [queryType={}]", it))
//				.flatMap(this::from)
//				.doOnNext(it -> log.info("Query type is recognized [queryType={}]", it))
//				.map(queryFactory::get)
//				.doOnNext(it -> log.info("Query resolver found [resolver={}]",
//						it.getClass()
//								.getCanonicalName()))
////				.flatMap(it -> {
//////					return queryUserProfileRequest.flatMap(it::resolve)
//////							.doOnNext(r -> log.info("Query result [result={}]", r))
//////							.cast(UserDTO.class)
//////							.doOnNext(r -> log.info("Cast result [result={}]", r));
////					Mono<Object> resolve = it.resolve(queryUserProfileRequest)
////							.doOnNext(r -> log.info(">>>>>>>> {}", r));
////					UserDTO dto = UserDTO.builder()
////							.firstName("John")
////							.lastName("Smith")
////							.dateOfBirth(LocalDate.now())
////							.membershipId(UUID.randomUUID()
////									.toString())
////							.build();
////					return Mono.just(dto);
////				})
//				.flatMap(it -> queryUserProfileRequest.log()
//						.cast(QueryByName.class)
//						.doOnNext(r -> log.info("Firstname={}, Lastname={}", r.getFirstName(), r.getLastName()))
//						.flatMap(it::resolve)
//						.cast(UserDTO.class)
//						.defaultIfEmpty(null))
//
//				.map(toRsp)
//				.doOnNext(it -> log.info("Result obtained [result={}]", it))
//				.map(r -> ResponseEntity.ok()
//						.body(r))
//				.switchIfEmpty(Mono.just(ResponseEntity.notFound()
//						.build()))
//				.onErrorResume(it -> Mono.just(it)
//						.map(toErrorRsp)
//						.map(r -> ResponseEntity.badRequest()
//								.body(r)));
//
////		return null;
//	}

//	Mono<String> from(String value) {
//		try {
//			return Mono.justOrEmpty(value)
//					.map(Query::from)
//					.map(Query::toString);
//		} catch (UnknownQueryException e) {
//			return Mono.error(e);
//		}
//	}
//
	Function<UserDTO, QueryUserResponse> toRsp = dto -> {
		QueryUserResult queryUserResult = new QueryUserResult();
		queryUserResult.setFirstName(dto.getFirstName());
		queryUserResult.setLastName(dto.getLastName());
		queryUserResult.setEmail("john.smith@foo.com");
		queryUserResult.setMembershipId("A0000001");
		queryUserResult.setId("0f1b0a0a-0c57-4323-8c9a-ebc6f345e1f2");
		queryUserResult.setVersion(JsonNullable.of("1a7533cf-8f19-43d3-9dde-8d181450c263"));

		Status status = new Status();
		status.code("OK");
		status.message("Request successfully completed.");
		QueryUserResponse response = new QueryUserResponse();
		response.setUser(queryUserResult);
		response.setStatus(status);

		return response;
	};

	Function<Throwable, QueryUserResponse> toErrorRsp = t -> {

		Status status = new Status();
		status.setCode("BAD");
		status.setMessage(t.getMessage());

		QueryUserResponse response = new QueryUserResponse();
		response.setStatus(status);

		return response;
	};
//
//	Supplier<UserDTO> createUserDTO = () -> UserDTO.builder()
//			.firstName("John")
//			.lastName("Smith")
//			.dateOfBirth(LocalDate.now())
//			.membershipId(UUID.randomUUID()
//					.toString())
//			.build();

}
