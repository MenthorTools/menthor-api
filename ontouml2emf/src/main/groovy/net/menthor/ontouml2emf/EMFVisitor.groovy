package net.menthor.ontouml2emf

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

import net.menthor.ontouml.OntoUMLAttribute
import net.menthor.ontouml.OntoUMLClass
import net.menthor.ontouml.OntoUMLDataType
import net.menthor.ontouml.OntoUMLPackage
import net.menthor.ontouml.OntoUMLEndPoint
import net.menthor.ontouml.OntoUMLGeneralization
import net.menthor.ontouml.OntoUMLGeneralizationSet
import net.menthor.ontouml.OntoUMLModel
import net.menthor.ontouml.OntoUMLRelationship
import net.menthor.core.traits.MContainer

trait EMFVisitor {

    OntoUMLModel ontomodel
    Map <Object, MContainer> srcPackagesMap = [:]
    Map <Object, OntoUMLClass> srcClassMap = [:]
    Map <Object, OntoUMLDataType> srcDataTypeMap = [:]
    Map <Object, OntoUMLRelationship> srcRelationshipsMap= [:]
    Map <Object, OntoUMLAttribute> srcAttributesMap= [:]
    Map <Object, OntoUMLEndPoint> srcEndpointsMap= [:]
    Map <Object, OntoUMLGeneralization> srcGensMap= [:]
    Map <Object, OntoUMLGeneralizationSet> srcGenSetsMap= [:]

    String accessMethod = "eContents"

    java.lang.Class packageClass
    java.lang.Class typeClass
    java.lang.Class classClass
    java.lang.Class dataTypeClass
    java.lang.Class relationshipClass
    java.lang.Class genClass
    java.lang.Class propertyClass
    java.lang.Class genSetClass

    void setup(java.lang.Class packageClass, java.lang.Class typeClass, java.lang.Class classClass, java.lang.Class dataTypeClass,
    java.lang.Class relationshipClass, java.lang.Class genClass, java.lang.Class propertyClass, java.lang.Class genSetClass){
        this.packageClass=packageClass
        this.typeClass=typeClass
        this.classClass=classClass
        this.dataTypeClass=dataTypeClass
        this.relationshipClass=relationshipClass
        this.genClass=genClass
        this.propertyClass=propertyClass
        this.genSetClass=genSetClass
    }

    abstract OntoUMLModel visitModel(Object sourceModel)
    abstract OntoUMLPackage visitPackage(Object srcPackage, OntoUMLPackage container)
    abstract OntoUMLClass visitClass(Object srcClass)
    abstract OntoUMLDataType visitDataType(Object srcDataType)
    abstract OntoUMLRelationship visitRelationship(Object srcRel)
    abstract OntoUMLAttribute visitAttribute(Object srcAttr)
    abstract OntoUMLEndPoint visitEndPoint(Object srcEp)
    abstract visitSubsetsAndRedefines(Object srcEp)
    abstract OntoUMLGeneralization visitGeneralization(Object srcGen)
    abstract OntoUMLGeneralizationSet visitGeneralizationSet(Object srcGs)

    OntoUMLModel visit(Object srcModel){
        runModel(srcModel)
        runPackages(srcModel, ontomodel)
        runTypes()
        runRelationships()
        runAttributes()
        runEndPoints()
        runSubsetsAndRedefines()
        runGeneralizations()
        runGeneralizationSets()
        return ontomodel;
    }

    private void runModel(Object srcModel) {
        this.ontomodel = visitModel(srcModel)
        srcPackagesMap.put(srcModel, ontomodel)
    }

    private void runPackages (Object srcPack, MContainer ontoParentPackage){
        srcPack."${accessMethod}"().each{ srcElem ->
            if(packageClass.isInstance(srcElem)){
                OntoUMLPackage ontopackage = visitPackage(srcElem, ontoParentPackage)
                srcPackagesMap.put(srcElem, ontopackage)
                runPackages(srcElem, ontopackage)
            }
        }
    }

    private void runTypes(){
        srcPackagesMap.keySet().each{ srcpack ->
            srcpack."${accessMethod}"().each{ srcelem ->
                if(classClass.isInstance(srcelem)){
                    OntoUMLClass ontoclass = visitClass(srcelem)
                    srcClassMap.put(srcelem,ontoclass)
                }
                else if(dataTypeClass.isInstance(srcelem)){
                    OntoUMLDataType ontoDataType = visitDataType(srcelem)
                    srcDataTypeMap.put(srcelem,ontoDataType)
                }
            }
        }
    }

    private void runRelationships(){
        srcPackagesMap.keySet().each{ srcpack ->
            srcpack."${accessMethod}"().each { srcelem ->
                if (relationshipClass.isInstance(srcelem)){
                    OntoUMLRelationship ontorel = visitRelationship(srcelem)
                    srcRelationshipsMap.put(srcelem, ontorel)
                }
            }
        }
    }

    private void runAttributes(){
        srcPackagesMap.keySet().each{ srcpack ->
            srcpack."${accessMethod}"().each { srcelem ->
                if(classClass.isInstance(srcelem) || dataTypeClass.isInstance(srcelem)){
                    srcelem."${accessMethod}"().each { attr ->
                        if(propertyClass.isInstance(attr)){
                            OntoUMLAttribute ontoattr = visitAttribute(attr)
                            srcAttributesMap.put(attr, ontoattr);
                        }
                    }
                }
            }
        }
    }

    private void runEndPoints(){
        srcPackagesMap.keySet().each{ srcpack ->
            srcpack."${accessMethod}"().each { srcelem ->
                if(relationshipClass.isInstance(srcelem)){
                    srcelem."${accessMethod}"().each { ep ->
                        OntoUMLEndPoint ontoendpoint = visitEndPoint(ep)
                        srcEndpointsMap.put(ep, ontoendpoint);
                    }
                }
            }
        }
    }

    private void runSubsetsAndRedefines(){
        srcEndpointsMap.keySet().each { srcep ->
           visitSubsetsAndRedefines(srcep)
        }
    }

   private void runGeneralizations(){
        srcPackagesMap.keySet().each {  srcpack ->
            srcpack."${accessMethod}"().each { srcelem ->
                if(typeClass.isInstance(srcelem) || relationshipClass.isInstance(srcelem)) {
                    srcelem."${accessMethod}"().each { srcgen ->
                        if (genClass.isInstance(srcgen)) {
                            OntoUMLGeneralization ontogen = visitGeneralization(srcgen)
                            srcGensMap.put(srcgen, ontogen)
                        }
                    }
                }
            }
        }
    }

    private void runGeneralizationSets(){
        srcPackagesMap.keySet().each { srcpack ->
            srcpack."${accessMethod}"().each { srcelem ->
                if(genSetClass.isInstance(srcelem)) {
                    OntoUMLGeneralizationSet ontogs = visitGeneralizationSet(srcelem)
                    srcGenSetsMap.put(srcelem, ontogs)
                }
            }
        }
    }
}
