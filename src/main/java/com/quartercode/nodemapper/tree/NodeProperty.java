/*
 * This file is part of Nodemapper.
 * Copyright (c) 2013 QuarterCode <http://quartercode.com/>
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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class NodeProperty implements Cloneable {

    private String name;
    private String value;

    public NodeProperty() {

    }

    public NodeProperty(String name, String value) {

        this.name = name;
        this.value = value;
    }

    @XmlAttribute
    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    @XmlValue
    public String getValue() {

        return value;
    }

    public void setValue(String value) {

        this.value = value;
    }

    @Override
    public NodeProperty clone() {

        return new NodeProperty(new String(name), new String(value));
    }

}
