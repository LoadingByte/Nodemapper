
package com.quartercode.nodemapper.treeold;

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlList;

public class NodeReference {

    private int           node;
    private List<Integer> children;

    public NodeReference() {

    }

    @XmlAttribute
    public int getNode() {

        return node;
    }

    public void setNode(final int node) {

        this.node = node;
    }

    @XmlList
    public List<Integer> getChildren() {

        return children;
    }

    public void setChildren(final List<Integer> children) {

        this.children = children;
    }

}
