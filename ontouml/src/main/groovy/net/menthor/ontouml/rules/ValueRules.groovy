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

import net.menthor.ontouml.OntoUMLRelationship
import net.menthor.core.traits.MClassifier
import net.menthor.ontouml.rules.types.value.CausationValuesRule
import net.menthor.ontouml.rules.types.value.CharacterizationValuesRule
import net.menthor.ontouml.rules.types.value.ComponentOfValuesRule
import net.menthor.ontouml.rules.types.value.ConstitutionValuesRule
import net.menthor.ontouml.rules.types.value.DuringValuesRule
import net.menthor.ontouml.rules.types.value.EqualsValuesRule
import net.menthor.ontouml.rules.types.value.FinishesValuesRule
import net.menthor.ontouml.rules.types.value.MediationValuesRule
import net.menthor.ontouml.rules.types.value.MeetsValuesRule
import net.menthor.ontouml.rules.types.value.MemberOfValuesRule
import net.menthor.ontouml.rules.types.value.MeronymicValuesRule
import net.menthor.ontouml.rules.types.value.OverlapsValuesRule
import net.menthor.ontouml.rules.types.value.PrecedesValuesRule
import net.menthor.ontouml.rules.types.value.StartsValuesRule
import net.menthor.ontouml.rules.types.value.SubCollectionOfValuesRule
import net.menthor.ontouml.rules.types.value.SubEventValuesRule
import net.menthor.ontouml.rules.types.value.SubQuantityOfValuesRule
/**
 * @author John Guerson
 */
class ValueRules {

    static name = "Values"

    static relationshipRules = initRelationshipRules()

    static getRules(){
        def result = []
        result.addAll(relationshipRules)
        return result
    }

    static List<SyntacticalError> check(MClassifier self){
        def errors = []
        set(self)
        def rules = getRules()
        rules.each { rule ->
            if((rule as SyntacticalRule).isRuleActived()) {
                errors += (rule as SyntacticalRule).check()
            }
        }
        return errors - null
    }

    private static set(self){
        if(self instanceof OntoUMLRelationship) relationshipRules.each{ c -> (c as SyntacticalRule).self = self as OntoUMLRelationship }
    }

    private static List initRelationshipRules(){
        def list = []
        list += new MediationValuesRule(null)
        list += new CharacterizationValuesRule(null)
        list += new CausationValuesRule(null)
        list += new MeronymicValuesRule(null)
        list += new MemberOfValuesRule(null)
        list += new ComponentOfValuesRule(null)
        list += new SubCollectionOfValuesRule(null)
        list += new SubQuantityOfValuesRule(null)
        list += new SubEventValuesRule(null)
        list += new ConstitutionValuesRule(null)
        list += new PrecedesValuesRule(null)
        list += new DuringValuesRule(null)
        list += new MeetsValuesRule(null)
        list += new FinishesValuesRule(null)
        list += new StartsValuesRule(null)
        list += new EqualsValuesRule(null)
        list += new OverlapsValuesRule(null)
        return list - null
    }
}
