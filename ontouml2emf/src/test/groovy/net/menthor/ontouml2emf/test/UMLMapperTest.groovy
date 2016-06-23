package net.menthor.ontouml2emf.test

import net.menthor.ontouml.OntoUMLModel
import net.menthor.ontouml.OntoUMLSerializer
import net.menthor.ontouml2emf.uml.UmlMapper

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

class UmlMapperTest {

    static void main(String[] args){
        toUMLTest()
        fromUMLTest()
    }

    static void fromUMLTest(){

    }

    static void toUMLTest(){
        println "======== UML: ECG TEST ===================="
        File input = new File("ontouml2emf/src/test/resources/ecg.json")
        File output = new File("ontouml2emf/src/test/resources/ecg.uml")
        OntoUMLSerializer s = new OntoUMLSerializer()
        OntoUMLModel ontomodel = s.fromJSONFile(input)
        def mapper = new UmlMapper()
        def umlmodel = mapper.toUML(ontomodel)
        mapper.serialize(umlmodel, output.getAbsolutePath())
        println "[FINISHED]."
        println "==========================================="

        println ""

        println "======== UML: FOOTBALL TEST ==============="
        input = new File("ontouml2emf/src/test/resources/football.json")
        output = new File("ontouml2emf/src/test/resources/football.uml")
        s = new OntoUMLSerializer()
        ontomodel = s.fromJSONFile(input)
        mapper = new UmlMapper()
        umlmodel = mapper.toUML(ontomodel)
        mapper.serialize(umlmodel, output.getAbsolutePath())
        println "[FINISHED]."
        println "==========================================="

        println ""

        println "======== UML: BIO-ENTITY TEST ============="
        input = new File("ontouml2emf/src/test/resources/bio-entity.json")
        output = new File("ontouml2emf/src/test/resources/bio-entity.uml")
        s = new OntoUMLSerializer()
        ontomodel = s.fromJSONFile(input)
        mapper = new UmlMapper()
        umlmodel = mapper.toUML(ontomodel)
        mapper.serialize(umlmodel, output.getAbsolutePath())
        println "[FINISHED]."
        println "==========================================="

        println ""

        println "======== UML: UFO-S TEST =================="
        input = new File("ontouml2emf/src/test/resources/ufo-s.json")
        output = new File("ontouml2emf/src/test/resources/ufo-s.uml")
        s = new OntoUMLSerializer()
        ontomodel = s.fromJSONFile(input)
        mapper = new UmlMapper()
        umlmodel = mapper.toUML(ontomodel)
        mapper.serialize(umlmodel, output.getAbsolutePath())
        println "[FINISHED]."
        println "=========================================="
    }
}
