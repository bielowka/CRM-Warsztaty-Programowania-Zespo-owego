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
import { Client } from '../features/clients/types';

interface Props {
    data: Client[];
    onDelete: (client: Client) => void;
    onEdit: (client: Client) => void;
    onRowClick: (client: Client) => void;
}

const columnHelper = createColumnHelper<Client>();

export default function ClientTable({ data, onDelete, onEdit, onRowClick }: Props) {
    const [pagination, setPagination] = useState({ pageIndex: 0, pageSize: 8 });

    const columns = [
        columnHelper.accessor('firstName', { header: 'First Name' }),
        columnHelper.accessor('lastName', { header: 'Last Name' }),
        columnHelper.accessor('email', { header: 'Email' }),
        columnHelper.accessor(client => client.company?.name || '', {
            id: 'companyName',
            header: 'Company',
        }),
        columnHelper.accessor('phoneNumber', { header: 'Phone' }),
        columnHelper.accessor('accountStatus', { header: 'Status' }),
        {
            id: 'actions',
            header: 'Actions',
            cell: ({ row }: { row: { original: Client } }) => {
                const client = row.original;
                return (
                    <Box sx={{ display: 'flex', gap: 1 }}>
                        <IconButton
                            size="small"
                            sx={{ color: 'black' }}
                            onClick={(e) => {
                                e.stopPropagation();
                                onEdit(client);
                            }}
                        >
                            <EditIcon fontSize="small" />
                        </IconButton>
                        <IconButton
                            size="small"
                            sx={{ color: 'black' }}
                            onClick={(e) => {
                                e.stopPropagation();
                                onDelete(client);
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
                            onClick={() => onRowClick(row.original)}
                            sx={{
                                '&:nth-of-type(odd)': { backgroundColor: '#fafafa' },
                                '&:hover': { backgroundColor: '#f0f0f0', cursor: 'pointer' },
                            }}
                        >
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
