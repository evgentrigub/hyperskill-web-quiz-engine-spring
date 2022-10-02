package webquiz.engine.models;

import java.util.List;

public class QuizForUser {
    Integer id;
    String title;
    String text;
    List<String> options;

    public QuizForUser(){
        this.id = null;
        this.title = null;
        this.text = null;
        this.options = null;
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

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
