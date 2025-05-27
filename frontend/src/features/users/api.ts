import axios from 'axios';
import { User } from './types';

export const fetchUsers = async (): Promise<User[]> => {
    const res = await axios.get('http://localhost:8080/api/users');
    return res.data;
};

export const deleteUser = async (id: number) => {
    return axios.delete(`http://localhost:8080/api/users/${id}`);
};
