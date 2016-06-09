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
import net.menthor.ontouml.OntoUMLRelationship
import net.menthor.ontouml.rules.SyntacticalRule
import net.menthor.ontouml.rules.cardinality.CharacterizationSourceCardinality
import net.menthor.ontouml.rules.cardinality.CharacterizationTargetCardinality
import net.menthor.ontouml.rules.cardinality.DerivationSourceCardinality
import net.menthor.ontouml.rules.cardinality.DerivationTargetCardinality
import net.menthor.ontouml.rules.cardinality.InstanceOfTargetCardinality
import net.menthor.ontouml.rules.cardinality.MediationTargetCardinality
import net.menthor.ontouml.rules.cardinality.QuaPartOfWholeCardinality
import net.menthor.ontouml.rules.cardinality.StructurationTargetCardinality
import net.menthor.ontouml.rules.cardinality.SubCollectionPartCardinality
import net.menthor.ontouml.rules.cardinality.SubQuantityPartCardinality
import net.menthor.ontouml.rules.cardinality.SubQuantityWholeCardinality
import net.menthor.ontouml.rules.cardinality.TruthMakerCardinalityEnds
import net.menthor.ontouml.rules.cardinality.WeakSupplementation
import net.menthor.core.traits.MClassifier

/**
 * @author John Guerson
 */
class CardinalityChecker {

    static List<SyntacticalError> check(MClassifier self){
        def errors = []
        def rules = []
        if(self instanceof OntoUMLClass) rules += getRules(self as OntoUMLClass)
        if(self instanceof OntoUMLRelationship) rules += getRules(self as OntoUMLRelationship)
        rules.each{ rule ->
            errors += (rule as SyntacticalRule).check()
        }
        return errors-null
    }

    static List<SyntacticalRule> getRules(OntoUMLClass self) {
        def list = []
        list += new TruthMakerCardinalityEnds(self)
        list += new WeakSupplementation(self)
        return list - null
    }

    static List<SyntacticalRule> getRules(OntoUMLRelationship self){
        def list = []
        list += new MediationTargetCardinality(self)
        list += new CharacterizationSourceCardinality(self)
        list += new CharacterizationTargetCardinality(self)
        list += new InstanceOfTargetCardinality(self)
        list += new SubCollectionPartCardinality(self)
        list += new SubQuantityWholeCardinality(self)
        list += new SubQuantityPartCardinality(self)
        list += new StructurationTargetCardinality(self)
        list += new DerivationSourceCardinality(self)
        list += new DerivationTargetCardinality(self)
        list += new QuaPartOfWholeCardinality(self)
        return list - null
    }
}
