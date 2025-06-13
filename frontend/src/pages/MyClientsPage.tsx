import React from 'react';
import {
    Box,
    Typography,
    Paper,
    Button
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import Sidebar from '../components/Sidebar';
import { useQuery } from '@tanstack/react-query';
import { fetchMyAccounts } from '../api/accountsApi';

const MyClientsPage: React.FC = () => {
    const navigate = useNavigate();
    
    const { data: accounts } = useQuery({
        queryKey: ['my-accounts'],
        queryFn: fetchMyAccounts,
    });

    const handleExampleClientClick = () => {
        if (accounts && accounts.length > 0) {
            const firstAccount = accounts[0];
            navigate(`/accounts/${firstAccount.id}`);
        } else {
            console.log('No accounts found, would redirect to account creation or demo account');
            alert('No accounts found. In a real scenario, this would redirect to account creation or a demo account.');
        }
    };

    return (
        <Box sx={{ display: 'flex', height: '100vh' }}>
            <Sidebar />
            <Box sx={{ flex: 1, padding: 3, overflow: 'auto' }}>
                <Paper sx={{ padding: 3, marginBottom: 3 }}>
                    <Typography variant="h4" gutterBottom>
                        My Clients
                    </Typography>
                    <Typography variant="body1" color="text.secondary" gutterBottom>
                        Manage your client relationships and accounts
                    </Typography>
                    
                    {/* Example Client Button */}
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={handleExampleClientClick}
                        sx={{ mt: 2, mb: 3 }}
                    >
                        Example Client
                    </Button>
                </Paper>
            </Box>
        </Box>
    );
};

export default MyClientsPage; 