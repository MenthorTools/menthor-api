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

import net.menthor.core.MPrinter
import net.menthor.ontouml.stereotypes.ClassStereotype
import net.menthor.ontouml.stereotypes.ConstraintStereotype
import net.menthor.ontouml.stereotypes.DataTypeStereotype
import net.menthor.ontouml.stereotypes.PrimitiveStereotype
import net.menthor.ontouml.stereotypes.RelationshipStereotype
import net.menthor.core.traits.MClassifier
import net.menthor.core.traits.MElement
import net.menthor.core.traits.MNamedElement
import net.menthor.core.traits.MProperty

/**
 * OntoUML printer.
 * @author John Guerson
 */
class OntoUMLPrinter extends MPrinter {

    protected OntoUMLPrinter() {}

    @Override
    static String print(MElement elem){
        if (elem instanceof OntoUMLModel) return stereotypeAndName(elem, false, false);
        if (elem instanceof OntoUMLPackage) return stereotypeAndName(elem, false, false);
        if (elem instanceof OntoUMLClass || elem instanceof OntoUMLDataType)return stereotypeAndName(elem, true, false);
        if(elem instanceof OntoUMLConstraint) return stereotypeAndName(elem,true,false)
        if (elem instanceof OntoUMLRelationship) return print(elem as OntoUMLRelationship)
        if (elem instanceof OntoUMLGeneralization) return print(elem as OntoUMLGeneralization)
        if (elem instanceof OntoUMLGeneralizationSet) return print(elem as OntoUMLGeneralizationSet)
        if (elem instanceof OntoUMLAttribute) return print(elem as OntoUMLAttribute)
        if (elem instanceof OntoUMLEndPoint) return print(elem as OntoUMLEndPoint)
        if (elem instanceof OntoUMLLiteral) return print(elem as OntoUMLLiteral)
        if(elem instanceof OntoUMLComment) return print(elem as OntoUMLComment)
        return stereotypeAndName(elem, true, false);
    }

    static String print(OntoUMLRelationship a){
        String result = stereotypeAndName(a,true, false)+" {"
        int i=1
        for(OntoUMLEndPoint ep: a.getEndPoints()){
            if(i<a.getEndPoints().size())
                result += name(ep.getClassifier())+" -> "
            else
                result += name(ep.getClassifier())+"}"
            i++;
        }
        return result;
    }

    static String print(OntoUMLGeneralization g){
        def result = new String()
        result += stereotype(g)+" {"+name(g.getSpecific())+" -> "+name(g.getGeneral())+"}";
        return result;
    }

    static String print(OntoUMLGeneralizationSet genset){
        def result = new String()
        MClassifier general = genset.general();
        result += stereotypeAndName(genset, false, false) + " / "+name(general)+" { ";
        int i=1;
        for(MClassifier specific: genset.specifics()){
            if(i < genset.specifics().size())
                result += name(specific)+", ";
            else
                result += name(specific) + " } ";
            i++;
        }
        return result;
    }

    static String print(OntoUMLAttribute p){
        return stereotypeAndName(p,true,false)+ " ["+multiplicity(p,false,"..")+"]";
    }

    static String print(OntoUMLEndPoint p){
        return stereotype(p)+" ("+name(p)+") "+name(p.getClassifier()) +" ["+multiplicity(p,false,"..")+"]";
    }

    static String print(OntoUMLLiteral p){
        return stereotype(p)+" ("+p.getText()+")";
    }

    static String print(OntoUMLComment p){
        return stereotype(p);
    }

    //======================================
    //Stereotype
    //======================================

    static String stereotype(OntoUMLClass c){
        if(c.getStereotype()!=null && c.getStereotype()!=ClassStereotype.UNSET) {
            return c.getStereotype().getName()
        } else return "Class"
    }

    static String stereotype(OntoUMLRelationship r){
        if(r.getStereotype()!=null && r.getStereotype()!=RelationshipStereotype.UNSET) {
            return r.getStereotype().getName()
        } else return "Relationship"
    }

    static String stereotype(OntoUMLDataType d){
        if(d.getStereotype()!=null && d.getStereotype()!=DataTypeStereotype.UNSET) {
            return d.getStereotype().getName()
        } else return "DataType"
    }

    static String stereotype(OntoUMLAttribute a){
        if(a.getStereotype()!=null && a.getStereotype()!=PrimitiveStereotype.UNSET) {
            return a.getStereotype().getName()
        } else return "Attribute"
    }

    static String stereotype(OntoUMLConstraint c){
        if(c.getStereotype()!=null && c.getStereotype()!= ConstraintStereotype.UNSET) {
            return c.getStereotype().getName()
        } else return "Constraint"
    }

    static String stereotype(MElement elem){
        if(elem==null) return "Null"
        if(elem instanceof OntoUMLClass) return stereotype(elem as OntoUMLClass)
        if(elem instanceof OntoUMLRelationship) return stereotype(elem as OntoUMLRelationship)
        if(elem instanceof OntoUMLDataType) return stereotype(elem as OntoUMLDataType)
        if(elem instanceof OntoUMLAttribute) return stereotype(elem as OntoUMLAttribute)
        if(elem instanceof OntoUMLConstraint) return stereotype(elem as OntoUMLConstraint)
        if(elem instanceof OntoUMLEndPoint) return "EndPoint"
        if(elem instanceof OntoUMLModel) return "Model"
        if(elem instanceof OntoUMLPackage) return "Package"
        if(elem instanceof OntoUMLGeneralizationSet) return "GeneralizationSet"
        if(elem instanceof OntoUMLGeneralization) return "Generalization"
        if(elem instanceof OntoUMLComment) return "Comment"
        if(elem instanceof OntoUMLLiteral) return "Literal"
        return "UnknownType"
    }

