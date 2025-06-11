import api from './../config/axios'

// src/types/reports.ts
export interface SalesRankingDTO {
    userId: number;
    firstName: string;
    lastName: string;
    totalAmount: number;
    dealsCount: number;
}

export interface TeamSalesReportDTO {
    teamId: number;
    teamName: string;
    managerId: number;
    managerName: string;
    totalSales: number;
    dealsCount: number;
    teamSize: number;
}

export interface Team {
    id: number;
    name: string;
    manager: {
        id: number;
        firstName: string;
        lastName: string;
    };
}


export const fetchTeams = async () => {
    const response = await api.get(`api/teams`);
    return response.data;
};

export const fetchIndividualReports = async (year: number, month: number) => {
    const response = await api.get(`api/ranking/sales`, {
        params: {year, month}
    });
    return response.data;
};

export const fetchTeamReports = async (year: number, month: number) => {
    const response = await api.get(`api/reports/team-sales`, {
        params: {year, month}
    });
    return response.data;
};