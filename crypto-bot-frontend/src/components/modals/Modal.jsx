// Modal genérico 
import React from 'react';
import { createPortal } from 'react-dom';

const Modal = ({ isOpen, onClose, title, children }) => {
  if (!isOpen) return null;

  return createPortal(
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-lg p-6">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-semibold">{title}</h2>
          <button
            className="text-gray-600 hover:text-gray-900"
            onClick={onClose}
          >
            ✕
          </button>
        </div>
        <div>{children}</div>
      </div>
    </div>,
    document.body
  );
};

export default Modal;
