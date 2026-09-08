import { useState } from 'react';
import { Home, Store, Users, User, Shield, Thermometer, MapPin, Heart, CheckCircle, Search } from 'lucide-react';

interface Post {
  id: string;
  title: string;
  location: string;
  date: string;
  gender: string;
  authorName: string;
  brix: number;
  tags: string[];
  deposit: string;
}

const initialPosts: Post[] = [
  {
    id: '1',
    title: '성수동 핫플 카페 & 베이커리 함께 가실 분!',
    location: '서울 성동구 성수동',
    date: '오늘 오후 3:00',
    gender: '성별무관',
    authorName: '민지',
    brix: 38.5,
    tags: ['디저트', '카페투어', '인생샷'],
    deposit: '5,000원'
  },
  {
    id: '2',
    title: '강남역 파스타 맛집 동행 구해요 (2명 모집)',
    location: '서울 강남구 역삼동',
    date: '내일 저녁 6:30',
    gender: '여성전용',
    authorName: '지은',
    brix: 42.0,
    tags: ['양식', '저녁모임', '소소한수다'],
    deposit: '10,000원'
  },
  {
    id: '3',
    title: '홍대 연남동 퓨전 오마카세 예약 같이 가실 분',
    location: '서울 마포구 연남동',
    date: '이번주 토요일 저녁 7:00',
    gender: '성별무관',
    authorName: '현우',
    brix: 36.8,
    tags: ['오마카세', '미식가', '예약완료'],
    deposit: '20,000원'
  }
];

