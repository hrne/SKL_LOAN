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

		String sql = "  SELECT  ";
		sql += "    ac.\"RvAmt\"         AS \"TxAmt\",";
		sql += "    ac.\"FacmNo\"         AS \"FacmNo\",";
		sql += "    ac.\"OpenTlrNo\"    AS \"TitaTlrNo\",";
		sql += "    ac.\"OpenTxtNo\"    AS \"TitaTxtNo\",";
		sql += "    tc.\"TranItem\"     AS \"TranItem\",";
		sql += "    ac.\"OpenTxCd\"     AS \"TitaTxCd\",";
		sql += "    ac.\"SlipNote\"     AS \"SlipNote\",";
		sql += "    ac.\"OpenAcDate\"   AS \"AcDate\",";
		sql += "    ac.\"CreateDate\"   AS \"CreateDate\",";
		sql += "    'C'                   AS \"DbCr\",";
		sql += "    0                    AS \"DB\"";
		sql += "  FROM";
		sql += "    \"AcReceivable\"   ac";
		sql += "    LEFT JOIN \"TxTranCode\"     tc ON tc.\"TranNo\" = ac.\"OpenTxCd\"";
		sql += "  WHERE";
		sql += "    ac.\"AcctCode\" = 'TAV' ";
		sql += "    AND ac.\"CustNo\" = :custno";
		if (iFacmNo > 0) {
			sql += "    AND ac.\"FacmNo\" = :facmno";
		}

		sql += "  UNION";
		sql += "  SELECT";
		sql += "    ad.\"TxAmt\"        AS \"TxAmt\",";
		sql += "    ad.\"FacmNo\"        AS \"FacmNo\",";
		sql += "    ad.\"TitaTlrNo\"    AS \"TitaTlrNo\",";
		sql += "    ad.\"TitaTxtNo\"    AS \"TitaTxtNo\",";
		sql += "    tc.\"TranItem\"     AS \"TranItem\",";
		sql += "    ad.\"TitaTxCd\"     AS \"TitaTxCd\",";
		sql += "    ad.\"SlipNote\"     AS \"SlipNote\",";
		sql += "    ad.\"AcDate\"       AS \"AcDate\",";
		sql += "    ad.\"CreateDate\"   AS \"CreateDate\",";
		sql += "    Ad.\"DbCr\"         AS \"DbCr\",";
		sql += "    1                   AS \"DB\"";
		sql += "  FROM";
		sql += "    \"AcReceivable\"   ar";
		sql += "  LEFT JOIN  \"AcDetail\"     ad";
		sql += "     ON ad.\"AcctCode\" = ar.\"AcctCode\" ";
		sql += "    AND ad.\"CustNo\" = :custno";
		sql += "    AND ad.\"FacmNo\" = ar.\"FacmNo\" ";
		sql += "    AND ad.\"AcDate\" >= ar.\"OpenAcDate\" ";
		sql += "    AND ad.\"AcDate\" <= ar.\"LastAcDate\" ";
		sql += "    AND CASE WHEN  ad.\"AcDate\" = ar.\"OpenAcDate\"";
		sql += "              AND  ad.\"TitaTlrNo\"= ar.\"OpenTlrNo\" ";
		sql += "              AND  ad.\"TitaTxtNo\"= ar.\"OpenTxtNo\" ";
		sql += "             THEN 1 ELSE 0 END = 0 ";
		sql += "  LEFT JOIN \"TxTranCode\"   tc ON tc.\"TranNo\" = ad.\"TitaTxCd\"";
		sql += "  WHERE";
		sql += "    ar.\"AcctCode\" = 'TAV' ";
		sql += "    AND ar.\"CustNo\" = :custno";
		if (iFacmNo > 0) {
			sql += "    AND ar.\"FacmNo\" = :facmno";
		}
		sql += " ORDER BY \"DB\", \"FacmNo\", \"AcDate\", \"CreateDate\"";

		sql += " " + sqlRow;

		this.info("sql = " + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可
		// 如果沒取得變數則不會傳入query
		query.setParameter("custno", iCustNo);
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