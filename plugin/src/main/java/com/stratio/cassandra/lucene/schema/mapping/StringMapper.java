/*
 * Copyright 2014, Stratio.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.cassandra.lucene.schema.mapping;

import com.google.common.base.Objects;
import org.apache.cassandra.db.marshal.*;

/**
 * A {@link Mapper} to map a string, not tokenized field.
 *
 * @author Andres de la Pena <adelapena@stratio.com>
 */
public class StringMapper extends KeywordMapper {

    /** The default case sensitive option. */
    public static final boolean DEFAULT_CASE_SENSITIVE = true;

    /** If it must be case sensitive. */
    private final boolean caseSensitive;

    /**
     * Builds a new {@link StringMapper}.
     *
     * @param name          The name of the mapper.
     * @param indexed       If the field supports searching.
     * @param sorted        If the field supports sorting.
     * @param caseSensitive If the getAnalyzer must be case sensitive.
     */
    public StringMapper(String name, Boolean indexed, Boolean sorted, Boolean caseSensitive) {
        super(name,
              indexed,
              sorted,
              AsciiType.instance,
              UTF8Type.instance,
              Int32Type.instance,
              LongType.instance,
              IntegerType.instance,
              FloatType.instance,
              DoubleType.instance,
              BooleanType.instance,
              UUIDType.instance,
              TimeUUIDType.instance,
              TimestampType.instance,
              BytesType.instance,
              InetAddressType.instance);
        this.caseSensitive = caseSensitive == null ? DEFAULT_CASE_SENSITIVE : caseSensitive;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    /** {@inheritDoc} */
    @Override
    public String base(String name, Object value) {
        if (value == null) {
            return null;
        } else {
            String string = value.toString();
            return caseSensitive ? string : string.toLowerCase();
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .add("indexed", indexed)
                      .add("sorted", sorted)
                      .add("caseSensitive", caseSensitive)
                      .toString();
    }
}
