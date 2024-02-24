package org.north.core.architecture.tree;

import org.north.core.architecture.entity.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class TreeNode<E extends TreeNode<E>> {
    protected E parent;
    protected List<E> daughters;

    protected TreeNode() {
        this.daughters = new ArrayList<>();
    }

    public E getParent() {
        return parent;
    }

    public Collection<E> getDaughters() {
        if (daughters == null || daughters.isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(daughters);
    }

    public List<E> getSiblings() {
        if (parent == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(this.parent.daughters);
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
    public void addDaughter(E daughter) {
        daughter.setParent((E) this);
    }

    @SuppressWarnings("unchecked")
    public void setParent(E parent) {
        if (parent == null) {
            this.parent = null;
            return;
        }
        if (parent.isParentOf((E) this)) {
            return;
        }
        if (this.isAncestorOf(parent)) {
            throw new TreeNodeLoopException("Cannot link entity to target parent if this entity is ancestor of target parent");
        }
        if (this.parent != null) {
            this.parent.daughters.remove((E) this);
        }
        parent.daughters.add((E) this);
        this.parent = parent;
    }

    @SuppressWarnings("unchecked")
    public void setDaughters(List<E> daughters) {
        for (E daughter: daughters) {
            daughter.setParent((E) this);
        }
    }

    public boolean isParentOf(E daughter) {
        return daughter.parent == this;
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
