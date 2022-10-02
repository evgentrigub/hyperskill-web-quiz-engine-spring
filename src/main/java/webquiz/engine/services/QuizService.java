package webquiz.engine.services;

import webquiz.engine.models.*;
import webquiz.engine.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizCompletedService quizCompletedService;

    @Autowired
    QuizService(
            QuizRepository quizRepository,
            QuizCompletedService quizCompletedService
    ){
        this.quizRepository = quizRepository;
        this.quizCompletedService = quizCompletedService;
    }

    private final QuizAnswer trueAnswer = new QuizAnswer(true, "Congratulations, you're right!");
    private final QuizAnswer falseAnswer = new QuizAnswer(false, "Wrong answer! Please, try again.");

    public Page<QuizForUser> getUserQuizzes(Integer pageNo) {
        Pageable page = PageRequest.of(pageNo, 10, Sort.by("id"));
        return this.quizRepository.findAll(page).map(QuizService::mapQuizForUser);
    }

    public QuizForUser getUserQuiz(Integer id){
        var response = quizRepository.findById(id);
        return response.map(QuizService::mapQuizForUser).orElse(null);
    }

    public Quiz getQuiz(Integer id){
        var response = quizRepository.findById(id);
        return response.orElse(null);
    }

    public QuizForUser postQuiz(QuizDto quizDto, User user){
        var options = quizDto.getOptions() == null
                ? new ArrayList<String>() : quizDto.getOptions();

        var quiz = new Quiz(
                quizDto.getAnswer(),
                quizDto.getTitle(),
                quizDto.getText(),
                options
        );

        quiz.setUser(user);

        quizRepository.save(quiz);

        return mapQuizForUser(quiz);
    }

    public QuizAnswer postQuizAnswer(Quiz quiz, Map<String, List<Integer>> answer, User user){
        AtomicBoolean isCorrect = new AtomicBoolean(true);
        var quizAnswer = quiz.getAnswer();
        var answerList = answer.get("answer");
        if(quizAnswer.size() != answerList.size()){
            return falseAnswer;
        }
        Collections.sort(quizAnswer);
        Collections.sort(answerList);
        quizAnswer.forEach(el -> {
            var res = answerList.contains(el);
            if(!res){
                isCorrect.set(false);
            }
        });

        if(isCorrect.get()){
            var completedQuiz = new QuizCompleted();
            completedQuiz.setUser(user);
            completedQuiz.setQuiz(quiz);
            this.quizCompletedService.postQuizCompleted(completedQuiz);
        }

        return isCorrect.get() ? trueAnswer : falseAnswer;
    }

    public void deleteQuiz(Quiz quiz) {
        quizRepository.delete(quiz);
    }

    private static QuizForUser mapQuizForUser(Quiz quiz) {
        var quizForUser = new QuizForUser();
        quizForUser.setId(quiz.getId());
        quizForUser.setText(quiz.getText());
        quizForUser.setTitle(quiz.getTitle());
        quizForUser.setOptions(quiz.getOptions());
        return quizForUser;
    }
}
