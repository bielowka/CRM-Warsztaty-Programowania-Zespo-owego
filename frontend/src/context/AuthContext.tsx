import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../config/axios';
import { UserRole, isUserRole } from '../types/UserRole';

interface User {
  userId: number;
  email: string;
  firstName: string;
  lastName: string;
  role: UserRole;
}

interface AuthContextType {
  user: User | null;
  token: string | null;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
  isAuthenticated: boolean;
  isLoading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
  const [isLoading, setIsLoading] = useState<boolean>(true);

  const clearAuthState = () => {
    localStorage.removeItem('token');
    setToken(null);
    setUser(null);
    setIsAuthenticated(false);
  };

  useEffect(() => {
    const validateToken = async () => {
      const storedToken = localStorage.getItem('token');
      if (!storedToken) {
        setIsLoading(false);
        return;
      }

      try {
        const response = await api.post('/api/auth/validate');
        if (response.data.authenticated && response.data.role && isUserRole(response.data.role)) {
          setUser({
            userId: response.data.userId,
            email: response.data.email,
            firstName: response.data.firstName,
            lastName: response.data.lastName,
            role: response.data.role as UserRole,
          });
          setToken(storedToken);
          setIsAuthenticated(true);
        } else {
          clearAuthState();
        }
      } catch (error) {
        clearAuthState();
      } finally {
        setIsLoading(false);
      }
    };

    validateToken();
  }, []);

  const login = async (email: string, password: string) => {
    try {
      const response = await api.post('/api/auth/login', {
        email,
        password,
      });

      if (response.data.role && isUserRole(response.data.role)) {
        const { token: newToken, ...userData } = response.data;
        localStorage.setItem('token', newToken);
        setToken(newToken);
        setUser({
          ...userData,
          role: userData.role as UserRole,
        });
        setIsAuthenticated(true);
      } else {
        throw new Error('Invalid role received from server');
      }
    } catch (error) {
      clearAuthState();
      throw new Error('Login failed');
    }
  };

  const logout = () => {
    clearAuthState();
  };

  if (isLoading) {
    // TODO loading spinner
    return <div>Loading...</div>;
  }

  return (
    <AuthContext.Provider value={{ user, token, login, logout, isAuthenticated, isLoading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}; 