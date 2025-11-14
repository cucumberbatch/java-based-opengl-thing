package org.north.core.editor.ui;

import org.slf4j.Logger;
import org.north.core.architecture.entity.Entity;
import org.slf4j.LoggerFactory;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class EntityTreeModel implements TreeModel {
    private final Logger log = LoggerFactory.getLogger(EntityTreeModel.class);
    private final Entity root;

    public EntityTreeModel(Entity root) {
        this.root = root;
    }

    @Override
    public Object getRoot() {
        log.info("[TreeModel] getting root. Result: " + root.getName());
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        int currentIndex = 0;
        Entity child = ((Entity) parent).getLeftMostDaughter();
        while (currentIndex++ != index) {
            child = child.getNextSibling();
        }
        log.info("[TreeModel] getting child by index '" + index + "' from parent '" + ((Entity) parent).getName() + "'. Result: " + child.getName());
        return child;
    }

    @Override
    public int getChildCount(Object parent) {
        int size = ((Entity) parent).getSubtrees().size();
        log.info("[TreeModel] getting child count from parent '" + ((Entity) parent).getName() + "'. Result: " + size);
        return size;
    }

    @Override
    public boolean isLeaf(Object node) {
        boolean leaf = ((Entity) node).isLeaf();
        log.info("[TreeModel] checking if node '" + ((Entity) node).getName() + "' is leaf. Result: " + leaf);
        return leaf;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        log.info("[TreeModel] (not implemented) valueForPathChanged invoked");
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        int index = 0;
        Entity entity = ((Entity) parent).getLeftMostDaughter();
        while (entity != child) {
            entity = entity.getNextSibling();
            index++;
        }
        log.info("[TreeModel] getting child index of '" + ((Entity) child).getName() + "'. Result: " + index);
        return index;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        log.info("[TreeModel] (not implemented) addTreeModelListener invoked");
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        log.info("[TreeModel] (not implemented) removeTreeModelListener invoked");
    }
}
