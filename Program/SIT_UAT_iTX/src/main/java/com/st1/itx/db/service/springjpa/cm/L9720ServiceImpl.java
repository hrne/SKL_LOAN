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
import com.st1.itx.util.date.DateUtil;

@Service
@Repository
public class L9720ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		return findAll(titaVo, 0);
	}

	@Autowired
	private DateUtil dUtil;

	public List<Map<String, String>> findAll(TitaVo titaVo, int validTime) throws Exception {
		this.info("l9720.findAll ");

		String sql = "SELECT M.\"CustNo\" AS \"戶號\"";
		sql += "           ,M.\"FacmNo\" AS \"額度\"";
		sql += "           ,CUS.\"CustName\" AS \"姓名\"";
		sql += "           ,M.\"LineAmt\" AS \"核准額度\"";
		sql += "           ,M.\"FirstDrawdownDate\" AS \"首次撥款日\"";
		sql += "           ,M.\"MaturityDate\" AS \"到期日\"";
		sql += "           ,M.\"LoanBalance\" AS \"餘額\"";
		sql += "           ,M.\"ProdNo\" AS \"商品代碼\"";
		sql += "           ,NVL(IO.\"InsuCovrg\",0) + NVL(IR.\"InsuCovrg\",0) AS \"火險保額\"";
		sql += "           ,CASE WHEN NVL(IO.\"InsuEndDate\",0) > NVL(IR.\"InsuEndDate\",0)";
		sql += "                 THEN NVL(IO.\"InsuEndDate\",0)";
		sql += "                 ELSE NVL(IR.\"InsuEndDate\",0)";
		sql += "            END AS \"火險迄日\"";
		sql += "           ,M.\"BusinessOfficer\" AS \"房貸專員員編\"";
		sql += "           ,PF.\"Fullname\" AS \"房貸專員姓名\"";
		sql += "           ,M.\"CreditOfficer\" AS \"授信\"";
		sql += "           ,PF.\"AreaCode\" AS \"區域中心代號\"";
		sql += "           ,PF.\"AreaItem\" AS \"區域中心中文\"";
		sql += "           ,CASE TRUNC(M.\"FirstDrawdownDate\"/100)";
		sql += "            WHEN TO_NUMBER(:validYearMonthFirst) THEN 1";
		sql += "            WHEN TO_NUMBER(:validYearMonthSecond) THEN 2";
		sql += "            END AS \"檢核時間\"";
		sql += "     FROM ( SELECT F.\"CustNo\"                    AS \"CustNo\"";
		sql += "                  ,F.\"FacmNo\"                    AS \"FacmNo\"";
		sql += "                  ,F.\"ProdNo\"                    AS \"ProdNo\"";
		sql += "                  ,F.\"LineAmt\"                   AS \"LineAmt\"";
		sql += "                  ,F.\"FirstDrawdownDate\"         AS \"FirstDrawdownDate\"";
		sql += "                  ,B.\"MaturityDate\"              AS \"MaturityDate\"";
		sql += "                  ,R.\"FitRate\"                   AS \"FitRate\"";
		sql += "                  ,F.\"UtilAmt\"                   AS \"LoanBalance\"";
		sql += "                  ,F.\"BusinessOfficer\"           AS \"BusinessOfficer\"";
		sql += "                  ,F.\"CreditOfficer\"             AS \"CreditOfficer\"";
		sql += "                  ,ROW_NUMBER() OVER (PARTITION BY F.\"CustNo\", F.\"FacmNo\"";
		sql += "                                      ORDER BY R.\"EffectDate\" DESC)";
		sql += "                   AS ROW_NUMBER";
		sql += "            FROM \"FacMain\" F";
		sql += "            LEFT JOIN \"FacProd\" P ON P.\"ProdNo\" = F.\"ProdNo\"";
		sql += "            LEFT JOIN \"LoanBorMain\" B ON B.\"CustNo\" = F.\"CustNo\"";
		sql += "                                       AND B.\"FacmNo\"  = F.\"FacmNo\"";
		sql += "                                       AND B.\"BormNo\" = 1";
		sql += "            LEFT JOIN \"LoanRateChange\" R ON R.\"CustNo\" = F.\"CustNo\"";
		sql += "                                          AND R.\"FacmNo\"  = F.\"FacmNo\"";
		sql += "                                          AND R.\"EffectDate\" <= :makeDate";
		if (validTime == 0) {
			sql += "            WHERE TRUNC(F.\"FirstDrawdownDate\"/100) IN (:validYearMonthFirst,:validYearMonthSecond)";
		} else if (validTime == 1) {
			sql += "            WHERE TRUNC(F.\"FirstDrawdownDate\"/100) IN (:validYearMonthFirst)";
		} else if (validTime == 2) {
			sql += "            WHERE TRUNC(F.\"FirstDrawdownDate\"/100) IN (:validYearMonthSecond)";
		}
		sql += "              AND P.\"FinancialFlag\" = 'Y'";
		sql += "          ) M";
		sql += "     LEFT JOIN \"CustMain\" CUS ON CUS.\"CustNo\" = M.\"CustNo\"";
		sql += "     LEFT JOIN \"ClFac\" C ON C.\"CustNo\" = M.\"CustNo\"";
		sql += "                          AND C.\"FacmNo\" = M.\"FacmNo\"";
		sql += "                          AND C.\"MainFlag\" = 'Y'";
		sql += "     LEFT JOIN ( SELECT \"ClCode1\"";
		sql += "                       ,\"ClCode2\"";
		sql += "                       ,\"ClNo\"";
		sql += "                       ,MAX(\"InsuEndDate\")  AS \"InsuEndDate\"";
		sql += "                       ,SUM(\"FireInsuCovrg\" + \"EthqInsuCovrg\")  AS \"InsuCovrg\"";
		sql += "                 FROM \"InsuOrignal\"";
		sql += "                 WHERE \"InsuStartDate\" <= :makeDate";
		sql += "                   AND \"InsuEndDate\" > :makeDate";
		sql += "                 GROUP BY \"ClCode1\", \"ClCode2\", \"ClNo\"";
		sql += "               ) IO  ON IO.\"ClCode1\" = C.\"ClCode1\"";
		sql += "                    AND IO.\"ClCode2\" = C.\"ClCode2\"";
		sql += "                    AND IO.\"ClNo\"    = C.\"ClNo\"";
		sql += "     LEFT JOIN ( SELECT \"ClCode1\"";
		sql += "                       ,\"ClCode2\"";
		sql += "                       ,\"ClNo\"";
		sql += "                       ,MAX(\"InsuEndDate\")  AS \"InsuEndDate\"";
		sql += "                       ,SUM(\"FireInsuCovrg\" + \"EthqInsuCovrg\")  AS \"InsuCovrg\"";
		sql += "                 FROM \"InsuRenew\"";
		sql += "                 WHERE \"InsuStartDate\" <= :makeDate";
		sql += "                   AND \"InsuEndDate\" > :makeDate";
		sql += "                 GROUP BY \"ClCode1\", \"ClCode2\", \"ClNo\"";
		sql += "               ) IR  ON IR.\"ClCode1\" = C.\"ClCode1\"";
		sql += "                    AND IR.\"ClCode2\" = C.\"ClCode2\"";
		sql += "                    AND IR.\"ClNo\"    = C.\"ClNo\"";
		sql += "     LEFT JOIN \"CdWorkMonth\" W ON W.\"StartDate\" <= M.\"FirstDrawdownDate\"";
		sql += "                                AND W.\"EndDate\" >= M.\"FirstDrawdownDate\"";
		sql += "     LEFT JOIN \"PfBsOfficer\" PF ON PF.\"WorkMonth\" > = W.\"Year\" * 100 + W.\"Month\"";
		sql += "                                 AND PF.\"EmpNo\"  = M.\"BusinessOfficer\"";
		sql += "     WHERE M.ROW_NUMBER = 1";

		this.info("sql=" + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query;
		query = em.createNativeQuery(sql);

		String EntDy = Integer.toString(titaVo.getEntDyI() + 19110000);
		query.setParameter("makeDate", EntDy);

		// 10 個月前

		dUtil.init();
		dUtil.setDate_1(EntDy);
		dUtil.setMons(-10);
		int minus10 = dUtil.getCalenderDay();
		this.info("validYearMonthFirst = " + minus10);
		query.setParameter("validYearMonthFirst", minus10);

		// 22 個月前

		dUtil.init();
		dUtil.setDate_1(EntDy);
		dUtil.setMons(-22);
		int minus22 = dUtil.getCalenderDay();
		this.info("validYearMonthSecond = " + minus22);
		query.setParameter("validYearMonthSecond", minus22);

		return this.convertToMap(query);
	}

}