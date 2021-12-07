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

@Service("l4510RServiceImpl")
@Repository
/* 逾期放款明細 */
public class L4510RServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	private int mediaDate = 0;
	private String procCode = "";

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("L4510R.findAll");

		String sql = " ";
		sql += " WITH EDD AS ( ";
		sql += "     SELECT EDD.\"EntryDate\" ";
		sql += "          , EDD.\"EmpNo\" ";
		sql += "          , EDD.\"CustNo\" ";
		sql += "          , EDD.\"AchRepayCode\" ";
		sql += "          , EDD.\"PerfMonth\" ";
		sql += "          , EDD.\"ProcCode\" ";
		sql += "          , EDD.\"RepayCode\" ";
		sql += "          , EDD.\"AcctCode\" ";
		sql += "          , EDD.\"FacmNo\" ";
		sql += "          , EDD.\"CustId\" ";
		sql += "          , JSON_VALUE(EDD.\"JsonFields\", '$.InsuNo') AS JSON_DATA "; // -- 先取得JSON_VALUE
		sql += "     FROM \"EmpDeductDtl\" EDD ";
		sql += "     WHERE JSON_VALUE(EDD.\"JsonFields\", '$.InsuNo') IS NOT NULL "; // -- 判斷NOT NULL 避免後面資料重複
		sql += "       AND EDD.\"ErrMsg\" IS NULL ";
		sql += "       AND EDD.\"MediaDate\" = :mediaDate ";
		sql += "       AND EDD.\"ProcCode\" IN ( :procCode ) ";
		sql += "       AND EDD.\"AchRepayCode\" = 5 ";
		sql += " ) ";
		sql += " ,EDD2 AS ( ";
		sql += "     SELECT EDD.\"EntryDate\" ";
		sql += "          , EDD.\"EmpNo\" ";
		sql += "          , EDD.\"CustNo\" ";
		sql += "          , EDD.\"AchRepayCode\" ";
		sql += "          , EDD.\"PerfMonth\" ";
		sql += "          , EDD.\"ProcCode\" ";
		sql += "          , EDD.\"RepayCode\" ";
		sql += "          , EDD.\"AcctCode\" ";
		sql += "          , EDD.\"FacmNo\" ";
		sql += "          , EDD.\"CustId\" ";
		sql += "          , REGEXP_SUBSTR(JSON_DATA,'[^,]+',1,LEVEL,'i') AS \"Splited_JSON_DATA\" ";
		// -- 用正規表達式+LEVEL 做到類似JAVA SPLIT的功能
		sql += "     FROM EDD ";
		sql += "     CONNECT BY LEVEL <= REGEXP_COUNT(JSON_DATA,',') + 1 ";
		sql += " ) ";
		sql += " SELECT EDD2.\"PerfMonth\" ";
		sql += "      , EDD2.\"ProcCode\" ";
		sql += "      , EDD2.\"EntryDate\" ";
		sql += "      , EDD2.\"EmpNo\" ";
		sql += "      , EDD2.\"CustId\" ";
		sql += "      , EDD2.\"CustNo\" ";
		sql += "      , CM.\"CustName\" ";
		sql += "      , EDD2.\"FacmNo\" ";
		sql += "      , IR.\"ClNo\" ";
		sql += "      , IR.\"FireInsuPrem\" ";
		sql += "      , IR.\"EthqInsuPrem\" ";
		sql += "      , IR.\"TotInsuPrem\" ";
		sql += " FROM EDD2 ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = EDD2.\"CustNo\" ";
		sql += " LEFT JOIN \"InsuRenew\" IR ON IR.\"CustNo\" = EDD2.\"CustNo\" ";
		sql += "                         AND IR.\"FacmNo\" = EDD2.\"FacmNo\" ";
		sql += "                         AND IR.\"PrevInsuNo\" = EDD2.\"Splited_JSON_DATA\" "; // -- 拿來串檔
		sql += " ORDER BY EDD2.\"CustNo\" ";
		sql += "        , EDD2.\"FacmNo\" ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		query.setParameter("mediaDate", mediaDate);
		query.setParameter("procCode", procCode);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findAll(int iMediaDate, String iProcCode, TitaVo titaVo) throws Exception {
		mediaDate = iMediaDate;
		procCode = iProcCode;

		return findAll(titaVo);
	}
}