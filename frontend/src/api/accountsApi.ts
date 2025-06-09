import api from './../config/axios'
import {Account} from "./leadsApi.ts";

export const fetchMyAccounts = async (): Promise<Account[]> => {
    const response = await api.get(`api/accounts/my`);
    return response.data;
};