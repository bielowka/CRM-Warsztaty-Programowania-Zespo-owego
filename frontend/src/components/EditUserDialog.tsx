import { Dialog, DialogTitle, DialogContent, DialogActions, Button, TextField, FormControl, InputLabel, Select, MenuItem, SelectChangeEvent } from '@mui/material';
import { useState, ChangeEvent, useEffect } from 'react';
import { UserRole } from '../types/UserRole';
import { User } from '../features/users/types';

interface Props {
    open: boolean;
    onClose: () => void;
    user: User | null;
    onSubmit: (userData: {
        id: number;
        firstName: string;
        lastName: string;
        email: string;
        password?: string;
        role: UserRole;
        position: string;
    }) => void;
}

export default function EditUserDialog({ open, onClose, user, onSubmit }: Props) {
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        role: UserRole.SALESPERSON,
        position: ''
    });

    useEffect(() => {
        if (user) {
            setFormData({
                firstName: user.firstName,
                lastName: user.lastName,
                email: user.email,
                password: '',
                role: user.role,
                position: user.position || ''
            });
        }
    }, [user]);

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (user) {
            const submitData = {
                id: user.id,
                ...formData,
                ...(formData.password ? {} : { password: undefined })
            };
            onSubmit(submitData);
        }
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
                <DialogTitle>Edit User</DialogTitle>
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
                        label="Password (leave empty to keep current)"
                        type="password"
                        fullWidth
                        value={formData.password}
                        onChange={handleTextChange}
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
                        Save Changes
                    </Button>
                </DialogActions>
            </form>
        </Dialog>
    );
} 