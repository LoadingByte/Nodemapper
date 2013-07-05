
package com.quartercode.nodemapper.treeold;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement (namespace = "http://quartercode.com/", name = "tree")
@XmlType (propOrder = { "name", "nodes", "references" })
public class ReferenceTree {

    private String              name;
    private List<ReferenceNode> nodes = new ArrayList<ReferenceNode>();

    public ReferenceTree() {

    }

    public ReferenceTree(final String name) {

        this();

        this.name = name;
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

    public ReferenceNode getRoot() {

        return getNode(0);
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

    public void addNode(final ReferenceNode node) {

        nodes.add(node);
    }

    public void removeNode(final ReferenceNode node) {

        nodes.remove(node);

        for (final ReferenceNode childRemoveNode : nodes) {
            childRemoveNode.removeChild(node);
        }
    }

    public void cleanupNodes() {

        int id = 1;
        final List<ReferenceNode> nodes = new ArrayList<ReferenceNode>(this.nodes);
        nodes.remove(getRoot());
        for (final ReferenceNode node : nodes) {
            node.setId(id);
            id++;
        }
    }

    @XmlElementWrapper (name = "references")
    @XmlElement (name = "reference")
    public NodeReference[] getReferences() {

        final List<NodeReference> references = new ArrayList<NodeReference>();

        for (final ReferenceNode node : nodes) {
            final NodeReference reference = new NodeReference();
            reference.setNode(node.getId());
            final List<Integer> children = new ArrayList<Integer>();
            for (final ReferenceNode child : node.getChildren()) {
                children.add(child.getId());
            }
            reference.setChildren(children);
            references.add(reference);
        }

        return references.toArray(new NodeReference[references.size()]);
    }

    public void setReferences(final NodeReference[] references) {

        for (final ReferenceNode node : nodes) {
            node.getChildren().clear();
            for (final NodeReference reference : references) {
                if (reference.getNode() == node.getId() && reference.getChildren() != null) {
                    final List<ReferenceNode> children = new ArrayList<ReferenceNode>();
                    for (final int child : reference.getChildren()) {
                        for (final ReferenceNode childNode : nodes) {
                            if (childNode.getId() == child) {
                                children.add(childNode);
                            }
                        }
                    }
                    node.setChildren(children);
                    break;
                }
            }
        }
    }

    public StructureTree toStructureTree() {

        final StructureTree tree = new StructureTree(name);

        final StructureNode root = new StructureNode(getRoot().getContent());
        root.setProperties(new ArrayList<NodeProperty>(getRoot().getProperties()));
        addStructureChildren(root, getRoot());
        tree.setRoot(root);

        return tree;
    }

    private void addStructureChildren(final StructureNode node, final ReferenceNode addNode) {

        for (final ReferenceNode child : addNode.getChildren()) {
            final StructureNode childNode = new StructureNode(child.getContent());
            childNode.setProperties(new ArrayList<NodeProperty>(node.getProperties()));
            node.addChild(childNode);
            addStructureChildren(childNode, child);
        }
    }

    @Override
    public ReferenceTree clone() {

        final ReferenceTree tree = new ReferenceTree(name);

        final Map<ReferenceNode, ReferenceNode> clonedNodes = new HashMap<ReferenceNode, ReferenceNode>();
        for (final ReferenceNode node : nodes) {
            final ReferenceNode clonedNode = node.clone();
            clonedNodes.put(node, clonedNode);
            tree.addNode(clonedNode);
        }
        for (final Entry<ReferenceNode, ReferenceNode> node : clonedNodes.entrySet()) {
            for (final ReferenceNode child : node.getKey().getChildren()) {
                node.getValue().addChild(clonedNodes.get(child));
            }
        }

        return tree;
    }

}
