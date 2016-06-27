package net.menthor.ontouml2emf.test

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
import net.menthor.ontouml.OntoUMLSerializer
import net.menthor.ontouml2emf.refontouml.RefontoumlMapper
import net.menthor.ontouml2emf.refontouml.RefontoumlOptions

/**
 * @author John Guerson
 */
class RefontoumlMapperTest {

    static void main(String[] args) {
        //toRefOntoTest()
        fromRefOntoTest()
    }

    static void fromRefOntoTest(){
        println "======== REFONTOUML: ECG TEST ===="
        File input = new File("ontouml2emf/src/test/resources/refontouml/ecg.refontouml")
        OntoUMLSerializer s = new OntoUMLSerializer()
        def mapper = new RefontoumlMapper()
        def refmodel = mapper.deserialize(input.getAbsolutePath())
        def options = new RefontoumlOptions()
        options.setAssumeClassAsEvent(true)
        def ontomodel = mapper.fromRefOntoUML(refmodel, options)
        s.toFormattedJSONFile(ontomodel, "ontouml2emf/src/test/resources/refontouml/","ecg")
        println "[FINISHED]."
        println "==========================================="
    }

    static void toRefOntoTest(){
        println "======== REFONTOUML: CAR ACCIDENT TEST ===="
        File input = new File("ontouml2emf/src/test/resources/json/car-accident.json")
        File output = new File("ontouml2emf/src/test/resources/refontouml/car-accident.refontouml")
        OntoUMLSerializer s = new OntoUMLSerializer()
        OntoUMLModel ontomodel = s.fromJSONFile(input)
        def mapper = new RefontoumlMapper()
        def model = mapper.toRefOntoUML(ontomodel)
        mapper.serialize(model, output.getAbsolutePath())
        println "[FINISHED]."
        println "==========================================="

        println ""

        println "======== REFONTOUML: ECG TEST ============="
        input = new File("ontouml2emf/src/test/resources/json/ecg.json")
        output = new File("ontouml2emf/src/test/resources/refontouml/ecg.refontouml")
        s = new OntoUMLSerializer()
        ontomodel = s.fromJSONFile(input)
        mapper = new RefontoumlMapper()
        model = mapper.toRefOntoUML(ontomodel)
        mapper.serialize(model, output.getAbsolutePath())
        println "[FINISHED]."
        println "==========================================="

        println ""

        println "======== REFONTOUML: FOOTBALL TEST ========"
        input = new File("ontouml2emf/src/test/resources/json/football.json")
        output = new File("ontouml2emf/src/test/resources/refontouml/football.refontouml")
        s = new OntoUMLSerializer()
        ontomodel = s.fromJSONFile(input)
        mapper = new RefontoumlMapper()
        model = mapper.toRefOntoUML(ontomodel)
        mapper.serialize(model, output.getAbsolutePath())
        println "[FINISHED]."
        println "==========================================="

        println ""

        println "======== REFONTOUML: BIO-ENTITY TEST ======"
        input = new File("ontouml2emf/src/test/resources/json/bio-entity.json")
        output = new File("ontouml2emf/src/test/resources/refontouml/bio-entity.refontouml")
        s = new OntoUMLSerializer()
        ontomodel = s.fromJSONFile(input)
        mapper = new RefontoumlMapper()
        model = mapper.toRefOntoUML(ontomodel)
        mapper.serialize(model, output.getAbsolutePath())
        println "[FINISHED]."
        println "==========================================="

        println ""

        println "======== REFONTOUML: UFO-S TEST ==========="
        input = new File("ontouml2emf/src/test/resources/json/ufo-s.json")
        output = new File("ontouml2emf/src/test/resources/refontouml/ufo-s.refontouml")
        s = new OntoUMLSerializer()
        ontomodel = s.fromJSONFile(input)
        mapper = new RefontoumlMapper()
        model = mapper.toRefOntoUML(ontomodel)
        mapper.serialize(model, output.getAbsolutePath())
        println "[FINISHED]."
        println "=========================================="

    }
}
