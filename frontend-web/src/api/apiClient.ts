import axios from 'axios';
import { message } from 'antd';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api/v1',
  timeout: 30000,
});

apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && (error.response.status === 401 || error.response.status === 403)) {
      message.error('세션이 만료되었거나 접근 권한이 없습니다. 다시 로그인해 주세요.');
      localStorage.removeItem('token');
      // 글로벌 윈도우 객체를 이용해 안전하게 홈(로그인)으로 튕겨냄
      window.location.href = '/login';
    } else if (error.response?.data?.message) {
      // SpringBoot의 GlobalExceptionHandler가 던져준 메시지 표출
      message.error(error.response.data.message);
    } else {
      message.error('서버와의 통신에 실패했습니다.');
    }
    return Promise.reject(error);
  }
);

export default apiClient;
