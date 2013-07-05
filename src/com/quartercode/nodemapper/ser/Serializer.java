
package com.quartercode.nodemapper.ser;

import java.io.IOException;
import com.quartercode.nodemapper.tree.Tree;

public abstract interface Serializer {

    public void serialize(Tree tree, Output output) throws IOException;

    public Tree deserialize(Input input) throws IOException;

}
