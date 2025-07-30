import { Box, Button, Typography, Paper, Container } from '@mui/material';
import GoogleIcon from '@mui/icons-material/Google';
import { useAuth } from '../Context/AuthContext';
 
const LoginPage = () => {
  const { loginWithGoogle } = useAuth();

  return (
    <Container maxWidth="xs">
      <Paper elevation={3} sx={{ p: 4, mt: 8 }}>
        <Typography variant="h5" component="h1" align="center" gutterBottom>
          Welcome to Task Management
        </Typography>
        <Typography variant="body1" align="center" sx={{ mb: 3 }}>
          Please sign in with your Google account
        </Typography>
        <Box sx={{ display: 'flex', justifyContent: 'center' }}>
          <Button
            variant="contained"
            startIcon={<GoogleIcon />}
            onClick={loginWithGoogle}
            sx={{ mt: 2 }}
          >
            Sign in with Google
          </Button>
        </Box>
      </Paper>
    </Container>
  );
};

export default LoginPage;