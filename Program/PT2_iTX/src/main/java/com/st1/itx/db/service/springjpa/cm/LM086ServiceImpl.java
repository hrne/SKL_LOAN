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

@Service("LM086ServiceImpl")
@Repository
public class LM086ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> doQuery(TitaVo titaVo) throws Exception {
		this.info("LM086ServiceImpl doQuery");

		int inputYear = Integer.parseInt(titaVo.getParam("inputYear"));
		int inputMonth = Integer.parseInt(titaVo.getParam("inputMonth"));
		String lowerLimit = titaVo.getParam("lowerLimit");

		int yearMonth = (inputYear + 1911) * 100 + inputMonth;

		this.info("LM086ServiceImpl inputYear = " + inputYear);
		this.info("LM086ServiceImpl inputMonth = " + inputMonth);
		this.info("LM086ServiceImpl lowerLimit = " + lowerLimit);

		String sql = "";
		sql += " SELECT ";
		sql += "   M.\"AcctCode\" ";
		sql += " , CAC.\"AcctItem\" ";
		sql += " , LPAD(M.\"CustNo\",7,'0') || '-' || LPAD(M.\"FacmNo\",3,'0') AS \"CustNo\" ";
		sql += " , SUBSTR(CM.\"CustName\",0,8) AS \"CustName\" ";
		sql += " , \"Fn_GetCdCode\"('CustTypeCode', FM.\"CustTypeCode\") AS \"CustType\" ";
		sql += " , M.\"PrinBalance\" ";
		sql += " , M.\"StoreRate\" ";
		sql += " , \"Fn_GetCdCode\"('ClCode2' || M.\"ClCode1\" , LPAD(M.\"ClCode2\",2,'0')) AS \"ClItem\" ";
		sql += " , CI.\"IndustryItem\" ";
		sql += " FROM \"MonthlyFacBal\" M ";
		sql += " LEFT JOIN \"CdAcCode\" CAC ON CAC.\"AcctCode\" = M.\"AcctCode\" ";
		sql += " LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\" = M.\"CustNo\" ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = M.\"CustNo\" ";
		sql += " LEFT JOIN \"FacMain\" FM ON FM.\"CustNo\" = M.\"CustNo\" ";
		sql += "                         AND FM.\"FacmNo\" = M.\"FacmNo\" ";
		sql += " LEFT JOIN \"CdIndustry\" CI ON CI.\"IndustryCode\" = CM.\"IndustryCode\" ";
		sql += " WHERE M.\"YearMonth\" = :yearMonth ";
		sql += "   AND M.\"PrinBalance\" >=  :lowerLimit ";
		sql += "   AND M.\"PrinBalance\" > 0 ";
		sql += "   AND M.\"AcctCode\" != '990' ";
		sql += " ORDER BY M.\"AcctCode\" ";
		sql += "        , M.\"CustNo\" ";
		sql += "        , M.\"FacmNo\" ";
		this.info("sql = " + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		Query query = em.createNativeQuery(sql);

		query.setParameter("yearMonth", yearMonth);
		query.setParameter("lowerLimit", lowerLimit);

		return this.convertToMap(query);
	}
}