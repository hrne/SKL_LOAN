package com.st1.itx.db.service.springjpa.cm;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Service("l5944ServiceImpl")
public class L5944ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L5944ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	public List<Map<String, String>> FindData(String DeptCode, String DistCode, String UnitCode) throws Exception {
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		String sql = "select \"UnitCode\", \"UnitItem\", \"DistCode\", \"DistItem\", \"DeptCode\", \"DeptItem\", \"EmpNo\", \"EmpName\", ";
		sql += "\"DirectorCode\", \"DepartOfficer\", \"GoalCnt\", \"SumGoalCnt\", \"GoalAmt\", \"SumGoalAmt\", \"WorkMonth\" from \"PfDeparment\" ";
		if (!DeptCode.equals("") || !DistCode.equals("") || !UnitCode.equals("")) {
			sql += "where ";
		}
		if (!DeptCode.equals("")) {
			sql += "\"DeptCode\" = \'" + DeptCode + "\' ";
		}
		if (!DistCode.equals("")) {
			if (!DeptCode.equals("")) {
				sql += " and";
			}
			sql += " \"DistCode\" = \'" + DistCode + "\' ";
		}
		if (!UnitCode.equals("")) {
			if (!DeptCode.equals("")) {
				if (!DistCode.equals("")) {
					sql += " and";
				}
			}
			sql += " \"UnitCode\" = \'" + UnitCode + "\' ";
		}
		sql += "order by \"WorkMonth\" asc";
		logger.info("sql = " + sql);

		query = em.createNativeQuery(sql);
		logger.info("L5944Service FindData=" + query.toString());
		return this.convertToMap(query.getResultList());
	}
}
