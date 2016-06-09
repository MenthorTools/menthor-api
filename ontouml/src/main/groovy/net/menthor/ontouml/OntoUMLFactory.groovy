package net.menthor.ontouml

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

import net.menthor.ontouml.stereotypes.ClassStereotype
import net.menthor.ontouml.stereotypes.ConstraintStereotype
import net.menthor.ontouml.stereotypes.DataTypeStereotype
import net.menthor.ontouml.stereotypes.PrimitiveStereotype
import net.menthor.ontouml.stereotypes.RelationshipStereotype
import net.menthor.core.traits.MContainer
import net.menthor.core.traits.MNamedElement
import net.menthor.core.traits.MType
import net.menthor.core.traits.MClassifier

/**
 * OntoUML factory.
 * @author John Guerson
 */
class OntoUMLFactory {

    private OntoUMLFactory(){}

    static OntoUMLPackage createModel(String name) {
        OntoUMLModel model = new OntoUMLModel()
        model.setName(name)
        return model
    }

    static OntoUMLPackage createPackage(String name, MContainer container){
        OntoUMLPackage p = new OntoUMLPackage()
        p.setName(name)
        p.setContainer(container)
        return p
    }

    static OntoUMLGeneralization createGeneralization (MClassifier specific, MClassifier general, MContainer container){
        OntoUMLGeneralization g = new OntoUMLGeneralization()
        g.setGeneral(general)
        g.setSpecific(specific)
        g.setContainer(container)
        return g;
    }

    static OntoUMLGeneralizationSet createGeneralizationSet(String name, boolean isCovering, boolean isDisjoint, MContainer container){
        OntoUMLGeneralizationSet gs = new OntoUMLGeneralizationSet()
        gs.setIsCovering(isCovering)
        gs.setIsDisjoint(isDisjoint)
        gs.setContainer(container)
        gs.setName(name)
        return gs
    }

    static OntoUMLGeneralizationSet createGeneralizationSet(String name, boolean isCovering, boolean isDisjoint, List generalizations, MContainer container) {
        OntoUMLGeneralizationSet gs = createGeneralizationSet(name, isCovering,isDisjoint,container);
        gs.setGeneralizations(generalizations)
        return gs;
    }

    static OntoUMLGeneralizationSet createPartition(String name, List<MClassifier> specifics, MClassifier general, MContainer container){
        OntoUMLGeneralizationSet gs = createGeneralizationSet(name, true,true,container)
        specifics.each{ spec ->
            OntoUMLGeneralization g = createGeneralization(spec, general, container)
            g.setGeneralizationSet(gs)
        }
        return gs;
    }

    static OntoUMLRelationship createRelationship(RelationshipStereotype stereotype, String name, MContainer container) {
        OntoUMLRelationship rel = new OntoUMLRelationship()
        rel.setStereotype(stereotype)
        rel.setDefaultEndPoints(2)
        rel.setName(name)
        rel.setContainer(container)
        return rel
    }

    static OntoUMLRelationship createRelationship(RelationshipStereotype stereotype, MClassifier source, MClassifier target, MContainer container){
        OntoUMLRelationship rel = createRelationship(stereotype,"",container)
        rel.setDefaultReflexivityValue()
        rel.setDefaultSymmetryValue()
        rel.setDefaultTransitivityValue()
        rel.setDefaultCyclicityValue()
        rel.getEndPoints().get(0).setClassifier(source)
        rel.getEndPoints().get(0).setDefaultName()
        rel.getEndPoints().get(1).setClassifier(target)
        rel.getEndPoints().get(1).setDefaultName()
        return rel
    }

    static OntoUMLRelationship createRelationship(RelationshipStereotype stereotype, MClassifier source, int srcLower, int srcUpper, String name,
                                                  MClassifier target, int tgtLower, int tgtUpper, MContainer container) {
        OntoUMLRelationship rel = createRelationship(stereotype, source, target, container)
        rel.setName(name)
        rel.sourceEndPoint().setCardinalities(srcLower, srcUpper)
        rel.targetEndPoint().setCardinalities(tgtLower, tgtUpper)
        return rel
    }

