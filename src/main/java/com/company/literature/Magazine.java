package com.company.literature;

public class Magazine extends Literature{

    private String classification;

    public Magazine(String typeOfLit, String author, String title, String content, String classification) {
        super(typeOfLit,author, title, content);
        this.classification = classification;
    }

    public String getClassification() {
        return classification;
    }
}
