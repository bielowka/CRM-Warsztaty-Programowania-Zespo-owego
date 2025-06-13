import React, { useState } from 'react';
import {
    Box,
    Typography,
    Paper,
    Button,
    Chip,
    Snackbar,
    Alert
} from '@mui/material';
import { Add as AddIcon } from '@mui/icons-material';
import { useParams, useNavigate } from 'react-router-dom';
import Sidebar from '../components/Sidebar';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { Account } from '../api/accountsApi';
import api from '../config/axios';
import LeadsTable from '../components/leads/LeadsTable';
import EditLeadStatusDialog from '../components/leads/EditLeadStatusDialog';
import { updateLeadStatus, LeadStatus, SortOptions } from '../api/leadsApi';
import NotesTable from '../components/notes/NotesTable';
import CreateNoteDialog from '../components/notes/CreateNoteDialog';
import { fetchNotesByAccount, createNote, deleteNote, CreateNoteRequest, updateNote, UpdateNoteRequest, Note } from '../api/notesApi';
import ViewEditNoteDialog from '../components/notes/ViewEditNoteDialog';

const fetchAccount = async (accountId: string): Promise<Account> => {
    const response = await api.get(`api/accounts/${accountId}`);
    return response.data;
};

const AccountDetailPage: React.FC = () => {
    const { accountId } = useParams<{ accountId: string }>();
    const navigate = useNavigate();
    const queryClient = useQueryClient();
    
    const [sortOptions, setSortOptions] = useState<SortOptions>({
        sortBy: 'createdAt',
        sortDirection: 'desc'
    });
    
    const [openEditDialog, setOpenEditDialog] = useState(false);
    const [selectedLeadId, setSelectedLeadId] = useState<number | null>(null);
    const [selectedLeadStatus, setSelectedLeadStatus] = useState<LeadStatus>(LeadStatus.NEW);
    const [openCreateNoteDialog, setOpenCreateNoteDialog] = useState(false);
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [selectedNote, setSelectedNote] = useState<Note | null>(null);
    const [viewEditDialogOpen, setViewEditDialogOpen] = useState(false);
    
    const { data: account, isLoading, error } = useQuery({
        queryKey: ['account', accountId],
        queryFn: () => fetchAccount(accountId!),
        enabled: !!accountId,
    });

    const { data: notes = [] } = useQuery({
        queryKey: ['notes', accountId],
        queryFn: () => fetchNotesByAccount(Number(accountId!)),
        enabled: !!accountId,
    });

    const updateLeadStatusMutation = useMutation({
        mutationFn: ({ id, status }: { id: number; status: LeadStatus }) =>
            updateLeadStatus(id, status),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['account', accountId] });
            setOpenEditDialog(false);
            setSnackbarMessage('Lead status updated successfully!');
            setSnackbarOpen(true);
        },
    });

    const createNoteMutation = useMutation({
        mutationFn: createNote,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['notes', accountId] });
            queryClient.invalidateQueries({ queryKey: ['account', accountId] });
            setOpenCreateNoteDialog(false);
            setSnackbarMessage('Note created successfully!');
            setSnackbarOpen(true);
        },
        onError: (error) => {
            console.error('Failed to create note:', error);
        },
    });

    const deleteNoteMutation = useMutation({
        mutationFn: deleteNote,
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['notes', accountId] });
            queryClient.invalidateQueries({ queryKey: ['account', accountId] });
            setSnackbarMessage('Note deleted successfully!');
            setSnackbarOpen(true);
        },
        onError: (error) => {
            console.error('Failed to delete note:', error);
        },
    });

    const updateNoteMutation = useMutation({
        mutationFn: ({ noteId, noteData }: { noteId: number; noteData: UpdateNoteRequest }) => 
            updateNote(noteId, noteData),
        onSuccess: (updatedNote) => {
            queryClient.invalidateQueries({ queryKey: ['notes', accountId] });
            queryClient.invalidateQueries({ queryKey: ['account', accountId] });
            setSelectedNote(updatedNote);
            setSnackbarMessage('Note updated successfully!');
            setSnackbarOpen(true);
        },
        onError: (error) => {
            console.error('Failed to update note:', error);
        },
    });

    const handleLeadClick = (lead: any) => {
        setSelectedLeadId(lead.id);
        setSelectedLeadStatus(lead.status);
        setOpenEditDialog(true);
    };

    const handleStatusUpdate = (id: number, status: LeadStatus) => {
        updateLeadStatusMutation.mutate({ id, status });
    };

    const handleSortChange = (newSortOptions: SortOptions) => {
        setSortOptions(newSortOptions);
    };

    const handleCreateNote = (noteData: CreateNoteRequest) => {
        createNoteMutation.mutate(noteData);
    };

    const handleDeleteNote = (noteId: number) => {
        if (window.confirm('Are you sure you want to delete this note?')) {
            deleteNoteMutation.mutate(noteId);
        }
    };

    const handleNoteRowClick = (note: Note) => {
        setSelectedNote(note);
        setViewEditDialogOpen(true);
    };

    const handleUpdateNote = (noteId: number, noteData: UpdateNoteRequest) => {
        updateNoteMutation.mutate({ noteId, noteData });
    };

    const sortedLeads = account?.leads ? [...account.leads].sort((a, b) => {
        const { sortBy, sortDirection } = sortOptions;
        let aValue: any, bValue: any;
        
        switch (sortBy) {
            case 'createdAt':
            case 'updatedAt':
                aValue = new Date(a[sortBy as keyof typeof a] as string);
                bValue = new Date(b[sortBy as keyof typeof b] as string);
                break;
            case 'estimatedValue':
                aValue = a.estimatedValue;
                bValue = b.estimatedValue;
                break;
            case 'description':
            case 'status':
                aValue = String(a[sortBy as keyof typeof a]).toLowerCase();
                bValue = String(b[sortBy as keyof typeof b]).toLowerCase();
                break;
            default:
                aValue = a.id;
                bValue = b.id;
        }
        
        if (aValue < bValue) return sortDirection === 'asc' ? -1 : 1;
        if (aValue > bValue) return sortDirection === 'asc' ? 1 : -1;
        return 0;
    }) : [];

    return (
        <Box sx={{ display: 'flex', height: '100vh' }}>
            <Sidebar />
            <Box sx={{ flex: 1, padding: 3, overflow: 'auto' }}>
                <Box sx={{ mb: 3 }}>
                    <Button 
                        variant="text" 
                        onClick={() => navigate('/my-clients')}
                        sx={{ mb: 2 }}
                    >
                        ← Back to My Clients
                    </Button>
                    
                    {isLoading && (
                        <Typography variant="body1">Loading account details...</Typography>
                    )}
                    
                    {error && (
                        <Typography variant="body1" color="error">
                            Error loading account details
                        </Typography>
                    )}
                    
                    {account && (
                        <Paper sx={{ padding: 2 }}>
                            <Typography variant="h5" gutterBottom>
                                {account.firstName} {account.lastName}
                            </Typography>
                            
                            <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 3, alignItems: 'center' }}>
                                <Box>
                                    <Typography variant="body2" color="text.secondary">
                                        Email
                                    </Typography>
                                    <Typography variant="body1">
                                        {account.email}
                                    </Typography>
                                </Box>
                                
                                <Box>
                                    <Typography variant="body2" color="text.secondary">
                                        Phone
                                    </Typography>
                                    <Typography variant="body1">
                                        {account.phoneNumber}
                                    </Typography>
                                </Box>
                                
                                <Box>
                                    <Typography variant="body2" color="text.secondary">
                                        Status
                                    </Typography>
                                    <Chip 
                                        label={account.accountStatus} 
                                        color={account.accountStatus === 'ACTIVE' ? 'success' : 'default'}
                                        size="small"
                                        sx={{ mt: 0.5 }}
                                    />
                                </Box>
                                
                                {account.company && (
                                    <Box>
                                        <Typography variant="body2" color="text.secondary">
                                            Company
                                        </Typography>
                                        <Typography variant="body1">
                                            {account.company.name}
                                        </Typography>
                                        <Typography variant="caption" color="text.secondary">
                                            {account.company.industry}
                                        </Typography>
                                    </Box>
                                )}
                            </Box>
                        </Paper>
                    )}
                </Box>

                {account && (
                    <Box sx={{ display: 'flex', gap: 3, flexDirection: { xs: 'column', lg: 'row' } }}>
                        <Box sx={{ flex: { lg: 1 }, minWidth: 0 }}>
                            <Paper sx={{ padding: 3 }}>
                                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                                    <Typography variant="h5">
                                        Latest Leads
                                    </Typography>
                                    <Button
                                        variant="outlined"
                                        size="small"
                                        onClick={() => navigate('/leads')}
                                    >
                                        Go to all leads
                                    </Button>
                                </Box>
                                <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
                                    Current sales opportunities for this client (click to edit status)
                                </Typography>
                                
                                <LeadsTable
                                    leads={sortedLeads}
                                    onRowClick={handleLeadClick}
                                    showCompanyColumns={false}
                                    showDateColumns={true}
                                    showSortControls={true}
                                    sortOptions={sortOptions}
                                    onSortChange={handleSortChange}
                                    compact={true}
                                />
                            </Paper>
                        </Box>

                        <Box sx={{ flex: { lg: 1 }, minWidth: 0 }}>
                            <Paper sx={{ padding: 3 }}>
                                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                                    <Typography variant="h5">
                                        Notes & Interactions
                                    </Typography>
                                    <Button
                                        variant="outlined"
                                        size="small"
                                        startIcon={<AddIcon />}
                                        onClick={() => setOpenCreateNoteDialog(true)}
                                    >
                                        Add Note
                                    </Button>
                                </Box>
                                <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
                                    History of meetings, calls, and other interactions
                                </Typography>
                                
                                <NotesTable
                                    notes={notes}
                                    onDeleteNote={handleDeleteNote}
                                    onRowClick={handleNoteRowClick}
                                    compact={true}
                                />
                            </Paper>
                        </Box>
                    </Box>
                )}

                <EditLeadStatusDialog
                    open={openEditDialog}
                    onClose={() => setOpenEditDialog(false)}
                    onSave={(id, status) => handleStatusUpdate(id, status)}
                    leadId={selectedLeadId}
                    currentStatus={selectedLeadStatus}
                />

                <CreateNoteDialog
                    open={openCreateNoteDialog}
                    onClose={() => setOpenCreateNoteDialog(false)}
                    onSubmit={handleCreateNote}
                    accountId={Number(accountId!)}
                    isLoading={createNoteMutation.isPending}
                />

                <ViewEditNoteDialog
                    open={viewEditDialogOpen}
                    onClose={() => setViewEditDialogOpen(false)}
                    onSave={handleUpdateNote}
                    note={selectedNote}
                    isLoading={updateNoteMutation.isPending}
                />

                <Snackbar
                    open={snackbarOpen}
                    autoHideDuration={3000}
                    onClose={() => setSnackbarOpen(false)}
                    anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
                >
                    <Alert severity="success" onClose={() => setSnackbarOpen(false)} sx={{ width: '100%' }}>
                        {snackbarMessage}
                    </Alert>
                </Snackbar>
            </Box>
        </Box>
    );
};

export default AccountDetailPage; 