
package com.quartercode.nodemapper.ser.types;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

        final Serializer serializer = new DialogueSerializer();
        SerializerManager.registerSerializer(serializer);
        SerializerManager.registerFileFilter(serializer, new FileNameExtensionFilter("Dialogue Tree (*.xml)", "xml"));
    }

    protected DialogueSerializer() {

    }

    @Override
    public void serialize(final Tree tree, final Output output) throws IOException {

        final Map<Integer, String> contents = new HashMap<Integer, String>();
        final Map<String, DialogueCharacter> characters = new HashMap<String, DialogueCharacter>();
        for (final Node node : tree.getNodes()) {
            node.removeProperty(node.getProperty("x"));
            node.removeProperty(node.getProperty("y"));

            final RenderNode renderNode = new RenderNode(tree, node);
            if (renderNode.getContentLines().length > 1) {
                final String header = renderNode.getContentLines()[0];
                if (header.startsWith(":")) {
                    final String executor = header.substring(1);
                    node.addProperty(new NodeProperty("executor", executor));
                    if (!executor.equals("player") && !characters.containsKey(executor)) {
                        characters.put(executor, new DialogueCharacter(executor, executor));
                    }
                } else if (header.startsWith("$")) {
                    final String type = header.substring(1);
                    node.addProperty(new NodeProperty(type, renderNode.getContentLines()[1]));
                }
                contents.put(renderNode.getNode().getId(), renderNode.getContent().substring(header.length() + 1));
                renderNode.setContent("");
            }
        }

        super.serialize(tree, output);

        final List<Integer> sortedIds = new ArrayList<Integer>(contents.keySet());
        Collections.sort(sortedIds);
        final Map<Integer, String> sortedContents = new LinkedHashMap<Integer, String>();
        for (final int id : sortedIds) {
            sortedContents.put(id, contents.get(id));
        }

        final PrintStream valueOutput = new PrintStream(output.getOutputStream("_values.lang", false));
        for (final Entry<Integer, String> entry : sortedContents.entrySet()) {
            valueOutput.println(entry.getKey() + "=" + entry.getValue().replaceAll("\\\\", " "));
        }
        valueOutput.close();

        final DialogueCharacterSet characterSet = new DialogueCharacterSet();
        characterSet.setCharacters(characters.values().toArray(new DialogueCharacter[characters.values().size()]));
        JAXB.marshal(characterSet, output.getOutputStream(".characters", false));
    }

}
