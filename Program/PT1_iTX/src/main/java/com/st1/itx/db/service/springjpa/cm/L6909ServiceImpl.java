package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
//import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

@Service("l6909ServiceImpl")
@Repository
public class L6909ServiceImpl extends ASpringJpaParm implements InitializingBean {

//	@Autowired
//	private CdCodeService sCdCodeDefService;

	@Autowired
	private BaseEntityManager baseEntityManager;
	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	// *** 折返控制相關 ***
	private int limit;

	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

	public List<Map<String, String>> FindAll(TitaVo titaVo, int index, int limit) throws Exception {
		this.info("FindAll CustNo=" + titaVo.get("CustNo") + ", AcDate=" + titaVo.get("AcDateS") + "~"
				+ titaVo.get("AcDateE"));

		int iCustNo = parse.stringToInteger(titaVo.get("CustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.get("FacmNo"));
		int iTitaHCode = parse.stringToInteger(titaVo.get("TitaHCode"));
		int iAcDateS = parse.stringToInteger(titaVo.get("AcDateS").trim());
		int iAcDateE = parse.stringToInteger(titaVo.get("AcDateE").trim());
		String iSortCode = titaVo.get("SortCode").trim(); // 0-會計日期+額度 1-入帳日期+額度 2-額度+入帳日期

		String sql = "  SELECT  ";

		sql += "    ad.\"FacmNo\"       AS \"FacmNo\",";
		sql += "    SUM(CASE WHEN ad.\"DbCr\" = 'D' then ad.\"TxAmt\" ELSE 0 END)  AS \"DbAmt\",";
		sql += "    SUM(CASE WHEN ad.\"DbCr\" = 'C' then ad.\"TxAmt\" ELSE 0 END)  AS \"CrAmt\",";
		sql += "    ad.\"TitaTlrNo\"    AS \"TitaTlrNo\",";
		sql += "    ad.\"TitaTxtNo\"    AS \"TitaTxtNo\",";
		sql += "    tc.\"TranItem\"     AS \"TranItem\",";
		sql += "    ad.\"TitaTxCd\"     AS \"TitaTxCd\",";
		sql += "  	MIN(ad.\"TitaHCode\")   AS \"TitaHCode\", ";
		sql += "    ad.\"AcDate\"           AS \"AcDate\",";
		sql += "    MIN(ad.\"CreateDate\")  AS \"CreateDate\",";
		sql += "    MIN(ad.\"AcSeq\")       AS \"AcSeq\", ";
		sql += "  	MAX(lx.\"EntryDate\")   AS \"EntryDate\", ";
		sql += "    MAX(lx.\"TitaCalDy\")   AS \"TitaCalDy\", ";
		sql += "    MAX(lx.\"TitaCalTm\")	AS \"TitaCalTm\"  ";
		sql += "  FROM";
		sql += "    \"AcDetail\"   ad";
		sql += "  LEFT JOIN  \"LoanBorTx\"     lx";
		sql += "    ON  lx.\"AcDate\" = ad.\"AcDate\" ";
		sql += "    AND lx.\"TitaTlrNo\"= ad.\"TitaTlrNo\" ";
		sql += "    AND lx.\"TitaTxtNo\" = ad.\"TitaTxtNo\" ";
		sql += "    AND lx.\"AcSeq\" = 1 ";
		sql += "  LEFT JOIN \"TxTranCode\"   tc ON tc.\"TranNo\" = ad.\"TitaTxCd\"";
		sql += "  WHERE";
		sql += "    ad.\"AcctCode\" = 'TAV' ";
		sql += "    AND ad.\"CustNo\" = :custno";
		if (iFacmNo > 0) {
			sql += "    AND ad.\"FacmNo\" = :facmno";
		}
		sql += "    AND ad.\"AcDate\" >= :acDateS ";
		sql += "    AND ad.\"AcDate\" <= :acDateE ";
		if (iTitaHCode == 0) {
			sql += "    AND ad.\"TitaHCode\" in ('0','3','4') ";
		}
		sql += "  GROUP BY ad.\"AcDate\"    ";
		sql += "          ,ad.\"TitaTlrNo\" ";
		sql += "          ,ad.\"TitaTxtNo\" ";
		sql += "          ,ad.\"TitaTxCd\"  ";
		sql += "          ,ad.\"FacmNo\" ";
		sql += "          ,tc.\"TranItem\"  ";
		if ("0".equals(iSortCode)) {
			sql += " ORDER BY \"AcDate\" ASC              ";
			sql += "         ,\"TitaCalDy\" ASC	          ";
			sql += "         ,\"TitaCalTm\"	ASC           ";
			sql += "         ,\"TitaTxtNo\"  ASC          ";
			sql += "         ,\"FacmNo\" ASC              ";
		} else if ("1".equals(iSortCode)) {
			sql += " ORDER BY \"EntryDate\" ASC           ";
			sql += "         ,\"FacmNo\" ASC              ";
			sql += "         ,\"AcDate\" ASC	          ";
			sql += "         ,\"TitaCalDy\" ASC	          ";
			sql += "         ,\"TitaCalTm\"	ASC           ";
			sql += "         ,\"TitaTxtNo\"  ASC          ";
		} else {
			sql += " ORDER BY \"FacmNo\" ASC              ";
			sql += "         ,\"EntryDate\" ASC	          ";
			sql += "         ,\"AcDate\" ASC              ";
			sql += "         ,\"TitaCalDy\" ASC	          ";
			sql += "         ,\"TitaCalTm\"	ASC           ";
			sql += "         ,\"TitaTxtNo\"  ASC          ";
		}

		sql += " " + sqlRow;

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可
		// 如果沒取得變數則不會傳入query
		query.setParameter("custno", iCustNo);
		query.setParameter("acDateS", iAcDateS + 19110000);
		query.setParameter("acDateE", iAcDateE + 19110000);
		if (iFacmNo > 0) {
			query.setParameter("facmno", iFacmNo);
		}

		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		this.info("L6909Service FindData=" + query);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		// query.setFirstResult(this.index*this.limit);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		return this.convertToMap(query);

	}

	// 讀取AcDate當日暫收餘額檔反算昨日餘額
	public List<Map<String, String>> queryDailyTav(TitaVo titaVo) throws LogicException {
		this.info("queryDailyTav CustNo=" + titaVo.get("CustNo") + ", EntryDate=" + titaVo.get("EntryDateS") + "~"
				+ titaVo.get("EntryDateE") + ", AcDate=" + titaVo.get("AcDateS") + "~" + titaVo.get("AcDateE"));

		int iCustNo = parse.stringToInteger(titaVo.get("CustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.get("FacmNo"));
		int iAcDateS = parse.stringToInteger(titaVo.get("AcDateS").trim());

		String sql = " ";
		sql += " WITH TavData AS ( ";
		sql += "     SELECT \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += "          , \"AcDate\" ";
		sql += "          , \"TavBal\" ";
		sql += "          , ROW_NUMBER ()  OVER ( PARTITION BY \"CustNo\", \"FacmNo\"  ORDER BY  \"AcDate\" DESC ) AS \"Seq\" ";
		sql += "     FROM \"DailyTav\"  ";
		sql += "     WHERE \"CustNo\" = :custno";
		sql += "       AND \"AcDate\" < :acDateS";
		if (iFacmNo > 0) {
			sql += "    AND \"FacmNo\" = :facmno";
		}
		sql += " ) ";
		sql += "  SELECT  ";
		sql += "    \"CustNo\" ";
		sql += "  , \"FacmNo\" ";
		sql += "  , \"AcDate\" ";
		sql += "  , \"TavBal\"    AS \"YdBal\" "; // 會計日相同需還原昨日餘額
		sql += "  FROM ( SELECT  ";
		sql += "           T.\"CustNo\" ";
		sql += "         , T.\"FacmNo\" ";
		sql += "         , T.\"AcDate\" ";
		sql += "         , T.\"TavBal\" ";
		sql += "         FROM TavData T ";
		sql += "         WHERE T.\"Seq\" =  1 ";
		sql += "         GROUP BY T.\"CustNo\", T.\"FacmNo\", T.\"AcDate\", T.\"TavBal\"  ";
		sql += "         ORDER BY T.\"CustNo\", T.\"FacmNo\" ";
		sql += "        ) ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("custno", iCustNo);
		query.setParameter("acDateS", iAcDateS + 19110000);
		if (iFacmNo > 0) {
			query.setParameter("facmno", iFacmNo);
		}

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		query.setFirstResult(0);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(Integer.MAX_VALUE);

		return this.convertToMap(query);
	}

	// 讀取暫收餘額檔餘額(非轉換)找出最小的AcDate; 會計日為零則以入帳日找會計日
	public List<Map<String, String>> queryAcDate(TitaVo titaVo) throws LogicException {

		this.info("queryAcDate CustNo=" + titaVo.get("CustNo") + ", EntryDate=" + titaVo.get("EntryDateS") + "~"
				+ titaVo.get("EntryDateE") + ", AcDate=" + titaVo.get("AcDateS") + "~" + titaVo.get("AcDateE"));
		int iCustNo = parse.stringToInteger(titaVo.get("CustNo"));
		int iEntryDateS = parse.stringToInteger(titaVo.get("EntryDateS").trim());
		int iEntryDateE = parse.stringToInteger(titaVo.get("EntryDateE").trim());
		int iAcDateS = parse.stringToInteger(titaVo.get("AcDateS").trim());
		int iAcDateE = parse.stringToInteger(titaVo.get("AcDateE").trim());

		String sql = " ";
		sql += " WITH TavData AS ( ";
		sql += "     SELECT \"CustNo\" ";
		sql += "          , Min(\"AcDate\") AS \"AcDateS\" ";
		sql += "     FROM \"DailyTav\"  ";
		sql += "     WHERE \"CustNo\" = :custno";
		sql += "      AND  \"CreateEmpNo\" <> '999999' ";
		sql += "     GROUP BY \"CustNo\" ";
		sql += " ) ";
		if (iAcDateS > 0) {
			sql += "  SELECT T.\"CustNo\" ";
			sql += "       , CASE WHEN T.\"AcDateS\" < :acDateS THEN :acDateS ELSE  T.\"AcDateS\"  END AS \"AcDateS\" ";
			sql += "       , :acDateE AS \"AcDateE\" ";
			sql += "  FROM TavData T ";
		} else {
			sql += "  SELECT T.\"CustNo\" ";
			sql += "       , MIN(L.\"AcDate\") AS \"AcDateS\" ";
			sql += "       , MAX(L.\"AcDate\") AS \"AcDateE\" ";
			sql += "  FROM TavData T ";
			sql += "  LEFT JOIN \"LoanBorTx\" L ";
			sql += "        ON L.\"CustNo\" = :custno";
			sql += "       AND L.\"AcDate\" >=  T.\"AcDateS\" ";
			sql += "       AND L.\"EntryDate\" >= :entryDateS ";
			sql += "       AND L.\"EntryDate\" <= :entryDateE ";
			sql += "  GROUP BY T.\"CustNo\" ";
		}
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("custno", iCustNo);
		if (iAcDateS > 0) {
			query.setParameter("acDateS", iAcDateS + 19110000);
			query.setParameter("acDateE", iAcDateE + 19110000);
		} else {
			query.setParameter("entryDateS", iEntryDateS + 19110000);
			query.setParameter("entryDateE", iEntryDateE + 19110000);
		}

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		query.setFirstResult(0);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(Integer.MAX_VALUE);

		return this.convertToMap(query);
	}

	// 讀取暫收餘額檔轉換日
	public List<Map<String, String>> queryConvertDate(TitaVo titaVo) throws LogicException {

		String sql = " ";
		sql += "     SELECT MAX(\"AcDate\") AS \"AcDateS\" ";
		sql += "     FROM \"DailyTav\"  ";
		sql += "     WHERE \"CreateEmpNo\" = '999999' ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		query.setFirstResult(0);

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(Integer.MAX_VALUE);

		return this.convertToMap(query);
	}

}