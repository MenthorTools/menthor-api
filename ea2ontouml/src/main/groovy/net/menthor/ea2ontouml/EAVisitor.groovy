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

/**
 *  Visitor pattern to visit EA's xmi file
 *  Transformations should implement this trait
 *
 *  @author John Guerson
 */
trait EAVisitor {

    GPathResult eaRoot
    Object model
    Map <GPathResult, Object> packagesMap = [:]
    Map <GPathResult, Object> classesMap = [:]
    Map <GPathResult, Object> datatypesMap = [:]
    Map <GPathResult, Object> gensMap = [:]
    Map <GPathResult, Object> genSetsMap = [:]
    Map <GPathResult, Object> assocMap = [:]
    Map <GPathResult, Object> endPointsMap = [:]
    Map <GPathResult, Object> attrsMap = [:]
    Map <GPathResult, Object> primitiveMap = [:]
    Map <GPathResult, Object> typesMap = [:] //derived
    Map <GPathResult, Object> classifierMap = [:] //derived
    Map <GPathResult, Object> diagramsMap = [:]

    void printResult(){
        packagesMap.keySet().each{ a -> println packagesMap.get(a) }
        classesMap.keySet().each{ a -> println classesMap.get(a) }
        datatypesMap.keySet().each { a -> println datatypesMap.get(a) }
        assocMap.keySet().each{ a -> println assocMap.get(a) }
        gensMap.keySet().each{ a -> println gensMap.get(a) }
        genSetsMap.keySet().each{ a -> println genSetsMap.get(a) }
        attrsMap.keySet().each{ a -> println attrsMap.get(a) }
        primitiveMap.keySet().each{ a -> println primitiveMap.get(a) }
        endPointsMap.keySet().each{ a -> println endPointsMap.get(a) }
    }

    Object getTypeById(String id){
        def result = null
        typesMap.keySet().each{ c -> if(c.'@xmi:id'.text()==id) result = typesMap.get(c) }
        return result
    }
    Object getPrimitiveById(String id){
        def result = null
        primitiveMap.keySet().each{ c -> if(c.'@xmi:id'.text()==id) result = primitiveMap.get(c) }
        return result
    }
    Object getClassifierById(String id){
        def result = null
        classifierMap.keySet().each{ c -> if(c.'@xmi:id'.text()==id) result = classifierMap.get(c) }
        return result
    }
    Object getAssociationById(String id){
        def result = null
        assocMap.keySet().each{ c -> if(c.'@xmi:id'.text()==id) result = assocMap.get(c) }
        return result
    }
    Object getGeneralizationById(String id){
        def result = null
        gensMap.keySet().each{ c -> if(c.'@xmi:id'.text()==id) result = gensMap.get(c) }
        return result
    }

    abstract Object processEAModel(GPathResult eaModel)
    abstract Object processEAPackage(GPathResult eaPack, GPathResult eaParentPack)
    abstract Object processEAClass(GPathResult eaClass, GPathResult eaParentPack)
    abstract Object processEAAssociationClass(GPathResult eaClass, GPathResult eaParentPack)
    abstract Object processEADataType(GPathResult eaClass, GPathResult eaParentPack)
    abstract Object processEAEnumeration(GPathResult eaEnum, GPathResult eaParentPack)
    abstract Object processEAAssociation(GPathResult eaRel, GPathResult eaParentPack)
    abstract Object processEAGeneralization(GPathResult eaGen, GPathResult eaSpecificType)
    abstract Object processEAGeneralizationSet(GPathResult eaGenSet, GPathResult eaParentPack)
    abstract Object processEAEndPoint(GPathResult eaProp)
    abstract Object processEAAttribute(GPathResult eaAttr, GPathResult ownerType)
    abstract Object processEAPrimitive(GPathResult eaPrimitive)
    abstract Object processEADiagram(GPathResult eaDiagram)

    Object visit(InputStream stream){
        def list = load(stream)
        visitEAPrimitives(list[1])
        visitEAModel(list[0])
        visitEADiagrams(list[1])
        return model
    }

    Object visit(File file){
        def list = load(file)
        visitEAPrimitives(list[1])
        visitEAModel(list[0])
        visitEADiagrams(list[1])
        return model
    }

    List load(InputStream stream){
        def doc = new XmlSlurper().parse(stream)
        return parse(doc)
    }


    List load(File file){
        if(!file.exists()) throw Exception("File does not exist ("+file.getAbsolutePath()+")");
        if(!file.getName().contains(".xml")) throw Exception("A XML file is expected, file format not supported ("+file.getName()+")");
        def doc = new XmlSlurper().parse(file)
        return parse(doc)
    }

