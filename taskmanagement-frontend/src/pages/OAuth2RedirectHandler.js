import { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../Context/AuthContext';
 
const OAuth2RedirectHandler = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { handleOAuthRedirect } = useAuth();

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const jwtToken = params.get('token');

    if (jwtToken) {
      handleOAuthRedirect(jwtToken);
    } else {
      navigate('/login', { state: { error: 'Authentication failed' } });
    }
  }, [location, navigate, handleOAuthRedirect]);

  return <div>Processing authentication...</div>;
};

export default OAuth2RedirectHandler;