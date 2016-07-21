package net.menthor

import groovy.json.JsonBuilder
import net.menthor.ontouml.rules.CardinalityRules
import net.menthor.ontouml.rules.ConnectionRules
import net.menthor.ontouml.rules.MetaAttributeRules
import net.menthor.ontouml.rules.SpecializationRules
import net.menthor.ontouml.rules.SyntacticalRules
import net.menthor.ontouml.rules.ValueRules
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

import javax.xml.ws.Response

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

/**
 * @author John Guerson
 */

@RestController
class SyntacticalRulesAPI {

    @RequestMapping(value = '/api/syntactical-rules', method = RequestMethod.GET)
    public def syntacticalRules(){
        def specBuilder = new JsonBuilder(SpecializationRules.getRules())
        def cardBuilder = new JsonBuilder(CardinalityRules.getRules())
        def attrBuilder = new JsonBuilder(MetaAttributeRules.getRules())
        def valueBuilder = new JsonBuilder(ValueRules.getRules())
        def connBuilder = new JsonBuilder(ConnectionRules.getRules())
        return "{"+
            "\"specialization\": "+specBuilder.toPrettyString()+
            ", \"cardinality\": "+cardBuilder.toPrettyString()+
            ", \"metaattribute\": "+attrBuilder.toPrettyString()+
            ", \"value\": "+valueBuilder.toPrettyString()+
            ", \"connection\": "+connBuilder.toPrettyString()+
        "}";
    }

    @RequestMapping(value = '/api/specialization-rules', method = RequestMethod.GET)
    public @ResponseBody def specializationRules(){
        return SpecializationRules.getRules()
    }

    @RequestMapping(value = '/api/cardinality-rules', method = RequestMethod.GET)
    public @ResponseBody def cardinalityRules(){
        return CardinalityRules.getRules()
    }

    @RequestMapping(value = '/api/connection-rules', method = RequestMethod.GET)
    public @ResponseBody def connectionRules(){
        return ConnectionRules.getRules()
    }

    @RequestMapping(value = '/api/metaattribute-rules', method = RequestMethod.GET)
    public @ResponseBody def metaattributeRules(){
        return MetaAttributeRules.getRules()
    }

    @RequestMapping(value = '/api/value-rules', method = RequestMethod.GET)
    public @ResponseBody def valueRules(){
        return ValueRules.getRules()
    }
}
