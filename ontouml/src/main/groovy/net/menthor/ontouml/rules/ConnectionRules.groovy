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
import net.menthor.ontouml.OntoUMLDataType
import net.menthor.ontouml.OntoUMLRelationship
import net.menthor.core.traits.MClassifier
import net.menthor.ontouml.rules.types.connection.CausationSourceAndTargetRule
import net.menthor.ontouml.rules.types.connection.CharacterizationSourceRule
import net.menthor.ontouml.rules.types.connection.CharacterizationTargetRule
import net.menthor.ontouml.rules.types.connection.ComponentOfPartRule
import net.menthor.ontouml.rules.types.connection.ComponentOfWholeRule
import net.menthor.ontouml.rules.types.connection.ConsitutionPartRule
import net.menthor.ontouml.rules.types.connection.ConsitutionWholeRule
import net.menthor.ontouml.rules.types.connection.DimensionConnectedToStructurationRule
import net.menthor.ontouml.rules.types.connection.DomainComposedDimensionsRule
import net.menthor.ontouml.rules.types.connection.DomainConnectedToStructurationRule
import net.menthor.ontouml.rules.types.connection.EnumComposedLiteralsRule
import net.menthor.ontouml.rules.types.connection.EnumConnectedToStructurationRule
import net.menthor.ontouml.rules.types.connection.EventConnectedToParticipationRule
import net.menthor.ontouml.rules.types.connection.HighOrderConnectedToInstanceOfRule
import net.menthor.ontouml.rules.types.connection.InstanceOfSourceRule
import net.menthor.ontouml.rules.types.connection.InstanceOfTargetRule
import net.menthor.ontouml.rules.types.connection.MaterialSourceAndTargetRule
import net.menthor.ontouml.rules.types.connection.MediationSourceRule
import net.menthor.ontouml.rules.types.connection.MediationTargetRule
import net.menthor.ontouml.rules.types.connection.MemberOfPartRule
import net.menthor.ontouml.rules.types.connection.MemberOfWholeRule
import net.menthor.ontouml.rules.types.connection.ModeConnectedToCharacterizationRule
import net.menthor.ontouml.rules.types.connection.ParticipationSourceAndTargetRule
import net.menthor.ontouml.rules.types.connection.QuaPartOfPartRule
import net.menthor.ontouml.rules.types.connection.QuaPartOfWholeRule
import net.menthor.ontouml.rules.types.connection.QualityConnectedToStructurationRule
import net.menthor.ontouml.rules.types.connection.RelatorConnectedToMediationRule
import net.menthor.ontouml.rules.types.connection.RoleConnectedToMediationOrParticipationRule
import net.menthor.ontouml.rules.types.connection.RoleMixinConnectedToMediationOrParticipationRule
import net.menthor.ontouml.rules.types.connection.StructurationSourceRule
import net.menthor.ontouml.rules.types.connection.StructurationTargetRule
import net.menthor.ontouml.rules.types.connection.SubCollectionOfPartRule
import net.menthor.ontouml.rules.types.connection.SubCollectionOfWholeRule
import net.menthor.ontouml.rules.types.connection.SubEventOfPartAndWholeRule
import net.menthor.ontouml.rules.types.connection.SubQuantityOfPartRule
import net.menthor.ontouml.rules.types.connection.SubQuantityOfWholeRule
import net.menthor.ontouml.rules.types.connection.TemporalSourceAndTargetRule

/**
 * @author John Guerson
 */
class ConnectionRules {

    static name = "Connection"

    static classRules = initClassRules()
    static datatypeRules = initDatatypeRules()
    static relationshipRules = initRelationshipRules()

    static getRules(){
        def result = []
        result.addAll(classRules)
        result.addAll(datatypeRules)
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
        return errors - null
    }

    private static set(self){
        if(self instanceof OntoUMLClass) classRules.each{ c -> (c as SyntacticalRule).self = self as OntoUMLClass }
        if(self instanceof OntoUMLDataType) datatypeRules.each{ d -> (d as SyntacticalRule).self = self as OntoUMLDataType }
        if(self instanceof OntoUMLRelationship) relationshipRules.each{ d -> (d as SyntacticalRule).self = self as OntoUMLRelationship }
    }

    private static List initClassRules(){
        def list = []
        list += new ModeConnectedToCharacterizationRule(null)
        list += new QualityConnectedToStructurationRule(null)
        list += new RelatorConnectedToMediationRule(null)
        list += new HighOrderConnectedToInstanceOfRule(null)
        list += new EventConnectedToParticipationRule(null)
        list += new RoleConnectedToMediationOrParticipationRule(null)
        list += new RoleMixinConnectedToMediationOrParticipationRule(null)
        return list - null
    }

    private static List initDatatypeRules(){
        def list = []
        list += new DomainComposedDimensionsRule(null)
        list += new DomainConnectedToStructurationRule(null)
        list += new EnumComposedLiteralsRule(null)
        list += new EnumConnectedToStructurationRule(null)
        list += new DimensionConnectedToStructurationRule(null)
        return list - null
    }

    private static List<SyntacticalRule> initRelationshipRules(){
        def list = []
        list += new MediationSourceRule(null)
        list += new StructurationSourceRule(null)
        list += new CharacterizationSourceRule(null)
        list += new CharacterizationTargetRule(null)
        list += new ComponentOfPartRule(null)
        list += new ComponentOfWholeRule(null)
        list += new MemberOfPartRule(null)
        list += new MemberOfWholeRule(null)
        list += new SubCollectionOfPartRule(null)
        list += new SubCollectionOfWholeRule(null)
        list += new SubQuantityOfPartRule(null)
        list += new SubQuantityOfWholeRule(null)
        list += new ConsitutionPartRule(null)
        list += new ConsitutionWholeRule(null)
        list += new QuaPartOfPartRule(null)
        list += new QuaPartOfWholeRule(null)
        list += new InstanceOfSourceRule(null)
        list += new InstanceOfTargetRule(null)
        list += new ParticipationSourceAndTargetRule(null)
        list += new TemporalSourceAndTargetRule(null)
        list += new CausationSourceAndTargetRule(null)
        list += new MaterialSourceAndTargetRule(null)
        list += new SubEventOfPartAndWholeRule(null)
        list += new MediationTargetRule(null)
        list += new StructurationTargetRule(null)
        return list - null
    }
}
