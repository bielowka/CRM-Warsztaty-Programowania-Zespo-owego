import React, {useState} from 'react';
import {Box, Typography, Tabs, Tab, Select, MenuItem, FormControl, InputLabel} from '@mui/material';
import IndividualReportsTable from '../components/reports/IndividualReportsTable';
import TeamReportsTable from '../components/reports/TeamReportsTable';
import {fetchTeams, fetchIndividualReports, fetchTeamReports} from '../api/reportsApi.ts';
import LoadingSpinner from '../components/common/LoadingSpinner';
import ErrorAlert from '../components/common/ErrorAlert';
import EmptyState from "../components/common/EmptyState.tsx";
import {formatMonthYear} from '../utils/formatUtils.ts';
import {useQuery} from "@tanstack/react-query";
import Sidebar from "../components/Sidebar.tsx";

const ReportsPage = () => {

    const [reportType, setReportType] = useState<'individual' | 'team'>('individual');
    const [year, setYear] = useState<number>(new Date().getFullYear());
    const [month, setMonth] = useState<number>(new Date().getMonth() + 1);

    const {
        data: teams,
        isLoading: teamsLoading,
    } = useQuery({
        queryKey: ['teams'],
        queryFn: fetchTeams,
    });

    const {
        data: individualReports,
        isLoading: individualLoading,
        error: individualError,
        refetch: refetchTeam,
    } = useQuery({
        queryKey: ['individualReports', year, month],
        queryFn: () => fetchIndividualReports(year, month),
    });

    const {
        data: teamReports,
        isLoading: teamLoading,
        error: teamError,
    } = useQuery({
        queryKey: ['teamReports', year, month],
        queryFn: () => fetchTeamReports(year, month),
        enabled: reportType === 'team',
    });

    const handleTabChange = (event: React.SyntheticEvent, newValue: 'individual' | 'team') => {
        setReportType(newValue);
    };

    const handleYearChange = (event: any) => {
        setYear(Number(event.target.value));
    };

    const handleMonthChange = (event: any) => {
        setMonth(Number(event.target.value));
    };

    const years = Array.from({length: 5}, (_, i) => year - 2 + i);


    return (
        <Box sx={{display: 'flex'}}>
            <Sidebar/>
            <Box sx={{p: 3, width: '100%'}}>
                <Typography variant="h4" gutterBottom>
                    Sales Rankings
                </Typography>
                <Box sx={{display: 'flex', justifyContent: 'space-between', mb: 3}}>
                    <Tabs value={reportType} onChange={handleTabChange}>
                        <Tab label="Individual Reports" value="individual"/>
                        <Tab label="Team Reports" value="team"/>
                    </Tabs>

                    <Box sx={{display: 'flex', gap: 2}}>
                        <FormControl sx={{minWidth: 120}}>
                            <InputLabel>Year</InputLabel>
                            <Select value={year} onChange={handleYearChange} label="Year">
                                {years.map(y => (
                                    <MenuItem key={y} value={y}>{y}</MenuItem>
                                ))}
                            </Select>
                        </FormControl>

                        <FormControl sx={{minWidth: 120}}>
                            <InputLabel>Month</InputLabel>
                            <Select value={month} onChange={handleMonthChange} label="Month">
                                {Array.from({length: 12}, (_, i) => i + 1).map(m => (
                                    <MenuItem key={m} value={m}>
                                        {new Date(0, m - 1).toLocaleString('default', {month: 'long'})}
                                    </MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                    </Box>
                </Box>

                {teamsLoading && <LoadingSpinner/>}
                {reportType === 'individual' ? (
                    <>
                        {individualLoading && <LoadingSpinner/>}
                        {individualError && <ErrorAlert message="Failed to load individual reports"/>}

                        {individualReports && individualReports.length > 0 ? (
                            <IndividualReportsTable reports={individualReports}/>
                        ) : (
                            !individualLoading && (
                                <Box sx={{p: 3, textAlign: 'center'}}>
                                    <Typography variant="h6" color="textSecondary">
                                        No individual sales data found
                                        for {formatMonthYear(year, month)}
                                    </Typography>
                                </Box>
                            )
                        )}
                    </>
                ) : (
                    <>
                        {teamLoading && <LoadingSpinner/>}
                        {teamError && (
                            <ErrorAlert
                                message="Failed to load team reports"
                                severity={"error"}
                                onRetry={() => refetchTeam()}
                            />
                        )}

                        {teamReports && teamReports.length > 0 ? (
                            <TeamReportsTable reports={teamReports} teams={teams || []}/>
                        ) : (
                            !teamLoading && !teamError && (
                                <EmptyState
                                    title="No team sales data"
                                    subtitle={`No team sales data found for ${formatMonthYear(year, month)}`}
                                />
                            )
                        )}
                    </>
                )}
            </Box>


        </Box>
    );
};

export default ReportsPage;