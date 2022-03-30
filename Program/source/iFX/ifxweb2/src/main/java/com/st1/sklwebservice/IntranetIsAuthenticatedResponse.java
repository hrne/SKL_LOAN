
package com.st1.sklwebservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Intranet_IsAuthenticatedResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "intranetIsAuthenticatedResult"
})
@XmlRootElement(name = "Intranet_IsAuthenticatedResponse")
public class IntranetIsAuthenticatedResponse {

    @XmlElement(name = "Intranet_IsAuthenticatedResult")
    protected boolean intranetIsAuthenticatedResult;

    /**
     * Gets the value of the intranetIsAuthenticatedResult property.
     * 
     */
    public boolean isIntranetIsAuthenticatedResult() {
        return intranetIsAuthenticatedResult;
    }

    /**
     * Sets the value of the intranetIsAuthenticatedResult property.
     * 
     */
    public void setIntranetIsAuthenticatedResult(boolean value) {
        this.intranetIsAuthenticatedResult = value;
    }

}
