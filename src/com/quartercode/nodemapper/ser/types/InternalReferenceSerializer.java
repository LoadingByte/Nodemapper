
package com.quartercode.nodemapper.ser.types;

import javax.swing.filechooser.FileNameExtensionFilter;
import com.quartercode.nodemapper.ser.InternalSerializer;
import com.quartercode.nodemapper.ser.Serializer;
import com.quartercode.nodemapper.ser.SerializerManager;

@InternalSerializer
public class InternalReferenceSerializer extends ReferenceSerializer {

    public static void register() {

        final Serializer serializer = new InternalReferenceSerializer();
        SerializerManager.registerSerializer(serializer);
        SerializerManager.registerFileFilter(serializer, new FileNameExtensionFilter("Nodemapper File (*.nxml)", "nxml"));
    }

    protected InternalReferenceSerializer() {

    }

}
