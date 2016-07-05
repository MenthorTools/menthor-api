package net.menthor.ontouml2emf.refontouml

import net.menthor.ontouml.OntoUMLComment

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

import net.menthor.ontouml.OntoUMLDataType
import net.menthor.ontouml.OntoUMLEndPoint
import net.menthor.ontouml.OntoUMLGeneralization
import net.menthor.ontouml.OntoUMLGeneralizationSet
import net.menthor.ontouml.OntoUMLModel
import net.menthor.ontouml.OntoUMLAttribute
import net.menthor.ontouml.OntoUMLPackage
import net.menthor.ontouml.OntoUMLClass
import net.menthor.ontouml.OntoUMLRelationship
import net.menthor.core.traits.MClassifier
import net.menthor.ontouml.visitor.OntoUMLVisitor
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.common.util.URI

import RefOntoUML.RefOntoUMLFactory
import RefOntoUML.util.RefOntoUMLResourceFactoryImpl
import org.eclipse.emf.ecore.xmi.XMLResource

/**
 * @author John Guerson
 */
class ToRefontoumlMapper implements OntoUMLVisitor {

    static RefOntoUMLFactory factory = RefOntoUMLFactory.eINSTANCE

    RefontoumlOptions options = new RefontoumlOptions()

    Object run(OntoUMLModel ontomodel, RefontoumlOptions opt) {
        this.options = opt
        return run(ontomodel)
    }

    Object run(OntoUMLModel ontomodel) {
        return visit(ontomodel)
    }

    @Override
    Object visitModel(OntoUMLModel ontomodel) {
        def refmodel = factory.createModel()
        refmodel.setName(ontomodel.getName())
        this.packagesMap.put(ontomodel, refmodel)
        return refmodel
    }

    @Override
    Object visitPackage(OntoUMLPackage ontopackage, Object tgtParentPackage) {
        RefOntoUML.Package refpack = factory.createPackage()
        refpack.setName(ontopackage.getName())
        if (!options.ignorePackages) (tgtParentPackage as RefOntoUML.Package).getPackagedElement().add(refpack)
        return refpack
    }

    @Override
    Object visitClass(OntoUMLClass ontoclass) {
        RefOntoUML.Class refClass=null
        if(ontoclass.isKind()) refClass = factory.createKind()
        else if(ontoclass.isSubKind()) refClass = factory.createSubKind()
        else if(ontoclass.isCollective()) { refClass = factory.createCollective(); refClass.setIsExtensional(ontoclass.isExtensional()) }
        else if(ontoclass.isQuantity()) refClass = factory.createQuantity()
        else if(ontoclass.isRole()) refClass = factory.createRole()
        else if(ontoclass.isPhase()) refClass = factory.createPhase()
        else if(ontoclass.isRelator()) refClass = factory.createRelator()
        else if(ontoclass.isMode()) refClass = factory.createMode()
        else if(ontoclass.isPerceivableQuality()) refClass = factory.createPerceivableQuality()
        else if(ontoclass.isNonPerceivableQuality()) refClass = factory.createNonPerceivableQuality()
        else if(ontoclass.isNominalQuality()) refClass = factory.createNominalQuality()
        else if(ontoclass.isCategory()) refClass = factory.createCategory()
        else if(ontoclass.isRoleMixin()) refClass = factory.createRoleMixin()
        else if(ontoclass.isPhaseMixin()) refClass = factory.createPhase()
        else if(ontoclass.isMixin()) refClass = factory.createMixin()
        else refClass = factory.createClass()
        if(refClass!=null) {
            String name = ontoclass.getName()
            boolean isAbstract = ontoclass.isAbstract_()
            refClass.setName(name)
            refClass.setIsAbstract(isAbstract)
            RefOntoUML.Package refpack = this.packagesMap.get(ontoclass.getContainer())
            if (options.ignorePackages) (this.model as RefOntoUML.Package).getPackagedElement().add(refClass)
            else (refpack as RefOntoUML.Package).getPackagedElement().add(refClass)
        }
        return refClass
    }

