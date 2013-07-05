
package com.quartercode.nodemapper.ser.types;

import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.quartercode.nodemapper.ser.Output;
import com.quartercode.nodemapper.ser.Serializer;
import com.quartercode.nodemapper.ser.SerializerManager;
import com.quartercode.nodemapper.tree.Node;
import com.quartercode.nodemapper.tree.Tree;

public class ReferenceXMLSerializer extends ReferenceSerializer {

    public static void register() {

        final Serializer serializer = new ReferenceXMLSerializer();
        SerializerManager.registerSerializer(serializer);
        SerializerManager.registerFileFilter(serializer, new FileNameExtensionFilter("Reference XML Tree (*.xml)", "xml"));
    }

    protected ReferenceXMLSerializer() {

    }

    @Override
    public void serialize(final Tree tree, final Output output) throws IOException {

        for (final Node node : tree.getNodes()) {
            node.removeProperty(node.getProperty("x"));
            node.removeProperty(node.getProperty("y"));
        }

        super.serialize(tree, output);
    }

}
