import React from 'react';
import { Box, Typography, Paper } from '@mui/material';
import { useAuth } from '../context/AuthContext';
import Sidebar from '../components/Sidebar';

const Dashboard: React.FC = () => {
  const { user } = useAuth();

  return (
    <Box sx={{ display: 'flex', height: '100vh' }}>
      <Sidebar />
      <Box sx={{ flex: 1, padding: 3, overflow: 'auto' }}>
        <Paper sx={{ padding: 3, marginBottom: 3 }}>
          <Typography variant="h4" gutterBottom>
            Welcome, {user?.firstName} {user?.lastName}!
          </Typography>
          <Typography variant="body1" color="text.secondary">
            Role: {user?.role}
          </Typography>
          <Typography variant="body1" color="text.secondary">
            Email: {user?.email}
          </Typography>
        </Paper>

        <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: 3 }}>
          <Paper sx={{ padding: 3 }}>
            <Typography variant="h6" gutterBottom>
              Lorem ipsum
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
            </Typography>
          </Paper>

          <Paper sx={{ padding: 3 }}>
            <Typography variant="h6" gutterBottom>
              Lorem ipsum
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
            </Typography>
          </Paper>

          <Paper sx={{ padding: 3 }}>
            <Typography variant="h6" gutterBottom>
              Lorem ipsum
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
            </Typography>
          </Paper>
        </Box>
      </Box>
    </Box>
  );
};

export default Dashboard; 