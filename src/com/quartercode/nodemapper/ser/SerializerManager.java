
package com.quartercode.nodemapper.ser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.filechooser.FileFilter;

public class SerializerManager {

    private static List<Serializer>            serializers = new ArrayList<Serializer>();
    private static Map<Serializer, FileFilter> fileFilters = new HashMap<Serializer, FileFilter>();

    public static List<Serializer> getSerializers() {

        return serializers;
    }

    public static List<Serializer> getSerializers(final boolean internal) {

        final List<Serializer> serializers = new ArrayList<Serializer>();
        for (final Serializer serializer : SerializerManager.serializers) {
            if (internal == serializer.getClass().isAnnotationPresent(InternalSerializer.class)) {
                serializers.add(serializer);
            }
        }

        return serializers;
    }

    public static Serializer getSerializer(final Class<? extends Serializer> c) {

        for (final Serializer serializer : serializers) {
            if (serializer.getClass().equals(c)) {
                return serializer;
            }
        }

        return null;
    }

    public static void registerSerializer(final Serializer serializer) {

        serializers.add(serializer);
    }

    public static FileFilter getFileFilter(final Serializer serializer) {

        return fileFilters.get(serializer);
    }

    public static Serializer getSerializer(final FileFilter fileFilter) {

        for (final Entry<Serializer, FileFilter> entry : fileFilters.entrySet()) {
            if (entry.getValue().equals(fileFilter)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public static void registerFileFilter(final Serializer serializer, final FileFilter fileFilter) {

        fileFilters.put(serializer, fileFilter);
    }

    private SerializerManager() {

    }

}
