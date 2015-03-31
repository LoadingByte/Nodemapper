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

package com.quartercode.nodemapper.ser.types;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXB;
import com.quartercode.nodemapper.gr.RenderNode;
import com.quartercode.nodemapper.ser.Output;
import com.quartercode.nodemapper.ser.Serializer;
import com.quartercode.nodemapper.ser.SerializerManager;
import com.quartercode.nodemapper.tree.Node;
import com.quartercode.nodemapper.tree.NodeProperty;
import com.quartercode.nodemapper.tree.Tree;

public class DialogueSerializer extends ReferenceSerializer {

    public static void register() {

        Serializer serializer = new DialogueSerializer();
        SerializerManager.registerSerializer(serializer);
        SerializerManager.registerFileFilter(serializer, new FileNameExtensionFilter("Dialogue Tree (*.xml)", "xml"));
    }

    protected DialogueSerializer() {

    }

    @Override
    public void serialize(Tree tree, Output output) throws IOException {

        Map<Integer, String> contents = new TreeMap<>();
        Map<String, DialogueCharacter> characters = new HashMap<>();
        for (Node node : tree.getNodes()) {
            node.removeProperty(node.getProperty("x"));
            node.removeProperty(node.getProperty("y"));

            RenderNode renderNode = new RenderNode(tree, node);
            if (renderNode.getContentLines().length > 1) {
                String header = renderNode.getContentLines()[0];
                if (header.startsWith(":")) {
                    String executor = header.substring(1);
                    node.addProperty(new NodeProperty("executor", executor));
                    if (!executor.equals("player") && !characters.containsKey(executor)) {
                        characters.put(executor, new DialogueCharacter(executor, executor));
                    }
                    contents.put(renderNode.getNode().getId(), renderNode.getContent().substring(header.length() + 1));
                } else if (header.startsWith("$")) {
                    String type = header.substring(1);
                    node.addProperty(new NodeProperty(type, renderNode.getContentLines()[1]));
                }
                renderNode.setContent("");
            }
        }

        super.serialize(tree, output);

        try (PrintStream valueOutput = new PrintStream(output.getOutputStream("_values.lang", false))) {
            for (Entry<Integer, String> entry : contents.entrySet()) {
                valueOutput.println(entry.getKey() + "=" + entry.getValue().replaceAll("\\\\", " "));
            }
        }

        DialogueCharacterSet characterSet = new DialogueCharacterSet();
        characterSet.setCharacters(characters.values().toArray(new DialogueCharacter[characters.values().size()]));
        JAXB.marshal(characterSet, output.getOutputStream(".characters", false));
    }

}
