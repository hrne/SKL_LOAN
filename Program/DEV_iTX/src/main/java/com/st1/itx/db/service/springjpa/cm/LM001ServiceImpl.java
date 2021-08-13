package com.st1.itx.db.service.springjpa.cm;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service
@Repository
/* 逾期放款明細 */
public class LM001ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LM001ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	/**
	 * 查詢本月及上月的戶數及金額
	 * 
	 * @param thisMonth 西元年本月月份(ex.帳務日為20201231時,請傳入202012)
	 * @param lastMonth 西元年上月月份(ex.帳務日為20201231時,請傳入202011)
	 * @param titaVo    titaVo
	 * @return 查詢結果只會有一筆
	 */
	public List<Map<String, String>> doQuery(int thisMonth, int lastMonth, TitaVo titaVo) {

		logger.info("doQuery");
		
		String sql = "SELECT \"ThisMonthCnt\" ";
		sql += "            ,NVL(\"ThisMonthLoanBalTotal\",0) AS \"ThisMonthLoanBalTotal\" ";
		sql += "            ,\"LastMonthCnt\" ";
		sql += "            ,NVL(\"LastMonthLoanBalTotal\",0) AS \"LastMonthLoanBalTotal\" ";
		sql += "            ,\"ThisMonthAddCnt\" ";
		sql += "            ,NVL(\"ThisMonthAddLoanBalTotal\",0) AS \"ThisMonthAddLoanBalTotal\" ";
		// 計算本月
		sql += "      FROM ( SELECT COUNT(*)              AS \"ThisMonthCnt\"";
		sql += "           		  , SUM(\"LoanBalTotal\") AS \"ThisMonthLoanBalTotal\"";
		// 因為計算戶數時,需先歸戶到額度層
		// 所以多一層子查詢做統計
		sql += "             FROM ( SELECT \"CustNo\"";
		sql += "                         , \"FacmNo\"";
		sql += "                         , SUM(\"LoanBalance\") AS \"LoanBalTotal\"";
		sql += "                    FROM \"MonthlyLoanBal\"";
		sql += "                    WHERE \"AcctCode\" = '340'"; // 篩選業務科目340
		sql += "                      AND \"YearMonth\" = :thisMonth"; // 篩選本月
		sql += "                      AND \"LoanBalance\" > 0"; // 放款餘額大於零才計件
		sql += "                    GROUP BY \"CustNo\"";
		sql += "                           , \"FacmNo\"";
		sql += "                  ) \"ThisMonthDetail\""; // 以戶號&額度加總放款餘額
		sql += "           ) \"ThisMonth\"";
		// 計算上月
		sql += "         , ( SELECT COUNT(*)              AS \"LastMonthCnt\"";
		sql += "           		  , SUM(\"LoanBalTotal\") AS \"LastMonthLoanBalTotal\"";
		// 因為計算戶數時,需先歸戶到額度層
		// 所以多一層子查詢做統計
		sql += "             FROM ( SELECT \"CustNo\"";
		sql += "                         , \"FacmNo\"";
		sql += "                         , SUM(\"LoanBalance\") AS \"LoanBalTotal\"";
		sql += "                    FROM \"MonthlyLoanBal\"";
		sql += "                    WHERE \"AcctCode\" = '340'"; // 篩選業務科目340
		sql += "                      AND \"YearMonth\" = :lastMonth"; // 篩選上月
		sql += "                      AND \"LoanBalance\" > 0"; // 放款餘額大於零才計件
		sql += "                    GROUP BY \"CustNo\"";
		sql += "                           , \"FacmNo\"";
		sql += "                  ) \"LastMonthDetail\""; // 以戶號&額度加總放款餘額
		sql += "           ) \"LastMonth\"";
		// 計算本月新增
		sql += "         , ( SELECT COUNT(*)              AS \"ThisMonthAddCnt\"";
		sql += "           		  , SUM(\"LoanBalTotal\") AS \"ThisMonthAddLoanBalTotal\"";
		// 因為計算戶數時,需先歸戶到額度層
		// 所以多一層子查詢做統計
		sql += "             FROM ( SELECT M.\"CustNo\"";
		sql += "                         , M.\"FacmNo\"";
		sql += "                         , SUM(M.\"LoanBalance\") AS \"LoanBalTotal\"";
		sql += "                    FROM \"MonthlyLoanBal\" M ";
		sql += "                    LEFT JOIN \"LoanBorMain\" LB ON LB.\"CustNo\" = M.\"CustNo\"";
		sql += "                                                AND LB.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                                AND LB.\"BormNo\" = M.\"BormNo\"";
		sql += "                    WHERE M.\"AcctCode\" = '340'"; // 篩選業務科目340
		sql += "                      AND M.\"YearMonth\" = :thisMonth"; // 篩選本月
		sql += "                      AND M.\"LoanBalance\" > 0"; // 放款餘額大於零才計件
		sql += "                      AND TRUNC(LB.\"DrawdownDate\" / 100) = :thisMonth"; // 篩選本月撥貸
		sql += "                    GROUP BY M.\"CustNo\"";
		sql += "                           , M.\"FacmNo\"";
		sql += "                  ) \"ThisMonthAddDetail\""; // 以戶號&額度加總放款餘額
		sql += "           ) \"ThisMonthAdd\"";

		logger.info("sql = " + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("lastMonth", lastMonth);
		query.setParameter("thisMonth", thisMonth);

		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> doQueryRate(int thisMonth, TitaVo titaVo) {

		logger.info("doQueryRate");
		
		String sql = "SELECT NVL(\"FirstRateMin\",0)    AS \"FirstRateMin\"";
		sql += "            ,NVL(\"FirstRateMax\",0)    AS \"FirstRateMax\"";
		sql += "            ,NVL(\"ContinueRateMin\",0) AS \"ContinueRateMin\"";
		sql += "            ,NVL(\"ContinueRateMax\",0) AS \"ContinueRateMax\"";
		sql += "      FROM ( SELECT MIN(\"StoreRate\") AS \"FirstRateMin\"";
		sql += "                  , MAX(\"StoreRate\") AS \"FirstRateMax\"";
		sql += "             FROM ( SELECT M.\"StoreRate\"";
		sql += "                         , ROW_NUMBER() OVER (PARTITION BY M.\"CustNo\"";
		sql += "                                                         , M.\"FacmNo\"";
		sql += "                                                         , M.\"BormNo\"";
		sql += "                                              ORDER BY LR.\"EffectDate\" DESC ) AS \"Seq\"";
		sql += "                    FROM \"MonthlyLoanBal\" M";
		sql += "                    LEFT JOIN \"LoanRateChange\" LR ON LR.\"CustNo\" = M.\"CustNo\"";
		sql += "                                                   AND LR.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                                   AND LR.\"BormNo\" = M.\"BormNo\"";
		sql += "                    LEFT JOIN \"LoanBorMain\" LB ON LB.\"CustNo\" = M.\"CustNo\"";
		sql += "                                                AND LB.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                                AND LB.\"BormNo\" = M.\"BormNo\"";
		sql += "                    WHERE M.\"AcctCode\" = '340'"; // 篩選業務科目340
		sql += "                      AND M.\"YearMonth\" = :thisMonth"; // 篩選本月
		sql += "                      AND M.\"LoanBalance\" > 0"; // 放款餘額大於零才計件
		sql += "                      AND TRUNC(LB.\"DrawdownDate\" / 100) = :thisMonth"; // 篩選本月撥貸
		sql += "                      AND TRUNC(LR.\"EffectDate\" / 100 ) = :thisMonth "; // 篩選本月生效
		sql += "                      AND NVL(LR.\"EffectDate\",99991231) <= LB.\"PrevPayIntDate\"";
		sql += "                  ) \"FirstRateDetail\"";
		sql += "             WHERE \"Seq\" = 1";
		sql += "           ) \"FirstRate\"";
		sql += "         , ( SELECT MIN(\"StoreRate\") AS \"ContinueRateMin\"";
		sql += "                  , MAX(\"StoreRate\") AS \"ContinueRateMax\"";
		sql += "             FROM ( SELECT M.\"StoreRate\"";
		sql += "                         , ROW_NUMBER() OVER (PARTITION BY M.\"CustNo\"";
		sql += "                                                         , M.\"FacmNo\"";
		sql += "                                                         , M.\"BormNo\"";
		sql += "                                              ORDER BY LR.\"EffectDate\" DESC ) AS \"Seq\"";
		sql += "                    FROM \"MonthlyLoanBal\" M";
		sql += "                    LEFT JOIN \"LoanRateChange\" LR ON LR.\"CustNo\" = M.\"CustNo\"";
		sql += "                                                   AND LR.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                                   AND LR.\"BormNo\" = M.\"BormNo\"";
		sql += "                    LEFT JOIN \"LoanBorMain\" LB ON LB.\"CustNo\" = M.\"CustNo\"";
		sql += "                                                AND LB.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                                AND LB.\"BormNo\" = M.\"BormNo\"";
		sql += "                    WHERE M.\"AcctCode\" = '340'"; // 篩選業務科目340
		sql += "                      AND M.\"YearMonth\" = :thisMonth"; // 篩選本月
		sql += "                      AND M.\"LoanBalance\" > 0"; // 放款餘額大於零才計件
		sql += "                      AND TRUNC(LB.\"DrawdownDate\" / 100) < :thisMonth"; // 篩選非本月撥貸
		sql += "                      AND NVL(LR.\"EffectDate\",99991231) <= LB.\"PrevPayIntDate\"";
		sql += "                  ) \"ContinueRateDetail\"";
		sql += "             WHERE \"Seq\" = 1";
		sql += "           ) \"ContinueRate\"";

		logger.info("sql = " + sql);
		
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("thisMonth", thisMonth);
		return this.convertToMap(query.getResultList());
	}
	
	public List<Map<String, String>> findNewCase(int thisMonth, TitaVo titaVo) {

		logger.info("dofindNewCase");
		
		String sql = "SELECT M.\"CustNo\"";
		sql += "            ,M.\"FacmNo\"";
		sql += "            ,M.\"BormNo\"";
		sql += "            ,L.\"DrawdownDate\"";
		sql += "            ,M.\"StoreRate\"";
		sql += "      FROM \"MonthlyLoanBal\" M";
		sql += "      LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = M.\"CustNo\"";
		sql += "                                 AND L.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                 AND L.\"BormNo\" = M.\"BormNo\"";
		sql += "      WHERE TRUNC(L.\"DrawdownDate\" / 100) = :thisMonth";
		sql += "        AND M.\"AcctCode\" = '340'";
		sql += "        AND L.\"RenewFlag\" <> 'Y'";
		sql += "        AND M.\"LoanBalance\" > 0";

		logger.info("sql = " + sql);
		
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("thisMonth", thisMonth);
		return this.convertToMap(query.getResultList());
	}
	
	public List<Map<String, String>> findMinMaxRate(int thisMonth, TitaVo titaVo) {

		logger.info("dofindMinMaxRate");
		
		String sql = "SELECT MIN(M.\"StoreRate\")    AS \"MIN\"";
		sql += "            ,MAX(M.\"StoreRate\")    AS \"MAX\"";
		sql += "      FROM \"MonthlyLoanBal\" M";
		sql += "      LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = M.\"CustNo\"";
		sql += "                                 AND L.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                 AND L.\"BormNo\" = M.\"BormNo\"";
		sql += "      WHERE M.\"YearMonth\" = :thisMonth";
		sql += "        AND M.\"AcctCode\" = '340'";
		sql += "        AND L.\"Status\" IN ('0', '2')";
		sql += "        AND M.\"LoanBalance\" > 0";

		logger.info("sql = " + sql);
		
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("thisMonth", thisMonth);
		return this.convertToMap(query.getResultList());
	}
	
	public List<Map<String, String>> findHigherRate(int thisMonth, BigDecimal maxRate, TitaVo titaVo) {

		logger.info("dofindMinMaxRate");
		
		String sql = "SELECT M.\"CustNo\"";
		sql += "            ,M.\"FacmNo\"";
		sql += "            ,M.\"BormNo\"";
		sql += "            ,L.\"DrawdownDate\"";
		sql += "            ,L.\"DrawdownAmt\"";
		sql += "            ,M.\"StoreRate\"";
		sql += "            ,L.\"PrevPayIntDate\"";
		sql += "      FROM \"MonthlyLoanBal\" M";
		sql += "      LEFT JOIN \"LoanBorMain\" L ON L.\"CustNo\" = M.\"CustNo\"";
		sql += "                                 AND L.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                                 AND L.\"BormNo\" = M.\"BormNo\"";
		sql += "      WHERE M.\"YearMonth\" = :thisMonth";
		sql += "        AND M.\"AcctCode\" = '340'";
		sql += "        AND M.\"StoreRate\" > :maxRate";
		sql += "        AND L.\"Status\" IN ('0', '2')";
		sql += "        AND M.\"LoanBalance\" > 0";

		logger.info("sql = " + sql);
		
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("thisMonth", thisMonth);
		query.setParameter("maxRate", maxRate);
		return this.convertToMap(query.getResultList());
	}
}