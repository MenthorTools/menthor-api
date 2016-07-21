package net.menthor.ontouml.rules.types.cardinality
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
import net.menthor.ontouml.OntoUMLClass
import net.menthor.ontouml.rules.traits.CardinalitySyntacticalRule
import net.menthor.ontouml.stereotypes.RelationshipStereotype

/**
 * @author John Guerson
 */
class TruthMakerCardinalityEndsRule implements CardinalitySyntacticalRule {

    TruthMakerCardinalityEndsRule(OntoUMLClass self){
        this.description = 'The sum of the minimum cardinalities of the mediated ends of a Truth Maker (element with a relator identity) must be greater or equal to 2'
        this.self = self
    }

    @Override
    boolean condition() {
        def sum = 0
        if((self as OntoUMLClass).isTruthMaker()){
            def oppositeEndPoints = self.allOppositeEndPoints().findAll{ ep ->
                (ep.getOwner() as OntoUMLRelationship).getStereotype() == RelationshipStereotype.MEDIATION
            }
            if(oppositeEndPoints.size()==0) return true
            oppositeEndPoints.each { ep ->
                sum += ep.getLowerBound()
            }
        }else{
            return true
        }
        if(sum>=2) return true
        else return false
    }

    @Override
    boolean quickFix(){
        return false
    }
}