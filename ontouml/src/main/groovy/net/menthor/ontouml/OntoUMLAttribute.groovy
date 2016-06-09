package net.menthor.ontouml

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

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import net.menthor.core.MAttribute
import net.menthor.ontouml.stereotypes.PrimitiveStereotype

/**
 * OntoUML attribute.
 * @author John Guerson
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
class OntoUMLAttribute extends MAttribute {

    protected PrimitiveStereotype stereotype

    //=============================
    // Getters
    //=============================

    PrimitiveStereotype getStereotype() { return stereotype }

    //=============================
    // Setters
    //=============================

    void setStereotype(PrimitiveStereotype stereotype){
        this.stereotype = stereotype
    }

    //=============================
    // Stereotype Checking
    //=============================

    @JsonIgnore
    boolean isInteger() {
        return stereotype==PrimitiveStereotype.INTEGER;
    }
    @JsonIgnore
    boolean isBoolean() {
        return stereotype==PrimitiveStereotype.BOOLEAN;
    }
    @JsonIgnore
    boolean isReal() {
        return stereotype==PrimitiveStereotype.REAL;
    }
    @JsonIgnore
    boolean isDate() {
        return stereotype==PrimitiveStereotype.DATE;
    }
    @JsonIgnore
    boolean isDateTime() {
        return stereotype==PrimitiveStereotype.DATE_TIME;
    }
    @JsonIgnore
    boolean isString() {
        return stereotype==PrimitiveStereotype.STRING;
    }

    String toString() { OntoUMLPrinter.print(this) }
}