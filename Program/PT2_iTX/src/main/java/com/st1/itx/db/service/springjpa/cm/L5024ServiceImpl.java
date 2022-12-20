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

@Service("L5024ServiceImpl")
public class L5024ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	public List<Map<String, String>> FindData(int index, int limit, String DeptCode, String DistCode, String UnitCode, TitaVo titaVo) throws Exception {
		
		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		String sql ="";
		sql += " select \"UnitCode\" "; // 單位代號
		sql += "      , \"UnitItem\" ";
		sql += "      , \"DistCode\" "; // 區部代號
		sql += "      , \"DistItem\" ";
		sql += "      , \"DeptCode\" "; // 部室代號
		sql += "      , \"DeptItem\" ";
		sql += "      , \"EmpNo\" ";
		sql += "      , \"EmpName\" ";
		sql += "      , \"DirectorCode\" ";
		sql += "      , \"DepartOfficer\" ";
		sql += "      , \"GoalCnt\" ";
		sql += "      , \"SumGoalCnt\" ";
		sql += "      , \"GoalAmt\" ";
		sql += "      , \"SumGoalAmt\" ";
		sql += " from \"PfDeparment\" ";
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
		// 2022-12-15 暫定排序
		// 部室代號由小到大
		// 同部室時，區部代號由小到大
		// 同區部時，單位代號由小到大
		sql += "  ORDER BY \"DeptCode\" ";
		sql += "         , NVL(\"DistCode\",'000000') ";
		sql += "         , NVL(\"UnitCode\",'000000') ";

		this.info("sql = " + sql);
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		Query query = em.createNativeQuery(sql);
		return switchback(query);
	}
}
