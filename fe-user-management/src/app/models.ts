export interface LoginRequest {
  username: string,
  password: string
}

export type Permissions = {
  canReadUser: 0 | 1,
  canCreateUser: 0 | 1,
  canUpdateUser: 0 | 1,
  canDeleteUser: 0 | 1
}

export interface User {
  name: string,
  surname: string,
  email: string,
  permission: Permissions
}