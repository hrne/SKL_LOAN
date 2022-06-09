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
		sql +=  "    t.\"RvNo\"          AS \"RvNo\","; 
		sql +=  "    t.\"TxAmt\"         AS \"RvAmt\","; 
		sql +=  "    ac.\"TitaTlrNo\"    AS \"TitaTlrNo\","; 
		sql +=  "    ac.\"TitaTxtNo\"    AS \"TitaTxtNo\","; 
		sql +=  "    tc.\"TranItem\"     AS \"TranItem\","; 
		sql +=  "    ac.\"TitaTxCd\"     AS \"TitaTxCd\","; 
		sql +=  "    NULL AS \"SlipNote\","; 
		sql +=  "    ac.\"OpenAcDate\"   AS \"AcDate\","; 
		sql +=  "    ac.\"CreateDate\"   AS \"CreateDate\","; 
		sql +=  "    lb.\"EntryDate\"    AS \"EntryDate\","; 
		sql +=  "    CASE"; 
		sql +=  "        WHEN t.\"TxAmt\" > 0 THEN"; 
		sql +=  "            0"; 
		sql +=  "        ELSE"; 
		sql +=  "            1"; 
		sql +=  "    END AS \"ClsFlag\",";
		sql +=  "    'AcReceivable'    AS \"DB\""; 
		sql +=  "  FROM"; 
		sql +=  "    \"AcReceivable\"   ac"; 
		sql +=  "    LEFT JOIN \"LoanBorTx\"      lb ON lb.\"AcDate\" = ac.\"OpenAcDate\""; 
		sql +=  "                                AND lb.\"TitaTlrNo\" = ac.\"TitaTlrNo\""; 
		sql +=  "                                AND lb.\"TitaTxtNo\" = ac.\"TitaTxtNo\""; 
		sql +=  "    LEFT JOIN \"TxTranCode\"     tc ON tc.\"TranNo\" = ac.\"OpenTxCd\""; 
		sql +=  "    LEFT JOIN ("; 
		sql +=  "        SELECT"; 
		sql +=  "            a.\"RvNo\" ,"; 
		sql +=  "            SUM("; 
		sql +=  "                CASE"; 
		sql +=  "                    WHEN a.\"ReceivableFlag\" >= 3 THEN"; 
		sql +=  "                        a.\"TxAmt\""; 
		sql +=  "                    WHEN a.\"DbCr\" = cd.\"DbCr\"    THEN"; 
		sql +=  "                        a.\"TxAmt\""; 
		sql +=  "                    ELSE"; 
		sql +=  "                        - a.\"TxAmt\""; 
		sql +=  "                END"; 
		sql +=  "            ) AS \"TxAmt\""; 
		sql +=  "        FROM"; 
		sql +=  "            \"AcDetail\"   a"; 
		sql +=  "            LEFT JOIN \"CdAcCode\"   cd ON cd.\"AcctCode\" = a.\"AcctCode\""; 
		sql +=  "        WHERE"; 
		sql +=  "            a.\"AcBookCode\" = :acbookcode"; 
		sql +=  "            AND a.\"AcSubBookCode\" = :acsubbookcode"; 
		sql +=  "            AND a.\"BranchNo\" = :branchno"; 
		sql +=  "            AND a.\"CurrencyCode\" = :currencycode"; 
		sql +=  "            AND a.\"AcNoCode\" = :acnocode"; 
		sql +=  "            AND a.\"AcSubCode\" = :acsubcode"; 
		sql +=  "            AND a.\"AcDtlCode\" = :acdtlcode"; 
		sql +=  "            AND a.\"CustNo\" = :custno"; 
		sql +=  "            AND a.\"FacmNo\" = :facmno"; 
		sql +=  "            AND a.\"AcDate\" >= :acdatest"; 
		sql +=  "            AND a.\"AcDate\" <= :acdateed"; 
		sql +=  "            AND a.\"EntAc\" = 1"; 
		sql +=  "            AND a.\"ReceivableFlag\" != 0"; 
		sql +=  "            AND a.\"RvNo\" IS NOT NULL"; 
