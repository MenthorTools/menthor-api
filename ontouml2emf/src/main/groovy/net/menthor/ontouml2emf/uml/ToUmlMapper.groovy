package net.menthor.ontouml2emf.uml

import net.menthor.ontouml.OntoUMLAttribute
import net.menthor.ontouml.OntoUMLClass
import net.menthor.ontouml.OntoUMLComment
import net.menthor.ontouml.OntoUMLDataType
import net.menthor.ontouml.OntoUMLEndPoint
import net.menthor.ontouml.OntoUMLGeneralization
import net.menthor.ontouml.OntoUMLGeneralizationSet
import net.menthor.ontouml.OntoUMLModel
import net.menthor.ontouml.OntoUMLPackage
import net.menthor.ontouml.OntoUMLRelationship
import net.menthor.ontouml.visitor.OntoUMLVisitor

import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl

import org.eclipse.uml2.uml.Association
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.Classifier
import org.eclipse.uml2.uml.DataType
import org.eclipse.uml2.uml.Enumeration
import org.eclipse.uml2.uml.EnumerationLiteral
import org.eclipse.uml2.uml.Generalization
import org.eclipse.uml2.uml.GeneralizationSet
import org.eclipse.uml2.uml.LiteralInteger
import org.eclipse.uml2.uml.LiteralUnlimitedNatural
import org.eclipse.uml2.uml.Package
import org.eclipse.uml2.uml.PrimitiveType
import org.eclipse.uml2.uml.Property
import org.eclipse.uml2.uml.Type
import org.eclipse.uml2.uml.UMLFactory
import org.eclipse.uml2.uml.UMLPackage
import org.eclipse.uml2.uml.resource.UMLResource

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

/**
 * @author John Guerson
 */
class ToUmlMapper implements OntoUMLVisitor {

    static UMLFactory theUmlFactory = UMLFactory.eINSTANCE

    UmlOptions options = new UmlOptions()

    Object run(OntoUMLModel ontomodel, UmlOptions opt) {
        this.options = opt
        return visit(ontomodel)
    }

    Object run(OntoUMLModel ontomodel) {
        return visit(ontomodel)
    }

    @Override
    Object visitModel(OntoUMLModel ontomodel) {
        def umlmodel = theUmlFactory.createModel()
        umlmodel.setName(ontomodel.getName())
        this.packagesMap.put(ontomodel, umlmodel)
        return umlmodel
    }

    @Override
    Object visitPackage(OntoUMLPackage ontopackage, Object tgtParentPackage) {
        Package umlpackage = theUmlFactory.createPackage()
        umlpackage.setName(ontopackage.getName())
        if (!options.ignorePackages) (tgtParentPackage as Package).getPackagedElements().add(umlpackage)
        return umlpackage
    }

    @Override
    Object visitClass(OntoUMLClass ontoclass) {
        Class umlclass = theUmlFactory.createClass()
        umlclass.setIsAbstract(ontoclass.isAbstract_())
        umlclass.setName(ontoclass.getName())
        def umlpack = this.packagesMap.get(ontoclass.getContainer())
        if (options.ignorePackages) (this.model as Package).getPackagedElements().add(umlclass)
        else (umlpack as Package).getPackagedElements().add(umlclass)
        return umlclass
    }

    @Override
    Object visitDataType(OntoUMLDataType ontodatatype) {
        if(ontodatatype.isEnumeration()) {
            return visitEnumeration(ontodatatype)
        }else{
            DataType umldt = theUmlFactory.createDataType()
            umldt.setName(ontodatatype.getName())
            def umlpack = this.packagesMap.get(ontodatatype.getContainer())
            if (options.ignorePackages) (this.model as Package).getPackagedElements().add(umldt);
            else (umlpack as Package).getPackagedElements().add(umldt);
            return umldt
        }
    }

