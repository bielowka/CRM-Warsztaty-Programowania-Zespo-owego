import { Box, Button, Typography, CircularProgress } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import { useState } from 'react';
import { useUsers, useDeleteUser } from '../features/users/hooks';
import { User } from '../features/users/types';
import UserTable from '../components/UserTable';
import DeleteUserDialog from '../components/DeleteUserDialog';
import Sidebar from '../components/Sidebar';

export default function UsersPage() {
    const [openModal, setOpenModal] = useState(false);
    const [userToDelete, setUserToDelete] = useState<User | null>(null);
    const { data: userData = [], isLoading } = useUsers();
    const deleteUserMutation = useDeleteUser();

    const handleDelete = (user: User) => {
        setUserToDelete(user);
        setOpenModal(true);
    };

    const confirmDelete = () => {
        if (userToDelete) {
            deleteUserMutation.mutate(userToDelete.id);
            setOpenModal(false);
            setUserToDelete(null);
        }
    };

    return (
        <Box sx={{ display: 'flex', height: '100vh' }}>
            <Sidebar />
            <Box sx={{ flex: 1, padding: 3, overflow: 'auto' }}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 4 }}>
                    <Typography variant="h5" fontWeight={600}>Users List</Typography>
                    <Button variant="contained" startIcon={<AddIcon />}>Add new user</Button>
                </Box>

                {isLoading ? (
                    <CircularProgress />
                ) : (
                    <UserTable data={userData} onDelete={handleDelete} />
                )}

                <DeleteUserDialog
                    open={openModal}
                    onClose={() => setOpenModal(false)}
                    onConfirm={confirmDelete}
                />
            </Box>
        </Box>
    );
}
