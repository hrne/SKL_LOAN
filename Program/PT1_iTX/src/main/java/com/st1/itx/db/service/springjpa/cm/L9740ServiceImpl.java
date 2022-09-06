package com.st1.itx.db.service.springjpa.cm;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service
@Repository
public class L9740ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	/**
	 * Query(新撥款之戶號)
	 * 
	 * @param titaVo
	 * @param dDate 撥款日(西元)
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> findPage1(TitaVo titaVo, int dDate) throws Exception {
		this.info("l9740.findPage1 ");

		int startDate = Integer.valueOf(String.valueOf(dDate / 100) + "01");

		String sql = " ";
		sql += "	SELECT M.\"CustNo\"";
		sql += "		  ,M.\"FacmNo\"";
		sql += "		  ,M.\"BormNo\"";
		sql += "		  ,M.\"DrawdownDate\"";
		sql += "		  ,M.\"DrawdownAmt\"";
		sql += "		  ,M.\"StoreRate\"";
		sql += "	FROM \"LoanBorMain\" M";
		sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "						   AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "	WHERE M.\"DrawdownAmt\" BETWEEN :startDate AND :endDate";
		sql += "	  AND M.\"RenewFlag\" <> 1 ";
		sql += "	  AND F.\"AcctCode\" = 340";
		sql += "	ORDER BY M.\"CustNo\" ASC";
		sql += "			,M.\"FacmNo\" ASC";
		sql += "			,M.\"BormNo\" ASC";


		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", dDate);
		return this.convertToMap(query);
	}

	/**
	 * Query(續期放款利率 最低 最高)
	 * 
	 * @param titaVo
	 * @param dDate 撥款日(西元)
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> findPage2(TitaVo titaVo, int dDate ) throws Exception {
		this.info("l9740.findPage2 ");

		String sql = " ";
		sql += "	SELECT MAX(M.\"StoreRate\") AS \"minRate\"";
		sql += "		  ,MAX(M.\"StoreRate\") AS \"maxRate\"";
		sql += "	FROM \"LoanBorMain\" M";
		sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "						   AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "	WHERE M.\"DrawdownAmt\" <= :endDate";
		sql += "	  AND M.\"Status\" IN (0,2)";
		sql += "	  AND F.\"AcctCode\" = 340";

		this.info("sql2=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("endDate", dDate);
		return this.convertToMap(query);
	}

	/**
	 * Query(x利率超過 X%之借戶)
	 * 
	 * @param titaVo
	 * @param dDate 撥款日(西元)
	 * @param rate 利率
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> findPage3(TitaVo titaVo, int dDate, BigDecimal rate) throws Exception {
		this.info("l9740.findPage3 ");

		String sql = " ";
		sql += "	SELECT M.\"CustNo\"";
		sql += "		  ,M.\"FacmNo\"";
		sql += "		  ,M.\"BormNo\"";
		sql += "		  ,M.\"DrawdownDate\"";
		sql += "		  ,M.\"DrawdownAmt\"";
		sql += "		  ,M.\"StoreRate\"";
		sql += "		  ,M.\"NextPayIntDate\"";
		sql += "	FROM \"LoanBorMain\" M";
		sql += "	LEFT JOIN \"FacMain\" F ON F.\"CustNo\" = M.\"CustNo\"";
		sql += "						   AND F.\"FacmNo\" = M.\"FacmNo\"";
		sql += "	WHERE M.\"DrawdownAmt\" <= :endDate";
		sql += "	  AND M.\"StoreRate\" > :rate ";
		sql += "	  AND M.\"Status\" IN (0,2)";
		sql += "	  AND F.\"AcctCode\" = 340";
		sql += "	ORDER BY M.\"CustNo\" ASC";
		sql += "			,M.\"FacmNo\" ASC";
		sql += "			,M.\"BormNo\" ASC";

		this.info("sql3=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);
		query.setParameter("rate", rate);
		query.setParameter("endDate", dDate);
		return this.convertToMap(query);
	}

}