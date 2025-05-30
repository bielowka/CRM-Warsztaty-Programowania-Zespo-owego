import { Dialog, DialogTitle, DialogContent, DialogActions, Button, TextField } from '@mui/material';
import { useState, ChangeEvent } from 'react';
import axios from 'axios';

interface Props {
    open: boolean;
    onClose: () => void;
    onSubmit: (passwords: { currentPassword: string; newPassword: string }) => Promise<void>;
}

export default function ChangePasswordDialog({ open, onClose, onSubmit }: Props) {
    const [formData, setFormData] = useState({
        currentPassword: '',
        newPassword: '',
        confirmPassword: ''
    });
    const [error, setError] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        if (formData.newPassword !== formData.confirmPassword) {
            setError('New passwords do not match');
            return;
        }

        setIsSubmitting(true);
        try {
            await onSubmit({
                currentPassword: formData.currentPassword,
                newPassword: formData.newPassword
            });

            setFormData({
                currentPassword: '',
                newPassword: '',
                confirmPassword: ''
            });
            onClose();
        } catch (err) {
            if (axios.isAxiosError(err)) {
                if (err.response?.status === 401 || err.response?.status === 403) {
                    setError('Current password is incorrect');
                } else if (err.response?.status === 404) {
                    setError('User not found');
                } else {
                    setError(err.response?.data?.message || 'Failed to change password. Please try again.');
                }
            } else {
                setError('An unexpected error occurred');
            }
        } finally {
            setIsSubmitting(false);
        }
    };

    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
        setError('');
    };

    const handleClose = () => {
        setFormData({
            currentPassword: '',
            newPassword: '',
            confirmPassword: ''
        });
        setError('');
        onClose();
    };

    return (
        <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth>
            <form onSubmit={handleSubmit}>
                <DialogTitle>Change Password</DialogTitle>
                <DialogContent>
                    <TextField
                        margin="dense"
                        name="currentPassword"
                        label="Current Password"
                        type="password"
                        fullWidth
                        value={formData.currentPassword}
                        onChange={handleChange}
                        required
                        error={!!error}
                    />
                    <TextField
                        margin="dense"
                        name="newPassword"
                        label="New Password"
                        type="password"
                        fullWidth
                        value={formData.newPassword}
                        onChange={handleChange}
                        required
                        error={!!error}
                    />
                    <TextField
                        margin="dense"
                        name="confirmPassword"
                        label="Confirm New Password"
                        type="password"
                        fullWidth
                        value={formData.confirmPassword}
                        onChange={handleChange}
                        required
                        error={!!error}
                        helperText={error}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button 
                        type="submit" 
                        variant="contained" 
                        color="primary"
                        disabled={isSubmitting}
                    >
                        {isSubmitting ? 'Changing Password...' : 'Change Password'}
                    </Button>
                </DialogActions>
            </form>
        </Dialog>
    );
} 