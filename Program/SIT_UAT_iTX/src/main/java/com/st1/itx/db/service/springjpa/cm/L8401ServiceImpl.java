package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.repository.online.LoanBorMainRepository;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

@Service("l8401ServiceImpl")
@Repository

/*
 * @param fg fg=1: B204 聯徵授信餘額日報檔 // 以下新系統［需求規格書］未列入 fg=2: B071 現金卡資料日報檔 fg=3:
 * B211 聯徵每日授信餘額變動資料檔
 */

public class L8401ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L8401ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List findAll(TitaVo titaVo, int fg) throws Exception {
		logger.info("----------- L8401.findAll ---------------");
		logger.info("L8401 TitaVo=" + titaVo);

		int dataStDate = Integer.parseInt(titaVo.get("ACCT_ST_DT")) + 19110000; // 會計起日(西元年月日)
		int dataEdDate = Integer.parseInt(titaVo.get("ACCT_ED_DT")) + 19110000; // 會計迄日(西元年月日)
		logger.info("dataStDate= " + dataStDate);
		logger.info("dataEdDate= " + dataEdDate);

		String sql = "";

		if (fg == 1) { // B204 聯徵授信餘額日報檔
			sql = "SELECT M.\"BankItem\"" + "     , M.\"BranchItem\"" + "     , M.\"DataDate\"" + "     , M.\"AcctNo\"" + "     , M.\"CustId\"" + "     , M.\"AcctCode\"" + "     , M.\"SubAcctCode\""
					+ "     , M.\"SubTranCode\"" + "     , M.\"LineAmt\"" + "     , M.\"DrawdownAmt\"" + "     , M.\"DBR22Amt\"" + "     , M.\"SeqNo\"" + "     , M.\"Filler13\""
					+ " FROM  \"JcicB204\" M" + " WHERE ( M.\"DataYMD\" >= " + dataStDate + " AND M.\"DataYMD\" <= " + dataEdDate + " )"
					+ " ORDER BY M.\"BankItem\", M.\"BranchItem\", M.\"DataDate\", M.\"AcctNo\", M.\"CustId\", " + "          M.\"AcctCode\", M.\"SubAcctCode\", M.\"SeqNo\" ";
		}

		/*
		 * 以下新系統［需求規格書］未列入 if (fg == 3) { // 未列在新系統需求書 B211 聯徵每日授信餘額變動資料檔 sql =
		 * "SELECT M.\"BankItem\"" + "     , M.\"BranchItem\"" + "     , M.\"TranCode\""
		 * + "     , M.\"CustId\"" + "     , M.\"SubTranCode\"" + "     , M.\"AcDate\""
		 * + "     , M.\"AcctNo\"" + "     , M.\"TxAmt\"" + "     , M.\"LoanBal\"" +
		 * "     , M.\"RepayCode\"" + "     , M.\"NegStatus\"" + "     , M.\"AcctCode\""
		 * + "     , M.\"SubAcctCode\"" + "     , M.\"BadDebtDate\"" +
		 * "     , M.\"ConsumeFg\"" + "     , M.\"FinCode\"" + "     , M.\"UsageCode\""
		 * + "     , M.\"Filler18\"" + " FROM  \"JcicB211\" M" +
		 * " WHERE M.\"DataYMD\" = " + dataYMD +
		 * " ORDER BY M.\"BankItem\", M.\"BranchItem\", M.\"CustId\", M.\"AcDate\", M.\"AcctNo\" "
		 * ; }
		 */

		logger.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}