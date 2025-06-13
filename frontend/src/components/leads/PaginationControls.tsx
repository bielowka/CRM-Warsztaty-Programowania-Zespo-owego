import React from 'react';
import {
    Box,
    Pagination,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    Typography
} from '@mui/material';
import { PaginationOptions } from '../../api/leadsApi';

interface PaginationControlsProps {
    paginationOptions: PaginationOptions;
    totalPages: number;
    totalElements: number;
    onPaginationChange: (options: PaginationOptions) => void;
    compact?: boolean;
}

const pageSizeOptions = [5, 10, 20, 50];

const PaginationControls: React.FC<PaginationControlsProps> = ({
    paginationOptions,
    totalPages,
    totalElements,
    onPaginationChange,
    compact = false
}) => {
    const handlePageChange = (event: React.ChangeEvent<unknown>, page: number) => {
        onPaginationChange({
            ...paginationOptions,
            page: page - 1 // MUI Pagination
        });
    };

    const handlePageSizeChange = (newSize: number) => {
        onPaginationChange({
            page: 0,
            size: newSize
        });
    };

    const startItem = paginationOptions.page * paginationOptions.size + 1;
    const endItem = Math.min((paginationOptions.page + 1) * paginationOptions.size, totalElements);

    return (
        <Box 
            sx={{ 
                display: 'flex', 
                justifyContent: 'space-between', 
                alignItems: 'center',
                flexWrap: 'wrap',
                gap: 2,
                mt: 2,
                pt: 2,
                borderTop: '1px solid #e0e0e0'
            }}
        >
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                <Typography variant="body2" color="text.secondary">
                    Showing {startItem}-{endItem} of {totalElements} results
                </Typography>
                
                {!compact && (
                    <FormControl size="small" sx={{ minWidth: 80 }}>
                        <InputLabel>Per page</InputLabel>
                        <Select
                            value={paginationOptions.size}
                            label="Per page"
                            onChange={(e) => handlePageSizeChange(Number(e.target.value))}
                        >
                            {pageSizeOptions.map((size) => (
                                <MenuItem key={size} value={size}>
                                    {size}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                )}
            </Box>

            <Pagination
                count={totalPages}
                page={paginationOptions.page + 1} // MUI Pagination
                onChange={handlePageChange}
                color="primary"
                size={compact ? "small" : "medium"}
                showFirstButton
                showLastButton
            />
        </Box>
    );
};

export default PaginationControls; 