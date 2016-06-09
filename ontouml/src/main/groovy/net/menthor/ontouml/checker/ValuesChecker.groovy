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
import net.menthor.ontouml.rules.GenericCondition
import net.menthor.ontouml.rules.SyntacticalRule
import net.menthor.ontouml.rules.value.CausationValues
import net.menthor.ontouml.rules.value.CharacterizationValues
import net.menthor.ontouml.rules.value.ComponentOfValues
import net.menthor.ontouml.rules.value.ConstitutionValues
import net.menthor.ontouml.rules.value.DuringValues
import net.menthor.ontouml.rules.value.EqualsValues
import net.menthor.ontouml.rules.value.FinishesValues
import net.menthor.ontouml.rules.value.MediationValues
import net.menthor.ontouml.rules.value.MeetsValues
import net.menthor.ontouml.rules.value.MemberOfValues
import net.menthor.ontouml.rules.value.MeronymicValues
import net.menthor.ontouml.rules.value.OverlapsValues
import net.menthor.ontouml.rules.value.PrecedesValues
import net.menthor.ontouml.rules.value.StartsValues
import net.menthor.ontouml.rules.value.SubCollectionOfValues
import net.menthor.ontouml.rules.value.SubEventValues
import net.menthor.ontouml.rules.value.SubQuantityOfValues

/**
 * @author John Guerson
 */
class ValuesChecker extends GenericCondition {

    static List<SyntacticalError> check(MClassifier self){
        def rules = []
        def errors = []
        if(self instanceof OntoUMLRelationship) rules += getRules(self as OntoUMLRelationship)
        rules.each { rule ->
            errors += (rule as SyntacticalRule).check()
        }
        return errors - null
    }

    static List<SyntacticalRule> getRules(OntoUMLRelationship self){
        def list = []
        list += new MediationValues(self)
        list += new CharacterizationValues(self)
        list += new CausationValues(self)
        list += new MeronymicValues(self)
        list += new MemberOfValues(self)
        list += new ComponentOfValues(self)
        list += new SubCollectionOfValues(self)
        list += new SubQuantityOfValues(self)
        list += new SubEventValues(self)
        list += new ConstitutionValues(self)
        list += new PrecedesValues(self)
        list += new DuringValues(self)
        list += new MeetsValues(self)
        list += new FinishesValues(self)
        list += new StartsValues(self)
        list += new EqualsValues(self)
        list += new OverlapsValues(self)
        return list - null
    }
}
