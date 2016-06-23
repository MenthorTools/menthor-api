package net.menthor.ontouml.visitor

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
import net.menthor.ontouml.OntoUMLComment
import net.menthor.ontouml.OntoUMLDataType
import net.menthor.ontouml.OntoUMLPackage
import net.menthor.ontouml.OntoUMLEndPoint
import net.menthor.ontouml.OntoUMLGeneralization
import net.menthor.ontouml.OntoUMLGeneralizationSet
import net.menthor.ontouml.OntoUMLModel
import net.menthor.ontouml.OntoUMLRelationship
import net.menthor.core.traits.MContainer
import net.menthor.ontouml.stereotypes.PrimitiveStereotype

/**
 * Visitor pattern.
 *
 * @author John Guerson
 * */

trait OntoUMLVisitor {

    Object model
    Map <OntoUMLPackage, Object> packagesMap = [:]
    Map <OntoUMLClass, Object> classMap = [:]
    Map <OntoUMLDataType, Object> dataTypeMap = [:]
    Map <OntoUMLRelationship, Object> relationshipsMap = [:]
    Map <OntoUMLAttribute, Object> attributesMap = [:]
    Map <OntoUMLEndPoint, Object> endpointsMap = [:]
    Map <OntoUMLGeneralization, Object> gensMap = [:]
    Map <OntoUMLGeneralizationSet, Object> genSetsMap = [:]
    Map <OntoUMLComment, Object> commentMap = [:]
    Map <PrimitiveStereotype, Object> primitiveMap = [:]

    abstract Object visitModel(OntoUMLModel ontomodel)
    abstract Object visitPackage(OntoUMLPackage ontopackage, Object tgtParentPackage)
    abstract Object visitClass(OntoUMLClass ontoclass)
    abstract Object visitDataType(OntoUMLDataType ontodatatype)
    abstract Object visitRelationship(OntoUMLRelationship ontorel)
    abstract Object visitAttribute(OntoUMLAttribute attr)
    abstract Object visitPrimitiveStereotype(OntoUMLAttribute attr)
    abstract Object visitEndPoint(OntoUMLEndPoint ep)
    abstract visitSubsetsAndRedefines(OntoUMLEndPoint ep)
    abstract Object visitGeneralization(OntoUMLGeneralization g)
    abstract Object visitGeneralizationSet(OntoUMLGeneralizationSet gs)
    abstract Object visitComment(OntoUMLComment c)

    abstract void finalizeVisit()
    abstract void finalizePackagesVisiting()
    abstract void finalizeClassesVisiting()
    abstract void finalizeDataTypesVisiting()
    abstract void finalizeRelationshipsVisiting()
    abstract void finalizeAttributesVisiting()
    abstract void finalizeEndPointsVisiting()
    abstract void finalizeGeneralizationsVisiting()
    abstract void finalizeGeneralizationSetsVisiting()
    abstract void finalizeCommentsVisiting()

    void printResult(){
        packagesMap.keySet().each{ a -> if(packagesMap.get(a)!=null) println packagesMap.get(a) }
        classMap.keySet().each{ a -> if(classMap.get(a)!=null) println classMap.get(a) }
        dataTypeMap.keySet().each { a -> if(dataTypeMap.get(a)!=null) println dataTypeMap.get(a) }
        relationshipsMap.keySet().each{ a -> if(relationshipsMap.get(a)!=null) println relationshipsMap.get(a) }
        gensMap.keySet().each{ a -> if(gensMap.get(a)!=null) println gensMap.get(a) }
        genSetsMap.keySet().each{ a -> if(genSetsMap.get(a)!=null) println genSetsMap.get(a) }
        attributesMap.keySet().each{ a -> if(attributesMap.get(a)!=null) println attributesMap.get(a) }
        primitiveMap.keySet().each{ a -> if(primitiveMap.get(a)!=null) println primitiveMap.get(a) }
        endpointsMap.keySet().each{ a -> if(endpointsMap.get(a)!=null) println endpointsMap.get(a) }
        commentMap.keySet().each{ a -> if(commentMap.get(a)!=null) println commentMap.get(a)}
    }

