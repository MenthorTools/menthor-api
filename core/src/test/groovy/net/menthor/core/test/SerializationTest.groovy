package net.menthor.core.test

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
import net.menthor.core.MSerializer

/**
 *  Generates a model example and serialize it to a JSON file.
 *  Then, it reads that example generated as a JSON file and serialize it again with another file name.
 *  It then checks if the two files are the same to make sure the serialization is correct.
 *
 *  @author John Guerson
 */

class SerializationTest {

    static void main(String[] args){

        def directory = "core/src/test/groovy/net/menthor/core/test/json/"
        def jsonGen = "SerializationGen"
        def jsonRead = "SerializationRead"

        MSerializer s = new MSerializer()

        //carAccidentExample example
        MPackage m = CarAccidentExample.generate()
        s.toFormattedJSONFile(m,directory,jsonGen)

        //read the example from file and write it again
        MPackage m2 = s.fromJSONFile(directory+jsonGen+".json")
        s.toFormattedJSONFile(m2,directory, jsonRead)

        //compare the two files
        String genContent = s.readToString(directory+jsonGen+".json")
        String readContent = s.readToString(directory+jsonRead+".json")
        if(genContent.equals(readContent)){
            println "SUCCESS: Serialization Test passed"
        }else{
            println "ERROR: Serialization Test failed. The two files are not identical"
        }
    }
}