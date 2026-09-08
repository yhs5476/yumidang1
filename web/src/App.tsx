import { useState } from 'react';
import { 
  Home, Store, Users, User, Shield, Thermometer, MapPin, Heart, CheckCircle, Search, 
  Plus, ChevronRight, X, UserCheck, Sparkles, Filter
} from 'lucide-react';

interface Post {
  id: string;
  title: string;
  category: string;
  categoryIcon: string;
  location: string;
  date: string;
  gender: string;
  authorName: string;
  brix: number;
  tags: string[];
  deposit: string;
  isKycVerified: boolean;
  content: string;
  currentParticipants: number;
  maxParticipants: number;
}

const CATEGORIES = [
  { id: 'ALL', label: '전체', icon: '🍽️' },
  { id: 'MEAL', label: '식사 동행', icon: '🍲' },
  { id: 'CAFE', label: '카페/디저트', icon: '☕' },
  { id: 'ALCOHOL', label: '반주/혼술', icon: '🍺' },
  { id: 'EXPLORE', label: '맛집 탐방', icon: '🏃' },
  { id: 'EXHIBITION', label: '전시/문화', icon: '🎨' },
];

const AREAS = ['전체', '종로구', '마포구', '성동구', '영등포구', '서초구', '강남구'];

const INITIAL_POSTS: Post[] = [
  {
    id: '1',
    title: '성수동 핫플 카페 & 베이커리 함께 가실 분!',
    category: 'CAFE',
    categoryIcon: '☕',
    location: '서울 성동구 성수동',
    date: '오늘 오후 3:00',
    gender: '성별무관',
    authorName: '김민지',
    brix: 38.5,
    tags: ['디저트', '카페투어', '인생샷'],
    deposit: '5,000원',
    isKycVerified: true,
    content: '요즘 성수동에서 가장 핫한 대형 베이커리 카페 예약 성공했습니다! 혼자가기 아쉬워 함께 수다떨며 사진 찍어주실 동행분 구합니다.',
    currentParticipants: 1,
    maxParticipants: 2
  },
  {
    id: '2',
    title: '강남역 파스타 맛집 동행 구해요 (2명 모집)',
    category: 'MEAL',
    categoryIcon: '🍲',
    location: '서울 강남구 역삼동',
    date: '내일 저녁 6:30',
    gender: '여성전용',
    authorName: '이지은',
    brix: 42.0,
    tags: ['양식', '저녁모임', '소소한수다'],
    deposit: '10,000원',
    isKycVerified: true,
    content: '퇴근 후 강남역 분위기 좋은 생면 파스타 집에서 맛있는 저녁 함께해요! 여성분 선호합니다.',
    currentParticipants: 1,
    maxParticipants: 3
  },
  {
    id: '3',
    title: '홍대 연남동 퓨전 오마카세 예약 같이 가실 분',
    category: 'EXPLORE',
    categoryIcon: '🏃',
    location: '서울 마포구 연남동',
    date: '이번주 토요일 저녁 7:00',
    gender: '성별무관',
    authorName: '박현우',
    brix: 36.8,
    tags: ['오마카세', '미식가', '예약완료'],
    deposit: '20,000원',
    isKycVerified: false,
    content: '2인 이상 전용 예약 오마카세 바입니다. 미식에 진심이신 분 환영합니다!',
    currentParticipants: 1,
    maxParticipants: 2
  }
];

