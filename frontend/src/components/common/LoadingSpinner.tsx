import React from 'react';
import {Box, CircularProgress, Typography} from '@mui/material';

interface LoadingSpinnerProps {
    size?: number;
    message?: string;
}

const LoadingSpinner: React.FC<LoadingSpinnerProps> = ({
                                                           size = 60,
                                                           message = "Loading..."
                                                       }) => {
    return (
        <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            p={4}
            sx={{minHeight: 200}}
        >
            <CircularProgress size={size} thickness={4}/>
            <Typography variant="body1" mt={2} color="textSecondary">
                {message}
            </Typography>
        </Box>
    );
};

export default LoadingSpinner;