package webquiz.engine.services;

import webquiz.engine.models.QuizCompleted;
import webquiz.engine.models.QuizCompletedDto;
import webquiz.engine.models.User;
import webquiz.engine.repositories.QuizCompletedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class QuizCompletedService {

    private final QuizCompletedRepository quizCompletedRepository;

    @Autowired
    QuizCompletedService(QuizCompletedRepository quizCompletedRepository){
        this.quizCompletedRepository = quizCompletedRepository;
    }

    public Page<QuizCompletedDto> getCompletedQuiz(Integer pageNo, User user){
        Pageable page = PageRequest.of(pageNo, 10);

        var completedQuizzes = quizCompletedRepository
                .findAllByUserEmailAndSortedDesc(user.getEmail(), page);

        return completedQuizzes.map(QuizCompletedService::mapCompleted);
    }

    public void postQuizCompleted(QuizCompleted quizCompleted){
        this.quizCompletedRepository.save(quizCompleted);
    }

    private static QuizCompletedDto mapCompleted(QuizCompleted quiz){
        var completedDto = new QuizCompletedDto();
        completedDto.setId(quiz.getQuiz().getId());
        completedDto.setCompletedAt(quiz.getCompletedAt());
        return completedDto;
    }
}
