package iuh.fit.library.models;

public class PaperBook implements Book {
    private String title;

    public PaperBook(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getType() {
        return "Paper Book";
    }
}

