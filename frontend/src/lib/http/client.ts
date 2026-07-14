import axios from 'axios'

export interface ApiError {
  status: number | null
  message: string
  errors: string[]
  fieldErrors: Record<string, string[]>
}

function isApiError(error: unknown): error is ApiError {
  if (!error || typeof error !== 'object') {
    return false
  }

  const candidate = error as Partial<ApiError>

  return (
    'message' in candidate &&
    typeof candidate.message === 'string' &&
    'errors' in candidate &&
    Array.isArray(candidate.errors) &&
    'fieldErrors' in candidate &&
    typeof candidate.fieldErrors === 'object' &&
    candidate.fieldErrors !== null &&
    'status' in candidate
  )
}

function normalizeFieldErrors(input: unknown) {
  if (!input || typeof input !== 'object' || Array.isArray(input)) {
    return {}
  }

  return Object.fromEntries(
    Object.entries(input).map(([key, value]) => [
      key,
      Array.isArray(value) ? value.filter((item): item is string => typeof item === 'string') : [],
    ]),
  )
}

function flattenFieldErrors(fieldErrors: Record<string, string[]>) {
  return Object.values(fieldErrors).flat()
}

export const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  headers: {
    Accept: 'application/json',
    'Content-Type': 'application/json',
  },
})

export function normalizeApiError(error: unknown): ApiError {
  if (isApiError(error)) {
    return error
  }

  if (!axios.isAxiosError(error)) {
    return {
      status: null,
      message: 'Ocorreu um erro inesperado. Tente novamente.',
      errors: [],
      fieldErrors: {},
    }
  }

  const status = error.response?.status ?? null
  const responseData = error.response?.data as { errors?: unknown; message?: unknown } | undefined
  const fieldErrors = normalizeFieldErrors(responseData?.errors)
  const errors = Array.isArray(responseData?.errors)
    ? responseData.errors.filter((item): item is string => typeof item === 'string')
    : flattenFieldErrors(fieldErrors)

  if (status === null) {
    return {
      status,
      message: 'Nao foi possivel conectar com a API. Verifique a configuracao e tente novamente.',
      errors,
      fieldErrors,
    }
  }

  if (typeof responseData?.message === 'string' && responseData.message.trim()) {
    return { status, message: responseData.message, errors, fieldErrors }
  }

  if (errors.length > 0) {
    return {
      status,
      message: errors[0] ?? 'Nao foi possivel concluir a solicitacao.',
      errors,
      fieldErrors,
    }
  }

  if (status === 404) {
    return { status, message: 'Registro nao encontrado.', errors, fieldErrors }
  }

  if (status >= 500) {
    return { status, message: 'A API encontrou um erro interno. Tente novamente mais tarde.', errors, fieldErrors }
  }

  return { status, message: 'Nao foi possivel concluir a solicitacao.', errors, fieldErrors }
}
