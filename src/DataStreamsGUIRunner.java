import javax.swing.*;

public class DataStreamsGUIRunner {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DataStreamsGUIFrame();
        });
    }
}
