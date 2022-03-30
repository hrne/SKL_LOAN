
package com.st1.sklwebservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IntraAuthenticated complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IntraAuthenticated">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ResultCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ResultMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ExpiredDay" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IsUserExpired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IsAccountLocked" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="accountExpires" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isLockoutTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isUserPwdExpired" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserCName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserEmpid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserUnitName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserUnitId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserBranchName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserBranchId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserDeptName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserDeptId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserFullDeptName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserFullUnitName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserEmail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserEmpBirth" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IsFifteenSalary" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IntraAuthenticated", propOrder = {
    "resultCode",
    "resultMessage",
    "expiredDay",
    "isUserExpired",
    "isAccountLocked",
    "accountExpires",
    "isLockoutTime",
    "isUserPwdExpired",
    "userCName",
    "userEmpid",
    "userId",
    "userUnitName",
    "userUnitId",
    "userBranchName",
    "userBranchId",
    "userDeptName",
    "userDeptId",
    "userFullDeptName",
    "userFullUnitName",
    "userEmail",
    "userEmpBirth",
    "isFifteenSalary"
})
public class IntraAuthenticated {

    @XmlElement(name = "ResultCode")
    protected String resultCode;
    @XmlElement(name = "ResultMessage")
    protected String resultMessage;
    @XmlElement(name = "ExpiredDay")
    protected String expiredDay;
    @XmlElement(name = "IsUserExpired")
    protected String isUserExpired;
    @XmlElement(name = "IsAccountLocked")
    protected String isAccountLocked;
    protected String accountExpires;
    protected String isLockoutTime;
    protected String isUserPwdExpired;
    @XmlElement(name = "UserCName")
    protected String userCName;
    @XmlElement(name = "UserEmpid")
    protected String userEmpid;
    @XmlElement(name = "UserId")
    protected String userId;
    @XmlElement(name = "UserUnitName")
    protected String userUnitName;
    @XmlElement(name = "UserUnitId")
    protected String userUnitId;
    @XmlElement(name = "UserBranchName")
    protected String userBranchName;
    @XmlElement(name = "UserBranchId")
    protected String userBranchId;
    @XmlElement(name = "UserDeptName")
    protected String userDeptName;
    @XmlElement(name = "UserDeptId")
    protected String userDeptId;
    @XmlElement(name = "UserFullDeptName")
    protected String userFullDeptName;
    @XmlElement(name = "UserFullUnitName")
    protected String userFullUnitName;
    @XmlElement(name = "UserEmail")
    protected String userEmail;
    @XmlElement(name = "UserEmpBirth")
    protected String userEmpBirth;
    @XmlElement(name = "IsFifteenSalary")
    protected String isFifteenSalary;

    /**
     * Gets the value of the resultCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultCode() {
        return resultCode;
    }

    /**
     * Sets the value of the resultCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultCode(String value) {
        this.resultCode = value;
    }

    /**
     * Gets the value of the resultMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultMessage() {
        return resultMessage;
    }

    /**
     * Sets the value of the resultMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultMessage(String value) {
        this.resultMessage = value;
    }

    /**
     * Gets the value of the expiredDay property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpiredDay() {
        return expiredDay;
    }

    /**
     * Sets the value of the expiredDay property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpiredDay(String value) {
        this.expiredDay = value;
    }

    /**
     * Gets the value of the isUserExpired property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsUserExpired() {
        return isUserExpired;
    }

    /**
     * Sets the value of the isUserExpired property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsUserExpired(String value) {
        this.isUserExpired = value;
    }

    /**
     * Gets the value of the isAccountLocked property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsAccountLocked() {
        return isAccountLocked;
    }

    /**
     * Sets the value of the isAccountLocked property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsAccountLocked(String value) {
        this.isAccountLocked = value;
    }

    /**
     * Gets the value of the accountExpires property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountExpires() {
        return accountExpires;
    }

    /**
     * Sets the value of the accountExpires property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountExpires(String value) {
        this.accountExpires = value;
    }

    /**
     * Gets the value of the isLockoutTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsLockoutTime() {
        return isLockoutTime;
    }

    /**
     * Sets the value of the isLockoutTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsLockoutTime(String value) {
        this.isLockoutTime = value;
    }

    /**
     * Gets the value of the isUserPwdExpired property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsUserPwdExpired() {
        return isUserPwdExpired;
    }

    /**
     * Sets the value of the isUserPwdExpired property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsUserPwdExpired(String value) {
        this.isUserPwdExpired = value;
    }

    /**
     * Gets the value of the userCName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserCName() {
        return userCName;
    }

    /**
     * Sets the value of the userCName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserCName(String value) {
        this.userCName = value;
    }

    /**
     * Gets the value of the userEmpid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserEmpid() {
        return userEmpid;
    }

    /**
     * Sets the value of the userEmpid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserEmpid(String value) {
        this.userEmpid = value;
    }

    /**
     * Gets the value of the userId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserId(String value) {
        this.userId = value;
    }

    /**
     * Gets the value of the userUnitName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserUnitName() {
        return userUnitName;
    }

    /**
     * Sets the value of the userUnitName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserUnitName(String value) {
        this.userUnitName = value;
    }

    /**
     * Gets the value of the userUnitId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserUnitId() {
        return userUnitId;
    }

    /**
     * Sets the value of the userUnitId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserUnitId(String value) {
        this.userUnitId = value;
    }

    /**
     * Gets the value of the userBranchName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserBranchName() {
        return userBranchName;
    }

    /**
     * Sets the value of the userBranchName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserBranchName(String value) {
        this.userBranchName = value;
    }

    /**
     * Gets the value of the userBranchId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserBranchId() {
        return userBranchId;
    }

    /**
     * Sets the value of the userBranchId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserBranchId(String value) {
        this.userBranchId = value;
    }

    /**
     * Gets the value of the userDeptName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserDeptName() {
        return userDeptName;
    }

    /**
     * Sets the value of the userDeptName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserDeptName(String value) {
        this.userDeptName = value;
    }

    /**
     * Gets the value of the userDeptId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserDeptId() {
        return userDeptId;
    }

    /**
     * Sets the value of the userDeptId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserDeptId(String value) {
        this.userDeptId = value;
    }

    /**
     * Gets the value of the userFullDeptName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserFullDeptName() {
        return userFullDeptName;
    }

    /**
     * Sets the value of the userFullDeptName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserFullDeptName(String value) {
        this.userFullDeptName = value;
    }

    /**
     * Gets the value of the userFullUnitName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserFullUnitName() {
        return userFullUnitName;
    }

    /**
     * Sets the value of the userFullUnitName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserFullUnitName(String value) {
        this.userFullUnitName = value;
    }

    /**
     * Gets the value of the userEmail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * Sets the value of the userEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserEmail(String value) {
        this.userEmail = value;
    }

    /**
     * Gets the value of the userEmpBirth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserEmpBirth() {
        return userEmpBirth;
    }

    /**
     * Sets the value of the userEmpBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserEmpBirth(String value) {
        this.userEmpBirth = value;
    }

    /**
     * Gets the value of the isFifteenSalary property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsFifteenSalary() {
        return isFifteenSalary;
    }

    /**
     * Sets the value of the isFifteenSalary property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsFifteenSalary(String value) {
        this.isFifteenSalary = value;
    }

}
