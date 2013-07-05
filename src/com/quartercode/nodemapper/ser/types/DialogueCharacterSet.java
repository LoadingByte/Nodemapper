
package com.quartercode.nodemapper.ser.types;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (namespace = "http://quartercode.com/", name = "characterSet")
public class DialogueCharacterSet {

    private DialogueCharacter[] characters;

    public DialogueCharacterSet() {

    }

    @XmlElement (name = "character")
    public DialogueCharacter[] getCharacters() {

        return characters;
    }

    public void setCharacters(final DialogueCharacter[] characters) {

        this.characters = characters;
    }

}
