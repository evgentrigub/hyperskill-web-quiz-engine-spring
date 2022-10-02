package webquiz.engine.repositories;

import webquiz.engine.models.Quiz;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuizRepository extends PagingAndSortingRepository<Quiz, Integer> {

}
