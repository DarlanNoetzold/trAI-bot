// Loader genérico 
import React from 'react';

const Loader = () => {
  return (
    <div className="flex items-center justify-center h-full p-4">
      <div className="w-8 h-8 border-4 border-blue-500 border-dashed rounded-full animate-spin"></div>
    </div>
  );
};

export default Loader;
