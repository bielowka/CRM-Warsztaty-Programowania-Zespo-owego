import React from 'react';
import {
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    Chip,
    Typography,
    Box,
    IconButton,
    Tooltip
} from '@mui/material';
import { Delete as DeleteIcon } from '@mui/icons-material';
import { Note, NoteType, noteTypeLabels } from '../../api/notesApi';

interface NotesTableProps {
    notes: Note[];
    onDeleteNote?: (noteId: number) => void;
    onRowClick?: (note: Note) => void;
    compact?: boolean;
}

const NotesTable: React.FC<NotesTableProps> = ({ 
    notes, 
    onDeleteNote,
    onRowClick,
    compact = false 
}) => {
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
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    const truncateContent = (content: string, maxLength: number = 100) => {
        if (content.length <= maxLength) return content;
        return content.substring(0, maxLength) + '...';
    };

    if (notes.length === 0) {
        return (
            <Box sx={{
                p: compact ? 3 : 5,
                border: '1px dashed #ccc',
                borderRadius: 1,
                textAlign: 'center',
                backgroundColor: 'background.paper'
            }}>
                <Typography variant={compact ? "body1" : "h6"} color="textSecondary">
                    No notes found
                </Typography>
                <Typography variant="body2" mt={1} color="textSecondary">
                    No interaction history for this client yet
                </Typography>
            </Box>
        );
    }

    return (
        <TableContainer component={Paper} elevation={compact ? 1 : 3}>
            <Table size={compact ? "small" : "medium"} aria-label="notes table">
                <TableHead sx={{ backgroundColor: '#f5f7fa' }}>
                    <TableRow>
                        <TableCell>Type</TableCell>
                        <TableCell>Date</TableCell>
                        <TableCell>Content</TableCell>
                        {onDeleteNote && <TableCell width="50">Actions</TableCell>}
                    </TableRow>
                </TableHead>
                <TableBody>
                    {notes.map((note) => (
                        <TableRow 
                            key={note.id} 
                            hover
                            onClick={() => onRowClick?.(note)}
                            sx={{
                                cursor: onRowClick ? 'pointer' : 'default',
                                '&:hover': onRowClick ? { backgroundColor: 'action.hover' } : {}
                            }}
                        >
                            <TableCell>
                                <Chip
                                    label={noteTypeLabels[note.noteType]}
                                    color={getNoteTypeColor(note.noteType) as any}
                                    variant="outlined"
                                    size="small"
                                />
                            </TableCell>
                            <TableCell>
                                <Typography variant="body2">
                                    {formatDate(note.noteDate)}
                                </Typography>
                            </TableCell>
                            <TableCell>
                                <Tooltip title={note.content} arrow>
                                    <Typography variant={compact ? "body2" : "body1"}>
                                        {truncateContent(note.content, compact ? 60 : 100)}
                                    </Typography>
                                </Tooltip>
                            </TableCell>
                            {onDeleteNote && (
                                <TableCell>
                                    <IconButton
                                        size="small"
                                        onClick={(e) => {
                                            e.stopPropagation();
                                            onDeleteNote(note.id);
                                        }}
                                        color="error"
                                    >
                                        <DeleteIcon fontSize="small" />
                                    </IconButton>
                                </TableCell>
                            )}
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
};

export default NotesTable; 