package net.menthor.ontouml2owl

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

import net.menthor.core.traits.MClassifier
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
import net.menthor.ontouml2owl.choice.GeneralizationSetChoice
import net.menthor.ontouml2owl.choice.ReasonerChoice
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.AddAxiom
import org.semanticweb.owlapi.model.IRI
import org.semanticweb.owlapi.model.OWLAsymmetricObjectPropertyAxiom
import org.semanticweb.owlapi.model.OWLAxiom
import org.semanticweb.owlapi.model.OWLCardinalityRestriction
import org.semanticweb.owlapi.model.OWLClass
import org.semanticweb.owlapi.model.OWLDataFactory
import org.semanticweb.owlapi.model.OWLDisjointClassesAxiom
import org.semanticweb.owlapi.model.OWLDisjointObjectPropertiesAxiom
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom
import org.semanticweb.owlapi.model.OWLNamedIndividual
import org.semanticweb.owlapi.model.OWLObjectOneOf
import org.semanticweb.owlapi.model.OWLObjectProperty
import org.semanticweb.owlapi.model.OWLOntology
import org.semanticweb.owlapi.model.OWLOntologyManager
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom
import org.semanticweb.owlapi.model.RemoveAxiom

import java.text.Normalizer

/**
 * @author John Guerson
 */
class OwlMapper implements OntoUMLVisitor {

    OWLOntologyManager owlManager
    OWLOntology owlOntology
    OWLDataFactory owlDataFactory
    OwlOptions owlOptions
    OwlSkeleton owlSkeleton
    OwlUtility owlUtility
    String owlNamespace

    public OwlMapper(){}

    public run(OntoUMLModel model, OwlOptions options){
        owlOptions = options
        visit(model)
        println serializeOntology();
    }

    private String serializeOntology(){
        ByteArrayOutputStream os = new ByteArrayOutputStream()
        owlManager.saveOntology(owlOntology, os)
        String result = new String(os.toByteArray(),"UTF-8")
        result = Normalizer.normalize(result, Normalizer.Form.NFD)
        result = result.replaceAll("[^\\p{ASCII}]", "")
        return result
    }

    //=====================
    //Model
    //=====================

    @Override
    Object visitModel(OntoUMLModel ontomodel) {
        owlManager = OWLManager.createOWLOntologyManager()
        owlDataFactory = owlManager.getOWLDataFactory()
        if(owlOptions==null) {
            owlOptions = new OwlOptions()
            owlOptions.setNamespace(owlOptions.getNamespace()+ontomodel.getUniqueName()+"#")
        }
        owlNamespace = owlOptions.getNamespace()
        owlOntology = owlManager.createOntology(IRI.create(owlNamespace))
        owlSkeleton = new OwlSkeleton()
        if(getOwlOptions().useSkeleton) owlSkeleton.getAxioms().each{ axiom -> owlManager.addAxiom(owlOntology, axiom) }
        owlUtility = new OwlUtility(owlManager,owlDataFactory,owlOntology)
        return owlOntology
    }

    //=====================
    //Package
    //=====================

    @Override
    Object visitPackage(OntoUMLPackage ontopackage, Object tgtParentPackage) {
        //no mapping
        return null
    }
    @Override
    void finalizePackagesVisiting() { /*nothing to be done*/ }

    //=====================
    //Class
    //=====================

    @Override
    Object visitClass(OntoUMLClass class_) {
        OWLClass owlClass = owlUtility.createClass(owlNamespace,class_.getUniqueName())
        if(getOwlOptions().useRDFLabel) {
            owlUtility.createRDFLabel(owlClass, class_.getName())
        }
        if(getOwlOptions().useSkeleton){
            OWLClass owlParentClass = owlSkeleton.getParentClassOf(class_)
            if(owlParentClass != null) owlUtility.createSubClassOf(owlClass, owlParentClass)
        }
        return owlClass;
    }
    @Override
    void finalizeClassesVisiting() { /*nothing to be done*/ }

    //=====================
    //Generalization
    //=====================

    @Override
    Object visitGeneralization(OntoUMLGeneralization g) {
        if(g.isAmongClasses()){
            OWLClass owlGeneral = getClassMap().get(g.getGeneral())
            OWLClass owlSpecific = getClassMap().get(g.getSpecific())
            return owlUtility.createSubClassOf(owlSpecific, owlGeneral)
        }
        if(g.isAmongDataTypes()){
            //if datatype is a owl primitive type -> subPropertyOf
            //if datatype is a owl class -> subclassof
            //nothing yet
            return null
        }
    }
    @Override
    void finalizeGeneralizationsVisiting() { /*nothing to be done*/ }

