package org.north.core.entities;

import java.util.Collections;
import java.util.List;

public abstract class TreeNode<E extends TreeNode<E>> {
    protected E parent;
    protected List<E> daughters;

    public E getParent() {
        return parent;
    }

    public List<E> getDaughters() {
        return daughters;
    }

    public List<E> getSiblings() {
        if (this.parent != null) {
            return this.parent.getDaughters();
        }
        return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    public E getRoot() {
        TreeNode<E> root = this;
        while (root.parent != null) {
            root = root.parent;
        }
        return (E) root;
    }

    @SuppressWarnings("unchecked")
    public void setParent(E parent) {
        if (this.parent != null) {
            this.parent.daughters.remove(this);
        }
        if (this.daughters.contains(parent)) {
            this.daughters.remove(parent);
            parent.parent = null;
        }
        if (this.parent != parent) {
            parent.daughters.add((E) this);
        }
        this.parent = parent;
    }

    @SuppressWarnings("unchecked")
    public void setDaughters(List<E> daughters) {
        for (E daughter: daughters) {
            daughter.setParent((E) this);
        }
    }

    public boolean isParentOf(E daughter) {
        return this.daughters.contains(daughter);
    }

    public boolean isAncestorOf(E daughter) {
        TreeNode<E> selected = daughter;
        while (selected.parent != null) {
            if (selected.parent == this) {
                return true;
            }
            selected = selected.parent;
        }
        return false;
    }

}