    Object visitEnumeration(OntoUMLDataType ontodatatype){
        def umlenum = theUmlFactory.createEnumeration()
        umlenum.setName(ontodatatype.getName())
        ontodatatype.getLiterals().each { ontolit ->
            EnumerationLiteral lit = theUmlFactory.createEnumerationLiteral()
            lit.setName(ontolit.getText())
            ((Enumeration) umlenum).getOwnedLiterals().add(lit)
            lit.setEnumeration(((Enumeration) umlenum))
        }
        def umlpack = this.packagesMap.get(ontodatatype.getContainer())
        if (options.ignorePackages) (this.model as Package).getPackagedElements().add(umlenum);
        else (umlpack as Package).getPackagedElements().add(umlenum);
        return umlenum
    }

    @Override
    Object visitRelationship(OntoUMLRelationship ontorel) {
        def umlassoc = theUmlFactory.createAssociation();
        String name = ontorel.getName();
        umlassoc.setName(name);
        umlassoc.setIsDerived(ontorel.isDerived())
        Package umlpack = this.packagesMap.get(ontorel.getContainer())
        if (options.ignorePackages) (this.model as Package).getPackagedElements().add(umlassoc);
        else (umlpack as Package).getPackagedElements().add(umlassoc);
        return umlassoc
    }

    @Override
    Object visitPrimitiveStereotype(OntoUMLAttribute attr){
        PrimitiveType primitiveType = theUmlFactory.createPrimitiveType()
        primitiveType.setName(attr.getStereotype().getName())
        Package umlpack = this.packagesMap.get(attr.getOwner().getContainer())
        if (options.ignorePackages) (this.model as Package).getPackagedElements().add(primitiveType);
        else (umlpack as Package).getPackagedElements().add(primitiveType);
        return primitiveType
    }

    @Override
    Object visitAttribute(OntoUMLAttribute attr) {
        Property umlAttr=theUmlFactory.createProperty()
        LiteralInteger lowerBound = theUmlFactory.createLiteralInteger()
        LiteralUnlimitedNatural upperBound = theUmlFactory.createLiteralUnlimitedNatural()
        lowerBound.setValue(attr.getLowerBound())
        upperBound.setValue(attr.getUpperBound())
        umlAttr.setLowerValue(lowerBound)
        umlAttr.setUpperValue(upperBound)
        umlAttr.setIsDerived(attr.isDerived())
        umlAttr.setIsReadOnly(attr.isDependency())
        umlAttr.setName(attr.getName())
        PrimitiveType primitiveType = primitiveMap.get(attr.getStereotype())
        umlAttr.setType(primitiveType)
        if(attr.getOwner() instanceof Class) {
            Type umltype = this.classMap.get(attr.getOwner())
            if(umltype!=null)((Class)umltype).getOwnedAttributes().add(umlAttr)
        }
        if(attr.getOwner() instanceof OntoUMLDataType) {
            Type umltype = this.dataTypeMap.get(attr.getOwner())
            if(umltype!=null)((DataType)umltype).getOwnedAttributes().add(umlAttr)
        }
        return umlAttr
    }

    @Override
    Object visitEndPoint(OntoUMLEndPoint ep) {
        Association assoc = this.relationshipsMap.get(ep.getOwner())
        Property umlEndPoint=theUmlFactory.createProperty()
        LiteralInteger lowerBound = theUmlFactory.createLiteralInteger()
        LiteralUnlimitedNatural upperBound = theUmlFactory.createLiteralUnlimitedNatural()
        lowerBound.setValue(ep.getLowerBound())
        upperBound.setValue(ep.getUpperBound())
        umlEndPoint.setLowerValue(lowerBound)
        umlEndPoint.setUpperValue(upperBound)
        umlEndPoint.setIsDerived(ep.isDerived())
        umlEndPoint.setIsReadOnly(ep.isDependency())
        umlEndPoint.setName(ep.getName())
        umlEndPoint.setAssociation(assoc)
        assoc.getMemberEnds().add(umlEndPoint)
        assoc.getOwnedEnds().add(umlEndPoint)
        assoc.getNavigableOwnedEnds().add(umlEndPoint)
        if(ep.getClassifier() instanceof OntoUMLClass) umlEndPoint.setType(this.classMap.get(ep.getClassifier()))
        if(ep.getClassifier() instanceof OntoUMLDataType) umlEndPoint.setType(this.dataTypeMap.get(ep.getClassifier()))
        if(ep.getClassifier() instanceof OntoUMLRelationship) umlEndPoint.setType(this.relationshipsMap.get(ep.getClassifier()))
        return umlEndPoint
    }

