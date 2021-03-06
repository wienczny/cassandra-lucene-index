/*
 * Copyright 2015, Stratio.
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
package com.stratio.cassandra.lucene.query.builder;

import com.stratio.cassandra.lucene.query.Condition;
import com.stratio.cassandra.lucene.query.FuzzyCondition;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Andres de la Pena <adelapena@stratio.com>
 */
public class FuzzyConditionBuilderTest {

    @Test
    public void testBuild() {
        FuzzyConditionBuilder builder = new FuzzyConditionBuilder("field", "value");
        builder.boost(0.7f);
        builder.maxEdits(2);
        builder.prefixLength(2);
        builder.maxExpansions(49);
        builder.transpositions(true);
        FuzzyCondition condition = builder.build();
        assertNotNull(condition);
        assertEquals(0.7f, condition.boost, 0);
        assertEquals("field", condition.field);
        assertEquals("value", condition.value);
        assertEquals(2, condition.maxEdits);
        assertEquals(2, condition.prefixLength);
        assertEquals(49, condition.maxExpansions);
        assertEquals(true, condition.transpositions);
    }

    @Test
    public void testBuildDefaults() {
        FuzzyConditionBuilder builder = new FuzzyConditionBuilder("field", "value");
        FuzzyCondition condition = builder.build();
        assertNotNull(condition);
        assertEquals(Condition.DEFAULT_BOOST, condition.boost, 0);
        assertEquals("field", condition.field);
        assertEquals("value", condition.value);
        assertEquals(FuzzyCondition.DEFAULT_MAX_EDITS, condition.maxEdits);
        assertEquals(FuzzyCondition.DEFAULT_PREFIX_LENGTH, condition.prefixLength);
        assertEquals(FuzzyCondition.DEFAULT_MAX_EXPANSIONS, condition.maxExpansions);
        assertEquals(FuzzyCondition.DEFAULT_TRANSPOSITIONS, condition.transpositions);
    }
}
