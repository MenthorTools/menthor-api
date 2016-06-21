package net.menthor.ea2ontouml

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

import groovy.util.slurpersupport.GPathResult

import net.menthor.core.traits.MClassifier
import net.menthor.ontouml.OntoUMLFactory
import net.menthor.ontouml.OntoUMLPackage
import net.menthor.ontouml.OntoUMLRelationship
import net.menthor.ontouml.stereotypes.PrimitiveStereotype

/**
 * Transforms a EA's xmi file to OntoUML
 *
 * @author John Guerson
 */

class EAMapper implements EAVisitor {

    EAOptions option = new EAOptions()

    EAMapper(){}

    Object run(File file, EAOptions opt){
        this.option = opt
        run(file)
    }

    Object run(InputStream stream, EAOptions opt){
        this.option = opt
        run(stream)
    }

    Object run(InputStream stream){
        return visit(stream)
    }

    Object run(File file){
        return visit(file)
        //printResult()
    }

    @Override
    Object processEAModel(GPathResult eaModel){
        def name = eaModel.@name.text()
        def ontoModel = OntoUMLFactory.createModel(name)
        return ontoModel
    }

    @Override
    Object processEAPackage(GPathResult eaPack, GPathResult eaParentPack) {
        def ontoContainer = packagesMap.get(eaParentPack)
        def name = eaPack.@name.text()
        def ontoPackage = OntoUMLFactory.createPackage(name, ontoContainer)
        return ontoPackage
    }

    @Override
    Object processEAClass(GPathResult eaClass, GPathResult eaParentPack) {
        def name = eaClass.@name.text()
        if (option.ignoreUnnamedTypes && (name == null || name.isEmpty())) {
            println "[IGNORED] CLASS: no @name was found {" + eaClass.'@xmi:id'.text() + "}."
            return null
        } else {
            def ontoContainer = packagesMap.get(eaParentPack)
            def ontoClass = OntoUMLFactory.createClass(null, name, ontoContainer)
            return ontoClass
        }
    }

    @Override
    Object processEAAssociationClass(GPathResult eaClass, GPathResult eaParentPack) {
        def name = eaClass.@name.text()
        if (option.ignoreUnnamedTypes && (name == null || name.isEmpty())) {
            println "[IGNORED] ASSOCIATION CLASS: no @name was found {" + eaClass.'@xmi:id'.text() + "}."
            return null
        } else {
            def ontoContainer = packagesMap.get(eaParentPack)
            def ontoClass = OntoUMLFactory.createClass(null, name, ontoContainer)
            return ontoClass
        }
    }

    @Override
    Object processEADataType(GPathResult eaDataType, GPathResult eaParentPack) {
        def name = eaDataType.@name.text()
        if (option.ignoreUnnamedTypes && (name == null || name.isEmpty())) {
            println "[IGNORED] DATA TYPE: no @name was found {" + eaDataType.'@xmi:id'.text() + "}."
            return null
        } else {
            def ontoContainer = packagesMap.get(eaParentPack)
            def ontoDataType = OntoUMLFactory.createDataType(null, name, ontoContainer)
            return ontoDataType
        }
    }

    @Override
    Object processEAEnumeration(GPathResult eaEnum, GPathResult eaParentPack) {
        def name = eaEnum.@name.text()
        if (option.ignoreUnnamedTypes && (name == null || name.isEmpty())) {
            println "[IGNORED] ENUMERATION: no @name was found {" + eaEnum.'@xmi:id'.text() + "}."
            return null
        } else {
            def ontoContainer = packagesMap.get(eaParentPack)
            def ontoEnum = OntoUMLFactory.createEnumeration(name, null, ontoContainer)
            return ontoEnum
        }
    }

    @Override
    Object processEAAssociation(GPathResult eaRel, GPathResult eaParentPack) {
        def ontoContainer = packagesMap.get(eaParentPack)
        def name = eaRel.@name.text()
        def ontoRelationship = OntoUMLFactory.createRelationship(null, name, ontoContainer)
        return ontoRelationship
    }

    @Override
    Object processEAGeneralization(GPathResult eaGen, GPathResult eaSpecificType) {
        def generalId = eaGen.@general.text()
        Object ontoGeneral = getClassifierById(generalId)
        Object ontoSpecific = classifierMap.get(eaSpecificType)
        if (ontoGeneral == null) {
            println "[IGNORED] GENERALIZATION: general classifier is missing {" + eaGen.'@xmi:id'.text() + "}."
            return null
        } else if(ontoSpecific==null){
            println "[IGNORED] GENERALIZATION: specific classifier is missing {" + eaGen.'@xmi:id'.text() + "}."
            return null
        }else {
            Object ontoContainer = (ontoSpecific as MClassifier).getContainer()
            def ontoGeneralization = OntoUMLFactory.createGeneralization(ontoSpecific, ontoGeneral, ontoContainer)
            return ontoGeneralization
        }
    }

    @Override
    Object processEAGeneralizationSet(GPathResult eaGenSet, GPathResult eaParentPack) {
        def ontoGens = []
        eaGenSet.generalization.each{ g ->
            def gen = getGeneralizationById(g.'@xmi:idref'.text())
            if(gen!=null) ontoGens.add(gen)
        }
        def name = eaGenSet.@name.text()
        def ontoContainer = packagesMap.get(eaParentPack)
        def isDisjoint = eaGenSet.@isDisjoint.text().toBoolean()
        def isCovering = eaGenSet.@isCovering.text().toBoolean()
        def ontoGenSet = OntoUMLFactory.createGeneralizationSet(name,isCovering, isDisjoint, ontoGens, ontoContainer)
        return ontoGenSet
    }

