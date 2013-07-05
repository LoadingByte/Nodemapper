
package com.quartercode.nodemapper.tree;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
public class Node {

    private int                id         = -1;
    private String             content;
    private List<NodeProperty> properties = new ArrayList<NodeProperty>();
    private List<Node>         children   = new ArrayList<Node>();

    public Node() {

    }

    public Node(final int id, final String content) {

        this.id = id;
        this.content = content;
    }

    public int getId() {

        return id;
    }

    public void setId(final int id) {

        this.id = id;
    }

    public String getContent() {

        return content;
    }

    public void setContent(final String content) {

        this.content = content;
    }

    public List<NodeProperty> getProperties() {

        return properties;
    }

    public NodeProperty getProperty(final String name) {

        for (final NodeProperty property : properties) {
            if (property.getName().equals(name)) {
                return property;
            }
        }

        return null;
    }

    public void setProperties(final List<NodeProperty> properties) {

        this.properties = properties;
    }

    public void addProperty(final NodeProperty property) {

        properties.add(property);
    }

    public void removeProperty(final NodeProperty property) {

        properties.remove(property);
    }

    public List<Node> getChildren() {

        return children;
    }

    public void setChildren(final List<Node> children) {

        this.children = children;
    }

    public void addChild(final Node node) {

        children.add(node);
    }

    public void removeChild(final Node node) {

        children.remove(node);
    }

    @Override
    public Node clone() {

        final Node clone = new Node(id, content);
        for (final NodeProperty property : properties) {
            clone.addProperty(property.clone());
        }
        return clone;
    }

}
