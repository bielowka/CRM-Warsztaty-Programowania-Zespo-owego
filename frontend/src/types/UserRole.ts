export enum UserRole {
  ADMIN = 'ADMIN',
  MANAGER = 'MANAGER',
  SALESPERSON = 'SALESPERSON'
}

export const isUserRole = (role: string): role is UserRole => {
  return Object.values(UserRole).includes(role as UserRole);
}; 