import {BrowserRouter as Router, Navigate, Route, Routes, useLocation} from 'react-router-dom';
import {AuthProvider, useAuth} from './context/AuthContext';
import Login from './pages/Login';
import UsersPage from './pages/UsersPage';
import Dashboard from './pages/Dashboard';
import RoleRoute from './components/RoleRoute';
import {UserRole} from './types/UserRole';
import ReportsPage from "./pages/ReportsPage.tsx";
import {Button, Typography} from "@mui/material";
import {FC, JSX} from "react";
import LeadsPage from "./pages/LeadsPage.tsx";
import MyClientsPage from "./pages/MyClientsPage.tsx";
import AccountDetailPage from "./pages/AccountDetailPage.tsx";

const PrivateRoute: React.FC<{ children: React.ReactNode }> = ({children}) => {
    const {isAuthenticated} = useAuth();
    return isAuthenticated ? <>{children}</> : <Navigate to="/login"/>;
};

interface ProtectedRouteProps {
    children: JSX.Element;
    allowedRoles?: UserRole[];
    redirectPath?: string;
}

const ProtectedRoute = ({
                            children,
                            allowedRoles = [],
                            redirectPath = '/login'
                        }: ProtectedRouteProps) => {
    const {user, isAuthenticated} = useAuth();
    const location = useLocation();

    if (!isAuthenticated) {
        return <Navigate to={redirectPath} state={{from: location}} replace/>;
    }

    if (allowedRoles.length > 0 && (!user || !allowedRoles.includes(user.role))) {
        return (
            <div style={{padding: '2rem', textAlign: 'center'}}>
                <Typography variant="h5" color="error">
                    Access Denied
                </Typography>
                <Typography variant="body1" mt={2}>
                    You don't have permission to view this page.
                </Typography>
                <Button
                    variant="contained"
                    color="primary"
                    sx={{mt: 3}}
                    onClick={() => window.history.back()}
                >
                    Go Back
                </Button>
            </div>
        );
    }

    return children;
};

const App: FC = () => {
    return (
        <AuthProvider>
            <Router>
                <Routes>
                    <Route path="/login" element={<Login/>}/>
                    <Route
                        path="/"
                        element={
                            <PrivateRoute>
                                <Navigate to="/dashboard"/>
                            </PrivateRoute>
                        }
                    />
                    <Route path="/reports" element={
                        <ProtectedRoute allowedRoles={[UserRole.ADMIN, UserRole.MANAGER]}>
                            <ReportsPage/>
                        </ProtectedRoute>
                    }/>
                    <Route path="/leads" element={
                        <ProtectedRoute allowedRoles={[UserRole.SALESPERSON, UserRole.MANAGER]}>
                            <LeadsPage/>
                        </ProtectedRoute>
                    }/>
                    <Route path="/my-clients" element={
                        <ProtectedRoute allowedRoles={[UserRole.SALESPERSON, UserRole.MANAGER]}>
                            <MyClientsPage/>
                        </ProtectedRoute>
                    }/>
                    <Route
                        path="/users"
                        element={
                            <RoleRoute allowedRoles={[UserRole.ADMIN, UserRole.MANAGER]}>
                                <UsersPage/>
                            </RoleRoute>
                        }
                    />
                    <Route
                        path="/dashboard"
                        element={
                            <PrivateRoute>
                                <Dashboard/>
                            </PrivateRoute>
                        }
                    />
                    <Route path="/accounts/:accountId" element={
                        <ProtectedRoute allowedRoles={[UserRole.SALESPERSON, UserRole.MANAGER, UserRole.ADMIN]}>
                            <AccountDetailPage/>
                        </ProtectedRoute>
                    }/>
                </Routes>
            </Router>
        </AuthProvider>
    );
};

export default App;
