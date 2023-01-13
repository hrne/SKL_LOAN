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
import com.st1.itx.util.parse.Parse;

@Service
@Repository
public class LM031ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo, int entdy) throws Exception {

		this.info("lM031.findAll ");
		String sql = "";
		sql += " SELECT   LB.\"CustNo\" ";
		sql += "         ,LB.\"FacmNo\" ";
		sql += "         ,\"Fn_ParseEOL\"(C.\"CustName\", 0) \"CustName\" ";
		sql += "         ,LB.\"BormNo\" ";
		sql += "         ,F.\"LineAmt\" ";
		sql += "         ,LB.\"LoanBal\" ";
		sql += "         ,C.\"EntCode\" ";
		sql += "         ,F.\"RecycleCode\" ";
		sql += "         ,F.\"RecycleDeadline\" ";
		sql += "         ,F.\"UtilDeadline\" ";
		sql += "         ,LB.\"PrevPayIntDate\" ";
		sql += " FROM \"LoanBorMain\" LB ";
		sql += " LEFT JOIN \"FacMain\" F ON LB.\"CustNo\" = F.\"CustNo\" ";
		sql += "                        AND LB.\"FacmNo\" = F.\"FacmNo\" ";
		sql += " LEFT JOIN \"CollList\" L ON L.\"CustNo\" = F.\"CustNo\" ";
		sql += "                         AND L.\"FacmNo\" = F.\"FacmNo\" ";
		sql += " LEFT JOIN \"CustMain\" C ON C.\"CustNo\" = F.\"CustNo\" ";
		sql += " WHERE C.\"EntCode\" IN ('1','2') ";//--1:企金、2：企金自然人
		sql += "   AND (LB.\"LoanBal\" > 0";//-- 有放款餘額出表 
		sql += "    OR (F.\"UtilAmt\" > 0 ";//--或未撥款的且可動用餘額>0 出表
		sql += "   AND F.\"UtilBal\" = 0))";
		sql += " ORDER BY LB.\"CustNo\" ";
		sql += "         ,LB.\"FacmNo\" ";
		sql += "         ,LB.\"BormNo\" ";
		sql += "  ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

}