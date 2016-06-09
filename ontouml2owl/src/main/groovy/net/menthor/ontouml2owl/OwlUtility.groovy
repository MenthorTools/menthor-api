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

import org.semanticweb.owlapi.model.AddAxiom
import org.semanticweb.owlapi.model.IRI
import org.semanticweb.owlapi.model.OWLAnnotation
import org.semanticweb.owlapi.model.OWLAxiom
import org.semanticweb.owlapi.model.OWLClass
import org.semanticweb.owlapi.model.OWLDataFactory
import org.semanticweb.owlapi.model.OWLDeclarationAxiom
import org.semanticweb.owlapi.model.OWLNamedObject
import org.semanticweb.owlapi.model.OWLObjectExactCardinality
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality
import org.semanticweb.owlapi.model.OWLObjectMinCardinality
import org.semanticweb.owlapi.model.OWLObjectProperty
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom
import org.semanticweb.owlapi.model.OWLOntology
import org.semanticweb.owlapi.model.OWLOntologyManager
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom
import org.semanticweb.owlapi.vocab.OWL2Datatype

/**
 * OWL API utility.
 * @author John Guerson
 */

class OwlUtility {

    OWLOntologyManager owlManager
    OWLDataFactory owlDataFactory
    OWLOntology owlOntology

    public OwlUtility(OWLOntologyManager manager, OWLDataFactory dataFactory, OWLOntology ontology) {
        this.owlManager = manager
        this.owlDataFactory = dataFactory
        this.owlOntology = ontology
    }

    void setDomain(OWLClass owlClass, OWLObjectProperty owlProperty) {
        owlManager.applyChange(new AddAxiom(owlOntology, owlDataFactory.getOWLObjectPropertyDomainAxiom(owlProperty, owlClass)))
    }

    void setRange(OWLClass owlClass, OWLObjectProperty owlProperty) {
        owlManager.applyChange(new AddAxiom(owlOntology, owlDataFactory.getOWLObjectPropertyRangeAxiom(owlProperty, owlClass)))
    }

    void setInverse(OWLObjectProperty inverseProperty, OWLObjectProperty owlProperty) {
        owlManager.applyChange(new AddAxiom(owlOntology, owlDataFactory.getOWLInverseObjectPropertiesAxiom(owlProperty, inverseProperty)));
    }

    OWLAxiom createRDFLabel(OWLNamedObject owlObj, String name) {
        OWLAnnotation annotation = owlDataFactory.getOWLAnnotation(owlDataFactory.getRDFSLabel(), owlDataFactory.getOWLLiteral(name))
        OWLAxiom annotationAsAxiom = owlDataFactory.getOWLAnnotationAssertionAxiom(owlObj.getIRI(), annotation)
        owlManager.applyChange(new AddAxiom(owlOntology, annotationAsAxiom))
        return annotationAsAxiom
    }

    OWLAxiom createSubClassOf(OWLClass specific, OWLClass general) {
        OWLAxiom axiom = owlDataFactory.getOWLSubClassOfAxiom(specific, general)
        owlManager.applyChange(new AddAxiom(owlOntology, axiom))
        return axiom
    }

    OWLObjectProperty createObjectProperty(String owlNamespace, String name) {
        OWLObjectProperty property = owlDataFactory.getOWLObjectProperty(owlNamespace + name)
        OWLDeclarationAxiom axiom = owlDataFactory.getOWLDeclarationAxiom(property)
        owlManager.addAxiom(owlOntology, axiom)
        return property
    }

    OWLObjectProperty createObjectProperty(String owlNamespace, String name, String labelName, OWLClass domain, OWLClass range) {
        OWLObjectProperty objectProperty = createObjectProperty(owlNamespace, name)
        if (labelName != null) createRDFLabel(objectProperty, labelName);
        if (domain != null) setDomain(domain, objectProperty)
        if (range != null) setRange(range, objectProperty)
        return objectProperty
    }

