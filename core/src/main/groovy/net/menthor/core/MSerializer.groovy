package net.menthor.core

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

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper

import groovy.json.JsonOutput
import net.menthor.core.traits.MElement

/**
 * Menthor CORE Serializer. A generic serializer to JSON and vice-versa.
 * @author John Guerson
 */

class MSerializer implements MElement {

    ObjectMapper mapper = new ObjectMapper()

    MSerializer() {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    MPackage fromJSONFile(File file){
        def jsonStr = readToString(file)
        return fromJSONString(jsonStr)
    }

    MPackage fromJSONFile(String path){
        def jsonStr = readToString(path)
        return fromJSONString(jsonStr)
    }

    MPackage fromJSONString(String json) {
        MPackage actual = (MPackage) mapper.readValue(json, MPackage.class);
        return actual
    }

    String toJSONString(GroovyObject obj) {
        return mapper.writeValueAsString(obj);
    }

    String toFormattedJSONString(GroovyObject obj){
        def jsonStr = toJSONString(obj)
        return JsonOutput.prettyPrint(jsonStr)
    }

    File toJSONFile(GroovyObject obj, String directory, String fileName) {
        def content = toJSONString(obj)
        writeToFile(content, directory, fileName, ".json")
    }

    File toFormattedJSONFile(GroovyObject obj, File file) {
        def content = toFormattedJSONString(obj)
        writeToFile(content, file)
    }

    File toFormattedJSONFile(GroovyObject obj, String directory, String fileName) {
        def content = toFormattedJSONString(obj)
        writeToFile(content, directory, fileName, ".json")
    }

    File writeToFile(String content, String directory, String fileName, String extension) {
        return new File("$directory/$fileName$extension").withWriter { out ->
            out.print content
        }
    }

    File writeToFile(String content, File file) {
        return file.withWriter { out ->
            out.print content
        }
    }

    String readToString(String abspath){
        return new File(abspath).getText('UTF-8')
    }

    String readToString(File file){
        return file.getText('UTF-8')
    }
}