import React, { useState } from 'react';
import { Card, Form, Input, Button, Tabs, message, Typography } from 'antd';
import { LockOutlined, UserOutlined } from '@ant-design/icons';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const { Title, Text } = Typography;

const Login: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const onLogin = async (values: any) => {
    setLoading(true);
    try {
      const res = await axios.post('http://localhost:8080/api/v1/auth/login', values);
      localStorage.setItem('token', res.data.accessToken);
      message.success('럭셔리-정 시스템에 연결되었습니다.');
      navigate('/resumes');
    } catch (err) {
      message.error('접근 권한이 없거나 비밀번호가 틀렸습니다.');
    } finally {
      setLoading(false);
    }
  };

  const onSignup = async (values: any) => {
    setLoading(true);
    try {
      await axios.post('http://localhost:8080/api/v1/auth/signup', {
          ...values,
          role: 'ADMIN' // 파일 처리를 위해 현재 가입되는 계정은 어드민 권한 처리
      });
      message.success('데이터망 접근 인가 자격이 부여되었습니다. 다시 로그인하여 주십시오.');
    } catch (err) {
      message.error('가입에 실패했습니다.');
    } finally {
      setLoading(false);
    }
  };

  const renderForm = (onFinish: (v: any) => void, isLogin: boolean) => (
    <Form name={isLogin ? 'login' : 'signup'} onFinish={onFinish} layout="vertical" size="large">
      <Form.Item name="email" rules={[{ required: true, message: '이메일을 입력하세요' }]}>
        <Input prefix={<UserOutlined />} placeholder="Admin Token Email" />
      </Form.Item>
      <Form.Item name="password" rules={[{ required: true, message: '비밀번호를 입력하세요' }]}>
        <Input.Password prefix={<LockOutlined />} placeholder="Secret Key" />
      </Form.Item>
      <Form.Item>
        <Button type="primary" htmlType="submit" loading={loading} block>
          {isLogin ? '인증 프로토콜 진입' : '새로운 자원 등록'}
        </Button>
      </Form.Item>
    </Form>
  );

  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', minHeight: '65vh' }}>
      <Title level={2} style={{ color: '#E0B553', marginBottom: 24, letterSpacing: '2px', textAlign: 'center' }}>
        Welcome to Luxury-Jung
      </Title>
      <Text style={{ color: '#888', marginBottom: 40, display: 'block', textAlign: 'center' }}>
        Enterprise AI Resume Data Center
      </Text>
      <Card
        className="glass-effect"
        style={{ width: 420, border: 'none', borderRadius: 16 }}
      >
        <Tabs
          defaultActiveKey="1"
          centered
          items={[
            { key: '1', label: 'Sign In', children: renderForm(onLogin, true) },
            { key: '2', label: 'Create Credentials', children: renderForm(onSignup, false) },
          ]}
        />
      </Card>
      <Text style={{ marginTop: 32, fontSize: 12, color: '#555' }}>Fully Powered By Antigravity AI</Text>
    </div>
  );
};

export default Login;
