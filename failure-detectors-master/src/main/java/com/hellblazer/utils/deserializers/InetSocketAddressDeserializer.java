/** 
 * (C) Copyright 2010 Hal Hildebrand, All Rights Reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.hellblazer.utils.deserializers;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

/**
 * @author hhildebrand
 * 
 */
public class InetSocketAddressDeserializer extends
        FromStringDeserializer<InetSocketAddress> {
    private static final long serialVersionUID = 1L;

    public InetSocketAddressDeserializer() {
        super(InetSocketAddress.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.fasterxml.jackson.databind.deser.std.FromStringDeserializer#_deserialize
     * (java.lang.String, com.fasterxml.jackson.databind.DeserializationContext)
     */
    @Override
    protected InetSocketAddress _deserialize(String value,
                                             DeserializationContext ctxt)
                                                                         throws IOException,
                                                                         JsonProcessingException {
        int i = value.indexOf(':');
        if (i < 0) {
            throw new InvalidFormatException(
                                             String.format("Must include port: %s",
                                                           value), value,
                                             InetSocketAddress.class);
        }
        String host = value.substring(0, i);
        int port = Integer.parseInt(value.substring(i + 1));
        if (host.isEmpty() || host.equals("*")) {
            return new InetSocketAddress(port);
        }
        return new InetSocketAddress(host, port);
    }

}
