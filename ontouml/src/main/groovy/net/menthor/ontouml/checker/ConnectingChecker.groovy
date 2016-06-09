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
import net.menthor.ontouml.rules.SyntacticalRule
import net.menthor.ontouml.rules.connection.CausationSourceAndTarget
import net.menthor.ontouml.rules.connection.CharacterizationSource
import net.menthor.ontouml.rules.connection.CharacterizationTarget
import net.menthor.ontouml.rules.connection.ComponentOfPart
import net.menthor.ontouml.rules.connection.ComponentOfWhole
import net.menthor.ontouml.rules.connection.ConsitutionPart
import net.menthor.ontouml.rules.connection.ConsitutionWhole
import net.menthor.ontouml.rules.connection.DimensionConnectedToStructuration
import net.menthor.ontouml.rules.connection.DomainComposedDimensions
import net.menthor.ontouml.rules.connection.DomainConnectedToStructuration
import net.menthor.ontouml.rules.connection.EnumComposedLiterals
import net.menthor.ontouml.rules.connection.EnumConnectedToStructuration
import net.menthor.ontouml.rules.connection.EventConnectedToParticipation
import net.menthor.ontouml.rules.connection.HighOrderConnectedToInstanceOf
import net.menthor.ontouml.rules.connection.InstanceOfSource
import net.menthor.ontouml.rules.connection.InstanceOfTarget
import net.menthor.ontouml.rules.connection.MaterialSourceAndTarget
import net.menthor.ontouml.rules.connection.MediationSource
import net.menthor.ontouml.rules.connection.MediationTarget
import net.menthor.ontouml.rules.connection.MemberOfPart
import net.menthor.ontouml.rules.connection.MemberOfWhole
import net.menthor.ontouml.rules.connection.ModeConnectedToCharacterization
import net.menthor.ontouml.rules.connection.ParticipationSourceAndTarget
import net.menthor.ontouml.rules.connection.QuaPartOfPart
import net.menthor.ontouml.rules.connection.QuaPartOfWhole
import net.menthor.ontouml.rules.connection.QualityConnectedToStructuration
import net.menthor.ontouml.rules.connection.RelatorConnectedToMediation
import net.menthor.ontouml.rules.connection.RoleConnectedToMediationOrParticipation
import net.menthor.ontouml.rules.connection.RoleMixinConnectedToMediationOrParticipation
import net.menthor.ontouml.rules.connection.StructurationSource
import net.menthor.ontouml.rules.connection.StructurationTarget
import net.menthor.ontouml.rules.connection.SubCollectionOfPart
import net.menthor.ontouml.rules.connection.SubCollectionOfWhole
import net.menthor.ontouml.rules.connection.SubEventOfPartAndWhole
import net.menthor.ontouml.rules.connection.SubQuantityOfPart
import net.menthor.ontouml.rules.connection.SubQuantityOfWhole
import net.menthor.ontouml.rules.connection.TemporalSourceAndTarget

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
            errors += (rule as SyntacticalRule).check()
        }
        return errors - null
    }

    static List<SyntacticalRule> getRules(OntoUMLClass self){
        def list = []
        list += new ModeConnectedToCharacterization(self)
        list += new QualityConnectedToStructuration(self)
        list += new RelatorConnectedToMediation(self)
        list += new HighOrderConnectedToInstanceOf(self)
        list += new EventConnectedToParticipation(self)
        list += new RoleConnectedToMediationOrParticipation(self)
        list += new RoleMixinConnectedToMediationOrParticipation(self)
        return list - null
    }

    static List<SyntacticalRule> getRules(OntoUMLDataType self){
        def list = []
        list += new DomainComposedDimensions(self)
        list += new DomainConnectedToStructuration(self)
        list += new EnumComposedLiterals(self)
        list += new EnumConnectedToStructuration(self)
        list += new DimensionConnectedToStructuration(self)
        return list - null
    }

    static List<SyntacticalRule> getRules(OntoUMLRelationship self){
        def list = []
        list += new MediationSource(self)
        list += new StructurationSource(self)
        list += new CharacterizationSource(self)
        list += new CharacterizationTarget(self)
        list += new ComponentOfPart(self)
        list += new ComponentOfWhole(self)
        list += new MemberOfPart(self)
        list += new MemberOfWhole(self)
        list += new SubCollectionOfPart(self)
        list += new SubCollectionOfWhole(self)
        list += new SubQuantityOfPart(self)
        list += new SubQuantityOfWhole(self)
        list += new ConsitutionPart(self)
        list += new ConsitutionWhole(self)
        list += new QuaPartOfPart(self)
        list += new QuaPartOfWhole(self)
        list += new InstanceOfSource(self)
        list += new InstanceOfTarget(self)
        list += new ParticipationSourceAndTarget(self)
        list += new TemporalSourceAndTarget(self)
        list += new CausationSourceAndTarget(self)
        list += new MaterialSourceAndTarget(self)
        list += new SubEventOfPartAndWhole(self)
        list += new MediationTarget(self)
        list += new StructurationTarget(self)
        return list - null
    }
}
