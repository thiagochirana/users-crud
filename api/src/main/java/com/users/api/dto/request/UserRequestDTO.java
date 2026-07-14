package com.users.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
	private String name;

	@JsonProperty("birth_date")
	private String birthDate;

	private String document;

	@JsonProperty("address_line")
	private String addressLine;

	@JsonProperty("address_number")
	private String addressNumber;

	private String city;
	private String state;
	private String zip;
}
