
package com.quartercode.nodemapper.treeold;

import javax.xml.bind.annotation.XmlAttribute;

public class NodeProperty {

    private String name;
    private String value;

    public NodeProperty() {

    }

    public NodeProperty(final String name, final String value) {

        this.name = name;
        this.value = value;
    }

    @XmlAttribute
    public String getName() {

        return name;
    }

    public void setName(final String name) {

        this.name = name;
    }

    @XmlAttribute
    public String getValue() {

        return value;
    }

    public void setValue(final String value) {

        this.value = value;
    }

    @Override
    public NodeProperty clone() {

        return new NodeProperty(new String(name), new String(value));
    }

}
