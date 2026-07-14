package com.users.api.controller;

import com.users.api.dto.request.UserRequestDTO;
import com.users.api.service.UsersService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

@Validated
@RestController
@RequestMapping("/api/v1/users")
public class UsersController {
	private final UsersService usersService;

	public UsersController(UsersService usersService) {
		this.usersService = usersService;
	}

	@GetMapping
	public Object index(
			@RequestParam(required = false) @Min(value = 1, message = "Page deve ser maior ou igual a 1") Integer page,
			@RequestParam(required = false) @Min(value = 1, message = "Size deve ser maior ou igual a 1")
			@Max(value = 100, message = "Size deve ser menor ou igual a 100") Integer size,
			@PageableDefault(size = 10, sort = "id") Pageable pageableRequest) {
		return usersService.index(pageableRequest);
	}

	@GetMapping("/{id}")
	public Object show(@PathVariable Long id) {
		return usersService.show(id);
	}

	@PostMapping
	public Object create(@RequestBody UserRequestDTO request) {
		return usersService.create(request);
	}

	@PatchMapping("/{id}")
	public Object update(@PathVariable Long id, @RequestBody UserRequestDTO request) {
		return usersService.update(id, request);
	}

	@DeleteMapping("/{id}")
	public Object delete(@PathVariable Long id) {
		return usersService.delete(id);
	}
}
