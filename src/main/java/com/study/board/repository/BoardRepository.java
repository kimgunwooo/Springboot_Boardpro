package com.study.board.repository;

import com.study.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board,Integer> {
    // JPARepository의 명령규칙에 따라 findBy(이름)Containing 이라고 메소드를 정의하면
    // 컬럼에서 키워드가 포함된 것을 찾겠다는 의미다.
    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);
}
