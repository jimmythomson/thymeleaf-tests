/*
 * =============================================================================
 * 
 *   Copyright (c) 2011-2012, The THYMELEAF team (http://www.thymeleaf.org)
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
package org.thymeleaf.engine21.stsm;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.testing.templateengine.context.web.SpringWebProcessingContextBuilder;
import org.thymeleaf.testing.templateengine.engine.TestExecutor;
import org.thymeleaf.tests.util.SpringSpecificVersionUtils;


public class STSM21Test {
    
    
    public STSM21Test() {
        super();
    }
    
    
    
    
    @Test
    public void testSTSM() throws Exception {

        final SpringWebProcessingContextBuilder contextBuilder = new SpringWebProcessingContextBuilder();
        contextBuilder.setApplicationContextConfigLocation("classpath:engine21/stsm/applicationContext.xml");

        final TestExecutor executor = new TestExecutor();
        executor.setProcessingContextBuilder(contextBuilder);
        executor.setDialects(Arrays.asList(new IDialect[] { SpringSpecificVersionUtils.createSpringStandardDialectInstance()}));
        executor.execute("classpath:engine21/stsm");
        
        Assert.assertTrue(executor.isAllOK());
        
        
    }
    
    
    
}
