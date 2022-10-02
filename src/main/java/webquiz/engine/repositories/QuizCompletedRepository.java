package webquiz.engine.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import webquiz.engine.models.QuizCompleted;

public interface QuizCompletedRepository extends PagingAndSortingRepository<QuizCompleted, Integer> {
    @Query("SELECT c FROM QuizCompleted c WHERE c.user.email = :email ORDER BY c.completedAt desc")
    Page<QuizCompleted> findAllByUserEmailAndSortedDesc(@Param("email") String email, Pageable page);
}
