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
import net.menthor.core.MClass
import net.menthor.core.MComment
import net.menthor.core.MConstraint
import net.menthor.core.MDataType
import net.menthor.core.MEndPoint
import net.menthor.core.MFactory
import net.menthor.core.MGeneralization
import net.menthor.core.MGeneralizationSet
import net.menthor.core.MPackage
import net.menthor.core.MRelationship

/**
 * Menthor CORE Container. It contains other elements such as a Package does.
 * @author John Guerson
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
trait MContainer implements MNamedElement {

    protected List<MContainedElement> elements = []

    //=============================
    // Getters
    //=============================

    List<MContainedElement> getElements() { return elements }

    //=============================
    // Setters were overwritten to ensure
    // opposite ends in the metamodel
    //=============================

    void setElement(MContainedElement ce){
        if(ce == null) return
        if(!elements.contains(ce)){
            elements.add(ce)
        }
        //Ensure the opposite end
        ce.setContainer(this)
    }

    void setElements(List<MContainedElement> elements){
       if(elements==null || elements == []){
           this.elements.clear()
           return
       }
       elements.each{ e ->
           setElement(e)
       }
    }

    //=============================
    // Contained MElement
    //=============================

    /* Search for the elements of a particular type in this container. */
    List<MContainedElement> elements(java.lang.Class type){
        List result = []
        getElements().each{ e ->
            if(type.isInstance(e)) result.add(e)
        }
        return result
    }

    /** Search in depth for all elements of a particular type in this container */
    List<MContainedElement> allElements(java.lang.Class type){
        def result = []
        allElements(this, type, result)
        return result
    }

    /** Search in depth for all elements of a particular type in this container. */
    List<MContainedElement> allElements(MContainer c, java.lang.Class type, List result){
        c.getElements().each{ e->
            if(type.isInstance(e)) {
                result.add(e)
            }
            if(e instanceof MContainer) allElements(e, type, result)
        }
    }

    //=============================
    // Package
    //=============================

    /* Packages of this container. */
    List<MPackage> packages() {
       return elements(MPackage.class)
    }

     /** All packages of this container (searching in depth) */
     List<MPackage> allPackages(){
        return allElements(MPackage.class)
     }

    //=============================
    // Relationship
    //=============================

    /* Relationships of this container. */
    List<MRelationship> relationships() {
        return elements(MRelationship.class)
    }

    /** All relationships of this container (searching in depth) */
    List<MRelationship> allRelationships(){
        return allElements(MRelationship.class)
    }

    //=============================
    // Classes
    //=============================

    /* Classes of this container. */
    List<MClass> classes() {
        return elements(MClass.class)
    }

    /** All classes of this container (searching in depth) */
    List<MClass> allClasses(){
        return allElements(MClass.class)
    }

    //=============================
    // DataTypes
    //=============================

    /* DataTypes of this container. */
    List<MDataType> dataTypes() {
        return elements(MDataType.class)
    }

    /** All datatypes of this container (searching in depth) */
    List<MDataType> allDataTypes(){
        return allElements(MDataType.class)
    }

    //=============================
    // Generalization
    //=============================

    /* Generalizations of this container. */
    List<MGeneralization> generalizations() {
        return elements(MGeneralization.class)
    }

    /** All generalizations of this container (searching in depth) */
    List<MGeneralization> allGeneralizations(){
        return allElements(MGeneralization.class)
    }

    //=============================
    // Generalization Set
    //=============================

    /* Generalization Sets of this container. */
    List<MGeneralizationSet> generalizationSets() {
        return elements(MGeneralizationSet.class)
    }

    /** All generalization Sets of this container (searching in depth) */
    List<MGeneralizationSet> allGeneralizationSets(){
        return allElements(MGeneralizationSet.class)
    }

    //=============================
    // Constraint
    //=============================

    /* Constraints of this container. */
    List<MConstraint> constraints() {
        return elements(MConstraint.class)
    }

    /** All constraints of this container (searching in depth) */
    List<MConstraint> allConstraints(){
        return allElements(MConstraint.class)
    }

    //=============================
    // MType
    //=============================

    /* Types of this container. */
    List<MType> types() {
        return elements(MType.class)
    }

    /** All choice of this container (searching in depth) */
    List<MType> allTypes(){
        return allElements(MType.class)
    }

    //=============================
    // MClassifier
    //=============================

    /* Classifiers of this container. */
    List<MClassifier> classifiers() {
        return elements(MClassifier.class)
    }

    /** All classifiers of this container (searching in depth) */
    List<MClassifier> allClassifiers(){
        return allElements(MClassifier.class)
    }

    //=============================
    // Comment
    //=============================

    List<MComment> comments(){
        List<MComment> result = []
        elements(MContainedElement.class).each{ elem ->
            (elem as MContainedElement).getComments().each{ ontoCom ->
                result.add(ontoCom)
            }
        }
        return result
    }

    List<MComment> allComments(){
        List<MComment> result = []
        allElements(MContainedElement.class).each{ elem ->
            (elem as MContainedElement).getComments().each{ ontoCom ->
                result.add(ontoCom)
            }
        }
        return result
    }

    //=============================
    // Attribute
    //=============================

     List<MAttribute> attributes(){
        List<MAttribute> result = []
        elements(MType.class).each{ elem ->
            (elem as MType).getAttributes().each{ ontoAttr ->
                result.add(ontoAttr)
            }
        }
        return result
    }

    List<MAttribute> allAttributes(){
        List<MAttribute> result = []
        allElements(MType.class).each{ elem ->
            (elem as MType).getAttributes().each{ ontoAttr ->
                result.add(ontoAttr)
            }
        }
        return result
    }

    //=============================
    // EndPoints
    //=============================

    List<MEndPoint> endPoints(){
        List<MEndPoint> endpoints= []
        getElements().each { elem ->
            if (elem instanceof MRelationship){
                (elem as MRelationship).getEndPoints().each{ endpoint ->
                    endpoints.add(endpoint)
                }
            }
        }
        return endpoints
    }

    //=============================
    // Creation
    //=============================

    MPackage createPackage(String name){
        return MFactory.createPackage(name,this)
    }

    MClass createClass(String name){
        return MFactory.createClass(name,this)
    }

    MDataType createDataType(String name){
        return MFactory.createDataType(name,this)
    }

    MDataType createDataType(String name, List<String> textValues){
        return MFactory.createDataType(name, textValues)
    }

    MRelationship createRelationship(MClassifier source, MClassifier target){
        return MFactory.createRelationship(source, target, this)
    }

    MRelationship createRelationship(MClassifier source, int srcLower, int srcUpper, String name, MClassifier target, int tgtLower, int tgtUpper){
        return MFactory.createRelationship(source, srcLower, srcUpper, name, target, tgtLower, tgtUpper, this)
    }

    MGeneralization createGeneralization(MClassifier source, MClassifier target){
        return MFactory.createGeneralization(source, target, this)
    }

    MGeneralizationSet createGeneralizationSet(boolean isCovering, boolean isDisj, List<MGeneralization> gens){
        return MFactory.createGeneralizationSet(isCovering, isDisj, gens, this)
    }

    MGeneralizationSet createPartition(List<MClassifier> specifics, MClassifier general){
        return MFactory.createPartition(specifics, general, this)
    }

    MConstraint createConstraint(MNamedElement context, String name, String condition){
        return MFactory.createConstraint(context, name, condition, this)
    }
}
