import java.util.HashSet;

public class statsManager {

    static int            questionsAnswered = 0;
    static HashSet<String> decksStudied     = new HashSet<>();
    static long           totalTimeMs       = 0;

    // Passed from quizScreen → resultsScreen
    static int lastScore = 0;
    static int lastTotal = 0;

    private static long sessionStart = 0;

    /** Call when a quiz begins. */
    static void startSession(String deckName) {
        decksStudied.add(deckName);
        sessionStart = System.currentTimeMillis();
    }

    /** Call when a quiz ends (all questions answered). */
    static void endSession(int score, int total) {
        lastScore          = score;
        lastTotal          = total;
        questionsAnswered += total;
        if (sessionStart > 0) {
            totalTimeMs  += System.currentTimeMillis() - sessionStart;
            sessionStart  = 0;
        }
    }

    /** Human-readable elapsed time, e.g. "2m 14s". */
    static String formatTime(long ms) {
        long seconds = ms / 1000;
        long minutes = seconds / 60;
        long hours   = minutes / 60;
        if (hours   > 0) return hours   + "h " + (minutes % 60) + "m";
        if (minutes > 0) return minutes + "m " + (seconds % 60) + "s";
        return seconds + "s";
    }
}