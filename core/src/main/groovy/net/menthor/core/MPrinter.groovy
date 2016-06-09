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

import net.menthor.core.traits.MContainedElement
import net.menthor.core.traits.MElement
import net.menthor.core.traits.MNamedElement
import net.menthor.core.traits.MProperty

/**
 * Menthor CORE Printer. A generic printer that writes an element string representation.
 * @author John Guerson
 */

class MPrinter implements MElement {

    protected MPrinter() {}

    static String print(MElement elem){
        if(elem instanceof MGeneralization){
            MGeneralization gen = (elem as MGeneralization)
            return type(elem)+ " {"+gen.getSpecific().getName()+" -> "+gen.getGeneral().getName()+"}"
        }
        if(elem instanceof MRelationship){
            MRelationship rel = elem as MRelationship
            return type(elem)+ " ("+name(elem)+") {"+rel.source().getName()+" -> "+rel.target().getName()+"}"
        }
        return type(elem)+" "+name(elem)
    }

    //======================================
    //Type
    //======================================

    static String type(MElement elem){
        return elem.getClass().getName().replace("net.menthor.mcore.","")
    }

    //======================================
    //Name
    //======================================

    static String name(MElement elem){
        if(elem==null) return "null"
        if(elem instanceof MNamedElement){
            String name = (elem as MNamedElement).getName()
            if(name == null) return ""
            return name
        }
        return " nameless"
    }

    static String name(MElement elem, boolean addSingleQuote, boolean addLowerUpper){
        if(addSingleQuote) return "'"+name(elem)+"'"
        if(addLowerUpper) return "<"+name(elem)+">"
        return name(elem)
    }

    //======================================
    //Multiplicity
    //======================================

    static String multiplicity(MProperty p, boolean alwaysShowLowerAndUpper, String separator){
        if(p==null) return "null"
        Integer lower = p.getLowerBound()
        Integer upper = p.getUpperBound()
        return multiplicity(lower, upper, alwaysShowLowerAndUpper, separator)
    }

    static String multiplicity(Integer lower, Integer upper, boolean alwaysShowLowerAndUpper, String separator) {
        String lowerString = lower.toString()
        String upperString = upper.toString()
        if (lower == -1) lowerString = "*"
        if (upper == -1) upperString = "*"
        if(!alwaysShowLowerAndUpper && lower==upper) return lowerString
        return lowerString+separator+upperString
    }

    //======================================
    //Path
    //======================================

    static String path(MContainedElement c){
        if(c == null) return ""
        if (c.getContainer()==null) return ""
        else{
            String containerName = ""
            if(c.getContainer() instanceof MNamedElement) containerName = ((MNamedElement) c.getContainer()).getName()
            else containerName = "unnamed"
            if(!c.getContainer() instanceof MContainedElement) return containerName
            return path(c.getContainer())+"::"+containerName
        }
    }
}
