import React, {useState, useEffect} from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button,
    FormControl,
    InputLabel,
    Select,
    MenuItem
} from '@mui/material';
import {LeadStatus} from '../../api/leadsApi.ts';
import CloseLeadConfirmationDialog from './CloseLeadConfirmationDialog';

interface EditLeadStatusDialogProps {
    open: boolean;
    onClose: () => void;
    onSave: (leadId: number, newStatus: LeadStatus) => void;
    leadId: number | null;
    currentStatus: LeadStatus | null;
    leadValue: number;
}

const EditLeadStatusDialog: React.FC<EditLeadStatusDialogProps> = ({
    open,
    onClose,
    onSave,
    leadId,
    currentStatus,
    leadValue
}) => {
    const [selectedStatus, setSelectedStatus] = useState<LeadStatus>(LeadStatus.NEW);
    const [showConfirmation, setShowConfirmation] = useState(false);

    useEffect(() => {
        if (currentStatus) {
            setSelectedStatus(currentStatus);
        }
    }, [currentStatus]);

    const handleSave = () => {
        if (leadId != null) {
            if (selectedStatus === LeadStatus.CLOSED_WON) {
                setShowConfirmation(true);
            } else {
                onSave(leadId, selectedStatus);
                onClose();
            }
        }
    };

    const handleConfirm = () => {
        if (leadId != null) {
            onSave(leadId, selectedStatus);
            setShowConfirmation(false);
            onClose();
        }
    };

    return (
        <>
            <Dialog open={open} onClose={onClose} fullWidth maxWidth="xs">
                <DialogTitle>Edit Lead Status</DialogTitle>
                <DialogContent>
                    <FormControl fullWidth sx={{mt: 2}}>
                        <InputLabel>Status</InputLabel>
                        <Select
                            label="Status"
                            value={selectedStatus}
                            onChange={(e) => setSelectedStatus(e.target.value as LeadStatus)}
                        >
                            {Object.values(LeadStatus).map((status) => (
                                <MenuItem key={status} value={status}>
                                    {status}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                </DialogContent>
                <DialogActions>
                    <Button onClick={onClose}>Cancel</Button>
                    <Button onClick={handleSave} variant="contained" color="primary">
                        Save
                    </Button>
                </DialogActions>
            </Dialog>

            <CloseLeadConfirmationDialog
                open={showConfirmation}
                onClose={() => setShowConfirmation(false)}
                onConfirm={handleConfirm}
                leadValue={leadValue}
            />
        </>
    );
};

export default EditLeadStatusDialog;
