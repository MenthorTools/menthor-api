package net.menthor.ontouml2emf.refontouml

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

import RefOntoUML.RefOntoUMLFactory
import RefOntoUML.util.RefOntoUMLResourceFactoryImpl
import net.menthor.ontouml.OntoUMLClass
import net.menthor.ontouml.OntoUMLDataType
import net.menthor.ontouml.OntoUMLEndPoint
import net.menthor.ontouml.OntoUMLFactory
import net.menthor.ontouml.OntoUMLGeneralization
import net.menthor.ontouml.OntoUMLGeneralizationSet
import net.menthor.ontouml.OntoUMLLiteral
import net.menthor.ontouml.OntoUMLModel
import net.menthor.ontouml.OntoUMLAttribute
import net.menthor.ontouml.OntoUMLPackage
import net.menthor.ontouml.OntoUMLRelationship
import net.menthor.ontouml.stereotypes.ClassStereotype
import net.menthor.ontouml.stereotypes.DataTypeStereotype
import net.menthor.ontouml.stereotypes.PrimitiveStereotype
import net.menthor.ontouml.stereotypes.QualityStereotype
import net.menthor.ontouml.stereotypes.RelationshipStereotype
import net.menthor.core.traits.MClassifier
import net.menthor.core.traits.MType
import net.menthor.ontouml.values.MeasurementValue
import net.menthor.ontouml.values.ScaleValue
import net.menthor.ontouml2emf.EMFVisitor
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.resource.impl.ResourceImpl
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.util.ExtendedMetaData
import org.eclipse.emf.ecore.xmi.XMLResource
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl

import java.text.Normalizer

/**
 * @author John Guerson
 */
class FromRefontoumlMapper implements EMFVisitor {

    RefontoumlLog log = new RefontoumlLog()
    RefontoumlOptions options = new RefontoumlOptions()

    OntoUMLModel run(RefOntoUML.Package refmodel, RefontoumlOptions options){
        this.options = options
        run(refmodel)
    }

    OntoUMLModel run(RefOntoUML.Package refmodel){
        log.clear();
        setup(RefOntoUML.Package.class,RefOntoUML.Type.class, RefOntoUML.Class.class, RefOntoUML.DataType.class,
                RefOntoUML.Association.class,RefOntoUML.Generalization.class, RefOntoUML.Property.class, RefOntoUML.GeneralizationSet.class)
        def result = visit(refmodel)
        return result
    }

    @Override
    OntoUMLModel visitModel(Object srcModel) {
        def refmodel = srcModel as RefOntoUML.Package
        OntoUMLModel ontomodel = OntoUMLFactory.createModel(refmodel.getName())
        return ontomodel
    }

    @Override
    OntoUMLPackage visitPackage(Object srcPackage, OntoUMLPackage ontoParentPackage) {
        def refpack = srcPackage as RefOntoUML.Package
        def ontopack = ontoParentPackage.createPackage(refpack.getName())
        return ontopack
    }

    private String getRefStereotype(Object srcElem){
        String type = srcElem.getClass().toString().replaceAll("class RefOntoUML.impl.","")
        type = type.replaceAll("Impl","")
        type = Normalizer.normalize(type, Normalizer.Form.NFD)
        if (!type.equalsIgnoreCase("association")) type = type.replace("Association","")
        return type
    }

    private ClassStereotype getClassStereotype(RefOntoUML.Class refElem){
        ClassStereotype cs = ClassStereotype.getEnum(getRefStereotype(refElem))
        if(cs==null){
            if(refElem instanceof RefOntoUML.Kind) cs = ClassStereotype.KIND
            else if(refElem instanceof RefOntoUML.Quantity) cs = ClassStereotype.QUANTITY
            else if(refElem instanceof RefOntoUML.Collective) cs = ClassStereotype.COLLECTIVE
            else if(refElem instanceof RefOntoUML.SubKind) cs = ClassStereotype.SUBKIND
            else if(refElem instanceof RefOntoUML.Phase) cs = ClassStereotype.PHASE
            else if(refElem instanceof RefOntoUML.Role) cs = ClassStereotype.ROLE
            else if(refElem instanceof RefOntoUML.RoleMixin) cs = ClassStereotype.ROLEMIXIN
            else if(refElem instanceof RefOntoUML.Category) cs = ClassStereotype.CATEGORY
            else if(refElem instanceof RefOntoUML.Mixin) cs = ClassStereotype.MIXIN
            else if(refElem instanceof RefOntoUML.Relator) cs = ClassStereotype.RELATOR
            else if(refElem instanceof RefOntoUML.Mode) cs = ClassStereotype.MODE
            else if(refElem instanceof RefOntoUML.PerceivableQuality) cs = ClassStereotype.QUALITY
            else if(refElem instanceof RefOntoUML.NonPerceivableQuality) cs = ClassStereotype.QUALITY
            else if(refElem instanceof RefOntoUML.NominalQuality) cs = ClassStereotype.QUALITY
            else{
                if(options.assumeClassAsEvent) {
                    log.appendLine("Class '"+refElem.getName()+"' handled as <<event>>")
                    cs = ClassStereotype.EVENT
                }
            }
        }
        return cs
    }

