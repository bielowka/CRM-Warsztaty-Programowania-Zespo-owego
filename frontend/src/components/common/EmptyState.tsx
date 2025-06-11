import React from 'react';
import {Box, Typography, SvgIcon} from '@mui/material';
import FolderOpenIcon from '@mui/icons-material/FolderOpen';

interface EmptyStateProps {
    title: string;
    subtitle?: string;
    icon?: React.ReactNode;
}

const EmptyState: React.FC<EmptyStateProps> = ({
                                                   title,
                                                   subtitle,
                                                   icon = <FolderOpenIcon fontSize="large"/>
                                               }) => {
    return (
        <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            p={4}
            sx={{
                minHeight: 300,
                border: '1px dashed rgba(0, 0, 0, 0.12)',
                borderRadius: 1
            }}
        >
            <SvgIcon
                fontSize="large"
                color="disabled"
                sx={{fontSize: 60, mb: 2}}
            >
                {icon}
            </SvgIcon>
            <Typography variant="h6" gutterBottom>
                {title}
            </Typography>
            {subtitle && (
                <Typography variant="body1" color="textSecondary" align="center">
                    {subtitle}
                </Typography>
            )}
        </Box>
    );
};

export default EmptyState;