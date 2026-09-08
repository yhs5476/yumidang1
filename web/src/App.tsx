import { useState } from 'react';
import { 
  Home, Store, Users, User, Shield, Thermometer, MapPin, CheckCircle, Search, 
  Plus, X, UserCheck, Sparkles, Filter, Calendar
} from 'lucide-react';

interface CompanionPost {
  id: string;
  authorId: string;
  authorName: string;
  authorGender: string;
  authorAge: number;
  authorKycVerified: boolean;
  authorMannerScore: number;
  category: string;
  categoryLabel: string;
  categoryIcon: string;
  title: string;
  content: string;
  dateTime: string;
  location: string;
  area: string;
  conditionGender: string;
  conditionAge: string;
  expenseType: string;
  status: string;
}

interface ProOffer {
  id: string;
  providerName: string;
  providerGender: string;
  providerAge: number;
  providerKycVerified: boolean;
  providerMannerScore: number;
  categoryTitle: string;
  title: string;
  description: string;
  priceKrw: number;
  schedule: string;
  includes: string;
  excludes: string;
}

interface JoinRequest {
  id: string;
  postId: string;
  postTitle: string;
  postDateTime: string;
  postLocation: string;
  requesterName: string;
  requesterGender: string;
  requesterAge: number;
  requesterKycVerified: boolean;
  requesterMannerScore: number;
  message: string;
  status: 'PENDING' | 'ACCEPTED' | 'REJECTED';
}

const CATEGORIES = [
  { id: 'ALL', label: '전체', icon: '🍽️' },
  { id: 'HOBBY', label: '문화/취미', icon: '🎨' },
  { id: 'DINING', label: '맛집/카페', icon: '🍲' },
  { id: 'FITNESS', label: '운동/러닝', icon: '🏃' },
  { id: 'TRAVEL', label: '여행/드라이브', icon: '🚗' },
  { id: 'STUDY', label: '스터디/자기계발', icon: '📖' },
];

const AREAS = ['전체', '종로구', '마포구', '성동구', '영등포구', '서초구', '강남구'];

const INITIAL_POSTS: CompanionPost[] = [
  {
    id: 'post_1',
    authorId: 'user_2',
    authorName: '박서연',
    authorGender: '여성',
    authorAge: 26,
    authorKycVerified: true,
    authorMannerScore: 39.1,
    category: 'HOBBY',
    categoryLabel: '문화/취미',
    categoryIcon: '🎨',
    title: '국립현대미술관 서울관 특별전 조용히 함께 관람해요',
    content: '이번 주말 삼청동 국립현대미술관 기획 전시 함께 보실 분 찾습니다! 평소 현대미술에 관심 많으신 분이면 더 반가워요. 관람 후 근처 조용한 카페에서 커피 한잔 가볍게 나눠요 :)',
    dateTime: '2026.09.13 (일) 14:30',
    location: '서울 종로구 삼청로 30 국립현대미술관 서울 로비',
    area: '종로구',
    conditionGender: '여성 전용',
    conditionAge: '20-30대',
    expenseType: '각자 부담',
    status: 'RECRUITING'
  },
  {
    id: 'post_2',
    authorId: 'user_3',
    authorName: '정다은',
    authorGender: '여성',
    authorAge: 31,
    authorKycVerified: true,
    authorMannerScore: 41.5,
    category: 'DINING',
    categoryLabel: '맛집/카페',
    categoryIcon: '🍲',
    title: '성수동 새로 오픈한 화덕피자 맛집 2인 런치 동행',
    content: '평소 가보고 싶었던 성수동 나폴리 피자 맛집인데 1인 방문하기 애매해서 동행 요청드려요! 맛있는 점심 같이 먹고 편하게 수다 떨어요.',
    dateTime: '2026.09.14 (월) 12:00',
    location: '서울 성동구 연무장길 성수역 3번 출구 앞',
    area: '성동구',
    conditionGender: '여성 전용',
    conditionAge: '연령 무관',
    expenseType: '각자 부담',
    status: 'RECRUITING'
  },
  {
    id: 'post_3',
    authorId: 'user_4',
    authorName: '한수진',
    authorGender: '여성',
    authorAge: 29,
    authorKycVerified: false,
    authorMannerScore: 37.0,
    category: 'FITNESS',
    categoryLabel: '운동/러닝',
    categoryIcon: '🏃',
    title: '퇴근 후 여의도 한강공원 5km 가벼운 시티런 함께해요',
    content: '혼자 뛰기 심심해서 1:1 페이스메이커로 함께 달려주실 분 구합니다! 페이스는 6분 30초 정도로 천천히 여유롭게 뛸 예정이에요. 초보 러너분 환영합니다.',
    dateTime: '2026.09.15 (화) 19:30',
    location: '서울 영등포구 여의나루역 2번 출구 광장',
    area: '영등포구',
    conditionGender: '성별 무관',
    conditionAge: '20-30대',
    expenseType: '작성자 부담 (음료 제공)',
    status: 'RECRUITING'
  },
  {
    id: 'post_4',
    authorId: 'user_5',
    authorName: '윤채원',
    authorGender: '여성',
    authorAge: 34,
    authorKycVerified: true,
    authorMannerScore: 42.0,
    category: 'TRAVEL',
    categoryLabel: '여행/드라이브',
    categoryIcon: '🚗',
    title: '강릉 당일치기 가을 바다 드라이브 & 안목 커피거리',
    content: '서울에서 출발해 당일치기로 강릉 안목해변과 정동진 둘러보실 분 모셔요. 자차로 이동하며 유류비는 각자 나누면 좋겠습니다. 맑은 공기 마시며 힐링해요!',
    dateTime: '2026.09.19 (토) 08:30',
    location: '서울 양재역 환승주차장 앞 집결',
    area: '서초구',
    conditionGender: '여성 전용',
    conditionAge: '20-40대',
    expenseType: '협의 (유류비 1/N)',
    status: 'RECRUITING'
  }
];

