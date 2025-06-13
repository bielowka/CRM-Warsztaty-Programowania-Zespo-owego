import React from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogContentText,
    DialogActions,
    Button,
    Typography
} from '@mui/material';
import { formatCurrency } from '../../utils/formatUtils';

interface CloseLeadConfirmationDialogProps {
    open: boolean;
    onClose: () => void;
    onConfirm: () => void;
    leadValue: number;
}

const CloseLeadConfirmationDialog: React.FC<CloseLeadConfirmationDialogProps> = ({
    open,
    onClose,
    onConfirm,
    leadValue
}) => {
    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>Confirm Lead Closure</DialogTitle>
            <DialogContent>
                <DialogContentText>
                    Are you sure you want to close this lead as won? This will:
                </DialogContentText>
                <Typography variant="body2" sx={{ mt: 2 }}>
                    1. Create a new sale with value: {formatCurrency(leadValue)}
                </Typography>
                <Typography variant="body2" sx={{ mt: 1 }}>
                    2. Remove this lead from the system
                </Typography>
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>Cancel</Button>
                <Button onClick={onConfirm} color="primary" variant="contained">
                    Confirm
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default CloseLeadConfirmationDialog; 