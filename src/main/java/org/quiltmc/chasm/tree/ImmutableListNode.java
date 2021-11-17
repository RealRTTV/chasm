package org.quiltmc.chasm.tree;

import org.quiltmc.chasm.transformer.NodePath;

import java.util.*;

public class ImmutableListNode extends AbstractList<Node> implements ListNode {
    private final List<Node> internal;
    private NodePath path;

    public ImmutableListNode(ListNode listNode) {
        this.internal = new ArrayList<>(listNode);
        this.path = listNode.getPath();
    }

    @Override
    public void initializePath(NodePath path) {
        throw new UnsupportedOperationException("Can't set path on immutable node.");
    }

    @Override
    public NodePath getPath() {
        return path;
    }

    @Override
    public boolean isImmutable() {
        return true;
    }

    @Override
    public Node get(int index) {
        return internal.get(index);
    }

    @Override
    public int size() {
        return internal.size();
    }
}