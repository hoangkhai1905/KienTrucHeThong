package iuh.fit.library.models;

public class EBook implements Book {
    private String title;

    public EBook(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getType() {
        return "E-Book";
    }
}

