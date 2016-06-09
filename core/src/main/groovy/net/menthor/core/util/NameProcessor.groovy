package net.menthor.core.util

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
 * Menthor CORE Name Processor. Removes invalid characters of a name mantaining a word VS. counter map.
 * @author John Guerson
 */
class NameProcessor {

    private static Map<String, Counter > freqMap = [:]

    private static def invalidChars = [
        '[^\\p{ASCII}]',' ',',','!','@','#','\\$','%','\"','\'','&','\\*','-','=','\\+',';',':','-','<','>',
        '\\?','\\.','\\{','\\}','\\(','\\)','\\[','\\]','\\\\','/','\\|'
    ] as String []

    static String removeInvalidChars(String name){
        def result = name
        invalidChars.each{ regex ->
            result = result.replaceAll(regex,"")
        }
        return result
    }

    private static def add(String name){
        Counter c = freqMap.get(name);
        if (c == null) {
            c = new Counter()
            freqMap.put(name, c)
        } else {
            c.increment();
        }
        return c.count()
    }

    static def remove(String name){
        Counter c = freqMap.get(name)
        if(c!=null){
            c.decrease()
            if(c.count()<=0) freqMap.remove(name)
        }
    }

    static def process(String name){
        name = removeInvalidChars(name)
        def count = add(name)
        if(count==1) return name
        else return name+count
    }
}

