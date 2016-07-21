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
import net.menthor.core.traits.MClassifier
import net.menthor.ontouml.OntoUMLClass
import net.menthor.ontouml.OntoUMLDataType
import net.menthor.ontouml.rules.types.specialization.AntiRigidProviderRule
import net.menthor.ontouml.rules.types.specialization.DataTypeAncestorsRule
import net.menthor.ontouml.rules.types.specialization.DimensionAncestorsRule
import net.menthor.ontouml.rules.types.specialization.DomainAncestorsRule
import net.menthor.ontouml.rules.types.specialization.EndurantAncestorsRule
import net.menthor.ontouml.rules.types.specialization.EnumAncestorsRule
import net.menthor.ontouml.rules.types.specialization.EventAncestorsRule
import net.menthor.ontouml.rules.types.specialization.HighOrderAncestorsRule
import net.menthor.ontouml.rules.types.specialization.IdentityProvidersRule
import net.menthor.ontouml.rules.types.specialization.MixinAncestorsRule
import net.menthor.ontouml.rules.types.specialization.PhaseMustNotSpecializeRule
import net.menthor.ontouml.rules.types.specialization.PhasePartitionRule
import net.menthor.ontouml.rules.types.specialization.RigidityAncestorsRule
import net.menthor.ontouml.rules.types.specialization.RoleMixinMustNotSpecializeRule
import net.menthor.ontouml.rules.types.specialization.RoleMustNotSpecializeRule
import net.menthor.ontouml.rules.types.specialization.SubKindProviderRule

/**
 * @author John Guerson
 */
class SpecializationRules {

    static name = "Specialization"

    static classRules = initClassRules()
    static datatypeRules = initDatatypeRules()

    static getRules(){
        def result = []
        result.addAll(classRules)
        result.addAll(datatypeRules)
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
        return errors - null
    }

    private static set(self){
        if(self instanceof OntoUMLClass) classRules.each{ c -> (c as SyntacticalRule).self = self as OntoUMLClass }
        if(self instanceof OntoUMLDataType) datatypeRules.each{ d -> (d as SyntacticalRule).self = self as OntoUMLDataType }
    }

    private static List initClassRules(){
        def list = []
        list += new RigidityAncestorsRule(null)
        list += new MixinAncestorsRule(null)
        list += new RoleMixinMustNotSpecializeRule(null)
        list += new EventAncestorsRule(null)
        list += new HighOrderAncestorsRule(null)
        list += new EndurantAncestorsRule(null)
        list += new SubKindProviderRule(null)
        list += new AntiRigidProviderRule(null)
        list += new RoleMustNotSpecializeRule(null)
        list += new PhaseMustNotSpecializeRule(null)
        list += new IdentityProvidersRule(null)
        list += new PhasePartitionRule(null)
        return list - null
    }

    private static List initDatatypeRules(){
        def list = []
        list += new DataTypeAncestorsRule(null)
        list += new DimensionAncestorsRule(null)
        list += new DomainAncestorsRule(null)
        list += new EnumAncestorsRule(null)
        return list - null
    }


}
