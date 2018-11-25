package com.company.literature;

import java.util.List;

public class Book extends Literature {

    private List<String> tableOfContent;

    public Book(String typeOfLit, String author, String title, String content, List<String> tableOfContent) {
        super(typeOfLit, author, title, content);
        this.tableOfContent = tableOfContent;
    }

    public List<String> getTableOfContent() {
        return tableOfContent;
    }
}