    private DataTypeStereotype getDataTypeStereotype(RefOntoUML.DataType refElem){
        DataTypeStereotype cs = DataTypeStereotype.getEnum(getRefStereotype(refElem))
        if(cs==null){
            if(refElem instanceof RefOntoUML.Enumeration) cs = DataTypeStereotype.ENUMERATION
            if(refElem instanceof RefOntoUML.StringNominalStructure) cs = DataTypeStereotype.DIMENSION
            if(refElem instanceof RefOntoUML.DecimalIntervalDimension) cs = DataTypeStereotype.DIMENSION
            if(refElem instanceof RefOntoUML.DecimalOrdinalDimension) cs = DataTypeStereotype.DIMENSION
            if(refElem instanceof RefOntoUML.DecimalRationalDimension) cs = DataTypeStereotype.DIMENSION
            if(refElem instanceof RefOntoUML.IntegerIntervalDimension) cs = DataTypeStereotype.DIMENSION
            if(refElem instanceof RefOntoUML.IntegerOrdinalDimension) cs = DataTypeStereotype.DIMENSION
            if(refElem instanceof RefOntoUML.IntegerRationalDimension) cs = DataTypeStereotype.DIMENSION
            if(refElem instanceof RefOntoUML.MeasurementDomain) cs = DataTypeStereotype.DOMAIN
        }
        return cs
    }

    private RelationshipStereotype getRelationshipStereotype(RefOntoUML.Association refElem) {
        RelationshipStereotype cs = RelationshipStereotype.getEnum(getRefStereotype(refElem))
        if(cs==null){
            if(refElem instanceof RefOntoUML.Derivation) cs = RelationshipStereotype.DERIVATION
            if(refElem instanceof RefOntoUML.Characterization) cs = RelationshipStereotype.CHARACTERIZATION
            if(refElem instanceof RefOntoUML.Mediation) cs = RelationshipStereotype.MEDIATION
            if(refElem instanceof RefOntoUML.MaterialAssociation) cs = RelationshipStereotype.MATERIAL
            if(refElem instanceof RefOntoUML.FormalAssociation) cs = RelationshipStereotype.FORMAL
            if(refElem instanceof RefOntoUML.componentOf) cs = RelationshipStereotype.COMPONENTOF
            if(refElem instanceof RefOntoUML.subCollectionOf) cs = RelationshipStereotype.SUBCOLLECTIONOF
            if(refElem instanceof RefOntoUML.subQuantityOf) cs = RelationshipStereotype.SUBQUANTITYOF
            if(refElem instanceof RefOntoUML.memberOf) cs = RelationshipStereotype.MEMBEROF
            if(refElem instanceof RefOntoUML.Structuration) cs = RelationshipStereotype.STRUCTURATION;
        }
        return cs;
    }

    private PrimitiveStereotype getPrimitiveStereotype(RefOntoUML.PrimitiveType elem){
        PrimitiveStereotype ps = PrimitiveStereotype.getEnum(elem.getName());
        if(ps==null && elem.getName()!=null){
            if(elem.getName().compareToIgnoreCase("Integer")==0) ps = PrimitiveStereotype.INTEGER;
            if(elem.getName().compareToIgnoreCase("int")==0) ps = PrimitiveStereotype.INTEGER;
            if(elem.getName().compareToIgnoreCase("Boolean")==0) ps = PrimitiveStereotype.BOOLEAN;
            if(elem.getName().compareToIgnoreCase("Real")==0) ps = PrimitiveStereotype.REAL;
            if(elem.getName().compareToIgnoreCase("String")==0) ps = PrimitiveStereotype.STRING;
            if(elem.getName().compareToIgnoreCase("Date")==0) ps = PrimitiveStereotype.DATE;
            if(elem.getName().compareToIgnoreCase("DateTime")==0) ps = PrimitiveStereotype.DATE_TIME;
        }
        return ps;
    }

