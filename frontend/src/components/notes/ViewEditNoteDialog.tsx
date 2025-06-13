import React, { useState, useEffect } from 'react';
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
    Stack,
    Typography,
    Chip,
    Box,
    Divider
} from '@mui/material';
import { Edit as EditIcon, Save as SaveIcon, Close as CloseIcon } from '@mui/icons-material';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { Note, NoteType, noteTypeLabels, UpdateNoteRequest } from '../../api/notesApi';

interface ViewEditNoteDialogProps {
    open: boolean;
    onClose: () => void;
    onSave: (noteId: number, noteData: UpdateNoteRequest) => void;
    note: Note | null;
    isLoading?: boolean;
}

const ViewEditNoteDialog: React.FC<ViewEditNoteDialogProps> = ({
    open,
    onClose,
    onSave,
    note,
    isLoading = false
}) => {
    const [isEditing, setIsEditing] = useState(false);
    const [content, setContent] = useState('');
    const [noteType, setNoteType] = useState<NoteType>(NoteType.MEETING);
    const [noteDate, setNoteDate] = useState<Date>(new Date());

    useEffect(() => {
        if (note) {
            setContent(note.content);
            setNoteType(note.noteType);
            setNoteDate(new Date(note.noteDate));
        }
    }, [note]);

    // Reset editing state when dialog closes
    useEffect(() => {
        if (!open) {
            setIsEditing(false);
        }
    }, [open]);

    const handleSave = () => {
        if (!note || !content.trim()) {
            return;
        }

        const noteData: UpdateNoteRequest = {
            content: content.trim(),
            noteType,
            noteDate: noteDate.toISOString()
        };

        onSave(note.id, noteData);
        setIsEditing(false); // Exit edit mode after saving
    };

    const handleClose = () => {
        setIsEditing(false);
        if (note) {
            setContent(note.content);
            setNoteType(note.noteType);
            setNoteDate(new Date(note.noteDate));
        }
        onClose();
    };

    const getNoteTypeColor = (type: NoteType) => {
        switch (type) {
            case NoteType.MEETING:
                return 'primary';
            case NoteType.PHONE_CALL:
                return 'success';
            case NoteType.EMAIL:
                return 'info';
            case NoteType.FOLLOW_UP:
                return 'warning';
            case NoteType.GENERAL:
                return 'secondary';
            default:
                return 'default';
        }
    };

    const formatDate = (dateString: string) => {
        return new Date(dateString).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    if (!note) return null;

    return (
        <Dialog open={open} onClose={handleClose} fullWidth maxWidth="md">
            <DialogTitle>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <Typography variant="h6">Note Details</Typography>
                    {!isEditing && (
                        <Button
                            startIcon={<EditIcon />}
                            onClick={() => setIsEditing(true)}
                            variant="outlined"
                            size="small"
                            type="button"
                        >
                            Edit
                        </Button>
                    )}
                </Box>
            </DialogTitle>
            
            <DialogContent>
                <Stack spacing={3} sx={{ mt: 1 }}>
                    {isEditing ? (
                        <>
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
                                rows={8}
                                value={content}
                                onChange={(e) => setContent(e.target.value)}
                                placeholder="Enter note content..."
                                fullWidth
                                variant="outlined"
                                inputProps={{ maxLength: 2000 }}
                                helperText={`${content.length}/2000 characters`}
                            />
                        </>
                    ) : (
                        <>
                            <Box sx={{ display: 'flex', gap: 2, alignItems: 'center' }}>
                                <Chip
                                    label={noteTypeLabels[note.noteType]}
                                    color={getNoteTypeColor(note.noteType) as any}
                                    variant="outlined"
                                />
                                <Typography variant="body2" color="text.secondary">
                                    {formatDate(note.noteDate)}
                                </Typography>
                            </Box>
                            
                            <Divider />
                            
                            <Typography variant="body1" sx={{ whiteSpace: 'pre-wrap' }}>
                                {note.content}
                            </Typography>
                            
                            <Box sx={{ pt: 2 }}>
                                <Typography variant="caption" color="text.secondary">
                                    Created: {formatDate(note.createdAt)}
                                    {note.updatedAt !== note.createdAt && (
                                        <span> • Last updated: {formatDate(note.updatedAt)}</span>
                                    )}
                                </Typography>
                            </Box>
                        </>
                    )}
                </Stack>
            </DialogContent>
            
            <DialogActions>
                {isEditing ? (
                    <>
                        <Button 
                            onClick={() => setIsEditing(false)} 
                            disabled={isLoading}
                            startIcon={<CloseIcon />}
                            type="button"
                        >
                            Cancel
                        </Button>
                        <Button
                            onClick={handleSave}
                            variant="contained"
                            disabled={!content.trim() || isLoading}
                            startIcon={<SaveIcon />}
                            type="button"
                        >
                            {isLoading ? 'Saving...' : 'Save Changes'}
                        </Button>
                    </>
                ) : (
                    <Button onClick={handleClose} type="button">
                        Close
                    </Button>
                )}
            </DialogActions>
        </Dialog>
    );
};

export default ViewEditNoteDialog; 