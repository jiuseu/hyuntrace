package com.example.hyuntrace.service;

import com.example.hyuntrace.domain.Board;
import com.example.hyuntrace.domain.Member;
import com.example.hyuntrace.domain.RecommendType;
import com.example.hyuntrace.domain.Recommendation;
import com.example.hyuntrace.dto.BoardDTO;
import com.example.hyuntrace.dto.RecommendRequestDTO;
import com.example.hyuntrace.dto.RecommendResponseDTO;
import com.example.hyuntrace.repository.BoardRepository;
import com.example.hyuntrace.repository.MemberRepository;
import com.example.hyuntrace.repository.RecommendRepository;
import com.example.hyuntrace.security.handler.RecommendSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {

    private final BoardRepository boardRepository;
    private final RecommendRepository recommendRepository;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RecommendSocketHandler recommendSocketHandler;

    private static final String REDIS_KEY_PREFIX = "voteCounts:";  // Redis 키 접두사
    private static final long CACHE_EXPIRATION = 10; // 캐시 만료 시간 (10분)

    @Override
    public void addVote(RecommendRequestDTO recommendRequestDTO) { //추천 비추천 등록

        Long bno = recommendRequestDTO.getBoardId();
        String mid = recommendRequestDTO.getMemberId();
        RecommendType vote = RecommendType.valueOf(recommendRequestDTO.getVoteType());

        // 기존에 해당 게시글에 대해 추천/비추천을 했는지 확인
        Recommendation existingRecommendation = recommendRepository.findByBoardIdAndMemberID(bno, mid)
                .orElse(null);

        // 이미 추천/비추천을 한 경우, 예외를 던져서 중복 투표 방지
        if (existingRecommendation != null) {
            throw new IllegalStateException("이미 추천/비추천을 한 게시글입니다.");
        }

        // 게시글과 회원 정보 조회
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        Member member = memberRepository.getWithRoles(mid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 추천/비추천 객체 생성
        Recommendation recommendation = Recommendation.builder()
                .board(board)
                .member(member)
                .recommendType(vote)
                .build();

        // 추천/비추천을 DB에 저장
        recommendRepository.save(recommendation);
        log.info(" 추천/비추천 저장 완료 - BoardId: {}", bno);

        // Redis 캐시 삭제 (추천/비추천 수치가 변경되었으니)
        String redisKey = REDIS_KEY_PREFIX + bno;
        redisTemplate.delete(redisKey);
        log.info(" Redis 캐시 삭제: {}", redisKey);

        RecommendResponseDTO dto = voteCounts(bno);
        recommendSocketHandler.sendUpdate(dto);
     }

    @Override
    public RecommendResponseDTO voteCounts(Long bno){//추천 비추천 개수 조회

        String redisKey = REDIS_KEY_PREFIX + bno;
        // **성능 측정 시작**
        long start = System.nanoTime();

        //redis에서 추천/비추천 개수 조회
        RecommendResponseDTO cachedData = (RecommendResponseDTO) redisTemplate.opsForValue().get(redisKey);
        log.info("redis의 추천/비추천 데이터 cacheData: {}",cachedData);

        if (cachedData != null) {
            long redisEnd = System.nanoTime();
            long redisTime = redisEnd - start;
            log.info("redis에서 추천/비추천 데이터 가져옴 - BoardId: {} (시간: {}ms)", bno, redisTime / 1_000_000);
            return cachedData;
        }

        // 게시글을 가져와 추천/비추천 수 바로 조회
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        Long upVoteCount = recommendRepository.countVotes(bno, RecommendType.UpVote);
        Long downVoteCount = recommendRepository.countVotes(bno, RecommendType.DownVote);

        RecommendResponseDTO dto = RecommendResponseDTO.builder()
                    .upVote(upVoteCount)
                    .downVote(downVoteCount)
                    .build();

        // 조회한 데이터를 Redis에 캐싱 (10분 만료 설정)
        redisTemplate.opsForValue().set(redisKey, dto, CACHE_EXPIRATION, TimeUnit.MINUTES);
        long dbEnd = System.nanoTime();
        long time = dbEnd - start;
        log.info("처음 db에서 불러오기 - Key: {} (TTL: 10분) (시간: {}ms)", redisKey, time / 1_000_000);
        return dto;
    }

}
