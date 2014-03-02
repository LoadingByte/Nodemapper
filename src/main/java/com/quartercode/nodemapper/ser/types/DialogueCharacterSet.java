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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (namespace = "http://quartercode.com/", name = "characterSet")
public class DialogueCharacterSet {

    private DialogueCharacter[] characters;

    public DialogueCharacterSet() {

    }

    @XmlElement (name = "character")
    public DialogueCharacter[] getCharacters() {

        return characters.clone();
    }

    public void setCharacters(DialogueCharacter[] characters) {

        this.characters = characters.clone();
    }

}
