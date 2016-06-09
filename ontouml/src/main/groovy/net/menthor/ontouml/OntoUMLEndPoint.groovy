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
import net.menthor.core.MEndPoint

/**
 * OntoUML end-point.
 * @author John Guerson
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
class OntoUMLEndPoint extends MEndPoint {

    protected List<OntoUMLEndPoint> subsets = []
    protected List<OntoUMLEndPoint> redefines = []
    protected List<OntoUMLEndPoint> subsettedBy = []
    protected List<OntoUMLEndPoint> redefinedBy = []

    //=============================
    // Getters
    //=============================

    List<OntoUMLEndPoint> getSubsets() { return subsets }

    List<OntoUMLEndPoint> getRedefines() { return redefines }

    @JsonIgnore
    List<OntoUMLEndPoint> getSubsettedBy() { return subsettedBy }

    @JsonIgnore
    List<OntoUMLEndPoint> getRedefinedBy() { return redefinedBy }

    //=============================
    // Setters were overwritten to ensure
    // opposite ends in the metamodel
    //=============================

    void setSubset(OntoUMLEndPoint superEp){
        if(superEp==null) return
        if(!subsets.contains(superEp)){
            subsets.add(superEp)
        }
        if(!superEp.subsettedBy.contains(this)){
            superEp.subsettedBy.add(this)
        }
    }

    void setSubsets(List<OntoUMLEndPoint> superEps){
        if(superEps==null || superEps==[]){
            this.subsets.clear()
            return
        }
        superEps.each{ ep ->
            setSubset(ep)
        }
    }

    void setSubsetted(OntoUMLEndPoint subEp){
        if(subEp==null) return
        if(!subsettedBy.contains(subEp)){
            subsettedBy.add(subEp)
        }
        if(!subEp.subsets.contains(this)){
            subEp.subsets.add(this)
        }
    }

    void setSubsettedBy(List<OntoUMLEndPoint> subEps){
        if(subEps==null || subEps==[]){
            this.subsets.clear()
            return
        }
        subEps.each{ ep ->
            setSubsetted(ep)
        }
    }

    void setRedefine(OntoUMLEndPoint superEp){
        if(superEp==null) return
        if(!redefines.contains(superEp)){
            redefines.add(superEp)
        }
        if(!superEp.redefinedBy.contains(this)){
            superEp.redefinedBy.add(this)
        }
    }

    void setRedefines(List<OntoUMLEndPoint> superEps){
        if(superEps==null || superEps==[]){
            this.redefines.clear()
            return
        }
        superEps.each{ ep ->
            setRedefine(ep)
        }
    }

    void setRedefined(OntoUMLEndPoint subEp){
        if(subEp==null) return
        if(!redefinedBy.contains(subEp)){
            redefinedBy.add(subEp)
        }
        if(!subEp.redefines.contains(this)){
            subEp.redefines.add(this)
        }
    }

    void setRedefinedBy(List<OntoUMLEndPoint> subEps){
        if(subEps==null || subEps==[]){
            this.redefinedBy.clear()
            return
        }
        subEps.each{ ep ->
            setRedefinedBy(ep)
        }
    }

    String toString() { OntoUMLPrinter.print(this) }
}
