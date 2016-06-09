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

import net.menthor.ontouml.OntoUMLRelationship
import net.menthor.core.traits.MType
import net.menthor.ontouml.values.CiclicityValue
import net.menthor.ontouml.values.ReflexivityValue
import net.menthor.ontouml.values.SymmetryValue
import net.menthor.ontouml.values.TransitivityValue

/**
 * @author John Guerson
 */
abstract class GenericCondition {

    static boolean exactOne(MType self, String stereotypeCheckMethod, String listElemsMethod){
        if(self."${stereotypeCheckMethod}"()){
            def value = self."${listElemsMethod}"().size()==1
            return value
        }
        return true
    }

    static boolean excludesAsAncestor(MType self, String stereotypeCheckMethod, String ancestorCheckMethod, String ancestorCheckMethod2){
        if(self."${stereotypeCheckMethod}"()) {
            def value = self.allParents().every { p ->
                self.class.isInstance(p) && !p."${ancestorCheckMethod}"() && !p."${ancestorCheckMethod2}"()
            }
            return value
        }
        return true
    }

    static boolean excludesAsAncestor(MType self, String stereotypeCheckMethod, String ancestorCheckMethod){
        if(self."${stereotypeCheckMethod}"()) {
            def value = self.allParents().every { p ->
                self.class.isInstance(p) && !p."${ancestorCheckMethod}"()
            }
            return value
        }
        return true
    }

    static boolean excludesAsParent(MType self, String stereotypeCheckMethod, String ancestorCheckMethod, String ancestorCheckMethod2){
        if(self."${stereotypeCheckMethod}"()) {
            def value = self.parents().every { p ->
                self.class.isInstance(p) && !p."${ancestorCheckMethod}"() && !p."${ancestorCheckMethod2}"()
            }
            return value
        }
        return true
    }

    static boolean excludesAsParent(MType self, String stereotypeCheckMethod, String ancestorCheckMethod){
        if(self."${stereotypeCheckMethod}"()) {
            def value = self.parents().every { p ->
                self.class.isInstance(p) && !p."${ancestorCheckMethod}"()
            }
            return value
        }
        return true
    }

    static boolean includesAsAncestorsOnly(MType self, String stereotypeCheckMethod){
        if(self."${stereotypeCheckMethod}"()) {
            def value = self.allParents().every { p ->
                self.class.isInstance(p) && p."${stereotypeCheckMethod}"()
            }
            return value
        }
        return true
    }

    static boolean typeMustBeConnectedToRelationship(MType self, String typeStereoCheckMethod, String relStereoCheckMethod){
        if(self."${typeStereoCheckMethod}"()){
            def value = self.allRelationships().any { r ->
                r."${relStereoCheckMethod}"()
            }
            return value
        }
        return true
    }

    static boolean typeComposedByAtLeastTwoElements(MType self, String typeStereoCheckMethod, String listElemMethod, Integer number){
        if(self."${typeStereoCheckMethod}"()) {
            return self."${listElemMethod}"().size() >= number
        }
        return true
    }


    static boolean sourceClassMustBe(OntoUMLRelationship self, String relStereotypeMethod, String sourceStereotypeMethod){
        if(self."${relStereotypeMethod}"()){
            def value = self.sourceClass()!=null && self.sourceClass()."${sourceStereotypeMethod}"()
            return value
        }
        return true
    }

    static boolean mustConnect(OntoUMLRelationship self, String relStereotypeMethod, String sourceStereotypeMethod, String targetStereotypeMethod){
        if(self."${relStereotypeMethod}"()){
            def valueSrc = self.sourceClass()!=null && self.sourceClass()."${sourceStereotypeMethod}"()
            def valueTgt = self.targetClass()!=null && self.targetClass()."${targetStereotypeMethod}"()
            return valueSrc && valueTgt
        }
        return true
    }

    static boolean mustNotConnect(OntoUMLRelationship self, String relStereotypeMethod, String sourceStereotypeMethod, String targetStereotypeMethod){
        if(self."${relStereotypeMethod}"()){
            def valueSrc = self.sourceClass()!=null && !self.sourceClass()."${sourceStereotypeMethod}"()
            def valueTgt = self.targetClass()!=null && !self.targetClass()."${targetStereotypeMethod}"()
            return valueSrc && valueTgt
        }
        return true
    }

    static boolean sourceClassMustNotBe(OntoUMLRelationship self, String relStereotypeMethod, String sourceStereotypeMethod){
        if(self."${relStereotypeMethod}"()){
            def value = self.sourceClass()!=null && !self.sourceClass()."${sourceStereotypeMethod}"()
            return value
        }
        return true
    }

    static boolean wholeMustBe(OntoUMLRelationship self, String relStereotypeMethod, String wholeStereotypeMethod){
        if(self."${relStereotypeMethod}"()){
            def value = self.wholeClass()!=null && self.wholeClass()."${wholeStereotypeMethod}"()
            return value
        }
        return true
    }

    static boolean targetClassMustBe(OntoUMLRelationship self, String relStereotypeMethod, String sourceStereotypeMethod){
        if(self."${relStereotypeMethod}"()){
            def value = self.targetClass()!=null && self.targetClass()."${sourceStereotypeMethod}"()
            return value
        }
        return true
    }

    static boolean partMustBe(OntoUMLRelationship self, String relStereotypeMethod, String partStereotypeMethod){
        if(self."${relStereotypeMethod}"()){
            def value = self.partClass()!=null && self.partClass()."${partStereotypeMethod}"()
            return value
        }
        return true
    }

