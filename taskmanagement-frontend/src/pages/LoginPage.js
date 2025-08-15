import { Box, Button, Divider, Typography } from '@mui/material';
import GoogleIcon from '@mui/icons-material/Google';
import { useAuth } from '../Context/AuthContext';
 
const LoginPage = () => {
  const { loginWithGoogle } = useAuth();

  return (
   <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: { xs: "flex-start", sm: "center" },
        height: "100vh",
        background: "#F0F4F9",
        
      }}
    >
      <Box
        sx={{
          height: { xs: "100vh", md: "auto",sm:"50vh" },
          width: { xs: "100%", sm:  "100%" },
          maxWidth: { lg: 600, md: 500 ,sm:400},
          backgroundColor: "#fff",
          borderRadius: { xs: 0, sm: 6 },
          display: "flex",
          
          flexDirection: "column"
        }}
      >
        
        <Box sx={{ display: "flex", alignItems: "center", p: 2 }}>
          <GoogleIcon sx={{ color: "#4285F4", mr: 1 }} />
          <Typography variant="body2">Sign in with Google</Typography>
        </Box>

        <Divider />
        <Box sx={{p:5,display: 'flex', flexDirection: 'column', alignItems: 'center',justifyContent:"center"}}>


      
    

      <Typography variant="h4" component="h1" align="center" gutterBottom>
        Welcome to Task Management
      </Typography>
      <Typography variant="body1" align="center" sx={{ mb: 2 }}>
        Please sign in with your Google account
        </Typography>
        <Box sx={{ display: 'flex', justifyContent: 'center' }}>
          <Button
            variant="outlined"
            startIcon={<GoogleIcon sx={{ color: '#4285F4' }} />}
            onClick={loginWithGoogle}
            sx={{ mt: 2, color: '#4285F4', border: '2px solid #4285F4', fontWeight: 'bold' }}
            >
            Sign in with Google
          </Button>
        </Box>
    
            </Box>
            </Box>
    </Box>
  );
};

export default LoginPage;