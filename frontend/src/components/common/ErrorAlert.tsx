import React from 'react';
import {Alert, AlertTitle, Button, Box} from '@mui/material';

interface ErrorAlertProps {
    message: string;
    onRetry?: () => void;
    severity?: 'error' | 'warning';
}

const ErrorAlert: React.FC<ErrorAlertProps> = ({
                                                   message,
                                                   onRetry,
                                                   severity = 'error'
                                               }) => {
    return (
        <Box my={2}>
            <Alert severity={severity}>
                <AlertTitle>
                    {severity === 'error' ? 'Error' : 'Warning'}
                </AlertTitle>
                {message}
                {onRetry && (
                    <Box mt={2}>
                        <Button
                            variant="outlined"
                            color={severity}
                            onClick={onRetry}
                            size="small"
                        >
                            Retry
                        </Button>
                    </Box>
                )}
            </Alert>
        </Box>
    );
};

export default ErrorAlert;