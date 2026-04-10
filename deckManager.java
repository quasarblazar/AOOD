import java.util.ArrayList;
import java.util.HashMap;

public class deckManager {
    static ArrayList<String> decks = new ArrayList<>();
    static HashMap<String, ArrayList<String[]>> terms = new HashMap<>();

    static void addDeck(String name) {
        decks.add(name);
        terms.put(name, new ArrayList<>());
    }

    static void removeDeck(String name) {
        decks.remove(name);
        terms.remove(name);
    }

    static void addTerm(String deck, String question, String answer) {
        if (terms.containsKey(deck)) {
            terms.get(deck).add(new String[]{question, answer});
        }
    }

    static void removeTerm(String deck, int index) {
        if (terms.containsKey(deck)) {
            ArrayList<String[]> list = terms.get(deck);
            if (index >= 0 && index < list.size()) {
                list.remove(index);
            }
        }
    }

    static void editTerm(String deck, int index, String question, String answer) {
        if (terms.containsKey(deck)) {
            ArrayList<String[]> list = terms.get(deck);
            if (index >= 0 && index < list.size()) {
                list.set(index, new String[]{question, answer});
            }
        }
    }

    static ArrayList<String[]> getTerms(String deck) {
        return terms.getOrDefault(deck, new ArrayList<>());
    }
}