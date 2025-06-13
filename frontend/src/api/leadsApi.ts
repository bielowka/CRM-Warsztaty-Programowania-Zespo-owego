import api from './../config/axios'
import { Account } from './accountsApi';

export type { Account };

export interface Lead {
    id: number;
    description: string;
    status: LeadStatus;
    estimatedValue: number;
    probability: number;
    companyName?: string;
    companyIndustry?: string;
    createdAt: string;
    updatedAt: string;
    account: {
        id: number;
        firstName: string;
        lastName: string;
        company?: {
            id: number;
            name: string;
        };
    };
}

export interface LeadForTable {
    id: number;
    description: string;
    status: string;
    estimatedValue: number;
    companyName?: string;
    companyIndustry?: string;
    createdAt: string;
    updatedAt: string;
}

export enum LeadStatus {
    NEW = "NEW",
    QUALIFICATION = "QUALIFICATION",
    NEEDS_ANALYSIS = "NEEDS_ANALYSIS",
    VALUE_PROPOSITION = "VALUE_PROPOSITION",
    ID_DECISION_MAKERS = "ID_DECISION_MAKERS",
    PERCEPTION_ANALYSIS = "PERCEPTION_ANALYSIS",
    PROPOSAL = "PROPOSAL",
    NEGOTIATION = "NEGOTIATION",
    CLOSED_WON = "CLOSED_WON",
    CLOSED_LOST = "CLOSED_LOST"
}

export interface SortOptions {
    sortBy: string;
    sortDirection: 'asc' | 'desc';
}

export interface PaginationOptions {
    page: number;
    size: number;
}

export interface LeadPageResponse {
    content: LeadForTable[];
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
    first: boolean;
    last: boolean;
    hasNext: boolean;
    hasPrevious: boolean;
}

export const fetchLeads = async (
    sortOptions?: SortOptions, 
    paginationOptions?: PaginationOptions
): Promise<LeadPageResponse> => {
    const params = new URLSearchParams();
    
    if (sortOptions) {
        params.append('sortBy', sortOptions.sortBy);
        params.append('sortDirection', sortOptions.sortDirection);
    }
    
    if (paginationOptions) {
        params.append('page', paginationOptions.page.toString());
        params.append('size', paginationOptions.size.toString());
    }
    
    const response = await api.get(`/api/leads?${params.toString()}`);
    return response.data;
};

export const createLead = async (leadData: any): Promise<Lead> => {
    const response = await api.post(`api/leads`, leadData);
    return response.data;
};

export const updateLeadStatus = async (leadId: number, status: LeadStatus) => {
    const response = await api.post(`api/leads/${leadId}/status`, {status});
    return response.data;
};
