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
import com.st1.itx.util.parse.Parse;

@Service("l6908ServiceImpl")
@Repository
public class L6908ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

		this.info("L6908FindData");
		// 取得變數
		String iAcctCode = titaVo.get("AcctCode").trim();
		String iCustNo = titaVo.get("CustNo").trim();
		String iFacmNo = titaVo.get("FacmNo").trim();
		String iRvNo = titaVo.get("RvNo").trim();

		if (iRvNo.isEmpty()) {
			iRvNo = " ";
		}

		String iAcbookCode = titaVo.get("AcBookCode").trim();
		String iAcSubBookCode = titaVo.get("AcSubBookCode").trim();
		String iBranchNo = titaVo.get("BranchNo").trim();
		String iCurrencyCode = titaVo.get("CurrencyCode").trim();
		String iAcNoCode = titaVo.get("AcNoCode").trim();
		String iAcSubCode = titaVo.get("AcSubCode").trim();
		String iAcDtlCode = titaVo.get("AcDtlCode").trim();
		int iAcdateSt = parse.stringToInteger(titaVo.get("AcDateSt").trim()) + 19110000;
		int iAcdateEd = parse.stringToInteger(titaVo.get("AcDateEd").trim()) + 19110000;

		if (iAcSubCode.isEmpty()) {
			iAcSubCode = "     ";
		}
		if (iAcDtlCode.isEmpty()) {
			iAcDtlCode = "  ";
		}

		this.info("iAcctCode = " + iAcctCode);
		this.info("iCustNo = " + iCustNo);
		this.info("iFacmNo = " + iFacmNo);
		this.info("iRvNo = " + iRvNo);

		this.info("iAcbookCode = " + iAcbookCode);
		this.info("iAcSubBookCode = " + iAcSubBookCode);
		this.info("iBranchNo = " + iBranchNo);
		this.info("iCurrencyCode = " + iCurrencyCode);
		this.info("iAcNoCode = " + iAcNoCode);
		this.info("iAcSubCode = " + iAcSubCode);
		this.info("iAcDtlCode = " + iAcDtlCode);
		this.info("iAcdateSt = " + iAcdateSt);
		this.info("iAcdateEd = " + iAcdateEd);

		String sql = "  SELECT  ";
		sql += "    ac.\"RvNo\"          AS \"RvNo\",";
		sql += "    ac.\"RvAmt\"         AS \"RvAmt\",";
		sql += "    ac.\"OpenTlrNo\"    AS \"TitaTlrNo\",";
		sql += "    ac.\"OpenTxtNo\"    AS \"TitaTxtNo\",";
		sql += "    tc.\"TranItem\"     AS \"TranItem\",";
		sql += "    ac.\"OpenTxCd\"     AS \"TitaTxCd\",";
		sql += "    JSON_VALUE  (tx.\"OtherFields\",  '$.Note')     AS \"SlipNote\",";
		sql += "    ac.\"OpenAcDate\"   AS \"AcDate\",";
		sql += "    ac.\"CreateDate\"   AS \"CreateDate\",";
		sql += "    0                   AS \"EntryDate\",";
		sql += "     0                   AS \"ClsFlag\",";
		sql += "    ac.\"ReceivableFlag\"   AS \"ReceivableFlag\",";
		sql += "    0                    AS \"DB\"";
		sql += "  FROM";
		sql += "    \"AcReceivable\"   ac";
		sql += "    LEFT JOIN \"TxTranCode\"     tc ON tc.\"TranNo\" = ac.\"OpenTxCd\" ";
		sql += "    LEFT JOIN \"LoanBorTx\"     tx ON tx.\"AcDate\" = ac.\"OpenAcDate\" ";
		sql += "    						AND   	  tx.\"TitaTlrNo\" = ac.\"OpenTlrNo\" ";
		sql += "    						AND   	  to_number(tx.\"TitaTxtNo\") = ac.\"OpenTxtNo\" ";
		sql += "    						AND 	  tx.\"AcSeq\" <= 1 ";
		sql += "  WHERE";
		sql += "    ac.\"AcctCode\" = :acctcode";
		sql += "    AND ac.\"CustNo\" = :custno";
		sql += "    AND ac.\"FacmNo\" = :facmno";
		sql += "    AND ac.\"RvNo\" = :rvno";
		sql += "    AND ac.\"ReceivableFlag\" > 0 ";
		sql += "  UNION ALL";
		sql += "  SELECT";
		sql += "    ad.\"RvNo\"         AS \"RvNo\",";
		sql += "    ad.\"TxAmt\"        AS \"RvAmt\",";
		sql += "    ad.\"TitaTlrNo\"    AS \"TitaTlrNo\",";
		sql += "    ad.\"TitaTxtNo\"    AS \"TitaTxtNo\",";
		sql += "    tc.\"TranItem\"     AS \"TranItem\",";
		sql += "    ad.\"TitaTxCd\"     AS \"TitaTxCd\",";
		sql += "    JSON_VALUE  (tx.\"OtherFields\",  '$.Note')     AS \"SlipNote\",";
		sql += "    ad.\"AcDate\"       AS \"AcDate\",";
		sql += "    ad.\"CreateDate\"   AS \"CreateDate\",";
		sql += "    0                   AS \"EntryDate\",";
		sql += "     CASE";
		sql += "        WHEN ad.\"ReceivableFlag\" >= 3 AND ad.\"DbCr\" = 'C' THEN 1 ";
		sql += "        WHEN ad.\"ReceivableFlag\" >= 3 AND ad.\"DbCr\" = 'D' THEN 0 ";
		sql += "        WHEN ad.\"AcctCode\" = 'TRO' AND ad.\"DbCr\" = 'D' THEN 0 ";
		sql += "        WHEN ad.\"AcctCode\" = 'TRO' AND ad.\"DbCr\" = 'C' THEN 1 ";
		sql += "        WHEN SUBSTR(ad.\"AcNoCode\",1,1) IN ('1','5','6','9') AND ad.\"DbCr\" = 'D' THEN 0 ";
		sql += "        WHEN SUBSTR(ad.\"AcNoCode\",1,1) IN ('1','5','6','9') AND ad.\"DbCr\" = 'C' THEN 1 ";
		sql += "        WHEN SUBSTR(ad.\"AcNoCode\",1,1) NOT IN ('1','5','6','9') AND ad.\"DbCr\" = 'D' THEN 1 ";
		sql += "        WHEN SUBSTR(ad.\"AcNoCode\",1,1) NOT IN ('1','5','6','9') AND ad.\"DbCr\" = 'C' THEN 0 ";
		sql += "        ELSE  1 ";
		sql += "      END AS \"ClsFlag\",";
		sql += "    ar.\"ReceivableFlag\"   AS \"ReceivableFlag\",";
		sql += "    1                   AS \"DB\"";
		sql += "  FROM";
		sql += "    \"AcReceivable\"   ar";
		sql += "  LEFT JOIN  \"AcDetail\"     ad";
		sql += "     ON ad.\"AcctCode\" = :acctcodeac";
		sql += "    AND ad.\"CustNo\" = :custno";
		sql += "    AND ad.\"FacmNo\" = :facmno";
		sql += "    AND ad.\"AcDate\" >= :acdatest";
		sql += "    AND ad.\"AcDate\" <= :acdateed";
		sql += "    AND ad.\"RvNo\"     = :rvno";
		sql += "    AND ad.\"ReceivableFlag\"  > 0 ";
		sql += "    AND CASE WHEN  ad.\"AcDate\" = ar.\"OpenAcDate\"";
		sql += "              AND  ad.\"TitaTlrNo\"= ar.\"OpenTlrNo\" ";
		sql += "              AND  ad.\"TitaTxtNo\"= ar.\"OpenTxtNo\" ";
		sql += "             THEN 1 ELSE 0 END = 0 ";
		sql += "  LEFT JOIN \"TxTranCode\"    tc ON   tc.\"TranNo\" = ad.\"TitaTxCd\"";
		sql += "  LEFT JOIN \"LoanBorTx\"     tx ON   tx.\"AcDate\" = ad.\"AcDate\" ";
		sql += "    					  		AND   tx.\"TitaTlrNo\" = ad.\"TitaTlrNo\" ";
		sql += "    							AND   to_number(tx.\"TitaTxtNo\") = ad.\"TitaTxtNo\" ";
		sql += "    							AND   tx.\"AcSeq\" <= 1 ";
		sql += "  WHERE";
		sql += "    ar.\"AcctCode\" = :acctcode";
		sql += "    AND ar.\"CustNo\" = :custno";
		sql += "    AND ar.\"FacmNo\" = :facmno";
		sql += "    AND ar.\"RvNo\" = :rvno";
		sql += "    AND NVL(ad.\"ReceivableFlag\", 0 ) > 0";
		sql += " ORDER BY \"DB\", \"AcDate\", \"CreateDate\"";

		sql += " " + sqlRow;

		this.info("sql = " + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		// 如果沒取得變數則不會傳入query
		String acctCodeAc = iAcctCode;
		if (acctCodeAc.substring(0, 1).equals("Z")) {
			acctCodeAc = "3" + acctCodeAc.substring(1, 3);
		}
		query.setParameter("acctcodeac", acctCodeAc);
		query.setParameter("acctcode", iAcctCode);
		query.setParameter("custno", iCustNo);
		query.setParameter("facmno", iFacmNo);
		query.setParameter("rvno", iRvNo);

		query.setParameter("acdatest", iAcdateSt);
		query.setParameter("acdateed", iAcdateEd);

		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		this.info("L6908Service FindData=" + query);

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