import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { fetchUsers, deleteUser } from './api';
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