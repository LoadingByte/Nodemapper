
package com.quartercode.nodemapper.treeold;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType (propOrder = { "properties", "children" })
public class StructureNode {

    private String              content;
    private List<NodeProperty>  properties = new ArrayList<NodeProperty>();
    private List<StructureNode> children   = new ArrayList<StructureNode>();

    public StructureNode() {

    }

    public StructureNode(final String content) {

        this.content = content;
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

    @XmlElement (name = "child")
    public List<StructureNode> getChildren() {

        return children;
    }

    public void setChildren(final List<StructureNode> children) {

        this.children = children;
    }

    public void addChild(final StructureNode node) {

        children.add(node);
    }

    public void removeChild(final StructureNode node) {

        children.remove(node);
    }

}
