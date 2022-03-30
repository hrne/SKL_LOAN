
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
 *         &lt;element name="AD_IsAuthenticatedResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "adIsAuthenticatedResult"
})
@XmlRootElement(name = "AD_IsAuthenticatedResponse")
public class ADIsAuthenticatedResponse {

    @XmlElement(name = "AD_IsAuthenticatedResult")
    protected boolean adIsAuthenticatedResult;

    /**
     * Gets the value of the adIsAuthenticatedResult property.
     * 
     */
    public boolean isADIsAuthenticatedResult() {
        return adIsAuthenticatedResult;
    }

    /**
     * Sets the value of the adIsAuthenticatedResult property.
     * 
     */
    public void setADIsAuthenticatedResult(boolean value) {
        this.adIsAuthenticatedResult = value;
    }

}