    OWLAnnotation createAnnotation(String text){
        def namespace = owlOntology.getOntologyID().getOntologyIRI().get().getNamespace()
        OWLAnnotation annotation = owlDataFactory.getOWLAnnotation(owlDataFactory.getRDFSComment(),  owlDataFactory.getOWLLiteral(text, "pt"));
        OWLAxiom ax = owlDataFactory.getOWLAnnotationAssertionAxiom(IRI.create(namespace.substring(0,namespace.length()-1)), annotation);
        owlManager.applyChange(new AddAxiom(owlOntology, ax));
    }

    OWLAnnotation createAnnotation (OWLClass owlClass, String text){
        OWLAnnotation annotation = owlDataFactory.getOWLAnnotation(owlDataFactory.getRDFSComment(), owlDataFactory.getOWLLiteral(text, "pt"));
        OWLAxiom axiom = owlDataFactory.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
        owlManager.applyChange(new AddAxiom (owlOntology, axiom));
        return OWLAnnotation
    }

    OWLAxiom createSubPropertyOf(OWLObjectProperty specificProperty, OWLObjectProperty generalProperty){
        OWLSubObjectPropertyOfAxiom axiom = owlDataFactory.getOWLSubObjectPropertyOfAxiom(specificProperty,generalProperty)
        owlManager.applyChange(new AddAxiom(owlOntology, axiom))
        return axiom
    }

    OWLClass createClass(String owlNamespace, String name){
        OWLClass owlClass = owlDataFactory.getOWLClass(owlNamespace + name)
        OWLDeclarationAxiom classAsAxiom = owlDataFactory.getOWLDeclarationAxiom(owlClass)
        owlManager.addAxiom(owlOntology, classAsAxiom)
        return owlClass
    }

    OWLAxiom createMultiplicity(OWLObjectProperty property, OWLClass domain, OWLClass range, int lower, int upper){
        OWLAxiom axiom
        if(upper == lower){ //x..x
            OWLObjectExactCardinality exact = owlDataFactory.getOWLObjectExactCardinality(lower, property, range);
            axiom = owlDataFactory.getOWLEquivalentClassesAxiom(domain, exact);
        }else if(upper == -1 && lower == 1){ //1..*
            OWLObjectSomeValuesFrom some = owlDataFactory.getOWLObjectSomeValuesFrom(property, range);
            axiom = owlDataFactory.getOWLEquivalentClassesAxiom(domain, some);
        }else if (upper != -1 && lower == 0){ //0..*
            OWLObjectMaxCardinality max = owlDataFactory.getOWLObjectMaxCardinality(upper, property,range);
            axiom = owlDataFactory.getOWLSubClassOfAxiom(domain, max);
        }else if(upper == -1 && lower != 0){ //x..*
            OWLObjectMinCardinality min = owlDataFactory.getOWLObjectMinCardinality(lower, property,range);
            axiom = owlDataFactory.getOWLEquivalentClassesAxiom(domain, min);
        }else if(upper != -1 && lower > 0){ //x..n
            OWLObjectMaxCardinality max = owlDataFactory.getOWLObjectMaxCardinality(upper, property,range);
            OWLObjectMinCardinality min = owlDataFactory.getOWLObjectMinCardinality(lower, property,range);
            OWLObjectIntersectionOf intersection =  owlDataFactory.getOWLObjectIntersectionOf(max,min);
            axiom = owlDataFactory.getOWLEquivalentClassesAxiom(domain, intersection);
        }
        if(axiom != null) owlManager.applyChange(new AddAxiom(owlOntology, axiom));
        return axiom
    }

