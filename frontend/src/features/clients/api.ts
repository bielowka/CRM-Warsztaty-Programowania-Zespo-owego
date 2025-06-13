import api from '../../config/axios';
import { Client } from './types';
import { User } from './types';
import { AccountStatus } from './types';

export const fetchClients = async (): Promise<Client[]> => {
    const res = await api.get('/api/accounts');
    return res.data;
};

export const GetAccountById = async (id: number) => {
    return api.get(`/api/accounts/${id}`);
};

export const GetAccountByUser = async (user: User) => {
    return api.get(`/api/accounts/user/${user}`);
};

export const GetAccountByUserIdAndStatus = async (user: User, status: AccountStatus) => {
    return api.get(`/api/accounts/user/${user}/status/${status}`);
};

export const createClient = async (clientData: {
    id: number;
    user: User;
    firstName: string;
    lastName: string;
    email: string;
    address: Address;
    accountStatus: AccountStatus;
    phoneNumber: string;
    company: Company;
}): Promise<Client> => {
    const res = await api.post('/api/accounts', clientData);
    return res.data;
};

export const updateClient = async ({ id, ...clientData }: {
    id: number;
    user: User;
    firstName: string;
    lastName: string;
    email: string;
    address: Address;
    accountStatus: AccountStatus;
    phoneNumber: string;
    company: Company;
}): Promise<Client> => {
    const res = await api.put(`/api/accounts/${id}`, clientData);
    return res.data;
};

export const deleteClient = async (id: number) => {
    return api.delete(`/api/accounts/${id}`);
};

