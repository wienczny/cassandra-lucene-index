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

import com.stratio.cassandra.lucene.query.MatchCondition;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Andres de la Pena <adelapena@stratio.com>
 */
public class MatchConditionBuilderTest {

    @Test
    public void testBuildString() {
        MatchConditionBuilder builder = new MatchConditionBuilder("field", "value");
        MatchCondition condition = builder.build();
        assertNotNull(condition);
        assertEquals("field", condition.field);
        assertEquals("value", condition.value);
    }

    @Test
    public void testBuildNumber() {
        MatchConditionBuilder builder = new MatchConditionBuilder("field", 3);
        MatchCondition condition = builder.build();
        assertNotNull(condition);
        assertEquals("field", condition.field);
        assertEquals(3, condition.value);
    }
}
