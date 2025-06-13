import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Login from './pages/Login';
import UsersPage from './pages/UsersPage';
import Dashboard from './pages/Dashboard';
import RoleRoute from './components/RoleRoute';
import { UserRole } from './types/UserRole';
import MyClients from './pages/Clients';

const PrivateRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { isAuthenticated } = useAuth();
  return isAuthenticated ? <>{children}</> : <Navigate to="/login" />;
};

const App: React.FC = () => {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route
            path="/"
            element={
              <PrivateRoute>
                <Navigate to="/dashboard" />
              </PrivateRoute>
            }
          />
          <Route
            path="/users"
            element={
              <RoleRoute allowedRoles={[UserRole.ADMIN]}>
                <UsersPage />
              </RoleRoute>
            }
          />
          <Route
            path="/dashboard"
            element={
              <PrivateRoute>
                <Dashboard />
              </PrivateRoute>
            }
          />
          <Route path="/clients" element={<MyClients />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
};

export default App;
