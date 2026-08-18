package sharks.lc3;

import java.util.ArrayList;
import java.util.List;

public class PasswordHistoryManager {

    private final List<String> history = new ArrayList<>();

    public boolean addPassword(String password) {
        if (password != null && !history.contains(password)) {
            return history.add(password);
        }
        return false;
    }

    public boolean removePassword(String password) {
        return history.remove(password);
    }

    public boolean containsPassword(String password) {
        return history.contains(password);
    }

    public int getSize() {
        return history.size();
    }

    public void clearHistory() {
        history.clear();
    }
}
