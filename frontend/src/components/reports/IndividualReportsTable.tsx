import React from 'react';
import {
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    Typography,
    TableSortLabel,
} from '@mui/material';
import {formatCurrency} from '../../utils/formatUtils';
import {SalesRankingDTO} from "../../api/reportsApi.ts";

interface IndividualReportsTableProps {
    reports: SalesRankingDTO[];
}

const IndividualReportsTable: React.FC<IndividualReportsTableProps> = ({reports}) => {
    return (
        <TableContainer component={Paper} elevation={3}>
            <Table>
                <TableHead>
                    <TableRow sx={{bgcolor: '#f5f7fa'}}>
                        <TableCell>
                            <TableSortLabel>#</TableSortLabel>
                        </TableCell>
                        <TableCell>Salesperson</TableCell>
                        <TableCell align="right">Closed Deals</TableCell>
                        <TableCell align="right">Total Sales</TableCell>
                        <TableCell align="right">Avg. Deal Size</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {reports.map((report, index) => (
                        <TableRow key={report.userId} hover>
                            <TableCell>
                                <Typography fontWeight="bold">{index + 1}</Typography>
                            </TableCell>
                            <TableCell>
                                <Typography fontWeight="medium">
                                    {report.firstName} {report.lastName}
                                </Typography>
                            </TableCell>
                            <TableCell align="right">{report.dealsCount}</TableCell>
                            <TableCell align="right">
                                <Typography fontWeight="medium">
                                    {formatCurrency(report.totalAmount)}
                                </Typography>
                            </TableCell>
                            <TableCell align="right">
                                {formatCurrency(report.totalAmount / report.dealsCount)}
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
};

export default IndividualReportsTable;