package com.st1.itx.db.service.springjpa.cm;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Service("L5024ServiceImpl")
public class L5024ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L5024ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}
	
	// *** 折返控制相關 ***
	private int index;

	// *** 折返控制相關 ***
	private int limit;
	
	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";
	

	public List<Map<String, String>> FindData(int index, int limit,String DeptCode,String DistCode,String UnitCode,TitaVo titaVo) throws Exception{
		Query query;
		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		String sql = "select \"UnitCode\", \"UnitItem\", \"DistCode\", \"DistItem\", \"DeptCode\", \"DeptItem\", \"EmpNo\", \"EmpName\", ";
				sql +="\"DirectorCode\", \"DepartOfficer\", \"GoalCnt\", \"SumGoalCnt\", \"GoalAmt\", \"SumGoalAmt\" from \"PfDeparment\" ";
				if(!DeptCode.equals("")||!DistCode.equals("")||!UnitCode.equals("")) {
					sql +="where ";
				}
				if (!DeptCode.equals("")) {
					sql += "\"DeptCode\" = \'"+DeptCode+"\' ";	
				}
				if (!DistCode.equals("")) {
					if (!DeptCode.equals("")) {
						sql += " and";
					}
					sql += " \"DistCode\" = \'"+DistCode+"\' ";
				}
				if (!UnitCode.equals("")) {
					if (!DeptCode.equals("")) {
						if (!DistCode.equals("")) {
							sql += " and";
						}
					}
					sql += " \"UnitCode\" = \'"+UnitCode+"\' ";
				}
				sql += sqlRow;
		logger.info("sql = "+sql); 

		query = em.createNativeQuery(sql);
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);
		
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);
		
		logger.info("L5024Service FindData=" + query.toString());
		return this.convertToMap(query.getResultList());
	}
}
