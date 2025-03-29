import React, { useState } from 'react';
import {
  useReactTable,
  getCoreRowModel,
  flexRender,
  createColumnHelper,
  getPaginationRowModel,
} from '@tanstack/react-table';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import axios from 'axios';

import {
  Box,
  Button,
  Typography,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Badge,
  Pagination,
  IconButton,
  CircularProgress,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
} from '@mui/material';

import AddIcon from '@mui/icons-material/Add';
import HomeIcon from '@mui/icons-material/Home';
import NotificationsIcon from '@mui/icons-material/Notifications';
import FolderIcon from '@mui/icons-material/Folder';
import AssignmentIcon from '@mui/icons-material/Assignment';
import BarChartIcon from '@mui/icons-material/BarChart';
import PeopleIcon from '@mui/icons-material/People';
import CubeIcon from '@mui/icons-material/ViewInAr';
import DeleteIcon from '@mui/icons-material/Delete';
import VisibilityIcon from '@mui/icons-material/Visibility';
import InfoOutlineIcon from '@mui/icons-material/InfoOutline';




type User = {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  role: string;
};

const fetchUsers = async (): Promise<User[]> => {
  const res = await axios.get('http://localhost:8080/api/users');
  return res.data;
};

const columnHelper = createColumnHelper<User>();

