package io.forest.openapi.polymorphism.adapter.api;

import java.util.Optional;
import java.util.function.Predicate;

import io.forest.openapi.polymorphism.common.throwable.UnknownQueryException;

public interface QueryFactory {

	public enum Query {
		QueryByName("QueryByName"),

		QueryByMembership("QueryByMembership");

		String value;

		Query(String value) {
			this.value = value;
		}

		public String toString() {
			return this.value;
		}

		public static Query from(String value) {
			return Optional.ofNullable(value)
					.filter(Predicate.not(String::isBlank))
					.map(String::toUpperCase)
					.map(String::trim)
					.map(it -> switch (it) {
					case "QUERYBYNAME" -> QueryByName;
					case "QUERYBYMEMBERSHIP" -> QueryByMembership;
					default -> null;
					})
					.orElseThrow(() -> new UnknownQueryException(
							String.format("Query type unrecognized [queryType=%s]", value)));
		}

		public interface TypeConstant {
			String QueryByName = "QueryByName";
			String QueryByMembership = "QueryByMembership";
		}

	}

	QueryResolver get(String value);
}
