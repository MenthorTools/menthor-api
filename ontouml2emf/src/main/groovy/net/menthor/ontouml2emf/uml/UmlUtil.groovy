package net.menthor.ontouml2emf.uml

import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.xmi.XMLResource
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl
import org.eclipse.uml2.uml.Package
import org.eclipse.uml2.uml.UMLPackage
import org.eclipse.uml2.uml.resource.UMLResource

class UmlUtil {

    static Resource serialize(org.eclipse.uml2.uml.Package model, String path){
        ResourceSet rset = new ResourceSetImpl()
        rset.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE)
        rset.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE)
        URI fileURI = URI.createFileURI(path)
        final Resource resource = rset.createResource(fileURI)
        resource.getContents().add(model)
        try{
            resource.save(Collections.emptyMap())
        }catch(IOException e){
            e.printStackTrace()
        }
        return resource
    }

    static String asXMLString(org.eclipse.uml2.uml.Package model){
        ResourceSet rset = new ResourceSetImpl()
        rset.getResourceFactoryRegistry().getExtensionToFactoryMap().put("uml",new XMLResourceFactoryImpl())
        rset.getPackageRegistry().put(UMLPackage.eNS_URI,	UMLPackage.eINSTANCE)
        URI fileURI = URI.createFileURI("fake.uml")
        final Resource resource = rset.createResource(fileURI)
        resource.getContents().add(model)
        Map<String, Object> options = new HashMap<String, Object>();
        options.put(XMLResource.OPTION_ENCODING, "UTF8" );
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try{
            resource.save(outputStream, Collections.emptyMap())
        }catch(IOException e){
            e.printStackTrace()
        }
        return outputStream.toString()
    }
}
