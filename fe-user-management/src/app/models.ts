export interface LoginRequest {
  username: string,
  password: string
}

export type Permissions = {
  canReadUser: 0 | 1,
  canCreateUser: 0 | 1,
  canUpdateUser: 0 | 1,
  canDeleteUser: 0 | 1,
  canSearchMachine: 0 | 1,
  canStartMachine: 0 | 1,
  canStopMachine: 0 | 1,
  canRestartMachine: 0 | 1,
  canCreateMachine: 0 | 1,
  canDestroyMachine: 0 | 1,
}

export interface User {
  id: number,
  name: string,
  surname: string,
  email: string,
  permission: Permissions
}