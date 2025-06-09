import React, {useState} from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button,
    TextField,
    MenuItem,
    Select,
    InputLabel,
    FormControl,
    Stack,
    CircularProgress,
    Snackbar,
    Alert
} from '@mui/material';
import {Account, LeadStatus} from '../../api/leadsApi.ts';

interface CreateLeadDialogProps {
    open: boolean;
    onClose: () => void;
    onSubmit: (leadData: any) => void;
    accounts: Account[];
    isLoading: boolean;
}

const CreateLeadDialog: React.FC<CreateLeadDialogProps> = ({
                                                               open,
                                                               onClose,
                                                               onSubmit,
                                                               accounts,
                                                               isLoading
                                                           }) => {
    const [formData, setFormData] = useState({
        description: '',
        status: LeadStatus.NEW,
        estimatedValue: '',
        probability: '30',
        accountId: ''
    });

    const [errors, setErrors] = useState({
        description: false,
        accountId: false
    });

    const [snackbarOpen, setSnackbarOpen] = useState(false);

    const handleChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement> | any
    ) => {
        const {name, value} = e.target;
        setFormData((prev) => ({...prev, [name]: value}));

        if (errors[name as keyof typeof errors]) {
            setErrors((prev) => ({...prev, [name]: false}));
        }
    };

    const resetForm = () => {
        setFormData({
            description: '',
            status: LeadStatus.NEW,
            estimatedValue: '',
            probability: '30',
            accountId: ''
        });
        setErrors({description: false, accountId: false});
    };

    const handleSubmit = () => {
        const newErrors = {
            description: !formData.description,
            accountId: !formData.accountId
        };

        if (newErrors.description || newErrors.accountId) {
            setErrors(newErrors);
            return;
        }

        const submitData = {
            ...formData,
            estimatedValue: parseFloat(formData.estimatedValue) || 0,
            probability: parseInt(formData.probability, 10) || 0,
            accountId: parseInt(formData.accountId, 10)
        };

        onSubmit(submitData);

        // Reset form and show snackbar
        resetForm();
        setSnackbarOpen(true);
    };

    const handleClose = () => {
        resetForm();
        onClose();
    };

    const handleSnackbarClose = () => {
        setSnackbarOpen(false);
    };

    return (
        <>
            <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">
                <DialogTitle>Create New Lead</DialogTitle>
                <DialogContent>
                    <Stack spacing={2} sx={{pt: 1}}>
                        <TextField
                            fullWidth
                            label="Description"
                            name="description"
                            value={formData.description}
                            onChange={handleChange}
                            error={errors.description}
                            helperText={errors.description ? 'Description is required' : ''}
                            required
                            multiline
                            rows={3}
                        />

                        <FormControl fullWidth required error={errors.accountId}>
                            <InputLabel>Account</InputLabel>
                            <Select
                                name="accountId"
                                value={formData.accountId}
                                onChange={handleChange}
                                label="Account"
                            >
                                <MenuItem value="" disabled>
                                    Select an account
                                </MenuItem>
                                {accounts.map((account) => (
                                    <MenuItem key={account.id} value={account.id}>
                                        {account.firstName} {account.lastName} - {account.companyName}
                                    </MenuItem>
                                ))}
                            </Select>
                            {errors.accountId && (
                                <span style={{color: '#d32f2f', fontSize: '0.75rem'}}>
                  Account is required
                </span>
                            )}
                        </FormControl>

                        <FormControl fullWidth>
                            <InputLabel>Status</InputLabel>
                            <Select
                                name="status"
                                value={formData.status}
                                onChange={handleChange}
                                label="Status"
                            >
                                {Object.values(LeadStatus).map((status) => (
                                    <MenuItem key={status} value={status}>
                                        {status}
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>

                        <TextField
                            fullWidth
                            label="Estimated Value"
                            name="estimatedValue"
                            type="number"
                            value={formData.estimatedValue}
                            onChange={handleChange}
                            inputProps={{min: 0, step: 1000}}
                        />
                    </Stack>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose} color="secondary">
                        Cancel
                    </Button>
                    <Button
                        onClick={handleSubmit}
                        color="primary"
                        variant="contained"
                        disabled={isLoading}
                    >
                        {isLoading ? <CircularProgress size={24}/> : 'Create Lead'}
                    </Button>
                </DialogActions>
            </Dialog>

            <Snackbar
                open={snackbarOpen}
                autoHideDuration={3000}
                onClose={handleSnackbarClose}
                anchorOrigin={{vertical: 'bottom', horizontal: 'center'}}
            >
                <Alert onClose={handleSnackbarClose} severity="success" sx={{width: '100%'}}>
                    Lead created successfully!
                </Alert>
            </Snackbar>
        </>
    );
};

export default CreateLeadDialog;
