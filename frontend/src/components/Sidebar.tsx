import {Box, Typography, List, ListItemButton, ListItemIcon, ListItemText, Badge, Divider} from '@mui/material';
import HomeIcon from '@mui/icons-material/Home';
import FolderIcon from '@mui/icons-material/Folder';
import BarChartIcon from '@mui/icons-material/BarChart';
import PeopleIcon from '@mui/icons-material/People';
import CubeIcon from '@mui/icons-material/ViewInAr';
import LogoutIcon from '@mui/icons-material/Logout';
import KeyIcon from '@mui/icons-material/Key';
import AttachMoneyIcon from '@mui/icons-material/AttachMoney';
import {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {useAuth} from '../context/AuthContext';
import {UserRole} from '../types/UserRole';
import ChangePasswordDialog from './ChangePasswordDialog';
import {useChangePassword} from '../features/users/hooks';

export default function Sidebar() {
    const navigate = useNavigate();
    const {user, logout} = useAuth();
    const [openPasswordDialog, setOpenPasswordDialog] = useState(false);
    const changePasswordMutation = useChangePassword();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    const handleChangePassword = async (passwords: { currentPassword: string; newPassword: string }) => {
        try {
            await changePasswordMutation.mutateAsync(passwords);
            setOpenPasswordDialog(false);
        } catch (error) {
            throw error;
        }
    };

    return (
        <>
            <Box sx={{
                width: '240px',
                height: '100vh',
                bgcolor: '#f5f7fa',
                padding: 2,
                borderRight: '1px solid rgba(0, 0, 0, 0.12)'
            }}>
                <Box sx={{display: 'flex', alignItems: 'center', gap: 1, mb: 2}}>
                    <CubeIcon/>
                    <Typography variant="h6" fontWeight="bold">CRM</Typography>
                </Box>
                <List>
                    <ListItemButton onClick={() => navigate('/dashboard')}>
                        <ListItemIcon><HomeIcon/></ListItemIcon>
                        <ListItemText primary="Home"/>
                    </ListItemButton>
                    {user?.role !== UserRole.ADMIN && (
                        <ListItemButton>
                            <ListItemIcon><FolderIcon/></ListItemIcon>
                            <ListItemText primary="My Clients"/>
                        </ListItemButton>
                    )}
                    {(user?.role === UserRole.MANAGER || user?.role === UserRole.SALESPERSON) && (
                        <ListItemButton onClick={() => navigate('/leads')}>
                            <ListItemIcon>
                                <AttachMoneyIcon/>
                            </ListItemIcon>
                            <ListItemText primary="Leads"/>
                        </ListItemButton>
                    )}
                    {user?.role === UserRole.ADMIN && (
                        <ListItemButton onClick={() => navigate('/users')}>
                            <ListItemIcon><PeopleIcon/></ListItemIcon>
                            <ListItemText primary="Users"/>
                        </ListItemButton>
                    )}

                    {user?.role === UserRole.MANAGER && (
                        <ListItemButton onClick={() => navigate('/reports')}>
                            <ListItemIcon><BarChartIcon/></ListItemIcon>
                            <ListItemText primary="Rankings"/>
                        </ListItemButton>
                    )}

                    <Divider sx={{my: 2}}/>

                    {user?.role !== UserRole.ADMIN && (
                        <ListItemButton onClick={() => setOpenPasswordDialog(true)}>
                            <ListItemIcon><KeyIcon/></ListItemIcon>
                            <ListItemText primary="Change Password"/>
                        </ListItemButton>
                    )}

                    <ListItemButton onClick={handleLogout}>
                        <ListItemIcon><LogoutIcon/></ListItemIcon>
                        <ListItemText primary="Logout"/>
                    </ListItemButton>
                </List>
            </Box>

            <ChangePasswordDialog
                open={openPasswordDialog}
                onClose={() => setOpenPasswordDialog(false)}
                onSubmit={handleChangePassword}
            />
        </>
    );
}