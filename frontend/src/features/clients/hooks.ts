import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { fetchClients, GetAccountById, GetAccountByUser, GetAccountByUserIdAndStatus, createClient, updateClient, deleteClient } from './api';
import { Client } from './types';

export const useClients = () => {
    return useQuery<Client[]>({
        queryKey: ['accounts'],
        queryFn: GetAccountByUser
    });
};

export const useDeleteClient = () => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: deleteClient,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['accounts'] });
        },
    });
};

export const useCreateClient = () => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: createClient,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['accounts'] });
        },
    });
};

export const useUpdateClient = () => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: updateClient,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['accounts'] });
        },
    });
};