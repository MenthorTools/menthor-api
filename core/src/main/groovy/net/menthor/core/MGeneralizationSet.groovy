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
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import net.menthor.core.traits.MClassifier
import net.menthor.core.traits.MContainedElement
import net.menthor.core.traits.MNamedElement

/**
 * Menthor CORE GeneralizationSet. A generic generalization set
 * @author John Guerson
 */

@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
class MGeneralizationSet implements MContainedElement, MNamedElement {

    protected boolean covering
    protected boolean disjoint
    protected List<MGeneralization> generalizations = []

    //=============================
    //Getters
    //=============================

    boolean isCovering() { return covering }

    boolean isDisjoint() { return disjoint }

    List<MGeneralization> getGeneralizations() { return generalizations }

    //=============================
    // Setters were overwritten to ensure
    // opposite ends in the metamodel
    //=============================

    void setIsCovering(boolean isCovering) { this.covering = isCovering }

    void setIsDisjoint(boolean isDisjoint) { this.disjoint = isDisjoint }

    void setGeneralization(MGeneralization g){
        if(g==null) return
        if(!generalizations.contains(g)){
            generalizations.add(g)
        }
        //Ensure the opposite end
        g.setGeneralizationSet(this)
    }

    void setGeneralizations(List<MGeneralization> gens){
        if(gens==null||gens==[]){
            this.generalizations.clear()
            return
        }
        gens.each { g ->
            setGeneralization(g)
        }
    }

    //=============================
    // General and Specifics
    //=============================

    MClassifier general(){
        if(generalizations.size()>0) { return generalizations.get(0).getGeneral(); }
        else { return null; }
    }

    List<MClassifier> specifics(){
        def result = []
        generalizations.each{ g ->
            result.add(g.specific)
        }
        return result
    }

    List<MClassifier> leafSpecifics(){
        def result = []
        specifics().each{ s ->
            result.addAll(s.allLeafChildren())
        }
        return result
    }

    //=============================
    // Default name
    //=============================

    void setDefaultName(){
        def name = "gs"
        if (general() != null) name += "-"+general().getUniqueName()
        if (specifics().size()>0) name += "-"+specifics().get(0).getUniqueName()
        name = name.trim().toLowerCase().replace(" ","-")
        setName(name)
    }
}