    @Override
    Object processEAEndPoint(GPathResult eaProp) {
        def ontoClassifier = getClassifierById(eaProp.type.'@xmi:idref'.text())
        if(ontoClassifier==null) {
            println "[IGNORED] END POINT: end-classifier is missing {"+eaProp.'@xmi:id'.text()+"}."
            return null
        }else {
            OntoUMLRelationship ontoRel = getAssociationById(eaProp.@association.text())
            def id = eaProp.'@xmi:id'.text()
            def isSource = id.contains("EAID_src")
            def ep = ontoRel.sourceEndPoint()
            if(!isSource) ep = ontoRel.targetEndPoint()
            ep.setClassifier(ontoClassifier)
            def name = eaProp.@name.text()
            ep.setName(name)
            if(name==null || name.isEmpty()) ep.setDefaultName()
            def lower = processEALowerValue(eaProp)
            ep.setLowerBound(lower)
            def upper = processEAUpperValue(eaProp)
            ep.setUpperBound(upper)
            return ep
        }
    }

    @Override
    Object processEAAttribute(GPathResult eaAttr, GPathResult ownerType) {
        def owner = typesMap.get(ownerType)
        if(owner==null){
            println "[IGNORED] ATTRIBUTE: owner type is missing {"+ownerType.'@xmi:id'.text()+"}"
            return null
        }else {
            def id = eaAttr.type.'@xmi:idref'.text()
            Object type = getTypeById(id)
            def lower = processEALowerValue(eaAttr)
            def upper = processEAUpperValue(eaAttr)
            def name = eaAttr.@name.text()
            if (type == null) { // it's a primitive type
                def primitiveStereotype = getPrimitiveById(id)
                def ontoAttribute = OntoUMLFactory.createAttribute(owner, primitiveStereotype, lower, upper, name, false, false)
                return ontoAttribute
            } else { // not a primitive type, therefore, a structuration
                def ontoPackage = (owner.getContainer() as OntoUMLPackage)
                def ontoStructuration = ontoPackage.createStructuration(owner, 0, -1, name, type, lower, upper)
                return ontoStructuration
            }
        }
    }

    int processEAUpperValue(GPathResult eaProp){
        def upper = 0
        try{
            upper = eaProp.upperValue.@value.text().toInteger()
        }catch(NumberFormatException e){
            println "[ERROR] UPPER VALUE: "+e.getLocalizedMessage()+" at {" + eaProp.'@xmi:id'.text() + "}. Value 1 was set by default."
            upper = 1
        }
        if(option.set1ForNullMultiplicities && upper==null) {
            println "[ERROR] UPPER VALUE: no @upperValue was found at {" + eaProp.'@xmi:id'.text() + "}. Value 1 was set by default."
            upper = 1
        }
        return upper
    }

    int processEALowerValue(GPathResult eaProp){
        def lower = 0
        try {
            lower = eaProp.lowerValue.@value.text().toInteger()
        }catch(NumberFormatException e){
            println "[ERROR] LOWER VALUE: "+e.getLocalizedMessage()+" at {" + eaProp.'@xmi:id'.text() + "}. Value 1 was set by default."
            lower = 1
        }
        if(option.set1ForNullMultiplicities && lower==null) {
            println "[ERROR] LOWER VALUE: no @lowerValue was found at {" + eaProp.'@xmi:id'.text() + "}. Value 1 was set by default."
            lower = 1
        }
        return lower
    }

    @Override
    Object processEAPrimitive(GPathResult eaPrimitive) {
        if(eaPrimitive.'@name'.text().compareToIgnoreCase('String')==0) return PrimitiveStereotype.STRING
        if(eaPrimitive.'@name'.text().compareToIgnoreCase('int')==0) return PrimitiveStereotype.INTEGER
        if(eaPrimitive.'@name'.text().compareToIgnoreCase('float')==0) return PrimitiveStereotype.REAL
        if(eaPrimitive.'@name'.text().compareToIgnoreCase('Date')==0) return PrimitiveStereotype.DATE
        if(eaPrimitive.'@name'.text().compareToIgnoreCase('Double')==0) return PrimitiveStereotype.REAL
        if(eaPrimitive.'@name'.text().compareToIgnoreCase('char')==0) return PrimitiveStereotype.STRING
        if(eaPrimitive.'@name'.text().compareToIgnoreCase('boolean')==0) return PrimitiveStereotype.BOOLEAN
        println "[IGNORED] PRIMITIVE: no '"+eaPrimitive.'@name'.text()+"' primitive stereotype was found."
    }

    /** End visit:  Correlates EA stereotypes & values at last */
    void endEAModelVisit(){
        EAProfileCorrelator pc = new EAProfileCorrelator(this)
        //classes and association classes
        pc.setupClassStereotypes()
        pc.setupClassValues()
        //datatypes and enumerations
        pc.setupDataTypeStereotypes()
        pc.setupDataTypeValues()
        //associations
        pc.setupAssociationStereotypes()
        pc.setupAssociationValues()
    }
}
