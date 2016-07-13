package net.menthor

import net.menthor.ea2ontouml.EAMapper
import net.menthor.ontouml.OntoUMLModel
import net.menthor.ontouml.OntoUMLSerializer
import net.menthor.ontouml2emf.refontouml.RefontoumlMapper
import net.menthor.ontouml2emf.refontouml.RefontoumlOptions
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

import javax.servlet.http.HttpServletRequest

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
class UploadAPI {

    static OntoUMLModel ontology;

    @RequestMapping(value = '/api/upload/json', method = RequestMethod.POST)
    public @ResponseBody def uploadJson(HttpServletRequest request){
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request
        MultipartFile file = multipartRequest.getFile("file")
        if(file!=null) {
            InputStream inputStream = file.getInputStream()
            String content = inputStream.getText()
            OntoUMLSerializer s = new OntoUMLSerializer()
            ontology = s.fromJSONString(content)
            return ontology;
        }
        return "[INFO] Server did not receive JSON file in the HTTP request"
    }

    @RequestMapping(value = '/api/upload/ea', method = RequestMethod.POST)
    public @ResponseBody def uploadEa(HttpServletRequest request){
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request
        MultipartFile file = multipartRequest.getFile("file")
        if(file!=null) {
            InputStream inputStream = file.getInputStream()
            EAMapper m = new EAMapper()
            ontology = m.run(inputStream)
            return m.getLogText();
        }
        return "[INFO] Server did not receive XML file in the HTTP request"
    }

    @RequestMapping(value = '/api/upload/refontouml', method = RequestMethod.POST)
    public @ResponseBody def uploadRefontouml(HttpServletRequest request){
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request
        MultipartFile file = multipartRequest.getFile("file")
        def ignorePackage = multipartRequest.getParameter("ignore-package").toBoolean()
        def classesAsEvents = multipartRequest.getParameter("classes-as-events").toBoolean()
        if(file!=null) {
            InputStream inputStream = file.getInputStream()
            def opt = new RefontoumlOptions()
            opt.setAssumeClassAsEvent(classesAsEvents)
            opt.setIgnorePackages(ignorePackage)
            RefontoumlMapper m = new RefontoumlMapper()
            def refmodel = m.deserialize(inputStream)
            ontology = m.fromRefOntoUML(refmodel,opt)
            return m.getLogText()
        }
        return "[INFO] Server did not receive RefOntoUML file in the HTTP request"
    }
 }

