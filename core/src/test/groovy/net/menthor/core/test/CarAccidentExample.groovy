package net.menthor.core.test

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

import net.menthor.core.MClass
import net.menthor.core.MFactory
import net.menthor.core.MPackage
import net.menthor.core.MRelationship
import net.menthor.core.MSerializer

/**
 * @author John Guerson
 */
class CarAccidentExample {

    static void main(String[] args) {
        MPackage m = generate()

        //save as JSON file
        MSerializer s = new MSerializer()
        s.toFormattedJSONFile(m,"core/src/test/groovy/net/menthor/core/test/json/","CarAccident")

        println "Car Accident generated"
    }

    static MPackage generate(){

        //create the model
        MPackage m = MFactory.createModel("Car Accident")

        //create kinds
        MClass person = m.createClass("Person")
        MClass vehicle = m.createClass("Vehicle")
        MClass roadway = m.createClass("Roadway")

        //create subkinds
        MClass man = m.createClass("Man")
        MClass woman = m.createClass("Woman")

        //create a partition between man and woman
        List genders = [man, woman]
        m.createPartition(genders, person)

        //create phases
        MClass living = m.createClass("Living")
        MClass deceased = m.createClass("Deceased")

        //create a partition between living and deceased
        List nature = [living, deceased]
        m.createPartition(nature, person)

        //create roles
        MClass traveler = m.createClass("Traveler")
        MClass victim = m.createClass("Victim")
        MClass crashedVehicle = m.createClass("CrashedVehicle")

        //create relators
        MClass accident = m.createClass("Traffic Accident")
        MClass rearEndCollision = m.createClass("Rear End Collision")
        MClass travel = m.createClass("Travel")

        // create generalizations/specializations
        m.createGeneralization(traveler, person)
        m.createGeneralization(victim, person)
        m.createGeneralization(crashedVehicle, vehicle)
        m.createGeneralization(rearEndCollision, accident)

        //create mediations
        m.createRelationship(accident, 1, -1, "occurs", roadway, 1, 1)
        m.createRelationship(accident, 1, 1, "has", victim, 1, -1)
        m.createRelationship(accident, 1, 1, "involves", crashedVehicle, 1, -1)
        m.createRelationship(travel, 1, 1, "has", traveler, 1, -1)
        m.createRelationship(travel, 1, 1, "made by", vehicle, 1, 1)

        //create material relationship
        MRelationship material = m.createRelationship(victim, 1, -1, "has been victim in", roadway, 1, 1)

        //create the derivation
        m.createRelationship(material, accident);

        //print
        m.getElements().each{ e -> println e }

        return m
    }
}
