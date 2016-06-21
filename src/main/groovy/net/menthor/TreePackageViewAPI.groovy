package net.menthor

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

import net.menthor.core.MPackage
import net.menthor.core.traits.MType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

/**
 * @author John Guerson
 */
@RestController
class TreePackageViewAPI {

    @RequestMapping(value = '/api/tree/package-hierarchy', method = RequestMethod.GET)
    public @ResponseBody def packageHierarchyView(){
        def result = "["+getJSON(UploadAPI.ontology)
        UploadAPI.ontology.allPackages().each{ p -> result += ","+getJSON(p) }
        UploadAPI.ontology.allTypes().each { t -> result += "," + getJSON(t); }
        result += "]"
        return result
    }

    private def getJSON(MPackage p){
        def parentId = ((p.getContainer() == null) ? "#" : p.getContainer().getUniqueName())
        return "{ \"id\" : \""+p.getUniqueName()+"\", \"parent\" : \""+parentId+"\", \"text\" : \""+p.toString()+"\", \"type\": \"package\" }"
    }

    private def getJSON(MType  t){
        def parentId = ((t.getContainer() == null) ? "#" : t.getContainer().getUniqueName())
        return "{ \"id\" : \""+t.getUniqueName()+"\", \"parent\" : \""+parentId+"\", \"text\" : \""+t.toString()+"\", \"type\": \"type\" }"
    }
}