    //=====================
    //Relationship
    //=====================

    @Override
    Object visitRelationship(OntoUMLRelationship r) {
        if (r.isAmongClasses()) {
            OWLClass owlDomain = getClassMap().get(r.source())
            OWLClass owlRange = getClassMap().get(r.target())
            String name = r.targetEndPoint().getName()
            String labelName = r.targetEndPoint().getUniqueName()
            String invName = r.sourceEndPoint().getName()
            String invLabelName = r.sourceEndPoint().getUniqueName()
            OWLObjectProperty owlProperty = owlUtility.createObjectProperty(owlNamespace,name,
                getOwlOptions().useRDFLabel?labelName:null, getOwlOptions().useDomain?owlDomain:null, getOwlOptions().useRange?owlRange:null)
            OWLObjectProperty inverseProperty = owlUtility.createObjectProperty(owlNamespace,invName,
                getOwlOptions().useRDFLabel?invLabelName:null, getOwlOptions().useDomain?owlRange:null, getOwlOptions().useRange?owlDomain:null)
            if(getOwlOptions().useInverse) {
                owlUtility.setInverse(inverseProperty, owlProperty)
            }
            if(getOwlOptions().useSkeleton) {
                owlUtility.createSubPropertyOf(owlProperty, owlSkeleton.getParentPropertyOfTargetProperty(r))
                owlUtility.createSubPropertyOf(inverseProperty, owlSkeleton.getParentPropertyOfSourceProperty(r))
            }
            if(getOwlOptions().useMultiplicity){
                owlUtility.createMultiplicity(owlProperty,owlDomain, owlRange, r.targetEndPoint().getLowerBound(), r.targetEndPoint().getUpperBound())
                owlUtility.createMultiplicity(inverseProperty,owlRange, owlDomain, r.sourceEndPoint().getLowerBound(), r.sourceEndPoint().getUpperBound())
            }
            return [owlProperty,inverseProperty]
        }
        if(r.isAmongDataTypes()){
            //nothing yet
            return null
        }
        if(r.isBetweenADataType()){
            //nothing yet
            return null
        }
        if(r.isBetweenARelationship()){
            //nothing yet
            return null
        }
    }
    @Override
    void finalizeRelationshipsVisiting() {
        if(getOwlOptions().useDisjointnessOfRelations){
            //OWLDisjointObjectPropertiesAxiom axiom = owlDataFactory.getOWLDisjointObjectPropertiesAxiom(relationshipsMap.values());
            //owlManager.applyChange(new AddAxiom(owlOntology, axiom));
        }
    }

    //=====================
    //DataType
    //=====================

    @Override
    Object visitDataType(OntoUMLDataType ontodatatype) {
        return null
    }
    @Override
    void finalizeDataTypesVisiting() { /*nothing to be done*/ }

    //=====================
    //Attribute
    //=====================

    @Override
    Object visitAttribute(OntoUMLAttribute attr) {
        return null
    }

    @Override
    Object visitPrimitiveStereotype(OntoUMLAttribute attr) {
        return null
    }

    @Override
    void finalizeAttributesVisiting() {/*nothing to be done*/ }

    //=====================
    //GeneralizationSet
    //=====================

    @Override
    Object visitGeneralizationSet(OntoUMLGeneralizationSet gs) {
        def choice = getOwlOptions().getGenSetMappings().get(gs)
        if(choice!=null) visitAsOwlEnumeration(gs, choice)
        return null
    }
    @Override
    void finalizeGeneralizationSetsVisiting() { /*nothing to be done*/ }

    /** visit a generalization set as owl enumeration */
    private visitAsOwlEnumeration(OntoUMLGeneralizationSet gs, GeneralizationSetChoice choice){
        List specifics = choice.chooseSpecifics(gs)
        def individuals = createAsIndividuals(specifics)
        OWLObjectOneOf oneOf = owlDataFactory.getOWLObjectOneOf(individuals)
        OWLClass owlGs = owlDataFactory.getOWLClass(owlNamespace, gs.getName().replaceAll(" ", "_"));
        OWLEquivalentClassesAxiom ax = owlDataFactory.getOWLEquivalentClassesAxiom(owlGs, oneOf);
        owlManager.applyChange(new AddAxiom(owlOntology, ax));
        int lower;
        int upper;
        if(gs.isCovering() && gs.isDisjoint()) { lower = 1; upper = 1;  }
        else if(gs.isCovering()){ lower = 1; upper = -1; }
        else { lower = 0; upper = 1; }
        String gsName = "has_"+gs.getName()
        String gsLabel = "has_"+gs.getName()
        OWLClass owlGeneral = getClassMap().get(gs.general())
        OWLObjectProperty property = owlUtility.createObjectProperty(gsName, gsLabel, owlGeneral, owlGs)
        owlUtility.createMultiplicity(property, owlGeneral, owlGs, lower, upper);
    }

