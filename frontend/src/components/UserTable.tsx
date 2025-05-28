import {
    useReactTable,
    getCoreRowModel,
    flexRender,
    createColumnHelper,
    getPaginationRowModel,
} from '@tanstack/react-table';
import { Box, IconButton, Pagination } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import { useState } from 'react';
import { User } from '../features/users/types';

interface Props {
    data: User[];
    onDelete: (user: User) => void;
    onEdit: (user: User) => void;
}

const columnHelper = createColumnHelper<User>();

export default function UserTable({ data, onDelete, onEdit }: Props) {
    const [pagination, setPagination] = useState({ pageIndex: 0, pageSize: 8 });

    const columns = [
        columnHelper.accessor('firstName', { header: 'First Name' }),
        columnHelper.accessor('lastName', { header: 'Last Name' }),
        columnHelper.accessor('email', { header: 'Email' }),
        columnHelper.accessor('role', { header: 'Role' }),
        columnHelper.accessor('position', { header: 'Position' }),
        {
            id: 'actions',
            header: 'Actions',
            cell: ({ row }: { row: { original: User } }) => {
                const user = row.original;
                return (
                    <Box sx={{ display: 'flex', gap: 1 }}>
                        <IconButton
                            size="small"
                            sx={{ color: 'black' }}
                            onClick={() => onEdit(user)}
                        >
                            <EditIcon fontSize="small" />
                        </IconButton>
                        <IconButton
                            size="small"
                            sx={{ color: 'black' }}
                            onClick={() => onDelete(user)}
                        >
                            <DeleteIcon fontSize="small" />
                        </IconButton>
                    </Box>
                );
            },
        },
    ];

    const table = useReactTable({
        data,
        columns,
        pageCount: Math.ceil(data.length / pagination.pageSize),
        state: { pagination },
        onPaginationChange: setPagination,
        getCoreRowModel: getCoreRowModel(),
        getPaginationRowModel: getPaginationRowModel(),
    });

    const handleChangePage = (_: any, newPage: number) => {
        setPagination(prev => ({ ...prev, pageIndex: newPage - 1 }));
    };

    return (
        <>
            <Box component="table" sx={{ width: '100%', borderCollapse: 'collapse' }}>
                <Box component="thead" sx={{ bgcolor: '#f5f5f5' }}>
                    {table.getHeaderGroups().map(headerGroup => (
                        <Box component="tr" key={headerGroup.id}>
                            {headerGroup.headers.map(header => (
                                <Box
                                    component="th"
                                    key={header.id}
                                    sx={{ border: '1px solid #ddd', padding: '12px', textAlign: 'left', fontWeight: 'bold' }}
                                >
                                    {header.isPlaceholder ? null : flexRender(header.column.columnDef.header, header.getContext())}
                                </Box>
                            ))}
                        </Box>
                    ))}
                </Box>
                <Box component="tbody">
                    {table.getRowModel().rows.map(row => (
                        <Box component="tr" key={row.id} sx={{ '&:nth-of-type(odd)': { backgroundColor: '#fafafa' }, '&:hover': { backgroundColor: '#f0f0f0' } }}>
                            {row.getVisibleCells().map(cell => (
                                <Box
                                    component="td"
                                    key={cell.id}
                                    sx={{ border: '1px solid #ddd', padding: '12px' }}
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
                    count={Math.ceil(data.length / pagination.pageSize)}
                    page={pagination.pageIndex + 1}
                    onChange={handleChangePage}
                    color="primary"
                    shape="rounded"
                />
            </Box>
        </>
    );
}