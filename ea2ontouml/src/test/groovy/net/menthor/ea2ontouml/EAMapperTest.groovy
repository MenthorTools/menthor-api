package net.menthor.ea2ontouml

import net.menthor.ontouml.OntoUMLSerializer

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

class EAMapperTest {

    public static void main(String[] args){

        println "======== EA: BIO ENTITY TEST ============="
        File input = new File("ea2ontouml/src/test/resources/bio-entity.xml")
        File output = new File("ea2ontouml/src/test/resources/bio-entity.json")
        def mapper = new EAMapper()
        def ontoumlModel = mapper.run(input)
        new OntoUMLSerializer().toFormattedJSONFile(ontoumlModel,output)
        println "[FINISHED]."
        println "=========================================="

        println ""

        println "======== EA: FOOTBALL TEST ==============="
        input = new File("ea2ontouml/src/test/resources/football.xml")
        output = new File("ea2ontouml/src/test/resources/football.json")
        mapper = new EAMapper()
        ontoumlModel = mapper.run(input)
        new OntoUMLSerializer().toFormattedJSONFile(ontoumlModel,output)
        println "[FINISHED]."
        println "=========================================="

        println ""

        println "======== EA: ECG TEST ===================="
        input = new File("ea2ontouml/src/test/resources/ecg.xml")
        output = new File("ea2ontouml/src/test/resources/ecg.json")
        mapper = new EAMapper()
        ontoumlModel = mapper.run(input)
        new OntoUMLSerializer().toFormattedJSONFile(ontoumlModel,output)
        println "[FINISHED]."
        println "=========================================="

        println ""

        println "======== EA: UFO-S TEST =================="
        input = new File("ea2ontouml/src/test/resources/ufo-s.xml")
        output = new File("ea2ontouml/src/test/resources/ufo-s.json")
        mapper = new EAMapper()
        ontoumlModel = mapper.run(input)
        new OntoUMLSerializer().toFormattedJSONFile(ontoumlModel,output)
        println "[FINISHED]."
        println "=========================================="
    }
}
