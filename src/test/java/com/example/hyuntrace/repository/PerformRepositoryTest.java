package com.example.hyuntrace.repository;

import com.example.hyuntrace.domain.Board;
import com.example.hyuntrace.domain.RecommendType;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StopWatch;

@SpringBootTest
@Log4j2
public class PerformRepositoryTest {

    @Autowired
    private RecommendRepository recommendRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate; // DB 사용을 위한 JdbcTemplate
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    private static final String REDIS_KEY = "testKey:"; // key에 ":" 추가 (Redis key 관례)

    @Test
    public void performTest() {
        StopWatch stopWatch = new StopWatch();
        Long bno = 109L;
        int testCount = 10; // 반복 횟수

        // 1. DB에서 데이터를 가져와 Redis에 저장
        System.out.println("Fetching data from DB and storing in Redis...");
        storeDataInRedis(bno);

        // 2. DB 성능 측정
        long totalDbTime = 0;
        for (int i = 0; i < testCount; i++) {
            stopWatch.start("DB Test " + (i + 1));
            testDbPerformance(bno);
            stopWatch.stop();
            totalDbTime += stopWatch.getTotalTimeMillis();  // Deprecated된 메서드 대신 getTotalTimeMillis 사용
        }

        // 3. Redis 성능 측정
        long totalRedisTime = 0;
        for (int i = 0; i < testCount; i++) {
            stopWatch.start("Redis Test " + (i + 1));
            testRedisPerformance(bno);
            stopWatch.stop();
            totalRedisTime += stopWatch.getTotalTimeMillis();  // Deprecated된 메서드 대신 getTotalTimeMillis 사용
        }

        // 결과 출력
        System.out.println("Average time for DB test: " + (totalDbTime / testCount) + "ms");
        System.out.println("Average time for Redis test: " + (totalRedisTime / testCount) + "ms");
    }


    private void testDbPerformance(Long bno) {
        long start = System.currentTimeMillis();

        // DB에서 데이터를 조회하는 예시
        Long upVoteCount = recommendRepository.countVotes(bno, RecommendType.UpVote);
        Long downVoteCount = recommendRepository.countVotes(bno, RecommendType.DownVote);
        Board board = boardRepository.findById(bno).orElseThrow();
        long end = System.currentTimeMillis();
        System.out.println("DB Query Time: " + (end - start) + "ms");
    }

    private void testRedisPerformance(Long bno) {
        long start = System.currentTimeMillis();

        // Redis에서 데이터를 조회
        redisTemplate.opsForValue().get(REDIS_KEY + bno);

        long end = System.currentTimeMillis();
        System.out.println("Redis Query Time: " + (end - start) + "ms");
    }

    private void storeDataInRedis(Long bno) {
        // DB에서 데이터를 조회한 후, Redis에 데이터를 저장
        Long upVoteCount = recommendRepository.countVotes(bno, RecommendType.UpVote);
        Long downVoteCount = recommendRepository.countVotes(bno, RecommendType.DownVote);
        Board board = boardRepository.findById(bno).orElseThrow();
        // 데이터를 Redis에 저장
        redisTemplate.opsForValue().set(REDIS_KEY + bno,"board: " + board+",upVote: " + upVoteCount + ", downVote: " + downVoteCount);
    }
}
