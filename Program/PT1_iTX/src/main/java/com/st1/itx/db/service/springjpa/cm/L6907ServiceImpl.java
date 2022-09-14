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

//  @Autowired
//  private CdCodeService sCdCodeDefService;

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	// *** 折返控制相關 ***
	private int limit;

	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

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
//    String[] iTmpFacmNoX = titaVo.get("TmpFacmNoX").toString(); // 指定額度
//
//    String[] thisColumn = thisLine.split("[,]");
//    String iTmpTxCode = titaVo.get("TmpTxCode").toString(); // 連動交易代號
//    if ("L3200".equals(iTmpTxCode)) {
//
//    }
		this.info("iAcSubBookCode   = " + iAcSubBookCode); //
		this.info("iAcNoCode   = " + iAcNoCode); //
		this.info("iAcSubCode  = " + iAcSubCode); //
		this.info("iAcDtlCode  = " + iAcDtlCode); //
		this.info("iAcctCode   = " + iAcctCode); // 310

		this.info("iClsFlag    = " + iClsFlag); // 0
		this.info("iCustNo     = " + iCustNo); // 0
		this.info("iFacmNo     = " + iFacmNo); // 0

		String sql = "";
		sql += " WITH DT1 AS ( ";
		sql += "   SELECT \"AcDate\" ";
		sql += "        , \"CustNo\" ";
		sql += "        , \"FacmNo\" ";
		sql += "        , \"TavBal\" ";
		sql += "   FROM \"DailyTav\" ";
		sql += "   WHERE \"LatestFlag\" = 'Y' ";
		sql += "     AND \"SelfUseFlag\" = 'Y' ";
		sql += " ) ";
		sql += " , DT2 AS ( ";
		sql += "   SELECT MIN(\"AcDate\") AS \"AcDate\" ";
		sql += "        , \"CustNo\" ";
		sql += "        , SUM(\"TavBal\") AS \"TavBal\" ";
		sql += "   FROM \"DailyTav\" ";
		sql += "   WHERE \"LatestFlag\" = 'Y' ";
		sql += "     AND \"SelfUseFlag\" = 'N' ";
		sql += "   GROUP BY \"CustNo\" ";
		sql += " ) ";		
		sql += " Select";
		sql += "    A.\"AcNoCode\"          AS \"AcNoCode\", ";
		sql += "    A.\"AcSubCode\"         AS \"AcSubCode\", ";
		sql += "    A.\"AcDtlCode\"         AS \"AcDtlCode\", ";
		sql += "    A.\"AcctCode\"          AS \"AcctCode\", ";
		sql += "    A.\"CustNo\"            AS \"CustNo\", ";
		sql += "    A.\"FacmNo\"            AS \"FacmNo\", ";
		sql += "    A.\"RvNo\"              AS \"RvNo\", ";
		sql += "    CASE ";
		sql += "        WHEN NVL(DT1.\"AcDate\",0) != 0 ";
		sql += "        THEN DT1.\"AcDate\" - 19110000 ";
		sql += "        WHEN NVL(DT2.\"AcDate\",0) != 0 ";
		sql += "        THEN DT2.\"AcDate\" - 19110000 ";
		sql += "        WHEN A.\"OpenAcDate\" = 0 THEN ";
		sql += "            0 ";
		sql += "        ELSE ";
		sql += "            trunc(A.\"OpenAcDate\" - 19110000) ";
		sql += "    END AS \"OpenAcDate\", ";
		sql += "    CASE";
		sql += "        WHEN NVL(DT1.\"AcDate\",0) != 0 ";
		sql += "        THEN DT1.\"TavBal\" ";
		sql += "        WHEN NVL(DT2.\"AcDate\",0) != 0 ";
		sql += "        THEN DT2.\"TavBal\" ";
		sql += "    ELSE A.\"RvAmt\" END    AS \"RvAmt\", ";
		sql += "    CASE ";
		sql += "        WHEN A.\"LastTxDate\" = 0 THEN ";
		sql += "            0 ";
		sql += "        ELSE ";
		sql += "            trunc(A.\"LastTxDate\" - 19110000) ";
		sql += "    END AS \"LastTxDate\", ";
		sql += "    A.\"RvBal\"             AS \"RvBal\", ";
		sql += "    B.\"ToAml\"             AS \"SumRvBal\", ";
		sql += "    A.\"AcBookCode\"        AS \"AcBookCode\", ";
		sql += "    A.\"AcSubBookCode\"     AS \"AcSubBookCode\", ";
		sql += "    A.\"LastUpdate\"        AS \"LastUpdate\", ";
		sql += "    A.\"LastUpdateEmpNo\"   AS \"LastUpdateEmpNo\", ";
		sql += "    A.\"ReceivableFlag\"    AS \"ReceivableFlag\" ";
		sql += " FROM  \"AcReceivable\" A ";
		sql += " LEFT JOIN ( ";
		sql += "    SELECT  ";
		sql += "    \"AcctCode\"         , ";
		sql += "    SUM(\"RvBal\") AS \"ToAml\" ";
		sql += "    FROM \"AcReceivable\" ";
		sql += "     where \"AcBookCode\" = 000 "; // 固定傳入
		sql += "       and \"AcctFlag\"   = 0 ";
		sql += "       and \"FacmNo\"   <= 999 ";
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
		sql += "         GROUP BY \"AcctCode\" ";
		sql += "    ) B ON A.\"AcctCode\" = B.\"AcctCode\" ";
		sql += " LEFT JOIN DT1 ON DT1.\"CustNo\" = A.\"CustNo\" ";
		sql += "              AND DT1.\"FacmNo\" = A.\"FacmNo\" ";
		sql += "              AND A.\"AcctCode\" = 'TAV' ";
		sql += " LEFT JOIN DT2 ON DT2.\"CustNo\" = A.\"CustNo\" ";
		sql += "              AND A.\"AcctCode\" = 'TAV' ";
		sql += " where A.\"AcBookCode\" = 000 "; // 固定傳入
		if ("".equals(iAcctCode)) {
			sql += "   and A.\"AcctFlag\"   = 0 ";
		}
		sql += "   and A.\"FacmNo\"   <= 999 ";
		// 加入判斷空白跳過該篩選
		// 區隔帳冊
		if (!"".equals(iAcSubBookCode)) {
			sql += "and A.\"AcSubBookCode\" = :iAcSubBookCode ";
		}
		// 科子項目
		if (!"".equals(iAcNoCode)) {
			sql += "and A.\"AcNoCode\" = :iAcNoCode   ";
		}
		if (!"".equals(iAcSubCode)) {
			sql += "and A.\"AcSubCode\" = :iAcSubCode  ";
		}
		if (!"".equals(iAcDtlCode)) {
			sql += "and A.\"AcDtlCode\" = :iAcDtlCode  ";
		}
		// 戶號
		if (iCustNo != 0) {
			sql += "and A.\"CustNo\" = :iCustNo  ";
		}
		if (iFacmNo != 0) {
			sql += "and A.\"FacmNo\" = :iFacmNo  ";
		}
		// 業務科目
		if (!"".equals(iAcctCode)) {
			sql += " and A.\"AcctCode\" = :iAcctCode ";
		}
		// 銷帳記號
		if (iClsFlag != 2) {
			sql += " and  A.\"ClsFlag\" = :iClsFlag";
		}
		sql += "  order by  A.\"AcctCode\" ";

		sql += " " + sqlRow;

		this.info("L6907Service SQL=" + sql);

		Query query;
//    query = em.createNativeQuery(sql,L5051Vo.class);//進SQL 所以不要用.class (要用.class 就要使用HQL)
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

		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

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