const INITIAL_PRO_OFFERS: ProOffer[] = [
  {
    id: 'pro_1',
    providerName: '오준석',
    providerGender: '남성',
    providerAge: 32,
    providerKycVerified: true,
    providerMannerScore: 45.3,
    categoryTitle: '스냅 사진 촬영 동행',
    title: '인생샷 전문 포토그래퍼의 서울 핫플 스냅 동행',
    description: '북촌 한옥마을, 성수동 등 감성적인 거리에서 자연스러운 고화질 사진을 촬영해 드립니다. 보정본 20장 포함.',
    priceKrw: 40000,
    schedule: '주말 2시간 기준 (사전 조율)',
    includes: '원본 전체 제공, 색감 보정 20장, 촬영 장소 동행',
    excludes: '개인 음료비, 입장료'
  },
  {
    id: 'pro_2',
    providerName: '강민주',
    providerGender: '여성',
    providerAge: 30,
    providerKycVerified: true,
    providerMannerScore: 44.8,
    categoryTitle: '전시 도슨트 & 예술 해설',
    title: '국립중앙박물관 및 주요 미술관 1:1 맞춤 도슨트',
    description: '미술사 전공자가 알기 쉽고 재미있게 시대 배경과 작품의 숨은 이야기를 들려드립니다. 혼자서는 놓치기 쉬운 감상 포인트를 짚어드려요.',
    priceKrw: 35000,
    schedule: '평일/주말 1시간 30분',
    includes: '1:1 전문 해설, 감상 가이드 리플렛',
    excludes: '전시회 티켓비 (각자 예매)'
  },
  {
    id: 'pro_3',
    providerName: '유지훈',
    providerGender: '남성',
    providerAge: 28,
    providerKycVerified: true,
    providerMannerScore: 42.6,
    categoryTitle: '러닝 & 페이스 코칭',
    title: '초보 러너를 위한 1:1 자세 교정 및 부상 방지 페이스메이커',
    description: '달리기 호흡법, 착지법 코칭과 함께 원하는 페이스로 안전하게 동행 페이스메이커 역할을 수행합니다.',
    priceKrw: 25000,
    schedule: '주 1회 60분 (야간 러닝 가능)',
    includes: '스트레칭 지도, 페이스 조절, 스포츠 이온음료',
    excludes: '개인 러닝화/운동복'
  }
];

