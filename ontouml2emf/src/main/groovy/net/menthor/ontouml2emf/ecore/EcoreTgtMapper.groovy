package net.menthor.ontouml2emf.ecore

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

import net.menthor.ontouml.*
import net.menthor.ontouml.stereotypes.PrimitiveStereotype
import net.menthor.ontouml.visitor.OntoUMLVisitor
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.EDataType
import org.eclipse.emf.ecore.EEnum
import org.eclipse.emf.ecore.EEnumLiteral
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.EStructuralFeature
import org.eclipse.emf.ecore.EcoreFactory
import org.eclipse.emf.ecore.EcorePackage
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.util.BasicExtendedMetaData
import org.eclipse.emf.ecore.util.ExtendedMetaData
import org.eclipse.emf.ecore.xmi.XMLResource
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl
import sun.security.krb5.internal.crypto.EType

/**
 * @author John Guerson
 */
class EcoreTgtMapper implements OntoUMLVisitor {

    static EcoreFactory theCoreFactory = EcoreFactory.eINSTANCE

    EcoreOptions options = new EcoreOptions()

    Object toEcore(OntoUMLModel ontomodel, EcoreOptions opt) {
        this.options = opt
        return visit(ontomodel)
    }

    Object toEcore(OntoUMLModel ontomodel) {
        return visit(ontomodel)
    }

