
package com.quartercode.nodemapper.ser.types;

import java.io.IOException;
import javax.xml.bind.JAXB;
import com.quartercode.nodemapper.ser.Input;
import com.quartercode.nodemapper.ser.Output;
import com.quartercode.nodemapper.ser.Serializer;
import com.quartercode.nodemapper.tree.Tree;

public class ReferenceSerializer implements Serializer {

    protected ReferenceSerializer() {

    }

    @Override
    public void serialize(final Tree tree, final Output output) throws IOException {

        final ReferenceTree referenceTree = new ReferenceTree();
        referenceTree.serialize(tree);
        JAXB.marshal(referenceTree, output.getOutputStream());
    }

    @Override
    public Tree deserialize(final Input input) throws IOException {

        final ReferenceTree referenceTree = JAXB.unmarshal(input.getInputStream(), ReferenceTree.class);
        return referenceTree.deserialize();
    }

}
