import { Box, Button, Typography, CircularProgress } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import { useState } from 'react';
import { useUsers, useDeleteUser, useCreateUser, useUpdateUser } from '../features/users/hooks';
import { User } from '../features/users/types';
import UserTable from '../components/UserTable';
import DeleteUserDialog from '../components/DeleteUserDialog';
import AddUserDialog from '../components/AddUserDialog';
import EditUserDialog from '../components/EditUserDialog';
import Sidebar from '../components/Sidebar';

export default function UsersPage() {
    const [openDeleteModal, setOpenDeleteModal] = useState(false);
    const [openAddModal, setOpenAddModal] = useState(false);
    const [openEditModal, setOpenEditModal] = useState(false);
    const [userToDelete, setUserToDelete] = useState<User | null>(null);
    const [userToEdit, setUserToEdit] = useState<User | null>(null);
    const { data: userData = [], isLoading } = useUsers();
    const deleteUserMutation = useDeleteUser();
    const createUserMutation = useCreateUser();
    const updateUserMutation = useUpdateUser();

    const handleDelete = (user: User) => {
        setUserToDelete(user);
        setOpenDeleteModal(true);
    };

    const handleEdit = (user: User) => {
        setUserToEdit(user);
        setOpenEditModal(true);
    };

    const confirmDelete = () => {
        if (userToDelete) {
            deleteUserMutation.mutate(userToDelete.id);
            setOpenDeleteModal(false);
            setUserToDelete(null);
        }
    };

    const handleAddUser = (userData: {
        firstName: string;
        lastName: string;
        email: string;
        password: string;
        role: string;
        position: string;
    }) => {
        createUserMutation.mutate(userData, {
            onSuccess: () => {
                setOpenAddModal(false);
            }
        });
    };

    const handleUpdateUser = (userData: {
        id: number;
        firstName: string;
        lastName: string;
        email: string;
        password?: string;
        role: string;
        position: string;
    }) => {
        updateUserMutation.mutate(userData, {
            onSuccess: () => {
                setOpenEditModal(false);
                setUserToEdit(null);
            }
        });
    };

    return (
        <Box sx={{ display: 'flex', height: '100vh' }}>
            <Sidebar />
            <Box sx={{ flex: 1, padding: 3, overflow: 'auto' }}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 4 }}>
                    <Typography variant="h5" fontWeight={600}>Users List</Typography>
                    <Button 
                        variant="contained" 
                        startIcon={<AddIcon />}
                        onClick={() => setOpenAddModal(true)}
                    >
                        Add new user
                    </Button>
                </Box>

                {isLoading ? (
                    <CircularProgress />
                ) : (
                    <UserTable 
                        data={userData} 
                        onDelete={handleDelete}
                        onEdit={handleEdit}
                    />
                )}

                <DeleteUserDialog
                    open={openDeleteModal}
                    onClose={() => setOpenDeleteModal(false)}
                    onConfirm={confirmDelete}
                />

                <AddUserDialog
                    open={openAddModal}
                    onClose={() => setOpenAddModal(false)}
                    onSubmit={handleAddUser}
                />

                <EditUserDialog
                    open={openEditModal}
                    onClose={() => {
                        setOpenEditModal(false);
                        setUserToEdit(null);
                    }}
                    user={userToEdit}
                    onSubmit={handleUpdateUser}
                />
            </Box>
        </Box>
    );
}
