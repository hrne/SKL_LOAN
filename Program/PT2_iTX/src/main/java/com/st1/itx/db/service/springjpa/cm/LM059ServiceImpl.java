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
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service
@Repository
/* 逾期放款明細 */
public class LM059ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}
	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * @param ilYearMonth 上西元年月
	 */
	public List<Map<String, String>> findAll(TitaVo titaVo, int yearMonth,int ilYearMonth) throws Exception {

		

		String iYearMonth = String.valueOf(yearMonth);

		this.info("lM059.findAll YYMM=" + iYearMonth);

		String sql = "SELECT SUM(CASE WHEN \"AcNoCode\" IN ('10603','10604') THEN  \"TdBal\" ELSE 0 END )   AS F0";
		sql += "            ,SUM(CASE WHEN \"AcNoCode\" IN ('10623','10624') THEN  \"TdBal\" ELSE 0 END )   AS F1";
		sql += "            ,SUM(CASE WHEN \"AcNoCode\" IN ('10603','10604','10623','10624') THEN  \"TdBal\" ELSE 0 END ) AS F2";
		sql += "           FROM \"CoreAcMain\" ";
		sql += "           WHERE \"AcDate\" =  TO_NUMBER(TO_CHAR(last_day(TO_DATE(TO_CHAR(:yymm*100+1), 'YYYYMMDD')),'YYYYMMDD'))";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("yymm", iYearMonth);
		query.setParameter("lyymm", ilYearMonth);

		return this.convertToMap(query);
	}

}