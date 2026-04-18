import React from 'react';
import { Navigate } from 'react-router-dom';

interface PrivateRouteProps {
  children: React.ReactElement;
}

/**
 * 인증되지 않은 사용자가 보호된 라우트에 접근하는 것을 차단하는 가드 컴포넌트입니다.
 * localStorage에 JWT 토큰이 없으면 즉시 /login 페이지로 리다이렉트합니다.
 */
const PrivateRoute: React.FC<PrivateRouteProps> = ({ children }) => {
  const token = localStorage.getItem('token');

  if (!token) {
    // 토큰이 없으면 로그인 페이지로 강제 이동 (replace: 뒤로가기 방지)
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default PrivateRoute;
