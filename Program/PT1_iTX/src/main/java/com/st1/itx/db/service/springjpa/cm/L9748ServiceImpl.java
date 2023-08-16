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

@Service
@Repository
public class L9748ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private Parse parse;

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws LogicException {
		this.info("L9748ServiceImpl.findAll");

		int startDate = parse.stringToInteger(titaVo.get("StartDate")) + 19110000;
		int endDate = parse.stringToInteger(titaVo.get("EndDate")) + 19110000;

		this.info("startDate = " + startDate);
		this.info("endDate = " + endDate);

		String sql = "";
		sql += " SELECT IR.\"NowInsuNo\" ";
		sql += "      , IR.\"PrevInsuNo\" ";
		sql += "      , CM.\"CustName\" ";
		sql += "      , CM.\"CustId\" ";
		sql += "      , IR.\"InsuStartDate\" ";
		sql += "      , IR.\"InsuEndDate\" ";
		sql += "      , IC.\"InsuCate\" ";
		sql += "      , IR.\"FireInsuCovrg\" ";
		sql += "      , IR.\"FireInsuPrem\" ";
		sql += "      , IR.\"EthqInsuCovrg\" ";
		sql += "      , IR.\"EthqInsuPrem\" ";
		sql += "      , IC.\"EmpName\" ";
		sql += "      , IC.\"FireOfficer\" ";
		sql += "      , IC.\"DueAmt\" ";
		sql += "      , IR.\"InsuCompany\" ";
		sql += "      , IR.\"ClCode1\" ";
		sql += "      , IR.\"ClCode2\" ";
		sql += "      , IR.\"ClNo\" ";
		sql += " FROM \"InsuRenew\" IR ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = IR.\"CustNo\" ";
		sql += " LEFT JOIN ( ";
		sql += " 	SELECT \"NowInsuNo\" ";
		sql += "      	 , \"InsuCate\" ";
		sql += "         , \"EmpName\" ";
		sql += "         , \"FireOfficer\" ";
		sql += "      	 , SUM( \"DueAmt\" ) AS \"DueAmt\" ";
		sql += " 	FROM \"InsuComm\"  ";
		sql += "    GROUP BY \"NowInsuNo\" ";
		sql += "      	   , \"InsuCate\" ";
		sql += "           , \"EmpName\" ";
		sql += "           , \"FireOfficer\" ";
		sql += ") IC ON IC.\"NowInsuNo\" = IR.\"NowInsuNo\" ";
		sql += " WHERE IR.\"NowInsuNo\" IS NOT NULL ";
		sql += "   AND IR.\"InsuEndDate\" >= :inputStartDate ";
		sql += "   AND IR.\"InsuEndDate\" <= :inputEndDate ";
		sql += " ORDER BY IR.\"NowInsuNo\" ";
		sql += "        , IR.\"PrevInsuNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputStartDate", startDate);
		query.setParameter("inputEndDate", endDate);

		return this.convertToMap(query);
	}

}