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
import net.menthor.ontouml.rules.types.cardinality.CharacterizationSourceCardinalityRule
import net.menthor.ontouml.rules.types.cardinality.CharacterizationTargetCardinalityRule
import net.menthor.ontouml.rules.types.cardinality.DerivationSourceCardinalityRule
import net.menthor.ontouml.rules.types.cardinality.DerivationTargetCardinalityRule
import net.menthor.ontouml.rules.types.cardinality.InstanceOfTargetCardinalityRule
import net.menthor.ontouml.rules.types.cardinality.MediationTargetCardinalityRule
import net.menthor.ontouml.rules.types.cardinality.QuaPartOfWholeCardinalityRule
import net.menthor.ontouml.rules.types.cardinality.StructurationTargetCardinalityRule
import net.menthor.ontouml.rules.types.cardinality.SubCollectionPartCardinalityRule
import net.menthor.ontouml.rules.types.cardinality.SubQuantityPartCardinalityRule
import net.menthor.ontouml.rules.types.cardinality.SubQuantityWholeCardinalityRule
import net.menthor.ontouml.rules.types.cardinality.TruthMakerCardinalityEndsRule
import net.menthor.ontouml.rules.types.cardinality.WeakSupplementationRule

/**
 * @author John Guerson
 */
class CardinalityRules {

    static name = "Cardinality"

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

    private static List initClassRules() {
        def list = []
        list += new TruthMakerCardinalityEndsRule(null)
        list += new WeakSupplementationRule(null)
        return list - null
    }

    private static List initRelationshipRules(){
        def list = []
        list += new MediationTargetCardinalityRule(null)
        list += new CharacterizationSourceCardinalityRule(null)
        list += new CharacterizationTargetCardinalityRule(null)
        list += new InstanceOfTargetCardinalityRule(null)
        list += new SubCollectionPartCardinalityRule(null)
        list += new SubQuantityWholeCardinalityRule(null)
        list += new SubQuantityPartCardinalityRule(null)
        list += new StructurationTargetCardinalityRule(null)
        list += new DerivationSourceCardinalityRule(null)
        list += new DerivationTargetCardinalityRule(null)
        list += new QuaPartOfWholeCardinalityRule(null)
        return list - null
    }
}
