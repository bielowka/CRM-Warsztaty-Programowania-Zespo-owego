import api from './../config/axios'
import { NoteType } from './notesApi'

export interface Lead {
    id: number;
    description: string;
    status: string;
    estimatedValue: number;
    createdAt: string;
    updatedAt: string;
}

export interface Account {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    phoneNumber: string;
    accountStatus: 'ACTIVE' | 'INACTIVE' | 'SUSPENDED';
    company?: {
        id: number;
        name: string;
        industry: string;
    };
    leads?: Lead[];
    notes?: Note[];
}

export interface Note {
    id: number;
    content: string;
    noteType: NoteType;
    noteDate: string;
    createdAt: string;
    updatedAt: string;
}

export const fetchMyAccounts = async (): Promise<Account[]> => {
    const response = await api.get(`api/accounts/my`);
    return response.data;
};