package net.menthor.ontouml2emf.refontouml

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

import net.menthor.ontouml.OntoUMLModel

/**
 * @author John Guerson
 */

class RefontoumlMapper {

    private FromRefontoumlMapper sourceMapper = new FromRefontoumlMapper()
    private ToRefontoumlMapper targetMapper = new ToRefontoumlMapper()

    RefontoumlLog log = new RefontoumlLog()

    String getLogText(){ log.getText() }

    //from

    OntoUMLModel fromRefOntoUML(RefOntoUML.Package refmodel){
        def result = sourceMapper.run(refmodel)
        log = sourceMapper.getLog()
        return result
    }

    OntoUMLModel fromRefOntoUML(RefOntoUML.Package refmodel, RefontoumlOptions options){
        def result = sourceMapper.run(refmodel, options)
        log = sourceMapper.getLog()
        return result
    }

    //to

    RefOntoUML.Package toRefOntoUML(OntoUMLModel m){
        def result = targetMapper.run(m)
        return result
    }
    RefOntoUML.Package toRefOntoUML(OntoUMLModel m, RefontoumlOptions options){
        def result = targetMapper.run(m, options)
        return result;
    }

    String asXMLString(RefOntoUML.Package model){
        return RefontoumlUtil.asXMLString(model)
    }

    void serialize(RefOntoUML.Package model, String path){
        RefontoumlUtil.serialize(model,path)
    }
    RefOntoUML.Package deserialize(InputStream is){
       return RefontoumlUtil.deserialize(is)
    }
    RefOntoUML.Package deserialize(String path){
        return RefontoumlUtil.deserialize(path)
    }
}
