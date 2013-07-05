
package com.quartercode.nodemapper.ser.types;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import com.quartercode.nodemapper.ser.SerializationTree;
import com.quartercode.nodemapper.tree.Node;
import com.quartercode.nodemapper.tree.NodeProperty;
import com.quartercode.nodemapper.tree.Tree;
import com.quartercode.nodemapper.tree.TreeUtil;

@XmlRootElement (namespace = "http://quartercode.com/", name = "tree")
@XmlType (propOrder = { "name", "nodes" })
public class ReferenceTree implements SerializationTree {

    private String              name;
    private List<ReferenceNode> nodes = new ArrayList<ReferenceNode>();

    public ReferenceTree() {

    }

    @XmlAttribute
    public String getName() {

        return name;
    }

    public void setName(final String name) {

        this.name = name;
    }

    @XmlElementWrapper (name = "nodes")
    @XmlElement (name = "node")
    public List<ReferenceNode> getNodes() {

        return nodes;
    }

    public ReferenceNode getNode(final int id) {

        for (final ReferenceNode node : nodes) {
            if (node.getId() == id) {
                return node;
            }
        }

        return null;
    }

    public void setNodes(final List<ReferenceNode> nodes) {

        this.nodes = nodes;
    }

    @Override
    public void serialize(final Tree tree) {

        setName(tree.getName());

        for (final Node node : tree.getNodes()) {
            final ReferenceNode referenceNode = new ReferenceNode();
            referenceNode.setId(node.getId());
            referenceNode.setContent(node.getContent());
            referenceNode.setProperties(node.getProperties());

            final List<ReferenceChildDefinition> referenceChildren = new ArrayList<ReferenceChildDefinition>();
            for (final Node child : node.getChildren()) {
                referenceChildren.add(new ReferenceChildDefinition(child.getId()));
            }
            referenceNode.setReferenceChildren(referenceChildren);

            nodes.add(referenceNode);
        }
    }

    @Override
    public Tree deserialize() {

        final Tree tree = new Tree(getName());

        for (final ReferenceNode referenceNode : nodes) {
            final Node node = new Node(referenceNode.getId(), referenceNode.getContent());
            node.setProperties(referenceNode.getProperties());
            TreeUtil.addNode(tree, node);
        }
        for (final Node node : tree.getNodes()) {
            for (final ReferenceChildDefinition child : getNode(node.getId()).getReferenceChildren()) {
                node.addChild(tree.getNode(child.getId()));
            }
        }

        return tree;
    }

    @XmlType (propOrder = { "content", "properties", "referenceChildren" })
    static class ReferenceNode extends Node {

        private List<ReferenceChildDefinition> referenceChildren = new ArrayList<ReferenceChildDefinition>();

        public ReferenceNode() {

        }

        @Override
        @XmlAttribute
        public int getId() {

            return super.getId();
        }

        @Override
        @XmlElement (name = "property")
        public List<NodeProperty> getProperties() {

            return super.getProperties();
        }

        @Override
        @XmlTransient
        public List<Node> getChildren() {

            return super.getChildren();
        }

        @XmlElement (name = "child")
        public List<ReferenceChildDefinition> getReferenceChildren() {

            return referenceChildren;
        }

        public void setReferenceChildren(final List<ReferenceChildDefinition> referenceChildren) {

            this.referenceChildren = referenceChildren;
        }
    }

    static class ReferenceChildDefinition {

        private int id;

        public ReferenceChildDefinition() {

        }

        public ReferenceChildDefinition(final int id) {

            this.id = id;
        }

        @XmlAttribute
        public int getId() {

            return id;
        }

        public void setId(final int id) {

            this.id = id;
        }
    }

}
