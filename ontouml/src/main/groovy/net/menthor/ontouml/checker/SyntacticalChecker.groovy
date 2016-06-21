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
import net.menthor.core.traits.MClassifier
import net.menthor.core.traits.MElement
import net.menthor.ontouml.OntoUMLModel
import net.menthor.ontouml.rule.SyntacticalError

/**
 * @author John Guerson
 */
class SyntacticalChecker {

    static private List<SyntacticalError> result = []

    static private synchronized checkSpecializingRules(MElement elem){ result += SpecializingChecker.check(elem) }
    static private synchronized checkConnectingRules(MElement elem){ result += ConnectingChecker.check(elem) }
    static private synchronized checkValueRules(MElement elem){ result += ValuesChecker.check(elem) }
    static private synchronized checkMetaAttributeRules(MElement elem){ result += MetaAttributeChecker.check(elem) }
    static private synchronized checkCardinalityRules(MElement elem){ result += CardinalityChecker.check(elem) }

    static List<SyntacticalError> execute(OntoUMLModel model){
        result.clear()
        model.getElements().each { elem ->
            if (elem instanceof MClassifier) {
                def thread1 = Thread.start { checkSpecializingRules(elem) }
                def thread2 = Thread.start { checkConnectingRules(elem)}
                def thread3 = Thread.start { checkValueRules(elem)}
                def thread4 = Thread.start { checkMetaAttributeRules(elem)}
                def thread5 = Thread.start { checkCardinalityRules(elem)}
                thread1.join()
                thread2.join()
                thread3.join()
                thread4.join()
                thread5.join()
            }
        }
        return result
    }
}
