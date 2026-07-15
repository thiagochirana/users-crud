import type { User, UserField, UserFieldErrors, UserListResponse, UserRequest } from '@/features/users/types/user'

type UnknownRecord = Record<string, unknown>

function toRecord(value: unknown): UnknownRecord {
  return value && typeof value === 'object' ? (value as UnknownRecord) : {}
}

function readString(value: unknown) {
  return typeof value === 'string' ? value : ''
}

function toFormDate(value: string) {
  if (!value) return ''
  if (/^\d{4}-\d{2}-\d{2}$/.test(value)) return value

  const match = value.match(/^(\d{2})\/(\d{2})\/(\d{4})$/)
  if (match) {
    const [, day, month, year] = match
    return `${year}-${month}-${day}`
  }

  return ''
}

function toApiBirthDate(value: string) {
  const match = value.match(/^(\d{4})-(\d{2})-(\d{2})$/)
  if (!match) return value

  const [, year, month, day] = match
  return `${day}/${month}/${year}`
}

function normalizeUserShape(input: unknown) {
  const source = toRecord(input)

  return {
    id: Number(source.id ?? 0),
    name: readString(source.name),
    birth_date: toFormDate(readString(source.birth_date ?? source.birthDate)),
    document: readString(source.document),
    address_line: readString(source.address_line ?? source.addressLine),
    address_number: readString(source.address_number ?? source.addressNumber),
    city: readString(source.city),
    state: readString(source.state),
    zip: readString(source.zip),
    created_at: readString(source.created_at ?? source.createdAt),
    updated_at: readString(source.updated_at ?? source.updatedAt),
  } satisfies User
}

function normalizePagination(input: unknown) {
  const source = toRecord(input)

  return {
    page: Number(source.page ?? 1),
    size: Number(source.size ?? 10),
    total_elements: Number(source.total_elements ?? source.totalElements ?? 0),
    total_pages: Number(source.total_pages ?? source.totalPages ?? 0),
  }
}

export function mapUserResponseToForm(input: unknown): UserRequest {
  const user = normalizeUserShape(input)

  return {
    name: user.name,
    birth_date: user.birth_date,
    document: user.document,
    address_line: user.address_line,
    address_number: user.address_number,
    city: user.city,
    state: user.state,
    zip: user.zip,
  }
}

export function mapUserFormToRequest(form: UserRequest): UserRequest {
  return {
    ...form,
    document: normalizeDocument(form.document),
    zip: form.zip.replace(/\D/g, ''),
    state: form.state.trim().toUpperCase(),
    birth_date: toApiBirthDate(form.birth_date),
  }
}

export function normalizeUserDetailsResponse(input: unknown): User {
  const source = toRecord(input)
  const userData = 'data' in source ? source.data : source
  return normalizeUserShape(userData)
}

export function normalizeUsersListResponse(input: unknown): UserListResponse {
  const source = toRecord(input)
  const data = Array.isArray(source.data) ? source.data.map(normalizeUserShape) : []

  return {
    data,
    message: readString(source.message),
    pagination: normalizePagination(source.pagination),
  }
}

export function normalizeDocument(value: string) {
  return value.replace(/[^a-zA-Z0-9]/g, '').toUpperCase()
}

export function formatDocument(value: string) {
  const normalized = normalizeDocument(value)

  if (!/^\d{11}$/.test(normalized)) {
    return normalized
  }

  return normalized
    .replace(/(\d{3})(\d)/, '$1.$2')
    .replace(/(\d{3})(\d)/, '$1.$2')
    .replace(/(\d{3})(\d{1,2})$/, '$1-$2')
}

export function formatZip(value: string) {
  const digits = value.replace(/\D/g, '').slice(0, 8)
  return digits.replace(/(\d{5})(\d{1,3})$/, '$1-$2')
}

export function mapApiErrorsToFieldErrors(fieldErrors: Record<string, string[]>): UserFieldErrors {
  const allowedFields: UserField[] = [
    'name',
    'birth_date',
    'document',
    'address_line',
    'address_number',
    'city',
    'state',
    'zip',
  ]

  return Object.fromEntries(
    Object.entries(fieldErrors)
      .filter(([key, value]) => allowedFields.includes(key as UserField) && Array.isArray(value) && value.length > 0)
      .map(([key, value]) => [key, value]),
  ) as UserFieldErrors
}