//		sql +=  "            AND lpad(a.\"FacmNo\", 3, '0') = :rvno"; 
		sql +=  "        GROUP BY"; 
		sql +=  "            a.\"RvNo\""; 
		sql +=  "    ) t ON t.\"RvNo\" = ac.\"RvNo\""; 
		sql +=  "  WHERE"; 
		sql +=  "    ac.\"AcctCode\" = :acctcode"; 
		sql +=  "    AND ac.\"CustNo\" = :custno"; 
		sql +=  "    AND ac.\"FacmNo\" = :facmno"; 
		sql +=  "    AND ac.\"RvNo\" = :rvno"; 
		sql +=  "  UNION"; 
		sql +=  "  SELECT"; 
		sql +=  "    ad.\"RvNo\"         AS \"RvNo\","; 
		sql +=  "    ad.\"TxAmt\"        AS \"RvAmt\","; 
		sql +=  "    ad.\"TitaTlrNo\"    AS \"TitaTlrNo\","; 
		sql +=  "    ad.\"TitaTxtNo\"    AS \"TitaTxtNo\","; 
		sql +=  "    tc.\"TranItem\"     AS \"TranItem\","; 
		sql +=  "    ad.\"TitaTxCd\"     AS \"TitaTxCd\","; 
		sql +=  "    ad.\"SlipNote\"     AS \"SlipNote\","; 
		sql +=  "    ad.\"AcDate\"       AS \"AcDate\","; 
		sql +=  "    ad.\"CreateDate\"   AS \"CreateDate\","; 
		sql +=  "    lb.\"EntryDate\"    AS \"EntryDate\","; 
		sql +=  "    CASE"; 
		sql +=  "        WHEN ad.\"ReceivableFlag\" >= 3 THEN"; 
		sql +=  "            1"; 
		sql +=  "        WHEN ad.\"DbCr\" = cd.\"DbCr\"    THEN"; 
		sql +=  "            0"; 
		sql +=  "        ELSE"; 
		sql +=  "            1"; 
		sql +=  "    END AS \"ClsFlag\","; 
		sql +=  "    'AcDetail'    AS \"DB\""; 
		sql +=  "  FROM"; 
		sql +=  "    \"AcDetail\"     ad"; 
		sql +=  "    LEFT JOIN \"LoanBorTx\"    lb ON lb.\"AcDate\" = ad.\"AcDate\""; 
		sql +=  "                                AND lb.\"TitaTlrNo\" = ad.\"TitaTlrNo\""; 
		sql +=  "                                AND lb.\"TitaTxtNo\" = ad.\"TitaTxtNo\""; 
		sql +=  "    LEFT JOIN \"TxTranCode\"   tc ON tc.\"TranNo\" = ad.\"TitaTxCd\""; 
		sql +=  "    LEFT JOIN \"CdAcCode\"     cd ON cd.\"AcctCode\" = ad.\"AcctCode\""; 
		sql +=  "  WHERE"; 
		sql +=  "    ad.\"AcBookCode\" = :acbookcode"; 
		sql +=  "    AND ad.\"AcSubBookCode\" = :acsubbookcode"; 
		sql +=  "    AND ad.\"BranchNo\" = :branchno"; 
		sql +=  "    AND ad.\"CurrencyCode\" = :currencycode"; 
		sql +=  "    AND ad.\"AcNoCode\" = :acnocode"; 
		sql +=  "    AND ad.\"AcSubCode\" = :acsubcode"; 
		sql +=  "    AND ad.\"AcDtlCode\" = :acdtlcode"; 
		sql +=  "    AND ad.\"CustNo\" = :custno"; 
		sql +=  "    AND ad.\"FacmNo\" = :facmno"; 
		sql +=  "    AND ad.\"AcDate\" >= :acdatest"; 
		sql +=  "    AND ad.\"AcDate\" <= :acdateed"; 
		sql +=  "    AND ad.\"RvNo\" IS NOT NULL"; 
		sql +=  "  ORDER BY \"CreateDate\"";
		
		sql += " " + sqlRow;
		
		this.info("sql = " + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		// 如果沒取得變數則不會傳入query
			query.setParameter("acctcode", iAcctCode);
			query.setParameter("custno", iCustNo);
			query.setParameter("facmno", iFacmNo);
			query.setParameter("rvno", iRvNo);			
			
			query.setParameter("acbookcode", iAcbookCode);
			query.setParameter("acsubbookcode", iAcSubBookCode);
			query.setParameter("branchno", iBranchNo);
			query.setParameter("currencycode", iCurrencyCode);
			query.setParameter("acnocode", iAcNoCode);
			query.setParameter("acsubcode", iAcSubCode);
			query.setParameter("acdtlcode", iAcDtlCode);
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