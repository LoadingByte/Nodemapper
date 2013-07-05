
package com.quartercode.nodemapper.treeold;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public class ReferenceNode {

    private int                 id         = -1;
    private String              content    = "";
    private List<NodeProperty>  properties = new ArrayList<NodeProperty>();
    private List<ReferenceNode> children   = new ArrayList<ReferenceNode>();

    public ReferenceNode() {

    }

    public ReferenceNode(final int id, final String content) {

        this.id = id;
        this.content = content;
    }

    @XmlAttribute
    public int getId() {

        return id;
    }

    public void setId(final int id) {

        this.id = id;
    }

    @XmlAttribute
    public String getContent() {

        return content;
    }

    public void setContent(final String content) {

        this.content = content;
    }

    @XmlElement (name = "property")
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

    @XmlTransient
    public List<ReferenceNode> getChildren() {

        return children;
    }

    public void setChildren(final List<ReferenceNode> children) {

        this.children = children;
    }

    public void addChild(final ReferenceNode node) {

        children.add(node);
    }

    public void removeChild(final ReferenceNode node) {

        children.remove(node);
    }

    @Override
    public ReferenceNode clone() {

        final ReferenceNode node = new ReferenceNode(id, new String(content));
        for (final NodeProperty property : properties) {
            node.addProperty(property.clone());
        }

        return node;
    }

}
