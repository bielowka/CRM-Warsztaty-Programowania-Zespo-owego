import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button,
    TextField,
} from '@mui/material';
import { useState, useEffect, ChangeEvent } from 'react';
import { Client } from '../features/clients/types'; // Adjust import path as needed

interface Props {
    open: boolean;
    onClose: () => void;
    client: Client | null;
    onSubmit: (clientData: {
        id: number;
        firstName: string;
        lastName: string;
        email: string;
        phoneNumber: string;
    }) => void;
}

export default function EditClientDialog({ open, onClose, client, onSubmit }: Props) {
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        phoneNumber: '',
    });

    useEffect(() => {
        if (client) {
            setFormData({
                firstName: client.firstName,
                lastName: client.lastName,
                email: client.email,
                phoneNumber: client.phoneNumber,
            });
        }
    }, [client]);

    const handleTextChange = (e: ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (client) {
            onSubmit({
                id: client.id,
                ...formData,
            });
        }
    };

    return (
        <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
            <form onSubmit={handleSubmit}>
                <DialogTitle>Edit Client</DialogTitle>
                <DialogContent>
                    <TextField
                        autoFocus
                        margin="dense"
                        name="firstName"
                        label="First Name"
                        type="text"
                        fullWidth
                        value={formData.firstName}
                        onChange={handleTextChange}
                        required
                    />
                    <TextField
                        margin="dense"
                        name="lastName"
                        label="Last Name"
                        type="text"
                        fullWidth
                        value={formData.lastName}
                        onChange={handleTextChange}
                        required
                    />
                    <TextField
                        margin="dense"
                        name="email"
                        label="Email"
                        type="email"
                        fullWidth
                        value={formData.email}
                        onChange={handleTextChange}
                        required
                    />
                    <TextField
                        margin="dense"
                        name="phoneNumber"
                        label="Phone Number"
                        type="text"
                        fullWidth
                        value={formData.phoneNumber}
                        onChange={handleTextChange}
                        required
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={onClose}>Cancel</Button>
                    <Button type="submit" variant="contained" color="primary">
                        Save Changes
                    </Button>
                </DialogActions>
            </form>
        </Dialog>
    );
}
