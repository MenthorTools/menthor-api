package net.menthor.ontouml

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

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import net.menthor.core.MClass
import net.menthor.ontouml.stereotypes.ClassStereotype
import net.menthor.ontouml.stereotypes.QualityStereotype
import net.menthor.ontouml.values.ClassificationValue
import net.menthor.ontouml.values.ExistenceValue

/**
 * OntoUML class.
 * @author John Guerson
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
class OntoUMLClass extends MClass {

    protected ClassStereotype stereotype
    protected boolean abstract_
    protected boolean derived
    protected QualityStereotype qualityStereotype
    protected boolean extensional
    protected ExistenceValue existenceValue
    protected ClassificationValue classificationValue
    protected OntoUMLGeneralizationSet generalizationSet

    //=============================
    // Getters
    //=============================

    ClassStereotype getStereotype(){ return stereotype }

    QualityStereotype getQualityStereotype() { return qualityStereotype }

    boolean isAbstract_(){ return abstract_ }

    boolean isDerived(){ return derived }

    boolean isExtensional(){ return extensional }

    ExistenceValue getExistenceValue(){ return existenceValue }

    ClassificationValue getClassificationValue() { return classificationValue }

    @JsonIgnore
    OntoUMLGeneralizationSet getGeneralizationSet() { return generalizationSet }

    //=============================
    // Setters
    //=============================

    void setStereotype(ClassStereotype stereo){ stereotype = stereo }

    void setQualityStereotype(QualityStereotype stereo) { qualityStereotype = stereo }

    void setIsAbstract(boolean value){ isAbstract  = value}

    void setIsDerived(boolean value){ derived = value }

    void setIsExtensional(boolean value){ extensional = value }

    void setExistenceValue(ExistenceValue value){ existenceValue = value }

    void setClassificationValue(ClassificationValue value) { classificationValue = value }

    void setGeneralizationSet(OntoUMLGeneralizationSet gs) { generalizationSet = gs }

    //================================
    //Stereotype Checking
    //================================

    @JsonIgnore
    boolean isKind(){ stereotype==ClassStereotype.KIND }

    @JsonIgnore
    boolean isSubKind(){ stereotype==ClassStereotype.SUBKIND }

    @JsonIgnore
    boolean isCollective(){ stereotype==ClassStereotype.COLLECTIVE }

    @JsonIgnore
    boolean isQuantity(){ stereotype==ClassStereotype.QUANTITY }

    @JsonIgnore
    boolean isRelator(){ stereotype==ClassStereotype.RELATOR }

    @JsonIgnore
    boolean isMode(){ stereotype==ClassStereotype.MODE }

    @JsonIgnore
    boolean isQuality(){ stereotype==ClassStereotype.QUALITY }

    @JsonIgnore
    boolean isRole(){ stereotype==ClassStereotype.ROLE }

    @JsonIgnore
    boolean isRoleMixin(){ stereotype==ClassStereotype.ROLEMIXIN }

    @JsonIgnore
    boolean isPhaseMixin(){ stereotype==ClassStereotype.PHASEMIXIN }

    @JsonIgnore
    boolean isPhase(){ stereotype==ClassStereotype.PHASE }

    @JsonIgnore
    boolean isCategory(){ stereotype==ClassStereotype.CATEGORY }

    @JsonIgnore
    boolean isMixin(){ stereotype==ClassStereotype.MIXIN }

    @JsonIgnore
    boolean isEvent(){ stereotype==ClassStereotype.EVENT }

    @JsonIgnore
    boolean isHighOrder(){ stereotype==ClassStereotype.HIGHORDER }

    @JsonIgnore
    boolean isRigid() {
        isKind() || isCollective() || isQuantity() ||isRelator() || isMode() || isQuality() ||isSubKind() || isCategory()
    }

    @JsonIgnore
    boolean isNonRigid() {
        isRole()|| isPhase() || isRoleMixin() || isPhaseMixin() || isMixin()
    }

    @JsonIgnore
    boolean isAntiRigid() {
        isRole() || isPhase() || isRoleMixin() || isPhaseMixin()
    }

    @JsonIgnore
    boolean isSemiRigid() {
        isMixin()
    }

    @JsonIgnore
    boolean isPerceivableQuality() {
        isQuality() && qualityStereotype!=null && qualityStereotype==QualityStereotype.PERCEIVABLE
    }

    @JsonIgnore
    boolean isNonPerceivableQuality() {
        isQuality() && qualityStereotype!=null && qualityStereotype==QualityStereotype.NON_PERCEIVABLE
    }

    @JsonIgnore
    boolean isNominalQuality() {
        isQuality() && qualityStereotype!=null && qualityStereotype==QualityStereotype.NOMINAL
    }

    @JsonIgnore
    boolean isEndurantClass() {
        !(isEvent() || isHighOrder())
    }

    @JsonIgnore
    boolean isIntrinsicMoment() {
        isNonQualitativeIntrinsicMoment() || isQualitativeIntrinsicMoment()
    }

    @JsonIgnore
    boolean isSubstanceSortalClass() {
        isKind() || isCollective() || isQuantity()
    }

    @JsonIgnore
    boolean isMomentClass() {
        isRelator() || isMode() || isQuality()
    }

    @JsonIgnore
    boolean isIdentityProviderClass() {
        isKind() || isQuantity() || isCollective() || isRelator() || isMode() || isQuality()
    }

    @JsonIgnore
    boolean isMixinClass() {
        isMixin() || isRoleMixin() || isPhaseMixin() || isCategory()
    }

    @JsonIgnore
    boolean isAntiRigidMixinClass() {
        isRoleMixin() || isPhaseMixin()
    }

    //================================
    //Identity Checking
    //================================

    /**
     * Checks if this element is an amount of matter i.e.
     * 1) if it is a quantity element, or,
     * 2) if it is a subKind or role/phase with exactly one identity provider of the type Quantity, or,
     * 3) if it is a mixin class in which all their children are quantities.
     */
    @JsonIgnore
    boolean isAmountOfMatter() {
        if(isQuantity()) return true;
        if(isRole() || isPhase() || isSubKind()){
            def providers = []
            providers.addAll(identityProviders());
            providers.each { c ->
                if (c.isQuantity()) {
                    return true
                }
            }
        }
        if(isMixinClass()) {
            if(children().size()==0) {
                return false
            }
            children().each{ child ->
                if(child instanceof OntoUMLClass) {
                    if(!child.isQuantity()) return false
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if this element is a functional complex i.e.
     * 1) If it is a kind, or
     * 2) if it is a subKind or role/phase with exactly one identity provider of the type kind, or,
     * 3) if it is a mixin class in which all their children are functional complexes.
     */
    @JsonIgnore
    boolean isFunctionalComplex(){
        if(isKind()) {
            return true
        }
        if(isRole() || isPhase() || isSubKind()){
            def providers = []
            providers.addAll(identityProviders());
            providers.each { c ->
                if (c.isKind()) {
                    return true
                }
            }
        }
        if(isMixinClass()){
            if(children().size()==0) {
                return false
            }
            children().each{ child ->
                if(child instanceof OntoUMLClass) {
                    if(!child.isKind()) {
                        return false
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if this element is a collective i.e.
     * 1) if it is a collective element, or,
     * 2) if it is a subKind or role/phase with exactly one identity provider of the type Collective, or,
     * 3) if it is a mixin class in which all their children are collectives.
     */
    @JsonIgnore
    boolean isCollection(){
        if(isCollective()) {
            return true
        }
        if(isRole() || isPhase() || isSubKind()){
            def providers = []
            providers.addAll(identityProviders());
            providers.each { c ->
                if (c.isCollective()) {
                    return true
                }
            }
        }
        if(isMixinClass()) {
            if(children().size()==0) {
                return false
            }
            children().each{ child ->
                if(child instanceof OntoUMLClass) {
                    if(!child.isCollective()) {
                        return false
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if this element is a moment  i.e.
     * 1) if it is a relator, mode or quality element, or,
     * 2) if it is a subKind or role/phase with exactly one identity provider being a relator, mode or quality, or,
     * 3) if it is a mixin class in which all their children are relators, qualities or modes.
     */
    @JsonIgnore
    boolean isMoment(){
        if(isMomentClass()) {
            return true
        }
        if(isRole() || isPhase() || isSubKind()){
            def providers = []
            providers.addAll(identityProviders());
            providers.each { c ->
                if (c.isMomentClass()) {
                    return true
                }
            }
        }
        if(isMixinClass()){
            if(children().size()==0) {
                return false
            }
           children().each{ child ->
                if(child instanceof OntoUMLClass) {
                    if(!child.isMomentClass()) {
                        return false
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if this element is a truth maker (relator) i.e.
     * 1) if it is a truth maker element, or,
     * 2) if it is a subKind or role/phase with exactly one identity provider being a truth maker, or,
     * 3) if it is a mixin class in which all their children are truth makers.
     */
    @JsonIgnore
    boolean isTruthMaker(){
        if(isRelator()) {
            return true
        }
        if(isRole() || isPhase() || isSubKind()){
            def providers = []
            providers.addAll(identityProviders());
            providers.each { c ->
                if (c.isRelator()) {
                    return true
                }
            }
        }
        if(isMixinClass()){
            if(children().size()==0) {
                return false
            }
            children().each{ child ->
                if(child instanceof OntoUMLClass) {
                    if(!child.isRelator()) {
                        return false
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if this element is a non qualitative instrinsic moment (a mode) i.e.
     * 1) if it is a instrinsic moment which is a non qualitative element, or,
     * 2) if it is a subKind or role/phase with exactly one identity provider being a non qualitative intrinsic moment, or,
     * 3) if it is a mixin class in which all their children are non qualitative intrinsic moment.
     */
    @JsonIgnore
    boolean isNonQualitativeIntrinsicMoment() {
        if(isMode()) {
            return true
        }
        if(isRole() || isPhase() || isSubKind()){
            def providers = []
            providers.addAll(identityProviders());
            providers.each { c ->
                if (c.isMode()) {
                    return true
                }
            }
        }
        if(isMixinClass()){
            if(children().size()==0) {
                return false
            }
            children().each{ child ->
                if(child instanceof OntoUMLClass) {
                    if(!child.isMode()) {
                        return false
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if this element is a qualitative instrinsic moment (quality) i.e.
     * 1) if it is a instrinsic moment which is a qualitative element, or,
     * 2) if it is a subKind or role/phase with exactly one identity provider being a qualitative intrinsic moment, or,
     * 3) if it is a mixin class in which all their children are qualitative intrinsic moment.
     */
    @JsonIgnore
    boolean isQualitativeIntrinsicMoment(){
        if(isQuality()) {
            return true
        }
        if(isRole() || isPhase() || isSubKind()){
            def providers = []
            providers.addAll(identityProviders());
            providers.each { c ->
                if (c.isQuality()) {
                    return true
                }
            }
        }
        if(isMixinClass()){
            if(children().size()==0) {
                return false
            }
            children().each{ child ->
                if(child instanceof OntoUMLClass) {
                    if(!child.isQuality()) {
                        return false
                    }
                }
            }
            return true;
        }
        return false;
    }

    /** Returns the identity providers amongst all parents of a class (more than one may be found) */
    List<OntoUMLClass> identityProvidersAtAllParents(){
        def result = []
        allParents().each{ p ->
            if(p instanceof OntoUMLClass){
                if(p.isIdentityProviderClass()) {
                    result.add(p)
                }
            }
        }
        return result;
    }

    /** Returns the identity providers amongst all children of a class (more than one may be found) */
    List<OntoUMLClass> identityProvidersAtAllChildren(){
        def result = []
        allChildren().each{ p ->
            if(p instanceof OntoUMLClass){
                if(p.isIdentityProviderClass()) {
                    result.add(p)
                }
                if(p.isRole() || isPhase() || p.isSubKind()) {
                    result.addAll(p.identityProvidersAtAllParents())
                }
            }
        }
        return result;
    }

    /** Returns the identity providers of a class (more than one may be found) */
    List<OntoUMLClass> identityProviders() {
        def result = []
        if (isIdentityProviderClass()) {
            result.add(this);
        }
        if (isRole() || isPhase() || isSubKind()) {
            result.addAll(identityProvidersAtAllParents());
        }
        if (isMixinClass()){
            result.addAll(identityProvidersAtAllChildren());
            allParents().each{ p ->
                if(p instanceof OntoUMLClass){
                    result.addAll(p.identityProvidersAtAllChildren());
                }
            }
        }
        return result;
    }

    /** If this is a 'part' in some meroymic relationship, the method return all its respective wholes */
    List<OntoUMLClass> wholes(){
        def list = []
        oppositeEndPoints().each{ opposite ->
            def relationship = opposite.getOwner()
            if(relationship!=null){
                def isMeronymic = (relationship as OntoUMLRelationship).isMeronymic()
                def part = (relationship as OntoUMLRelationship).partClass()
                if(isMeronymic && part.equals(this)) list.add(opposite.getClassifier())
            }
        }
        return list;
    }

    /** All (direct and indirect) wholes related to this part */
    void allWholes(OntoUMLClass c, List result){
        c.wholes().each{ w ->
            result.add(w);
            allWholes(w,result);
        }
    }
    List<OntoUMLClass> allWholes(){
        def result = []
        allWholes(this, result)
        return result
    }

    /** If this is a 'whle' in some meroymic relationship, the method return all its respective parts */
    List<OntoUMLClass> parts(){
        def list = []
        oppositeEndPoints().each{ opposite ->
            def relationship = opposite.getOwner()
            if(relationship!=null){
                def isMeronymic = (relationship as OntoUMLRelationship).isMeronymic()
                def whole = (relationship as OntoUMLRelationship).wholeClass()
                if(isMeronymic && whole.equals(this)) list.add(opposite.getClassifier())
            }
        }
        return list;
    }

    /** All (direct and indirect) parts related to this whole */
    void allParts(OntoUMLClass c, List result){
        c.parts().each{ p ->
            result.add(p)
            allParts(p,result)
        }
    }
    List<OntoUMLClass> allParts(){
        def result = []
        allParts(this, result)
        return result
    }

    String toString() { OntoUMLPrinter.print(this) }

}
