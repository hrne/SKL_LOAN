package com.st1.itx.db.service.springjpa.cm;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.eum.ContentName;

@Service
@Repository
/* 逾期放款明細 */
public class LH001ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(LH001ServiceImpl.class);

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> findAll() throws Exception {
		logger.info("lH001.findAll ");
		String sql = "SELECT   \"F1\"";
		sql += "               , \"F2\"";
		sql += "               , \"F3\"";
		sql += "               , \"F4\"";
		sql += "               , \"F5\"";
		sql += "               , \"F6\"";// 總表
		sql += "               , SUM(F7)";
		sql += "               , SUM(F8)";
		sql += "        FROM ( SELECT B0.\"DeptItem\" AS F1";// 部室稱號
		sql += "                    , B0.\"DistItem\" AS F2";// 區部名稱
		sql += "                    , E0.\"Fullname\" AS F3";// 姓名
		sql += "                    , I.\"WorkMonth\" AS F4";// 工作月
		sql += "                    , W.\"StartDate\" AS F5";// 開始日期
		sql += "                    , W.\"EndDate\" AS F6";// 終止日期
		sql += "                    , I.\"PerfCnt\" AS F7";// 件數
		sql += "                    , I.\"PerfAmt\" AS F8";// 業績金額
		sql += "               FROM \"PfItDetail\" I";
		sql += "               LEFT JOIN \"CdBcm\" B0 ON B0.\"UnitCode\" = I.\"DeptCode\" ";
		sql += "               LEFT JOIN \"CdEmp\" E0 ON E0.\"EmployeeNo\" = B0.\"DeptManager\" ";
		sql += "               LEFT JOIN \"CdWorkMonth\" W ON   W.\"Year\" = TRUNC(I.\"WorkMonth\" / 100)";
		sql += "                                          AND  W.\"Month\" = MOD(I.\"WorkMonth\", 100)       ";
		sql += "               UNION ALL ";
		sql += "               SELECT DISTINCT B0.\"DeptItem\" AS F1";
		sql += "                             , B0.\"DistItem\" AS F2";
		sql += "                             , E0.\"Fullname\" AS F3";
		sql += "                             , W.\"Year\" * 100 + W.\"Month\"";
		sql += "                             , W.\"StartDate\" AS F5";
		sql += "                             , W.\"EndDate\" AS F6";
		sql += "                             , 0 AS F7";
		sql += "                             , 0 AS F8";
		sql += "               FROM \"PfItDetail\" I CROSS JOIN \"CdWorkMonth\" W";
		sql += "               LEFT JOIN \"CdBcm\" B0 ON B0.\"UnitCode\" = I.\"DeptCode\" ";
		sql += "               LEFT JOIN \"CdEmp\" E0 ON E0.\"EmployeeNo\" = B0.\"DeptManager\" ) ";
		sql += "        GROUP BY \"F1\"";
		sql += "                , \"F2\"";
		sql += "                , \"F3\"";
		sql += "                , \"F4\"";
		sql += "                , \"F5\"";
		sql += "                , \"F6\"";
		sql += "        ORDER BY \"F1\"";
		sql += "                , \"F2\"";
		sql += "                , \"F3\"";
		sql += "                , \"F4\"";
		sql += "                , \"F5\"";
		sql += "                , \"F6\"";
		logger.info("sql=" + sql);
		String sql1 = "SELECT \"F1\"";
		sql += "               , \"F2\"";
		sql += "               , \"F3\"";
		sql += "               , \"F4\"";
		sql += "               , \"F5\"";
		sql += "               , \"F6\"";// 部室分表
		sql += "               , SUM(F7)";
		sql += "               , SUM(F8)";
		sql += "         FROM ( SELECT B0.\"DistItem\" AS F1";// 部室名稱
		sql += "                     , B0.\"UnitItem\" AS F2";// 區部名稱
		sql += "                     , E0.\"Fullname\" AS F3";// 姓名
		sql += "                     , I.\"WorkMonth\" AS F4";// 工作月份
		sql += "                     , W.\"StartDate\" AS F5";// 開始日期
		sql += "                     , W.\"EndDate\" AS F6";// 結束日期
		sql += "                     , I.\"PerfCnt\" AS F7";// 件數
		sql += "                     , I.\"PerfAmt\" AS F8";// 業績金額
		sql += "                FROM \"PfItDetail\" I";
		sql += "                LEFT JOIN \"CdBcm\" B0 ON B0.\"UnitCode\" = I.\"DeptCode\" ";
		sql += "                LEFT JOIN \"CdEmp\" E0 ON E0.\"EmployeeNo\" = B0.\"DistManager\" ";
		sql += "                LEFT JOIN \"CdWorkMonth\" W ON W.\"Year\" = TRUNC(I.\"WorkMonth\" / 100 ) ";
		sql += "                                           AND  W.\"Month\" = MOD(I.\"WorkMonth\", 100) ";
		sql += "                UNION ALL ";
		sql += "                SELECT DISTINCT B0.\"DistItem\" AS F1";
		sql += "                              , B0.\"UnitItem\" AS F2";
		sql += "                              , E0.\"Fullname\" AS F3";
		sql += "                              , W.\"Year\" * 100 ";
		sql += "                              , W.\"Month\" AS F4";
		sql += "                              , W.\"StartDate\" AS F5";
		sql += "                              , W.\"EndDate\" AS F6";
		sql += "                              , 0 AS F7";
		sql += "                              , 0 AS F8";
		sql += "                FROM \"PfItDetail\" I CROSS JOIN \"CdWorkMonth\" W ";
		sql += "                LEFT JOIN \"CdBcm\" B0 ON B0.\"UnitCode\" = I.\"DeptCode\" ";
		sql += "                LEFT JOIN \"CdEmp\" E0 ON E0.\"EmployeeNo\" = B0.\"DistManager\" ) ";
		sql += "         GROUP BY \"F1\"";
		sql += "                , \"F2\"";
		sql += "                , \"F3\"";
		sql += "                , \"F4\"";
		sql += "                , \"F5\"";
		sql += "                , \"F6\"";
		sql += "         ORDER BY \"F1\"";
		sql += "                , \"F2\"";
		sql += "                , \"F3\"";
		sql += "                , \"F4\"";
		sql += "                , \"F5\"";
		sql += "                , \"F6\"";
		logger.info("sql1=" + sql1);
		Query query;
//		query = em.createQuery(sql, lH001Vo.class);
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(ContentName.onLine);
		query = em.createNativeQuery(sql);
		query = em.createNativeQuery(sql1);
		// 設定參數
//		query.setParameter("defno", defno);

		return this.convertToMap(query.getResultList());
	}

//	private List convertToTranScanResponsetest(List list) {
//
//		List<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
//		
//		try {
//
//			for (Iterator iter = list.iterator(); iter.hasNext();) {
//
//				Object[] values = (Object[]) iter.next();
//
//				HashMap<String, String> m = new HashMap<String, String>();
//
//				for (int i = 0; i < values.length; i++) {
//
//					m.put("f"+Integer.toString(i+1), values[i].toString());
//
//				}
//
//				logger.info(" m : " + m);
//				result.add(m);
//
//			}
//
//		} catch (Exception e) {
//
//			logger.error("Exception:" + e);
//
//		}
//
//		logger.info("result:" + result.size());
//
//		return result;
//	}
}