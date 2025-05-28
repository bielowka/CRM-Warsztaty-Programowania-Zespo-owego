import api from '../../config/axios';
import { User } from './types';

export const fetchUsers = async (): Promise<User[]> => {
    const res = await api.get('/api/users');
    return res.data;
};

export const deleteUser = async (id: number) => {
    return api.delete(`/api/users/${id}`);
};
