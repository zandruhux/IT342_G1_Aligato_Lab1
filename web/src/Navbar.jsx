import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { authAPI } from './api';

export function Navbar() {
  const navigate = useNavigate();
  const location = useLocation();
  const token = localStorage.getItem('token');
  const user = localStorage.getItem('user');
  const [showLogoutModal, setShowLogoutModal] = useState(false);

  const handleLogout = async () => {
    try {
      await authAPI.logout();
    } catch (err) {
      console.error('Logout error:', err);
    } finally {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      setShowLogoutModal(false);
      navigate('/login');
    }
  };

  const isAuthPage = location.pathname === '/login' || location.pathname === '/register';

  return (
    <nav className="bg-indigo-600 text-white shadow-lg">
      <div className="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
        <div className="text-2xl font-bold">Auth System</div>

        <div className="space-x-4">
          {token && user ? (
            <>
              <span className="text-indigo-200">
                Welcome, {JSON.parse(user).firstName}!
              </span>
              <button
                onClick={() => setShowLogoutModal(true)}
                className="bg-red-500 hover:bg-red-600 px-4 py-2 rounded-lg transition duration-200"
              >
                Logout
              </button>
            </>
          ) : !isAuthPage ? (
            <>
              <button
                onClick={() => navigate('/login')}
                className="bg-indigo-700 hover:bg-indigo-800 px-4 py-2 rounded-lg transition duration-200"
              >
                Login
              </button>
              <button
                onClick={() => navigate('/register')}
                className="bg-indigo-700 hover:bg-indigo-800 px-4 py-2 rounded-lg transition duration-200"
              >
                Register
              </button>
            </>
          ) : null}
        </div>
      </div>

      {/* Logout Confirmation Modal */}
      {showLogoutModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-xl p-6 max-w-md w-full mx-4">
            <h2 className="text-xl font-bold text-gray-900 mb-4">Confirm Logout</h2>
            <p className="text-gray-600 mb-6">
              Are you sure you want to logout? You will need to login again to access your account.
            </p>
            <div className="flex gap-3 justify-end">
              <button
                onClick={() => setShowLogoutModal(false)}
                className="px-4 py-2 bg-gray-200 hover:bg-gray-300 text-gray-800 rounded-lg transition duration-200"
              >
                Cancel
              </button>
              <button
                onClick={handleLogout}
                className="px-4 py-2 bg-red-500 hover:bg-red-600 text-white rounded-lg transition duration-200"
              >
                Logout
              </button>
            </div>
          </div>
        </div>
      )}
    </nav>
  );
}