    Object visit(OntoUMLModel ontomodel){
        this.model = visitModel(ontomodel)
        packagesMap.put(ontomodel as OntoUMLPackage,model)
        runPackages(ontomodel, model)
        runClasses()
        runDataTypes()
        runRelationships()
        runAttributes()
        runEndPoints()
        runSubsetsAndRedefines()
        runGeneralizations()
        runGeneralizationSets()
        runComments()
        finalizeVisit()
        printResult()
        return model;
    }

    void runPackages(MContainer ontopackage, Object tgtParentPackage){
        ontopackage.getElements().each{ ontoelem ->
            if(ontoelem instanceof OntoUMLPackage){
                def tgtpackage = visitPackage(ontoelem as OntoUMLPackage, tgtParentPackage)
                packagesMap.put(ontoelem as OntoUMLPackage, tgtpackage)
                runPackages(ontoelem as OntoUMLPackage,tgtpackage as Object)
            }
        }
        finalizePackagesVisiting()
    }

    private void runClasses() {
        packagesMap.keySet().each { ontopack ->
            ontopack.types().each { elem ->
                if (elem instanceof OntoUMLClass) {
                    OntoUMLClass ontoclass = elem as OntoUMLClass
                    Object tgtClass = visitClass(ontoclass)
                    classMap.put((ontoclass), tgtClass)
                }
            }
        }
        finalizeClassesVisiting()
    }

    private void runDataTypes(){
        packagesMap.keySet().each{ ontopack ->
            ontopack.types().each{ elem ->
                if(elem instanceof OntoUMLDataType){
                    OntoUMLDataType ontodatatype = elem as OntoUMLDataType
                    Object tgtDataType = visitDataType(ontodatatype)
                    dataTypeMap.put(ontodatatype,tgtDataType)
                }
            }
        }
        finalizeDataTypesVisiting()
    }

    private void runRelationships(){
        packagesMap.keySet().each{ ontopack ->
            ontopack.relationships().each{ rel ->
                Object tgtRel = visitRelationship(rel)
                relationshipsMap.put(rel, tgtRel)
            }
        }
        finalizeRelationshipsVisiting()
    }

    private void runAttributes(){
        packagesMap.keySet().each{ ontopack ->
            ontopack.attributes().each{ attr ->
                //primitive type
                def ontoprimitive = (attr as OntoUMLAttribute).getStereotype()
                if(!primitiveMap.containsKey(ontoprimitive)) {
                    Object umlprimitive = visitPrimitiveStereotype(attr)
                    primitiveMap.put(ontoprimitive, umlprimitive)
                }
                //attribute
                Object tgtAttr = visitAttribute(attr)
                attributesMap.put(attr, tgtAttr);
            }
        }
        finalizeAttributesVisiting()
    }

   private void runEndPoints(){
        packagesMap.keySet().each{ ontopack ->
            ontopack.endPoints().each{ ep ->
                Object tgtEndPoint = visitEndPoint(ep)
                endpointsMap.put(ep, tgtEndPoint);
            }
        }
       finalizeEndPointsVisiting()
    }

   private void runSubsetsAndRedefines(){
        packagesMap.keySet().each { ontopack ->
            ontopack.endPoints().each{ ep ->
                visitSubsetsAndRedefines(ep)
            }
        }
    }

    private void runGeneralizations(){
        packagesMap.keySet().each { ontopack ->
            ontopack.generalizations().each{ g ->
                Object tgtGen = visitGeneralization(g)
                gensMap.put(g, tgtGen)
            }
        }
        finalizeGeneralizationsVisiting()
    }

    private void runGeneralizationSets(){
        packagesMap.keySet().each { ontopack ->
            ontopack.generalizationSets().each{ gs ->
                Object tgtGenSet = visitGeneralizationSet(gs)
                genSetsMap.put(gs, tgtGenSet)
            }
        }
       finalizeGeneralizationSetsVisiting()
    }

    private void runComments(){
        packagesMap.keySet().each{ ontopack ->
            ontopack.comments().each{ c ->
                Object tgtCom = visitComment(c)
                commentMap.put(c, tgtCom)
            }
        }
        finalizeCommentsVisiting()
    }
}
