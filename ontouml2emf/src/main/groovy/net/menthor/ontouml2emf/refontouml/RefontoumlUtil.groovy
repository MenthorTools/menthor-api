package net.menthor.ontouml2emf.refontouml

import RefOntoUML.util.RefOntoUMLResourceFactoryImpl
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.xmi.XMLResource
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl

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

class RefontoumlUtil {

    static RefOntoUML.Package deserialize(String path){
        ResourceSet rset = new ResourceSetImpl();
        rset.getResourceFactoryRegistry().getExtensionToFactoryMap().put("refontouml",new RefOntoUMLResourceFactoryImpl());
        rset.getPackageRegistry().put(RefOntoUML.RefOntoUMLPackage.eNS_URI,	RefOntoUML.RefOntoUMLPackage.eINSTANCE);
        File file = new File(path);
        URI fileURI = URI.createFileURI(file.getAbsolutePath());
        Resource resource = rset.createResource(fileURI);
        /**Load options that significantly improved the performance of loading EMF instances*/
        Map<Object,Object> loadOptions = ((XMLResourceImpl)resource).getDefaultLoadOptions();
        loadOptions.put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl());
        loadOptions.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
        resource.load(loadOptions);
        return resource.getContents().get(0);
    }

    static RefOntoUML.Package deserialize(InputStream is){
        ResourceSet rset = new ResourceSetImpl();
        rset.getResourceFactoryRegistry().getExtensionToFactoryMap().put("refontouml",new RefOntoUMLResourceFactoryImpl());
        rset.getPackageRegistry().put(RefOntoUML.RefOntoUMLPackage.eNS_URI,	RefOntoUML.RefOntoUMLPackage.eINSTANCE);
        Resource resource = rset.createResource(URI.createURI("fakeFile.refontouml"))
        /**Load options that significantly improved the performance of loading EMF instances*/
        Map<Object,Object> loadOptions = ((XMLResourceImpl)resource).getDefaultLoadOptions();
        loadOptions.put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl());
        loadOptions.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
        try {
            resource.load(is, loadOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resource.getContents().get(0)
    }

    static Resource serialize(RefOntoUML.Package model, String path){
        ResourceSet rset = new ResourceSetImpl()
        rset.getResourceFactoryRegistry().getExtensionToFactoryMap().put("refontouml",new RefOntoUMLResourceFactoryImpl())
        rset.getPackageRegistry().put(RefOntoUML.RefOntoUMLPackage.eNS_URI,	RefOntoUML.RefOntoUMLPackage.eINSTANCE)
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

    static String asXMLString(RefOntoUML.Package model){
        ResourceSet rset = new ResourceSetImpl()
        rset.getResourceFactoryRegistry().getExtensionToFactoryMap().put("refontouml",new RefOntoUMLResourceFactoryImpl())
        rset.getPackageRegistry().put(RefOntoUML.RefOntoUMLPackage.eNS_URI,	RefOntoUML.RefOntoUMLPackage.eINSTANCE)
        URI fileURI = URI.createFileURI("fake.refontouml")
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
