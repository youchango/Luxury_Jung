import React, { useEffect, useState } from 'react';
import { Table, Tag, Typography, Slider, Select, Row, Col, Card } from 'antd';
import axios from 'axios';

const { Title, Text } = Typography;
const { Option } = Select;

// Antd Data 컬럼 정의
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
    title: '데이터 삽입 일시',
    dataIndex: 'careerStartDate', // 추후 Entity의 createdAt 등으로 변경 가능
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

  const fetchResumes = async () => {
    setLoading(true);
    try {
      const config = {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` },
        params: {
          minCareer: minCareer,
          techStacks: techFilter.join(','),
          page: page - 1, // 백엔드 Pageable은 0부터 시작함
          size: 10
        }
      };
      // 백엔드 API (QueryDSL 컨트롤러 연동)
      const res = await axios.get('http://localhost:8080/api/v1/resumes', config);
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

      {/* 동적 검색 결과 데이터 테이블 */}
      <Table 
        columns={columns} 
        dataSource={data} 
        rowKey="id"
        loading={loading}
        pagination={{
          current: page,
          pageSize: 10,
          total: total,
          onChange: (p) => setPage(p),
          position: ['bottomCenter']
        }}
        rowClassName={() => 'table-row-luxury'}
        style={{ background: 'transparent' }}
      />
    </div>
  );
};

export default ResumeExplorer;
