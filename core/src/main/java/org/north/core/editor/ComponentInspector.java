package org.north.core.editor;

import org.north.core.architecture.entity.Entity;
import org.north.core.component.Component;
import org.north.core.utils.logger.LoggerFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;

public class ComponentInspector extends JFrame {
    private final Logger log = LoggerFactory.createLogger(ComponentInspector.class);

    private Entity rootEntityNode;

    public ComponentInspector(String title, Entity rootNode) {
        super(title);
        rootEntityNode = rootNode;

        setLocation(new Point(200, 400));
        setIconImage(createComponentInspectorIconImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        //        panel.setLayout(new GridLayout(1, 2));
        //        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setLayout(new BorderLayout());

        JPanel treePanel = new JPanel();
        JPanel componentPanel = new JPanel();

        treePanel.setLayout(new BorderLayout());
        JPanel innerTreePanel = new JPanel();
        innerTreePanel.setLayout(new BorderLayout());

        componentPanel.setLayout(new BoxLayout(componentPanel, BoxLayout.PAGE_AXIS));

        EntityTreeModel treeModel = new EntityTreeModel(rootEntityNode);
        JTree tree = new JTree(treeModel);

        DefaultTreeCellRenderer cellRenderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        cellRenderer.setOpenIcon(new ImageIcon());
        cellRenderer.setClosedIcon(new ImageIcon());
        cellRenderer.setLeafIcon(new ImageIcon());

//        treePanel.setMinimumSize(new Dimension(240, 400));
        tree.addTreeSelectionListener(createTreeSelectionListener(componentPanel));

        treePanel.add(innerTreePanel, BorderLayout.WEST);
        innerTreePanel.add(tree, BorderLayout.NORTH);

        panel.add(new JScrollPane(treePanel), BorderLayout.NORTH);
        panel.add(new JScrollPane(componentPanel), BorderLayout.EAST);

        add(panel);
        pack();
        setVisible(true);
    }

    private TreeSelectionListener createTreeSelectionListener(JPanel componentPanel) {
        return selectionEvent -> {
            Collection<Component> components =
                    ((Entity) selectionEvent.getPath().getLastPathComponent()).getComponentMap().values();

            for (java.awt.Component component : componentPanel.getComponents()) {
                componentPanel.remove(component);
            }

            for (Component component : components) {
                componentPanel.add(new ComponentPanel(component));
            }
            componentPanel.add(new Container());
            componentPanel.revalidate();
            ComponentInspector.this.pack();
        };
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
                add(new ComponentField(component, field));
            }
        }
    }

    public class ComponentField extends JComponent {
        private Component component;
        private Class<?> fieldType;
        private Field field;
        private Object value;

        public ComponentField(Component component, Field field) {
            super();
            try {
                this.component = component;
                this.field = field;
                this.fieldType = field.getType();
                this.value = field.get(component);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            setLayout(new BorderLayout());

            add(new JLabel(field.getName()), BorderLayout.WEST);
            JTextField textField = new JTextField(String.valueOf(value), 20);
            setMaximumSize(new Dimension(240, 24));
            add(textField, BorderLayout.EAST);

        }
    }

}
