package com.example.hyuntrace.repository;

import com.example.hyuntrace.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,String> {

    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.mid =:mid and m.social = false")
    Optional<Member> getWithRoles(@Param("mid") String mid);
}
