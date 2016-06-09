package net.menthor.ontouml.checker

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
import net.menthor.core.traits.MClassifier
import net.menthor.ontouml.rules.SyntacticalRule
import net.menthor.ontouml.rules.attribute.CausationDependency
import net.menthor.ontouml.rules.attribute.CharacterizationDependency
import net.menthor.ontouml.rules.attribute.CollectionWithEssentialParts
import net.menthor.ontouml.rules.attribute.DerivationDependency
import net.menthor.ontouml.rules.attribute.InstanceOfDependency
import net.menthor.ontouml.rules.attribute.MediationDependency
import net.menthor.ontouml.rules.attribute.MemberOfAndExtensionalWhole
import net.menthor.ontouml.rules.attribute.MixinsAbstract
import net.menthor.ontouml.rules.attribute.ParticipationDependency
import net.menthor.ontouml.rules.attribute.QuaPartOfEssentialPart
import net.menthor.ontouml.rules.attribute.QuaPartOfImmutablePart
import net.menthor.ontouml.rules.attribute.QuaPartOfImmutableWhole
import net.menthor.ontouml.rules.attribute.QuaPartOfInseparablePart
import net.menthor.ontouml.rules.attribute.QuaPartOfNonShareable
import net.menthor.ontouml.rules.attribute.SubEventDependency
import net.menthor.ontouml.rules.attribute.SubEventOfEssentialPart
import net.menthor.ontouml.rules.attribute.SubEventOfImmutablePart
import net.menthor.ontouml.rules.attribute.SubEventOfImmutableWhole
import net.menthor.ontouml.rules.attribute.SubEventOfInseparablePart
import net.menthor.ontouml.rules.attribute.SubQuantityOfEssentialPart
import net.menthor.ontouml.rules.attribute.SubQuantityOfImmutablePart
import net.menthor.ontouml.rules.attribute.SubQuantityOfNonShareable
import net.menthor.ontouml.rules.attribute.TemporalDependency

/**
 * @author John Guerson
 */
class MetaAttributeChecker {

    static List<SyntacticalError> check(MClassifier self){
        def rules = []
        def errors = []
        if(self instanceof OntoUMLClass) rules += getRules(self as OntoUMLClass)
        if(self instanceof OntoUMLRelationship) rules += getRules(self as OntoUMLRelationship)
        rules.each{ rule ->
            errors += (rule as SyntacticalRule).check()
        }
        return errors-null
    }

    static List<SyntacticalRule> getRules(OntoUMLClass self){
        def list = []
        list += new MixinsAbstract(self)
        list += new CollectionWithEssentialParts(self)
        return list - null
    }

    static List<SyntacticalRule> getRules(OntoUMLRelationship self){
        def list = []
        list += new MemberOfAndExtensionalWhole(self)
        list += new DerivationDependency(self)
        list += new TemporalDependency(self)
        list += new CausationDependency(self)
        list += new SubEventDependency(self)
        list += new MediationDependency(self)
        list += new CharacterizationDependency(self)
        list += new ParticipationDependency(self)
        list += new InstanceOfDependency(self)
        list += new SubQuantityOfNonShareable(self)
        list += new QuaPartOfNonShareable(self)
        list += new SubQuantityOfEssentialPart(self)
        list += new SubQuantityOfImmutablePart(self)
        list += new SubEventOfEssentialPart(self)
        list += new SubEventOfImmutablePart(self)
        list += new QuaPartOfEssentialPart(self)
        list += new QuaPartOfImmutablePart(self)
        list += new SubEventOfInseparablePart(self)
        list += new SubEventOfImmutableWhole(self)
        list += new QuaPartOfInseparablePart(self)
        list += new QuaPartOfImmutableWhole(self)
        return list - null
    }
}
