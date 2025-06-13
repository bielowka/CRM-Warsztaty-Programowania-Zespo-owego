import React from 'react';
import {
    Box,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    ToggleButton,
    ToggleButtonGroup,
    Typography
} from '@mui/material';
import { ArrowUpward, ArrowDownward } from '@mui/icons-material';
import { SortOptions } from '../../api/leadsApi';

interface LeadsSortControlsProps {
    sortOptions: SortOptions;
    onSortChange: (sortOptions: SortOptions) => void;
    compact?: boolean;
}

const sortFields = [
    { value: 'id', label: 'ID' },
    { value: 'description', label: 'Description' },
    { value: 'status', label: 'Status' },
    { value: 'estimatedValue', label: 'Estimated Value' },
    { value: 'createdAt', label: 'Created Date' },
    { value: 'updatedAt', label: 'Updated Date' }
];

const LeadsSortControls: React.FC<LeadsSortControlsProps> = ({ 
    sortOptions, 
    onSortChange, 
    compact = false 
}) => {
    const handleSortFieldChange = (newSortBy: string) => {
        onSortChange({ ...sortOptions, sortBy: newSortBy });
    };

    const handleSortDirectionChange = (newDirection: 'asc' | 'desc') => {
        onSortChange({ ...sortOptions, sortDirection: newDirection });
    };

    return (
        <Box 
            sx={{ 
                display: 'flex', 
                gap: 2, 
                alignItems: 'center',
                flexWrap: compact ? 'wrap' : 'nowrap',
                mb: compact ? 1 : 2
            }}
        >
            {!compact && (
                <Typography variant="body2" sx={{ minWidth: 'fit-content' }}>
                    Sort by:
                </Typography>
            )}
            
            <FormControl size={compact ? "small" : "medium"} sx={{ minWidth: 140 }}>
                <InputLabel>Sort Field</InputLabel>
                <Select
                    value={sortOptions.sortBy}
                    label="Sort Field"
                    onChange={(e) => handleSortFieldChange(e.target.value)}
                >
                    {sortFields.map((field) => (
                        <MenuItem key={field.value} value={field.value}>
                            {field.label}
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>

            <ToggleButtonGroup
                value={sortOptions.sortDirection}
                exclusive
                onChange={(_, value) => value && handleSortDirectionChange(value)}
                size={compact ? "small" : "medium"}
            >
                <ToggleButton value="asc" aria-label="ascending">
                    <ArrowUpward fontSize={compact ? "small" : "medium"} />
                    {!compact && <span style={{ marginLeft: 4 }}>Asc</span>}
                </ToggleButton>
                <ToggleButton value="desc" aria-label="descending">
                    <ArrowDownward fontSize={compact ? "small" : "medium"} />
                    {!compact && <span style={{ marginLeft: 4 }}>Desc</span>}
                </ToggleButton>
            </ToggleButtonGroup>
        </Box>
    );
};

export default LeadsSortControls; 