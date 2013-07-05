
package com.quartercode.nodemapper.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Tree {

    private String           name;
    private final List<Node> nodes = new ArrayList<Node>();

    public Tree() {

    }

    public Tree(final String name) {

        this.name = name;
    }

    public String getName() {

        return name;
    }

    public void setName(final String name) {

        this.name = name;
    }

    public List<Node> getNodes() {

        return nodes;
    }

    public Node getNode(final int id) {

        for (final Node node : nodes) {
            if (node.getId() == id) {
                return node;
            }
        }

        return null;
    }

    public Node getRoot() {

        return getNode(0);
    }

    public void addNode(final Node node) {

        nodes.add(node);
    }

    public void removeNode(final Node node) {

        nodes.remove(node);
    }

    @Override
    public Tree clone() {

        final Tree clone = new Tree(getName());

        final Map<Node, Node> clonedNodes = new HashMap<Node, Node>();
        for (final Node node : nodes) {
            final Node clonedNode = node.clone();
            clonedNodes.put(node, clonedNode);
            clone.addNode(clonedNode);
        }

        for (final Entry<Node, Node> entry : clonedNodes.entrySet()) {
            for (final Node child : entry.getKey().getChildren()) {
                entry.getValue().addChild(clone.getNode(child.getId()));
            }
        }

        return clone;
    }

}
