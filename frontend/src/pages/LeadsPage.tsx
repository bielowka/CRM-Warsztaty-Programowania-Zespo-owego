import {useState} from 'react';
import {
    Box,
    Typography,
    Button,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    CircularProgress,
    Chip,
    Snackbar,
    Alert,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import {useNavigate} from 'react-router-dom';
import {useAuth} from '../context/AuthContext';
import {UserRole} from '../types/UserRole';
import {fetchLeads, createLead, updateLeadStatus, LeadStatus} from '../api/leadsApi';
import {fetchMyAccounts} from '../api/accountsApi';
import CreateLeadDialog from '../components/leads/CreateLeadDialog';
import EditLeadStatusDialog from '../components/leads/EditLeadStatusDialog';
import {formatCurrency} from '../utils/formatUtils';
import {useMutation, useQuery, useQueryClient} from '@tanstack/react-query';
import Sidebar from '../components/Sidebar.tsx';

const LeadsPage = () => {
    const {user} = useAuth();
    const queryClient = useQueryClient();
    const [openCreateDialog, setOpenCreateDialog] = useState(false);
    const [openEditDialog, setOpenEditDialog] = useState(false);
    const [selectedLeadId, setSelectedLeadId] = useState<number | null>(null);
    const [selectedLeadStatus, setSelectedLeadStatus] = useState<LeadStatus | null>(null);
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');

    const {data: leads, isLoading, error} = useQuery({
        queryKey: ['leads', user?.role],
        queryFn: () => fetchLeads(),
    });

    const {data: accounts} = useQuery({
        queryKey: ['my-accounts', user?.userId],
        queryFn: () =>
            user?.role === UserRole.SALESPERSON ? fetchMyAccounts() : Promise.resolve([]),
        enabled: user?.role === UserRole.SALESPERSON,
    });

    const createLeadMutation = useMutation({
        mutationFn: createLead,
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: ['leads']});
            setSnackbarMessage('Lead created successfully!');
            setSnackbarOpen(true);
            setOpenCreateDialog(false);
        },
    });

    const updateLeadStatusMutation = useMutation({
        mutationFn: ({id, status}: { id: number; status: LeadStatus }) =>
            updateLeadStatus(id, status),
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: ['leads']});
            setSnackbarMessage('Lead status updated successfully!');
            setSnackbarOpen(true);
            setOpenEditDialog(false);
        },
    });

    const handleCreateLead = (leadData: any) => {
        createLeadMutation.mutate(leadData);
    };

    const handleRowClick = (lead: any) => {
        setSelectedLeadId(lead.id);
        setSelectedLeadStatus(lead.status);
        setOpenEditDialog(true);
    };

    const handleStatusUpdate = (id: number, status: LeadStatus) => {
        updateLeadStatusMutation.mutate({id, status});
    };

    const getStatusColor = (status: LeadStatus) => {
        switch (status) {
            case LeadStatus.NEW:
                return 'default';
            case LeadStatus.QUALIFICATION:
                return 'primary';
            case LeadStatus.NEEDS_ANALYSIS:
                return 'info';
            case LeadStatus.VALUE_PROPOSITION:
                return 'secondary';
            case LeadStatus.ID_DECISION_MAKERS:
                return 'warning';
            case LeadStatus.PERCEPTION_ANALYSIS:
                return 'warning';
            case LeadStatus.PROPOSAL:
                return 'info';
            case LeadStatus.NEGOTIATION:
                return 'secondary';
            case LeadStatus.CLOSED_WON:
                return 'success';
            case LeadStatus.CLOSED_LOST:
                return 'error';
            default:
                return 'default';
        }
    };

    return (
        <Box sx={{display: 'flex'}}>
            <Sidebar/>
            <Box sx={{p: 3, width: '100%'}}>
                <Box sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3}}>
                    <Typography variant="h4">Leads Management</Typography>

                    {user?.role === UserRole.SALESPERSON && (
                        <Button
                            variant="contained"
                            color="primary"
                            startIcon={<AddIcon/>}
                            onClick={() => setOpenCreateDialog(true)}
                        >
                            Add New Lead
                        </Button>
                    )}
                </Box>

                {isLoading ? (
                    <Box sx={{display: 'flex', justifyContent: 'center', mt: 4}}>
                        <CircularProgress size={60}/>
                    </Box>
                ) : error ? (
                    <Box sx={{
                        p: 3,
                        backgroundColor: 'error.light',
                        color: 'error.contrastText',
                        borderRadius: 1,
                        textAlign: 'center'
                    }}>
                        <Typography>Failed to load leads. Please try again later.</Typography>
                    </Box>
                ) : leads && leads.length > 0 ? (
                    <TableContainer component={Paper} elevation={3}>
                        <Table sx={{minWidth: 650}} aria-label="leads table">
                            <TableHead sx={{backgroundColor: '#f5f7fa'}}>
                                <TableRow>
                                    <TableCell>Description</TableCell>
                                    <TableCell>Status</TableCell>
                                    <TableCell align="right">Estimated Value</TableCell>
                                    <TableCell>Company</TableCell>
                                    <TableCell>Industry</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {leads.map((lead: any) => (
                                    <TableRow
                                        key={lead.id}
                                        hover
                                        onClick={() => handleRowClick(lead)}
                                        sx={{cursor: 'pointer', '&:hover': {backgroundColor: 'action.hover'}}}
                                    >
                                        <TableCell>
                                            <Typography fontWeight="medium">{lead.description}</Typography>
                                        </TableCell>
                                        <TableCell>
                                            <Chip
                                                label={lead.status}
                                                color={getStatusColor(lead.status)}
                                                variant="outlined"
                                                size="small"
                                            />
                                        </TableCell>
                                        <TableCell align="right">{formatCurrency(lead.estimatedValue)}</TableCell>
                                        <TableCell>{lead.companyName || 'N/A'}</TableCell>
                                        <TableCell>{lead.companyIndustry || 'N/A'}</TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                ) : (
                    <Box sx={{
                        p: 5,
                        border: '1px dashed #ccc',
                        borderRadius: 1,
                        textAlign: 'center',
                        backgroundColor: 'background.paper'
                    }}>
                        <Typography variant="h6" color="textSecondary">
                            No leads found
                        </Typography>
                        <Typography variant="body1" mt={1}>
                            {user?.role === UserRole.SALESPERSON
                                ? 'Start by adding your first lead'
                                : 'There are currently no leads in the system'}
                        </Typography>
                        {user?.role === UserRole.SALESPERSON && (
                            <Button
                                variant="outlined"
                                startIcon={<AddIcon/>}
                                onClick={() => setOpenCreateDialog(true)}
                                sx={{mt: 2}}
                            >
                                Create Your First Lead
                            </Button>
                        )}
                    </Box>
                )}

                <CreateLeadDialog
                    open={openCreateDialog}
                    onClose={() => setOpenCreateDialog(false)}
                    onSubmit={handleCreateLead}
                    accounts={accounts || []}
                    isLoading={createLeadMutation.isPending}
                />

                <EditLeadStatusDialog
                    open={openEditDialog}
                    onClose={() => setOpenEditDialog(false)}
                    onSave={(id, status) => handleStatusUpdate(id, status)}
                    leadId={selectedLeadId}
                    currentStatus={selectedLeadStatus}
                />

                <Snackbar
                    open={snackbarOpen}
                    autoHideDuration={3000}
                    onClose={() => setSnackbarOpen(false)}
                    anchorOrigin={{vertical: 'bottom', horizontal: 'center'}}
                >
                    <Alert severity="success" onClose={() => setSnackbarOpen(false)} sx={{width: '100%'}}>
                        {snackbarMessage}
                    </Alert>
                </Snackbar>
            </Box>
        </Box>
    );
};

export default LeadsPage;