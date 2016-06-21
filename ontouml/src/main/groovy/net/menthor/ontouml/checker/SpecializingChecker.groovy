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
import net.menthor.core.traits.MClassifier
import net.menthor.ontouml.OntoUMLClass
import net.menthor.ontouml.OntoUMLDataType
import net.menthor.ontouml.rule.SyntacticalRule
import net.menthor.ontouml.rule.specialization.AntiRigidProviderRule
import net.menthor.ontouml.rule.specialization.DataTypeAncestorsRule
import net.menthor.ontouml.rule.specialization.DimensionAncestorsRule
import net.menthor.ontouml.rule.specialization.DomainAncestorsRule
import net.menthor.ontouml.rule.specialization.EndurantAncestorsRule
import net.menthor.ontouml.rule.specialization.EnumAncestorsRule
import net.menthor.ontouml.rule.specialization.EventAncestorsRule
import net.menthor.ontouml.rule.specialization.HighOrderAncestorsRule
import net.menthor.ontouml.rule.specialization.IdentityProvidersRule
import net.menthor.ontouml.rule.specialization.MixinAncestorsRule
import net.menthor.ontouml.rule.specialization.PhaseMustNotSpecializeRule
import net.menthor.ontouml.rule.specialization.PhasePartitionRule
import net.menthor.ontouml.rule.specialization.RigidityAncestorsRule
import net.menthor.ontouml.rule.specialization.RoleMixinMustNotSpecializeRule
import net.menthor.ontouml.rule.specialization.RoleMustNotSpecializeRule
import net.menthor.ontouml.rule.specialization.SubKindProviderRule
import net.menthor.ontouml.rule.SyntacticalError

/**
 * @author John Guerson
 */
class SpecializingChecker {

    static List<SyntacticalError> check(MClassifier self){
        def rules = []
        def errors = []
        if(self instanceof OntoUMLClass) rules += getRules(self as OntoUMLClass)
        if(self instanceof OntoUMLDataType) rules += getRules(self as OntoUMLDataType)
        rules.each{ rule ->
            if((rule as SyntacticalRule).isRuleActived()) {
                errors += (rule as SyntacticalRule).check()
            }
        }
        return errors - null
    }

    static List<SyntacticalRule> getRules(OntoUMLClass self){
        def list = []
        list += new RigidityAncestorsRule(self)
        list += new MixinAncestorsRule(self)
        list += new RoleMixinMustNotSpecializeRule(self)
        list += new EventAncestorsRule(self)
        list += new HighOrderAncestorsRule(self)
        list += new EndurantAncestorsRule(self)
        list += new SubKindProviderRule(self)
        list += new AntiRigidProviderRule(self)
        list += new RoleMustNotSpecializeRule(self)
        list += new PhaseMustNotSpecializeRule(self)
        list += new IdentityProvidersRule(self)
        list += new PhasePartitionRule(self)
        return list - null
    }

    static List<SyntacticalRule> getRules(OntoUMLDataType self){
        def list = []
        list += new DataTypeAncestorsRule(self)
        list += new DimensionAncestorsRule(self)
        list += new DomainAncestorsRule(self)
        list += new EnumAncestorsRule(self)
        return list - null
    }
}
