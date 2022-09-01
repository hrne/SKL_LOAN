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
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.parse.Parse;

@Service("L4211BServiceImpl")
@Repository
public class L4211BServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	int iENTDY;
	String inputReconCode;

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		inputReconCode = String.valueOf(titaVo.get("ReconCode")).trim();
		if (inputReconCode.equals("A7")) {
			inputReconCode = "P03";
		}
		iENTDY = Integer.valueOf(titaVo.get("EntryDate")) + 19110000;

		this.info("ReconCode     = " + inputReconCode);
		this.info("ENTDY ==>" + iENTDY);

		// 匯款轉帳失敗表 - 明細
		String sql = "SELECT ";
		sql += "       BATX.\"ReconCode\""; // 存摺代號(表頭) 0
		sql += "     , BATX.\"BatchNo\""; // 批次號碼(表頭) 1
		sql += "     , BATX.\"EntryDate\""; // 匯款日 2
		sql += "    , BATX.\"DetailSeq\""; // 匯款序號 3
		sql += "     , BATX.\"RepayAmt\""; // 匯款金額 4
		sql += "     , BR.\"RemintBank\""; // 匯款銀行 5
		sql += "     , BATX.\"CustNo\""; // 戶號 6
		sql += "     , CASE WHEN BATX.\"CustNo\" <> 0 THEN CM.\"CustName\"";
		sql += "            ELSE N''  END AS \"CustName\""; // 戶名 7
		sql += "     ,CASE ";
		sql += "		WHEN BATX.\"CustNo\" <> 0 AND LENGTH(TRIM(\"Fn_GetTelNo\"(CM.\"CustUKey\",'01',1))) >=7 then \"Fn_GetTelNo\"(CM.\"CustUKey\",'01',1)";
		sql += "		WHEN BATX.\"CustNo\" <> 0 AND LENGTH(TRIM(\"Fn_GetTelNo\"(CM.\"CustUKey\",'02',1))) >=7 then \"Fn_GetTelNo\"(CM.\"CustUKey\",'02',1)";
		sql += "		WHEN BATX.\"CustNo\" <> 0 AND LENGTH(TRIM(\"Fn_GetTelNo\"(CM.\"CustUKey\",'03',1))) >=7 then \"Fn_GetTelNo\"(CM.\"CustUKey\",'03',1)";
		sql += " 	    END AS \"CustTel\"";// 聯絡電話 8
		sql += "     , BATX.\"AcDate\""; // 會計日期 9
		sql += "     , BATX.\"TitaTxtNo\""; // 交易序號 10
		sql += "     , BATX.\"ProcCode\""; // 備註 11
		sql += "     , BATX.\"ProcStsCode\"";// 判斷代碼 12
		sql += " FROM \"BatxDetail\" BATX ";
		sql += " LEFT JOIN \"BankRmtf\" BR ON BR.\"AcDate\" = BATX.\"AcDate\" ";
		sql += "                       AND BR.\"BatchNo\" = BATX.\"BatchNo\" ";
		sql += "                       AND BR.\"DetailSeq\" = BATX.\"DetailSeq\" ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = BATX.\"CustNo\" ";
		sql += "                       AND BATX.\"CustNo\" <> 0 ";
		sql += " WHERE BATX.\"RepayCode\" = '01' ";
		sql += "  AND BATX.\"EntryDate\" = :iENTDY ";
		sql += " AND CASE";
		sql += "       WHEN NVL(TRIM( :inputReconCode ),' ') != ' ' ";// 輸入空白時查全部
		sql += "       THEN :inputReconCode";
		sql += "     ELSE BATX.\"ReconCode\" ";
		sql += "     END = BATX.\"ReconCode\"";
		sql += " ORDER BY BATX.\"ReconCode\"  "; // 存摺代號(表頭)
		sql += "       , BATX.\"BatchNo\"   "; // 批次號碼(表頭)
		sql += "       , BATX.\"DetailSeq\" "; // 匯款序號
		sql += "       , BATX.\"CustNo\"   "; // 戶號
		sql += "       , BATX.\"ProcStsCode\" DESC  ";
		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		query.setParameter("iENTDY", iENTDY);
		query.setParameter("inputReconCode", inputReconCode);
		return this.convertToMap(query);
	}

}