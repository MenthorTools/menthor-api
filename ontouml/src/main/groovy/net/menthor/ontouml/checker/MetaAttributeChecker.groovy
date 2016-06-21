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
import net.menthor.ontouml.rule.SyntacticalRule
import net.menthor.ontouml.rule.attribute.CausationDependencyRule
import net.menthor.ontouml.rule.attribute.CharacterizationDependencyRule
import net.menthor.ontouml.rule.attribute.CollectionWithEssentialPartsRule
import net.menthor.ontouml.rule.attribute.DerivationDependencyRule
import net.menthor.ontouml.rule.attribute.InstanceOfDependencyRule
import net.menthor.ontouml.rule.attribute.MediationDependencyRule
import net.menthor.ontouml.rule.attribute.MemberOfAndExtensionalWholeRule
import net.menthor.ontouml.rule.attribute.MixinsAbstractRule
import net.menthor.ontouml.rule.attribute.ParticipationDependencyRule
import net.menthor.ontouml.rule.attribute.QuaPartOfEssentialPartRule
import net.menthor.ontouml.rule.attribute.QuaPartOfImmutablePartRule
import net.menthor.ontouml.rule.attribute.QuaPartOfImmutableWholeRule
import net.menthor.ontouml.rule.attribute.QuaPartOfInseparablePartRule
import net.menthor.ontouml.rule.attribute.QuaPartOfNonShareableRule
import net.menthor.ontouml.rule.attribute.SubEventDependencyRule
import net.menthor.ontouml.rule.attribute.SubEventOfEssentialPartRule
import net.menthor.ontouml.rule.attribute.SubEventOfImmutablePartRule
import net.menthor.ontouml.rule.attribute.SubEventOfImmutableWholeRule
import net.menthor.ontouml.rule.attribute.SubEventOfInseparablePartRule
import net.menthor.ontouml.rule.attribute.SubQuantityOfEssentialPartRule
import net.menthor.ontouml.rule.attribute.SubQuantityOfImmutablePartRule
import net.menthor.ontouml.rule.attribute.SubQuantityOfNonShareableRule
import net.menthor.ontouml.rule.attribute.TemporalDependencyRule
import net.menthor.ontouml.rule.SyntacticalError

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
            if((rule as SyntacticalRule).isRuleActived()) {
                errors += (rule as SyntacticalRule).check()
            }
        }
        return errors-null
    }

    static List<SyntacticalRule> getRules(OntoUMLClass self){
        def list = []
        list += new MixinsAbstractRule(self)
        list += new CollectionWithEssentialPartsRule(self)
        return list - null
    }

    static List<SyntacticalRule> getRules(OntoUMLRelationship self){
        def list = []
        list += new MemberOfAndExtensionalWholeRule(self)
        list += new DerivationDependencyRule(self)
        list += new TemporalDependencyRule(self)
        list += new CausationDependencyRule(self)
        list += new SubEventDependencyRule(self)
        list += new MediationDependencyRule(self)
        list += new CharacterizationDependencyRule(self)
        list += new ParticipationDependencyRule(self)
        list += new InstanceOfDependencyRule(self)
        list += new SubQuantityOfNonShareableRule(self)
        list += new QuaPartOfNonShareableRule(self)
        list += new SubQuantityOfEssentialPartRule(self)
        list += new SubQuantityOfImmutablePartRule(self)
        list += new SubEventOfEssentialPartRule(self)
        list += new SubEventOfImmutablePartRule(self)
        list += new QuaPartOfEssentialPartRule(self)
        list += new QuaPartOfImmutablePartRule(self)
        list += new SubEventOfInseparablePartRule(self)
        list += new SubEventOfImmutableWholeRule(self)
        list += new QuaPartOfInseparablePartRule(self)
        list += new QuaPartOfImmutableWholeRule(self)
        return list - null
    }
}
