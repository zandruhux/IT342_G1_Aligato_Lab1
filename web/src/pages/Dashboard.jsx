import { useState, useEffect } from 'react';
import { authAPI } from '../api';

export function Dashboard() {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
    setLoading(false);
  }, []);

  if (loading) {
    return <div className="text-center py-12">Loading...</div>;
  }

  if (error) {
    return <div className="text-red-600 text-center py-12">{error}</div>;
  }

  return (
    <div className="max-w-4xl mx-auto">
      <div className="bg-white rounded-lg shadow-lg p-8">
        <h2 className="text-4xl font-bold text-gray-900 mb-8">Dashboard</h2>

        <div className="bg-gradient-to-r from-indigo-50 to-blue-50 rounded-lg p-6 mb-8">
          <h3 className="text-2xl font-semibold text-gray-800 mb-4">Welcome, {user?.firstName}!</h3>
          <p className="text-gray-600">You have successfully logged in to your account.</p>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div className="border border-gray-200 rounded-lg p-6">
            <h4 className="text-lg font-semibold text-gray-800 mb-3">Profile Information</h4>
            <div className="space-y-2 text-sm text-gray-600">
              <p>
                <span className="font-semibold">Name:</span> {user?.firstName} {user?.lastName}
              </p>
              <p>
                <span className="font-semibold">Email:</span> {user?.email}
              </p>
              <p>
                <span className="font-semibold">Role:</span> {user?.role || 'User'}
              </p>
            </div>
          </div>

          <div className="border border-gray-200 rounded-lg p-6">
            <h4 className="text-lg font-semibold text-gray-800 mb-3">Account Status</h4>
            <div className="space-y-2 text-sm text-gray-600">
              <p>
                <span className="font-semibold">Status:</span>{' '}
                <span className="inline-block bg-green-100 text-green-800 px-3 py-1 rounded-full text-xs">
                  Active
                </span>
              </p>
              <p>
                <span className="font-semibold">Member Since:</span> {user?.createdAt ? new Date(user.createdAt).toLocaleDateString() : 'N/A'}
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
