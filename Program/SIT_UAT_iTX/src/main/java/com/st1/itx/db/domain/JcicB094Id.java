package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JcicB094 聯徵股票擔保品明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicB094Id implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -2562445009888627959L;

// 資料日期
  @Column(name = "`DataYM`")
  private int dataYM = 0;

  // 擔保品控制編碼
  /* Key,左靠右補空白 */
  @Column(name = "`ClActNo`", length = 50)
  private String clActNo = " ";

  // 擔保品所有權人或代表人IDN/BAN
  /* Key,左靠，股票持有人身份證或統一證號 */
  @Column(name = "`OwnerId`", length = 10)
  private String ownerId = " ";

  // 發行機構 BAN
  /* Key, */
  @Column(name = "`CompanyId`", length = 8)
  private String companyId = " ";

  // 股票代號
  /* 左靠右補空白，請填證交所或櫃買中心所編賦之代號，上市上櫃股票須申報 */
  @Column(name = "`StockCode`", length = 10)
  private String stockCode = " ";

  // 股票種類
  /* Key,1=普通股 2=特別股 */
  @Column(name = "`StockType`")
  private int stockType = 0;

  public JcicB094Id() {
  }

  public JcicB094Id(int dataYM, String clActNo, String ownerId, String companyId, String stockCode, int stockType) {
    this.dataYM = dataYM;
    this.clActNo = clActNo;
    this.ownerId = ownerId;
    this.companyId = companyId;
    this.stockCode = stockCode;
    this.stockType = stockType;
  }

/**
	* 資料日期<br>
	* 
	* @return Integer
	*/
  public int getDataYM() {
    return this.dataYM;
  }

/**
	* 資料日期<br>
	* 
  *
  * @param dataYM 資料日期
	*/
  public void setDataYM(int dataYM) {
    this.dataYM = dataYM;
  }

/**
	* 擔保品控制編碼<br>
	* Key,左靠右補空白
	* @return String
	*/
  public String getClActNo() {
    return this.clActNo == null ? "" : this.clActNo;
  }

/**
	* 擔保品控制編碼<br>
	* Key,左靠右補空白
  *
  * @param clActNo 擔保品控制編碼
	*/
  public void setClActNo(String clActNo) {
    this.clActNo = clActNo;
  }

/**
	* 擔保品所有權人或代表人IDN/BAN<br>
	* Key,左靠，股票持有人身份證或統一證號
	* @return String
	*/
  public String getOwnerId() {
    return this.ownerId == null ? "" : this.ownerId;
  }

/**
	* 擔保品所有權人或代表人IDN/BAN<br>
	* Key,左靠，股票持有人身份證或統一證號
  *
  * @param ownerId 擔保品所有權人或代表人IDN/BAN
	*/
  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

/**
	* 發行機構 BAN<br>
	* Key,
	* @return String
	*/
  public String getCompanyId() {
    return this.companyId == null ? "" : this.companyId;
  }

/**
	* 發行機構 BAN<br>
	* Key,
  *
  * @param companyId 發行機構 BAN
	*/
  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }

/**
	* 股票代號<br>
	* 左靠右補空白，請填證交所或櫃買中心所編賦之代號，上市上櫃股票須申報
	* @return String
	*/
  public String getStockCode() {
    return this.stockCode == null ? "" : this.stockCode;
  }

/**
	* 股票代號<br>
	* 左靠右補空白，請填證交所或櫃買中心所編賦之代號，上市上櫃股票須申報
  *
  * @param stockCode 股票代號
	*/
  public void setStockCode(String stockCode) {
    this.stockCode = stockCode;
  }

/**
	* 股票種類<br>
	* Key,1=普通股 2=特別股
	* @return Integer
	*/
  public int getStockType() {
    return this.stockType;
  }

/**
	* 股票種類<br>
	* Key,1=普通股 2=特別股
  *
  * @param stockType 股票種類
	*/
  public void setStockType(int stockType) {
    this.stockType = stockType;
  }


  @Override
  public int hashCode() {
    return Objects.hash(dataYM, clActNo, ownerId, companyId, stockCode, stockType);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicB094Id jcicB094Id = (JcicB094Id) obj;
    return dataYM == jcicB094Id.dataYM && clActNo.equals(jcicB094Id.clActNo) && ownerId.equals(jcicB094Id.ownerId) && companyId.equals(jcicB094Id.companyId) && stockCode.equals(jcicB094Id.stockCode) && stockType == jcicB094Id.stockType;
  }

  @Override
  public String toString() {
    return "JcicB094Id [dataYM=" + dataYM + ", clActNo=" + clActNo + ", ownerId=" + ownerId + ", companyId=" + companyId + ", stockCode=" + stockCode + ", stockType=" + stockType + "]";
  }
}
