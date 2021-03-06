package net.menthor.ontouml.rules.types.attribute

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
import net.menthor.ontouml.rules.traits.AttributeSyntacticalRule

/**
 * @author John Guerson
 */
class MixinsAbstractRule implements AttributeSyntacticalRule {

    MixinsAbstractRule(OntoUMLClass self){
        this.description = 'A Mixin class (Category, PhaseMixin, RoleMixin, Mixin) must always be abstract'
        this.self = self
    }

    @Override
    boolean condition() {
        if(self.isMixinClass()) {
            return self.isAbstract_()
        }
        return true
    }

    @Override
    boolean quickFix(){
        def rel = (this.self as OntoUMLClass)
        if(rel.isMixinClass()) {
            rel.setIsAbstract(true)
            return true
        }
        return false
    }
}