    static boolean partMustBeEither(OntoUMLRelationship self, String relStereotypeMethod, String partStereotypeMethod, String partStereotypeMethod2){
        if(self."${relStereotypeMethod}"()){
            def value = self.partClass()!=null && (self.partClass()."${partStereotypeMethod}"() || self.partClass()."${partStereotypeMethod2}"())
            return value
        }
        return true
    }

    static boolean sourceAndTargetDependents(OntoUMLRelationship self, String relStereotypeMethod){
        if(self."${relStereotypeMethod}"()){
            def value = self.sourceEndPoint()!=null && self.sourceEndPoint().isDependency() &&
                    self.targetEndPoint()!=null && self.targetEndPoint().isDependency()
            return value
        }
        return true
    }

    static boolean sourceDependent(OntoUMLRelationship self, String relStereotypeMethod){
        if(self."${relStereotypeMethod}"()){
            def value = self.sourceEndPoint()!=null && self.sourceEndPoint().isDependency()
            return value
        }
        return true
    }

    static boolean targetDependent(OntoUMLRelationship self, String relStereotypeMethod){
        if(self."${relStereotypeMethod}"()){
            def value = self.targetEndPoint()!=null && self.targetEndPoint().isDependency()
            return value
        }
        return true
    }

    static boolean partAndWholeDependents(OntoUMLRelationship self, String relStereotypeMethod){
        if(self."${relStereotypeMethod}"()){
            def value = self.partEndPoint()!=null && self.partEndPoint().isDependency() &&
                    self.wholeEndPoint()!=null && self.wholeEndPoint().isDependency()
            return value
        }
        return true
    }

    static boolean partDependent(OntoUMLRelationship self, String relStereotypeMethod){
        if(self."${relStereotypeMethod}"()){
            def value = self.partEndPoint()!=null && self.partEndPoint().isDependency()
            return value
        }
        return true
    }

    static boolean wholeDependent(OntoUMLRelationship self, String relStereotypeMethod){
        if(self."${relStereotypeMethod}"()){
            def value = self.wholeEndPoint()!=null && self.wholeEndPoint().isDependency()
            return value
        }
        return true
    }

    static boolean isMetaAttribute(OntoUMLRelationship self, String relStereotypeMethod, String metaAttrMethod, boolean value){
        if(self."${relStereotypeMethod}"()){
            def result = self."${metaAttrMethod}"()==value
            return result
        }
        return true
    }

    static boolean isMetaAttributeIfWholeIs(OntoUMLRelationship self, String relStereotypeMethod, String metaAttrMethod, boolean value, String wholeStereotypeMethod){
        if(self."${relStereotypeMethod}"() && self.wholeClass()!=null && self.wholeClass()."${wholeStereotypeMethod}"){
            def result = self."${metaAttrMethod}"()==value
            return result
        }
        return true
    }

    static boolean isMetaAttributeIfPartIs(OntoUMLRelationship self, String relStereotypeMethod, String metaAttrMethod, boolean value, String partStereotypeMethod){
        if(self."${relStereotypeMethod}"() && self.partClass()!=null && self.partClass()."${partStereotypeMethod}"){
            def result = self."${metaAttrMethod}"()==value
            return result
        }
        return true
    }

    static boolean isValue(OntoUMLRelationship self, String relStereotypeMethod, ReflexivityValue rValue, SymmetryValue sValue, TransitivityValue tValue, CiclicityValue cValue){
        if(self."${relStereotypeMethod}"()){
            def result = true
            if(rValue!=null) result = result && self.reflexivityValue == rValue
            if(sValue!=null) result = result && self.symmetryValue == sValue
            if(tValue!=null) result = result && self.transitivityValue == tValue
            if(cValue!=null) result = result && self.ciclicityValue == cValue
            return result
        }
        return true
    }

    static boolean targetMultiplicity(OntoUMLRelationship self, String relStereotypeMethod, Integer lowerBound, Integer upperBound){
        if(self."${relStereotypeMethod}"()){
            def value = true
            if(lowerBound!=null) value = value && self.targetEndPoint()!=null && self.targetEndPoint().getLowerBound() == lowerBound
            if(upperBound!=null) value = value && self.targetEndPoint()!=null && self.targetEndPoint().getUpperBound() == upperBound
            return value
        }
        return true
    }

    static boolean partMultiplicity(OntoUMLRelationship self, String relStereotypeMethod, Integer lowerBound, Integer upperBound){
        if(self."${relStereotypeMethod}"()){
            def value = true
            if(lowerBound!=null) value = value && self.partEndPoint()!=null && self.partEndPoint().getLowerBound() == lowerBound
            if(upperBound!=null) value = value && self.partEndPoint()!=null && self.partEndPoint().getUpperBound() == upperBound
            return value
        }
        return true
    }

    static boolean wholeMultiplicity(OntoUMLRelationship self, String relStereotypeMethod, Integer lowerBound, Integer upperBound){
        if(self."${relStereotypeMethod}"()){
            def value = true
            if(lowerBound!=null) value = value && self.wholeEndPoint()!=null && self.wholeEndPoint().getLowerBound() == lowerBound
            if(upperBound!=null) value = value && self.wholeEndPoint()!=null && self.wholeEndPoint().getUpperBound() == upperBound
            return value
        }
        return true
    }

    static boolean sourceMultiplicity(OntoUMLRelationship self, String relStereotypeMethod, Integer lowerBound, Integer upperBound){
        if(self."${relStereotypeMethod}"()){
            def value = true
            if(lowerBound!=null) value = value && self.sourceEndPoint()!=null && self.sourceEndPoint().getLowerBound() == lowerBound
            if(upperBound!=null) value = value && self.sourceEndPoint()!=null && self.sourceEndPoint().getUpperBound() == upperBound
            return value
        }
        return true
    }

}

