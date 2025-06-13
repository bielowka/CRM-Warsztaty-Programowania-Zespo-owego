export enum AccountStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  SUSPENDED = 'SUSPENDED'
}

export const isAccountStatus = (status: string): status is AccountStatus => {
  return Object.values(AccountStatus).includes(status as AccountStatus);
};