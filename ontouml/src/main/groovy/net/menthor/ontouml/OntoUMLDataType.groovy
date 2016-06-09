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
import net.menthor.core.MDataType
import net.menthor.ontouml.stereotypes.DataTypeStereotype
import net.menthor.ontouml.values.MeasurementValue
import net.menthor.ontouml.values.ScaleValue

/**
 * OntoUML datatype.
 * @author John Guerson
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
class OntoUMLDataType extends MDataType {

    protected DataTypeStereotype stereotype

    //in case of domains
    protected List<OntoUMLDataType> dimensions = []

    //in case of dimensions
    protected ScaleValue scale
    protected MeasurementValue measurement
    protected String unitOfMeasure
    protected float lowerBoundRegion
    protected float upperBoundRegion
    protected OntoUMLDataType ownerDomain

    //in case of enumerations
    protected OntoUMLDataType ownerStructure

    //=============================
    //Getters
    //=============================

    DataTypeStereotype getStereotype() { return stereotype }

    List<OntoUMLDataType> getDimensions() { return dimensions }

    ScaleValue getScale() { return scale }

    MeasurementValue getMeasurement() { return measurement }

    String getUnitOfMeasure() { return unitOfMeasure }

    float getLowerBoundRegion() { return lowerBoundRegion }

    float getUpperBoundRegion() { return upperBoundRegion }

    @JsonIgnore
    OntoUMLDataType getOwnerDomain() { return ownerDomain }

    OntoUMLDataType getOwnerStructure() { return ownerStructure }

    //=============================
    // Setters were overwritten to ensure
    // opposite ends in the metamodel
    //=============================

    void setStereotype(DataTypeStereotype stereotype) { this.stereotype = stereotype }

    void setScale(ScaleValue scale) { this.scale = scale }

    void setMeasurement(MeasurementValue measurement) { this.measurement = measurement }

    void setUnitOfMeasure(String unitOfMeasure) { this.unitOfMeasure = unitOfMeasure }

    void setLowerBoundRegion(float lowerBoundRegion) { this.lowerBoundRegion = lowerBoundRegion }

    void setUpperBoundRegion(float upperBoundRegion) { this.upperBoundRegion = upperBoundRegion }

    void setOwnerStructure(OntoUMLDataType ownerStructure) { this.ownerStructure = ownerStructure }

    void setDimension(OntoUMLDataType dimension){
        if(dimension==null) return
        if(!dimensions.contains(dimension)){
            dimensions.add(dimension)
        }
        dimension.ownerDomain = this
    }

    void setDimensions(List<OntoUMLDataType> dimensions){
        if(dimensions==null || dimensions==[]){
            this.dimensions.clear()
            return
        }
        dimensions.each{ d ->
            setDimension(d)
        }
    }

    void setOwnerDomain(OntoUMLDataType domain){
        ownerDomain = domain
        if(domain==null) return
        if(!domain.dimensions.contains(this)){
            domain.dimensions.add(this)
        }
    }

    //=============================
    // Stereotype Checking
    //=============================

    @JsonIgnore
    boolean isEnumeration(){ stereotype==DataTypeStereotype.ENUMERATION }

    @JsonIgnore
    boolean isDomain(){ stereotype==DataTypeStereotype.DOMAIN }

    @JsonIgnore
    boolean isDimension(){ stereotype==DataTypeStereotype.DIMENSION }

    @JsonIgnore
    boolean isDataType(){ stereotype==DataTypeStereotype.UNSET || stereotype==null }

    @JsonIgnore
    boolean isNominal() { scale!=null && scale==ScaleValue.NOMINAL }

    @JsonIgnore
    boolean isInterval() { scale!=null && scale==ScaleValue.INTERVAL }

    @JsonIgnore
    boolean isOrdinal() { scale!=null && scale==ScaleValue.ORDINAL }

    @JsonIgnore
    boolean isRational() { scale!=null && scale==ScaleValue.RATIONAL }

    @JsonIgnore
    boolean isString() { measurement!=null && measurement==MeasurementValue.STRING }

    @JsonIgnore
    boolean isInteger() { measurement!=null && measurement==MeasurementValue.INTEGER }

    @JsonIgnore
    boolean isDecimal() { measurement!=null && measurement==MeasurementValue.DECIMAL }

    @JsonIgnore
    boolean isReal() { measurement!=null && measurement==MeasurementValue.REAL }

    @JsonIgnore
    boolean isNominalString() { isNominal() && isString() }

    @JsonIgnore
    boolean isIntervalInteger() { isInterval() && isInteger() }

    @JsonIgnore
    boolean isIntervalDecimal() { isInterval() && isDecimal() }

    @JsonIgnore
    boolean isOrdinalInteger() { isOrdinal() && isInteger() }

    @JsonIgnore
    boolean isOrdinalDecimal() { isOrdinal() && isDecimal() }

    @JsonIgnore
    boolean isRationalInteger() { isRational() && isInteger() }

    @JsonIgnore
    boolean isRationalDecimal() { isRational() && isDecimal() }

    @JsonIgnore
    boolean isIntervalReal() { isInterval() && isReal() }

    @JsonIgnore
    boolean isOrdinalReal() { isOrdinal() && isReal() }

    @JsonIgnore
    boolean isRationalReal() { isRational() && isReal() }

    @JsonIgnore
    boolean isStructure() { isDimension() || isDomain() }

    String toString() { OntoUMLPrinter.print(this) }
}
