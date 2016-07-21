package net.menthor.ontouml.rules

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
import net.menthor.ontouml.rules.types.attribute.CausationDependencyRule
import net.menthor.ontouml.rules.types.attribute.CharacterizationDependencyRule
import net.menthor.ontouml.rules.types.attribute.CollectionWithEssentialPartsRule
import net.menthor.ontouml.rules.types.attribute.DerivationDependencyRule
import net.menthor.ontouml.rules.types.attribute.InstanceOfDependencyRule
import net.menthor.ontouml.rules.types.attribute.MediationDependencyRule
import net.menthor.ontouml.rules.types.attribute.MemberOfAndExtensionalWholeRule
import net.menthor.ontouml.rules.types.attribute.MixinsAbstractRule
import net.menthor.ontouml.rules.types.attribute.ParticipationDependencyRule
import net.menthor.ontouml.rules.types.attribute.QuaPartOfEssentialPartRule
import net.menthor.ontouml.rules.types.attribute.QuaPartOfImmutablePartRule
import net.menthor.ontouml.rules.types.attribute.QuaPartOfImmutableWholeRule
import net.menthor.ontouml.rules.types.attribute.QuaPartOfInseparablePartRule
import net.menthor.ontouml.rules.types.attribute.QuaPartOfNonShareableRule
import net.menthor.ontouml.rules.types.attribute.SubEventDependencyRule
import net.menthor.ontouml.rules.types.attribute.SubEventOfEssentialPartRule
import net.menthor.ontouml.rules.types.attribute.SubEventOfImmutablePartRule
import net.menthor.ontouml.rules.types.attribute.SubEventOfImmutableWholeRule
import net.menthor.ontouml.rules.types.attribute.SubEventOfInseparablePartRule
import net.menthor.ontouml.rules.types.attribute.SubQuantityOfEssentialPartRule
import net.menthor.ontouml.rules.types.attribute.SubQuantityOfImmutablePartRule
import net.menthor.ontouml.rules.types.attribute.SubQuantityOfNonShareableRule
import net.menthor.ontouml.rules.types.attribute.TemporalDependencyRule

/**
 * @author John Guerson
 */
class MetaAttributeRules {

    static name = "Meta-Attribute"

    static classRules = initClassRules()
    static relationshipRules = initRelationshipRules()

    static getRules(){
        def result = []
        result.addAll(classRules)
        result.addAll(relationshipRules)
        return result
    }

    static List<SyntacticalError> check(MClassifier self){
        def errors = []
        set(self)
        def rules = getRules()
        rules.each{ rule ->
            if((rule as SyntacticalRule).isRuleActived()) {
                errors += (rule as SyntacticalRule).check()
            }
        }
        return errors-null
    }

    private static set(self){
        if(self instanceof OntoUMLClass) classRules.each{ c -> (c as SyntacticalRule).self = self as OntoUMLClass }
        if(self instanceof OntoUMLRelationship) relationshipRules.each{ d -> (d as SyntacticalRule).self = self as OntoUMLRelationship }
    }

    private static List initClassRules(){
        def list = []
        list += new MixinsAbstractRule(null)
        list += new CollectionWithEssentialPartsRule(null)
        return list - null
    }

    static List initRelationshipRules(){
        def list = []
        list += new MemberOfAndExtensionalWholeRule(null)
        list += new DerivationDependencyRule(null)
        list += new TemporalDependencyRule(null)
        list += new CausationDependencyRule(null)
        list += new SubEventDependencyRule(null)
        list += new MediationDependencyRule(null)
        list += new CharacterizationDependencyRule(null)
        list += new ParticipationDependencyRule(null)
        list += new InstanceOfDependencyRule(null)
        list += new SubQuantityOfNonShareableRule(null)
        list += new QuaPartOfNonShareableRule(null)
        list += new SubQuantityOfEssentialPartRule(null)
        list += new SubQuantityOfImmutablePartRule(null)
        list += new SubEventOfEssentialPartRule(null)
        list += new SubEventOfImmutablePartRule(null)
        list += new QuaPartOfEssentialPartRule(null)
        list += new QuaPartOfImmutablePartRule(null)
        list += new SubEventOfInseparablePartRule(null)
        list += new SubEventOfImmutableWholeRule(null)
        list += new QuaPartOfInseparablePartRule(null)
        list += new QuaPartOfImmutableWholeRule(null)
        return list - null
    }
}
