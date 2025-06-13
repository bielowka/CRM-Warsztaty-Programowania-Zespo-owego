import {useState} from 'react';
import {
    Box,
    Typography,
    Button,
    CircularProgress,
    Snackbar,
    Alert,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import {useAuth} from '../context/AuthContext';
import {UserRole} from '../types/UserRole';
import {fetchLeads, createLead, updateLeadStatus, LeadStatus, SortOptions, PaginationOptions} from '../api/leadsApi';
import {fetchMyAccounts} from '../api/accountsApi';
import CreateLeadDialog from '../components/leads/CreateLeadDialog';
import EditLeadStatusDialog from '../components/leads/EditLeadStatusDialog';
import {useMutation, useQuery, useQueryClient} from '@tanstack/react-query';
import Sidebar from '../components/Sidebar.tsx';
import LeadsTable from '../components/leads/LeadsTable';

const LeadsPage = () => {
    const {user} = useAuth();
    const queryClient = useQueryClient();
    const [openCreateDialog, setOpenCreateDialog] = useState(false);
    const [openEditDialog, setOpenEditDialog] = useState(false);
    const [selectedLeadId, setSelectedLeadId] = useState<number | null>(null);
    const [selectedLeadStatus, setSelectedLeadStatus] = useState<LeadStatus | null>(null);
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [sortOptions, setSortOptions] = useState<SortOptions>({
        sortBy: 'createdAt',
        sortDirection: 'desc'
    });
    const [paginationOptions, setPaginationOptions] = useState<PaginationOptions>({
        page: 0,
        size: 10
    });

    const {data: leadsResponse, isLoading, error} = useQuery({
        queryKey: ['leads', user?.role, sortOptions, paginationOptions],
        queryFn: () => fetchLeads(sortOptions, paginationOptions),
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

    const handleSortChange = (newSortOptions: SortOptions) => {
        setSortOptions(newSortOptions);
        setPaginationOptions(prev => ({ ...prev, page: 0 }));
    };

    const handlePaginationChange = (newPaginationOptions: PaginationOptions) => {
        setPaginationOptions(newPaginationOptions);
    };

    const leads = leadsResponse?.content || [];
    const pagination = leadsResponse ? {
        totalPages: leadsResponse.totalPages,
        totalElements: leadsResponse.totalElements
    } : { totalPages: 0, totalElements: 0 };

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
                    <Box sx={{display: 'flex', justifyContent: 'center', py: 5}}>
                        <CircularProgress/>
                    </Box>
                ) : error ? (
                    <Typography color="error">Failed to load leads</Typography>
                ) : leads && leads.length > 0 ? (
                    <LeadsTable
                        leads={leads}
                        onRowClick={handleRowClick}
                        showCompanyColumns={true}
                        showDateColumns={true}
                        showSortControls={true}
                        showPaginationControls={true}
                        sortOptions={sortOptions}
                        onSortChange={handleSortChange}
                        paginationOptions={paginationOptions}
                        totalPages={pagination.totalPages}
                        totalElements={pagination.totalElements}
                        onPaginationChange={handlePaginationChange}
                    />
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

                <Snackbar open={snackbarOpen} autoHideDuration={6000} onClose={() => setSnackbarOpen(false)}>
                    <Alert onClose={() => setSnackbarOpen(false)} severity="success">
                        {snackbarMessage}
                    </Alert>
                </Snackbar>
            </Box>
        </Box>
    );
};

export default LeadsPage;