package net.menthor.ontouml2emf.ecore

import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EcorePackage
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.util.BasicExtendedMetaData
import org.eclipse.emf.ecore.util.ExtendedMetaData
import org.eclipse.emf.ecore.xmi.XMLResource
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl

class EcoreUtil {

    static Resource serialize (EPackage ecoremodel, String ecorepath){
        ResourceSet ecoreResourceSet = new ResourceSetImpl()
        URI ecoreURI = URI.createFileURI(ecorepath)
        ecoreResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new XMLResourceFactoryImpl())
        ecoreResourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE)
        // enable extended metadata
        final ExtendedMetaData extendedMetaData = new BasicExtendedMetaData(ecoreResourceSet.getPackageRegistry())
        ecoreResourceSet.getLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetaData)
        Resource resource = ecoreResourceSet.createResource(ecoreURI)
        resource.getContents().add(ecoremodel)
        try{
            resource.save(Collections.emptyMap())
        }catch(IOException e){
            e.printStackTrace()
        }
        return resource;
    }

    static String asXMLString(EPackage model){
        ResourceSet rset = new ResourceSetImpl()
        rset.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",new XMLResourceFactoryImpl())
        rset.getPackageRegistry().put(EcorePackage.eNS_URI,	EcorePackage.eINSTANCE)
        URI fileURI = URI.createFileURI("fake.ecore")
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
