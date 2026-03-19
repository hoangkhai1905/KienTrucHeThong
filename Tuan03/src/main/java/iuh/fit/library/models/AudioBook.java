package iuh.fit.library.models;

public class AudioBook implements Book {
    private String title;

    public AudioBook(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getType() {
        return "Audio Book";
    }
}

