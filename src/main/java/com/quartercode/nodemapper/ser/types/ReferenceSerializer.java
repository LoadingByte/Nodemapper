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
    public void serialize(Tree tree, Output output) throws IOException {

        ReferenceTree referenceTree = new ReferenceTree();
        referenceTree.serialize(tree);
        JAXB.marshal(referenceTree, output.getOutputStream());
    }

    @Override
    public Tree deserialize(Input input) throws IOException {

        ReferenceTree referenceTree = JAXB.unmarshal(input.getInputStream(), ReferenceTree.class);
        return referenceTree.deserialize();
    }

}
