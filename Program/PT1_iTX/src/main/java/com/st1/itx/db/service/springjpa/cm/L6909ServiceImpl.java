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

		this.info("L6909FindData");
		// 取得變數
		int iCustNo = parse.stringToInteger(titaVo.get("CustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.get("FacmNo"));
		int iEntryDateS = parse.stringToInteger(titaVo.get("EntryDateS").trim());
		int iEntryDateE = parse.stringToInteger(titaVo.get("EntryDateE").trim());
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
		sql += "  	lx.\"EntryDate\"  	AS \"EntryDate\", ";
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
		sql += "    AND JSON_VALUE  (ad.\"JsonFields\",  '$.EntryDate') >= :entryDateS ";
		sql += "    AND JSON_VALUE  (ad.\"JsonFields\",  '$.EntryDate') <= :entryDateE ";
		sql += "    AND ad.\"CustNo\" = :custno";
		if (iFacmNo > 0) {
			sql += "    AND ad.\"FacmNo\" = :facmno";
		}
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
			sql += " ORDER BY  \"EntryDate\", \"CreateDate\", \"FacmNo\" ,\"AcSeq\" ";
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
		this.info("entryDateS" + iEntryDateS);
		this.info("entryDateE" + iEntryDateE);
		query.setParameter("custno", iCustNo);
		query.setParameter("entryDateS", iEntryDateS + 19110000);
		query.setParameter("entryDateE", iEntryDateE + 19110000);
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

}