    private List<OWLNamedIndividual> createAsIndividuals(List<MClassifier> classifiers){
        def individuals = []
        classifiers.each(){ s ->
            OWLNamedIndividual individual = owlDataFactory.getOWLNamedIndividual(owlNamespace, (s as MClassifier).getUniqueName())
            individuals.add(individual)
        }
        return individuals
    }

    //=====================
    //Comment
    //=====================

    @Override
    Object visitComment(OntoUMLComment c) {
        if(getOwlOptions().useComments){
            OWLClass cls = owlDataFactory.getOWLClass(IRI.create(owlNamespace+c.getIdentifier()));
            return owlUtility.createAnnotation(cls, c.getText())
        }
        return null
    }

    @Override
    void finalizeCommentsVisiting() { /* nothing to be done */ }


    //=====================
    //Subsets and Redefines
    //=====================

    @Override
    def visitSubsetsAndRedefines(OntoUMLEndPoint ep) {
        return null
    }

    //=====================
    //EndPoint
    //=====================

    @Override
    Object visitEndPoint(OntoUMLEndPoint ep) {
        //see visitRelationship() method
        return null
    }
    @Override
    void finalizeEndPointsVisiting() { /* nothing to be done*/  }

    //=====================
    //Finalization
    //=====================

    @Override
    void finalizeVisit() {
        for (OWLAxiom owlAxiom : owlOntology.axioms()) {
            if(getOwlOptions().reasoner.equals(ReasonerChoice.PELLET)) {
                if(OWLTransitiveObjectPropertyAxiom.isInstance(owlAxiom)) owlManager.applyChange(new RemoveAxiom(owlOntology, owlAxiom))
                if(OWLAsymmetricObjectPropertyAxiom.isInstance(owlAxiom)) owlManager.applyChange(new RemoveAxiom(owlOntology, owlAxiom))
            }
            else if(!getOwlOptions().useFunctionalRelations){
                if(OWLFunctionalObjectPropertyAxiom.isInstance(owlAxiom)) owlManager.applyChange(new RemoveAxiom(owlOntology, owlAxiom))
            }
            else if(!getOwlOptions().useInverseFunctionalRelations){
                if(OWLInverseFunctionalObjectPropertyAxiom.isInstance(owlAxiom)) owlManager.applyChange(new RemoveAxiom(owlOntology, owlAxiom))
            }
            else if(!getOwlOptions().useSymmetricRelations){
                if(OWLSymmetricObjectPropertyAxiom.isInstance(owlAxiom)) owlManager.applyChange(new RemoveAxiom(owlOntology, owlAxiom))
            }
            else if(!getOwlOptions().useTransitiveRelations){
                if(OWLTransitiveObjectPropertyAxiom.isInstance(owlAxiom)) owlManager.applyChange(new RemoveAxiom(owlOntology, owlAxiom))
            }
            else if(!getOwlOptions().useAssymetricRelations){
                if(OWLAsymmetricObjectPropertyAxiom.isInstance(owlAxiom)) owlManager.applyChange(new RemoveAxiom(owlOntology, owlAxiom))
            }
            else if(!getOwlOptions().useReflexiveRelations){
                if(OWLReflexiveObjectPropertyAxiom.isInstance(owlAxiom)) owlManager.applyChange(new RemoveAxiom(owlOntology, owlAxiom))
            }
            else if(!getOwlOptions().useIrreflexiveRelations){
                if(OWLIrreflexiveObjectPropertyAxiom.isInstance(owlAxiom)) owlManager.applyChange(new RemoveAxiom(owlOntology, owlAxiom))
            }
            else if(!getOwlOptions().useDisjointnessOfClasses){
                if(OWLDisjointClassesAxiom.isInstance(owlAxiom)) owlManager.applyChange(new RemoveAxiom(owlOntology, owlAxiom))
            }
            else if(!getOwlOptions().useDisjointnessOfRelations){
                if(OWLDisjointObjectPropertiesAxiom.isInstance(owlAxiom)) owlManager.applyChange(new RemoveAxiom(owlOntology, owlAxiom))
            }
            else if(!getOwlOptions().useMultiplicity){
                if(OWLCardinalityRestriction.isInstance(owlAxiom)) owlManager.applyChange(new RemoveAxiom(owlOntology, owlAxiom))
            }
        }
    }
}
