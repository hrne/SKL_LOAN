
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
 *         &lt;element name="IsAuthenticatedClassResult" type="{http://tempuri.org/}IntraAuthenticated" minOccurs="0"/>
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
    "isAuthenticatedClassResult"
})
@XmlRootElement(name = "IsAuthenticatedClassResponse")
public class IsAuthenticatedClassResponse {

    @XmlElement(name = "IsAuthenticatedClassResult")
    protected IntraAuthenticated isAuthenticatedClassResult;

    /**
     * Gets the value of the isAuthenticatedClassResult property.
     * 
     * @return
     *     possible object is
     *     {@link IntraAuthenticated }
     *     
     */
    public IntraAuthenticated getIsAuthenticatedClassResult() {
        return isAuthenticatedClassResult;
    }

    /**
     * Sets the value of the isAuthenticatedClassResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link IntraAuthenticated }
     *     
     */
    public void setIsAuthenticatedClassResult(IntraAuthenticated value) {
        this.isAuthenticatedClassResult = value;
    }

}
