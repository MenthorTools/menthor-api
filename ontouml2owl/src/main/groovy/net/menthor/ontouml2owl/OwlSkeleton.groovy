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

import net.menthor.ontouml.OntoUMLClass
import net.menthor.ontouml.OntoUMLRelationship
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.OWLAxiom
import org.semanticweb.owlapi.model.OWLClass
import org.semanticweb.owlapi.model.OWLDataFactory
import org.semanticweb.owlapi.model.OWLObjectProperty
import org.semanticweb.owlapi.model.OWLOntology
import org.semanticweb.owlapi.model.OWLOntologyManager

import java.util.stream.Stream

/**
 * OntoUML skeleton in OWL.
 * @author John Guerson
 */

class OwlSkeleton {

    OWLOntologyManager manager = OWLManager.createOWLOntologyManager()
    OWLDataFactory factory = manager.getOWLDataFactory()

    File file = new File("ontouml-owl/src/main/resources/base-structure.owl")
    String namespace

    OwlSkeleton(){}

    Stream<OWLAxiom> getAxioms(){
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
        def iri = ontology.getOntologyID().getOntologyIRI().get()
        namespace = iri.getNamespace()
        return ontology.axioms()
    }

    OWLClass getParentClassOf(OntoUMLClass class_){
        if (class_.isCollective()) {
            return factory.getOWLClass(namespace+"Collection")
        }else if (class_.isKind()) {
            return factory.getOWLClass(namespace+"FunctionalComplex")
        }else if(class_.isQuantity()){
            return factory.getOWLClass(namespace+"Quantity")
        }else if(class_.isMode()){
            return factory.getOWLClass(namespace+"Mode")
        }else if(class_.isQuality()){
            return factory.getOWLClass(namespace+"Quality")
        }else if(class_.isRelator()){
            return factory.getOWLClass(namespace+"Relator")
        }else if(class_.isEvent()){
            return factory.getOWLClass(namespace+"Event")
        }
    }

    OWLObjectProperty getParentPropertyOfTargetProperty(OntoUMLRelationship r) {
        if(r.isSubCollectionOf()){
            return factory.getOWLObjectProperty(namespace+"collectionPart")
        }else if(r.isMemberOf()){
            return factory.getOWLObjectProperty(namespace+"isMemberOf")
        }else if(r.isComponentOf()){
            return factory.getOWLObjectProperty(namespace+"functionalPart")
        }else if(r.isSubQuantityOf()){
            return factory.getOWLObjectProperty(namespace+"quantityPart")
        }else if(r.isMaterial()){
            return factory.getOWLObjectProperty(namespace+"materialProperty")
        }else if(r.isFormal()){
            return factory.getOWLObjectProperty(namespace+"formalProperty")
        }else if(r.isMediation()){
            return factory.getOWLObjectProperty(namespace+"mediatedBy")
        }else if(r.isCharacterization()){
            def srcClass = (r.sourceAClass()) as OntoUMLClass
            if(srcClass.isQualitativeIntrinsicMoment()){
                return factory.getOWLObjectProperty(namespace+"intrinsicProperty")
            }else{
                return factory.getOWLObjectProperty(namespace+"bearer")
            }
        }
    }

    OWLObjectProperty getParentPropertyOfSourceProperty(OntoUMLRelationship r) {
        if (r.isSubCollectionOf()) {
            return factory.getOWLObjectProperty(namespace + "collectionWhole")
        } else if (r.isMemberOf()) {
            return factory.getOWLObjectProperty(namespace + "isMemberOf")
        } else if (r.isComponentOf()) {
            return factory.getOWLObjectProperty(namespace + "functionalWhole")
        } else if (r.isSubQuantityOf()) {
            return factory.getOWLObjectProperty(namespace + "quantityWhole")
        } else if (r.isMaterial()) {
            return factory.getOWLObjectProperty(namespace + "materialProperty")
        } else if (r.isFormal()) {
            return factory.getOWLObjectProperty(namespace + "formalProperty")
        } else if (r.isMediation()) {
            return factory.getOWLObjectProperty(namespace + "mediates")
        } else if (r.isCharacterization()) {
            def srcClass = (r.sourceAClass()) as OntoUMLClass
            if (srcClass.isQualitativeIntrinsicMoment()) {
                return factory.getOWLObjectProperty(namespace + "bearer")
            } else {
                return factory.getOWLObjectProperty(namespace + "intrinsicProperty")
            }
        }
    }
}
