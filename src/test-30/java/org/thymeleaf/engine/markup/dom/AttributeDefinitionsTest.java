/*
 * =============================================================================
 * 
 *   Copyright (c) 2011-2014, The THYMELEAF team (http://www.thymeleaf.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * =============================================================================
 */
package org.thymeleaf.engine.markup.dom;

import org.junit.Assert;
import org.junit.Test;


public final class AttributeDefinitionsTest {



    @Test
    public void test() {

        final int standardSize = AttributeDefinitions.ALL_STANDARD_ATTRIBUTES.size();
        Assert.assertEquals(standardSize, AttributeDefinitions.ALL_STANDARD_ATTRIBUTE_NAMES.size());

        for (final String name : AttributeDefinitions.ALL_STANDARD_ATTRIBUTE_NAMES) {
            final AttributeDefinition def1 = AttributeDefinitions.forName(name);
            final AttributeDefinition def2 = AttributeDefinitions.forName(name);
            final AttributeDefinition def3 = AttributeDefinitions.forName(name.toUpperCase());
            Assert.assertSame(def1, def2);
            Assert.assertSame(def2, def3);
        }

        final AttributeDefinition new1 = AttributeDefinitions.forName("NEW");
        Assert.assertNotNull(new1);
        Assert.assertEquals("new", new1.getNormalizedName());
        final AttributeDefinition new2 = AttributeDefinitions.forName("new");
        Assert.assertSame(new1, new2);
        final AttributeDefinition new3 = AttributeDefinitions.forName("NeW");
        Assert.assertSame(new1, new3);

        Assert.assertEquals(standardSize, AttributeDefinitions.ALL_STANDARD_ATTRIBUTES.size());
        Assert.assertEquals(standardSize, AttributeDefinitions.ALL_STANDARD_ATTRIBUTE_NAMES.size());
        Assert.assertFalse(AttributeDefinitions.ALL_STANDARD_ATTRIBUTE_NAMES.contains("new"));
        Assert.assertFalse(AttributeDefinitions.ALL_STANDARD_ATTRIBUTES.contains(new1));

    }



    
}
