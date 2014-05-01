/*
 * This file is part of Nodemapper.
 * Copyright (c) 2013 QuarterCode <http://www.quartercode.com/>
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
    private List<ReferenceNode> nodes = new ArrayList<>();

    public ReferenceTree() {

    }

    @XmlAttribute
    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    @XmlElementWrapper (name = "nodes")
    @XmlElement (name = "node")
    public List<ReferenceNode> getNodes() {

        return nodes;
    }

    public ReferenceNode getNode(int id) {

        for (ReferenceNode node : nodes) {
            if (node.getId() == id) {
                return node;
            }
        }

        return null;
    }

    public void setNodes(List<ReferenceNode> nodes) {

        this.nodes = nodes;
    }

    @Override
    public void serialize(Tree tree) {

        setName(tree.getName());

        for (Node node : tree.getNodes()) {
            ReferenceNode referenceNode = new ReferenceNode();
            referenceNode.setId(node.getId());
            referenceNode.setContent(node.getContent());
            referenceNode.setProperties(node.getProperties());

            List<ReferenceChildDefinition> referenceChildren = new ArrayList<>();
            for (Node child : node.getChildren()) {
                referenceChildren.add(new ReferenceChildDefinition(child.getId()));
            }
            referenceNode.setReferenceChildren(referenceChildren);

            nodes.add(referenceNode);
        }
    }

    @Override
    public Tree deserialize() {

        Tree tree = new Tree(getName());

        for (ReferenceNode referenceNode : nodes) {
            Node node = new Node(referenceNode.getId(), referenceNode.getContent());
            node.setProperties(referenceNode.getProperties());
            TreeUtil.addNode(tree, node);
        }
        for (Node node : tree.getNodes()) {
            for (ReferenceChildDefinition child : getNode(node.getId()).getReferenceChildren()) {
                node.addChild(tree.getNode(child.getId()));
            }
        }

        return tree;
    }

    @XmlType (propOrder = { "content", "properties", "referenceChildren" })
    static class ReferenceNode extends Node {

        private List<ReferenceChildDefinition> referenceChildren = new ArrayList<>();

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

        public void setReferenceChildren(List<ReferenceChildDefinition> referenceChildren) {

            this.referenceChildren = referenceChildren;
        }
    }

    static class ReferenceChildDefinition {

        private int id;

        public ReferenceChildDefinition() {

        }

        public ReferenceChildDefinition(int id) {

            this.id = id;
        }

        @XmlAttribute
        public int getId() {

            return id;
        }

        public void setId(int id) {

            this.id = id;
        }
    }

}
