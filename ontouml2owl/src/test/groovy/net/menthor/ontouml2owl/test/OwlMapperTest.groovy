package net.menthor.ontouml2owl.test

import net.menthor.ontouml.OntoUMLSerializer
import net.menthor.ontouml2owl.OwlMapper

class OwlMapperTest {

    static void main(String[] args){

        def serializer = new OntoUMLSerializer()
        def model = serializer.fromJSONFile("ontouml2owl/src/test/resources/CarAccident.json")

        def mapper = new OwlMapper()
        mapper.run(model, null)
    }
}
