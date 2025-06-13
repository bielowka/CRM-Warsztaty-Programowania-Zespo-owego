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
    Box
} from '@mui/material';
import { formatCurrency } from '../../utils/formatUtils';
import { LeadForTable, SortOptions, PaginationOptions } from '../../api/leadsApi';
import LeadsSortControls from './LeadsSortControls';
import PaginationControls from './PaginationControls';

interface LeadsTableProps {
    leads: LeadForTable[];
    onRowClick?: (lead: LeadForTable) => void;
    showCompanyColumns?: boolean;
    showDateColumns?: boolean;
    showSortControls?: boolean;
    showPaginationControls?: boolean;
    sortOptions?: SortOptions;
    onSortChange?: (sortOptions: SortOptions) => void;
    paginationOptions?: PaginationOptions;
    totalPages?: number;
    totalElements?: number;
    onPaginationChange?: (options: PaginationOptions) => void;
    compact?: boolean;
}

const LeadsTable: React.FC<LeadsTableProps> = ({ 
    leads, 
    onRowClick, 
    showCompanyColumns = true,
    showDateColumns = false,
    showSortControls = false,
    showPaginationControls = false,
    sortOptions,
    onSortChange,
    paginationOptions,
    totalPages = 0,
    totalElements = 0,
    onPaginationChange,
    compact = false 
}) => {
    const getStatusColor = (status: string) => {
        switch (status) {
            case 'NEW':
                return 'default';
            case 'QUALIFICATION':
                return 'primary';
            case 'NEEDS_ANALYSIS':
                return 'info';
            case 'VALUE_PROPOSITION':
                return 'secondary';
            case 'ID_DECISION_MAKERS':
                return 'warning';
            case 'PERCEPTION_ANALYSIS':
                return 'warning';
            case 'PROPOSAL':
                return 'info';
            case 'NEGOTIATION':
                return 'secondary';
            case 'CLOSED_WON':
                return 'success';
            case 'CLOSED_LOST':
                return 'error';
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

    if (leads.length === 0) {
        return (
            <Box>
                {showSortControls && sortOptions && onSortChange && (
                    <LeadsSortControls
                        sortOptions={sortOptions}
                        onSortChange={onSortChange}
                        compact={compact}
                    />
                )}
                <Box sx={{
                    p: compact ? 3 : 5,
                    border: '1px dashed #ccc',
                    borderRadius: 1,
                    textAlign: 'center',
                    backgroundColor: 'background.paper'
                }}>
                    <Typography variant={compact ? "body1" : "h6"} color="textSecondary">
                        No leads found
                    </Typography>
                    <Typography variant="body2" mt={1} color="textSecondary">
                        No sales opportunities for this client yet
                    </Typography>
                </Box>
            </Box>
        );
    }

    return (
        <Box>
            {showSortControls && sortOptions && onSortChange && (
                <LeadsSortControls
                    sortOptions={sortOptions}
                    onSortChange={onSortChange}
                    compact={compact}
                />
            )}
            
            <TableContainer component={Paper} elevation={compact ? 1 : 3}>
                <Table sx={{ minWidth: compact ? 400 : 650 }} aria-label="leads table" size={compact ? "small" : "medium"}>
                    <TableHead sx={{ backgroundColor: '#f5f7fa' }}>
                        <TableRow>
                            <TableCell>Description</TableCell>
                            <TableCell>Status</TableCell>
                            <TableCell align="right">Estimated Value</TableCell>
                            {showCompanyColumns && (
                                <>
                                    <TableCell>Company</TableCell>
                                    <TableCell>Industry</TableCell>
                                </>
                            )}
                            {showDateColumns && (
                                <>
                                    <TableCell>Created</TableCell>
                                    <TableCell>Updated</TableCell>
                                </>
                            )}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {leads.map((lead) => (
                            <TableRow
                                key={lead.id}
                                hover
                                onClick={() => onRowClick?.(lead)}
                                sx={{
                                    cursor: onRowClick ? 'pointer' : 'default',
                                    '&:hover': onRowClick ? { backgroundColor: 'action.hover' } : {}
                                }}
                            >
                                <TableCell>
                                    <Typography fontWeight="medium" variant={compact ? "body2" : "body1"}>
                                        {lead.description}
                                    </Typography>
                                </TableCell>
                                <TableCell>
                                    <Chip
                                        label={lead.status}
                                        color={getStatusColor(lead.status) as any}
                                        variant="outlined"
                                        size="small"
                                    />
                                </TableCell>
                                <TableCell align="right">
                                    <Typography variant={compact ? "body2" : "body1"} fontWeight="medium">
                                        {formatCurrency(lead.estimatedValue)}
                                    </Typography>
                                </TableCell>
                                {showCompanyColumns && (
                                    <>
                                        <TableCell>{lead.companyName || 'N/A'}</TableCell>
                                        <TableCell>{lead.companyIndustry || 'N/A'}</TableCell>
                                    </>
                                )}
                                {showDateColumns && (
                                    <>
                                        <TableCell>
                                            <Typography variant="body2">
                                                {formatDate(lead.createdAt)}
                                            </Typography>
                                        </TableCell>
                                        <TableCell>
                                            <Typography variant="body2">
                                                {formatDate(lead.updatedAt)}
                                            </Typography>
                                        </TableCell>
                                    </>
                                )}
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
                
                {showPaginationControls && paginationOptions && onPaginationChange && totalPages > 0 && (
                    <PaginationControls
                        paginationOptions={paginationOptions}
                        totalPages={totalPages}
                        totalElements={totalElements}
                        onPaginationChange={onPaginationChange}
                        compact={compact}
                    />
                )}
            </TableContainer>
        </Box>
    );
};

export default LeadsTable; 