import java.util.*;

public class RobotNetwork {
    private List<String> messages = new ArrayList<>();

    public synchronized void sendMessage(String message) {
        messages.add(message);
    }

    public synchronized List<String> retrieveMessages() {
        List<String> newMessages = new ArrayList<>(messages);
        messages.clear();
        return newMessages;
    }
}