    static OntoUMLClass createClass(ClassStereotype stereotype, String name, MContainer container){
        OntoUMLClass c = new OntoUMLClass()
        c.setName(name)
        c.setContainer(container)
        c.setStereotype(stereotype)
        if(c.isMixinClass()) {
            c.setIsAbstract(true)
        }
        return c
    }

    static OntoUMLDataType createDataType(DataTypeStereotype stereotype, String name, MContainer container){
        OntoUMLDataType dt = new OntoUMLDataType()
        dt.setName(name)
        dt.setContainer(container)
        dt.setStereotype(stereotype)
        return dt;
    }

    static OntoUMLLiteral createLiteral(String text){
        OntoUMLLiteral lit = new OntoUMLLiteral()
        lit.setText(text)
        return lit
    }

    static List<OntoUMLLiteral> createLiterals(Collection<String> textValues){
        List<OntoUMLLiteral> result = []
        textValues.each{ v ->
            result.add(createLiteral(v))
        }
        return result;
    }

    static OntoUMLDataType createEnumeration(String name, Collection<String> textValues, MContainer container){
        OntoUMLDataType enu = createDataType(DataTypeStereotype.ENUMERATION,name,container);
        List<OntoUMLLiteral> literals = createLiterals(textValues)
        enu.setLiterals(literals)
        return enu;
    }

    static OntoUMLAttribute createAttribute (MType owner, PrimitiveStereotype primitive, int lower, int upper){
        OntoUMLAttribute attribute = new OntoUMLAttribute()
        attribute.setStereotype(primitive)
        attribute.setOwner(owner)
        attribute.setLowerBound(lower)
        attribute.setUpperBound(upper)
        attribute.setName("")
        owner.setAttribute(attribute)
        return attribute
    }

    static OntoUMLAttribute createAttribute (MType owner, PrimitiveStereotype primitive, int lower, int upper, String name, boolean isDerived, boolean isDependency){
        OntoUMLAttribute attribute = createAttribute(owner, primitive, lower, upper)
        attribute.setName(name)
        attribute.setDerived(isDerived)
        attribute.setDependency(isDependency)
        return attribute;
    }

    static OntoUMLAttribute createAttribute (MType owner, PrimitiveStereotype primitive){
        OntoUMLAttribute attribute = createAttribute(owner, primitive, 1, 1, "", false, false)
        return attribute
    }

    static OntoUMLEndPoint createEndPoint(OntoUMLRelationship rel, MClassifier classifier, int lower, int upper, String name){
        OntoUMLEndPoint ep = new OntoUMLEndPoint()
        ep.setClassifier(classifier)
        ep.setLowerBound(lower)
        ep.setUpperBound(upper)
        ep.setName(name)
        rel.setEndPoint(ep)
        return ep;
    }

    static OntoUMLEndPoint createEndPoint(OntoUMLRelationship rel, MClassifier classifier, int lower, int upper){
        return createEndPoint(rel, classifier, lower, upper, "");
    }

    static OntoUMLEndPoint createEndPoint (OntoUMLRelationship rel, MClassifier classifier, int lower, int upper, String name, boolean isDerived, boolean isDependency){
        OntoUMLEndPoint endpoint = createEndPoint(rel, classifier, lower, upper, name)
        endpoint.setDerived(isDerived)
        endpoint.setDependency(isDependency)
        return endpoint;
    }

    static OntoUMLConstraint createConstraint(MNamedElement context, ConstraintStereotype stereotype, String name, String condition, MContainer container){
        OntoUMLConstraint c =  OntoUMLConstraint()
        c.setContext(context)
        c.setName(name)
        c.setCondition(condition)
        c.setContainer(container)
        c.setStereotype(stereotype)
        return c
    }
}
