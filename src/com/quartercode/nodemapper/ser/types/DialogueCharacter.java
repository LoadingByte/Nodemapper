
package com.quartercode.nodemapper.ser.types;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class DialogueCharacter {

    private String name;
    private String displayName;

    public DialogueCharacter() {

    }

    public DialogueCharacter(final String name, final String displayName) {

        this.name = name;
        this.displayName = displayName;
    }

    @XmlAttribute
    public String getName() {

        return name;
    }

    public void setName(final String name) {

        this.name = name;
    }

    @XmlValue
    public String getDisplayName() {

        return displayName;
    }

    public void setDisplayName(final String displayName) {

        this.displayName = displayName;
    }

}