export default function App() {
  const [activeTab, setActiveTab] = useState<'home' | 'pro' | 'matches' | 'profile'>('home');
  const [posts] = useState<Post[]>(initialPosts);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
      {/* Header */}
      <header style={{
        background: 'white',
        padding: '14px 20px',
        borderBottom: '1px solid #EBE5F5',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        position: 'sticky',
        top: 0,
        zIndex: 10
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <img src="/logo.jpg" alt="Logo" style={{ width: '32px', height: '32px', borderRadius: '8px' }} />
          <h1 style={{ fontSize: '18px', fontWeight: 'bold', color: '#7C4DFF' }}>유미당</h1>
        </div>
        <span style={{ fontSize: '12px', background: '#EDE7F6', color: '#651FFF', padding: '4px 10px', borderRadius: '12px', fontWeight: 'bold' }}>
          너와 나의 동행
        </span>
      </header>

      {/* Main Content Area */}
      <main className="scroll-container" style={{ padding: '16px' }}>

        {/* Home Screen */}
        {activeTab === 'home' && (
          <div>
            {/* Safety Banner */}
            <div style={{
              background: '#F3E5F5',
              borderRadius: '14px',
              padding: '14px',
              marginBottom: '16px',
              display: 'flex',
              alignItems: 'center',
              gap: '12px',
              border: '1px solid #E1BEE7'
            }}>
              <div style={{ background: '#E1BEE7', padding: '8px', borderRadius: '50%', display: 'flex' }}>
                <Shield size={20} color="#7B1FA2" />
              </div>
              <div style={{ flex: 1 }}>
                <h3 style={{ fontSize: '14px', fontWeight: 'bold', color: '#4A148C', marginBottom: '2px' }}>유미당 안심 동행 수칙</h3>
                <p style={{ fontSize: '12px', color: '#6A1B9A' }}>공공장소에서 만남 및 사전 송금 요구 거절 수칙을 준수하세요.</p>
              </div>
            </div>

            {/* Filter / Search Bar */}
            <div style={{ display: 'flex', gap: '8px', marginBottom: '16px' }}>
              <div style={{ flex: 1, background: 'white', border: '1px solid #EBE5F5', borderRadius: '10px', padding: '8px 12px', display: 'flex', alignItems: 'center', gap: '8px' }}>
                <Search size={16} color="#9CA3AF" />
                <input placeholder="지역, 음식 종류 검색" style={{ border: 'none', outline: 'none', width: '100%', fontSize: '13px' }} />
              </div>
            </div>

            {/* Post List */}
            <h2 style={{ fontSize: '15px', fontWeight: 'bold', marginBottom: '12px', color: '#1E1F24' }}>실시간 모집 중인 동행</h2>
            {posts.map(post => (
              <div key={post.id} className="card">
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '6px' }}>
                  <span className="badge badge-purple">{post.gender}</span>
                  <span style={{ fontSize: '12px', color: '#6B7280', display: 'flex', alignItems: 'center', gap: '2px' }}>
                    <MapPin size={12} /> {post.location}
                  </span>
                </div>
                <h3 style={{ fontSize: '15px', fontWeight: 'bold', marginBottom: '8px', color: '#1E1F24' }}>{post.title}</h3>
                
                <div style={{ display: 'flex', gap: '6px', marginBottom: '12px' }}>
                  {post.tags.map((t, idx) => (
                    <span key={idx} style={{ fontSize: '11px', background: '#F8F5FF', color: '#7C4DFF', padding: '2px 6px', borderRadius: '4px' }}>#{t}</span>
                  ))}
                </div>

                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', paddingTop: '10px', borderTop: '1px solid #F3F4F6' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                    <span style={{ fontSize: '13px', fontWeight: '600' }}>{post.authorName}</span>
                    <span style={{ fontSize: '12px', color: '#9C27B0', fontWeight: 'bold', display: 'flex', alignItems: 'center', gap: '2px' }}>
                      <Thermometer size={14} /> {post.brix} Brix
                    </span>
                  </div>
                  <button className="btn-primary" style={{ width: 'auto', padding: '6px 14px', fontSize: '12px' }}>참여 신청</button>
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Pro Market Screen */}
        {activeTab === 'pro' && (
          <div>
            <div style={{ background: 'linear-gradient(135deg, #7C4DFF, #651FFF)', color: 'white', padding: '20px', borderRadius: '16px', marginBottom: '16px' }}>
              <span style={{ fontSize: '12px', background: 'rgba(255,255,255,0.2)', padding: '3px 8px', borderRadius: '8px' }}>프리미엄 동행</span>
              <h2 style={{ fontSize: '18px', fontWeight: 'bold', marginTop: '6px' }}>전문 푸드 가이드 오퍼</h2>
              <p style={{ fontSize: '12px', opacity: 0.9, marginTop: '4px' }}>인증된 미식 가이드와 함께 안전하고 특별한 식사 경험을 만들어보세요.</p>
            </div>

            <div className="card">
              <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
                <img src="/logo.jpg" style={{ width: '50px', height: '50px', borderRadius: '12px' }} />
                <div>
                  <h3 style={{ fontSize: '15px', fontWeight: 'bold' }}>셰프 출신 김도윤 가이드</h3>
                  <p style={{ fontSize: '12px', color: '#6B7280' }}>미슐랭 레스토랑 투어 및 꿀팁 제공</p>
                  <span style={{ fontSize: '12px', color: '#9C27B0', fontWeight: 'bold' }}>49.5 Brix</span>
                </div>
              </div>
              <button className="btn-primary" style={{ marginTop: '12px' }}>오퍼 상세보기</button>
            </div>
          </div>
        )}

        {/* Matches / My Companion Screen */}
        {activeTab === 'matches' && (
          <div>
            <h2 style={{ fontSize: '16px', fontWeight: 'bold', marginBottom: '12px' }}>나의 동행 내역</h2>
            <div className="card" style={{ textAlign: 'center', padding: '30px 16px' }}>
              <Heart size={40} color="#B388FF" style={{ margin: '0 auto 12px' }} />
              <h3 style={{ fontSize: '15px', fontWeight: 'bold', marginBottom: '6px' }}>예정된 동행 모임</h3>
              <p style={{ fontSize: '13px', color: '#6B7280' }}>성수동 핫플 카페 동행 (오늘 저녁 7시)</p>
              <div style={{ display: 'flex', justifyContent: 'center', gap: '8px', marginTop: '16px' }}>
                <span className="badge badge-purple">보증금 예치 완료</span>
                <span className="badge badge-purple">매너 당도 42.0 Brix</span>
              </div>
            </div>
          </div>
        )}

        {/* Profile Screen */}
        {activeTab === 'profile' && (
          <div>
            <div className="card" style={{ display: 'flex', alignItems: 'center', gap: '14px' }}>
              <img src="/logo.jpg" style={{ width: '60px', height: '60px', borderRadius: '50%' }} />
              <div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                  <h2 style={{ fontSize: '16px', fontWeight: 'bold' }}>홍길동 님</h2>
                  <CheckCircle size={16} color="#7C4DFF" />
                </div>
                <p style={{ fontSize: '12px', color: '#6B7280' }}>KYC 본인인증 완료</p>
                <div style={{ marginTop: '4px', fontSize: '13px', fontWeight: 'bold', color: '#9C27B0', display: 'flex', alignItems: 'center', gap: '4px' }}>
                  <Thermometer size={16} /> 당도: 38.5 Brix
                </div>
              </div>
            </div>

            <div className="card">
              <h3 style={{ fontSize: '14px', fontWeight: 'bold', marginBottom: '12px' }}>나의 인증 정보</h3>
              <div style={{ display: 'flex', justifyContent: 'space-between', padding: '10px 0', borderBottom: '1px solid #F3F4F6', fontSize: '13px' }}>
                <span>실명/KYC 인증</span>
                <span style={{ color: '#2E7D32', fontWeight: 'bold' }}>인증됨</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', padding: '10px 0', fontSize: '13px' }}>
                <span>에스크로 보증금</span>
                <span style={{ color: '#7C4DFF', fontWeight: 'bold' }}>50,000원 예치중</span>
              </div>
            </div>
          </div>
        )}

      </main>

      {/* Bottom Navigation */}
      <nav style={{
        background: 'white',
        borderTop: '1px solid #EBE5F5',
        display: 'flex',
        justifyContent: 'space-around',
        padding: '10px 0',
        position: 'absolute',
        bottom: 0,
        left: 0,
        right: 0
      }}>
        <button onClick={() => setActiveTab('home')} style={{ border: 'none', background: 'none', cursor: 'pointer', display: 'flex', flexDirection: 'column', alignItems: 'center', color: activeTab === 'home' ? '#7C4DFF' : '#9CA3AF' }}>
          <Home size={22} />
          <span style={{ fontSize: '11px', marginTop: '2px', fontWeight: activeTab === 'home' ? 'bold' : 'normal' }}>동행 찾기</span>
        </button>
        <button onClick={() => setActiveTab('pro')} style={{ border: 'none', background: 'none', cursor: 'pointer', display: 'flex', flexDirection: 'column', alignItems: 'center', color: activeTab === 'pro' ? '#7C4DFF' : '#9CA3AF' }}>
          <Store size={22} />
          <span style={{ fontSize: '11px', marginTop: '2px', fontWeight: activeTab === 'pro' ? 'bold' : 'normal' }}>전문 오퍼</span>
        </button>
        <button onClick={() => setActiveTab('matches')} style={{ border: 'none', background: 'none', cursor: 'pointer', display: 'flex', flexDirection: 'column', alignItems: 'center', color: activeTab === 'matches' ? '#7C4DFF' : '#9CA3AF' }}>
          <Users size={22} />
          <span style={{ fontSize: '11px', marginTop: '2px', fontWeight: activeTab === 'matches' ? 'bold' : 'normal' }}>나의 동행</span>
        </button>
        <button onClick={() => setActiveTab('profile')} style={{ border: 'none', background: 'none', cursor: 'pointer', display: 'flex', flexDirection: 'column', alignItems: 'center', color: activeTab === 'profile' ? '#7C4DFF' : '#9CA3AF' }}>
          <User size={22} />
          <span style={{ fontSize: '11px', marginTop: '2px', fontWeight: activeTab === 'profile' ? 'bold' : 'normal' }}>내 프로필</span>
        </button>
      </nav>
    </div>
  );
}
