package net.menthor.ea2ontouml

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
import net.menthor.ontouml.stereotypes.ClassStereotype
import net.menthor.ontouml.stereotypes.DataTypeStereotype
import net.menthor.ontouml.stereotypes.RelationshipStereotype

/**
 *  This class should be called at the end of a XMI visit.
 *
 *  The whole idea here is that all OntoUML stereotypes, values and meta-attributes in the EA model
 *  are found inside the XMI tags: "<thecustomprofile> </thecustomprofile>" at the end of a "</uml:Model>" tag.
 *
 *  To illustrate this XMI structure:
 *
 *  <uml:Model xmi:type="uml:Model" name="EA_Model" visibility="public">
 *      <packagedElement>  (...) </packagedElement>
 *      <thecustomprofile> (...) </thecustomprofile>
 *      <thecustomprofile> (...) </thecustomprofile>
 *      <thecustomprofile> (...) </thecustomprofile>
 *  </uml:Model>
 *
 *  @author John Guerson
 */

class EAProfileCorrelator {

    EAMapper mapper

    EAProfileCorrelator(EAMapper mapper){
        this.mapper = mapper
    }

    void setupClassStereotypes(){
        setupClassStereotype('OntoUML__Kind', ClassStereotype.KIND)
        setupClassStereotype("OntoUML__SubKind", ClassStereotype.SUBKIND)
        setupClassStereotype("OntoUML__Collective", ClassStereotype.COLLECTIVE)
        setupClassStereotype("OntoUML__Quantity", ClassStereotype.QUANTITY)
        setupClassStereotype("OntoUML__Role", ClassStereotype.ROLE)
        setupClassStereotype("OntoUML__Phase", ClassStereotype.PHASE)
        setupClassStereotype("OntoUML__Relator", ClassStereotype.RELATOR)
        setupClassStereotype("OntoUML__Mode", ClassStereotype.MODE)
        setupClassStereotype("OntoUML__Quality", ClassStereotype.QUALITY)
        setupClassStereotype("OntoUML__Mixin", ClassStereotype.MIXIN)
        setupClassStereotype("OntoUML__RoleMixin", ClassStereotype.ROLEMIXIN)
        setupClassStereotype("OntoUML__PhaseMixin", ClassStereotype.PHASEMIXIN)
        setupClassStereotype("OntoUML__Category", ClassStereotype.CATEGORY)
        setupClassStereotype("OntoUML__Event", ClassStereotype.EVENT)
        setupClassStereotype("OntoUML__HighOrder", ClassStereotype.HIGHORDER)
    }
    void setupClassValues(){
        setupClassValue("existence","net.menthor.ontouml.values.ExistenceValue","setExistenceValue")
        setupClassValue("classification","net.menthor.ontouml.values.ClassificationValue","setClassificationValue")
    }

    void setupDataTypeStereotypes() {
        setupDataTypeStereotype("OntoUML__Dimension", DataTypeStereotype.DIMENSION)
        setupDataTypeStereotype("OntoUML__Domain", DataTypeStereotype.DOMAIN)
        setupDataTypeStereotype("OntoUML__Enumeration", DataTypeStereotype.ENUMERATION)
    }
    void setupDataTypeValues(){
        setupDataTypeValue("scale","net.menthor.ontouml.values.ScaleValue","setScale")
        setupDataTypeValue("basicType","net.menthor.ontouml.values.MeasurementValue","setMeasurement")
        setupDataTypeAttribute("upperBound", "toFloat", "setUpperBoundRegion")
        setupDataTypeAttribute("lowerBound", "toFloat", "setLowerBoundRegion")
        setupDataTypeAttribute("unitOfMeasure", "toString", "setUnitOfMeasure")
    }

