package net.menthor.ontouml2owl

import net.menthor.ontouml.OntoUMLAttribute
import net.menthor.ontouml.OntoUMLDataType
import net.menthor.ontouml.OntoUMLGeneralizationSet
import net.menthor.ontouml.stereotypes.PrimitiveStereotype
import net.menthor.ontouml2owl.choice.GeneralizationSetChoice
import net.menthor.ontouml2owl.choice.ReasonerChoice
import org.semanticweb.owlapi.vocab.OWL2Datatype

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
 * Owl options.
 * @author John Guerson
 */
class OwlOptions {

    String namespace ="http://www.menthor.net/"

    boolean useSkeleton =false
    boolean useRDFLabel=true
    boolean useDomain=true
    boolean useRange=true
    boolean useInverse=true
    boolean useMultiplicity=true
    boolean useDisjointnessOfRelations=true
    boolean useDisjointnessOfClasses=true
    boolean useFunctionalRelations=true
    boolean useInverseFunctionalRelations=true
    boolean useSymmetricRelations=true
    boolean useAssymetricRelations=true
    boolean useTransitiveRelations=true
    boolean useReflexiveRelations=true
    boolean useIrreflexiveRelations=true
    boolean useComments=true

    ReasonerChoice reasoner = ReasonerChoice.HERMIT
    Map<OntoUMLGeneralizationSet, GeneralizationSetChoice> genSetMappings = [:]

    Map<PrimitiveStereotype, OWL2Datatype> primitiveMappping = [:]
    Map<OntoUMLDataType, OWL2Datatype> dataTypeMappings = [:]
    Map<OntoUMLAttribute, OWL2Datatype> attributeMapppings = [:]
}
