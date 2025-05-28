import { UserRole } from '../../types/UserRole';

export type User = {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    role: UserRole;
};