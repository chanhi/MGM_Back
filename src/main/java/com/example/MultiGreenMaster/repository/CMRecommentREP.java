package com.example.MultiGreenMaster.repository;

import com.example.MultiGreenMaster.entity.CMRecomment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CMRecommentREP extends CrudRepository<CMRecomment, Long> {
    List<CMRecomment> findByCmCommentId(Long cmCommentId); // 특정 댓글의 모든 대댓글을 조회하는 메서드

    @Query(value = "SELECT c.content AS content, c.regdate AS regdate, 'comment' AS type " +
            "FROM CMComment c WHERE c.user.id = :userId " +
            "UNION ALL " +
            "SELECT r.content AS content, r.regdate AS regdate, 'recomment' AS type " +
            "FROM CMRecomment r WHERE r.user.id = :userId " +
            "ORDER BY regdate DESC", nativeQuery = true)
    List<Object[]> findCommentsAndRecommentsByUserId(@Param("userId") Long userId);

    // 특정 사용자의 대댓글을 가져오는 메서드
    List<CMRecomment> findByUser_Id(Long userId);
}