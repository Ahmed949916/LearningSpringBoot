import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { CssBaseline, ThemeProvider, createTheme } from '@mui/material';
import LoginPage from './pages/LoginPage';
import TasksPage from './pages/TasksPage';
import UsersPage from './pages/UsersPage';
import OAuth2RedirectHandler from './pages/OAuth2RedirectHandler';
import { AuthProvider } from './Context/AuthContext';
import Profile from './pages/Profile';
import Layout from './Components/Layout';
 
const theme = createTheme({
  palette: {
    primary: {
      main: '#004030',
      lightMain: '#4A9782',
      textLight:"#FFFFFF"
    },
    secondary: {
      main: '#f50057',
    },
  },
});

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <AuthProvider>
          <Layout>
            <Routes>
              <Route path="/" element={<LoginPage />} />
              <Route path="/login" element={<LoginPage />} />
              <Route path="/oauth2/redirect" element={<OAuth2RedirectHandler />} />
              <Route path="/profile" element={<Profile />}/>
              <Route path="/tasks" element={<TasksPage />} />
              <Route path="/users" element={<UsersPage />} />
              <Route path="*" element={<LoginPage />} />
            </Routes>
          </Layout>
        </AuthProvider>
      </Router>
    </ThemeProvider>
  );
}export default App;