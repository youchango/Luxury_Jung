import React, { useEffect, useState } from 'react';
import { Table, Tag, Typography, Slider, Select, Row, Col, Card, Drawer, Descriptions, Divider } from 'antd';
import apiClient from '../../api/apiClient';

const { Title, Text } = Typography;
const { Option } = Select;

// Antd Data 컬럼 정의 (약식)
const columns = [
  {
    title: '후보자 식별명',
    dataIndex: 'name',
    key: 'name',
    render: (text: string) => <strong style={{ color: '#fff', fontSize: '15px' }}>{text || '익명'}</strong>,
  },
  {
    title: '연차 산정',
    dataIndex: 'yearsOfExperience',
    key: 'yearsOfExperience',
    render: (val: number) => <span style={{ color: '#E0B553', fontWeight: 600 }}>{val || 0}년차</span>,
  },
  {
    title: '매칭된 핵심기술 (AI 추출)',
    dataIndex: 'techStacks',
    key: 'techStacks',
    render: (tags: string[]) => (
      <>
        {tags?.map(tag => {
          let color = tag.length > 5 ? 'purple' : 'gold';
          return (
            <Tag color={color} key={tag} style={{ border: '1px solid rgba(224, 181, 83, 0.3)', background: 'transparent' }}>
              {tag}
            </Tag>
          );
        })}
      </>
    ),
  },
  {
    title: '경력 시작 일자',
    dataIndex: 'careerStartDate', 
    key: 'careerStartDate',
    render: (date: string) => <Text style={{ color: '#666' }}>{date || '-'}</Text>,
  },
];

const ResumeExplorer: React.FC = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [total, setTotal] = useState(0);
  
  // 조건 필터 State
  const [minCareer, setMinCareer] = useState<number>(0);
  const [techFilter, setTechFilter] = useState<string[]>([]);
  const [page, setPage] = useState(1);

  // Drawer 상태 관리
  const [drawerVisible, setDrawerVisible] = useState(false);
  const [resumeDetail, setResumeDetail] = useState<any>(null);

  const fetchResumes = async () => {
    setLoading(true);
    try {
      const res = await apiClient.get('/resumes', {
        params: { minCareer, techStacks: techFilter.join(','), page: page - 1, size: 10 }
      });
      setData(res.data.content);
      setTotal(res.data.totalElements);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchResumes();
  }, [minCareer, techFilter, page]);

  // 테이블 Row 클릭 핸들러 (상세조회)
  const handleRowClick = async (record: any) => {
    try {
      setLoading(true);
      const res = await apiClient.get(`/resumes/${record.id}`);
      setResumeDetail(res.data);
      setDrawerVisible(true);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <Title level={3} style={{ color: '#E0B553', marginBottom: 24 }}>전사적 인재 데이터 마이닝</Title>
      
      {/* 검색 필터 대시보드 컴포넌트 */}
      <Card style={{ marginBottom: 24, background: 'rgba(255,255,255,0.02)', borderColor: '#333' }}>
        <Row gutter={48}>
          <Col span={12}>
            <Text style={{ display: 'block', marginBottom: 12, color: '#aaa', fontWeight: 600 }}>1. 최소 지원 연차 한계치</Text>
            <Slider 
              marks={{ 0: '제한없음', 3: '주니어(3)', 5: '미들(5)', 10: '시니어(10)' }} 
              min={0} max={10} 
              defaultValue={0} 
              onAfterChange={(v) => { setMinCareer(v); setPage(1); }} 
            />
          </Col>
          <Col span={12}>
            <Text style={{ display: 'block', marginBottom: 12, color: '#aaa', fontWeight: 600 }}>2. 핵심 기술 스택 포함 여부 조사</Text>
            <Select
              mode="multiple"
              allowClear
              size="large"
              style={{ width: '100%' }}
              placeholder="추출할 기술을 골라주세요 (Java, Python ...)"
              onChange={(v) => { setTechFilter(v); setPage(1); }}
            >
              <Option key="Java">Java</Option>
              <Option key="Spring Boot">Spring Boot</Option>
              <Option key="Python">Python</Option>
              <Option key="React">React</Option>
              <Option key="JavaScript">JavaScript</Option>
              <Option key="TypeScript">TypeScript</Option>
              <Option key="AWS">AWS</Option>
              <Option key="Docker">Docker</Option>
            </Select>
          </Col>
        </Row>
      </Card>

      {/* 동적 검색 결과 데이터 테이블 (마우스 호버 시 Pointer 효과 추가) */}
      <Table 
        columns={columns} 
        dataSource={data} 
        rowKey="id"
        loading={loading}
        onRow={(record) => ({
          onClick: () => handleRowClick(record),
          style: { cursor: 'pointer' } 
        })}
        pagination={{ current: page, pageSize: 10, total: total, onChange: (p) => setPage(p), position: ['bottomCenter'] }}
        style={{ background: 'transparent' }}
      />

      {/* 우측 럭셔리 상세 조회 Drawer */}
      <Drawer
        title={<span style={{ color: '#E0B553' }}>인재 상세 포트폴리오</span>}
        width={720}
        onClose={() => setDrawerVisible(false)}
        open={drawerVisible}
        bodyStyle={{ paddingBottom: 80, background: '#1f1f1f' }}
      >
        {resumeDetail && (
          <div>
             <Descriptions title={<span style={{ color: '#fff' }}>기본 정보</span>} column={2} style={{ marginBottom: 32 }}>
                <Descriptions.Item label="이름">{resumeDetail.name || '익명'}</Descriptions.Item>
                <Descriptions.Item label="연차">{resumeDetail.yearsOfExperience || 0}년차</Descriptions.Item>
                <Descriptions.Item label="원본 파일명">{resumeDetail.originalFileName}</Descriptions.Item>
                <Descriptions.Item label="경력 시작일">{resumeDetail.careerStartDate || '-'}</Descriptions.Item>
             </Descriptions>
             <Divider style={{ borderColor: '#333' }} />
             
             <Title level={5} style={{ color: '#E0B553' }}>주요 기술(Tech Stack)</Title>
             <div style={{ marginBottom: 24 }}>
                {resumeDetail.techStacks?.map((s: string) => <Tag color="gold" key={s}>{s}</Tag>)}
             </div>

             <Title level={5} style={{ color: '#E0B553' }}>보유 스킬(Skills)</Title>
             <div style={{ marginBottom: 24 }}>
                {resumeDetail.skills?.map((s: string) => <Tag color="green" key={s}>{s}</Tag>)}
             </div>

             <Title level={5} style={{ color: '#E0B553' }}>경력 요약 기록(Career)</Title>
             <ul style={{ color: '#ddd' }}>
                 {resumeDetail.careers?.map((c: string, idx: number) => <li key={idx} style={{ marginBottom: 8 }}>{c}</li>)}
             </ul>
             
             <Title level={5} style={{ color: '#E0B553', marginTop: 24 }}>자격증(Certificates)</Title>
             <div style={{ marginBottom: 24 }}>
                {resumeDetail.certificates?.map((c: string) => <Tag color="purple" key={c}>{c}</Tag>)}
             </div>
          </div>
        )}
      </Drawer>
    </div>
  );
};

export default ResumeExplorer;
