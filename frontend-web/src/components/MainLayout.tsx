import React from 'react';
import { Layout, Menu, Typography } from 'antd';
import { Link, Outlet, useLocation } from 'react-router-dom';
import { ContainerOutlined, FileAddOutlined, LoginOutlined } from '@ant-design/icons';

const { Header, Content, Footer, Sider } = Layout;
const { Title } = Typography;

const MainLayout: React.FC = () => {
  const location = useLocation();

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
