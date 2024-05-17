package org.north.core.editor;

import org.north.core.architecture.entity.Entity;
import org.north.core.component.Component;
import org.north.core.utils.logger.LoggerFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComponentInspector extends JFrame {
    private final Logger log = LoggerFactory.createLogger(ComponentInspector.class);

    public ComponentInspector(String title, Entity rootNode) {
        super(title);

        setLocation(new Point(200, 400));
        setIconImage(createComponentInspectorIconImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        setPreferredSize(new Dimension(500, 400));

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());


        JPanel treePanel = new JPanel();
        JPanel componentPanel = new JPanel();

//        treePanel.setPreferredSize(new Dimension(200, 200));
//        treePanel.setLayout(new GridBagLayout());

        EntityTreeModel treeModel = new EntityTreeModel(rootNode);
        JTree tree = new JTree(treeModel);

        DefaultTreeCellRenderer cellRenderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        cellRenderer.setOpenIcon(new ImageIcon());
        cellRenderer.setClosedIcon(new ImageIcon());
        cellRenderer.setLeafIcon(new ImageIcon());

        tree.addTreeSelectionListener(selectionEvent -> {
            Collection<Component> components =
                    ((Entity) selectionEvent.getPath().getLastPathComponent()).getComponentMap().values();

            for (java.awt.Component component : componentPanel.getComponents()) {
                componentPanel.remove(component);
            }

            for (Component component : components) {
                componentPanel.add(new ComponentPanel(component));
            }
            componentPanel.revalidate();
        });

        treePanel.add(tree);
        panel.add(new JScrollPane(treePanel), BorderLayout.WEST);

//        add(Box.createHorizontalGlue());

        add(new JPanel(), BorderLayout.CENTER);

//        add(Box.createHorizontalGlue());

//        componentPanel.setPreferredSize(new Dimension(200, 200));

        componentPanel.setLayout(new BoxLayout(componentPanel, BoxLayout.Y_AXIS));
        panel.add(new JScrollPane(componentPanel), BorderLayout.EAST);

        add(panel);
        pack();
        setVisible(true);
    }

    public class ComponentPanel extends JPanel {
        Component component;
        Field[] componentFields;
        Class<? extends Component> componentType;

        ComponentPanel(Component component) {
            this.component = component;
            this.componentType = component.getClass();
            this.componentFields = EditorUtils.getComponentFields(component);
            String componentName = componentType.getSimpleName();

            TitledBorder border = new TitledBorder(componentName);
            setBorder(border);

            log.info(Arrays.toString(componentFields));

            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            for (Field field : componentFields) {
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                try {
                    panel.add(new JLabel(field.getName()), BorderLayout.WEST);
                    panel.add(new JTextField(String.valueOf(field.get(component))), BorderLayout.EAST);
                    add(panel);
                } catch (IllegalAccessException e) {
                    log.log(Level.WARNING, "Failed to get component value", e);
                }
            }
        }
    }

    private BufferedImage createComponentInspectorIconImage() {
        BufferedImage iconImage = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics g = iconImage.getGraphics();

        g.setColor(Color.decode("#B4B8C5"));
        g.fillRect(12, 12, 28, 24);

        g.setColor(Color.decode("#E9EBF8"));
        g.fillRect(8, 8, 18, 21);

        g.setColor(Color.decode("#FEFFFE"));
        g.fillRect(2, 2, 18, 24);

        g.setColor(Color.decode("#A5A299"));
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("i", 8, 22);

        return iconImage;
    }

}
