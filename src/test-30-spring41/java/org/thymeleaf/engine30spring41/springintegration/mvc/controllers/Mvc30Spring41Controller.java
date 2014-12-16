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
package org.thymeleaf.engine30spring41.springintegration.mvc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class Mvc30Spring41Controller {


    public Mvc30Spring41Controller() {
        super();
    }
    
    

    @RequestMapping("/something")
    public String someController() {
        return "something";
    }


    @RequestMapping("/somethingArgs")
    public String someControllerWithArgs(@RequestParam String argOne) {
        return "somethingArgs";
    }

    @RequestMapping("/somethingOtherArgs/{argTwo}")
    public String someControllerWithOtherArgs(@RequestParam String argOne, @PathVariable final String argTwo, @RequestParam final String argThree) {
        return "somethingArgs";
    }


}
