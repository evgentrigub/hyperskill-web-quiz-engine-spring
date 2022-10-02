package webquiz.engine.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;

public class QuizDto {
    ArrayList<Integer> answer;
    @NotBlank(message = "title")
    String title;
    @NotBlank(message = "text")
    String text;
    @NotNull(message = "options")
    @Size()
    ArrayList<String> options;

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

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public ArrayList<Integer> getAnswer() {
        return answer;
    }

    public void setAnswer(ArrayList<Integer> answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "QuizDto{" +
                "answer=" + answer +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", options=" + options +
                '}';
    }
}
