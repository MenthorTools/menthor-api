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
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import net.menthor.core.traits.MClassifier
import net.menthor.core.traits.MNamedElement
import net.menthor.core.MPackage
import net.menthor.ontouml.stereotypes.ClassStereotype
import net.menthor.ontouml.stereotypes.ConstraintStereotype
import net.menthor.ontouml.stereotypes.DataTypeStereotype
import net.menthor.ontouml.stereotypes.RelationshipStereotype

/**
 * OntoUML package.
 * @author John Guerson
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
class OntoUMLPackage extends MPackage {

    List<OntoUMLClass> allNominalQualities(){
        return allClasses().findAll { (it as OntoUMLClass).isNominalQuality() }
    }

    List<OntoUMLClass> allTopLevelWholes(){
        def result = []
        def classes = allElements(OntoUMLClass.class)
        if(classes==null || classes.size()==0) return result
        classes.each{ e ->
            if((e as OntoUMLClass).wholes().isEmpty()) result.add(e)
        }
        return result
    }

    //============================
    //Factory utilities direct from a package
    //============================

    OntoUMLPackage createPackage(String name) {
        return OntoUMLFactory.createPackage(name,this)
    }

    OntoUMLGeneralization createGeneralization(MClassifier source, MClassifier target){
        return OntoUMLFactory.createGeneralization(source, target, this)
    }

    OntoUMLGeneralizationSet createGeneralizationSet(String name, boolean isCovering, boolean isDisjoint, List generalizations){
        return OntoUMLFactory.createGeneralizationSet(name, isCovering,isDisjoint,generalizations,this)
    }

    OntoUMLGeneralizationSet createGeneralizationSet(String name, boolean isCovering, boolean isDisjoint){
        return OntoUMLFactory.createGeneralizationSet(name, isCovering,isDisjoint,this)
    }

    OntoUMLGeneralizationSet createPartition(String name, List<MClassifier> specifics, MClassifier general) {
        return OntoUMLFactory.createPartition(name, specifics,general,this)
    }

    OntoUMLClass createClass(ClassStereotype c, String name){
        return OntoUMLFactory.createClass(c, name,this)
    }

    OntoUMLDataType createDataType(DataTypeStereotype c, String name){
        return OntoUMLFactory.createDataType(c, name,this)
    }

    OntoUMLClass createKind(String name){
        return OntoUMLFactory.createClass(ClassStereotype.KIND,name,this)
    }

    OntoUMLClass createCollective(String name){
        return OntoUMLFactory.createClass(ClassStereotype.COLLECTIVE,name,this)
    }

    OntoUMLClass createQuantity(String name){
        return OntoUMLFactory.createClass(ClassStereotype.QUANTITY,name,this)
    }

    OntoUMLClass createSubKind(String name){
        return OntoUMLFactory.createClass(ClassStereotype.SUBKIND,name,this)
    }

    OntoUMLClass createPhase(String name){
        return OntoUMLFactory.createClass(ClassStereotype.PHASE,name,this)
    }

    OntoUMLClass createRole(String name){
        return OntoUMLFactory.createClass(ClassStereotype.ROLE,name,this)
    }

    OntoUMLClass createMixin(String name){
        return OntoUMLFactory.createClass(ClassStereotype.MIXIN,name,this)
    }

    OntoUMLClass createCategory(String name){
        return OntoUMLFactory.createClass(ClassStereotype.CATEGORY,name,this)
    }

    OntoUMLClass createRoleMixin(String name){
        return OntoUMLFactory.createClass(ClassStereotype.ROLEMIXIN,name,this)
    }

    OntoUMLClass createPhaseMixin(String name){
        return OntoUMLFactory.createClass(ClassStereotype.PHASEMIXIN,name,this)
    }

    OntoUMLClass createMode(String name){
        return OntoUMLFactory.createClass(ClassStereotype.MODE,name,this)
    }

    OntoUMLClass createRelator(String name){
        return OntoUMLFactory.createClass(ClassStereotype.RELATOR,name,this)
    }

    OntoUMLClass createQuality(String name){
        return OntoUMLFactory.createClass(ClassStereotype.QUALITY,name,this)
    }

    OntoUMLClass createEvent(String name){
        return OntoUMLFactory.createClass(ClassStereotype.EVENT,name,this)
    }

    OntoUMLClass createHighorder(String name){
        return OntoUMLFactory.createClass(ClassStereotype.HIGHORDER,name,this)
    }

    OntoUMLRelationship createRelationship(RelationshipStereotype stereo, MClassifier source, MClassifier target){
        return OntoUMLFactory.createRelationship(stereo, source, target, this)
    }
    OntoUMLRelationship createDerivation(MClassifier source, MClassifier target){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.DERIVATION, source, target, this)
    }

    OntoUMLRelationship createTemporal(MClassifier source, MClassifier target){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.TEMPORAL, source, target, this)
    }

    OntoUMLRelationship createStructuration(MClassifier source, MClassifier target){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.STRUCTURATION, source, target, this)
    }

    OntoUMLRelationship createCausation(MClassifier source, MClassifier target){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.CAUSATION, source, target, this)
    }

    OntoUMLRelationship createCharacterization(MClassifier source, MClassifier target){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.CHARACTERIZATION, source, target, this)
    }

    OntoUMLRelationship createComponentOf(MClassifier source, MClassifier target){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.COMPONENTOF, source, target, this)
    }

    OntoUMLRelationship createConsitution(MClassifier source, MClassifier target){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.CONSTITUTION, source, target, this)
    }

    OntoUMLRelationship createFormal(MClassifier source, MClassifier target){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.FORMAL, source, target, this)
    }

    OntoUMLRelationship createInstanceOf(MClassifier source, MClassifier target){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.INSTANCEOF, source, target, this)
    }

    OntoUMLRelationship createMaterial(MClassifier source, MClassifier target){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.MATERIAL, source, target, this)
    }

    OntoUMLRelationship createMediation(MClassifier source, MClassifier target){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.MEDIATION, source, target, this)
    }

    OntoUMLRelationship createMemberOf(MClassifier source, MClassifier target){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.MEMBEROF, source, target, this)
    }

    OntoUMLRelationship createParticipation(MClassifier source, MClassifier target){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.PARTICIPATION, source, target, this)
    }

    OntoUMLRelationship createQuaPartOf(MClassifier source, MClassifier target){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.QUAPARTOF, source, target, this)
    }

    OntoUMLRelationship createSubCollectionOf(MClassifier source, MClassifier target){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.SUBCOLLECTIONOF, source, target, this)
    }

    OntoUMLRelationship createSubEventOf(MClassifier source, MClassifier target){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.SUBEVENTOF, source, target, this)
    }

    OntoUMLRelationship createSubQuantityOf(MClassifier source, MClassifier target){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.SUBQUANTITYOF, source, target, this)
    }

    OntoUMLRelationship createRelationship(RelationshipStereotype stereo, MClassifier source, int sourceLower, int sourceUpper, String name,
                                           MClassifier target, int targetLower, int targetUpper){
        return OntoUMLFactory.createRelationship(stereo, source, sourceLower, sourceUpper, name, target, targetLower, targetUpper, this)
    }

    OntoUMLRelationship createMediation(MClassifier source, int sourceLower, int sourceUpper, String name, MClassifier target, int targetLower, int targetUpper){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.MEDIATION, source, sourceLower, sourceUpper, name, target, targetLower, targetUpper, this)
    }

    OntoUMLRelationship createMaterial(MClassifier source, int sourceLower, int sourceUpper, String name, MClassifier target, int targetLower, int targetUpper){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.MATERIAL, source, sourceLower, sourceUpper, name, target, targetLower, targetUpper, this)
    }

    OntoUMLRelationship createFormal(MClassifier source, int sourceLower, int sourceUpper, String name, MClassifier target, int targetLower, int targetUpper){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.FORMAL, source, sourceLower, sourceUpper, name, target, targetLower, targetUpper, this)
    }

    OntoUMLRelationship createCharacterization(MClassifier source, int sourceLower, int sourceUpper, String name, MClassifier target, int targetLower, int targetUpper){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.CHARACTERIZATION, source, sourceLower, sourceUpper, name, target, targetLower, targetUpper, this)
    }

    OntoUMLRelationship createCausation(MClassifier source, int sourceLower, int sourceUpper, String name, MClassifier target, int targetLower, int targetUpper){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.CAUSATION, source, sourceLower, sourceUpper, name, target, targetLower, targetUpper, this)
    }

    OntoUMLRelationship createComponentOf(MClassifier source, int sourceLower, int sourceUpper, String name, MClassifier target, int targetLower, int targetUpper){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.COMPONENTOF, source, sourceLower, sourceUpper, name, target, targetLower, targetUpper, this)
    }

    OntoUMLRelationship createConstitution(MClassifier source, int sourceLower, int sourceUpper, String name, MClassifier target, int targetLower, int targetUpper){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.CONSTITUTION, source, sourceLower, sourceUpper, name, target, targetLower, targetUpper, this)
    }

    OntoUMLRelationship createDerivation(MClassifier source, int sourceLower, int sourceUpper, String name, MClassifier target, int targetLower, int targetUpper){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.DERIVATION, source, sourceLower, sourceUpper, name, target, targetLower, targetUpper, this)
    }

    OntoUMLRelationship createInstanceOf(MClassifier source, int sourceLower, int sourceUpper, String name, MClassifier target, int targetLower, int targetUpper){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.INSTANCEOF, source, sourceLower, sourceUpper, name, target, targetLower, targetUpper, this)
    }

    OntoUMLRelationship createMemberOf(MClassifier source, int sourceLower, int sourceUpper, String name, MClassifier target, int targetLower, int targetUpper){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.MEMBEROF, source, sourceLower, sourceUpper, name, target, targetLower, targetUpper, this)
    }

    OntoUMLRelationship createParticipation(MClassifier source, int sourceLower, int sourceUpper, String name, MClassifier target, int targetLower, int targetUpper){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.PARTICIPATION, source, sourceLower, sourceUpper, name, target, targetLower, targetUpper, this)
    }

    OntoUMLRelationship createQuaPartOf(MClassifier source, int sourceLower, int sourceUpper, String name, MClassifier target, int targetLower, int targetUpper){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.QUAPARTOF, source, sourceLower, sourceUpper, name, target, targetLower, targetUpper, this)
    }

    OntoUMLRelationship createStructuration(MClassifier source, int sourceLower, int sourceUpper, String name, MClassifier target, int targetLower, int targetUpper){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.STRUCTURATION, source, sourceLower, sourceUpper, name, target, targetLower, targetUpper, this)
    }

    OntoUMLRelationship createSubCollectionOf(MClassifier source, int sourceLower, int sourceUpper, String name, MClassifier target, int targetLower, int targetUpper){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.SUBCOLLECTIONOF, source, sourceLower, sourceUpper, name, target, targetLower, targetUpper, this)
    }

    OntoUMLRelationship createSubEventOf(MClassifier source, int sourceLower, int sourceUpper, String name, MClassifier target, int targetLower, int targetUpper){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.SUBEVENTOF, source, sourceLower, sourceUpper, name, target, targetLower, targetUpper, this)
    }

    OntoUMLRelationship createSubQuantityOf(MClassifier source, int sourceLower, int sourceUpper, String name, MClassifier target, int targetLower, int targetUpper){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.SUBQUANTITYOF, source, sourceLower, sourceUpper, name, target, targetLower, targetUpper, this)
    }

    OntoUMLRelationship createTemporal(MClassifier source, int sourceLower, int sourceUpper, String name, MClassifier target, int targetLower, int targetUpper){
        return OntoUMLFactory.createRelationship(RelationshipStereotype.TEMPORAL, source, sourceLower, sourceUpper, name, target, targetLower, targetUpper, this)
    }

    OntoUMLConstraint createConstraint(MNamedElement context, ConstraintStereotype stereo, String identifier, String expression){
        return OntoUMLFactory.createConstraint(context, stereo, identifier, expression, this)
    }

    String toString() { OntoUMLPrinter.print(this) }
}
