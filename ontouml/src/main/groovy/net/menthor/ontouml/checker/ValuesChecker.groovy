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

import net.menthor.ontouml.OntoUMLRelationship
import net.menthor.core.traits.MClassifier
import net.menthor.ontouml.rule.GenericCondition
import net.menthor.ontouml.rule.SyntacticalRule
import net.menthor.ontouml.rule.value.CausationValuesRule
import net.menthor.ontouml.rule.value.CharacterizationValuesRule
import net.menthor.ontouml.rule.value.ComponentOfValuesRule
import net.menthor.ontouml.rule.value.ConstitutionValuesRule
import net.menthor.ontouml.rule.value.DuringValuesRule
import net.menthor.ontouml.rule.value.EqualsValuesRule
import net.menthor.ontouml.rule.value.FinishesValuesRule
import net.menthor.ontouml.rule.value.MediationValuesRule
import net.menthor.ontouml.rule.value.MeetsValuesRule
import net.menthor.ontouml.rule.value.MemberOfValuesRule
import net.menthor.ontouml.rule.value.MeronymicValuesRule
import net.menthor.ontouml.rule.value.OverlapsValuesRule
import net.menthor.ontouml.rule.value.PrecedesValuesRule
import net.menthor.ontouml.rule.value.StartsValuesRule
import net.menthor.ontouml.rule.value.SubCollectionOfValuesRule
import net.menthor.ontouml.rule.value.SubEventValuesRule
import net.menthor.ontouml.rule.value.SubQuantityOfValuesRule
import net.menthor.ontouml.rule.SyntacticalError

/**
 * @author John Guerson
 */
class ValuesChecker extends GenericCondition {

    static List<SyntacticalError> check(MClassifier self){
        def rules = []
        def errors = []
        if(self instanceof OntoUMLRelationship) rules += getRules(self as OntoUMLRelationship)
        rules.each { rule ->
            if((rule as SyntacticalRule).isRuleActived()) {
                errors += (rule as SyntacticalRule).check()
            }
        }
        return errors - null
    }

    static List<SyntacticalRule> getRules(OntoUMLRelationship self){
        def list = []
        list += new MediationValuesRule(self)
        list += new CharacterizationValuesRule(self)
        list += new CausationValuesRule(self)
        list += new MeronymicValuesRule(self)
        list += new MemberOfValuesRule(self)
        list += new ComponentOfValuesRule(self)
        list += new SubCollectionOfValuesRule(self)
        list += new SubQuantityOfValuesRule(self)
        list += new SubEventValuesRule(self)
        list += new ConstitutionValuesRule(self)
        list += new PrecedesValuesRule(self)
        list += new DuringValuesRule(self)
        list += new MeetsValuesRule(self)
        list += new FinishesValuesRule(self)
        list += new StartsValuesRule(self)
        list += new EqualsValuesRule(self)
        list += new OverlapsValuesRule(self)
        return list - null
    }
}
