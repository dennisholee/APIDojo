package io.forest.openapi.polymorphism.app.dto;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Getter
@Setter(AccessLevel.PACKAGE)
@EqualsAndHashCode
@ToString
@Builder
@Accessors(chain = true)
public class UserDTO {

	String firstName;

	String lastName;

	String membershipId;

	LocalDate dateOfBirth;
}
