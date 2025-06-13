// src/pages/MyClients.tsx

import { Box, Button, Typography, CircularProgress } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import { useState } from 'react';
import { useClients, useDeleteClient, useCreateClient, useUpdateClient } from '../features/clients/hooks';
import { Client } from '../features/clients/types';
import ClientTable from '../components/ClientTable';
import DeleteClientDialog from '../components/DeleteClientDialog';
import AddClientDialog from '../components/AddClientDialog';
import EditClientDialog from '../components/EditClientDialog';
import Sidebar from '../components/Sidebar';
import React from 'react';

export default function  MyClients() {
    const [openDeleteModal, setOpenDeleteModal] = useState(false);
    const [openAddModal, setOpenAddModal] = useState(false);
    const [openEditModal, setOpenEditModal] = useState(false);
    const [clientToDelete, setClientToDelete] = useState<Client | null>(null);
    const [clientToEdit, setClientToEdit] = useState<Client | null>(null);

    const { data: clientData = [], isLoading } = useClients();
    const deleteClientMutation = useDeleteClient();
    const createClientMutation = useCreateClient();
    const updateClientMutation = useUpdateClient();

    const handleDelete = (client: Client) => {
        setClientToDelete(client);
        setOpenDeleteModal(true);
    };

    const handleEdit = (client: Client) => {
        setClientToEdit(client);
        setOpenEditModal(true);
    };

    const confirmDelete = () => {
        if (clientToDelete) {
            deleteClientMutation.mutate(clientToDelete.id);
            setOpenDeleteModal(false);
            setClientToDelete(null);
        }
    };

    const handleAddClient = (clientData: {
        firstName: string;
        lastName: string;
        email: string;
        phoneNumber: string;
        company?: string;
    }) => {
        createClientMutation.mutate(clientData, {
            onSuccess: () => {
                setOpenAddModal(false);
            }
        });
    };

    const handleUpdateClient = ( clientData: {
        firstName: string;
        lastName: string;
        email: string;
        phoneNumber: string;
        company?: string;
    }) => {
//         createClientMutation.mutate(clientData, {
//             onSuccess: () => {
//                 setOpenAddModal(false);
//             }
//         });
    };

    return (
        <Box sx={{ display: 'flex', height: '100vh' }}>
            <Sidebar />
            <Box sx={{ flex: 1, padding: 3, overflow: 'auto' }}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 4 }}>
                    <Typography variant="h5" fontWeight={600}>My Clients</Typography>
                    <Button
                        variant="contained"
                        startIcon={<AddIcon />}
                        onClick={() => setOpenAddModal(true)}
                    >
                        Add new client
                    </Button>
                </Box>

                {isLoading ? (
                    <CircularProgress />
                ) : (
                    <ClientTable
                        data={clientData}
                        onDelete={handleDelete}
                        onEdit={handleEdit}
                    />
                )}

                <DeleteClientDialog
                    open={openDeleteModal}
                    onClose={() => setOpenDeleteModal(false)}
                    onConfirm={confirmDelete}
                />

                <AddClientDialog
                    open={openAddModal}
                    onClose={() => setOpenAddModal(false)}
                    onSubmit={handleAddClient}
                />

                <EditClientDialog
                    open={openEditModal}
                    onClose={() => {
                        setOpenEditModal(false);
                        setClientToEdit(null);
                    }}
                    client={clientToEdit}
                    onSubmit={handleUpdateClient}
                />
            </Box>
        </Box>
    );

}
