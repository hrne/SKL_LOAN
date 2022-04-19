package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
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

@Service("lNM008ServiceImpl")
@Repository

/*
 * LNM008 清單8：放款與應收帳款-風險參數用
 */

public class LNM008ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private LoanBorMainRepository loanBorMainRepos;

	@Override
	public void afterPropertiesSet() throws Exception {
		org.junit.Assert.assertNotNull(loanBorMainRepos);
	}

	public List findAll(TitaVo titaVo) throws Exception {
//		boolean onLineMode = true;
		boolean onLineMode = false;

		this.info("----------- LNM008.findAll ---------------");
		this.info("-----LNM008 TitaVo=" + titaVo);
		this.info("-----LNM008 Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		int dateMonth = Integer.parseInt(titaVo.getEntDy().substring(0, 6)) + 191100; // 年月份(西元年月)

//		TEST
//		if (onLineMode == true) {
//			dateMonth = 202003;
//		}

		this.info("dataMonth= " + dateMonth);

		String sql = "";

		// 清單8：放款與應收帳款-風險參數用
		sql = "SELECT F.\"CustNo\"" + "     , F.\"CustId\"" + "     , F.\"FacmNo\"" + "     , F.\"ApplNo\"" + "     , CASE" + "         WHEN F.\"EntCode\" = '0' THEN '2'"
				+ "         WHEN F.\"EntCode\" = '1' THEN '1'" + "         WHEN F.\"EntCode\" = '2' THEN '1'" + "         ELSE '2'" + "       END                   AS \"EntCode\"" + "     , CASE"
				+ "         WHEN F.\"ApproveDate\" = 0 THEN ''" + "         ELSE to_char(F.\"ApproveDate\")" + "       END                   AS \"ApproveDate\"" + "     , CASE"
				+ "         WHEN F.\"FirstDrawdownDate\" = 0 THEN ''" + "         ELSE to_char(F.\"FirstDrawdownDate\")" + "       END                   AS \"FirstDrawdownDate\"" + "     , CASE"
				+ "         WHEN F.\"CurrencyCode\" = 'TWD' THEN F.\"LineAmt\"" + "         ELSE 0" + "       END                   AS \"LineAmt\"" + "     , F.\"ProdNo\"" + "     , CASE"
				+ "         WHEN F.\"CurrencyCode\" = 'TWD' THEN" + "           CASE WHEN F.\"LineAmt\" > F.\"UtilBal\" THEN ( F.\"LineAmt\" - F.\"UtilBal\" )" + "                ELSE 0"
				+ "           END" + "         ELSE 0" + "       END                   AS \"UsableBal\"" + "     , F.\"RecycleCode\"" + "     , F.\"IrrevocableFlag\"" + "     , F.\"IndustryCode\""
				+ "     , ''                    AS \"OrgRating\"" + "     , ''                    AS \"OrgModel\"" + "     , ''                    AS \"FinRating\""
				+ "     , ''                    AS \"FinModel\"" + "     , ''                    AS \"PDModel\"" + "     , ''                    AS \"PD\""
				+ "     , F.\"LineAmt\"           AS \"CurrLineAmt\"" + "     , CASE" + "         WHEN F.\"LineAmt\" > F.\"UtilBal\" THEN ( F.\"LineAmt\" - F.\"UtilBal\" )" + "         ELSE 0"
				+ "       END                   AS \"CurrUsableBal\"" + " FROM  \"Ifrs9FacData\" F" + " WHERE F.\"DataYM\" = " + dateMonth + "   AND F.\"Status\" IN (0, 2, 4, 6)"
				+ " ORDER BY \"CustNo\", \"FacmNo\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode == true) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LNM008.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}
