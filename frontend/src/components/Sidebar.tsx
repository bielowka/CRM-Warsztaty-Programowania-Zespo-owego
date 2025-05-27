import { Box, Typography, List, ListItemButton, ListItemIcon, ListItemText, Badge } from '@mui/material';
import HomeIcon from '@mui/icons-material/Home';
import FolderIcon from '@mui/icons-material/Folder';
import AssignmentIcon from '@mui/icons-material/Assignment';
import BarChartIcon from '@mui/icons-material/BarChart';
import PeopleIcon from '@mui/icons-material/People';
import NotificationsIcon from '@mui/icons-material/Notifications';
import InfoOutlineIcon from '@mui/icons-material/InfoOutline';
import CubeIcon from '@mui/icons-material/ViewInAr';

export default function Sidebar() {
    return (
        <Box sx={{ width: '240px', height: '100vh', bgcolor: '#f5f7fa', padding: 2 }}>
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 2 }}>
                <CubeIcon />
                <Typography variant="h6" fontWeight="bold">CRM</Typography>
            </Box>
            <List>
                <ListItemButton><ListItemIcon><HomeIcon /></ListItemIcon><ListItemText primary="Home" /></ListItemButton>
                <ListItemButton><ListItemIcon><FolderIcon /></ListItemIcon><ListItemText primary="My Clients" /></ListItemButton>
                <ListItemButton>
                    <ListItemIcon>
                        <Badge badgeContent={10} color="primary"><AssignmentIcon /></Badge>
                    </ListItemIcon>
                    <ListItemText primary="Tasks" />
                </ListItemButton>
                <ListItemButton><ListItemIcon><BarChartIcon /></ListItemIcon><ListItemText primary="Rankings" /></ListItemButton>
                <ListItemButton><ListItemIcon><PeopleIcon /></ListItemIcon><ListItemText primary="Users" /></ListItemButton>
                <ListItemButton><ListItemIcon><InfoOutlineIcon /></ListItemIcon><ListItemText primary="Reports" /></ListItemButton>
                <ListItemButton><ListItemIcon><NotificationsIcon /></ListItemIcon><ListItemText primary="Notifications" /></ListItemButton>
            </List>
        </Box>
    );
}