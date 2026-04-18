import React, { useState } from 'react';
import { Upload, message, Typography, Card } from 'antd';
import { InboxOutlined } from '@ant-design/icons';
import apiClient from '../../api/apiClient';

const { Dragger } = Upload;
const { Title, Text } = Typography;

const AdminUpload: React.FC = () => {
  const [uploading, setUploading] = useState(false);

  const customRequest = async (options: any) => {
    const { file, onSuccess, onError, onProgress } = options;
    const formData = new FormData();
    formData.append('files', file);

    const config = {
      headers: { 
        'Content-Type': 'multipart/form-data',
        // Authorization Bearer는 apiClient의 인터셉터가 자동으로 주입합니다.
      },
      onUploadProgress: (event: any) => {
        onProgress({ percent: (event.loaded / event.total) * 100 });
      }
    };

    try {
      setUploading(true);
      const res = await apiClient.post('/admin/resumes/upload/multi', formData, config);
      onSuccess(res.data, file);
      message.success(`${file.name} 데이터를 AI 인큐베이터로 전송했습니다.`);
    } catch (err: any) {
      onError(err);
      message.error(`${file.name} 업로드에 실패했습니다. (토큰이 만료되었거나 에이전트 오프라인)`);
    } finally {
      setUploading(false);
    }
  };

  return (
    <div>
      <Title level={3} style={{ color: '#E0B553' }}>AI Batch Resume Processor</Title>
      <Text style={{ display: 'block', marginBottom: 40, fontSize: 16, color: '#aaa' }}>
        수십 장의 이력서를 드롭존에 던져주세요. AI 엔진이 비정형 텍스트를 분석하여 시스템에 정형화 배열합니다.
      </Text>

      <Card style={{ background: 'rgba(0,0,0,0.1)', borderColor: '#333' }}>
        <Dragger
          customRequest={customRequest}
          multiple={true}
          showUploadList={{ showRemoveIcon: true }}
          style={{
            padding: '80px 0',
            background: 'rgba(224, 181, 83, 0.05)',
            borderColor: '#E0B553',
            borderStyle: 'dashed'
          }}
        >
          <p className="ant-upload-drag-icon">
            <InboxOutlined style={{ color: '#E0B553', fontSize: 72 }} />
          </p>
          <p className="ant-upload-text" style={{ color: '#f5f5f5', fontSize: 20, marginTop: 24 }}>
            이 거대한 영역 안으로 당신의 데이터를 끌어다 놓으세요
          </p>
          <p className="ant-upload-hint" style={{ color: '#888', marginTop: 16 }}>
            * PDF, DOC, DOCX 등 다수 포맷의 문서 파일이 호환됩니다.
          </p>
        </Dragger>
      </Card>
    </div>
  );
};

export default AdminUpload;
