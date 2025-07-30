import { useEffect } from 'react';
import { useAuth } from '../Context/AuthContext';
import { useSearchParams } from 'react-router-dom';

const OAuthRedirect = () => {
  const { handleOAuthRedirect, handleUserId } = useAuth();
  const [searchParams] = useSearchParams();

  useEffect(() => {
   
    const token = searchParams.get('token');
    const userId = searchParams.get('userId');
    const accessToken = searchParams.get('access_token');
    
 
    const finalToken = token || accessToken;
    if (userId) {
      handleUserId(userId);
    }else{
        console.error('No userId found in OAuth redirect');
    }
    if (finalToken) {
      handleOAuthRedirect(finalToken);
      
    } else {
      console.error('No token found in OAuth redirect');
    }
  
  }, [searchParams, handleOAuthRedirect, handleUserId]);

  return (
    <div className="flex items-center justify-center min-h-screen">
      <div className="text-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500 mx-auto"></div>
        <p className="mt-4 text-gray-600">Completing login...</p>
      </div>
    </div>
  );
};

export default OAuthRedirect;
