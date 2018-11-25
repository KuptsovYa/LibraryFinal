package com.company.literature;

public abstract class Literature {

    private String author;
    private String title;
    private String content;
    private String typeOfLit;

    public Literature(String typeOfLit, String author, String title, String content) {
        this.typeOfLit = typeOfLit;
        this.author = author;
        this.title = title;
        this.content = content;
    }

    public String getTypeOfLit() { return typeOfLit; }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

}
