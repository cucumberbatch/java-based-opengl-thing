package org.north.core.architecture.tree;

public class TreeNodeLoopException extends RuntimeException {
    public TreeNodeLoopException(String message) {
        super(message);
    }
}
