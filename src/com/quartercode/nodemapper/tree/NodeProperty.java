
package com.quartercode.nodemapper.tree;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

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

    @XmlValue
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
