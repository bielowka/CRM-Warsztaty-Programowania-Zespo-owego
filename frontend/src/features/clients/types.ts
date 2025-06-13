import { User } from './types';
import { Address } from './types';
import { AccountStatus } from './types';
import { Company } from './types';

export type Client = {
id: number;
user: User;
firstName: string;
lastName: string;
email: string;
address: Address;
accountStatus: AccountStatus;
phoneNumber: string;
company: Company;
};