package net.menthor.ontouml.test

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
import net.menthor.ontouml.OntoUMLFactory
import net.menthor.ontouml.OntoUMLModel
import net.menthor.ontouml.OntoUMLRelationship
    import net.menthor.ontouml.OntoUMLSerializer

/**
 * @author John Guerson
 */
class CarAccidentExample {

    static void main(String[] args) {
        OntoUMLModel m = generate()

        //save as JSON file
        OntoUMLSerializer s = new OntoUMLSerializer()
        s.toFormattedJSONFile(m,"ontouml/src/test/groovy/net/menthor/ontouml/test/json/","CarAccident")

        println "Car Accident generated"
    }

    static OntoUMLModel generate(){

        //create the model
        OntoUMLModel m = OntoUMLFactory.createModel("Car Accident")

        //create kinds
        OntoUMLClass person = m.createKind("Person")
        OntoUMLClass vehicle = m.createKind("Vehicle")
        OntoUMLClass roadway = m.createKind("Roadway")

        //create subkinds
        OntoUMLClass man = m.createSubKind("Man")
        OntoUMLClass woman = m.createSubKind("Woman")

        //create a partition between man and woman
        List genders = [man, woman]
        m.createPartition("gender", genders, person)

        //create phases
        OntoUMLClass living = m.createPhase("Living")
        OntoUMLClass deceased = m.createPhase("Deceased")

        //create a partition between living and deceased
        List nature = [living, deceased]
        m.createPartition("life", nature, person)

        //create roles
        OntoUMLClass traveler = m.createRole("Traveler")
        OntoUMLClass victim = m.createRole("Victim")
        OntoUMLClass crashedVehicle = m.createRole("CrashedVehicle")

        //create relators
        OntoUMLClass accident = m.createRelator("Traffic Accident")
        OntoUMLClass rearEndCollision = m.createRelator("Rear End Collision")
        OntoUMLClass travel = m.createRelator("Travel")

        // create generalizations/specializations
        m.createGeneralization(traveler, person)
        m.createGeneralization(victim, person)
        m.createGeneralization(crashedVehicle, vehicle)
        m.createGeneralization(rearEndCollision, accident)

        //create mediations
        m.createMediation(accident, 1, -1, "occurs", roadway, 1, 1)
        m.createMediation(accident, 1, 1, "has", victim, 1, -1)
        m.createMediation(accident, 1, 1, "involves", crashedVehicle, 1, -1)
        m.createMediation(travel, 1, 1, "has", traveler, 1, -1)
        m.createMediation(travel, 1, 1, "made by", vehicle, 1, 1)

        //create material relationship
        OntoUMLRelationship material = m.createMaterial(victim, 1, -1, "has been victim in", roadway, 1, 1)

        //create the derivation
        m.createDerivation(material, accident);

        //print
        m.getElements().each{ e -> println e }

        return m
    }
}
