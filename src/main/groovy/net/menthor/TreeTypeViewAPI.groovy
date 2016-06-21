package net.menthor

import net.menthor.core.traits.MClassifier
import net.menthor.core.traits.MType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016  Menthor Consulting in Information Technology
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

/**
  * @author John Guerson
 */
@RestController
class TreeTypeViewAPI {

    def processed = []

    @RequestMapping(value = '/api/tree/type-hierarchy', method = RequestMethod.GET)
    public @ResponseBody def typeHierarchyView(){
        processed.clear()
        def result = "["
        def toplevels = UploadAPI.ontology.allTopLevelTypes()
        result += getJSONForTypes(toplevels)
        toplevels.each { toplevel ->
            def children = toplevel.allChildren()
            children.each { c ->
                def jsonParents = getJSONForAllParents(c);
                if(!jsonParents.isEmpty()) result += ","+getJSONForAllParents(c)
                if(!processed.contains(c)) {
                    def jsonChild = getJSONForType(c)
                    if(!jsonChild.isEmpty()) result += ","+jsonChild;
                }
            }
        }
        result += "]"
        return result
    }

    private String getJSONForAllParents(MClassifier c){
        def result = ""
        def parents = c.allParents().reverse();
        parents.eachWithIndex { p, k ->
            if(!processed.contains(p)) {
                result += getJSONForType(p)
                if (k < parents.size() - 1) result += ", "
            }
        }
        return result
    }

    private String getJSONForType(MClassifier t){
        def result = ""
        def list = t.parents()
        if(list!=null && list.size()>0) {
            list.eachWithIndex { p, idx ->
                result += "{\"id\" : \"" + t.getUniqueName() + "\", \"parent\" : \"" + (p as MClassifier).getUniqueName() + "\", \"text\" : \"" + t.toString() + "\", \"type\": \"type\" }"
                if (idx<list.size()-1) result+=", "
            }
        } else{
            result += "{\"id\" : \"" + t.getUniqueName() + "\", \"parent\" : \"" + "#" + "\", \"text\" : \"" + t.toString() + "\", \"type\": \"type\" }"
        }
        processed.add(t)
        return result;
    }

    private String getJSONForTypes(List types){
        def result = ""
        types.eachWithIndex{ t, idx ->
            result += getJSONForType(t)
            if(idx<types.size()-1) result+=", "
        }
        return result
    }
}
