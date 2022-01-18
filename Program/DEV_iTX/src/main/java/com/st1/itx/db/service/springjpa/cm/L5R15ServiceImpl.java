package com.st1.itx.db.service.springjpa.cm;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Service("l5R15ServiceImpl")
public class L5R15ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	public String FindL5R15(int CustNo, int FacmNo) throws Exception {
		String sql = "select " + "l.\"AccCollPsn\"," + "l.\"LegalPsn\"," + "l.\"PrevIntDate\"," + "l.\"PrinBalance\"," + "l.\"NextIntDate\"," + "l.\"ClNo\"," + "l.\"ClCode1\"," + "l.\"ClCode2\","
				+ "m.\"CustName\"," + "m.\"CustUKey\"," + "f.\"FirstDrawdownDate\"," + "f.\"UtilAmt\"," + "c.\"Fullname\" as AccCollPsnX,"// 催收人員姓名
				+ "d.\"Fullname\" as LegalPsnX " // 法務人員姓名
				+ "from \"CollList\" l " + "left join \"CustMain\" m on m.\"CustNo\" = l.\"CustNo\" " + "left join \"FacMain\" f on f.\"CustNo\" = l.\"CustNo\" and f.\"FacmNo\" = l.\"FacmNo\" "
				+ "left join \"CdEmp\" c on c.\"EmployeeNo\" = l.\"AccCollPsn\" " + "left join \"CdEmp\" d on d.\"EmployeeNo\" = l.\"LegalPsn\" " + "where l.\"CustNo\" = " + CustNo
				+ " and l.\"FacmNo\" = " + FacmNo;

		this.info("sql = " + sql);
		return sql;
	}

	public List<Map<String, String>> FindData(String sql, TitaVo titaVo) throws Exception {
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		this.info("L5R15Service FindData=" + query.toString());
		return this.convertToMap(query.getResultList());
	}
}
