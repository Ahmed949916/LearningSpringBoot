import React from 'react';
import { FormControl, InputLabel, Select } from '@mui/material';

const CustomSelect = ({ 
  label,
  name,
  value,
  onChange,
  children,
  fullWidth = true,
  sx = {},
  ...props 
}) => {
  return (
    <FormControl 
      fullWidth={fullWidth} 
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
    >
      <InputLabel>{label}</InputLabel>
      <Select
        name={name}
        value={value}
        onChange={onChange}
        label={label}
        {...props}
      >
        {children}
      </Select>
    </FormControl>
  );
};

export default CustomSelect;