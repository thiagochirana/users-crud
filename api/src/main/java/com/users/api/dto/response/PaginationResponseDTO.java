package com.users.api.dto.response;

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
public class PaginationResponseDTO {
	private int page;
	private int size;

	@JsonProperty("total_elements")
	private long totalElements;

	@JsonProperty("total_pages")
	private int totalPages;
}
