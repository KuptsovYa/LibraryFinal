package com.company.literature;

public class Catalog extends Literature {

    private String type;
    private String pointers;

    public Catalog(String typeOfLit, String author, String title, String content, String type, String pointers) {
        super(typeOfLit, author, title, content);
        this.type = type;
        this.pointers = pointers;
    }

    public String getType() {
        return type;
    }

    public String getPointers() {
        return pointers;
    }
}