    @Override
    def visitSubsetsAndRedefines(OntoUMLEndPoint ep) {
        Property umlProp = this.endpointsMap.get(ep);
        for(OntoUMLEndPoint superProp: ep.getSubsets()){
            if(umlProp!=null) umlProp.getSubsettedProperties().add(this.endpointsMap.get(superProp));
        }
        for(OntoUMLEndPoint superProp: ep.getRedefines()){
            if(umlProp!=null) umlProp.getRedefinedProperties().add(this.endpointsMap.get(superProp));
        }
    }

    @Override
    Object visitGeneralization(OntoUMLGeneralization g) {
        def general = g.getGeneral()
        def specific = g.getSpecific()
        Classifier umlGeneral=null
        if(general instanceof OntoUMLClass) umlGeneral= this.classMap.get(general)
        if(general instanceof OntoUMLDataType) umlGeneral = this.dataTypeMap.get(general)
        if(general instanceof OntoUMLRelationship) umlGeneral = this.relationshipsMap.get(general)
        Classifier umlSpecific = null
        if(specific instanceof OntoUMLClass) umlSpecific= this.classMap.get(specific)
        if(specific instanceof OntoUMLDataType) umlSpecific = this.dataTypeMap.get(specific)
        if(specific instanceof OntoUMLRelationship) umlSpecific = this.relationshipsMap.get(specific)
        Generalization umlGen = theUmlFactory.createGeneralization()
        if(umlGeneral!=null) {
            umlGen.setGeneral(umlGeneral)
            umlGeneral.getGeneralizations().add(umlGen)
        }
        if(umlSpecific!=null) {
            umlGen.setSpecific(umlSpecific)
            umlSpecific.getGeneralizations().add(umlGen)
        }
        return umlGen
    }

    @Override
    Object visitGeneralizationSet(OntoUMLGeneralizationSet gs) {
        boolean isCovering = gs.isCovering()
        boolean isDisjoint = gs.isDisjoint()
        List<OntoUMLGeneralization> gens = gs.getGeneralizations()
        List<Generalization> umlGens = []
        gens.each{ g ->
            if(this.gensMap.get(g)!=null) umlGens.add(this.gensMap.get(g))
        }
        GeneralizationSet umlGenSet = theUmlFactory.createGeneralizationSet()
        umlGenSet.setIsCovering(isCovering)
        umlGenSet.setIsDisjoint(isDisjoint)
        umlGenSet.setName("")
        umlGenSet.getGeneralizations().addAll(umlGens)
        Package umlpack = this.packagesMap.get(gs.getContainer())
        if (options.ignorePackages) (this.model as Package).getPackagedElements().add(umlGenSet);
        else (umlpack as Package).getPackagedElements().add(umlGenSet);
        return umlGenSet
    }

    @Override
    Object visitComment(OntoUMLComment c) {
        //no mapping (yet)
        return null
    }

    @Override
    void finalizeVisit() {}
    @Override
    void finalizePackagesVisiting() {}
    @Override
    void finalizeClassesVisiting() {}
    @Override
    void finalizeDataTypesVisiting() {}
    @Override
    void finalizeRelationshipsVisiting() {}
    @Override
    void finalizeAttributesVisiting() {}
    @Override
    void finalizeEndPointsVisiting() {}
    @Override
    void finalizeGeneralizationsVisiting() {}
    @Override
    void finalizeGeneralizationSetsVisiting() {}
    @Override
    void finalizeCommentsVisiting() {}
}