    void setupAssociationStereotypes(){
        setupAssociationStereotype("OntoUML__Mediation", RelationshipStereotype.MEDIATION)
        setupAssociationStereotype("OntoUML__Material", RelationshipStereotype.MATERIAL)
        setupAssociationStereotype("OntoUML__Formal", RelationshipStereotype.FORMAL)
        setupAssociationStereotype("OntoUML__Causation", RelationshipStereotype.CAUSATION)
        setupAssociationStereotype("OntoUML__Characterization", RelationshipStereotype.CHARACTERIZATION)
        setupAssociationStereotype("OntoUML__ComponentOf", RelationshipStereotype.COMPONENTOF)
        setupAssociationStereotype("OntoUML__Constitution", RelationshipStereotype.CONSTITUTION)
        setupAssociationStereotype("OntoUML__Derivation", RelationshipStereotype.DERIVATION)
        setupAssociationStereotype("OntoUML__InstanceOf", RelationshipStereotype.INSTANCEOF)
        setupAssociationStereotype("OntoUML__MemberOf", RelationshipStereotype.MEMBEROF)
        setupAssociationStereotype("OntoUML__Participation", RelationshipStereotype.PARTICIPATION)
        setupAssociationStereotype("OntoUML__QuaPartOf", RelationshipStereotype.QUAPARTOF)
        setupAssociationStereotype("OntoUML__Structuration", RelationshipStereotype.STRUCTURATION)
        setupAssociationStereotype("OntoUML__SubCollectionOf", RelationshipStereotype.SUBCOLLECTIONOF)
        setupAssociationStereotype("OntoUML__SubEventOf", RelationshipStereotype.SUBEVENTOF)
        setupAssociationStereotype("OntoUML__SubQuantityOf", RelationshipStereotype.SUBQUANTITYOF)
        setupAssociationStereotype("OntoUML__Temporal", RelationshipStereotype.TEMPORAL)
    }
    void setupAssociationValues(){
        setupAssociationValue("cyclicity", "net.menthor.ontouml.values.CiclicityValue","setCiclicityValue")
        setupAssociationValue("transitivity", "net.menthor.ontouml.values.TransitivityValue","setTransitivityValue")
        setupAssociationValue("reflexivity", "net.menthor.ontouml.values.ReflexivityValue","setReflexivityValue")
        setupAssociationValue("symmetry", "net.menthor.ontouml.values.SymmetryValue","setSymmetryValue")
        SetupAssociationTgtDependency("partEssential")
        SetupAssociationTgtDependency("wholeImmutable")
        setupAssociationSrcDependency("partInseparable")
        setupAssociationSrcDependency("partImmutable")
    }

    // ====== Stereotype =======

    def setupAssociationStereotype = { eaStereo, ontoumlStereo ->
        mapper.eaRoot.'uml:Model'."$eaStereo".each{ k ->
            def id = k.'@base_Association'.text()
            OntoUMLRelationship ontoRelationship = mapper.getAssociationById(id)
            if(ontoRelationship!=null) ontoRelationship.setStereotype(ontoumlStereo)
            else mapper.log.appendLine("[EA] An error occurred when relating stereotypes EA:"+eaStereo+" and Menthor:"+ontoumlStereo)
        }
    }

    def setupClassStereotype = { eaStereo, ontoumlStereo ->
        mapper.eaRoot.'uml:Model'."$eaStereo".each{ k ->
            def id = k.'@base_Class'.text()
            if(id.isEmpty()) id = k.'@base_AssociationClass'.text()
            OntoUMLClass ontoClass = mapper.getTypeById(id)
            if(ontoClass!=null) ontoClass.setStereotype(ontoumlStereo)
            else mapper.log.appendLine("[EA] A SEVERE ERROR occurred when relating stereotypes EA:"+eaStereo+" and Menthor:"+ontoumlStereo)
        }
    }

    def setupDataTypeStereotype = { eaStereo, ontoumlStereo ->
        mapper.eaRoot.'uml:Model'."$eaStereo".each{ k ->
            def id = k.'@base_DataType'.text()
            if(id.isEmpty()) id = k.'@base_Enumeration'.text()
            OntoUMLDataType ontoDataType = mapper.getTypeById(id)
            if(ontoDataType!=null) ontoDataType.setStereotype(ontoumlStereo)
            else mapper.log.appendLine("[EA] A SEVERE ERROR occurred when relating stereotypes EA:"+eaStereo+" and Menthor:"+ontoumlStereo)
        }
    }

