import { Dialog, DialogTitle, DialogContent, DialogActions, Button, TextField, FormControl, InputLabel, Select, MenuItem, SelectChangeEvent } from '@mui/material';
import { useState, ChangeEvent } from 'react';
import { UserRole } from '../types/UserRole';

interface Props {
    open: boolean;
    onClose: () => void;
    onSubmit: (userData: {
        firstName: string;
        lastName: string;
        email: string;
        password: string;
        role: UserRole;
        position: string;
    }) => void;
}

export default function AddUserDialog({ open, onClose, onSubmit }: Props) {
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        role: UserRole.SALESPERSON,
        position: ''
    });

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onSubmit(formData);
        setFormData({
            firstName: '',
            lastName: '',
            email: '',
            password: '',
            role: UserRole.SALESPERSON,
            position: ''
        });
    };

    const handleTextChange = (e: ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSelectChange = (e: SelectChangeEvent) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    return (
        <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
            <form onSubmit={handleSubmit}>
                <DialogTitle>Add New User</DialogTitle>
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
                        name="password"
                        label="Password"
                        type="password"
                        fullWidth
                        value={formData.password}
                        onChange={handleTextChange}
                        required
                    />
                    <FormControl fullWidth margin="dense">
                        <InputLabel>Role</InputLabel>
                        <Select
                            name="role"
                            value={formData.role}
                            label="Role"
                            onChange={handleSelectChange}
                            required
                        >
                            {Object.values(UserRole).map((role) => (
                                <MenuItem key={role} value={role}>
                                    {role}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                    <TextField
                        margin="dense"
                        name="position"
                        label="Position"
                        type="text"
                        fullWidth
                        value={formData.position}
                        onChange={handleTextChange}
                        required
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={onClose}>Cancel</Button>
                    <Button type="submit" variant="contained" color="primary">
                        Add User
                    </Button>
                </DialogActions>
            </form>
        </Dialog>
    );
} 