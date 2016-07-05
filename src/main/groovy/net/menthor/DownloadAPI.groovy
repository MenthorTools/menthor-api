package net.menthor

import net.menthor.ontouml.OntoUMLSerializer
import net.menthor.ontouml2emf.ecore.EcoreMapper
import net.menthor.ontouml2emf.refontouml.RefontoumlMapper
import net.menthor.ontouml2emf.uml.UmlMapper
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import sun.misc.IOUtils

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

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
class DownloadAPI {

    @RequestMapping(value = '/api/download/json', method = RequestMethod.GET)
    public @ResponseBody def downloadJson(){
        OntoUMLSerializer s = new OntoUMLSerializer()
        return s.toFormattedJSONString(UploadAPI.ontology)
    }

    @RequestMapping(value = '/api/download/refontouml', method = RequestMethod.GET)
    public @ResponseBody def downloadRefontouml(HttpServletResponse response){
        RefontoumlMapper mapper = new RefontoumlMapper()
        def refmodel = mapper.toRefOntoUML(UploadAPI.ontology)
        byte[] documentBody = mapper.asXMLString(refmodel).getBytes();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("text","xml"));
        header.setContentLength(documentBody.length);
        return new HttpEntity<byte[]>(documentBody, header);
    }

    @RequestMapping(value = '/api/download/ecore', method = RequestMethod.GET)
    public @ResponseBody def downloadEcore (HttpServletResponse response){
        EcoreMapper mapper = new EcoreMapper()
        def ecoremodel = mapper.toEcore(UploadAPI.ontology)
        byte[] documentBody = mapper.asXMLString(ecoremodel).getBytes();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("text","xml"));
        header.setContentLength(documentBody.length);
        return new HttpEntity<byte[]>(documentBody, header);
    }

    @RequestMapping(value = '/api/download/uml', method = RequestMethod.GET)
    public @ResponseBody def downloadUml (HttpServletResponse response){
        UmlMapper mapper = new UmlMapper()
        def umlmodel = mapper.toUML(UploadAPI.ontology)
        byte[] documentBody = mapper.asXMLString(umlmodel).getBytes();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("text","xml"));
        header.setContentLength(documentBody.length);
        return new HttpEntity<byte[]>(documentBody, header);
    }
}
