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
    Box,
    Chip
} from '@mui/material';
import {formatCurrency} from '../../utils/formatUtils';
import {Team, TeamSalesReportDTO} from "../../api/reportsApi.ts";

interface TeamReportsTableProps {
    reports: TeamSalesReportDTO[];
    teams: Team[];
}

const TeamReportsTable: React.FC<TeamReportsTableProps> = ({reports, teams}) => {
    const getTeamDetails = (teamId: number) => {
        return teams.find(team => team.id === teamId);
    };

    return (
        <TableContainer component={Paper} elevation={3}>
            <Table>
                <TableHead>
                    <TableRow sx={{bgcolor: '#f5f7fa'}}>
                        <TableCell>
                            <TableSortLabel>Team</TableSortLabel>
                        </TableCell>
                        <TableCell align="right">Team Size</TableCell>
                        <TableCell align="right">Closed Deals</TableCell>
                        <TableCell align="right">Total Sales</TableCell>
                        <TableCell align="right">Avg. Deal Size</TableCell>
                        <TableCell align="right">Sales per Member</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {reports.map((report) => {
                        const team = getTeamDetails(report.teamId);
                        return (
                            <TableRow key={report.teamId} hover>
                                <TableCell>
                                    <Box sx={{display: 'flex', alignItems: 'center'}}>
                                        <Chip
                                            label={report.teamName}
                                            size="small"
                                            sx={{
                                                mr: 1,
                                                bgcolor: team ? '#e3f2fd' : '#f5f5f5',
                                                fontWeight: 'medium'
                                            }}
                                        />
                                    </Box>
                                </TableCell>
                                <TableCell align="right">{report.teamSize}</TableCell>
                                <TableCell align="right">{report.dealsCount}</TableCell>
                                <TableCell align="right">
                                    <Typography fontWeight="medium">
                                        {formatCurrency(report.totalSales)}
                                    </Typography>
                                </TableCell>
                                <TableCell align="right">
                                    {formatCurrency(report.totalSales / report.dealsCount)}
                                </TableCell>
                                <TableCell align="right">
                                    {formatCurrency(report.totalSales / report.teamSize)}
                                </TableCell>
                            </TableRow>
                        );
                    })}
                </TableBody>
            </Table>
        </TableContainer>
    );
};

export default TeamReportsTable;