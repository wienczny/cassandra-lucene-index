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

import com.stratio.cassandra.lucene.schema.Schema;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.search.SortField;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class DateMapperTest {

    private static final String PATTERN = "yyyy-MM-dd";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);

    @Test
    public void testConstructorWithoutArgs() {
        DateMapper mapper = new DateMapper("field", null, null, null);
        assertEquals(Mapper.DEFAULT_INDEXED, mapper.isIndexed());
        assertEquals(Mapper.DEFAULT_SORTED, mapper.isSorted());
        assertEquals(DateMapper.DEFAULT_PATTERN, mapper.getPattern());
    }

    @Test
    public void testConstructorWithAllArgs() {
        DateMapper mapper = new DateMapper("field", false, true, PATTERN);
        assertFalse(mapper.isIndexed());
        assertTrue(mapper.isSorted());
        assertEquals(PATTERN, mapper.getPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithWrongPattern() {
        new DateMapper("field", false, false, "hello");
    }

    @Test()
    public void testBaseClass() {
        DateMapper mapper = new DateMapper("field", null, null, PATTERN);
        assertEquals(Long.class, mapper.baseClass());
    }

    @Test()
    public void testSortField() {
        DateMapper mapper = new DateMapper("field", null, null, PATTERN);
        SortField sortField = mapper.sortField(true);
        assertNotNull(sortField);
        assertTrue(sortField.getReverse());
    }

    @Test()
    public void testValueNull() {
        DateMapper mapper = new DateMapper("field", null, null, PATTERN);
        Long parsed = mapper.base("test", null);
        assertNull(parsed);
    }

    @Test
    public void testValueDate() {
        DateMapper mapper = new DateMapper("field", null, null, PATTERN);
        Date date = new Date();
        long parsed = mapper.base("test", date);
        assertEquals(date.getTime(), parsed);
    }

    @Test
    public void testValueInteger() {
        DateMapper mapper = new DateMapper("field", null, null, PATTERN);
        Long parsed = mapper.base("test", 3);
        assertEquals(Long.valueOf(3), parsed);
    }

    @Test
    public void testValueLong() {
        DateMapper mapper = new DateMapper("field", null, null, PATTERN);
        Long parsed = mapper.base("test", 3l);
        assertEquals(Long.valueOf(3), parsed);
    }

    @Test
    public void testValueFloatWithoutDecimal() {
        DateMapper mapper = new DateMapper("field", null, null, PATTERN);
        Long parsed = mapper.base("test", 3f);
        assertEquals(Long.valueOf(3), parsed);
    }

    @Test
    public void testValueFloatWithDecimalFloor() {
        DateMapper mapper = new DateMapper("field", null, null, PATTERN);
        Long parsed = mapper.base("test", 3.5f);
        assertEquals(Long.valueOf(3), parsed);

    }

    @Test
    public void testValueFloatWithDecimalCeil() {
        DateMapper mapper = new DateMapper("field", null, null, PATTERN);
        Long parsed = mapper.base("test", 3.6f);
        assertEquals(Long.valueOf(3), parsed);

    }

    @Test
    public void testValueDoubleWithoutDecimal() {
        DateMapper mapper = new DateMapper("field", null, null, PATTERN);
        Long parsed = mapper.base("test", 3d);
        assertEquals(Long.valueOf(3), parsed);
    }

    @Test
    public void testValueDoubleWithDecimalFloor() {
        DateMapper mapper = new DateMapper("field", null, null, PATTERN);
        Long parsed = mapper.base("test", 3.5d);
        assertEquals(Long.valueOf(3), parsed);

    }

    @Test
    public void testValueDoubleWithDecimalCeil() {
        DateMapper mapper = new DateMapper("field", null, null, PATTERN);
        Long parsed = mapper.base("test", 3.6d);
        assertEquals(Long.valueOf(3), parsed);

    }

    @Test
    public void testValueStringWithPattern() throws ParseException {
        DateMapper mapper = new DateMapper("field", null, null, PATTERN);
        long parsed = mapper.base("test", "2014-03-19");
        assertEquals(sdf.parse("2014-03-19").getTime(), parsed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueStringWithPatternInvalid() {
        DateMapper mapper = new DateMapper("field", null, null, PATTERN);
        mapper.base("test", "2014/03/19");
    }

    @Test
    public void testValueStringWithoutPattern() throws ParseException {
        DateMapper mapper = new DateMapper("field", null, null, null);
        long parsed = mapper.base("test", "2014/03/19 00:00:00.000");
        assertEquals(sdf.parse("2014-03-19").getTime(), parsed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueStringWithoutPatternInvalid() throws ParseException {
        DateMapper mapper = new DateMapper("field", null, null, null);
        mapper.base("test", "2014-03-19");
    }

    @Test
    public void testIndexedField() throws ParseException {
        long time = sdf.parse("2014-03-19").getTime();
        DateMapper mapper = new DateMapper("field", true, null, PATTERN);
        Field field = mapper.indexedField("name", time);
        assertNotNull(field);
        assertEquals(time, field.numericValue().longValue());
        assertEquals("name", field.name());
        assertEquals(false, field.fieldType().stored());
    }

    @Test
    public void testSortedField() throws ParseException {
        long time = sdf.parse("2014-03-19").getTime();
        DateMapper mapper = new DateMapper("field", null, true, PATTERN);
        Field field = mapper.sortedField("name", time, false);
        assertNotNull(field);
        assertEquals(DocValuesType.NUMERIC, field.fieldType().docValuesType());
    }

    @Test
    public void testSortedFieldCollection() throws ParseException {
        long time = sdf.parse("2014-03-19").getTime();
        DateMapper mapper = new DateMapper("field", null, true, PATTERN);
        Field field = mapper.sortedField("name", time, true);
        assertNotNull(field);
        assertEquals(DocValuesType.NUMERIC, field.fieldType().docValuesType());
    }

    @Test
    public void testExtractAnalyzers() {
        DateMapper mapper = new DateMapper("field", null, null, PATTERN);
        String analyzer = mapper.getAnalyzer();
        assertEquals(Mapper.KEYWORD_ANALYZER, analyzer);
    }

    @Test
    public void testParseJSONWithoutArgs() throws IOException {
        String json = "{fields:{age:{type:\"date\"}}}";
        Schema schema = Schema.fromJson(json);
        Mapper mapper = schema.getMapper("age");
        assertNotNull(mapper);
        assertEquals(DateMapper.class, mapper.getClass());
        assertEquals(Mapper.DEFAULT_INDEXED, mapper.isIndexed());
        assertEquals(Mapper.DEFAULT_SORTED, mapper.isSorted());
        assertEquals(DateMapper.DEFAULT_PATTERN, ((DateMapper) mapper).getPattern());
    }

    @Test
    public void testParseJSONWithAllArgs() throws IOException {
        String json = "{fields:{age:{type:\"date\", indexed:\"false\", sorted:\"true\"," +
                      " pattern:\"" + PATTERN + "\"}}}";
        Schema schema = Schema.fromJson(json);
        Mapper mapper = schema.getMapper("age");
        assertNotNull(mapper);
        assertEquals(DateMapper.class, mapper.getClass());
        assertFalse(mapper.isIndexed());
        assertTrue(mapper.isSorted());
        assertEquals(PATTERN, ((DateMapper) mapper).getPattern());
    }

    @Test
    public void testParseJSONEmpty() throws IOException {
        String json = "{fields:{}}";
        Schema schema = Schema.fromJson(json);
        Mapper mapper = schema.getMapper("age");
        assertNull(mapper);
    }

    @Test(expected = IOException.class)
    public void testParseJSONInvalid() throws IOException {
        String json = "{fields:{age:{}}";
        Schema.fromJson(json);
    }

    @Test
    public void testToString() {
        DateMapper mapper = new DateMapper("field", false, false, PATTERN);
        assertEquals("DateMapper{indexed=false, sorted=false, pattern=yyyy-MM-dd}", mapper.toString());
    }
}
