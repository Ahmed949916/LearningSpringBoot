import React from 'react';
import { TextField } from '@mui/material';

const CustomInput = ({ 
  label,
  name,
  value,
  onChange,
  type = "text",
  required = false,
  fullWidth = true,
  margin = "normal",
  sx = {},
  ...props 
}) => {
  return (
    <TextField
      fullWidth={fullWidth}
      label={label}
      name={name}
      value={value}
      onChange={onChange}
      type={type}
      required={required}
      margin={margin}
      sx={{ 
        mb: 2,
        '& .MuiOutlinedInput-root': {
          '&.Mui-focused fieldset': {
            borderColor: 'primary.lightMain',
          },
        },
        '& .MuiInputLabel-root': {
          '&.Mui-focused': {
            color: 'primary.lightMain',
          },
        },
        ...sx
      }}
      {...props}
    />
  );
};

export default CustomInput;