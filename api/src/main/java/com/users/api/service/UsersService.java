package com.users.api.service;

import com.users.api.dto.response.ApiResponseDTO;
import com.users.api.dto.response.ErrorResponseDTO;
import com.users.api.dto.response.PaginatedResponseDTO;
import com.users.api.dto.response.PaginationResponseDTO;
import com.users.api.dto.request.UserRequestDTO;
import com.users.api.dto.response.UserResponseDTO;
import com.users.api.model.User;
import com.users.api.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
	private static final DateTimeFormatter BIRTH_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private final UserRepository userRepository;

	public UsersService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// GET /users
	public ResponseEntity<?> index(Pageable pageable) {
		Page<UserResponseDTO> usersPage = userRepository.findAll(pageable).map(this::toResponse);

		PaginatedResponseDTO<List<UserResponseDTO>> response = PaginatedResponseDTO.<List<UserResponseDTO>>builder()
				.data(usersPage.getContent())
				.message("Usuários encontrados com sucesso")
				.pagination(PaginationResponseDTO.builder()
						.page(usersPage.getNumber() + 1)
						.size(usersPage.getSize())
						.totalElements(usersPage.getTotalElements())
						.totalPages(usersPage.getTotalPages())
						.build())
				.build();

		return ResponseEntity.ok(response);
	}

    // GET /users/:id
	public ResponseEntity<?> show(Long id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(buildErrorResponse("Usuário não encontrado"));
		}

		return ResponseEntity.ok(ApiResponseDTO.builder()
				.data(toResponse(user.get()))
				.message("Usuário encontrado com sucesso")
				.build());
	}

    // POST /users
	public ResponseEntity<?> create(UserRequestDTO request) {
		Map<String, List<String>> errors = validateCreateRequest(request, null);
		if (!errors.isEmpty()) {
			return buildUnprocessableEntity(errors);
		}

		LocalDateTime now = LocalDateTime.now();
		String cpf = normalizeCpf(request.getDocument().trim());
		User user = User.builder()
				.name(request.getName().trim())
				.birthDate(parseBirthDate(request.getBirthDate().trim()))
				.document(cpf)
				.addressLine(request.getAddressLine().trim())
				.addressNumber(request.getAddressNumber().trim())
				.city(request.getCity().trim())
				.state(request.getState().trim())
				.zip(request.getZip().trim())
				.createdAt(now)
				.updatedAt(now)
				.build();

		userRepository.save(user);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponseDTO.builder()
//						.data(toResponse(savedUser))
						.message("Usuário criado com sucesso")
						.build());
	}

    // PATCH /users
	public ResponseEntity<?> update(Long id, UserRequestDTO request) {
		Optional<User> existingUser = userRepository.findById(id);
		if (existingUser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(buildErrorResponse("Usuário não encontrado"));
		}

		Map<String, List<String>> errors = validatePatchRequest(request, id);
		if (!errors.isEmpty()) {
			return buildUnprocessableEntity(errors);
		}

		User user = existingUser.get();
		String name = normalize(request.getName());
		String birthDate = normalize(request.getBirthDate());
		String document = normalizeCpf(request.getDocument());
		String addressLine = normalize(request.getAddressLine());
		String addressNumber = normalize(request.getAddressNumber());
		String city = normalize(request.getCity());
		String state = normalize(request.getState());
		String zip = normalize(request.getZip());

		if (request.getName() != null) {
			user.setName(name);
		}
		if (request.getBirthDate() != null) {
			user.setBirthDate(parseBirthDate(birthDate));
		}
		if (request.getDocument() != null) {
			user.setDocument(document);
		}
		if (request.getAddressLine() != null) {
			user.setAddressLine(addressLine);
		}
		if (request.getAddressNumber() != null) {
			user.setAddressNumber(addressNumber);
		}
		if (request.getCity() != null) {
			user.setCity(city);
		}
		if (request.getState() != null) {
			user.setState(state);
		}
		if (request.getZip() != null) {
			user.setZip(zip);
		}
		user.setUpdatedAt(LocalDateTime.now());

		User savedUser = userRepository.save(user);
		return ResponseEntity.ok(ApiResponseDTO.builder()
//				.data(toResponse(savedUser))
				.message("Usuário atualizado com sucesso")
				.build());
	}

    // DETELE /users
	public ResponseEntity<?> delete(Long id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(buildErrorResponse("Usuário não encontrado"));
		}

		userRepository.delete(user.get());
		return ResponseEntity.ok(ApiResponseDTO.<Void>builder()
				.data(null)
				.message("Usuário deletado com sucesso")
				.build());
	}

	private Map<String, List<String>> validateCreateRequest(UserRequestDTO request, Long userId) {
		Map<String, List<String>> errors = new LinkedHashMap<>();

		String name = normalize(request.getName());
		String birthDate = normalize(request.getBirthDate());
		String document = normalizeCpf(request.getDocument());
		String addressLine = normalize(request.getAddressLine());
		String addressNumber = normalize(request.getAddressNumber());
		String city = normalize(request.getCity());
		String state = normalize(request.getState());
		String zip = normalize(request.getZip());

		validateRequired("name", name, "Nome é obrigatório", 255, "Nome deve ter no máximo 255 caracteres", errors);
		validateRequired("document", document, "CPF é obrigatório", 11, "CPF deve conter 11 dígitos", errors);
		validateRequired("address_line", addressLine, "Endereço é obrigatório", 255, "Endereço deve ter no máximo 255 caracteres", errors);
		validateRequired("address_number", addressNumber, "Número do endereço é obrigatório", 50, "Número do endereço deve ter no máximo 50 caracteres", errors);
		validateRequired("city", city, "Cidade é obrigatória", 255, "Cidade deve ter no máximo 255 caracteres", errors);
		validateRequired("state", state, "Estado é obrigatório", 100, "Estado deve ter no máximo 100 caracteres", errors);
		validateRequired("zip", zip, "CEP é obrigatório", 20, "CEP deve ter no máximo 20 caracteres", errors);

		if (document != null && !isValidCpf(document)) {
			addError(errors, "document", "CPF deve ser válido");
		}

		if (birthDate == null) {
			addError(errors, "birth_date", "Data de nascimento é obrigatória");
		} else {
			try {
				validateBirthDate(parseBirthDate(birthDate), "birth_date", errors);
			} catch (DateTimeParseException exception) {
				addError(errors, "birth_date", "Data de nascimento deve ser uma data válida no formato dd/MM/yyyy");
			}
		}

		if (document != null) {
			Optional<User> existingUser = userRepository.findByDocument(document);
			if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
				addError(errors, "document", "CPF já existe");
			}
		}

		return errors;
	}

	private Map<String, List<String>> validatePatchRequest(UserRequestDTO request, Long userId) {
		Map<String, List<String>> errors = new LinkedHashMap<>();

		validateOptional("name", request.getName(), normalize(request.getName()), "Nome é obrigatório", 255,
				"Nome deve ter no máximo 255 caracteres", errors);
		validateOptional("document", request.getDocument(), normalizeCpf(request.getDocument()), "CPF é obrigatório", 11,
				"CPF deve conter 11 dígitos", errors);
		validateOptional("address_line", request.getAddressLine(), normalize(request.getAddressLine()), "Endereço é obrigatório", 255,
				"Endereço deve ter no máximo 255 caracteres", errors);
		validateOptional("address_number", request.getAddressNumber(), normalize(request.getAddressNumber()), "Número do endereço é obrigatório", 50,
				"Número do endereço deve ter no máximo 50 caracteres", errors);
		validateOptional("city", request.getCity(), normalize(request.getCity()), "Cidade é obrigatória", 255,
				"Cidade deve ter no máximo 255 caracteres", errors);
		validateOptional("state", request.getState(), normalize(request.getState()), "Estado é obrigatório", 100,
				"Estado deve ter no máximo 100 caracteres", errors);
		validateOptional("zip", request.getZip(), normalize(request.getZip()), "CEP é obrigatório", 20,
				"CEP deve ter no máximo 20 caracteres", errors);

		if (request.getBirthDate() != null) {
			String birthDate = normalize(request.getBirthDate());
			if (birthDate == null) {
				addError(errors, "birth_date", "Data de nascimento é obrigatória");
			} else {
				try {
					validateBirthDate(parseBirthDate(birthDate), "birth_date", errors);
				} catch (DateTimeParseException exception) {
					addError(errors, "birth_date", "Data de nascimento deve ser uma data válida no formato dd/MM/yyyy");
				}
			}
		}

		if (request.getDocument() != null) {
			String document = normalizeCpf(request.getDocument());
			if (document != null && !isValidCpf(document)) {
				addError(errors, "document", "CPF deve ser válido");
			}
		}

		if (request.getDocument() != null) {
			String document = normalizeCpf(request.getDocument());
			if (document != null) {
				Optional<User> existingUser = userRepository.findByDocument(document);
				if (existingUser.isPresent() && !existingUser.get().getId().equals(userId)) {
					addError(errors, "document", "CPF já existe");
				}
			}
		}

		return errors;
	}

	private void validateRequired(String field, String value, String requiredMessage, int maxLength, String lengthMessage,
			Map<String, List<String>> errors) {
		if (value == null) {
			addError(errors, field, requiredMessage);
			return;
		}

		if (value.length() > maxLength) {
			addError(errors, field, lengthMessage);
		}
	}

	private void validateOptional(String field, String originalValue, String normalizedValue, String requiredMessage, int maxLength,
			String lengthMessage, Map<String, List<String>> errors) {
		if (originalValue == null) {
			return;
		}

		if (normalizedValue == null) {
			addError(errors, field, requiredMessage);
			return;
		}

		if (normalizedValue.length() > maxLength) {
			addError(errors, field, lengthMessage);
		}
	}

	private String normalize(String value) {
		if (value == null) {
			return null;
		}

		String trimmedValue = value.trim();
		return trimmedValue.isEmpty() ? null : trimmedValue;
	}

	private String normalizeCpf(String value) {
		String normalizedValue = normalize(value);
		if (normalizedValue == null) {
			return null;
		}

		String cpf = normalizedValue.replaceAll("\\D", "");
		return cpf.isEmpty() ? null : cpf;
	}

	private boolean isValidCpf(String cpf) {
		if (cpf == null || cpf.length() != 11 || cpf.chars().distinct().count() == 1) {
			return false;
		}

		return calculateCpfDigit(cpf, 9) == Character.getNumericValue(cpf.charAt(9))
				&& calculateCpfDigit(cpf, 10) == Character.getNumericValue(cpf.charAt(10));
	}

	private int calculateCpfDigit(String cpf, int length) {
		int sum = 0;
		for (int index = 0; index < length; index++) {
			sum += Character.getNumericValue(cpf.charAt(index)) * ((length + 1) - index);
		}

		int remainder = sum % 11;
		return remainder < 2 ? 0 : 11 - remainder;
	}

	private LocalDate parseBirthDate(String value) {
		return LocalDate.parse(value, BIRTH_DATE_FORMATTER);
	}

	private void validateBirthDate(LocalDate birthDate, String field, Map<String, List<String>> errors) {
		if (birthDate.isAfter(LocalDate.now())) {
			addError(errors, field, "Data de nascimento deve ser anterior ou igual a hoje");
		}
	}

	private void addError(Map<String, List<String>> errors, String field, String message) {
		errors.computeIfAbsent(field, key -> new ArrayList<>()).add(message);
	}

	private ResponseEntity<ErrorResponseDTO> buildUnprocessableEntity(Map<String, List<String>> errors) {
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ErrorResponseDTO.builder().message("Erro de validação").errors(errors).build());
	}

	private ErrorResponseDTO buildErrorResponse(String message) {
		Map<String, List<String>> errors = new LinkedHashMap<>();
		addError(errors, "_general", message);
		return ErrorResponseDTO.builder().message(message).errors(errors).build();
	}

	private UserResponseDTO toResponse(User user) {
		return UserResponseDTO.builder()
				.id(user.getId())
				.name(user.getName())
				.birthDate(user.getBirthDate())
				.document(user.getDocument())
				.addressLine(user.getAddressLine())
				.addressNumber(user.getAddressNumber())
				.city(user.getCity())
				.state(user.getState())
				.zip(user.getZip())
				.createdAt(user.getCreatedAt())
				.updatedAt(user.getUpdatedAt())
				.build();
	}
}
