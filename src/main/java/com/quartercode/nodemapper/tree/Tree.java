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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Tree implements Cloneable {

    private String           name;
    private final List<Node> nodes = new ArrayList<>();

    public Tree() {

    }

    public Tree(String name) {

        this.name = name;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public List<Node> getNodes() {

        return nodes;
    }

    public Node getNode(int id) {

        for (Node node : nodes) {
            if (node.getId() == id) {
                return node;
            }
        }

        return null;
    }

    public Node getRoot() {

        return getNode(0);
    }

    public void addNode(Node node) {

        nodes.add(node);
    }

    public void removeNode(Node node) {

        nodes.remove(node);
    }

    @Override
    public Tree clone() {

        Tree clone = new Tree(getName());

        Map<Node, Node> clonedNodes = new HashMap<>();
        for (Node node : nodes) {
            Node clonedNode = node.clone();
            clonedNodes.put(node, clonedNode);
            clone.addNode(clonedNode);
        }

        for (Entry<Node, Node> entry : clonedNodes.entrySet()) {
            for (Node child : entry.getKey().getChildren()) {
                entry.getValue().addChild(clone.getNode(child.getId()));
            }
        }

        return clone;
    }

}
