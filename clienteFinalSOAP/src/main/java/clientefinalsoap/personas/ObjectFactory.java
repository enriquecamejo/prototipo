//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.11.10 at 07:25:48 PM UYT 
//


package clientefinalsoap.personas;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the clientefinalsoap.personas package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: clientefinalsoap.personas
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BuscarPorCIResponse }
     * 
     */
    public BuscarPorCIResponse createBuscarPorCIResponse() {
        return new BuscarPorCIResponse();
    }

    /**
     * Create an instance of {@link Persona }
     * 
     */
    public Persona createPersona() {
        return new Persona();
    }

    /**
     * Create an instance of {@link BuscarPorCIRequest }
     * 
     */
    public BuscarPorCIRequest createBuscarPorCIRequest() {
        return new BuscarPorCIRequest();
    }

}
