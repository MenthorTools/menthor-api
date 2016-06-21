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
import net.menthor.ontouml.rule.SyntacticalRule
import net.menthor.ontouml.rule.cardinality.CharacterizationSourceCardinalityRule
import net.menthor.ontouml.rule.cardinality.CharacterizationTargetCardinalityRule
import net.menthor.ontouml.rule.cardinality.DerivationSourceCardinalityRule
import net.menthor.ontouml.rule.cardinality.DerivationTargetCardinalityRule
import net.menthor.ontouml.rule.cardinality.InstanceOfTargetCardinalityRule
import net.menthor.ontouml.rule.cardinality.MediationTargetCardinalityRule
import net.menthor.ontouml.rule.cardinality.QuaPartOfWholeCardinalityRule
import net.menthor.ontouml.rule.cardinality.StructurationTargetCardinalityRule
import net.menthor.ontouml.rule.cardinality.SubCollectionPartCardinalityRule
import net.menthor.ontouml.rule.cardinality.SubQuantityPartCardinalityRule
import net.menthor.ontouml.rule.cardinality.SubQuantityWholeCardinalityRule
import net.menthor.ontouml.rule.cardinality.TruthMakerCardinalityEndsRule
import net.menthor.ontouml.rule.cardinality.WeakSupplementationRule
import net.menthor.core.traits.MClassifier
import net.menthor.ontouml.rule.SyntacticalError

/**
 * @author John Guerson
 */
class CardinalityChecker {

    static List<SyntacticalError> check(MClassifier self){
        def errors = []
        def rules = []
        if(self instanceof OntoUMLClass) rules += getRules(self as OntoUMLClass)
        if(self instanceof OntoUMLRelationship) rules += getRules(self as OntoUMLRelationship)
        rules.each{ rule ->
            if((rule as SyntacticalRule).isRuleActived()) {
                errors += (rule as SyntacticalRule).check()
            }
        }
        return errors-null
    }

    static List<SyntacticalRule> getRules(OntoUMLClass self) {
        def list = []
        list += new TruthMakerCardinalityEndsRule(self)
        list += new WeakSupplementationRule(self)
        return list - null
    }

    static List<SyntacticalRule> getRules(OntoUMLRelationship self){
        def list = []
        list += new MediationTargetCardinalityRule(self)
        list += new CharacterizationSourceCardinalityRule(self)
        list += new CharacterizationTargetCardinalityRule(self)
        list += new InstanceOfTargetCardinalityRule(self)
        list += new SubCollectionPartCardinalityRule(self)
        list += new SubQuantityWholeCardinalityRule(self)
        list += new SubQuantityPartCardinalityRule(self)
        list += new StructurationTargetCardinalityRule(self)
        list += new DerivationSourceCardinalityRule(self)
        list += new DerivationTargetCardinalityRule(self)
        list += new QuaPartOfWholeCardinalityRule(self)
        return list - null
    }
}