const INITIAL_REQUESTS: JoinRequest[] = [
  {
    id: 'req_1',
    postId: 'post_5',
    postTitle: '망원동 독립서점 북토크 & 산책 같이 가실 분',
    postDateTime: '2026.09.16 (수) 15:00',
    postLocation: '서울 마포구 망원역 2번 출구',
    requesterName: '최은지',
    requesterGender: '여성',
    requesterAge: 27,
    requesterKycVerified: true,
    requesterMannerScore: 40.2,
    message: '안녕하세요! 저도 독립서점 탐방 정말 좋아하는데 같이 가고 싶어요 :)',
    status: 'PENDING'
  },
  {
    id: 'req_2',
    postId: 'post_1',
    postTitle: '국립현대미술관 서울관 특별전 조용히 함께 관람해요',
    postDateTime: '2026.09.13 (일) 14:30',
    postLocation: '서울 종로구 삼청로 30 국립현대미술관 서울 로비',
    requesterName: '김민서 (나)',
    requesterGender: '여성',
    requesterAge: 28,
    requesterKycVerified: true,
    requesterMannerScore: 38.2,
    message: '안녕하세요 서연님! 이번 전시 꼭 보고 싶었는데 마침 좋은 공고를 올려주셔서 신청합니다. 편안한 관람 약속드려요.',
    status: 'ACCEPTED'
  }
];

