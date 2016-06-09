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
import net.menthor.ontouml.rules.SyntacticalRule
import net.menthor.ontouml.rules.specialization.AntiRigidProvider
import net.menthor.ontouml.rules.specialization.DataTypeAncestors
import net.menthor.ontouml.rules.specialization.DimensionAncestors
import net.menthor.ontouml.rules.specialization.DomainAncestors
import net.menthor.ontouml.rules.specialization.EndurantAncestors
import net.menthor.ontouml.rules.specialization.EnumAncestors
import net.menthor.ontouml.rules.specialization.EventAncestors
import net.menthor.ontouml.rules.specialization.HighOrderAncestors
import net.menthor.ontouml.rules.specialization.IdentityProviders
import net.menthor.ontouml.rules.specialization.MixinAncestors
import net.menthor.ontouml.rules.specialization.PhaseMustNotSpecialize
import net.menthor.ontouml.rules.specialization.PhasePartition
import net.menthor.ontouml.rules.specialization.RigidityAncestors
import net.menthor.ontouml.rules.specialization.RoleMixinMustNotSpecialize
import net.menthor.ontouml.rules.specialization.RoleMustNotSpecialize
import net.menthor.ontouml.rules.specialization.SubKindProvider

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
            errors += (rule as SyntacticalRule).check()
        }
        return errors - null
    }

    static List<SyntacticalRule> getRules(OntoUMLClass self){
        def list = []
        list += new RigidityAncestors(self)
        list += new MixinAncestors(self)
        list += new RoleMixinMustNotSpecialize(self)
        list += new EventAncestors(self)
        list += new HighOrderAncestors(self)
        list += new EndurantAncestors(self)
        list += new SubKindProvider(self)
        list += new AntiRigidProvider(self)
        list += new RoleMustNotSpecialize(self)
        list += new PhaseMustNotSpecialize(self)
        list += new IdentityProviders(self)
        list += new PhasePartition(self)
        return list - null
    }

    static List<SyntacticalRule> getRules(OntoUMLDataType self){
        def list = []
        list += new DataTypeAncestors(self)
        list += new DimensionAncestors(self)
        list += new DomainAncestors(self)
        list += new EnumAncestors(self)
        return list - null
    }
}
