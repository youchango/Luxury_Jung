import { ConfigProvider, theme } from 'antd';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import MainLayout from './components/MainLayout';
import Login from './pages/Login';
import AdminUpload from './pages/AdminUpload';
import ResumeExplorer from './pages/ResumeExplorer';
import PrivateRoute from './components/PrivateRoute';

function App() {
  return (
    // Ant Design 커스텀 테마: Dark Algorithm 기반 프라이머리 컬러(Gold) 적용
    <ConfigProvider
      theme={{
        algorithm: theme.darkAlgorithm,
        token: {
          colorPrimary: '#E0B553', // 럭셔리한 샴페인 골드 톤
          colorBgContainer: '#1a1a1a',
          colorBgElevated: '#2a2a2a',
          borderRadius: 8,
          fontFamily: "'Outfit', sans-serif"
        },
      }}
    >
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<MainLayout />}>
            <Route index element={<Navigate to="/login" replace />} />
            <Route path="login" element={<Login />} />
            {/* PrivateRoute로 보호된 페이지 - 로그인하지 않으면 /login으로 리다이렉트 */}
            <Route path="resumes" element={<PrivateRoute><ResumeExplorer /></PrivateRoute>} />
            <Route path="admin/upload" element={<PrivateRoute><AdminUpload /></PrivateRoute>} />
          </Route>
        </Routes>
      </BrowserRouter>
    </ConfigProvider>
  );
}

export default App;
