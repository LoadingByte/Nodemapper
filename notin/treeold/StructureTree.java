
package com.quartercode.nodemapper.treeold;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (namespace = "http://quartercode.com/", name = "tree")
public class StructureTree {

    private String        name;
    private StructureNode root;

    public StructureTree() {

    }

    public StructureTree(final String name) {

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

    public StructureNode getRoot() {

        return root;
    }

    public void setRoot(final StructureNode root) {

        this.root = root;
    }

    public List<StructureNode> getNodes() {

        return getNodes(root);
    }

    private List<StructureNode> getNodes(final StructureNode node) {

        final List<StructureNode> nodes = new ArrayList<StructureNode>();
        nodes.add(node);
        for (final StructureNode child : node.getChildren()) {
            nodes.addAll(getNodes(child));
        }

        return nodes;
    }

    public ReferenceTree toReferenceTree() {

        final ReferenceTree tree = new ReferenceTree(name);

        final Map<StructureNode, ReferenceNode> nodes = new HashMap<StructureNode, ReferenceNode>();
        int id = 0;
        for (final StructureNode node : getNodes()) {
            final ReferenceNode convertNode = new ReferenceNode(id, node.getContent());
            convertNode.setProperties(new ArrayList<NodeProperty>(node.getProperties()));
            nodes.put(node, convertNode);
            id++;
        }

        for (final Entry<StructureNode, ReferenceNode> node : nodes.entrySet()) {
            for (final StructureNode child : node.getKey().getChildren()) {
                node.getValue().addChild(nodes.get(child));
            }
        }

        tree.setNodes(new ArrayList<ReferenceNode>(nodes.values()));

        return tree;
    }

}