    @Override
    OntoUMLClass visitClass(Object srcClass) {
        def refclass = srcClass as RefOntoUML.Class
        boolean isAbstract = refclass.isIsAbstract()
        String name = refclass.getName()
        def cs = getClassStereotype(refclass)
        OntoUMLClass ontoclass = OntoUMLFactory.createClass(cs,name,srcPackagesMap.get(refclass.eContainer()))
        if(refclass instanceof RefOntoUML.PerceivableQuality) ontoclass.setQualityStereotype(QualityStereotype.PERCEIVABLE)
        else if(refclass instanceof RefOntoUML.NonPerceivableQuality) ontoclass.setQualityStereotype(QualityStereotype.NON_PERCEIVABLE)
        else if(refclass instanceof RefOntoUML.NominalQuality) ontoclass.setQualityStereotype(QualityStereotype.NOMINAL)
        if(refclass instanceof RefOntoUML.Collective) ontoclass.setIsExtensional((refclass as RefOntoUML.Collective).isIsExtensional())
        return ontoclass
    }

    @Override
    OntoUMLDataType visitDataType(Object srcDataType) {
        def refdatatype = srcDataType as RefOntoUML.DataType
        String name = refdatatype.getName()
        DataTypeStereotype cs = getDataTypeStereotype(refdatatype)
        OntoUMLDataType ontodatatype = OntoUMLFactory.createDataType(cs, name, srcPackagesMap.get(refdatatype.eContainer()))
        if(refdatatype instanceof RefOntoUML.MeasurementDimension){
            ontodatatype.setUnitOfMeasure((refdatatype as RefOntoUML.MeasurementDimension).getUnitOfMeasure())
        }
        if(refdatatype instanceof RefOntoUML.IntegerIntervalDimension) {
            ontodatatype.setScale(ScaleValue.INTERVAL)
            ontodatatype.setMeasurement(MeasurementValue.INTEGER)
        }
        if(refdatatype instanceof RefOntoUML.IntegerOrdinalDimension) {
            ontodatatype.setScale(ScaleValue.ORDINAL)
            ontodatatype.setMeasurement(MeasurementValue.INTEGER)
        }
        if(refdatatype instanceof RefOntoUML.IntegerRationalDimension) {
            ontodatatype.setScale(ScaleValue.RATIONAL)
            ontodatatype.setMeasurement(MeasurementValue.INTEGER)
        }
        if(refdatatype instanceof RefOntoUML.DecimalIntervalDimension) {
            ontodatatype.setScale(ScaleValue.INTERVAL)
            ontodatatype.setMeasurement(MeasurementValue.DECIMAL)
        }
        if(refdatatype instanceof RefOntoUML.DecimalOrdinalDimension) {
            ontodatatype.setScale(ScaleValue.ORDINAL)
            ontodatatype.setMeasurement(MeasurementValue.DECIMAL)
        }
        if(refdatatype instanceof RefOntoUML.DecimalRationalDimension) {
            ontodatatype.setScale(ScaleValue.RATIONAL)
            ontodatatype.setMeasurement(MeasurementValue.DECIMAL)
        }
        if(refdatatype instanceof RefOntoUML.Enumeration){
            for(RefOntoUML.EnumerationLiteral lt: (refdatatype as RefOntoUML.Enumeration).getOwnedLiteral()){
                OntoUMLLiteral ontoLit = OntoUMLFactory.createLiteral(lt.getName());
                ontoLit.setOwner(ontodatatype);
            }
        }
        return ontodatatype
    }

    @Override
    OntoUMLRelationship visitRelationship(Object srcRel) {
        def refrel = srcRel as RefOntoUML.Association
        String name = refrel.getName();
        RelationshipStereotype rs = getRelationshipStereotype(srcRel);
        OntoUMLRelationship ontorel = OntoUMLFactory.createRelationship(rs, name, srcPackagesMap.get(refrel.eContainer()))
        return ontorel
    }

