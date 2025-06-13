import React, { useState } from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button,
    TextField,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    Stack
} from '@mui/material';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { NoteType, noteTypeLabels, CreateNoteRequest } from '../../api/notesApi';

interface CreateNoteDialogProps {
    open: boolean;
    onClose: () => void;
    onSubmit: (noteData: CreateNoteRequest) => void;
    accountId: number;
    isLoading?: boolean;
}

const CreateNoteDialog: React.FC<CreateNoteDialogProps> = ({
    open,
    onClose,
    onSubmit,
    accountId,
    isLoading = false
}) => {
    const [content, setContent] = useState('');
    const [noteType, setNoteType] = useState<NoteType>(NoteType.MEETING);
    const [noteDate, setNoteDate] = useState<Date>(new Date());

    const handleSubmit = () => {
        if (!content.trim()) {
            return;
        }

        const noteData: CreateNoteRequest = {
            content: content.trim(),
            noteType,
            noteDate: noteDate.toISOString(),
            accountId
        };

        onSubmit(noteData);
        handleClose();
    };

    const handleClose = () => {
        setContent('');
        setNoteType(NoteType.MEETING);
        setNoteDate(new Date());
        onClose();
    };

    return (
        <Dialog open={open} onClose={handleClose} fullWidth maxWidth="md">
            <DialogTitle>Add New Note</DialogTitle>
            <DialogContent>
                <Stack spacing={3} sx={{ mt: 1 }}>
                    <FormControl fullWidth>
                        <InputLabel>Note Type</InputLabel>
                        <Select
                            value={noteType}
                            label="Note Type"
                            onChange={(e) => setNoteType(e.target.value as NoteType)}
                        >
                            {Object.values(NoteType).map((type) => (
                                <MenuItem key={type} value={type}>
                                    {noteTypeLabels[type]}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>

                    <LocalizationProvider dateAdapter={AdapterDateFns}>
                        <DateTimePicker
                            label="Note Date"
                            value={noteDate}
                            onChange={(newValue) => newValue && setNoteDate(newValue)}
                            slotProps={{
                                textField: {
                                    fullWidth: true,
                                    variant: "outlined"
                                }
                            }}
                        />
                    </LocalizationProvider>

                    <TextField
                        label="Content"
                        multiline
                        rows={6}
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        placeholder="Enter note content..."
                        fullWidth
                        variant="outlined"
                        inputProps={{ maxLength: 2000 }}
                        helperText={`${content.length}/2000 characters`}
                    />
                </Stack>
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose} disabled={isLoading}>
                    Cancel
                </Button>
                <Button
                    onClick={handleSubmit}
                    variant="contained"
                    disabled={!content.trim() || isLoading}
                >
                    {isLoading ? 'Adding...' : 'Add Note'}
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default CreateNoteDialog; 