export default function App() {
  const [activeTab, setActiveTab] = useState<'home' | 'pro' | 'matches' | 'profile'>('home');
  const [selectedCategory, setSelectedCategory] = useState('ALL');
  const [selectedArea, setSelectedArea] = useState('전체');
  const [searchQuery, setSearchQuery] = useState('');
  const [showSafetyModal, setShowSafetyModal] = useState(false);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [selectedPost, setSelectedPost] = useState<CompanionPost | null>(null);
  const [selectedProOffer, setSelectedProOffer] = useState<ProOffer | null>(null);
  
  const [posts, setPosts] = useState<CompanionPost[]>(INITIAL_POSTS);
  const [requests, setRequests] = useState<JoinRequest[]>(INITIAL_REQUESTS);
  const [currentUser] = useState({
    name: '김민서',
    gender: '여성',
    age: 28,
    phone: '010-4829-1029',
    isKycVerified: true,
    kycVerifiedDate: '2026.08.15',
    bio: '주말 미술관 전시 관람과 조용한 북카페 투어를 좋아합니다. 매너 있고 편안한 동행을 환영해요!',
    interests: ['전시회', '카페투어', '러닝', '필름카메라'],
    mannerScore: 38.2,
    completedCompanionsCount: 5,
    positiveReviewTags: ['시간 약속을 잘 지켜요', '대화가 편안해요', '배려심이 넘쳐요']
  });

  // Create Post Form
  const [newTitle, setNewTitle] = useState('');
  const [newCategory, setNewCategory] = useState('HOBBY');
  const [newArea, setNewArea] = useState('종로구');
  const [newContent, setNewContent] = useState('');
  const [newConditionGender, setNewConditionGender] = useState('여성 전용');
  const [newConditionAge] = useState('20-30대');
  const [newExpenseType] = useState('각자 부담');

  const filteredPosts = posts.filter(post => {
    const matchesCategory = selectedCategory === 'ALL' || post.category === selectedCategory;
    const matchesArea = selectedArea === '전체' || post.area === selectedArea;
    const matchesSearch = searchQuery === '' || 
      post.title.includes(searchQuery) || 
      post.location.includes(searchQuery) ||
      post.content.includes(searchQuery);
    return matchesCategory && matchesArea && matchesSearch;
  });

  const handleCreatePostSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newTitle.trim()) return;

    const catObj = CATEGORIES.find(c => c.id === newCategory) || CATEGORIES[1];

    const createdPost: CompanionPost = {
      id: `post_${Date.now()}`,
      authorId: 'user_me',
      authorName: currentUser.name,
      authorGender: currentUser.gender,
      authorAge: currentUser.age,
      authorKycVerified: currentUser.isKycVerified,
      authorMannerScore: currentUser.mannerScore,
      category: newCategory,
      categoryLabel: catObj.label,
      categoryIcon: catObj.icon,
      title: newTitle,
      content: newContent || '즐거운 동행을 만들어요!',
      dateTime: '2026.09.20 (일) 14:00',
      location: `서울 ${newArea} 인근 공공장소`,
      area: newArea,
      conditionGender: newConditionGender,
      conditionAge: newConditionAge,
      expenseType: newExpenseType,
      status: 'RECRUITING'
    };

    setPosts([createdPost, ...posts]);
    setShowCreateModal(false);
    setNewTitle('');
    setNewContent('');
  };

  const handleApplyCompanion = (post: CompanionPost) => {
    const newReq: JoinRequest = {
      id: `req_${Date.now()}`,
      postId: post.id,
      postTitle: post.title,
      postDateTime: post.dateTime,
      postLocation: post.location,
      requesterName: currentUser.name,
      requesterGender: currentUser.gender,
      requesterAge: currentUser.age,
      requesterKycVerified: currentUser.isKycVerified,
      requesterMannerScore: currentUser.mannerScore,
      message: '안녕하세요! 공고 내용 확인하고 신청합니다.',
      status: 'PENDING'
    };
    setRequests([newReq, ...requests]);
    setSelectedPost(null);
    alert('동행 신청이 작성자에게 전송되었습니다.');
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
      {/* 안드로이드 앱 100% 동일 TopAppBar */}
      <header style={{
        background: '#F8F5FF',
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
          <TextLogo />
        </div>
        <button 
          onClick={() => setShowSafetyModal(true)}
          style={{ border: 'none', background: 'none', cursor: 'pointer', padding: '4px' }}
        >
          <Shield size={22} color="#7C4DFF" />
        </button>
      </header>

      {/* Main Content Area */}
      <main className="scroll-container" style={{ padding: '12px 16px' }}>

        {/* 1. HOME SCREEN */}
        {activeTab === 'home' && (
          <div>
            {/* Search OutlinedTextField */}
            <div style={{
              background: 'white',
              border: '1px solid #EBE5F5',
              borderRadius: '14px',
              padding: '10px 14px',
              display: 'flex',
              alignItems: 'center',
              gap: '8px',
              marginBottom: '10px'
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

            {/* SafetyNoticeCard (보라색 톤앤매너 100% 동일) */}
            <div 
              onClick={() => setShowSafetyModal(true)}
              style={{
                background: '#F3E5F5',
                border: '1px solid #E1BEE7',
                borderRadius: '14px',
                padding: '12px 14px',
                marginBottom: '10px',
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

            {/* CompanionCategory Chips */}
            <div style={{ display: 'flex', gap: '6px', overflowX: 'auto', paddingBottom: '6px', marginBottom: '4px' }}>
              {CATEGORIES.map(cat => {
                const isSelected = selectedCategory === cat.id;
                return (
                  <button
                    key={cat.id}
                    onClick={() => setSelectedCategory(cat.id)}
                    style={{
                      border: isSelected ? '1px solid #7C4DFF' : '1px solid #E5E7EB',
                      borderRadius: '20px',
                      padding: '6px 14px',
                      fontSize: '12px',
                      fontWeight: isSelected ? 'bold' : 'normal',
                      background: isSelected ? '#7C4DFF' : '#FFFFFF',
                      color: isSelected ? '#FFFFFF' : '#4B5563',
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
            <div style={{ display: 'flex', gap: '6px', overflowX: 'auto', paddingBottom: '8px', marginBottom: '8px' }}>
              {AREAS.map(area => {
                const isSelected = selectedArea === area;
                return (
                  <button
                    key={area}
                    onClick={() => setSelectedArea(area)}
                    style={{
                      border: isSelected ? '1px solid #7C4DFF' : '1px solid #E5E7EB',
                      borderRadius: '12px',
                      padding: '4px 10px',
                      fontSize: '11px',
                      fontWeight: isSelected ? 'bold' : 'normal',
                      background: isSelected ? '#F3EFEA' : 'transparent',
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

            {/* Post Header */}
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '10px' }}>
              <span style={{ fontSize: '13px', fontWeight: 'bold', color: '#1E1F24' }}>
                동행 공고 <span style={{ color: '#7C4DFF' }}>{filteredPosts.length}</span>건
              </span>
              <span style={{ fontSize: '11px', color: '#9CA3AF', display: 'flex', alignItems: 'center', gap: '2px' }}>
                <Filter size={12} /> 최신순
              </span>
            </div>

            {/* CompanionPostItem (안드로이드 동일 컴포넌트) */}
            {filteredPosts.map(post => (
              <div 
                key={post.id} 
                className="card"
                onClick={() => setSelectedPost(post)}
                style={{ cursor: 'pointer' }}
              >
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '6px' }}>
                  <div style={{ display: 'flex', gap: '6px', alignItems: 'center' }}>
                    <span className="badge badge-purple">{post.categoryLabel}</span>
                    <span style={{ fontSize: '11px', background: '#F8F5FF', color: '#7C4DFF', padding: '2px 8px', borderRadius: '4px', border: '1px solid #EDE7F6' }}>
                      {post.conditionGender}
                    </span>
                  </div>
                  <span style={{ fontSize: '11px', color: '#6B7280' }}>모집중</span>
                </div>

                <h3 style={{ fontSize: '15px', fontWeight: 'bold', color: '#1E1F24', marginBottom: '6px', lineHeight: '1.3' }}>
                  {post.title}
                </h3>

                <p style={{ fontSize: '12px', color: '#6B7280', marginBottom: '10px', display: '-webkit-box', WebkitLineClamp: 2, WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>
                  {post.content}
                </p>

                <div style={{ fontSize: '11.5px', color: '#4B5563', display: 'flex', flexDirection: 'column', gap: '4px', marginBottom: '12px', background: '#F8F5FF', padding: '8px 10px', borderRadius: '8px' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                    <Calendar size={13} color="#7C4DFF" /> {post.dateTime}
                  </div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                    <MapPin size={13} color="#7C4DFF" /> {post.location}
                  </div>
                </div>

                {/* Author Info Bar & MannerScoreBar (당도 중복 제거 수정 완료) */}
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', paddingTop: '10px', borderTop: '1px solid #F3F4F6' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                    <div style={{ background: '#EDE7F6', width: '28px', height: '28px', borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '12px', fontWeight: 'bold', color: '#7C4DFF' }}>
                      {post.authorName[0]}
                    </div>
                    <div>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                        <span style={{ fontSize: '12px', fontWeight: 'bold' }}>{post.authorName} ({post.authorGender}·{post.authorAge}세)</span>
                        {post.authorKycVerified && <UserCheck size={14} color="#2E7D32" />}
                      </div>
                    </div>
                  </div>

                  {/* Single MannerScoreBar */}
                  <MannerScoreBar score={post.authorMannerScore} />
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
              padding: '18px 16px', 
              borderRadius: '16px', 
              marginBottom: '14px',
              boxShadow: '0 4px 14px rgba(124, 77, 255, 0.25)'
            }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '6px', marginBottom: '4px' }}>
                <Sparkles size={16} color="#B388FF" />
                <span style={{ fontSize: '11px', background: 'rgba(255,255,255,0.2)', padding: '2px 8px', borderRadius: '8px', fontWeight: 'bold' }}>
                  전문가 전용 마켓
                </span>
              </div>
              <h2 style={{ fontSize: '17px', fontWeight: 'bold', marginBottom: '2px' }}>전문 동행 오퍼 마켓</h2>
              <p style={{ fontSize: '12px', opacity: 0.9 }}>검증된 포토그래퍼, 도슨트, 페이스메이커와 안전한 1:1 맞춤 동행 서비스를 경험해 보세요.</p>
            </div>

            {INITIAL_PRO_OFFERS.map(offer => (
              <div key={offer.id} className="card" style={{ marginBottom: '14px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '8px' }}>
                  <span className="badge badge-purple">{offer.categoryTitle}</span>
                  <span style={{ fontSize: '15px', fontWeight: 'bold', color: '#7C4DFF' }}>{offer.priceKrw.toLocaleString()}원</span>
                </div>

                <h3 style={{ fontSize: '15.5px', fontWeight: 'bold', color: '#1E1F24', marginBottom: '6px' }}>{offer.title}</h3>
                <p style={{ fontSize: '12px', color: '#6B7280', marginBottom: '12px', lineHeight: '1.4' }}>{offer.description}</p>

                <div style={{ background: '#F8F5FF', padding: '10px 12px', borderRadius: '10px', fontSize: '11.5px', color: '#4B5563', marginBottom: '12px', display: 'flex', flexDirection: 'column', gap: '4px' }}>
                  <div>• 일정: {offer.schedule}</div>
                  <div>• 포함: {offer.includes}</div>
                  <div>• 불포함: {offer.excludes}</div>
                </div>

                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', paddingTop: '10px', borderTop: '1px solid #F3F4F6', marginBottom: '12px' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                    <div style={{ background: '#EDE7F6', width: '32px', height: '32px', borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center', fontWeight: 'bold', color: '#7C4DFF' }}>
                      {offer.providerName[0]}
                    </div>
                    <div>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                        <span style={{ fontSize: '12.5px', fontWeight: 'bold' }}>{offer.providerName} ({offer.providerGender}·{offer.providerAge}세)</span>
                        <UserCheck size={14} color="#2E7D32" />
                      </div>
                    </div>
                  </div>

                  {/* Single MannerScoreBar (당도 중복 제거) */}
                  <MannerScoreBar score={offer.providerMannerScore} />
                </div>

                <button 
                  className="btn-primary" 
                  onClick={() => setSelectedProOffer(offer)}
                >
                  전문 동행 예약 신청하기
                </button>
              </div>
            ))}
          </div>
        )}

        {/* 3. MY MATCHES SCREEN (안드로이드 100% 동일) */}
        {activeTab === 'matches' && (
          <div>
            <h2 style={{ fontSize: '16px', fontWeight: 'bold', marginBottom: '12px', color: '#1E1F24' }}>나의 동행 관리</h2>
            
            {requests.map(req => (
              <div key={req.id} className="card" style={{ marginBottom: '12px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '6px' }}>
                  <span style={{ fontSize: '11px', background: req.status === 'ACCEPTED' ? '#E8F5E9' : '#FFF3E0', color: req.status === 'ACCEPTED' ? '#2E7D32' : '#E65100', padding: '2px 8px', borderRadius: '6px', fontWeight: 'bold' }}>
                    {req.status === 'ACCEPTED' ? '동행 확정 (에스크로 진행중)' : '대기중인 신청'}
                  </span>
                  <span style={{ fontSize: '11px', color: '#9CA3AF' }}>1:1 안심동행</span>
                </div>

                <h3 style={{ fontSize: '14.5px', fontWeight: 'bold', marginBottom: '6px' }}>{req.postTitle}</h3>
                
                <div style={{ fontSize: '11.5px', color: '#6B7280', marginBottom: '10px' }}>
                  <div>📅 {req.postDateTime}</div>
                  <div>📍 {req.postLocation}</div>
                </div>

                <div style={{ background: '#F8F5FF', padding: '10px', borderRadius: '10px', marginBottom: '10px' }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '4px' }}>
                    <span style={{ fontSize: '12px', fontWeight: 'bold' }}>신청자: {req.requesterName}</span>
                    <MannerScoreBar score={req.requesterMannerScore} />
                  </div>
                  <p style={{ fontSize: '12px', color: '#4B5563', fontStyle: 'italic' }}>"{req.message}"</p>
                </div>

                {req.status === 'ACCEPTED' ? (
                  <button className="btn-primary" style={{ background: '#651FFF' }}>
                    1:1 안심 채팅방 입장하기
                  </button>
                ) : (
                  <div style={{ display: 'flex', gap: '8px' }}>
                    <button className="btn-primary" style={{ background: '#7C4DFF' }}>신청 수락</button>
                    <button style={{ flex: 1, padding: '10px', border: '1px solid #E5E7EB', borderRadius: '12px', background: 'white', fontSize: '13px', cursor: 'pointer' }}>거절</button>
                  </div>
                )}
              </div>
            ))}
          </div>
        )}

        {/* 4. PROFILE SCREEN (안드로이드 100% 동일) */}
        {activeTab === 'profile' && (
          <div>
            <div className="card" style={{ padding: '16px' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '14px', marginBottom: '14px' }}>
                <img src="/logo.jpg" style={{ width: '60px', height: '60px', borderRadius: '50%', border: '2px solid #7C4DFF' }} />
                <div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                    <h2 style={{ fontSize: '17px', fontWeight: 'bold', color: '#1E1F24' }}>{currentUser.name} ({currentUser.gender}·{currentUser.age}세)</h2>
                    {currentUser.isKycVerified && <UserCheck size={18} color="#2E7D32" />}
                  </div>
                  <p style={{ fontSize: '12px', color: '#6B7280', marginTop: '2px' }}>{currentUser.bio}</p>
                </div>
              </div>

              {/* Single MannerScoreBar */}
              <div style={{ background: '#F8F5FF', padding: '12px', borderRadius: '12px', border: '1px solid #EDE7F6' }}>
                <MannerScoreBar score={currentUser.mannerScore} />
              </div>
            </div>

            {/* KYC & Verification Card */}
            <div className="card">
              <h3 style={{ fontSize: '14px', fontWeight: 'bold', marginBottom: '12px', color: '#1E1F24' }}>신원 인증 및 안심 에스크로</h3>
              <div style={{ display: 'flex', justifyContent: 'space-between', padding: '10px 0', borderBottom: '1px solid #F3F4F6', fontSize: '13px' }}>
                <span style={{ color: '#4B5563' }}>휴대폰 명의 인증</span>
                <span style={{ color: '#2E7D32', fontWeight: 'bold' }}>인증 완료</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', padding: '10px 0', borderBottom: '1px solid #F3F4F6', fontSize: '13px' }}>
                <span style={{ color: '#4B5563' }}>실명/KYC 본인인증</span>
                <span style={{ color: '#2E7D32', fontWeight: 'bold', display: 'flex', alignItems: 'center', gap: '4px' }}>
                  <CheckCircle size={14} /> {currentUser.kycVerifiedDate} 완료
                </span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', padding: '10px 0', fontSize: '13px' }}>
                <span style={{ color: '#4B5563' }}>완료한 동행 횟수</span>
                <span style={{ color: '#7C4DFF', fontWeight: 'bold' }}>{currentUser.completedCompanionsCount}회 완료</span>
              </div>
            </div>

            {/* Positive Review Tags */}
            <div className="card">
              <h3 style={{ fontSize: '14px', fontWeight: 'bold', marginBottom: '10px', color: '#1E1F24' }}>동행 키워드 평가</h3>
              <div style={{ display: 'flex', gap: '6px', flexWrap: 'wrap' }}>
                {currentUser.positiveReviewTags.map((tag, idx) => (
                  <span key={idx} style={{ fontSize: '12px', background: '#EDE7F6', color: '#651FFF', padding: '6px 12px', borderRadius: '16px', fontWeight: 'bold' }}>
                    👍 {tag}
                  </span>
                ))}
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
          <form onSubmit={handleCreatePostSubmit} style={{ background: 'white', borderRadius: '16px', padding: '20px', maxWidth: '380px', width: '100%', maxHeight: '90vh', overflowY: 'auto' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '14px' }}>
              <h3 style={{ fontSize: '16px', fontWeight: 'bold' }}>동행 공고 작성</h3>
              <X size={20} style={{ cursor: 'pointer' }} onClick={() => setShowCreateModal(false)} />
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
              <label style={{ fontSize: '12px', fontWeight: 'bold', display: 'block', marginBottom: '4px' }}>공고 제목</label>
              <input 
                value={newTitle} 
                onChange={e => setNewTitle(e.target.value)} 
                placeholder="제목을 입력하세요" 
                required
                style={{ width: '100%', padding: '10px', border: '1px solid #E5E7EB', borderRadius: '8px', fontSize: '13px' }} 
              />
            </div>

            <div style={{ marginBottom: '12px' }}>
              <label style={{ fontSize: '12px', fontWeight: 'bold', display: 'block', marginBottom: '4px' }}>지역구 선택</label>
              <select value={newArea} onChange={e => setNewArea(e.target.value)} style={{ width: '100%', padding: '10px', border: '1px solid #E5E7EB', borderRadius: '8px', fontSize: '13px' }}>
                {AREAS.filter(a => a !== '전체').map(a => (
                  <option key={a} value={a}>{a}</option>
                ))}
              </select>
            </div>

            <div style={{ marginBottom: '12px' }}>
              <label style={{ fontSize: '12px', fontWeight: 'bold', display: 'block', marginBottom: '4px' }}>동행 조건 (성별)</label>
              <select value={newConditionGender} onChange={e => setNewConditionGender(e.target.value)} style={{ width: '100%', padding: '10px', border: '1px solid #E5E7EB', borderRadius: '8px', fontSize: '13px' }}>
                <option value="여성 전용">여성 전용</option>
                <option value="성별 무관">성별 무관</option>
              </select>
            </div>

            <div style={{ marginBottom: '12px' }}>
              <label style={{ fontSize: '12px', fontWeight: 'bold', display: 'block', marginBottom: '4px' }}>상세 내용</label>
              <textarea 
                value={newContent}
                onChange={e => setNewContent(e.target.value)}
                placeholder="동행 약속 장소, 시간, 주제를 설명해 주세요." 
                rows={3} 
                style={{ width: '100%', padding: '10px', border: '1px solid #E5E7EB', borderRadius: '8px', fontSize: '13px' }} 
              />
            </div>

            <button type="submit" className="btn-primary" style={{ marginTop: '6px' }}>동행 공고 등록하기</button>
          </form>
        </div>
      )}

      {/* Post Detail Modal */}
      {selectedPost && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(0,0,0,0.5)', zIndex: 100, display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '20px' }}>
          <div style={{ background: 'white', borderRadius: '16px', padding: '20px', maxWidth: '380px', width: '100%' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
              <span className="badge badge-purple">{selectedPost.categoryLabel}</span>
              <X size={20} style={{ cursor: 'pointer' }} onClick={() => setSelectedPost(null)} />
            </div>

            <h3 style={{ fontSize: '16px', fontWeight: 'bold', marginBottom: '8px' }}>{selectedPost.title}</h3>
            <p style={{ fontSize: '12.5px', color: '#4B5563', marginBottom: '14px', lineHeight: '1.5' }}>{selectedPost.content}</p>

            <div style={{ background: '#F8F5FF', padding: '12px', borderRadius: '10px', marginBottom: '14px', fontSize: '12px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '6px' }}>
                <span style={{ color: '#6B7280' }}>작성자</span>
                <span style={{ fontWeight: 'bold' }}>{selectedPost.authorName} ({selectedPost.authorGender}·{selectedPost.authorAge}세)</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '6px' }}>
                <span style={{ color: '#6B7280' }}>일시</span>
                <span style={{ fontWeight: 'bold' }}>{selectedPost.dateTime}</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '6px' }}>
                <span style={{ color: '#6B7280' }}>장소</span>
                <span style={{ fontWeight: 'bold' }}>{selectedPost.location}</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                <span style={{ color: '#6B7280' }}>비용 조건</span>
                <span style={{ fontWeight: 'bold', color: '#7C4DFF' }}>{selectedPost.expenseType}</span>
              </div>
            </div>

            <button className="btn-primary" onClick={() => handleApplyCompanion(selectedPost)}>
              1:1 안심 동행 신청하기
            </button>
          </div>
        </div>
      )}

      {/* Pro Offer Detail Modal */}
      {selectedProOffer && (
        <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, background: 'rgba(0,0,0,0.5)', zIndex: 100, display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '20px' }}>
          <div style={{ background: 'white', borderRadius: '16px', padding: '20px', maxWidth: '380px', width: '100%' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
              <span className="badge badge-purple">{selectedProOffer.categoryTitle}</span>
              <X size={20} style={{ cursor: 'pointer' }} onClick={() => setSelectedProOffer(null)} />
            </div>

            <h3 style={{ fontSize: '16px', fontWeight: 'bold', marginBottom: '8px' }}>{selectedProOffer.title}</h3>
            <p style={{ fontSize: '12.5px', color: '#4B5563', marginBottom: '14px', lineHeight: '1.5' }}>{selectedProOffer.description}</p>

            <div style={{ background: '#F8F5FF', padding: '12px', borderRadius: '10px', marginBottom: '14px', fontSize: '12px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '6px' }}>
                <span style={{ color: '#6B7280' }}>전문가</span>
                <span style={{ fontWeight: 'bold' }}>{selectedProOffer.providerName} (당도 {Math.floor(selectedProOffer.providerMannerScore)})</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '6px' }}>
                <span style={{ color: '#6B7280' }}>서비스 금액</span>
                <span style={{ fontWeight: 'bold', color: '#7C4DFF' }}>{selectedProOffer.priceKrw.toLocaleString()}원</span>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                <span style={{ color: '#6B7280' }}>제공 일정</span>
                <span style={{ fontWeight: 'bold' }}>{selectedProOffer.schedule}</span>
              </div>
            </div>

            <button className="btn-primary" onClick={() => { alert('전문 동행 예약 신청이 접수되었습니다.'); setSelectedProOffer(null); }}>
              에스크로 예약 진행하기
            </button>
          </div>
        </div>
      )}

    </div>
  );
}

// Logo Header (Android 동일)
function TextLogo() {
  return (
    <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
      <img src="/logo.jpg" alt="Logo" style={{ width: '28px', height: '28px', borderRadius: '8px' }} />
      <span style={{ fontSize: '20px', fontWeight: '900', color: '#7C4DFF', letterSpacing: '-0.5px' }}>유미당</span>
      <span style={{ fontSize: '11px', fontWeight: 'bold', background: '#EDE7F6', color: '#651FFF', padding: '2px 8px', borderRadius: '10px' }}>
        1:1 안심동행
      </span>
    </div>
  );
}

// Single MannerScoreBar Component (100% Android 동일)
function MannerScoreBar({ score }: { score: number }) {
  const progress = Math.min(Math.max(score / 100, 0), 1) * 100;
  const color = score >= 40.0 ? '#9C27B0' : score >= 36.5 ? '#7C4DFF' : '#6B7280';

  return (
    <div style={{ width: '100px' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2px' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '2px' }}>
          <Thermometer size={13} color={color} />
          <span style={{ fontSize: '11px', color: '#6B7280' }}>당도</span>
        </div>
        <span style={{ fontSize: '12px', fontWeight: 'bold', color }}>{Math.floor(score)}</span>
      </div>
      <div style={{ width: '100%', background: '#E5E7EB', height: '5px', borderRadius: '3px', overflow: 'hidden' }}>
        <div style={{ width: `${progress}%`, background: color, height: '100%', borderRadius: '3px' }} />
      </div>
    </div>
  );
}
