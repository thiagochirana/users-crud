package com.users.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class UserResponseDTO {
	private Long id;
	private String name;

	@JsonProperty("birth_date")
	private LocalDate birthDate;

	private String document;

	@JsonProperty("address_line")
	private String addressLine;

	@JsonProperty("address_number")
	private String addressNumber;

	private String city;
	private String state;
	private String zip;

	@JsonProperty("created_at")
	private LocalDateTime createdAt;

	@JsonProperty("updated_at")
	private LocalDateTime updatedAt;
}
