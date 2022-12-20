package com.st1.itx.db.service.springjpa.cm;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.parse.Parse;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Service
public class L5R10ServiceImpl extends ASpringJpaParm implements InitializingBean {
	@Autowired
	private Parse parse;

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	public List<Map<String, String>> doQuery(TitaVo titaVo) throws LogicException {
		int iWorkMonth = Integer.valueOf(titaVo.getParam("RimWorkMonth")) + 191100;
		String iEmpNo = titaVo.getParam("RimEmpNo").trim();// 員工代號

		String sql = "";
		sql += " SELECT SUM(NVL(B.\"PerfCnt\",0)) AS \"PfCnt\" ";
		sql += "      , SUM(NVL(B.\"DrawdownAmt\",0)) AS \"DrawdownAmt\" ";
		sql += " FROM \"PfBsDetail\" B ";
		sql += " WHERE B.\"WorkMonth\" = :workMonth ";
		sql += "   AND B.\"BsOfficer\" = :empNo ";
		this.info("sql = " + sql);

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		Query query = em.createNativeQuery(sql);

		query.setParameter("workMonth", iWorkMonth);
		query.setParameter("empNo", iEmpNo);

		return this.convertToMap(query);
	}
}