    @Override
    Object visitDataType(OntoUMLDataType ontodatatype) {
        Object refdatatype=null
        if(ontodatatype.isDimension()){
            if(ontodatatype.isIntervalDecimal()) refdatatype = factory.createDecimalIntervalDimension()
            if(ontodatatype.isIntervalInteger()) refdatatype = factory.createIntegerIntervalDimension()
            if(ontodatatype.isRationalInteger()) refdatatype = factory.createIntegerRationalDimension()
            if(ontodatatype.isRationalDecimal()) refdatatype = factory.createDecimalRationalDimension()
            if(ontodatatype.isOrdinalDecimal()) refdatatype = factory.createDecimalOrdinalDimension()
            if(ontodatatype.isOrdinalInteger()) refdatatype = factory.createIntegerOrdinalDimension()
            if(ontodatatype.isNominalString()) refdatatype = factory.createStringNominalStructure()
            ((RefOntoUML.MeasurementDimension)refdatatype).setUnitOfMeasure(ontodatatype.getUnitOfMeasure())
        } else if(ontodatatype.isDomain()) {
            refdatatype = factory.createMeasurementDomain()
        } else if(ontodatatype.isEnumeration()){
            refdatatype = factory.createEnumeration()
            ontodatatype.getLiterals().each{ ontolit ->
                RefOntoUML.EnumerationLiteral lit = factory.createEnumerationLiteral()
                lit.setName(ontolit.getText())
                ((RefOntoUML.Enumeration)refdatatype).getOwnedLiteral().add(lit)
                lit.setEnumeration(((RefOntoUML.Enumeration)refdatatype))
            }
        } else if(ontodatatype.isDataType()) {
            refdatatype = factory.createDataType()
        } else {
            refdatatype = factory.createDataType()
        }
        if(refdatatype!=null) {
            String name = ontodatatype.getName()
            ((RefOntoUML.Type)refdatatype).setName(name)
            RefOntoUML.Package refpack = this.packagesMap.get(ontodatatype.getContainer())
            if (options.ignorePackages) (this.model as RefOntoUML.Package).getPackagedElement().add(refdatatype)
            else (refpack as RefOntoUML.Package).getPackagedElement().add(refdatatype)
        }
        return refdatatype
    }

    @Override
    Object visitRelationship(OntoUMLRelationship ontorel) {
        RefOntoUML.Association refRel=null;
        if(ontorel.isCharacterization()) refRel = factory.createCharacterization();
        else if(ontorel.isMediation()) refRel = factory.createMediation();
        else if(ontorel.isDerivation()) refRel = factory.createDerivation();
        else if(ontorel.isFormal()) refRel = factory.createFormalAssociation();
        else if(ontorel.isMaterial()) refRel = factory.createMaterialAssociation();
        else if(ontorel.isComponentOf()) refRel = factory.createcomponentOf();
        else if(ontorel.isMemberOf()) refRel = factory.creatememberOf();
        else if(ontorel.isSubCollectionOf()) refRel = factory.createsubCollectionOf();
        else if(ontorel.isSubQuantityOf()) refRel = factory.createsubQuantityOf();
        else if(ontorel.isStructuration()) refRel = factory.createStructuration();
        else refRel = factory.createAssociation();
        if(refRel!=null){
            String name = ontorel.getName();
            refRel.setName(name);
            RefOntoUML.Package refpack = this.packagesMap.get(ontorel.getContainer())
            if (options.ignorePackages) (this.model as RefOntoUML.Package).getPackagedElement().add(refRel)
            else (refpack as RefOntoUML.Package).getPackagedElement().add(refRel)
        }
        return refRel
    }

    @Override
    Object visitPrimitiveStereotype(OntoUMLAttribute attr){
        RefOntoUML.PrimitiveType primitiveType = factory.createPrimitiveType()
        primitiveType.setName(attr.getStereotype().getName())
        RefOntoUML.Package refpack = this.packagesMap.get(attr.getOwner().getContainer())
        if (options.ignorePackages) (this.model as RefOntoUML.Package).getPackagedElement().add(primitiveType)
        else (refpack as RefOntoUML.Package).getPackagedElement().add(primitiveType)
        return primitiveType
    }

    @Override
    Object visitAttribute(OntoUMLAttribute attr) {
        RefOntoUML.Property refAttr=factory.createProperty()
        RefOntoUML.LiteralInteger lowerBound = factory.createLiteralInteger()
        RefOntoUML.LiteralUnlimitedNatural upperBound = factory.createLiteralUnlimitedNatural()
        lowerBound.setValue(attr.getUpperBound())
        upperBound.setValue(attr.getLowerBound())
        refAttr.setLowerValue(lowerBound)
        refAttr.setUpperValue(upperBound)
        refAttr.setIsDerived(attr.isDerived())
        refAttr.setIsReadOnly(attr.isDependency())
        refAttr.setName(attr.getName())
        RefOntoUML.PrimitiveType primitiveType = primitiveMap.get(attr.getStereotype())
        refAttr.setType(primitiveType)
        if(attr.getOwner() instanceof OntoUMLClass) {
            RefOntoUML.Type reftype = this.classMap.get(attr.getOwner())
            if(reftype!=null)((RefOntoUML.Class)reftype).getOwnedAttribute().add(refAttr)
        }
        if(attr.getOwner() instanceof OntoUMLDataType) {
            RefOntoUML.Type reftype = this.dataTypeMap.get(attr.getOwner())
            if(reftype!=null)((RefOntoUML.DataType)reftype).getOwnedAttribute().add(refAttr)
        }
        return refAttr
    }