    // ====== Value =======

    def setupClassValue = { eaStereo, ontoValueClass, ontoSetMethod ->
        mapper.eaRoot.'uml:Model'."$eaStereo".each{ k ->
            def id = k.'@base_Class'.text()
            if(id.isEmpty()) id = k.'@base_AssociationClass'.text()
            def var = "@"+eaStereo
            def value = k."$var".text()
            OntoUMLClass ontoClass = mapper.getTypeById(id)
            def ontoValue = ("$ontoValueClass" as Class).getEnum(value)
            //println eaStereo+"="+ontoValue
            if(ontoClass!=null && ontoValue!=null) ontoClass."$ontoSetMethod"(ontoValue)
        }
    }

    def setupDataTypeValue = { eaStereo, ontoValueClass, ontoSetMethod ->
        mapper.eaRoot.'uml:Model'."$eaStereo".each{ k ->
            def id = k.'@base_DataType'.text()
            def var = "@"+eaStereo
            def value = k."$var".text()
            OntoUMLDataType ontoDataType = mapper.getTypeById(id)
            def ontoValue = ("$ontoValueClass" as Class).getEnum(value)
            //println eaStereo+"="+ontoValue
            if(ontoDataType!=null && ontoValue!=null) ontoDataType."$ontoSetMethod"(ontoValue)
        }
    }

    def setupAssociationValue = { eaStereo, ontoValueClass, ontoSetMethod ->
        mapper.eaRoot.'uml:Model'."$eaStereo".each{ k ->
            def id = k.'@base_Association'.text()
            def var = "@"+eaStereo
            def value = k."$var".text()
            OntoUMLRelationship ontoRelationship = mapper.getAssociationById(id)
            def ontoValue = ("$ontoValueClass" as Class).getEnum(value)
            //println eaStereo+"="+ontoValue
            if(ontoRelationship!=null)  ontoRelationship."$ontoSetMethod"(ontoValue)
        }
    }

    // ====== Meta attribute =======

    def setupDataTypeAttribute = { eaStereo, primitiveConversionMethod, ontoSetMethod ->
        mapper.eaRoot.'uml:Model'."$eaStereo".each{ k ->
            def id = k.'@base_DataType'.text()
            if(id.isEmpty()) id = k.'@base_Enumeration'.text()
            def var = "@"+eaStereo
            def attr = k."$var".text()."$primitiveConversionMethod"()
            OntoUMLDataType ontoDataType = mapper.getTypeById(id)
            //println eaStereo+"="+attr
            if(ontoDataType!=null && attr!=null) ontoDataType."$ontoSetMethod"(attr)
        }
    }

    def SetupAssociationTgtDependency = { eaStereo ->
        mapper.eaRoot.'uml:Model'."$eaStereo".each{ k ->
            def id = k.'@base_Association'.text()
            OntoUMLRelationship ontoRelationship = mapper.getAssociationById(id)
            def var = "@"+eaStereo
            def value = k."$var".text().toBoolean()
            //println eaStereo+"="+value
            if(ontoRelationship!=null && value!=null)  ontoRelationship.targetEndPoint().setDependency(value)
        }
    }

    def setupAssociationSrcDependency = { eaStereo ->
        mapper.eaRoot.'uml:Model'."$eaStereo".each { k ->
            def id = k.'@base_Association'.text()
            OntoUMLRelationship ontoRelationship = mapper.getAssociationById(id)
            def var = "@"+eaStereo
            def value = k."$var".text().toBoolean()
            //println eaStereo+"="+value
            if (ontoRelationship != null && value!=null) ontoRelationship.sourceEndPoint().setDependency(value)
        }
    }
}
