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

	private String sqlRow = "OFFSET :ThisIndex * :ThisLimit ROWS FETCH NEXT :ThisLimit ROW ONLY ";

	public List<Map<String, String>> findData(TitaVo titaVo) throws LogicException {
		int workMonth = parse.stringToInteger(titaVo.getParam("Ym")) + 191100;

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		String sql = " ";
		sql += " SELECT a.\"Coorgnizer\" ";
		sql += "      , b.\"Fullname\" ";
		sql += "      , a.\"CustNo\" ";
		sql += "      , a.\"FacmNo\" ";
		sql += "      , a.\"BormNo\" ";
		sql += "      , a.\"DrawdownAmt\" ";
		sql += "      , a.\"ComputeCoBonusAmt\" ";
		sql += "      , a.\"CoorgnizerBonus\" ";
		sql += " FROM \"PfDetail\" a ";
		sql += " LEFT JOIN \"CdEmp\" b ON b.\"EmployeeNo\" = a.\"Coorgnizer\" ";
		sql += " WHERE a.\"WorkMonth\" = :workmonth ";
		sql += "   AND a.\"Coorgnizer\" IS NOT NULL ";
		sql += "   AND a.\"ComputeCoBonusAmt\" > 0 ";
		sql += "   AND a.\"RepayType\" = 0 ";
		sql += " ORDER BY a.\"Coorgnizer\" ";
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
		// 協辦人員業績件數的檔案要產出不只有協辦人員業績件數的檔案
		String sql = " ";
		sql += " WITH rawData AS ( ";
		sql += "     SELECT L.\"CustNo\" ";
		sql += "          , L.\"FacmNo\" ";
		sql += "          , SUM(L.\"DrawdownAmt\") AS \"UtilBal\" ";
		sql += "     FROM \"PfItDetail\" L ";
		sql += "     WHERE L.\"RepayType\" = 0 ";
		sql += "       AND L.\"WorkMonth\" = :inputWorkMonth ";
		sql += "     GROUP BY L.\"CustNo\" ";
		sql += "            , L.\"FacmNo\" ";
		sql += " ) ";
		sql += "SELECT * FROM ( ";	
		sql += " SELECT 5                       AS \"RewardType\" "; // 獎金類別
		sql += "      , PR.\"PieceCode\"        AS \"PieceCode\" "; // 計件代碼
		sql += "      , 0                       AS \"DrawdownAmt\" "; // 撥款金額
		sql += "      , PR.\"CoorgnizerBonusDate\" ";
		sql += "                                AS \"BonusDate\" "; // 車馬費發放日期
		sql += "      , PR.\"CustNo\"           AS \"CustNo\" "; // 戶號
		sql += "      , CM.\"CustName\"         AS \"CustName\" "; // 戶名
		sql += "      , PR.\"FacmNo\"           AS \"FacmNo\" "; // 額度號碼
		sql += "      , PR.\"BormNo\"           AS \"BormNo\" "; // 撥款序號
		sql += "      , CM.\"CustId\"           AS \"CustId\" "; // 統一編號
		sql += "      , rd.\"UtilBal\"          AS \"UtilBal\" "; // 已用額度
		sql += "      , PR.\"CoorgnizerBonus\"  AS \"Bonus\" "; // 車馬費發放額
		sql += "      , PR.\"Coorgnizer\"       AS \"EmpNo\" "; // 介紹人
		sql += "      , \"Fn_GetEmpName\"(PR.\"Coorgnizer\",0) ";
		sql += "                                AS \"EmpName\" "; // 員工姓名
		sql += "      , PCO.\"DeptItem\"        AS \"Dept\" "; // 部室
		sql += "      , PCO.\"DistItem\"        AS \"Dist\" "; // 區部
		sql += "      , PCO.\"AreaItem\"        AS \"Unit\" "; // 單位
		sql += " FROM \"PfReward\" PR ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = PR.\"CustNo\" ";
		sql += " LEFT JOIN rawData rd ON rd.\"CustNo\" = PR.\"CustNo\" ";
		sql += "                     AND rd.\"FacmNo\" = PR.\"FacmNo\" ";
		sql += " LEFT JOIN \"PfCoOfficer\" PCO ON PCO.\"EmpNo\" = PR.\"Coorgnizer\" ";
		sql += "                              AND TRUNC(PCO.\"EffectiveDate\" / 100) <= :inputWorkMonth ";
		sql += "                              AND TRUNC(PCO.\"IneffectiveDate\" / 100) >= :inputWorkMonth ";
		sql += " WHERE PR.\"CoorgnizerBonus\" > 0 ";
		sql += "   AND PR.\"WorkMonth\" = :inputWorkMonth ";
		sql += " UNION ALL";
		sql += " SELECT 1                       AS \"RewardType\" "; // 獎金類別
		sql += "      , PR.\"PieceCode\"        AS \"PieceCode\" "; // 計件代碼
		sql += "      , PI.\"DrawdownAmt\"      AS \"DrawdownAmt\" "; // 撥款金額
		sql += "      , PR.\"IntroducerBonusDate\"                 ";
		sql += "                                AS \"BonusDate\" "; // 車馬費發放日期
		sql += "      , PR.\"CustNo\"           AS \"CustNo\" "; // 戶號
		sql += "      , CM.\"CustName\"         AS \"CustName\" "; // 戶名
		sql += "      , PR.\"FacmNo\"           AS \"FacmNo\" "; // 額度號碼
		sql += "      , PR.\"BormNo\"           AS \"BormNo\" "; // 撥款序號
		sql += "      , CM.\"CustId\"           AS \"CustId\" "; // 統一編號
		sql += "      , rd.\"UtilBal\"          AS \"UtilBal\" "; // 已用額度
		sql += "      , PR.\"IntroducerBonus\"  AS \"Bonus\" "; // 車馬費發放額
		sql += "      , PR.\"Introducer\"       AS \"EmpNo\" "; // 介紹人
		sql += "      , \"Fn_GetEmpName\"(PR.\"Introducer\",0) ";
		sql += "                                AS \"EmpName\" "; // 員工姓名
		sql += "      , B1.\"UnitItem\"         AS \"Dept\" "; // 部室
		sql += "      , B2.\"UnitItem\"         AS \"Dist\" "; // 區部
		sql += "      , B3.\"UnitItem\"         AS \"Unit\" "; // 單位
		sql += " FROM \"PfReward\" PR ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = PR.\"CustNo\" ";
		sql += " LEFT JOIN rawData rd ON rd.\"CustNo\" = PR.\"CustNo\" ";
		sql += "                     AND rd.\"FacmNo\" = PR.\"FacmNo\" ";
		sql += " LEFT JOIN \"PfItDetail\" PI ON PI.\"CustNo\" = PR.\"CustNo\" ";
		sql += "                            AND PI.\"FacmNo\" = PR.\"FacmNo\" ";
		sql += "                            AND PI.\"BormNo\" = PR.\"BormNo\" ";
		sql += "                            AND PI.\"RepayType\" = 0 ";
		sql += " LEFT JOIN \"CdBcm\" B1 ON B1.\"UnitCode\" = PI.\"DeptCode\" "; // 部室
		sql += " LEFT JOIN \"CdBcm\" B2 ON B2.\"UnitCode\" = PI.\"DistCode\" "; // 部室
		sql += " LEFT JOIN \"CdBcm\" B3 ON B3.\"UnitCode\" = PI.\"UnitCode\" "; // 部室
		sql += " WHERE PR.\"IntroducerBonusDate\" > 0 ";
		sql += "   AND PR.\"WorkMonth\" = :inputWorkMonth ";
		sql += ") ";	
		sql += " ORDER BY SUBSTR(\"EmpNo\",1,1) ";
		sql += "        , CASE WHEN SUBSTR(\"EmpNo\",2,1) BETWEEN '0' AND '9' THEN 'B' ELSE 'A' END "; //數字排後面
		sql += "        , SUBSTR(\"EmpNo\",3,4) " ;
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
		sql += "SELECT * FROM ( ";	
		sql += " SELECT PD.\"CustNo\"            AS \"CustNo\" "; // -- F0 戶號
		sql += "      , PD.\"FacmNo\"            AS \"FacmNo\" "; // -- F1 額度
		sql += "      , SUM(PD.\"DrawdownAmt\")  AS \"DrawdownAmt\" "; // -- F2 撥款金額
		sql += "      , PD.\"PieceCode\"         AS \"PieceCode\" "; // -- F3 計件代碼
		sql += "      , NVL(PD.\"Coorgnizer\",' ') ";
		sql += "                                 AS \"EmpNo\"  "; // -- F4 員工代號
		sql += "      , \"Fn_GetEmpName\"(PD.\"Coorgnizer\",0) ";
		sql += "                                 AS \"EmpName\"     "; // -- F5 員工姓名
		sql += "      , NVL(PCO.\"DeptItem\", ' ') ";
		sql += "                                 AS \"Dept\"        "; // -- F6 部室
		sql += "      , NVL(PCO.\"DistItem\", ' ') ";
		sql += "                                 AS \"Dist\"        "; // -- F7 區部
		sql += "      , NVL(PCO.\"AreaItem\", ' ') ";
		sql += "                                 AS \"Unit\"        "; // -- F8 單位
		sql += " FROM \"PfDetail\" PD ";
		sql += " LEFT JOIN \"PfCoOfficer\" PCO ON PCO.\"EmpNo\" = PD.\"Coorgnizer\" ";
		sql += "                              AND TRUNC(PCO.\"EffectiveDate\" / 100) <= :inputWorkMonth ";
		sql += "                              AND TRUNC(PCO.\"IneffectiveDate\" / 100) >= :inputWorkMonth ";
		sql += " WHERE PD.\"PieceCode\" IN ('1','2','A','B','8','9') ";
		sql += "   AND PD.\"ProdCode\" NOT IN ('TB') ";
		sql += "   AND PD.\"DrawdownAmt\" > 0 ";
		sql += "   AND PD.\"WorkMonth\" = :inputWorkMonth ";
		sql += " GROUP BY PD.\"CustNo\" ";
		sql += "        , PD.\"FacmNo\" ";
		sql += "        , PD.\"PieceCode\" ";
		sql += "        , NVL(PD.\"Coorgnizer\",' ')  ";
		sql += "        , \"Fn_GetEmpName\"(PD.\"Coorgnizer\",0) ";
		sql += "        , NVL(PCO.\"DeptItem\", ' ') ";
		sql += "        , NVL(PCO.\"DistItem\", ' ') ";
		sql += "        , NVL(PCO.\"AreaItem\", ' ') ";
		sql += ") ";	
		sql += " ORDER BY CASE ";
		sql += "            WHEN \"EmpNo\" = ' ' ";
		sql += "            THEN 1 ";
		sql += "          ELSE 0 END ";
		sql += "        , SUBSTR(\"EmpNo\",1,1) ";
		sql += "        , CASE WHEN SUBSTR(\"EmpNo\",2,1) BETWEEN '0' AND '9' THEN 'B' ELSE 'A' END "; //數字排後面
		sql += "        , SUBSTR(\"EmpNo\",3,4) " ;
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
