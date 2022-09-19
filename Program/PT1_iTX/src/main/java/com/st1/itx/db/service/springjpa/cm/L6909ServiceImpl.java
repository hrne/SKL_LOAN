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
		this.info("FindAll CustNo=" + titaVo.get("CustNo") + ", EntryDate=" + titaVo.get("EntryDateS") + "~"
				+ titaVo.get("EntryDateE") + ", AcDate=" + titaVo.get("AcDateS") + "~" + titaVo.get("AcDateE"));

		int iCustNo = parse.stringToInteger(titaVo.get("CustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.get("FacmNo"));
		int iEntryDateS = parse.stringToInteger(titaVo.get("EntryDateS").trim());
		int iEntryDateE = parse.stringToInteger(titaVo.get("EntryDateE").trim());
		int iAcDateS = parse.stringToInteger(titaVo.get("AcDateS").trim());
		int iAcDateE = parse.stringToInteger(titaVo.get("AcDateE").trim());
		String iSortCode = titaVo.get("SortCode").trim();

		String sql = "  SELECT  ";

		sql += "    ad.\"FacmNo\"       AS \"FacmNo\",";
		sql += "    SUM(CASE WHEN ad.\"DbCr\" = 'D' then ad.\"TxAmt\" ELSE 0 END)  AS \"DbAmt\",";
		sql += "    SUM(CASE WHEN ad.\"DbCr\" = 'C' then ad.\"TxAmt\" ELSE 0 END)  AS \"CrAmt\",";
		sql += "    ad.\"TitaTlrNo\"    AS \"TitaTlrNo\",";
		sql += "    ad.\"TitaTxtNo\"    AS \"TitaTxtNo\",";
		sql += "    tc.\"TranItem\"     AS \"TranItem\",";
		sql += "    ad.\"TitaTxCd\"     AS \"TitaTxCd\",";
		sql += "  	lx.\"Desc\"  		AS \"Desc\", ";
		sql += "  	NVL(lx.\"EntryDate\",0) AS \"EntryDate\", ";
		sql += "  	MIN( case when ad.\"EntAc\" = 2 	 THEN (CASE WHEN ad.\"RelDy\" = ad.\"AcDate\" THEN 2 ELSE 4 END)	 ";
		sql += "  			  when ad.\"EntAc\" = 3 	 THEN (CASE WHEN ad.\"RelDy\" = ad.\"AcDate\" THEN 1 ELSE 3 END ) ELSE 0	END) AS \"TitaHCode\", ";
		sql += "    ad.\"AcDate\"       AS \"AcDate\",";
		sql += "    MIN(ad.\"CreateDate\")   AS \"CreateDate\",";
		sql += "    ad.\"AcSeq\"        AS \"AcSeq\" ";
		sql += "  FROM";
		sql += "    \"AcDetail\"   ad";
		sql += "  LEFT JOIN  \"LoanBorTx\"     lx";
		sql += "    ON  lx.\"CustNo\" = ad.\"CustNo\" ";
		sql += "    AND lx.\"FacmNo\" = NVL(JSON_VALUE(ad.\"JsonFields\",  '$.FacmNo'),ad.\"FacmNo\") ";
		sql += "    AND lx.\"BormNo\" = NVL(JSON_VALUE(ad.\"JsonFields\",  '$.BormNo'),ad.\"BormNo\") ";
		sql += "    AND lx.\"BorxNo\" = JSON_VALUE  (ad.\"JsonFields\",  '$.BorxNo') ";
		sql += "  LEFT JOIN \"TxTranCode\"   tc ON tc.\"TranNo\" = ad.\"TitaTxCd\"";
		sql += "  WHERE";
		sql += "    ad.\"AcctCode\" = 'TAV' ";
		sql += "    AND ad.\"CustNo\" = :custno";
		if (iFacmNo > 0) {
			sql += "    AND ad.\"FacmNo\" = :facmno";
		}
		sql += "    AND ad.\"AcDate\" >= :acDateS ";
		sql += "    AND ad.\"AcDate\" <= :acDateE ";
		sql += "  GROUP BY ad.\"FacmNo\" ";
		sql += "          ,ad.\"AcDate\"    ";
		sql += "          ,ad.\"TitaTlrNo\" ";
		sql += "          ,ad.\"TitaTxtNo\" ";
		sql += "          ,ad.\"TitaTxCd\"  ";
		sql += "          ,ad.\"AcSeq\"  ";
		sql += "          ,tc.\"TranItem\"  ";
		sql += "          ,lx.\"Desc\"      ";
		sql += "  	      ,lx.\"EntryDate\" ";
		if ("1".equals(iSortCode)) {
			sql += " ORDER BY  \"EntryDate\", \"CreateDate\",\"AcSeq\" ";
		} else {
			sql += " ORDER BY  \"FacmNo\", \"EntryDate\", \"CreateDate\", \"AcSeq\" ";
		}

		sql += " " + sqlRow;

		this.info("sql = " + sql);

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

	// 讀取暫收餘額檔餘額
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
		sql += "       AND \"AcDate\" <= :acDateS";
		if (iFacmNo > 0) {
			sql += "    AND \"FacmNo\" = :facmno";
		}
		sql += " ) ";
		sql += "  SELECT  ";
		sql += "    \"CustNo\" ";
		sql += "  , \"FacmNo\" ";
		sql += "  , \"AcDate\" ";
		sql += "  , \"TavBal\" + NVL(\"TxAmt\",0)  AS \"YdBal\" "; // 會計日相同需還原昨日餘額
		sql += "  FROM ( SELECT  ";
		sql += "           T.\"CustNo\" ";
		sql += "         , T.\"FacmNo\" ";
		sql += "         , T.\"AcDate\" ";
		sql += "         , T.\"TavBal\" ";
		sql += "         , SUM(CASE WHEN NVL(A.\"DbCr\",' ') = 'D' then A.\"TxAmt\"  ";
		sql += "                    WHEN NVL(A.\"DbCr\",' ') = 'C' then 0 - A.\"TxAmt\" ";
		sql += "                    ELSE 0  END)  AS \"TxAmt\" ";
		sql += "         FROM TavData T ";
		sql += "         LEFT JOIN \"AcDetail\" A ON A.\"CustNo\" = T.\"CustNo\" ";
		sql += "                                 AND A.\"FacmNo\" = T.\"FacmNo\" ";
		sql += "                                 AND A.\"AcDate\" = T.\"AcDate\" ";
		sql += "                                 AND A.\"AcctCode\" = 'TAV' ";
		sql += "                                 AND T.\"AcDate\" = :acDateS ";
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
			sql += "  GROUP BY L.\"CustNo\" ";
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

}