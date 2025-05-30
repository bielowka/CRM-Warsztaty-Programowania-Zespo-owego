import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { UserRole, isUserRole } from '../types/UserRole';

interface RoleRouteProps {
  children: React.ReactNode;
  allowedRoles: UserRole[];
  redirectTo?: string;
}

const RoleRoute: React.FC<RoleRouteProps> = ({ 
  children, 
  allowedRoles, 
  redirectTo = '/dashboard' 
}) => {
  const { user, isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" />;
  }

  if (!user || !isUserRole(user.role) || !allowedRoles.includes(user.role)) {
    return <Navigate to={redirectTo} />;
  }

  return <>{children}</>;
};

export default RoleRoute; 