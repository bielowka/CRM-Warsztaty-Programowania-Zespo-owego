import api from '../../config/axios';
import { User } from './types';

export const fetchUsers = async (): Promise<User[]> => {
    const res = await api.get('/api/users');
    return res.data;
};

export const deleteUser = async (id: number) => {
    return api.delete(`/api/users/${id}`);
};

export const createUser = async (userData: {
    firstName: string;
    lastName: string;
    email: string;
    password: string;
    role: string;
    position: string;
}): Promise<User> => {
    const res = await api.post('/api/users', userData);
    return res.data;
};

export const updateUser = async ({ id, ...userData }: {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    password?: string;
    role: string;
    position: string;
}): Promise<User> => {
    const res = await api.put(`/api/users/${id}`, userData);
    return res.data;
};
