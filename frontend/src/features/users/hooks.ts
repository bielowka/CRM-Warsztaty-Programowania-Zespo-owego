import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { fetchUsers, deleteUser, createUser, updateUser, changePassword } from './api';
import { User } from './types';

export const useUsers = () => {
    return useQuery<User[]>({
        queryKey: ['users'],
        queryFn: fetchUsers,
    });
};

export const useDeleteUser = () => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: deleteUser,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['users'] });
        },
    });
};

export const useCreateUser = () => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: createUser,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['users'] });
        },
    });
};

export const useUpdateUser = () => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: updateUser,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['users'] });
        },
    });
};

export const useChangePassword = () => {
    return useMutation({
        mutationFn: changePassword,
        onError: (error) => {
            throw error;
        }
    });
};