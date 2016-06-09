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

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import net.menthor.core.traits.MClassifier
import net.menthor.core.traits.MContainedElement

/**
 * Menthor CORE Generalization. A generic generalization
 * @author John Guerson
 */

@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
class MGeneralization implements MContainedElement {

    protected MClassifier general
    protected MClassifier specific
    protected MGeneralizationSet generalizationSet

    //=============================
    // Getters
    //=============================

    MClassifier getGeneral() { return general }

    MClassifier getSpecific() { return specific }

    @JsonIgnore
    MGeneralizationSet getGeneralizationSet() { return generalizationSet }

    @JsonIgnore
    boolean isAmongClasses(){ return MClass.isInstance(general) && MClass.isInstance(specific) }

    @JsonIgnore
    boolean isAmongDataTypes(){ return MDataType.isInstance(general) && MDataType.isInstance(specific) }

    //=============================
    // Setters were overwritten to ensure
    // opposite ends in the metamodel
    //=============================

    void setGeneralizationSet(MGeneralizationSet gs) {
        generalizationSet = gs
        if(gs==null) return
        //Ensuring the opposite end
        if(!gs.generalizations.contains(this)){
            gs.generalizations.add(this)
        }
    }

    void setGeneral(MClassifier general){
        this.general = general
        if(general==null) return
        //Ensuring the opposite end
        if(!general.getIsGeneralIn().contains(this)){
            general.getIsGeneralIn().add(this)
        }
    }

    void setSpecific(MClassifier specific){
        this.specific = specific
        if(specific == null) return
        //Ensuring the opposite end
        if(!specific.getIsSpecificIn().contains(this)){
            specific.getIsSpecificIn().add(this)
        }
    }
}
