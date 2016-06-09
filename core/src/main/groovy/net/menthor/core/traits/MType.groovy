package net.menthor.core.traits

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
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import net.menthor.core.MAttribute

/**
 * Menthor CORE Type. Usually classes and datatypes.
 * @author John Guerson
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
trait MType implements MClassifier {

    protected List<MAttribute> attributes = []

    //=============================
    // Getters
    //=============================

    List<MAttribute> getAttributes() { return attributes }

    //=============================
    // Setters were overwritten to ensure
    // opposite ends in the metamodel
    //=============================

    void setAttribute(MAttribute attr){
        if(attr==null) return
        if(!attributes.contains(attr)){
            attributes.add(attr)
        }
        //Ensure the opposite end
        attr.setOwner(this)
    }

    void setAttributes(List<MAttribute> attributes){
        if(attributes==null || attributes==[]){
            this.attributes.clear()
            return
        }
        attributes.each{ a ->
            setAttribute(a)
        }
    }

    //=============================
    // Opposite Types
    //=============================

    /** Returns all choice directly connected to this type through a relationship. */
    List<MType> oppositeTypes(){
        def result = []
        oppositeEndPoints().each{ ep ->
            def t = ep.getClassifier();
            if(t!=null){
                result.add(t as MType)
            }
        }
        return result;
    }

    /**Returns all choice directly and indirectly connected to this type through a relationship. */
    List<MType> allOppositeTypes(){
        def result = []
        allOppositeEndPoints().each { ep ->
            def t = ep.getClassifier();
            if(t!=null){
                result.add(t as MType)
            }
        }
        return result;
    }
}