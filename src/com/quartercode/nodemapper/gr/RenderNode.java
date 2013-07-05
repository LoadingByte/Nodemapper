
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

    public RenderNode(final Tree tree, final Node node) {

        this.tree = tree;
        this.node = node;
        location = null;
        editCursor = -1;
    }

    public RenderNode(final Tree tree, final Node node, final Point location, final int editCursor) {

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

        final List<String> lines = new ArrayList<String>();
        final char[] chars = getContent().toCharArray();
        String line = "";
        for (final char c : chars) {
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

    public void setContent(final String content) {

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
