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

@Service("lNM001ServiceImpl")
@Repository

/*
 * LNM001 清單1：表內放款與應收帳款-資產基本資料與計算原始有效利率用
 */

public class LNM001ServiceImpl extends ASpringJpaParm implements InitializingBean {

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

		this.info("----------- LNM001.findAll ---------------");
		this.info("-----LNM001 TitaVo=" + titaVo);
		this.info("-----LNM001 Tita ENTDY=" + titaVo.getEntDy().substring(0, 6));

		int dateMonth = Integer.parseInt(titaVo.getEntDy().substring(0, 6)) + 191100; // 年月份(西元年月)

//		TEST
//		if (onLineMode == true) {
//			dateMonth = 202004;
//		}

		this.info("dataMonth= " + dateMonth);

		String sql = "";

		// 清單1：表內放款與應收帳款-資產基本資料與計算原始有效利率用
		sql = "SELECT \"CustNo\", \"CustId\", \"FacmNo\", \"ApplNo\", \"BormNo\"" + ", \"AcCode\", \"Status\", \"FirstDrawdownDate\", \"DrawdownDate\", \"FacLineDate\""
				+ ", \"MaturityDate\", \"LineAmt\", \"DrawdownAmt\", \"AcctFee\", \"LoanBal\"" + ", \"IntAmt\", \"Fee\", \"Rate\", \"OvduDays\", \"OvduDate\""
				+ ", \"BadDebtDate\", \"BadDebtAmt\", \"GracePeriod\", \"ApproveRate\"" + ", \"AmortizedCode\", \"RateCode\", \"RepayFreq\", \"PayIntFreq\", \"IndustryCode\"" + ", \"ClTypeJCIC\""
				+ ", CASE" + "    WHEN \"CityCode\" = '05' THEN 'A'" + "    WHEN \"CityCode\" = '10' THEN 'B'" + "    WHEN \"CityCode\" = '15' THEN 'C'" + "    WHEN \"CityCode\" = '35' THEN 'D'"
				+ "    WHEN \"CityCode\" = '65' THEN 'E'" + "    WHEN \"CityCode\" = '70' THEN 'F'" + "    ELSE 'G'" + "  END                                  as \"CityCode\""
				+ ", \"BaseRateCode\", \"CustKind\", \"AssetKind\"" + ", \"ProdNo\", \"EvaAmt\", \"FirstDueDate\", \"TotalPeriod\"" + ", CASE"
				+ "    WHEN NVL(\"LineAmt\",0) > NVL(\"UtilBal\",0) THEN NVL(\"LineAmt\",0) - NVL(\"UtilBal\",0)" + "    ELSE 0 " + "  END                                  as \"AvblBal\""
				+ ", \"RecycleCode\", \"IrrevocableFlag\"" + ", \"TempAmt\", \"AcCurcd\", \"AcBookCode\", \"CurrencyCode\", \"ExchangeRate\"" + ", \"LineAmt\"      as \"txLineAmt\""
				+ ", \"DrawdownAmt\"  as \"txDrawdownAmt\"" + ", \"AcctFee\"      as \"txAcctFee\"" + ", \"LoanBal\"      as \"txLoanBal\"" + ", \"IntAmt\"       as \"txIntAmt\""
				+ ", \"Fee\"          as \"txFee\"" + ", CASE" + "    WHEN NVL(\"LineAmt\",0) > NVL(\"UtilBal\",0) THEN NVL(\"LineAmt\",0) - NVL(\"UtilBal\",0)" + "    ELSE 0 "
				+ "  END              as \"txAvblBal\"" + ", \"TempAmt\"      as \"txTempAmt\"" + " FROM  \"Ias34Ap\"" + " WHERE \"DataYM\" = " + dateMonth
				+ " ORDER BY \"CustNo\", \"FacmNo\", \"BormNo\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em;
		if (onLineMode == true) {
			em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine); // onLine 資料庫
		} else {
			em = this.baseEntityManager.getCurrentEntityManager(titaVo); // 從 LNM001.java 帶入資料庫環境
		}
		query = em.createNativeQuery(sql);

		// 轉成 List<HashMap<String, String>>
		return this.convertToMap(query.getResultList());
	}
}
