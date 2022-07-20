package ecs;

import ecs.entities.Entity;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SceneView extends JFrame {
    private JPanel panel;
    private JPanel scrollablePanel;
    private JButton button;

    private Map<String, JComponent> components = new HashMap<>();

    public SceneView(String title) {
        super(title);

        // scrollablePanel.setLayout(new BoxLayout(scrollablePanel, BoxLayout.Y_AXIS));
        // scrollablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

//        scrollablePanel.setLayout(new FlowLayout());

        // this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // this.setContentPane(panel);
        // this.setPreferredSize(new Dimension(420, 430));
        // this.setLocation(50, 100);
        // this.setVisible(true);
        // this.pack();
    }

    public void addEntityInfo(Entity entity) {
        String name = entity.name;

        if (!components.containsKey(name)) {
            JPanel horizontalPanel = new JPanel();
            horizontalPanel.setLayout(new BoxLayout(horizontalPanel, BoxLayout.Y_AXIS));
            horizontalPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            JButton button = new JButton(name);
            button.setPreferredSize(new Dimension(200, 15));
            JTextArea textArea = new JTextArea(entity.transform.toString());
            horizontalPanel.add(button);
            horizontalPanel.add(textArea);
            scrollablePanel.add(horizontalPanel);
            components.put(name, button);
        }
    }

    public void updateTextField(String text) {
//        if (!text.equals(textArea.getText())) {
//            textArea.setText(text);
//            updateFrameSize();
//        }
    }

    private void updateFrameSize() {
//        int height = textArea.getFont().getAttributes().size() * textArea.getLineCount() * 2;
//        this.setSize(420, height);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
