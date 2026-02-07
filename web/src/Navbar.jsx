import { useNavigate, useLocation } from 'react-router-dom';
import { authAPI } from './api';

export function Navbar() {
  const navigate = useNavigate();
  const location = useLocation();
  const token = localStorage.getItem('token');
  const user = localStorage.getItem('user');

  const handleLogout = async () => {
    try {
      await authAPI.logout();
    } catch (err) {
      console.error('Logout error:', err);
    } finally {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
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
                onClick={handleLogout}
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
    </nav>
  );
}