export default function App() {
  const [activeTab, setActiveTab] = useState<'home' | 'pro' | 'matches' | 'profile'>('home');
  const [selectedCategory, setSelectedCategory] = useState('ALL');
  const [selectedArea, setSelectedArea] = useState('전체');
  const [searchQuery, setSearchQuery] = useState('');
  const [showSafetyModal, setShowSafetyModal] = useState(false);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [selectedPost, setSelectedPost] = useState<Post | null>(null);
  const [posts, setPosts] = useState<Post[]>(INITIAL_POSTS);

  // New Post Form State
  const [newTitle, setNewTitle] = useState('');
  const [newCategory, setNewCategory] = useState('MEAL');
  const [newLocation] = useState('성동구');
  const [newContent, setNewContent] = useState('');
  const [newGender] = useState('성별무관');
  const [newDeposit] = useState('5,000원');

  const filteredPosts = posts.filter(post => {
    const matchesCategory = selectedCategory === 'ALL' || post.category === selectedCategory;
    const matchesArea = selectedArea === '전체' || post.location.includes(selectedArea);
    const matchesSearch = searchQuery === '' || 
      post.title.includes(searchQuery) || 
      post.location.includes(searchQuery) ||
      post.tags.some(t => t.includes(searchQuery));
    return matchesCategory && matchesArea && matchesSearch;
  });

  const handleCreatePost = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newTitle.trim()) return;

    const created: Post = {
      id: String(Date.now()),
      title: newTitle,
      category: newCategory,
      categoryIcon: CATEGORIES.find(c => c.id === newCategory)?.icon || '🍽️',
      location: `서울 ${newLocation}`,
      date: '오늘 저녁',
      gender: newGender,
      authorName: '나 (본인)',
      brix: 36.5,
      tags: ['신규동행', '매너보장'],
      deposit: newDeposit,
      isKycVerified: true,
      content: newContent || '즐거운 식사 동행 만들어요!',
      currentParticipants: 1,
      maxParticipants: 2
    };

    setPosts([created, ...posts]);
    setShowCreateModal(false);
    setNewTitle('');
    setNewContent('');
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
      {/* Top App Bar (Android과 동일) */}
      <header style={{
        background: 'white',
        padding: '12px 16px',
        borderBottom: '1px solid #EBE5F5',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        position: 'sticky',
        top: 0,
        zIndex: 10
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
          <img src="/logo.jpg" alt="Logo" style={{ width: '30px', height: '30px', borderRadius: '8px' }} />
          <h1 style={{ fontSize: '20px', fontWeight: '900', color: '#7C4DFF', letterSpacing: '-0.5px' }}>유미당</h1>
          <span style={{
            fontSize: '11px',
            fontWeight: 'bold',
            background: '#EDE7F6',
            color: '#651FFF',
            padding: '2px 8px',
            borderRadius: '10px'
          }}>
            1:1 안심동행
          </span>
        </div>
        <button 
          onClick={() => setShowSafetyModal(true)}
          style={{ border: 'none', background: 'none', cursor: 'pointer', padding: '6px' }}
        >
          <Shield size={22} color="#7C4DFF" />
        </button>
      </header>

      {/* Main Content Scroll Container */}
      <main className="scroll-container" style={{ padding: '12px 16px' }}>

        {/* 1. HOME SCREEN */}
        {activeTab === 'home' && (
          <div>
            {/* Search Input Bar */}
            <div style={{
              background: 'white',
              border: '1.5px solid #EBE5F5',
              borderRadius: '14px',
              padding: '10px 14px',
              display: 'flex',
              alignItems: 'center',
              gap: '10px',
              marginBottom: '10px',
              boxShadow: '0 2px 6px rgba(124, 77, 255, 0.03)'
            }}>
              <Search size={18} color="#9CA3AF" />
              <input 
                value={searchQuery}
                onChange={e => setSearchQuery(e.target.value)}
                placeholder="어디서 어떤 동행을 찾으시나요?" 
                style={{ border: 'none', outline: 'none', width: '100%', fontSize: '13px', background: 'transparent' }} 
              />
              {searchQuery && (
                <X size={16} color="#9CA3AF" style={{ cursor: 'pointer' }} onClick={() => setSearchQuery('')} />
              )}
            </div>

            {/* Safety Banner (보라색 톤앤매너 완벽 적용) */}
            <div 
              onClick={() => setShowSafetyModal(true)}
              style={{
                background: '#F3E5F5',
                border: '1px solid #E1BEE7',
                borderRadius: '14px',
                padding: '12px 14px',
                marginBottom: '12px',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
                cursor: 'pointer'
              }}
            >
              <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                <div style={{ background: '#E1BEE7', width: '36px', height: '36px', borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  <Shield size={20} color="#7B1FA2" />
                </div>
                <div>
                  <h3 style={{ fontSize: '13.5px', fontWeight: 'bold', color: '#4A148C', marginBottom: '2px' }}>유미당 안심 동행 수칙</h3>
                  <p style={{ fontSize: '11.5px', color: '#6A1B9A' }}>낮 시간, 공공장소에서 만나요. 사전 송금 유도는 거절하세요.</p>
                </div>
              </div>
              <span style={{ fontSize: '11.5px', fontWeight: 'bold', color: '#7B1FA2', whiteSpace: 'nowrap' }}>수칙보기</span>
            </div>

            {/* Category Chips Scroll */}
            <div style={{ display: 'flex', gap: '6px', overflowX: 'auto', paddingBottom: '8px', marginBottom: '4px' }}>
              {CATEGORIES.map(cat => {
                const isSelected = selectedCategory === cat.id;
                return (
                  <button
                    key={cat.id}
                    onClick={() => setSelectedCategory(cat.id)}
                    style={{
                      border: 'none',
                      borderRadius: '20px',
                      padding: '6px 14px',
                      fontSize: '12px',
                      fontWeight: isSelected ? 'bold' : 'normal',
                      background: isSelected ? '#7C4DFF' : '#FFFFFF',
                      color: isSelected ? '#FFFFFF' : '#4B5563',
                      borderWidth: '1px',
                      borderStyle: 'solid',
                      borderColor: isSelected ? '#7C4DFF' : '#E5E7EB',
                      cursor: 'pointer',
                      whiteSpace: 'nowrap',
                      display: 'flex',
                      alignItems: 'center',
                      gap: '4px'
                    }}
                  >
                    <span>{cat.icon}</span>
                    <span>{cat.label}</span>
                  </button>
                );
              })}
            </div>

            {/* Area Filter Buttons */}
            <div style={{ display: 'flex', gap: '6px', overflowX: 'auto', paddingBottom: '10px', marginBottom: '8px' }}>
              {AREAS.map(area => {
                const isSelected = selectedArea === area;
                return (
                  <button
                    key={area}
                    onClick={() => setSelectedArea(area)}
                    style={{
                      border: isSelected ? '1px solid #7C4DFF' : '1px solid #E5E7EB',
                      borderRadius: '10px',
                      padding: '4px 10px',
                      fontSize: '11px',
                      fontWeight: isSelected ? 'bold' : 'normal',
                      background: isSelected ? '#EDE7F6' : 'transparent',
                      color: isSelected ? '#7C4DFF' : '#6B7280',
                      cursor: 'pointer',
                      whiteSpace: 'nowrap'
                    }}
                  >
                    {area}
                  </button>
                );
              })}
            </div>

            {/* Post Count Header */}
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '10px' }}>
              <span style={{ fontSize: '13px', fontWeight: 'bold', color: '#1E1F24' }}>
                검색 결과 <span style={{ color: '#7C4DFF' }}>{filteredPosts.length}</span>건
              </span>
              <span style={{ fontSize: '11px', color: '#9CA3AF', display: 'flex', alignItems: 'center', gap: '2px' }}>
                <Filter size={12} /> 최신순
              </span>
            </div>

            {/* Post Card List */}
            {filteredPosts.map(post => (
              <div 
                key={post.id} 
                className="card"
                onClick={() => setSelectedPost(post)}
                style={{ cursor: 'pointer', transition: 'transform 0.1s' }}
              >
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
                  <span className="badge badge-purple" style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                    <span>{post.categoryIcon}</span> {post.gender}
                  </span>
                  <span style={{ fontSize: '11.5px', color: '#6B7280', display: 'flex', alignItems: 'center', gap: '3px' }}>
                    <MapPin size={13} color="#9CA3AF" /> {post.location}
                  </span>
                </div>

                <h3 style={{ fontSize: '15px', fontWeight: 'bold', color: '#1E1F24', marginBottom: '6px', lineHeight: '1.3' }}>
                  {post.title}
                </h3>

                <p style={{ fontSize: '12px', color: '#6B7280', marginBottom: '10px', display: '-webkit-box', WebkitLineClamp: 2, WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>
                  {post.content}
                </p>

                <div style={{ display: 'flex', gap: '6px', marginBottom: '12px', flexWrap: 'wrap' }}>
                  {post.tags.map((tag, idx) => (
                    <span key={idx} style={{ fontSize: '11px', background: '#F8F5FF', color: '#7C4DFF', padding: '2px 8px', borderRadius: '6px', border: '1px solid #EDE7F6' }}>
                      #{tag}
                    </span>
                  ))}
                </div>

                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', paddingTop: '10px', borderTop: '1px solid #F3F4F6' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                    <span style={{ fontSize: '13px', fontWeight: 'bold', color: '#1E1F24' }}>{post.authorName}</span>
                    {post.isKycVerified && (
                      <UserCheck size={14} color="#2E7D32" />
                    )}
                    <span style={{ fontSize: '12px', color: '#9C27B0', fontWeight: 'bold', display: 'flex', alignItems: 'center', gap: '2px', background: '#F3E5F5', padding: '2px 6px', borderRadius: '6px' }}>
                      <Thermometer size={13} /> 당도 {Math.floor(post.brix)}
                    </span>
                  </div>
                  <span style={{ fontSize: '12px', fontWeight: 'bold', color: '#7C4DFF', display: 'flex', alignItems: 'center', gap: '2px' }}>
                    상세보기 <ChevronRight size={14} />
                  </span>
                </div>
              </div>
            ))}
          </div>
        )}

        {/* 2. PRO OFFERS SCREEN */}
        {activeTab === 'pro' && (
          <div>
            <div style={{ 
              background: 'linear-gradient(135deg, #7C4DFF 0%, #651FFF 100%)', 
              color: 'white', 
              padding: '20px 16px', 
              borderRadius: '16px', 
              marginBottom: '16px',
              boxShadow: '0 4px 14px rgba(124, 77, 255, 0.25)'
            }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '6px' }}>
                <Sparkles size={16} color="#B388FF" />
                <span style={{ fontSize: '11px', background: 'rgba(255,255,255,0.2)', padding: '2px 8px', borderRadius: '8px', fontWeight: 'bold' }}>
                  검증된 미식 파트너
                </span>
              </div>
              <h2 style={{ fontSize: '18px', fontWeight: 'bold', marginBottom: '4px' }}>전문 푸드 가이드 오퍼</h2>
              <p style={{ fontSize: '12px', opacity: 0.9 }}>인증된 전문가 및 셰프 가이드와 안전하고 깊이 있는 미식 기행을 즐겨보세요.</p>
            </div>

            <div className="card">
              <div style={{ display: 'flex', gap: '12px', alignItems: 'center', marginBottom: '10px' }}>
                <img src="/logo.jpg" style={{ width: '52px', height: '52px', borderRadius: '12px' }} />
                <div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                    <h3 style={{ fontSize: '15px', fontWeight: 'bold' }}>김도윤 프로 셰프</h3>
                    <span style={{ fontSize: '10px', background: '#EDE7F6', color: '#651FFF', padding: '1px 6px', borderRadius: '4px', fontWeight: 'bold' }}>공식인증</span>
                  </div>
                  <p style={{ fontSize: '12px', color: '#6B7280', marginTop: '2px' }}>성수/강남 오마카세 및 와인 페어링 전담</p>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '4px', marginTop: '4px' }}>
                    <span style={{ fontSize: '12px', color: '#9C27B0', fontWeight: 'bold' }}>당도 49</span>
                    <span style={{ fontSize: '11px', color: '#9CA3AF' }}>• 리뷰 128개</span>
                  </div>
                </div>
              </div>
              <button className="btn-primary">프로 오퍼 문의하기</button>
            </div>
          </div>
        )}

        {/* 3. MY MATCHES SCREEN */}
        {activeTab === 'matches' && (
          <div>
            <h2 style={{ fontSize: '16px', fontWeight: 'bold', marginBottom: '12px', color: '#1E1F24' }}>나의 동행 내역</h2>
            <div className="card" style={{ textAlign: 'center', padding: '28px 16px' }}>
              <div style={{ background: '#EDE7F6', width: '56px', height: '56px', borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 12px' }}>
                <Heart size={28} color="#7C4DFF" />
              </div>
              <h3 style={{ fontSize: '15px', fontWeight: 'bold', marginBottom: '4px', color: '#1E1F24' }}>진행 예정인 동행 모임</h3>
              <p style={{ fontSize: '13px', color: '#6B7280', marginBottom: '14px' }}>성수동 핫플 카페 동행 (오늘 오후 3시)</p>
              
              <div style={{ background: '#F8F5FF', padding: '12px', borderRadius: '12px', border: '1px solid #EDE7F6', textAlign: 'left', marginBottom: '14px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '12px', marginBottom: '6px' }}>
                  <span style={{ color: '#6B7280' }}>에스크로 보증금</span>
                  <span style={{ fontWeight: 'bold', color: '#7C4DFF' }}>5,000원 예치완료</span>
                </div>
                <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '12px' }}>
                  <span style={{ color: '#6B7280' }}>상대방 당도</span>
                  <span style={{ fontWeight: 'bold', color: '#9C27B0' }}>당도 38</span>
                </div>
              </div>

              <button className="btn-primary" style={{ background: '#651FFF' }}>동행 채팅방 입장하기</button>
            </div>
          </div>
        )}

        {/* 4. PROFILE SCREEN */}
        {activeTab === 'profile' && (
          <div>
            <div className="card" style={{ display: 'flex', alignItems: 'center', gap: '14px', padding: '18px' }}>
              <img src="/logo.jpg" style={{ width: '64px', height: '64px', borderRadius: '50%', border: '2px solid #7C4DFF' }} />
              <div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                  <h2 style={{ fontSize: '17px', fontWeight: 'bold', color: '#1E1F24' }}>홍길동 님</h2>
                  <UserCheck size={18} color="#2E7D32" />
                </div>
                <p style={{ fontSize: '12px', color: '#6B7280', marginTop: '2px' }}>KYC 본인인증 완료 회원</p>
                <div style={{ marginTop: '6px', fontSize: '13px', fontWeight: 'bold', color: '#9C27B0', display: 'flex', alignItems: 'center', gap: '4px' }}>
                  <Thermometer size={16} /> 나의 당도: 38
                </div>
              </div>
            </div>

            <div className="card">
              <h3 style={{ fontSize: '14px', fontWeight: 'bold', marginBottom: '12px', color: '#1E1F24' }}>안심 인증 및 에스크로 정보</h3>
              <div style={{ display: 'flex', justifyContent: 'space-between', padding: '10px 0', borderBottom: '1px solid #F3F4F6', fontSize: '13px' }}>
                <span style={{ color: '#4B5563' }}>실명/KYC 본인인증</span>
                <span style={{ color: '#2E7D32', fontWeight: 'bold', display: 'flex', alignItems: 'center', gap: '4px' }}>
                  <CheckCircle size={14} /> 인증 완료
                </span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', padding: '10px 0', fontSize: '13px' }}>
                <span style={{ color: '#4B5563' }}>에스크로 보증금 잔액</span>
                <span style={{ color: '#7C4DFF', fontWeight: 'bold' }}>50,000원 예치중</span>
              </div>
            </div>
          </div>
        )}

      </main>

      {/* Floating Action Button (Android FAB 동일) */}
      {activeTab === 'home' && (
        <button
          onClick={() => setShowCreateModal(true)}
          style={{
            position: 'absolute',
            right: '16px',
            bottom: '72px',
            background: '#7C4DFF',
            color: 'white',
            border: 'none',
            borderRadius: '30px',
            padding: '12px 20px',
            fontWeight: 'bold',
            fontSize: '13px',
            boxShadow: '0 4px 14px rgba(124, 77, 255, 0.4)',
            cursor: 'pointer',
            display: 'flex',
            alignItems: 'center',
            gap: '6px',
            zIndex: 5
          }}
        >
          <Plus size={18} />
          <span>동행 공고 올리기</span>
        </button>
      )}

      {/* Bottom Navigation (Android 하단 탭과 100% 동일) */}
      <nav style={{
        background: 'white',
        borderTop: '1px solid #EBE5F5',
        display: 'flex',
        justifyContent: 'space-around',
        padding: '8px 0',
        position: 'absolute',
        bottom: 0,
        left: 0,
        right: 0
      }}>
        <button onClick={() => setActiveTab('home')} style={{ border: 'none', background: 'none', cursor: 'pointer', display: 'flex', flexDirection: 'column', alignItems: 'center', color: activeTab === 'home' ? '#7C4DFF' : '#9CA3AF', width: '25%' }}>
          <Home size={20} />
          <span style={{ fontSize: '11px', marginTop: '3px', fontWeight: activeTab === 'home' ? 'bold' : 'normal' }}>동행 찾기</span>
        </button>
        <button onClick={() => setActiveTab('pro')} style={{ border: 'none', background: 'none', cursor: 'pointer', display: 'flex', flexDirection: 'column', alignItems: 'center', color: activeTab === 'pro' ? '#7C4DFF' : '#9CA3AF', width: '25%' }}>
          <Store size={20} />
          <span style={{ fontSize: '11px', marginTop: '3px', fontWeight: activeTab === 'pro' ? 'bold' : 'normal' }}>전문 오퍼</span>
        </button>
        <button onClick={() => setActiveTab('matches')} style={{ border: 'none', background: 'none', cursor: 'pointer', display: 'flex', flexDirection: 'column', alignItems: 'center', color: activeTab === 'matches' ? '#7C4DFF' : '#9CA3AF', width: '25%' }}>
          <Users size={20} />
          <span style={{ fontSize: '11px', marginTop: '3px', fontWeight: activeTab === 'matches' ? 'bold' : 'normal' }}>나의 동행</span>
        </button>
        <button onClick={() => setActiveTab('profile')} style={{ border: 'none', background: 'none', cursor: 'pointer', display: 'flex', flexDirection: 'column', alignItems: 'center', color: activeTab === 'profile' ? '#7C4DFF' : '#9CA3AF', width: '25%' }}>
          <User size={20} />
          <span style={{ fontSize: '11px', marginTop: '3px', fontWeight: activeTab === 'profile' ? 'bold' : 'normal' }}>내 프로필</span>
        </button>
      </nav>

      {/* Safety Rules Modal */}
      {showSafetyModal && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(0,0,0,0.5)', zIndex: 100, display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '20px' }}>
          <div style={{ background: 'white', borderRadius: '16px', padding: '20px', maxWidth: '380px', width: '100%' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '14px' }}>
              <h3 style={{ fontSize: '16px', fontWeight: 'bold', color: '#4A148C' }}>안심 동행 5대 수칙</h3>
              <X size={20} style={{ cursor: 'pointer' }} onClick={() => setShowSafetyModal(false)} />
            </div>
            <ol style={{ fontSize: '12.5px', color: '#4B5563', paddingLeft: '18px', lineHeight: '1.7' }}>
              <li>첫 만남은 카페·역 로비 등 밝은 공공장소에서 갖습니다.</li>
              <li>플랫폼 외 메신저 이동이나 사전 송금 요구는 단호히 거절하세요.</li>
              <li>만남 전 지인에게 일정 및 장소를 공유하세요.</li>
              <li>상대방 프로필, KYC 인증 마크 및 <b>당도 수치</b>를 미리 확인하세요.</li>
              <li>위급 상황 발생 시 즉시 112/119로 신고하세요.</li>
            </ol>
            <button className="btn-primary" style={{ marginTop: '16px' }} onClick={() => setShowSafetyModal(false)}>확인했습니다</button>
          </div>
        </div>
      )}

      {/* Create Post Modal */}
      {showCreateModal && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(0,0,0,0.5)', zIndex: 100, display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '20px' }}>
          <form onSubmit={handleCreatePost} style={{ background: 'white', borderRadius: '16px', padding: '20px', maxWidth: '380px', width: '100%', maxHeight: '90vh', overflowY: 'auto' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '14px' }}>
              <h3 style={{ fontSize: '16px', fontWeight: 'bold' }}>동행 공고 작성</h3>
              <X size={20} style={{ cursor: 'pointer' }} onClick={() => setShowCreateModal(false)} />
            </div>

            <div style={{ marginBottom: '12px' }}>
              <label style={{ fontSize: '12px', fontWeight: 'bold', display: 'block', marginBottom: '4px' }}>공고 제목</label>
              <input 
                value={newTitle} 
                onChange={e => setNewTitle(e.target.value)} 
                placeholder="예: 성수동 맛집 파스타 같이 가요" 
                required
                style={{ width: '100%', padding: '10px', border: '1px solid #E5E7EB', borderRadius: '8px', fontSize: '13px' }} 
              />
            </div>

            <div style={{ marginBottom: '12px' }}>
              <label style={{ fontSize: '12px', fontWeight: 'bold', display: 'block', marginBottom: '4px' }}>카테고리</label>
              <select value={newCategory} onChange={e => setNewCategory(e.target.value)} style={{ width: '100%', padding: '10px', border: '1px solid #E5E7EB', borderRadius: '8px', fontSize: '13px' }}>
                {CATEGORIES.filter(c => c.id !== 'ALL').map(c => (
                  <option key={c.id} value={c.id}>{c.icon} {c.label}</option>
                ))}
              </select>
            </div>

            <div style={{ marginBottom: '12px' }}>
              <label style={{ fontSize: '12px', fontWeight: 'bold', display: 'block', marginBottom: '4px' }}>상세 설명</label>
              <textarea 
                value={newContent}
                onChange={e => setNewContent(e.target.value)}
                placeholder="어떤 약속인지, 선호하는 대화 주제 등을 적어주세요." 
                rows={3} 
                style={{ width: '100%', padding: '10px', border: '1px solid #E5E7EB', borderRadius: '8px', fontSize: '13px' }} 
              />
            </div>

            <button type="submit" className="btn-primary" style={{ marginTop: '10px' }}>공고 등록하기</button>
          </form>
        </div>
      )}

      {/* Post Detail Modal */}
      {selectedPost && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(0,0,0,0.5)', zIndex: 100, display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '20px' }}>
          <div style={{ background: 'white', borderRadius: '16px', padding: '20px', maxWidth: '380px', width: '100%' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
              <span className="badge badge-purple">{selectedPost.gender}</span>
              <X size={20} style={{ cursor: 'pointer' }} onClick={() => setSelectedPost(null)} />
            </div>

            <h3 style={{ fontSize: '16px', fontWeight: 'bold', marginBottom: '8px' }}>{selectedPost.title}</h3>
            <p style={{ fontSize: '12.5px', color: '#4B5563', marginBottom: '14px', lineHeight: '1.5' }}>{selectedPost.content}</p>

            <div style={{ background: '#F8F5FF', padding: '12px', borderRadius: '10px', marginBottom: '14px', fontSize: '12px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '6px' }}>
                <span style={{ color: '#6B7280' }}>작성자</span>
                <span style={{ fontWeight: 'bold' }}>{selectedPost.authorName} (당도 {Math.floor(selectedPost.brix)})</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '6px' }}>
                <span style={{ color: '#6B7280' }}>위치</span>
                <span style={{ fontWeight: 'bold' }}>{selectedPost.location}</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                <span style={{ color: '#6B7280' }}>에스크로 보증금</span>
                <span style={{ fontWeight: 'bold', color: '#7C4DFF' }}>{selectedPost.deposit}</span>
              </div>
            </div>

            <button className="btn-primary" onClick={() => { alert('동행 신청이 전달되었습니다.'); setSelectedPost(null); }}>
              동행 신청하기
            </button>
          </div>
        </div>
      )}

    </div>
  );
}
