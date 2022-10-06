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
import com.st1.itx.util.parse.Parse;

@Service("L9741ServiceImpl")
@Repository
/* 逾期放款明細 */
public class L9741ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	private Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws LogicException {

		this.info("L9741.findAll");
		int intInsuYearMonth = parse.stringToInteger(titaVo.getParam("InsuYearMonth")) + 191100;
		int intCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		int intSearchOption = parse.stringToInteger(titaVo.getParam("SearchOption"));//1:正常未繳;2:借支;3:催收未繳;9:全部
		this.info("intInsuYearMonth = " + intInsuYearMonth);
		this.info("intSearchOption = " + intSearchOption);

		String sql = " ";
		sql += " WITH rawData AS (                           ";
		sql += "   SELECT  \"CustNo\" ,  \"FacmNo\"           ";
		sql += "          , MIN(\"NextRepayDate\")  AS \"NextRepayDate\"    ";
		sql += "   FROM  \"LoanBorMain\"              ";
        sql += "   WHERE  \"Status\" NOT IN (1 , 3 , 5 , 9) ";
        sql += "   GROUP BY \"CustNo\" ,  \"FacmNo\"  ";
        sql += "  )";		

        sql += " SELECT                                                    ";
		sql += "  I.\"InsuYearMonth\"    AS \"InsuYearMonth\"               ";
		sql += " ,I.\"CustNo\"           AS \"CustNo\"                      ";
		sql += " ,I.\"FacmNo\"           AS \"FacmNo\"                      ";
		sql += " ,I.\"FireInsuCovrg\"    AS \"FireInsuCovrg\"               ";
		sql += " ,I.\"EthqInsuCovrg\"    AS \"EthqInsuCovrg\"               ";
		sql += " ,I.\"FireInsuPrem\"     AS \"FireInsuPrem\"                ";
		sql += " ,I.\"EthqInsuPrem\"     AS \"EthqInsuPrem\"                ";
		sql += " ,I.\"InsuStartDate\"    AS \"InsuStartDate\"               ";
		sql += " ,I.\"InsuEndDate\"      AS \"InsuEndDate\"                 ";
		sql += " ,I.\"TotInsuPrem\"      AS \"TotInsuPrem\"                 ";
		sql += " ,I.\"RepayCode\"        AS \"RepayCode\"                   ";
		sql += " ,I.\"StatusCode\"       AS \"StatusCode\"                  ";
		sql += " ,CB.\"BdLocation\"      AS \"BdLocation\"                  ";
		sql += " ,R.\"NextRepayDate\"    AS \"NextRepayDate\"               ";

		sql += " FROM \"InsuRenew\" I                                       ";
		sql += " LEFT JOIN \"ClBuilding\" CB ON CB.\"ClCode1\"  = I.\"ClCode1\"  ";
		sql += "                            AND CB.\"ClCode2\"  = I.\"ClCode2\"  ";
		sql += "                            AND CB.\"ClNo\"     = I.\"ClNo\"     ";
		sql += " LEFT JOIN rawData R ON R.\"CustNo\"  = I.\"CustNo\"     ";
		sql += "                    AND R.\"FacmNo\"  = I.\"FacmNo\"     ";
		
		sql += " WHERE I.\"InsuYearMonth\" = :intInsuYearMonth"  ;

		if (intCustNo > 0) {
			sql += " AND  I.\"CustNo\" =  :intCustNo     \n";
		}

		// SearchOption 1:正常未繳;2:借支;3:催收未繳;9:全部
		// status 0:正常 1:借支 2:催收 4:結案
		switch (intSearchOption) {
		case 1: //  0:正常未繳
			sql += "   AND I.\"StatusCode\" = 0                            ";
			break;
		case 2: //  1:借支
			sql += "   AND I.\"StatusCode\" = 1                            ";
			break;
		case 3: //  2:催收 未入帳
			sql += "   AND I.\"StatusCode\" = 2                            ";
			break;
		case 9: // 全部:0~2
			sql += "   AND I.\"StatusCode\" in (0, 1, 2)                   ";
			break;
		}

		sql += "   AND I.\"RenewCode\" = 2                             ";
		sql += "   AND I.\"AcDate\" = 0                                ";
		sql += "  ORDER BY I.\"InsuYearMonth\", I.\"CustNo\", I.\"FacmNo\" ";

		this.info("sql=" + sql);
		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("intInsuYearMonth", intInsuYearMonth);

		if (intCustNo > 0) {
			query.setParameter("intCustNo", intCustNo);
		}

		return this.convertToMap(query);
	}

}