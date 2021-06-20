package ecs;

import javax.swing.*;
import java.awt.*;

public class SceneView extends JFrame {
    private JPanel panel;
    private JTextArea textArea;

    public SceneView(String title) {
        super(title);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.setPreferredSize(new Dimension(420, 430));
        this.setLocation(50, 100);
        this.setVisible(true);
        this.pack();
    }

    public void updateTextField(String text) {
        if (!text.equals(textArea.getText())) {
            textArea.setText(text);
            updateFrameSize();
        }
    }

    private void updateFrameSize() {
        int height = textArea.getFont().getAttributes().size() * textArea.getLineCount() * 2;
        this.setSize(420, height);
    }

}
