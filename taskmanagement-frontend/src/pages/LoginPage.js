import { Box, Button, Typography, Paper, Container } from '@mui/material';
import GoogleIcon from '@mui/icons-material/Google';
import { useAuth } from '../Context/AuthContext';
 
const LoginPage = () => {
  const { loginWithGoogle } = useAuth();

  return (
    <Box sx={{display:"flex", flexDirection:"column", alignItems:"center", justifyContent:"center", height:"100vh",background:"#eaf3ffff"}} >
      <Box sx={{  width: '400px', padding: 6, backgroundColor: '#fff', borderRadius: 7 }}>

      <Typography variant="h4" component="h1" align="center" gutterBottom>
        Welcome to Task Management
      </Typography>
      <Typography variant="body1" align="center" sx={{ mb: 3 }}>
        Please sign in with your Google account
        </Typography>
        <Box sx={{ display: 'flex', justifyContent: 'center' }}>
          <Button
            variant="outlined"
            startIcon={<GoogleIcon />}
            onClick={loginWithGoogle}
            sx={{ mt: 2 }}
            >
            Sign in with Google
          </Button>
        </Box>
    
            </Box>
    </Box>
  );
};

export default LoginPage;