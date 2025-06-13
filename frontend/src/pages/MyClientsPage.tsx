import React, { useState } from 'react';
import {
    Box,
    Typography,
    Paper,
    Button
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import Sidebar from '../components/Sidebar';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { fetchMyAccounts, fetchTeamAccounts } from '../api/accountsApi';
import ClientTable from '../components/ClientTable';
import AddClientDialog from '../components/AddClientDialog';
import EditClientDialog from '../components/EditClientDialog';
import { Client } from '../features/clients/types';
import api from '../config/axios';
import { useAuth } from '../context/AuthContext';

const MyClientsPage: React.FC = () => {
    const navigate = useNavigate();
    const queryClient = useQueryClient();
    const { user } = useAuth();
    const [isAddDialogOpen, setIsAddDialogOpen] = useState(false);
    const [isEditDialogOpen, setIsEditDialogOpen] = useState(false);
    const [selectedClient, setSelectedClient] = useState<Client | null>(null);

    const { data: accounts } = useQuery({
        queryKey: ['my-accounts'],
        queryFn: user?.role === 'MANAGER' ? fetchTeamAccounts : fetchMyAccounts,
    });

    const createAccountMutation = useMutation({
        mutationFn: async (data: {
            firstName: string;
            lastName: string;
            email: string;
            phoneNumber: string;
            companyName?: string;
        }) => {
            const response = await api.post('/api/accounts', {
                firstName: data.firstName,
                lastName: data.lastName,
                email: data.email,
                phoneNumber: data.phoneNumber,
                accountStatus: 'ACTIVE',
                companyName: data.companyName || null,
                industry: data.companyName ? 'General' : null
            });
            return response.data;
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['my-accounts'] });
        },
    });

    const updateAccountMutation = useMutation({
        mutationFn: async (data: { id: number; updateData: any }) => {
            const response = await api.put(`/api/accounts/${data.id}`, data.updateData);
            return response.data;
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['my-accounts'] });
        },
    });

    const deleteAccountMutation = useMutation({
        mutationFn: async (id: number) => {
            await api.delete(`/api/accounts/${id}`);
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['my-accounts'] });
        },
    });

    const handleAddClient = () => {
        setIsAddDialogOpen(true);
    };

    const handleEditClient = (client: Client) => {
        setSelectedClient(client);
        setIsEditDialogOpen(true);
    };

    const handleDeleteClient = (client: Client) => {
        if (window.confirm('Are you sure you want to delete this client?')) {
            deleteAccountMutation.mutate(client.id);
        }
    };

    const handleClientClick = (client: Client) => {
        navigate(`/accounts/${client.id}`);
    };

    const handleAddDialogClose = () => {
        setIsAddDialogOpen(false);
    };

    const handleEditDialogClose = () => {
        setIsEditDialogOpen(false);
        setSelectedClient(null);
    };

    const handleAddSubmit = (clientData: {
        firstName: string;
        lastName: string;
        email: string;
        phoneNumber: string;
        companyName?: string;
    }) => {
        createAccountMutation.mutate(clientData);
        handleAddDialogClose();
    };

    const handleEditSubmit = (clientData: {
        id: number;
        firstName: string;
        lastName: string;
        email: string;
        phoneNumber: string;
    }) => {
        updateAccountMutation.mutate({
            id: clientData.id,
            updateData: {
                firstName: clientData.firstName,
                lastName: clientData.lastName,
                email: clientData.email,
                phoneNumber: clientData.phoneNumber,
                accountStatus: selectedClient?.accountStatus || 'ACTIVE'
            }
        });
        handleEditDialogClose();
    };

    const clients: Client[] = accounts?.map(account => ({
        id: account.id,
        firstName: account.firstName,
        lastName: account.lastName,
        email: account.email,
        phoneNumber: account.phoneNumber,
        accountStatus: account.accountStatus,
        user: { id: 0, email: '', firstName: '', lastName: '', role: 'SALESPERSON' }, // not used in table so not loading this
        address: { street: '', city: '', state: '', zipCode: '' },
        company: account.companyName ? { name: account.companyName, industry: '' } : undefined
    })) || [];

    return (
        <Box sx={{ display: 'flex', height: '100vh' }}>
            <Sidebar />
            <Box sx={{ flex: 1, padding: 3, overflow: 'auto' }}>
                <Paper sx={{ padding: 3, marginBottom: 3 }}>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                        <Box>
                            <Typography variant="h4" gutterBottom>
                                {user?.role === 'MANAGER' ? 'Team Clients' : 'My Clients'}
                            </Typography>
                            <Typography variant="body1" color="text.secondary">
                                {user?.role === 'MANAGER'
                                    ? 'Manage your team\'s client relationships and accounts'
                                    : 'Manage your client relationships and accounts'}
                            </Typography>
                        </Box>
                        <Button
                            variant="contained"
                            color="primary"
                            onClick={handleAddClient}
                        >
                            Add New Client
                        </Button>
                    </Box>

                    {clients.length > 0 ? (
                        <ClientTable
                            data={clients}
                            onEdit={handleEditClient}
                            onDelete={handleDeleteClient}
                            onRowClick={handleClientClick}
                        />
                    ) : (
                        <Typography variant="body1" color="text.secondary" sx={{ mt: 2 }}>
                            No clients found. Add your first client to get started.
                        </Typography>
                    )}
                </Paper>
            </Box>

            <AddClientDialog
                open={isAddDialogOpen}
                onClose={handleAddDialogClose}
                onSubmit={handleAddSubmit}
            />

            {selectedClient && (
                <EditClientDialog
                    open={isEditDialogOpen}
                    onClose={handleEditDialogClose}
                    client={selectedClient}
                    onSubmit={handleEditSubmit}
                />
            )}
        </Box>
    );
};

export default MyClientsPage; 