    private List parse(doc){
        def umlNs = doc.lookupNamespace('uml')
        def nsMap = [ xmi: 'http://schema.omg.org/spec/XMI/2.1',
                      thecustomprofile: "http://www.sparxsystems.com/profiles/thecustomprofile/1.0"]
        if ('http://schema.omg.org/spec/UML/2.2' == umlNs) {
            nsMap['uml'] = 'http://schema.omg.org/spec/UML/2.2'
        } else {
            nsMap['uml'] = 'http://schema.omg.org/spec/UML/2.1'
        }
        eaRoot = doc.declareNamespace(nsMap)
        def eaModel = eaRoot.'uml:Model'.packagedElement
        def eaExtension = eaRoot.'xmi:Extension'
        return [eaModel, eaExtension]
    }

    void visitEAModel(GPathResult eaModel){
        model = processEAModel(eaModel)
        packagesMap.put(eaModel, model)
        visitEAPackages(eaModel)
        visitEAClassifiers()
        visitEAGeneralizations()
        visitEAGeneralizationSets()
        visitEAEndPoints()
        visitEAAttributes()
        endEAModelVisit()
    }

    abstract void endEAModelVisit()

    void visitEAPrimitives(GPathResult eaExtension){
        eaExtension.primitivetypes.packagedElement.'**'.each{ p ->
            if(p.'@xmi:type'.text()=='uml:PrimitiveType'){
                def obj = processEAPrimitive(p)
                primitiveMap.put(p, obj)
            }
        }
    }

    void visitEADiagrams(GPathResult eaExtension){
        eaExtension.diagrams.each{ d ->
            def obj = processEADiagram(d)
            diagramsMap.put(d, obj)
        }
    }

    void visitEAPackages(GPathResult eamodel){
        eamodel.packagedElement.each{ p ->
            if(p.'@xmi:type' == 'uml:Package') {
                def obj = processEAPackage(p, eamodel)
                packagesMap.put(p, obj)
                visitEAPackages(p)
            }
        }
    }

    void visitEAClassifiers(){
        packagesMap.keySet().each{ p ->
            p.packagedElement.each { c ->
                if (c.'@xmi:type' == 'uml:Class') {
                    def obj = processEAClass(c, p)
                    classesMap.put(c, obj)
                }
                if (c.'@xmi:type' == 'uml:AssociationClass'){
                    def obj = processEAAssociationClass(c, p)
                    classesMap.put(c, obj)
                }
                if (c.'@xmi:type' == 'uml:DataType') {
                    def obj = processEADataType(c, p)
                    datatypesMap.put(c, obj)
                }
                if (c.'@xmi:type' == 'uml:Enumeration') {
                    def obj = processEAEnumeration(c, p)
                    datatypesMap.put(c, obj)
                }
                if (c.'@xmi:type' == 'uml:Association') {
                    def obj = processEAAssociation(c, p)
                    assocMap.put(c, obj)
                }
            }
        }
        typesMap.putAll(classesMap)
        typesMap.putAll(datatypesMap)
        classifierMap.putAll(typesMap)
        classifierMap.putAll(assocMap)
    }

    void visitEAGeneralizations(){
        def typesMap = [:]
        typesMap.putAll(classesMap)
        typesMap.putAll(datatypesMap)
        typesMap.putAll(assocMap)
        typesMap.keySet().each{ t ->
            t.generalization.each{ g ->
                if(g.'@xmi:type' == 'uml:Generalization') {
                    def obj = processEAGeneralization(g, t)
                    gensMap.put(g, obj)
                }
            }
        }
    }

    void visitEAGeneralizationSets() {
        packagesMap.keySet().each { p ->
            p.packagedElement.each { gs ->
                if (gs.'@xmi:type' == 'uml:GeneralizationSet') {
                    def obj = processEAGeneralizationSet(gs, p)
                    genSetsMap.put(gs, obj)
                }
            }
        }
    }

    void visitEAEndPoints(){
        assocMap.keySet().each{ a ->
            a.ownedEnd.each{ ep ->
                if (ep.'@xmi:type' == 'uml:Property') {
                    def obj = processEAEndPoint(ep)
                    endPointsMap.put(ep, obj)
                }
            }
        }
    }

    void visitEAAttributes(){
        typesMap.keySet().each{ t ->
            t.ownedAttribute.each{ attr ->
                if (attr.'@xmi:type' == 'uml:Property') {
                    def obj = processEAAttribute(attr, t)
                    attrsMap.put(attr, obj)
                }
            }
        }
    }
}
