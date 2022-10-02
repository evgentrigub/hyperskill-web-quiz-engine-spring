package webquiz.engine.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "quiz_id")
    Integer id;

    @Column(name = "title")
    String title;
    @Column(name = "text")
    String text;
    @Column(name = "options")
    ArrayList<String> options;
    @Column(name = "answer")
    ArrayList<Integer> answer;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "quiz")
    private List<QuizCompleted> completions;

    public Quiz(ArrayList<Integer> answer, String title, String text, ArrayList<String> options){
        this.answer = answer == null ? new ArrayList<>() : answer;
        this.title = title;
        this.text = text;
        this.options = options;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public List<Integer> getAnswer() {
        List<Integer> x = null;
        if (Objects.equals(x, answer)){
            return new ArrayList<>();
        }
        return answer;
    }

    public void setAnswer(ArrayList<Integer> answer) {
        this.answer = answer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<QuizCompleted> getCompletions() {
        return completions;
    }

    public void setCompletions(List<QuizCompleted> completions) {
        this.completions = completions;
    }
}