    @Override
    Object visitEndPoint(OntoUMLEndPoint ep) {
        int upper = ep.getUpperBound()
        int lower = ep.getLowerBound()
        boolean isDerived = ep.isDerived()
        boolean isDependency = ep.isDependency()
        String name = ep.getName()
        RefOntoUML.Association assoc = this.relationshipsMap.get(ep.getOwner())
        RefOntoUML.Property refEndPoint=factory.createProperty()
        RefOntoUML.LiteralInteger lowerBound = factory.createLiteralInteger()
        RefOntoUML.LiteralUnlimitedNatural upperBound = factory.createLiteralUnlimitedNatural()
        lowerBound.setValue(lower)
        upperBound.setValue(upper)
        refEndPoint.setLowerValue(lowerBound)
        refEndPoint.setUpperValue(upperBound)
        refEndPoint.setIsDerived(isDerived)
        refEndPoint.setIsReadOnly(isDependency)
        refEndPoint.setName(name)
        refEndPoint.setAssociation(assoc)
        assoc.getMemberEnd().add(refEndPoint)
        assoc.getOwnedEnd().add(refEndPoint)
        assoc.getNavigableOwnedEnd().add(refEndPoint)
        if(ep.getClassifier() instanceof OntoUMLClass) refEndPoint.setType(this.classMap.get(ep.getClassifier()))
        if(ep.getClassifier() instanceof OntoUMLDataType) refEndPoint.setType(this.dataTypeMap.get(ep.getClassifier()))
        if(ep.getClassifier() instanceof OntoUMLRelationship) refEndPoint.setType(this.relationshipsMap.get(ep.getClassifier()))
        return refEndPoint
    }

    @Override
    def visitSubsetsAndRedefines(OntoUMLEndPoint ep) {
        RefOntoUML.Property refProp = this.endpointsMap.get(ep);
        for(OntoUMLEndPoint superProp: ep.getSubsets()){
            if(refProp!=null) refProp.getSubsettedProperty().add(this.endpointsMap.get(superProp));
        }
        for(OntoUMLEndPoint superProp: ep.getRedefines()){
            if(refProp!=null) refProp.getRedefinedProperty().add(this.endpointsMap.get(superProp));
        }
    }

    @Override
    Object visitGeneralization(OntoUMLGeneralization g) {
        MClassifier general = g.getGeneral()
        MClassifier specific = g.getSpecific()
        RefOntoUML.Classifier refGeneral=null
        if(general instanceof OntoUMLClass) refGeneral= this.classMap.get(general)
        if(general instanceof OntoUMLDataType) refGeneral = this.dataTypeMap.get(general)
        if(general instanceof OntoUMLRelationship) refGeneral = this.relationshipsMap.get(general)
        RefOntoUML.Classifier refSpecific = null
        if(specific instanceof OntoUMLClass) refSpecific= this.classMap.get(specific)
        if(specific instanceof OntoUMLDataType) refSpecific = this.dataTypeMap.get(specific)
        if(specific instanceof OntoUMLRelationship) refSpecific = this.relationshipsMap.get(specific)
        RefOntoUML.Generalization refGen = factory.createGeneralization()
        if(refGeneral!=null) {
            refGen.setGeneral(refGeneral)
            refGeneral.getGeneralization().add(refGen)
        }
        if(refSpecific!=null) {
            refGen.setSpecific(refSpecific)
            refSpecific.getGeneralization().add(refGen)
        }
        return refGen
    }

    @Override
    Object visitGeneralizationSet(OntoUMLGeneralizationSet gs) {
        boolean isCovering = gs.isCovering()
        boolean isDisjoint = gs.isDisjoint()
        List<OntoUMLGeneralization> gens = gs.getGeneralizations()
        List<RefOntoUML.Generalization> refGens = []
        gens.each{ g ->
            if(this.gensMap.get(g)!=null) refGens.add(this.gensMap.get(g))
        }
        RefOntoUML.GeneralizationSet refGenSet = factory.createGeneralizationSet()
        refGenSet.setIsCovering(isCovering)
        refGenSet.setIsDisjoint(isDisjoint)
        refGenSet.setName("")
        refGenSet.getGeneralization().addAll(refGens)
        RefOntoUML.Package refpack = this.packagesMap.get(gs.getContainer())
        if (options.ignorePackages) (this.model as RefOntoUML.Package).getPackagedElement().add(refGenSet)
        else (refpack as RefOntoUML.Package).getPackagedElement().add(refGenSet)
        return refGenSet
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
    void finalizeRelationshipsVisiting() {}
    @Override
    void finalizeClassesVisiting() {}
    @Override
    void finalizeDataTypesVisiting() {}
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
