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

package com.quartercode.nodemapper.gr;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import com.quartercode.nodemapper.tree.Node;
import com.quartercode.nodemapper.tree.Tree;

public class RenderNode {

    private final Tree  tree;
    private final Node  node;
    private final Point location;
    private final int   editCursor;

    public RenderNode(Tree tree, Node node) {

        this.tree = tree;
        this.node = node;
        location = null;
        editCursor = -1;
    }

    public RenderNode(Tree tree, Node node, Point location, int editCursor) {

        this.tree = tree;
        this.node = node;
        this.location = location;
        this.editCursor = editCursor;
    }

    public Tree getTree() {

        return tree;
    }

    public Node getNode() {

        return node;
    }

    public String getContent() {

        return node.getContent() != null ? node.getContent() : "";
    }

    public String[] getContentLines() {

        List<String> lines = new ArrayList<>();
        char[] chars = getContent().toCharArray();
        String line = "";
        for (char c : chars) {
            if (c == '\\') {
                lines.add(line);
                line = "";
            } else {
                line += c;
            }
        }
        lines.add(line);

        return lines.toArray(new String[lines.size()]);
    }

    public void setContent(String content) {

        node.setContent(content.isEmpty() ? null : content);
    }

    public List<Node> getChildren() {

        return node.getChildren();
    }

    public Point getLocation() {

        return location;
    }

    public int getEditCursor() {

        return editCursor;
    }

    public boolean isEdit() {

        return editCursor >= 0;
    }

}
