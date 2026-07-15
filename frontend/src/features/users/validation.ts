import type { UserFieldErrors, UserRequest } from '@/features/users/types/user'

function isBlank(value: string) {
  return value.trim().length === 0
}

function isValidDate(value: string) {
  const match = value.match(/^(\d{4})-(\d{2})-(\d{2})$/)
  if (!match) return false

  const [, year, month, day] = match
  const parsedYear = Number(year)
  const parsedMonth = Number(month)
  const parsedDay = Number(day)
  const date = new Date(Date.UTC(parsedYear, parsedMonth - 1, parsedDay))

  if (Number.isNaN(date.getTime())) {
    return false
  }

  return (
    date.getUTCFullYear() === parsedYear &&
    date.getUTCMonth() === parsedMonth - 1 &&
    date.getUTCDate() === parsedDay
  )
}

function isFutureDate(value: string) {
  const today = new Date()
  const currentDate = new Date(today.getFullYear(), today.getMonth(), today.getDate())
  const selectedDate = new Date(`${value}T00:00:00`)

  return selectedDate > currentDate
}

function appendError(errors: UserFieldErrors, field: keyof UserRequest, message: string) {
  errors[field] = [...(errors[field] ?? []), message]
}

export function validateUserForm(form: UserRequest): UserFieldErrors {
  const errors: UserFieldErrors = {}
  const documentDigits = form.document.replace(/\D/g, '')

  if (isBlank(form.name)) appendError(errors, 'name', 'Nome e obrigatorio.')
  if (isBlank(form.birth_date)) appendError(errors, 'birth_date', 'Data de nascimento e obrigatoria.')
  if (isBlank(form.document)) appendError(errors, 'document', 'Documento e obrigatorio.')
  if (isBlank(form.address_line)) appendError(errors, 'address_line', 'Endereco e obrigatorio.')
  if (isBlank(form.address_number)) appendError(errors, 'address_number', 'Numero do endereco e obrigatorio.')
  if (isBlank(form.city)) appendError(errors, 'city', 'Cidade e obrigatoria.')
  if (isBlank(form.state)) appendError(errors, 'state', 'Estado e obrigatorio.')
  if (isBlank(form.zip)) appendError(errors, 'zip', 'CEP e obrigatorio.')

  if (form.birth_date && !isValidDate(form.birth_date)) {
    appendError(errors, 'birth_date', 'Informe uma data valida.')
  } else if (form.birth_date && isFutureDate(form.birth_date)) {
    appendError(errors, 'birth_date', 'A data de nascimento nao pode ser futura.')
  }

  if (form.document && documentDigits.length !== 11) {
    appendError(errors, 'document', 'Informe um CPF com 11 digitos.')
  }

  if (form.zip && form.zip.replace(/\D/g, '').length !== 8) {
    appendError(errors, 'zip', 'Informe um CEP com 8 digitos.')
  }

  if (form.state && !/^[A-Za-z]{2}$/.test(form.state.trim())) {
    appendError(errors, 'state', 'Informe a sigla do estado com 2 letras.')
  }

  return errors
}
