import React from 'react';
import { Layout, Menu, Typography, Button } from 'antd';
import { Link, Outlet, useLocation, useNavigate } from 'react-router-dom';
import { ContainerOutlined, FileAddOutlined, LoginOutlined, LogoutOutlined } from '@ant-design/icons';
import { message } from 'antd';

const { Header, Content, Footer, Sider } = Layout;
const { Title } = Typography;

const MainLayout: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();

  /**
   * 로그아웃 처리: localStorage의 JWT 토큰을 삭제하고 로그인 페이지로 이동합니다.
   */
  const handleLogout = () => {
    localStorage.removeItem('token');
    message.success('안전하게 로그아웃 되었습니다.');
    navigate('/login');
  };

  return (
    <Layout style={{ minHeight: '100vh', background: 'transparent' }}>
      <Sider breakpoint="lg" collapsedWidth="0" theme="dark">
        <div style={{ padding: '24px 16px', textAlign: 'center' }}>
          <Title level={3} style={{ color: '#E0B553', margin: 0, fontWeight: 300, letterSpacing: '1px' }}>
            Luxury-Jung
          </Title>
        </div>
        <Menu theme="dark" mode="inline" selectedKeys={[location.pathname]} items={[
            { key: '/login', icon: <LoginOutlined />, label: <Link to="/login">회원 인증</Link> },
            { key: '/admin/upload', icon: <FileAddOutlined />, label: <Link to="/admin/upload">데이터 업로드</Link> },
            { key: '/resumes', icon: <ContainerOutlined />, label: <Link to="/resumes">이력서 검색 망</Link> },
        ]} />
        {/* 사이드바 하단 로그아웃 버튼 */}
        <div style={{ position: 'absolute', bottom: 24, left: 0, right: 0, padding: '0 16px' }}>
          <Button
            icon={<LogoutOutlined />}
            onClick={handleLogout}
            block
            danger
            type="text"
            style={{ color: '#ff7875', borderColor: 'transparent' }}
          >
            로그아웃
          </Button>
        </div>
      </Sider>
      <Layout style={{ background: '#121212' }}>
        <Header style={{ padding: 0, background: 'transparent' }} />
        <Content style={{ margin: '0 24px' }}>
          <div className="glass-effect" style={{ padding: 32, minHeight: '80vh', borderRadius: 16 }}>
            <Outlet />
          </div>
        </Content>
        <Footer style={{ textAlign: 'center', color: '#666' }}>
          Luxury-Jung Resume System ©2026 Created by Antigravity AI
        </Footer>
      </Layout>
    </Layout>
  );
};

export default MainLayout;
