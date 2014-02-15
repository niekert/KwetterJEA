package kwetter.utils;

/**
 * Created by Niek on 12/02/14.
 */
public class Constants {
    private static final String PERSON_SESSION = "Person";
    public static final String MENTIONS_REGEX = "(?<=^|(?<=[^a-zA-Z0-9-_\\.]))@([A-Za-z]+[A-Za-z0-9]+)";
    public static final String HASHTAG_REGEX = "(?<=^|(?<=[^a-zA-Z0-9-_\\.]))#([A-Za-z]+[A-Za-z0-9]+)";

    public static String getPersonSession() {
        return PERSON_SESSION;
    }
}
