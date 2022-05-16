package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
//import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("l6907ServiceImpl")
@Repository
public class L6907ServiceImpl extends ASpringJpaParm implements InitializingBean {

//	@Autowired
//	private CdCodeService sCdCodeDefService;

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;

	public List<Map<String, String>> FindAll(TitaVo titaVo, int index, int limit) throws Exception {

		this.info("L6907FindData");
		// 取得變數
		String iAcSubBookCode = titaVo.get("AcSubBookCode").trim();
		String iAcNoCode = titaVo.get("AcNoCode").trim();
		String iAcSubCode = titaVo.get("AcSubCode").trim();
		String iAcDtlCode = titaVo.get("AcDtlCode").trim();
		String iAcctCode = titaVo.get("AcctCode").trim();

		int iClsFlag = Integer.parseInt(titaVo.get("ClsFlag").toString());
		int iCustNo = Integer.parseInt(titaVo.get("CustNo").toString());
		int iFacmNo = Integer.parseInt(titaVo.get("FacmNo").toString());

		this.info("iAcSubBookCode   = " + iAcSubBookCode); //
		this.info("iAcNoCode   = " + iAcNoCode); //
		this.info("iAcSubCode  = " + iAcSubCode); //
		this.info("iAcDtlCode  = " + iAcDtlCode); //
		this.info("iAcctCode   = " + iAcctCode); // 310

		this.info("iClsFlag    = " + iClsFlag); // 0
		this.info("iCustNo     = " + iCustNo); // 0
		this.info("iFacmNo     = " + iFacmNo); // 0

		String sql = "Select";
		sql += " \"AcNoCode\" as  \"AcNoCode\", "; // F0
		sql += " \"AcSubCode\" as \"AcSubCode\", "; // F1
		sql += " \"AcDtlCode\" as \"AcDtlCode\", "; // F2
		sql += " \"AcctCode\" as \"AcctCode\",  "; // F3
		sql += " \"CustNo\" as \"CustNo\", "; // F4
		sql += " \"FacmNo\" as \"FacmNo\", "; // F5
		sql += " \"RvNo\" as \"RvNo\",  "; // F6
		sql += " CASE WHEN \"OpenAcDate\" = 0 ";
		sql += " THEN 0 ";
		sql += " ELSE TRUNC(\"OpenAcDate\" - 19110000)  END as \"OpenAcDate\", "; // F7
		sql += " \"RvAmt\" as \"RvAmt\", "; // F8
		sql += " CASE WHEN \"LastTxDate\" = 0 ";
		sql += " THEN 0 ";
		sql += " ELSE TRUNC(\"LastTxDate\" - 19110000)  END as \"LastTxDate\", "; // F9
		sql += " \"RvBal\" as \"RvBal\", "; // F10
		sql += " \"AcBookCode\" as \"AcBookCode\", "; // F11
		sql += " \"AcSubBookCode\" as \"AcSubBookCode\", "; // F12
		sql += " \"LastUpdate\" as \"LastUpdate\", "; // F13
		sql += " \"LastUpdateEmpNo\" as \"LastUpdateEmpNo\", "; // F14
		sql += " \"ReceivableFlag\" as \"ReceivableFlag\" "; // F15
//		sql += " \"ClsFlag\" as ClsFlag "; //F15 拿來判斷用的
		sql += " FROM \"AcReceivable\" ";
		sql += " where \"AcBookCode\" = 000 "; // 固定傳入
		// 加入判斷空白跳過該篩選
		// 區隔帳冊
		if (!"".equals(iAcSubBookCode)) {
			sql += "and \"AcSubBookCode\" = :iAcSubBookCode ";
		}
		// 科子項目
		if (!"".equals(iAcNoCode)) {
			sql += "and \"AcNoCode\" = :iAcNoCode   ";
		}
		if (!"".equals(iAcSubCode)) {
			sql += "and \"AcSubCode\" = :iAcSubCode  ";
		}
		if (!"".equals(iAcDtlCode)) {
			sql += "and \"AcDtlCode\" = :iAcDtlCode  ";
		}
		// 戶號
		if (iCustNo != 0) {
			sql += "and \"CustNo\" = :iCustNo  ";
		}
		if (iFacmNo != 0) {
			sql += "and \"FacmNo\" = :iFacmNo  ";
		}
		// 業務科目
		if (!"".equals(iAcctCode)) {
			sql += " and \"AcctCode\" = :iAcctCode ";
		}
		// 銷帳記號
		if (iClsFlag != 2) {
			sql += " and  \"ClsFlag\" = :iClsFlag";
		}

		this.info("L6907Service SQL=" + sql);

		Query query;
//		query = em.createNativeQuery(sql,L5051Vo.class);//進SQL 所以不要用.class (要用.class 就要使用HQL)
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		// 如果沒取得變數則不會傳入query
		if (iAcSubBookCode.length() > 0) {
			query.setParameter("iAcSubBookCode", iAcSubBookCode);
		}

		if (iAcNoCode.length() > 0) {
			query.setParameter("iAcNoCode", iAcNoCode);
		}

		if (iAcSubCode.length() > 0) {
			query.setParameter("iAcSubCode", iAcSubCode);
		}

		if (iAcDtlCode.length() > 0) {
			query.setParameter("iAcDtlCode", iAcDtlCode);
		}

		if (iCustNo != 0) {
			query.setParameter("iCustNo", iCustNo);
		}

		if (iFacmNo != 0) {
			query.setParameter("iFacmNo", iFacmNo);
		}

		if (iAcctCode.length() > 0) {
			query.setParameter("iAcctCode", iAcctCode);
		}

		if (iClsFlag != 2) {
			query.setParameter("iClsFlag", iClsFlag);
		}

		this.info("L6907Service FindData=" + query);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		// query.setFirstResult(this.index*this.limit);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		return this.convertToMap(query);

	}

}