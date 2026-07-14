import { http, normalizeApiError, type ApiError } from '@/lib/http/client'
import {
  mapUserFormToRequest,
  normalizeUserDetailsResponse,
  normalizeUsersListResponse,
} from '@/features/users/api/mappers'
import type { User, UserListResponse, UserRequest } from '@/features/users/types/user'

export interface UsersQuery {
  page: number
  size: number
}

export async function listUsers(query: UsersQuery): Promise<UserListResponse> {
  try {
    const response = await http.get('/api/v1/users', {
      params: query,
    })

    return normalizeUsersListResponse(response.data)
  } catch (error) {
    throw normalizeApiError(error)
  }
}

export async function getUser(id: number): Promise<User> {
  try {
    const response = await http.get(`/api/v1/users/${id}`)
    return normalizeUserDetailsResponse(response.data)
  } catch (error) {
    throw normalizeApiError(error)
  }
}

export async function createUser(payload: UserRequest): Promise<{ message: string }> {
  try {
    const response = await http.post('/api/v1/users', mapUserFormToRequest(payload))
    return { message: response.data?.message ?? 'Usuario criado com sucesso.' }
  } catch (error) {
    throw normalizeApiError(error)
  }
}

export async function updateUser(id: number, payload: UserRequest): Promise<{ message: string }> {
  try {
    const response = await http.patch(`/api/v1/users/${id}`, mapUserFormToRequest(payload))
    return { message: response.data?.message ?? 'Usuario atualizado com sucesso.' }
  } catch (error) {
    throw normalizeApiError(error)
  }
}

export async function deleteUser(id: number): Promise<{ message: string }> {
  try {
    const response = await http.delete(`/api/v1/users/${id}`)
    return { message: response.data?.message ?? 'Usuario excluido com sucesso.' }
  } catch (error) {
    throw normalizeApiError(error)
  }
}

export function asApiError(error: unknown): ApiError {
  return normalizeApiError(error)
}
