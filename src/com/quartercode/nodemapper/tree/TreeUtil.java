
package com.quartercode.nodemapper.tree;

import java.util.ArrayList;
import java.util.List;

public class TreeUtil {

    public static void addNode(final Tree tree, final Node node) {

        addNode(tree, node, null);
    }

    public static void addNode(final Tree tree, final Node node, final Node parent) {

        tree.addNode(node);

        if (parent != null) {
            parent.addChild(node);
        }
    }

    public static void removeNode(final Tree tree, final Node node) {

        tree.removeNode(node);

        for (final Node parent : tree.getNodes()) {
            if (parent.getChildren().contains(node)) {
                parent.removeChild(node);
            }
        }
    }

    public static void cleanupNodes(final Tree tree) {

        int id = 1;
        final List<Node> nodes = new ArrayList<Node>(tree.getNodes());
        nodes.remove(tree.getRoot());
        for (final Node node : nodes) {
            node.setId(id);
            id++;
        }
    }

    private TreeUtil() {

    }

}
