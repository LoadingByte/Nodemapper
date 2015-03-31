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

package com.quartercode.nodemapper.ser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.filechooser.FileFilter;

public class SerializerManager {

    private static List<Serializer>            serializers = new ArrayList<>();
    private static Map<Serializer, FileFilter> fileFilters = new HashMap<>();

    public static List<Serializer> getSerializers() {

        return serializers;
    }

    public static List<Serializer> getSerializers(boolean internal) {

        List<Serializer> serializers = new ArrayList<>();
        for (Serializer serializer : SerializerManager.serializers) {
            if (internal == serializer.getClass().isAnnotationPresent(InternalSerializer.class)) {
                serializers.add(serializer);
            }
        }

        return serializers;
    }

    public static Serializer getSerializer(Class<? extends Serializer> c) {

        for (Serializer serializer : serializers) {
            if (serializer.getClass().equals(c)) {
                return serializer;
            }
        }

        return null;
    }

    public static void registerSerializer(Serializer serializer) {

        serializers.add(serializer);
    }

    public static FileFilter getFileFilter(Serializer serializer) {

        return fileFilters.get(serializer);
    }

    public static Serializer getSerializer(FileFilter fileFilter) {

        for (Entry<Serializer, FileFilter> entry : fileFilters.entrySet()) {
            if (entry.getValue().equals(fileFilter)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public static void registerFileFilter(Serializer serializer, FileFilter fileFilter) {

        fileFilters.put(serializer, fileFilter);
    }

    private SerializerManager() {

    }

}
