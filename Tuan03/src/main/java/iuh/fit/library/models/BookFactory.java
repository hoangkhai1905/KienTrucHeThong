package iuh.fit.library.models;

public class BookFactory {
    public static Book createBook(String type, String title) {
        if (type == null) {
            return null;
        }
        if (type.equalsIgnoreCase("PAPER")) {
            return new PaperBook(title);
        } else if (type.equalsIgnoreCase("EBOOK")) {
            return new EBook(title);
        } else if (type.equalsIgnoreCase("AUDIO")) {
            return new AudioBook(title);
        }
        return null;
    }
}