    static boolean hasStereotype(MElement elem){
        boolean hasStereotypeDefined=false
        if(elem instanceof OntoUMLClass && (elem as OntoUMLClass).getStereotype()!=null && (elem as OntoUMLClass).getStereotype()!=ClassStereotype.UNSET){
            hasStereotypeDefined=true
        } else if(elem instanceof OntoUMLRelationship && (elem as OntoUMLRelationship).getStereotype()!=null && (elem as OntoUMLRelationship).getStereotype()!=RelationshipStereotype.UNSET){
            hasStereotypeDefined=true
        } else if(elem instanceof OntoUMLDataType && (elem as OntoUMLDataType).getStereotype()!=null && (elem as OntoUMLDataType).getStereotype()!=DataTypeStereotype.UNSET){
            hasStereotypeDefined=true
        } else if(elem instanceof OntoUMLConstraint && (elem as OntoUMLConstraint).getStereotype()!=null && (elem as OntoUMLConstraint).getStereotype()!=ConstraintStereotype.UNSET){
            hasStereotypeDefined=true
        } else if(elem instanceof OntoUMLAttribute && (elem as OntoUMLAttribute).getStereotype()!=null && (elem as OntoUMLAttribute).getStereotype()!=PrimitiveStereotype.UNSET){
            hasStereotypeDefined=true
        }
        return hasStereotypeDefined
    }

    static String stereotype(MElement elem, boolean addGuillemets) {
        if(hasStereotype(elem)) {
            //Changed to unicode because on mac this character appear like "?"
            if(addGuillemets) return "\u00AB"+stereotype(elem)+"\u00BB"
            return stereotype(elem)
        }else{
            return stereotype(elem)
        }
    }

    //======================================
    //Name + Stereotype
    //======================================

    static String stereotypeAndName(MElement elem, boolean addGuillemets, boolean addSingleQuotes) {
        String n = ""
        if(elem instanceof MNamedElement) n = " "+name(elem,addSingleQuotes,false)
        return stereotype(elem,addGuillemets)+n
    }

    static String nameAndStereotype(MElement elem, boolean addGuillemets, boolean addSingleQuotes){
        String n = ""
        if(elem instanceof MNamedElement) n = name(elem,addSingleQuotes,false)
        return n+" ("+stereotype(elem,addGuillemets)+")"
    }

    static String nameAndStereotype(MProperty p, boolean addTypeStereotype){
        if(p instanceof OntoUMLAttribute){
            if(addTypeStereotype) return name(p, true, false)+" ("+stereotypeAndName(p, true, false)+")"
            else return name(p, true, false)+" (PrimitiveType)"
        }
        if(p instanceof OntoUMLEndPoint){
            if(addTypeStereotype) return name(p, true, false)+" ("+stereotypeAndName(((OntoUMLEndPoint)p).getClassifier(), true, false)+")"
            else return name(p, true, false)+" ("+name(((OntoUMLEndPoint)p).getClassifier())+")"
        }
        return name(p, true, false)+" (typeless)"
    }

    static String nameAndStereotype(MProperty p){
        if(p instanceof OntoUMLAttribute){
            return name(p, true, false)+" ("+stereotype(p)+")"
        }
        if(p instanceof OntoUMLEndPoint){
            return name(p, true, false)+" ("+name(((OntoUMLEndPoint)p).getClassifier())+")"
        }
        return name(p, true, false)+" (typeless)"

    }

    //======================================
    //Name + Stereotype + Multiplicity
    //======================================

    static String nameStereotypeAndMultiplicity(MProperty p, boolean quotePropertyName, boolean quoteTypeName, boolean alwaysShowLowerAndUpper, boolean addTypeStereotype, boolean guillemetTypeStereotype){
        if(p instanceof OntoUMLEndPoint){
            String typeDesc = new String()
            if(addTypeStereotype) typeDesc = stereotypeAndName(((OntoUMLEndPoint)p).getClassifier(), guillemetTypeStereotype, quoteTypeName)
            else typeDesc = name(((OntoUMLEndPoint)p).getClassifier(),quoteTypeName,false)
            return name(p, quotePropertyName, false)+" ["+multiplicity(p, alwaysShowLowerAndUpper, "..")+"] ("+typeDesc+")"
        }
        if(p instanceof OntoUMLAttribute){
            String typeDesc = new String();
            if(addTypeStereotype) typeDesc = stereotypeAndName(((OntoUMLAttribute)p), guillemetTypeStereotype, quoteTypeName)
            else typeDesc = name(((OntoUMLAttribute)p),quoteTypeName,false)
            return name(p, quotePropertyName, false)+" ["+multiplicity(p, alwaysShowLowerAndUpper, "..")+"] ("+typeDesc+")"
        }
        return name(p, quotePropertyName, false)+" ["+multiplicity(p, alwaysShowLowerAndUpper, "..")+"] (typeless)"
    }

    //======================================
    //All details
    //======================================

    static String allDetails(OntoUMLRelationship a){
        String result = stereotypeAndName(a,true, true)+" {"
        int i=1
        for(OntoUMLEndPoint ep: a.getEndPoints()){
            if(i<a.getEndPoints().size())
                result += name(ep.getClassifier(),true,false)+"("+multiplicity(ep, true, "..")+")"+" -> "
            else
                result += name(ep.getClassifier(),true,false)+"("+multiplicity(ep, true, "..")+")"+"}"
            i++
        }
        return result
    }
}
