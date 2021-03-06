package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * PostAuthLog 郵局授權記錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class PostAuthLogId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

// 建檔日期
  @Column(name = "`AuthCreateDate`")
  private int authCreateDate = 0;

  // 申請代號，狀態碼
  /* CdCode.AuthApplCode1:申請2:終止3:郵局終止4:誤終止9:暫停授權(DeleteDate &amp;gt; 0時，顯示用) */
  @Column(name = "`AuthApplCode`", length = 1)
  private String authApplCode = " ";

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 帳戶別
  /* CdCode.PostDepCodeP:存簿G:劃撥 */
  @Column(name = "`PostDepCode`", length = 1)
  private String postDepCode = " ";

  // 儲金帳號
  @Column(name = "`RepayAcct`", length = 14)
  private String repayAcct = " ";

  // 授權類別
  /* CdCode.AuthCode1:期款2:火險 */
  @Column(name = "`AuthCode`", length = 1)
  private String authCode = " ";

  public PostAuthLogId() {
  }

  public PostAuthLogId(int authCreateDate, String authApplCode, int custNo, String postDepCode, String repayAcct, String authCode) {
    this.authCreateDate = authCreateDate;
    this.authApplCode = authApplCode;
    this.custNo = custNo;
    this.postDepCode = postDepCode;
    this.repayAcct = repayAcct;
    this.authCode = authCode;
  }

/**
	* 建檔日期<br>
	* 
	* @return Integer
	*/
  public int getAuthCreateDate() {
    return  StaticTool.bcToRoc(this.authCreateDate);
  }

/**
	* 建檔日期<br>
	* 
  *
  * @param authCreateDate 建檔日期
  * @throws LogicException when Date Is Warn	*/
  public void setAuthCreateDate(int authCreateDate) throws LogicException {
    this.authCreateDate = StaticTool.rocToBc(authCreateDate);
  }

/**
	* 申請代號，狀態碼<br>
	* CdCode.AuthApplCode
1:申請
2:終止
3:郵局終止
4:誤終止
9:暫停授權(DeleteDate &amp;gt; 0時，顯示用)
	* @return String
	*/
  public String getAuthApplCode() {
    return this.authApplCode == null ? "" : this.authApplCode;
  }

/**
	* 申請代號，狀態碼<br>
	* CdCode.AuthApplCode
1:申請
2:終止
3:郵局終止
4:誤終止
9:暫停授權(DeleteDate &amp;gt; 0時，顯示用)
  *
  * @param authApplCode 申請代號，狀態碼
	*/
  public void setAuthApplCode(String authApplCode) {
    this.authApplCode = authApplCode;
  }

/**
	* 戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 戶號<br>
	* 
  *
  * @param custNo 戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 帳戶別<br>
	* CdCode.PostDepCode
P:存簿
G:劃撥
	* @return String
	*/
  public String getPostDepCode() {
    return this.postDepCode == null ? "" : this.postDepCode;
  }

/**
	* 帳戶別<br>
	* CdCode.PostDepCode
P:存簿
G:劃撥
  *
  * @param postDepCode 帳戶別
	*/
  public void setPostDepCode(String postDepCode) {
    this.postDepCode = postDepCode;
  }

/**
	* 儲金帳號<br>
	* 
	* @return String
	*/
  public String getRepayAcct() {
    return this.repayAcct == null ? "" : this.repayAcct;
  }

/**
	* 儲金帳號<br>
	* 
  *
  * @param repayAcct 儲金帳號
	*/
  public void setRepayAcct(String repayAcct) {
    this.repayAcct = repayAcct;
  }

/**
	* 授權類別<br>
	* CdCode.AuthCode
1:期款
2:火險
	* @return String
	*/
  public String getAuthCode() {
    return this.authCode == null ? "" : this.authCode;
  }

/**
	* 授權類別<br>
	* CdCode.AuthCode
1:期款
2:火險
  *
  * @param authCode 授權類別
	*/
  public void setAuthCode(String authCode) {
    this.authCode = authCode;
  }


  @Override
  public int hashCode() {
    return Objects.hash(authCreateDate, authApplCode, custNo, postDepCode, repayAcct, authCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    PostAuthLogId postAuthLogId = (PostAuthLogId) obj;
    return authCreateDate == postAuthLogId.authCreateDate && authApplCode.equals(postAuthLogId.authApplCode) && custNo == postAuthLogId.custNo && postDepCode.equals(postAuthLogId.postDepCode) && repayAcct.equals(postAuthLogId.repayAcct) && authCode.equals(postAuthLogId.authCode);
  }

  @Override
  public String toString() {
    return "PostAuthLogId [authCreateDate=" + authCreateDate + ", authApplCode=" + authApplCode + ", custNo=" + custNo + ", postDepCode=" + postDepCode + ", repayAcct=" + repayAcct + ", authCode=" + authCode + "]";
  }
}