    Resource serialize (EPackage ecoremodel, String ecorepath){
        ResourceSet ecoreResourceSet = new ResourceSetImpl()
        URI ecoreURI = URI.createFileURI(ecorepath)
        ecoreResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new XMLResourceFactoryImpl())
        ecoreResourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE)
        // enable extended metadata
        final ExtendedMetaData extendedMetaData = new BasicExtendedMetaData(ecoreResourceSet.getPackageRegistry())
        ecoreResourceSet.getLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetaData)
        Resource resource = ecoreResourceSet.createResource(ecoreURI)
        resource.getContents().add(ecoremodel)
        try{
            resource.save(Collections.emptyMap())
        }catch(IOException e){
            e.printStackTrace()
        }
        return resource;
    }

    @Override
    Object visitModel(OntoUMLModel ontomodel) {
        def ecoreRootModel = theCoreFactory.createEPackage()
        ecoreRootModel.setName(ontomodel.getName())
        this.packagesMap.put(ontomodel, ecoreRootModel)
        ecoreRootModel.setNsPrefix(ecoreRootModel.getName().toLowerCase().replace(" ","-"));
        ecoreRootModel.setNsURI("http://menthor.net/"+ecoreRootModel.getName().toLowerCase().replace(" ","-")+"/");
        return ecoreRootModel
    }

    @Override
    Object visitPackage(OntoUMLPackage ontopackage, Object tgtParentPackage) {
        EPackage ecorepackage = theCoreFactory.createEPackage()
        ecorepackage.setName(ontopackage.getName())
        if (!options.ignorePackages) (tgtParentPackage as EPackage).getESubpackages().add(ecorepackage)
        return ecorepackage
    }

    @Override
    Object visitClass(OntoUMLClass ontoclass) {
        EClass ecoreclass = theCoreFactory.createEClass()
        ecoreclass.setAbstract(ontoclass.isAbstract_())
        ecoreclass.setName(ontoclass.getName())
        def ontopack = this.packagesMap.get(ontoclass.getContainer())
        if (options.ignorePackages) (this.model as EPackage).getEClassifiers().add(ecoreclass)
        else (ontopack as EPackage).getEClassifiers().add(ecoreclass)
        return ecoreclass
    }

    Object visitEnumeration(OntoUMLDataType ontodatatype){
        EEnum ecoreEnum = theCoreFactory.createEEnum()
        ecoreEnum.setName(ontodatatype.getName())
        for (OntoUMLLiteral lit : ontodatatype.getLiterals()) {
            EEnumLiteral elit = theCoreFactory.createEEnumLiteral();
            elit.setName(lit.getText());
            ecoreEnum.getELiterals().add(elit);
        }
        def ecorepack = this.packagesMap.get(ontodatatype.getContainer())
        if (options.ignorePackages) (this.model as EPackage).getEClassifiers().add(ecoreEnum);
        else (ecorepack as EPackage).getEClassifiers().add(ecoreEnum);
        return ecoreEnum
    }

    @Override
    Object visitDataType(OntoUMLDataType ontodatatype) {
        if(ontodatatype.isEnumeration()){
            return visitEnumeration(ontodatatype)
        }
        EClass ecoredt = theCoreFactory.createEClass()
        ecoredt.setName(ontodatatype.getName())
        def ecorepack = this.packagesMap.get(ontodatatype.getContainer())
        if (options.ignorePackages) (this.model as EPackage).getEClassifiers().add(ecoredt);
        else (ecorepack as EPackage).getEClassifiers().add(ecoredt);
        return ecoredt
    }

    @Override
    Object visitRelationship(OntoUMLRelationship ontorel) {
        if (ontorel.isDerivation()) return null
        EClassifier esource = this.classMap.get(ontorel.source())
        if (esource == null) esource = this.dataTypeMap.get(ontorel.source())
        if (esource == null) esource = this.relationshipsMap.get(ontorel.source())
        EClassifier etarget = this.classMap.get(ontorel.target())
        if (etarget == null) etarget = this.dataTypeMap.get(ontorel.target())
        if (etarget == null) etarget = this.relationshipsMap.get(ontorel.target())
        List<EReference> ereferenceList = []
        EReference eRef = null
        ontorel.getEndPoints().each { ep ->
            eRef = theCoreFactory.createEReference()
            eRef.setLowerBound(ep.getLowerBound())
            eRef.setUpperBound(ep.getUpperBound())
            EClassifier etype = this.classMap.get(ep.getClassifier())
            if (etype == null) etype = this.dataTypeMap.get(ep.getClassifier())
            if (etype == null) etype = this.relationshipsMap.get(ep.getClassifier())
            eRef.setEType(etype)
            if (ep.getName() == null || ep.getName().isEmpty()) eRef.setName(eRef.getEType().getName().toLowerCase())
            else eRef.setName(ep.getName())
            eRef.setDerived(ep.isDerived())
            if (ontorel.isMeronymic() && ep.equals(ontorel.sourceEndPoint())) {
                eRef.setContainment(true)
            }
            ereferenceList.add(eRef)
            //store endpoint in the mapping
            this.endpointsMap.put(ep, eRef)
        }
        // adding to owner class
        if (etarget instanceof EClass) {
            (etarget as EClass).getEStructuralFeatures().add(ereferenceList.get(0))
        }
        if(esource instanceof EClass) {
            (esource as EClass).getEStructuralFeatures().add(ereferenceList.get(1))
        }
        if(etarget instanceof EClass && esource instanceof EClass){
            ((EReference)ereferenceList.get(0)).setEOpposite(ereferenceList.get(1))
            ((EReference)ereferenceList.get(1)).setEOpposite(ereferenceList.get(0))
        }
        return ereferenceList
    }

    @Override
    Object visitPrimitiveStereotype(OntoUMLAttribute attr){
        PrimitiveStereotype pt = (PrimitiveStereotype)attr.getStereotype()
        EType etype=null
        if (pt == PrimitiveStereotype.INTEGER) { etype = EcorePackage.eINSTANCE.getEInt() }
        else if (pt == PrimitiveStereotype.BOOLEAN) { etype = EcorePackage.eINSTANCE.getEBoolean() }
        else if (pt == PrimitiveStereotype.REAL) { etype = EcorePackage.eINSTANCE.getEDouble() }
        else if (pt == PrimitiveStereotype.STRING) { etype = EcorePackage.eINSTANCE.getEString() }
        else if (pt == PrimitiveStereotype.DATE) { etype = EcorePackage.eINSTANCE.getEDate() }
        else if (pt == PrimitiveStereotype.DATE_TIME) { etype = EcorePackage.eINSTANCE.getEDate() }
        return etype
    }

    @Override
    Object visitAttribute(OntoUMLAttribute attr) {
        EStructuralFeature eSF = theCoreFactory.createEAttribute()
        def eOwner = this.classMap.get(attr.getOwner())
        if(eOwner instanceof EClass) {
            EClass eclass = this.classMap.get(attr.getOwner())
            eclass.getEStructuralFeatures().add(eSF)
        }
        eSF.setName(attr.getName())
        eSF.setDerived(attr.isDerived())
        eSF.setEType(primitiveMap.get(attr.getStereotype()))
        if(attr.getName()==null || attr.getName().isEmpty()) eSF.setName(eSF.getEType().getName().toLowerCase())
        else eSF.setName(attr.getName())
        return eSF
    }

    @Override
    Object visitEndPoint(OntoUMLEndPoint ep) {
        //see visitRelationship() method
        return null
    }

    @Override
    def visitSubsetsAndRedefines(OntoUMLEndPoint ep) {
        //no mapping
        return null
    }

    @Override
    Object visitGeneralizationSet(OntoUMLGeneralizationSet gs) {
        //no mapping
        return null
    }

    @Override
    Object visitComment(OntoUMLComment c) {
        //no mapping
        return null
    }

    @Override
    Object visitGeneralization(OntoUMLGeneralization g) {
        EClass egeneral = null
        egeneral = this.classMap.get(g.getGeneral())
        if(egeneral==null) egeneral = this.dataTypeMap.get(g.getGeneral())
        if(egeneral==null) egeneral = this.relationshipsMap.get(g.getGeneral())
        EClass especific = null
        especific = this.classMap.get(g.getSpecific())
        if(especific==null) especific = this.dataTypeMap.get(g.getSpecific())
        if(especific==null) especific = this.relationshipsMap.get(g.getSpecific())
         // add to specific super choice
        especific.getESuperTypes().add(egeneral);
        return [egeneral, especific]
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
    void finalizeCommentsVisiting(){}
}
