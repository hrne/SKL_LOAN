package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

/**
 * L9735 建商餘額明細
 * 
 * @author ST1-ChihWei
 * @version 1.0.0
 *
 */
@Service("L9735ServiceImpl")
@Repository
public class L9735ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> getConstructionCompanyLoanData(int inputDrawdownDate, TitaVo titaVo) {

		this.info("getConstructionCompanyLoanData");

		// 轉西元年
		if (inputDrawdownDate <= 19110000) {
			inputDrawdownDate += 19110000;
		}

		this.info("inputDrawdownDate = " + inputDrawdownDate);

		int entdy = titaVo.getEntDyI();

		// 轉西元年
		if (entdy <= 19110000) {
			entdy += 19110000;
		}

		String sql = "  ";

		sql += " SELECT CM.\"CustName\" ";
		sql += "      , MLB.\"CustNo\" ";
		sql += "      , MLB.\"FacmNo\" ";
		sql += "      , MLB.\"BormNo\" ";
		sql += "      , MLB.\"LoanBalance\" ";
		sql += "      , FM.\"LineAmt\" ";
		sql += "      , LBM.\"PrevPayIntDate\" ";
		sql += "      , MLB.\"ClCode1\" ";
		sql += "      , FM.\"UsageCode\" ";
		sql += "      , MLB.\"AcctCode\" ";
		sql += "      , MLB.\"ProdNo\" ";
		sql += "      , LBM.\"Status\" ";
		sql += "      , LBM.\"DrawdownDate\" ";
		sql += "      , CI.\"EvaNetWorth\" ";
		sql += " FROM \"ConstructionCompany\" CC ";
		sql += " LEFT JOIN \"MonthlyLoanBal\" MLB ON MLB.\"CustNo\" = CC.\"CustNo\" ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = MLB.\"CustNo\" ";
		sql += " LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\" = MLB.\"CustNo\" ";
		sql += "                       AND FM.\"FacmNo\" = MLB.\"FacmNo\" ";
		sql += " LEFT JOIN \"LoanBorMain\" LBM ON LBM.\"CustNo\" = MLB.\"CustNo\" ";
		sql += "                            AND LBM.\"FacmNo\" = MLB.\"FacmNo\" ";
		sql += "                            AND LBM.\"BormNo\" = MLB.\"BormNo\" ";
		sql += "LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = FM.\"CustNo\" ";
		sql += "                    AND CF.\"FacmNo\" = FM.\"FacmNo\" ";
		sql += "                    AND CF.\"MainFlag\" = 'Y' ";
		sql += "LEFT JOIN \"ClImm\" CI ON CI.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                    AND CI.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                    AND CI.\"ClNo\" = CF.\"ClNo\" ";
		sql += " WHERE MLB.\"YearMonth\" = :inputYearMonth ";
		sql += "   AND MLB.\"LoanBalance\" > 0 ";
		sql += "   AND NVL(CC.\"DeleteFlag\", ' ') != '*' ";
		sql += "   AND LBM.\"DrawdownDate\" <= :inputDrawdownDate ";
		sql += " ORDER BY CC.\"CustNo\" ";
		sql += "        , MLB.\"FacmNo\" ";
		sql += "        , MLB.\"BormNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputYearMonth", entdy / 100);
		query.setParameter("inputDrawdownDate", inputDrawdownDate);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> getBankOnline(TitaVo titaVo) {

		try {
			this.info("1." + titaVo.getParam("DATABASE"));
		} catch (LogicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String sql = "  ";
		sql += " SELECT COUNT(*) AS \"Int\" FROM \"CdBank\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> getBankMonth(TitaVo titaVo) {

		try {
			titaVo.setDataBaseOnMon();
			this.info("2." + titaVo.getParam("DATABASE"));
		} catch (LogicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String sql = "  ";

		sql += " SELECT COUNT(*) AS \"Int\" FROM \"CdBank\"";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}
}