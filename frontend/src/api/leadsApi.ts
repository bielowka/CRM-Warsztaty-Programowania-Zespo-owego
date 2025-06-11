import api from './../config/axios'


export interface Lead {
    id: number;
    description: string;
    status: LeadStatus;
    estimatedValue: number;
    probability: number;
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

export interface Account {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    phoneNumber: string;
    accountStatus: AccountStatus;
    company?: {
        id: number;
        name: string;
        industry: string;
    };
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

export enum AccountStatus {
    ACTIVE = "ACTIVE",
    PENDING = "PENDING",
    INACTIVE = "INACTIVE"
}

export const fetchLeads = async (): Promise<Lead[]> => {
    const response = await api.get('api/leads');
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