    private OWL2Datatype getOwlPrimitive(String primitiveName){
        if (primitiveName.equalsIgnoreCase("unsigned_int") || primitiveName.equalsIgnoreCase("unsignedInt")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_UNSIGNED_INT.getIRI());
        }else if(primitiveName.equalsIgnoreCase("int") || primitiveName.equalsIgnoreCase("integer") || primitiveName.equalsIgnoreCase("IntegerIntervalDimension") || primitiveName.equalsIgnoreCase("IntegerOrdinalDimension") || primitiveName.equalsIgnoreCase("IntegerRationalDimension")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_INTEGER.getIRI());
        }else if(primitiveName.equalsIgnoreCase("unsigned_byte") || primitiveName.equalsIgnoreCase("unsignedByte")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_UNSIGNED_BYTE.getIRI());
        }else if(primitiveName.equalsIgnoreCase("double")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_DOUBLE.getIRI());
        }else if(primitiveName.equalsIgnoreCase("string") || primitiveName.equalsIgnoreCase("NominalQuality")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_STRING.getIRI());
        }else if(primitiveName.equalsIgnoreCase("normalized_string") || primitiveName.equalsIgnoreCase("normalizedString")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_NORMALIZED_STRING.getIRI());
        }else if(primitiveName.equalsIgnoreCase("boolean")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_BOOLEAN.getIRI());
        }else if(primitiveName.equalsIgnoreCase("hex_binary") || primitiveName.equalsIgnoreCase("hexBinary")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_HEX_BINARY.getIRI());
        }else if(primitiveName.equalsIgnoreCase("short")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_SHORT.getIRI());
        }else if(primitiveName.equalsIgnoreCase("byte")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_BYTE.getIRI());
        }else if(primitiveName.equalsIgnoreCase("unsigned_long") || primitiveName.equalsIgnoreCase("unsignedLong")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_UNSIGNED_LONG.getIRI());
        }else if(primitiveName.equalsIgnoreCase("anyURI")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_ANY_URI.getIRI());
        }else if(primitiveName.equalsIgnoreCase("base64Binary")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_BASE_64_BINARY.getIRI());
        }else if(primitiveName.equalsIgnoreCase("date")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_DATE_TIME.getIRI());
        }else if(primitiveName.equalsIgnoreCase("dateTime")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_DATE_TIME_STAMP.getIRI());
        }else if(primitiveName.equalsIgnoreCase("decimal") || primitiveName.equalsIgnoreCase("DecimalIntervalDimension") || primitiveName.equalsIgnoreCase("DecimalOrdinalDimension") || primitiveName.equalsIgnoreCase("DecimalRationalDimension")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_DECIMAL.getIRI());
        }else if(primitiveName.equalsIgnoreCase("Name")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_NAME.getIRI());
        }else if(primitiveName.equalsIgnoreCase("NCName")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_NCNAME.getIRI());
        }else if(primitiveName.equalsIgnoreCase("nonPositiveInteger")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_NON_POSITIVE_INTEGER.getIRI());
        }else if(primitiveName.equalsIgnoreCase("nonNegativeInteger")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_NON_NEGATIVE_INTEGER.getIRI());
        }else if(primitiveName.equalsIgnoreCase("unsignedShort")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_UNSIGNED_SHORT.getIRI());
        }else if(primitiveName.equalsIgnoreCase("negativeInteger")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_NEGATIVE_INTEGER.getIRI());
        }else if(primitiveName.equalsIgnoreCase("positiveInteger")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_POSITIVE_INTEGER.getIRI());
        }else if(primitiveName.equalsIgnoreCase("language")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_LANGUAGE.getIRI());
        }else if(primitiveName.equalsIgnoreCase("long")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_LONG.getIRI());
        }else if(primitiveName.equalsIgnoreCase("float")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_FLOAT.getIRI());
        }else if(primitiveName.equalsIgnoreCase("token")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_TOKEN.getIRI());
        }else if(primitiveName.equalsIgnoreCase("NMTOKEN")){
            return owlDataFactory.getOWLDatatype(OWL2Datatype.XSD_NMTOKEN.getIRI());
        }
        return owlDataFactory.getOWLDatatype(OWL2Datatype.RDFS_LITERAL.getIRI());
    }
}