    @Override
    OntoUMLAttribute visitAttribute(Object srcAttr) {
        def refAttr = srcAttr as RefOntoUML.Property
        MType ontotype = srcClassMap.get(refAttr.eContainer())
        if(ontotype==null) ontotype = srcDataTypeMap.get(refAttr.eContainer())
        PrimitiveStereotype ps = getPrimitiveStereotype((RefOntoUML.PrimitiveType)refAttr.getType())
        int lower = refAttr.getLower()
        int upper = refAttr.getUpper()
        String name = refAttr.getName()
        boolean isDerived = refAttr.isIsDerived()
        boolean isDependency = refAttr.isIsReadOnly()
        OntoUMLAttribute ontoattr = OntoUMLFactory.createAttribute(ontotype, ps, lower, upper, name, isDerived, isDependency)
        return ontoattr
    }

    @Override
    OntoUMLEndPoint visitEndPoint(Object srcEp) {
        def ep = srcEp as RefOntoUML.Property
        OntoUMLRelationship rel = srcRelationshipsMap.get(ep.getAssociation())
        MClassifier ontotype = srcClassMap.get(ep.getType())
        if(ontotype==null) ontotype = srcDataTypeMap.get(ep.getType())
        if(ontotype==null) ontotype = srcRelationshipsMap.get(ep.getType())
        int lower = ep.getLower()
        int upper = ep.getUpper()
        String name = ep.getName()
        boolean isDerived = ep.isIsDerived()
        boolean isDependency = ep.isIsReadOnly()
        OntoUMLEndPoint ontopoint = OntoUMLFactory.createEndPoint(rel, ontotype, lower, upper, name, isDerived, isDependency);
        return ontopoint
    }

    @Override
    def visitSubsetsAndRedefines(Object srcEp) {
        def refEp = srcEp as RefOntoUML.Property
        OntoUMLEndPoint ontoep = srcEndpointsMap.get(refEp)
        for(RefOntoUML.Property subsetted: refEp.getSubsettedProperty()){
            if(ontoep!=null) ontoep.getSubsets().add(srcEndpointsMap.get(subsetted))
        }
        for(RefOntoUML.Property redefined: refEp.getRedefinedProperty()){
            if(ontoep!=null) ontoep.getRedefines().add(srcEndpointsMap.get(redefined))
        }
    }

    @Override
    OntoUMLGeneralization visitGeneralization(Object srcGen) {
        def g = srcGen as RefOntoUML.Generalization
        RefOntoUML.Classifier general = g.getGeneral()
        RefOntoUML.Classifier specific = g.getSpecific()
        MClassifier ontogeneral = srcClassMap.get(general)
        if(ontogeneral==null) ontogeneral = srcDataTypeMap.get(general)
        if(ontogeneral==null) ontogeneral = srcRelationshipsMap.get(general)
        MClassifier ontospecific = srcClassMap.get(specific)
        if(ontospecific==null) ontospecific = srcDataTypeMap.get(specific)
        if(ontospecific==null) ontospecific = srcRelationshipsMap.get(specific)
        OntoUMLGeneralization ontog = OntoUMLFactory.createGeneralization(ontospecific, ontogeneral, srcPackagesMap.get(g.eContainer()))
        return ontog
    }

    @Override
    OntoUMLGeneralizationSet visitGeneralizationSet(Object srcGs) {
        def refgs = srcGs as RefOntoUML.GeneralizationSet
        String name = refgs.getName()
        boolean isCovering = refgs.isIsCovering()
        boolean isDisjoint = refgs.isIsDisjoint()
        List<RefOntoUML.Generalization> refgens = refgs.getGeneralization()
        List<OntoUMLGeneralization> gens = new ArrayList<OntoUMLGeneralization>()
        for(RefOntoUML.Generalization g: refgens){
            if(srcGensMap.get(g)!=null) gens.add(srcGensMap.get(g))
        }
        OntoUMLGeneralizationSet ontogs = OntoUMLFactory.createGeneralizationSet(name,isCovering, isDisjoint, gens, srcPackagesMap.get(refgs))
        return ontogs
    }
}