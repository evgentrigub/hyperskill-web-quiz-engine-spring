package webquiz.engine;

import webquiz.engine.models.*;
import webquiz.engine.services.QuizCompletedService;
import webquiz.engine.services.QuizService;
import webquiz.engine.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Validated
public class Controller {

    private final UserService userService;
    private final QuizService quizService;
    private final QuizCompletedService quizCompletedService;

    @Autowired
    public Controller(
            QuizService quizService,
            UserService userService,
            QuizCompletedService quizCompletedService) {
        this.quizService = quizService;
        this.userService = userService;
        this.quizCompletedService = quizCompletedService;
    }

    @GetMapping("/quizzes")
    public ResponseEntity<Page<QuizForUser>> getAll(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer pageNo
    ){
        var quizzes =  this.quizService.getUserQuizzes(pageNo);
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }

    @GetMapping("/quizzes/completed")
    public ResponseEntity<Page<QuizCompletedDto>> getCompleted(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) Integer pageNo,
            Principal principal
    ){
        User user = this.userService.getUserIfExists(principal.getName());
        var quizzes =  this.quizCompletedService.getCompletedQuiz(pageNo, user);
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }

    @GetMapping("/quizzes/{id}")
    public ResponseEntity<QuizForUser> getQuiz(@PathVariable Integer id) {
        var quiz = quizService.getUserQuiz(id);
        if (quiz == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found. Quiz ID: " + id);
        }

        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @PostMapping("/quizzes")
    public ResponseEntity<QuizForUser> postQuiz(@Valid @RequestBody QuizDto quizDto, Principal principal) {
        User user = this.userService.getUserIfExists(principal.getName());
        var quiz = quizService.postQuiz(quizDto, user);
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @PostMapping("/quizzes/{id}/solve")
    public ResponseEntity<QuizAnswer> postQuizAnswer(
            @PathVariable Integer id,
            @RequestBody Map<String, List<Integer>> answer,
            Principal principal
    ){
        User user = this.userService.getUserIfExists(principal.getName());
        var quiz = this.quizService.getQuiz(id);
        if (quiz == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found. ID: " + id);
        }

        var solution = this.quizService.postQuizAnswer(quiz, answer, user);
        return new ResponseEntity<>(solution, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@Valid @RequestBody User user) {
        if (userService.isEmailExists(user.getEmail())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "The email is already taken by another user. Email: " + user.getEmail());
        }

        this.userService.postUser(user);
        return new ResponseEntity<>("The registration has been completed successfully", HttpStatus.OK);
    }

    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<String> deleteQuiz(@PathVariable Integer id, Principal principal){
        User user = this.userService.getUserIfExists(principal.getName());
        Quiz quiz = quizService.getQuiz(id);
        if(quiz == null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Quiz not found. Quiz ID: " + id);
        }

        if (!quiz.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Quiz belong another user.");
        }

        quizService.deleteQuiz(quiz);
        return new ResponseEntity<>("Successful", HttpStatus.NO_CONTENT);
    }

}
