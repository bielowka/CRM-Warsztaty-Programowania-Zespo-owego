import { Box } from '@mui/material';
import Sidebar from './components/Sidebar';
import UsersPage from './pages/UsersPage';

function App() {
  return (
      <Box sx={{ display: 'flex' }}>
        <Sidebar />
        <Box sx={{ flexGrow: 1 }}>
          <UsersPage />
        </Box>
      </Box>
  );
}

export default App;
