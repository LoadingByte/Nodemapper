/*
 * This file is part of Nodemapper.
 * Copyright (c) 2013 QuarterCode <http://quartercode.com/>
 *
 * Nodemapper is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nodemapper is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Nodemapper. If not, see <http://www.gnu.org/licenses/>.
 */

package com.quartercode.nodemapper.tree;

import java.util.ArrayList;
import java.util.List;

public class TreeUtil {

    public static void addNode(Tree tree, Node node) {

        addNode(tree, node, null);
    }

    public static void addNode(Tree tree, Node node, Node parent) {

        tree.addNode(node);

        if (parent != null) {
            parent.addChild(node);
        }
    }

    public static void removeNode(Tree tree, Node node) {

        tree.removeNode(node);

        for (Node parent : tree.getNodes()) {
            if (parent.getChildren().contains(node)) {
                parent.removeChild(node);
            }
        }
    }

    public static void cleanupNodes(Tree tree) {

        int id = 1;
        List<Node> nodes = new ArrayList<>(tree.getNodes());
        nodes.remove(tree.getRoot());
        for (Node node : nodes) {
            node.setId(id);
            id++;
        }
    }

    private TreeUtil() {

    }

}