export default function App() {
  const [pagination, setPagination] = useState({ pageIndex: 0, pageSize: 8 });
  const [openModal, setOpenModal] = useState(false);
  const [userToDelete, setUserToDelete] = useState<User | null>(null);
  const queryClient = useQueryClient();

  const { data: userData = [], isLoading } = useQuery<User[]>({
    queryKey: ['users'],
    queryFn: fetchUsers,
  });

  const deleteUserMutation = useMutation({
    mutationFn: async (id: number) => {
      await axios.delete(`http://localhost:8080/api/users/${id}`);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['users'] });
      setOpenModal(false);
      setUserToDelete(null);
    },
  });

  const columns = [
    columnHelper.accessor('firstName', { header: 'First Name' }),
    columnHelper.accessor('lastName', { header: 'Last Name' }),
    columnHelper.accessor('email', { header: 'Email' }),
    columnHelper.accessor('role', { header: 'Role' }),
    {
      id: 'actions',
      header: 'Actions',
      cell: ({ row }) => {
        const user = row.original;
        return (
          <Box sx={{ display: 'flex', gap: 1 }}>
            <IconButton size="small" sx={{ color: 'black' }}>
              <VisibilityIcon fontSize="small" />
            </IconButton>
            <IconButton
              size="small"
              sx={{ color: 'black' }}
              onClick={() => {
                setUserToDelete(user);
                setOpenModal(true);
              }}
            >
              <DeleteIcon fontSize="small" />
            </IconButton>
          </Box>
        );
      },
    },
  ];

  const table = useReactTable({
    data: userData,
    columns,
    pageCount: Math.ceil(userData.length / pagination.pageSize),
    state: { pagination },
    onPaginationChange: setPagination,
    getCoreRowModel: getCoreRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
  });

  const handleChangePage = (_: any, newPage: number) => {
    setPagination(prev => ({ ...prev, pageIndex: newPage - 1 }));
  };

  const totalPages = Math.ceil(userData.length / pagination.pageSize);

  return (
    <Box sx={{ width: '100vw', height: '100vh', display: 'flex' }}>
      {/* SIDEBAR */}
      <Box
        sx={{
          width: '240px',
          height: '100vh',
          bgcolor: '#f5f7fa',
          padding: 2,
          display: 'flex',
          flexDirection: 'column',
          gap: 2,
        }}
      >
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
          <CubeIcon />
          <Typography variant="h6" fontWeight="bold">
            CRM
          </Typography>
        </Box>

        <List>
          <ListItemButton>
            <ListItemIcon><HomeIcon /></ListItemIcon>
            <ListItemText primary="Home" />
          </ListItemButton>
          <ListItemButton>
            <ListItemIcon><FolderIcon /></ListItemIcon>
            <ListItemText primary="My Clients" />
          </ListItemButton>
          <ListItemButton>
            <ListItemIcon>
              <Badge badgeContent={10} color="primary">
                <AssignmentIcon />
              </Badge>
            </ListItemIcon>
            <ListItemText primary="Tasks" />
          </ListItemButton>
          <ListItemButton>
            <ListItemIcon><BarChartIcon /></ListItemIcon>
            <ListItemText primary="Rankings" />
          </ListItemButton>
          <ListItemButton>
            <ListItemIcon><PeopleIcon /></ListItemIcon>
            <ListItemText primary="Users" />
          </ListItemButton>
                    <ListItemButton>
                      <ListItemIcon><InfoOutlineIcon /></ListItemIcon>
                      <ListItemText primary="Reports" />
                    </ListItemButton>
                                        <ListItemButton>
                                          <ListItemIcon><NotificationsIcon /></ListItemIcon>
                                          <ListItemText primary="Notifications" />
                                        </ListItemButton>
        </List>
      </Box>

      {/* MAIN CONTENT */}
      <Box sx={{ flexGrow: 1, padding: 3 }}>
        <Box
          sx={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            marginBottom: 4,
          }}
        >
          <Typography variant="h5" fontWeight={600}>
            Users List
          </Typography>
          <Button variant="contained" startIcon={<AddIcon />} sx={{ textTransform: 'none' }}>
            Add new user
          </Button>
        </Box>

        {isLoading ? (
          <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
            <CircularProgress />
          </Box>
        ) : (
          <>
            <Box component="table" sx={{ width: '100%', borderCollapse: 'collapse' }}>
              <Box component="thead" sx={{ bgcolor: '#f5f5f5' }}>
                {table.getHeaderGroups().map(headerGroup => (
                  <Box component="tr" key={headerGroup.id}>
                    {headerGroup.headers.map(header => (
                      <Box
                        component="th"
                        key={header.id}
                        sx={{
                          border: '1px solid #ddd',
                          padding: '12px',
                          textAlign: 'left',
                          fontWeight: 'bold',
                        }}
                      >
                        {header.isPlaceholder
                          ? null
                          : flexRender(header.column.columnDef.header, header.getContext())}
                      </Box>
                    ))}
                  </Box>
                ))}
              </Box>
              <Box component="tbody">
                {table.getRowModel().rows.map(row => (
                  <Box
                    component="tr"
                    key={row.id}
                    sx={{
                      '&:nth-of-type(odd)': { backgroundColor: '#fafafa' },
                      '&:hover': { backgroundColor: '#f0f0f0' },
                    }}
                  >
                    {row.getVisibleCells().map(cell => (
                      <Box
                        component="td"
                        key={cell.id}
                        sx={{
                          border: '1px solid #ddd',
                          padding: '12px',
                        }}
                      >
                        {flexRender(cell.column.columnDef.cell, cell.getContext())}
                      </Box>
                    ))}
                  </Box>
                ))}
              </Box>
            </Box>

            <Box sx={{ marginTop: 3, display: 'flex', justifyContent: 'center' }}>
              <Pagination
                count={totalPages}
                page={pagination.pageIndex + 1}
                onChange={handleChangePage}
                color="primary"
                shape="rounded"
              />
            </Box>
          </>
        )}

        {/* MODAL */}
        <Dialog open={openModal} onClose={() => setOpenModal(false)}>
          <DialogTitle>Confirm Deletion</DialogTitle>
          <DialogContent>
            <DialogContentText>
              Are you sure you want to delete that user?
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setOpenModal(false)}>Cancel</Button>
            <Button
              onClick={() => {
                if (userToDelete) {
                  deleteUserMutation.mutate(userToDelete.id);
                }
              }}
              color="error"
              variant="contained"
            >
              Yes, delete
            </Button>
          </DialogActions>
        </Dialog>
      </Box>
    </Box>
  );
}
