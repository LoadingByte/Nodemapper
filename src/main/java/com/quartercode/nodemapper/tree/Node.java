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

package com.quartercode.nodemapper.tree;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlTransient;

@XmlTransient
public class Node implements Cloneable {

    private int                id         = -1;
    private String             content;
    private List<NodeProperty> properties = new ArrayList<NodeProperty>();
    private List<Node>         children   = new ArrayList<Node>();

    public Node() {

    }

    public Node(int id, String content) {

        this.id = id;
        this.content = content;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {

        this.content = content;
    }

    public List<NodeProperty> getProperties() {

        return properties;
    }

    public NodeProperty getProperty(String name) {

        for (NodeProperty property : properties) {
            if (property.getName().equals(name)) {
                return property;
            }
        }

        return null;
    }

    public void setProperties(List<NodeProperty> properties) {

        this.properties = properties;
    }

    public void addProperty(NodeProperty property) {

        properties.add(property);
    }

    public void removeProperty(NodeProperty property) {

        properties.remove(property);
    }

    public List<Node> getChildren() {

        return children;
    }

    public void setChildren(List<Node> children) {

        this.children = children;
    }

    public void addChild(Node node) {

        children.add(node);
    }

    public void removeChild(Node node) {

        children.remove(node);
    }

    @Override
    public Node clone() {

        Node clone = new Node(id, content);
        for (NodeProperty property : properties) {
            clone.addProperty(property.clone());
        }
        return clone;
    }

}
