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

@Service("l5915ServiceImpl")
public class L5915ServiceImpl extends ASpringJpaParm implements InitializingBean {
	@Autowired
	private Parse parse;

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	// *** 折返控制相關 ***
	private int limit;

	public List<Map<String, String>> findData(TitaVo titaVo) throws LogicException {
		int workMonth = parse.stringToInteger(titaVo.getParam("Ym")) + 191100;

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		String sql = " ";
		sql += " SELECT a.\"EmployeeNo\" AS \"Coorgnizer\" ";
		sql += "      , b.\"Fullname\" ";
		sql += "      , a.\"CustNo\" ";
		sql += "      , a.\"FacmNo\" ";
		sql += "      , a.\"BormNo\" ";
		sql += "      , NVL(p.\"DrawdownAmt\",0) AS \"DrawdownAmt\" ";
		sql += "      , a.\"Bonus\" ";
		sql += "      , a.\"AdjustBonus\"  AS \"CoorgnizerBonus\" ";
		sql += " FROM \"PfRewardMedia\" a ";
		sql += " LEFT JOIN \"PfItDetail\" p ON p.\"CustNo\" = a.\"CustNo\" ";
		sql += "                            AND p.\"FacmNo\" = a.\"FacmNo\" ";
		sql += "                            AND p.\"BormNo\" = a.\"BormNo\" ";
		sql += "                            AND p.\"RepayType\" = 0 ";
		sql += " LEFT JOIN \"CdEmp\" b ON b.\"EmployeeNo\" = a.\"EmployeeNo\" ";
		sql += " WHERE a.\"WorkMonth\" = :workmonth ";
		sql += "   AND a.\"EmployeeNo\" IS NOT NULL ";
		sql += "   AND a.\"AdjustBonus\" > 0 ";
		sql += "   AND a.\"BonusType\" = 5 ";
		sql += " ORDER BY a.\"EmployeeNo\" ";
		sql += "        , a.\"CustNo\" ";
		sql += "        , a.\"FacmNo\" ";
		sql += "        , a.\"BormNo\" ";
		this.info("sql = " + sql);

		query = em.createNativeQuery(sql);

		query.setParameter("workmonth", workMonth);

		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可
		query.setMaxResults(this.limit);
//		this.info("L5915Service FindData=" + query.toString());
		return this.convertToMap(query);
	}

