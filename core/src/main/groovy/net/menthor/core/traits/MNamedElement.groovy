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
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import net.menthor.core.MConstraint
import net.menthor.core.util.UniqueNameProcessor

/**
 * Menthor CORE Named Element. Metamodel element wwhich has a name
 * @author John Guerson
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
trait MNamedElement implements MElement {

    protected List<MConstraint> isContextIn
    protected String name
    protected String uniqueName
    protected List<String> definitions = []
    protected List<String> synonyms = []
    protected String text

    //=============================
    // Getters
    //=============================

    @JsonIgnore
    List<MConstraint> getIsContextIn(){ return isContextIn }

    String getName() { return name }

    String getUniqueName() { return uniqueName }

    List<String> getDefinitions() { return definitions }

    List<String> getSynonyms(){ return synonyms }

    String getText(){ return text }

    //=============================
    // Setters
    //=============================

    void setName(String name){
        if(uniqueName!=null) UniqueNameProcessor.remove(uniqueName)
        if(name==null || name.isEmpty()){
            int idx = getClass().getName().lastIndexOf(".")
            this.uniqueName = UniqueNameProcessor.process(getClass().getName().substring(idx))
        }else{
            this.uniqueName = UniqueNameProcessor.process(name)
            this.name = name
        }
    }

    void setUniqueName(String uniqueName){
        if(uniqueName!=null) this.uniqueName = uniqueName
        else this.uniqueName = ""
    }

    void setText(String text){
        if(text!=null) this.text = text
        else this.text = ""
    }

    void setDefinitions(List<String> definitions){
        this.definitions = definitions
    }

    void setSynonyms(List<String> synonyms){
        this.synonyms = synonyms
    }

    void setIsContextIn(MConstraint c){
        if(c==null) return
        if(!isContextIn.contains(c)){
            isContextIn.add(c)
        }
        //Ensuring opposite end
        c.setContext(this)
    }

    void setIsContextIn(List<MConstraint> constraints){
        if(constraints==null || constraints ==[]) {
            isContextIn.clear()
            return
        }
        constraints.each{ c ->
            setIsContextIn(c)
        }
    }
}
