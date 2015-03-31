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

import javax.swing.filechooser.FileNameExtensionFilter;
import com.quartercode.nodemapper.ser.InternalSerializer;
import com.quartercode.nodemapper.ser.Serializer;
import com.quartercode.nodemapper.ser.SerializerManager;

@InternalSerializer
public class InternalReferenceSerializer extends ReferenceSerializer {

    public static void register() {

        Serializer serializer = new InternalReferenceSerializer();
        SerializerManager.registerSerializer(serializer);
        SerializerManager.registerFileFilter(serializer, new FileNameExtensionFilter("Nodemapper File (*.nxml)", "nxml"));
    }

    protected InternalReferenceSerializer() {

    }

}
