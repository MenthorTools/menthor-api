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
import net.menthor.ontouml.OntoUMLDataType
import net.menthor.ontouml.OntoUMLRelationship
import net.menthor.core.traits.MClassifier
import net.menthor.ontouml.rule.SyntacticalRule
import net.menthor.ontouml.rule.connection.CausationSourceAndTargetRule
import net.menthor.ontouml.rule.connection.CharacterizationSourceRule
import net.menthor.ontouml.rule.connection.CharacterizationTargetRule
import net.menthor.ontouml.rule.connection.ComponentOfPartRule
import net.menthor.ontouml.rule.connection.ComponentOfWholeRule
import net.menthor.ontouml.rule.connection.ConsitutionPartRule
import net.menthor.ontouml.rule.connection.ConsitutionWholeRule
import net.menthor.ontouml.rule.connection.DimensionConnectedToStructurationRule
import net.menthor.ontouml.rule.connection.DomainComposedDimensionsRule
import net.menthor.ontouml.rule.connection.DomainConnectedToStructurationRule
import net.menthor.ontouml.rule.connection.EnumComposedLiteralsRule
import net.menthor.ontouml.rule.connection.EnumConnectedToStructurationRule
import net.menthor.ontouml.rule.connection.EventConnectedToParticipationRule
import net.menthor.ontouml.rule.connection.HighOrderConnectedToInstanceOfRule
import net.menthor.ontouml.rule.connection.InstanceOfSourceRule
import net.menthor.ontouml.rule.connection.InstanceOfTargetRule
import net.menthor.ontouml.rule.connection.MaterialSourceAndTargetRule
import net.menthor.ontouml.rule.connection.MediationSourceRule
import net.menthor.ontouml.rule.connection.MediationTargetRule
import net.menthor.ontouml.rule.connection.MemberOfPartRule
import net.menthor.ontouml.rule.connection.MemberOfWholeRule
import net.menthor.ontouml.rule.connection.ModeConnectedToCharacterizationRule
import net.menthor.ontouml.rule.connection.ParticipationSourceAndTargetRule
import net.menthor.ontouml.rule.connection.QuaPartOfPartRule
import net.menthor.ontouml.rule.connection.QuaPartOfWholeRule
import net.menthor.ontouml.rule.connection.QualityConnectedToStructurationRule
import net.menthor.ontouml.rule.connection.RelatorConnectedToMediationRule
import net.menthor.ontouml.rule.connection.RoleConnectedToMediationOrParticipationRule
import net.menthor.ontouml.rule.connection.RoleMixinConnectedToMediationOrParticipationRule
import net.menthor.ontouml.rule.connection.StructurationSourceRule
import net.menthor.ontouml.rule.connection.StructurationTargetRule
import net.menthor.ontouml.rule.connection.SubCollectionOfPartRule
import net.menthor.ontouml.rule.connection.SubCollectionOfWholeRule
import net.menthor.ontouml.rule.connection.SubEventOfPartAndWholeRule
import net.menthor.ontouml.rule.connection.SubQuantityOfPartRule
import net.menthor.ontouml.rule.connection.SubQuantityOfWholeRule
import net.menthor.ontouml.rule.connection.TemporalSourceAndTargetRule
import net.menthor.ontouml.rule.SyntacticalError

/**
 * @author John Guerson
 */
class ConnectingChecker {

    static List<SyntacticalError> check(MClassifier self){
        def rules = []
        def errors = []
        if(self instanceof OntoUMLClass) rules += getRules(self as OntoUMLClass)
        if(self instanceof OntoUMLDataType) rules += getRules(self as OntoUMLDataType)
        if(self instanceof OntoUMLRelationship) rules += getRules(self as OntoUMLRelationship)
        rules.each{ rule ->
            if((rule as SyntacticalRule).isRuleActived()) {
                errors += (rule as SyntacticalRule).check()
            }
        }
        return errors - null
    }

    static List<SyntacticalRule> getRules(OntoUMLClass self){
        def list = []
        list += new ModeConnectedToCharacterizationRule(self)
        list += new QualityConnectedToStructurationRule(self)
        list += new RelatorConnectedToMediationRule(self)
        list += new HighOrderConnectedToInstanceOfRule(self)
        list += new EventConnectedToParticipationRule(self)
        list += new RoleConnectedToMediationOrParticipationRule(self)
        list += new RoleMixinConnectedToMediationOrParticipationRule(self)
        return list - null
    }

    static List<SyntacticalRule> getRules(OntoUMLDataType self){
        def list = []
        list += new DomainComposedDimensionsRule(self)
        list += new DomainConnectedToStructurationRule(self)
        list += new EnumComposedLiteralsRule(self)
        list += new EnumConnectedToStructurationRule(self)
        list += new DimensionConnectedToStructurationRule(self)
        return list - null
    }

    static List<SyntacticalRule> getRules(OntoUMLRelationship self){
        def list = []
        list += new MediationSourceRule(self)
        list += new StructurationSourceRule(self)
        list += new CharacterizationSourceRule(self)
        list += new CharacterizationTargetRule(self)
        list += new ComponentOfPartRule(self)
        list += new ComponentOfWholeRule(self)
        list += new MemberOfPartRule(self)
        list += new MemberOfWholeRule(self)
        list += new SubCollectionOfPartRule(self)
        list += new SubCollectionOfWholeRule(self)
        list += new SubQuantityOfPartRule(self)
        list += new SubQuantityOfWholeRule(self)
        list += new ConsitutionPartRule(self)
        list += new ConsitutionWholeRule(self)
        list += new QuaPartOfPartRule(self)
        list += new QuaPartOfWholeRule(self)
        list += new InstanceOfSourceRule(self)
        list += new InstanceOfTargetRule(self)
        list += new ParticipationSourceAndTargetRule(self)
        list += new TemporalSourceAndTargetRule(self)
        list += new CausationSourceAndTargetRule(self)
        list += new MaterialSourceAndTargetRule(self)
        list += new SubEventOfPartAndWholeRule(self)
        list += new MediationTargetRule(self)
        list += new StructurationTargetRule(self)
        return list - null
    }
}