	public List<Map<String, String>> queryCounts(int inputWorkMonth, TitaVo titaVo) {

		this.info("L5915ServiceImpl queryCounts inputWorkMonth = " + inputWorkMonth);

		// 2022-08-23 ST1-智偉修改
		// SKL User 李珮君 要求跟AS400產一樣的檔案
		// 協辦人員業績件數的檔案要產出含協辦人員及介紹人業績件數的檔案
		String sql = " ";
		sql += " WITH UTILBAL AS ( ";
		sql += "  SELECT PR.\"CustNo\" ";
		sql += "       , PR.\"FacmNo\" ";
		sql += "       , SUM(NVL(PI.\"DrawdownAmt\",0)) AS \"UtilBal\" ";
		sql += "  FROM ";
		sql += "  ( SELECT \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += "          , \"BormNo\" ";
		sql += "    FROM ( SELECT \"CustNo\" ";
		sql += "                 , \"FacmNo\" ";
		sql += "                 , \"BormNo\" ";
		sql += "           FROM \"PfRewardMedia\" a ";
		sql += "           WHERE \"WorkMonth\" = :inputWorkMonth ";
		sql += "             AND \"BonusType\" in (1,5) "; // 介紹及協辦
		sql += "             AND \"AdjustBonus\" > 0 ";
		sql += "           UNION ALL";
		sql += "           SELECT \"CustNo\" ";
		sql += "                , \"FacmNo\" ";
		sql += "                , \"BormNo\" ";
		sql += "           FROM \"PfReward\" ";
		sql += "           WHERE \"WorkMonth\" = :inputWorkMonth ";
		sql += "         )  ";
		sql += "    GROUP BY \"CustNo\" ";
		sql += "           , \"FacmNo\" ";
		sql += "           , \"BormNo\" ";
		sql += "  ) PR ";
		sql += "  LEFT JOIN \"PfItDetail\" PI ON PI.\"CustNo\" = PR.\"CustNo\" ";
		sql += "                             AND PI.\"FacmNo\" = PR.\"FacmNo\" ";
		sql += "                             AND PI.\"BormNo\" = PR.\"BormNo\" ";
		sql += "                             AND PI.\"RepayType\" = 0 ";
		sql += "  GROUP BY PR.\"CustNo\" ";
		sql += "         , PR.\"FacmNo\" ";
		sql += " ) ";
		sql += "SELECT * FROM ( ";
		sql += " SELECT 5                       AS \"RewardType\" "; // 獎金類別
		sql += "      , PR.\"PieceCode\"        AS \"PieceCode\" "; // 計件代碼
		sql += "      , 0                       AS \"DrawdownAmt\" "; // 撥款金額
		sql += "      , PR.\"CustNo\"           AS \"CustNo\" "; // 戶號
		sql += "      , CM.\"CustName\"         AS \"CustName\" "; // 戶名
		sql += "      , PR.\"FacmNo\"           AS \"FacmNo\" "; // 額度號碼
		sql += "      , PR.\"BormNo\"           AS \"BormNo\" "; // 撥款序號
		sql += "      , CM.\"CustId\"           AS \"CustId\" "; // 統一編號
		sql += "      , UT.\"UtilBal\"          AS \"UtilBal\" "; // 已用額度
		sql += "      , PR.\"AdjustBonus\"      AS \"Bonus\" "; // 車馬費發放額
		sql += "      , PR.\"EmployeeNo\"       AS \"EmpNo\" "; // 介紹人
		sql += "      , \"Fn_GetEmpName\"(PR.\"EmployeeNo\",0) ";
		sql += "                                AS \"EmpName\" "; // 員工姓名
		sql += "      , PCO.\"DeptItem\"        AS \"Dept\" "; // 部室
		sql += "      , PCO.\"DistItem\"        AS \"Dist\" "; // 區部
		sql += "      , PCO.\"AreaItem\"        AS \"Unit\" "; // 單位
		sql += " FROM \"PfRewardMedia\" PR ";
		sql += " LEFT JOIN UTILBAL UT ON UT.\"CustNo\" = PR.\"CustNo\" ";
		sql += "                     AND UT.\"FacmNo\" = PR.\"FacmNo\" ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = PR.\"CustNo\" ";
		sql += " LEFT JOIN \"PfCoOfficer\" PCO ON PCO.\"EmpNo\" = PR.\"EmployeeNo\" ";
		sql += "                              AND PCO.\"EffectiveDate\" = \"Fn_GetPfCoOfficeEffectiveDate\"(PR.\"EmployeeNo\",PR.\"WorkMonth\") ";
		sql += " WHERE PR.\"AdjustBonus\" > 0 ";
		sql += "   AND PR.\"BonusType\" in (5) ";
		sql += "   AND PR.\"WorkMonth\" = :inputWorkMonth ";
		sql += " UNION ALL";
		sql += " SELECT 1                       AS \"RewardType\" "; // 獎金類別
		sql += "      , PR.\"PieceCode\"        AS \"PieceCode\" "; // 計件代碼
		sql += "      , PI.\"DrawdownAmt\"      AS \"DrawdownAmt\" "; // 撥款金額
		sql += "      , PR.\"CustNo\"           AS \"CustNo\" "; // 戶號
		sql += "      , CM.\"CustName\"         AS \"CustName\" "; // 戶名
		sql += "      , PR.\"FacmNo\"           AS \"FacmNo\" "; // 額度號碼
		sql += "      , PR.\"BormNo\"           AS \"BormNo\" "; // 撥款序號
		sql += "      , CM.\"CustId\"           AS \"CustId\" "; // 統一編號
		sql += "      , UT.\"UtilBal\"          AS \"UtilBal\" "; // 已用額度
		sql += "      , PR.\"AdjustBonus\"      AS \"Bonus\" "; // 車馬費發放額
		sql += "      , PR.\"EmployeeNo\"       AS \"EmpNo\" "; // 介紹人
		sql += "      , \"Fn_GetEmpName\"(PR.\"EmployeeNo\",0) ";
		sql += "                                AS \"EmpName\" "; // 員工姓名
		sql += "      , B1.\"UnitItem\"         AS \"Dept\" "; // 部室
		sql += "      , B2.\"UnitItem\"         AS \"Dist\" "; // 區部
		sql += "      , B3.\"UnitItem\"         AS \"Unit\" "; // 單位
		sql += " FROM \"PfRewardMedia\" PR ";
		sql += " LEFT JOIN UTILBAL UT ON UT.\"CustNo\" = PR.\"CustNo\" ";
		sql += "                     AND UT.\"FacmNo\" = PR.\"FacmNo\" ";
		sql += " LEFT JOIN \"PfItDetail\" PI ON PI.\"CustNo\" = PR.\"CustNo\" ";
		sql += "                            AND PI.\"FacmNo\" = PR.\"FacmNo\" ";
		sql += "                            AND PI.\"BormNo\" = PR.\"BormNo\" ";
		sql += "                            AND PI.\"RepayType\" = 0 ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = PR.\"CustNo\" ";
		sql += " LEFT JOIN \"CdBcm\" B1 ON B1.\"UnitCode\" = PI.\"DeptCode\" "; // 部室
		sql += " LEFT JOIN \"CdBcm\" B2 ON B2.\"UnitCode\" = PI.\"DistCode\" "; // 部室
		sql += " LEFT JOIN \"CdBcm\" B3 ON B3.\"UnitCode\" = PI.\"UnitCode\" "; // 部室
		sql += " WHERE PR.\"BonusType\" in (1) ";
		sql += "   AND PR.\"AdjustBonus\" > 0 ";
		sql += "   AND PR.\"WorkMonth\" = :inputWorkMonth ";
		sql += ") ";
		sql += " ORDER BY SUBSTR(\"EmpNo\",1,1) ";
		sql += "        , CASE WHEN SUBSTR(\"EmpNo\",2,1) BETWEEN '0' AND '9' THEN 'B' ELSE 'A' END "; // 數字排後面
		sql += "        , SUBSTR(\"EmpNo\",3,4) ";
		sql += "        , \"CustNo\" ";
		sql += "        , \"FacmNo\" ";
		sql += "        , \"BormNo\" ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputWorkMonth", inputWorkMonth);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> queryAmt(int inputWorkMonth, TitaVo titaVo) {

		this.info("L5915ServiceImpl queryAmt inputWorkMonth = " + inputWorkMonth);

		// 2022-08-23 ST1-智偉修改
		// SKL User 李珮君 要求跟AS400產一樣的檔案
		// 協辦人員業績金額的檔案要產出不只有協辦人員業績金額的檔案
		String sql = " ";
		sql += " WITH UTILBAL AS ( ";
		sql += "  SELECT PR.\"CustNo\" ";
		sql += "       , PR.\"FacmNo\" ";
		sql += "       , PR.\"EmployeeNo\" ";
		sql += "       , PR.\"PieceCode\"  "; 
		sql += "       , PR.\"ProdCode\"  "; 
		sql += "       , SUM(NVL(PI.\"DrawdownAmt\",0)) AS \"UtilBal\" ";
		sql += "  FROM ";
		sql += "  ( SELECT \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += "          , \"BormNo\" ";
		sql += "          , \"EmployeeNo\" ";
		sql += "          , \"PieceCode\"  "; 
		sql += "          , \"ProdCode\"  "; 
		sql += "    FROM ( SELECT \"CustNo\" ";
		sql += "                 , \"FacmNo\" ";
		sql += "                 , \"BormNo\" ";
		sql += "                 , \"EmployeeNo\" ";
		sql += "                 , \"PieceCode\"  "; 
		sql += "                 , \"ProdCode\"  "; 
		sql += "           FROM \"PfRewardMedia\" a ";
		sql += "           WHERE \"WorkMonth\" = :inputWorkMonth ";
		sql += "             AND \"BonusType\" in (5) "; // 協辦
		sql += "             AND (\"AdjustBonus\" > 0  OR \"ManualFg\" = 1) ";
		sql += "           UNION ALL";
		sql += "           SELECT \"CustNo\" ";
		sql += "                 , \"FacmNo\" ";
		sql += "                 , \"BormNo\" ";
		sql += "                 , \"Coorgnizer\" AS \"EmployeeNo\" ";
		sql += "                 , \"PieceCode\"  "; 
		sql += "                 , \"ProdCode\"  "; 
		sql += "           FROM \"PfReward\" ";
		sql += "           WHERE \"WorkMonth\" = :inputWorkMonth ";
		sql += "         )  ";
		sql += "    GROUP BY \"CustNo\" ";
		sql += "           , \"FacmNo\" ";
		sql += "           , \"BormNo\" ";
		sql += "           , \"EmployeeNo\" ";
		sql += "           , \"PieceCode\"  "; 
		sql += "           , \"ProdCode\"  "; 
		sql += "  ) PR ";
		sql += "  LEFT JOIN \"PfItDetail\" PI ON PI.\"CustNo\" = PR.\"CustNo\" ";
		sql += "                             AND PI.\"FacmNo\" = PR.\"FacmNo\" ";
		sql += "                             AND PI.\"BormNo\" = PR.\"BormNo\" ";
		sql += "                             AND PI.\"RepayType\" = 0 ";
		sql += "  WHERE PR.\"PieceCode\" IN ('1','2','A','B','8','9') ";
		sql += "    AND PR.\"ProdCode\" NOT IN ('TB') ";
		sql += "  GROUP BY PR.\"CustNo\" ";
		sql += "         , PR.\"FacmNo\" ";
		sql += "         , PR.\"EmployeeNo\" ";
		sql += "         , PR.\"PieceCode\"  "; 
		sql += "         , PR.\"ProdCode\"  "; 
		sql += " ) ";
		sql += "SELECT * FROM ( ";
		sql += " SELECT PR.\"CustNo\"            AS \"CustNo\" "; // -- F0 戶號
		sql += "      , PR.\"FacmNo\"            AS \"FacmNo\" "; // -- F1 額度
		sql += "      , PR.\"UtilBal\"           AS \"DrawdownAmt\" "; // -- F2 撥款金額
		sql += "      , PR.\"PieceCode\"         AS \"PieceCode\" "; // -- F3 計件代碼
		sql += "      , NVL(PR.\"EmployeeNo\",' ') ";
		sql += "                                 AS \"EmpNo\"  "; // -- F4 員工代號
		sql += "      , \"Fn_GetEmpName\"(PR.\"EmployeeNo\",0) ";
		sql += "                                 AS \"EmpName\"     "; // -- F5 員工姓名
		sql += "      , NVL(PCO.\"DeptItem\", ' ') ";
		sql += "                                 AS \"Dept\"        "; // -- F6 部室
		sql += "      , NVL(PCO.\"DistItem\", ' ') ";
		sql += "                                 AS \"Dist\"        "; // -- F7 區部
		sql += "      , NVL(PCO.\"AreaItem\", ' ') ";
		sql += "                                 AS \"Unit\"        "; // -- F8 單位
		sql += " FROM UTILBAL PR ";
		sql += " LEFT JOIN \"PfCoOfficer\" PCO ON PCO.\"EmpNo\" = PR.\"EmployeeNo\" ";
		sql += "                              AND PCO.\"EffectiveDate\" = \"Fn_GetPfCoOfficeEffectiveDate\"(PR.\"EmployeeNo\",:inputWorkMonth) ";
		sql += ") ";
		sql += " ORDER BY CASE ";
		sql += "            WHEN \"EmpNo\" = ' ' ";
		sql += "            THEN 1 ";
		sql += "          ELSE 0 END ";
		sql += "        , SUBSTR(\"EmpNo\",1,1) ";
		sql += "        , CASE WHEN SUBSTR(\"EmpNo\",2,1) BETWEEN '0' AND '9' THEN 'B' ELSE 'A' END "; // 數字排後面
		sql += "        , SUBSTR(\"EmpNo\",3,4) ";
		sql += "        , \"CustNo\" ";
		sql += "        , \"FacmNo\" ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputWorkMonth", inputWorkMonth);

		return this.convertToMap(query);
	}
}
