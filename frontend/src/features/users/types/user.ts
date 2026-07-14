export interface UserRequest {
  name: string
  birth_date: string
  document: string
  address_line: string
  address_number: string
  city: string
  state: string
  zip: string
}

export interface User extends UserRequest {
  id: number
  created_at: string
  updated_at: string
}

export interface UserListResponse {
  data: User[]
  pagination: {
    page: number
    size: number
    total_elements: number
    total_pages: number
  }
  message: string
}

export type UserField = keyof UserRequest

export type UserFieldErrors = Partial<Record<UserField, string[]>>

export const initialUserForm: UserRequest = {
  name: '',
  birth_date: '',
  document: '',
  address_line: '',
  address_number: '',
  city: '',
  state: '',
  zip: '',
}
