import React from 'react';
import Button from '@mui/material/Button';

    const CustomButton = ({ 
        children, 
        onClick, 
        startIcon, 
        disabled = false, 
        sx = {}, 
        ...props 
    }) => {
        return (
            <Button
                sx={{
                    backgroundColor: '#4A9782',
                    color: "#FFF9E5",
                    fontWeight: "600",
                    padding: "10px",
                    height:"30px",
                    
                    ...sx
                }}
                startIcon={startIcon}
                onClick={onClick}
                disabled={disabled}
                {...props}
            >
                {children}
            </Button>
        );
    };

    export default CustomButton;