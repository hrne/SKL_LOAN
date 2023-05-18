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

@Service
@Repository
public class LP005ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	public List<Map<String, String>> findAll(TitaVo titaVo) {

		this.info("LP005ServiceImpl findAll ");

		String sql = " ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> queryCounts(int inputWorkMonth, TitaVo titaVo) {

		this.info("LP005ServiceImpl queryCounts inputWorkMonth = " + inputWorkMonth);

		String sql = " ";
		sql += " SELECT 5                       AS RewardType      "; // -- F0 獎金類別
		sql += "      , PR.\"CustNo\"           AS CustNo          "; // -- F1 戶號
		sql += "      , CM.\"CustName\"         AS CustName        "; // -- F2 戶名
		sql += "      , PR.\"AdjustBonus\"      AS CoorgnizerBonus "; // -- F3 車馬費發放額
		sql += "      , PR.\"EmployeeNo\"       AS Coorgnizer      "; // -- F4 介紹人
		sql += "      , \"Fn_GetEmpName\"(PR.\"EmployeeNo\",0) ";
		sql += "                                AS EmpName         "; // -- F5 員工姓名
		sql += "      , PCO.\"DeptItem\"        AS Dept            "; // -- F6 部室
		sql += "      , PCO.\"DistItem\"        AS Dist            "; // -- F7 區部
		sql += "      , PCO.\"AreaItem\"        AS Area            "; // -- F8 單位
		sql += " FROM \"PfRewardMedia\" PR ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = PR.\"CustNo\" ";
		sql += " LEFT JOIN \"PfCoOfficer\" PCO ON PCO.\"EmpNo\" = PR.\"EmployeeNo\" ";
		sql += "                              AND PCO.\"EffectiveDate\" = \"Fn_GetPfCoOfficeEffectiveDate\"(PR.\"EmployeeNo\",:inputWorkMonth) ";
		sql += " WHERE \"WorkMonth\" = :inputWorkMonth ";
		sql += "   AND \"BonusType\" in (5) "; // 協辦
		sql += "   AND \"AdjustBonus\" > 0 ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputWorkMonth", inputWorkMonth);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> queryAmt(int inputWorkMonth, TitaVo titaVo) {

		this.info("LP005ServiceImpl queryAmt inputWorkMonth = " + inputWorkMonth);

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
		sql += " SELECT PR.\"CustNo\"            AS CustNo      "; // -- F0 戶號
		sql += "      , PR.\"FacmNo\"            AS FacmNo      "; // -- F1 額度
		sql += "      , PR.\"UtilBal\"           AS DrawdownAmt "; // -- F2 撥款金額
		sql += "      , PR.\"PieceCode\"         AS PieceCode   "; // -- F3 計件代碼
		sql += "      , PR.\"EmployeeNo\"        AS Coorgnizer  "; // -- F4 員工代號
		sql += "      , \"Fn_GetEmpName\"(PR.\"EmployeeNo\",0) ";
		sql += "                                 AS EmpName     "; // -- F5 員工姓名
		sql += "      , PCO.\"DeptItem\"         AS Dept        "; // -- F6 部室
		sql += "      , PCO.\"DistItem\"         AS Dist        "; // -- F7 區部
		sql += "      , PCO.\"AreaItem\"         AS Area        "; // -- F8 單位
		sql += " FROM UTILBAL PR ";
		sql += " LEFT JOIN \"PfCoOfficer\" PCO ON PCO.\"EmpNo\" = PR.\"EmployeeNo\" ";
		sql += "                              AND PCO.\"EffectiveDate\" = \"Fn_GetPfCoOfficeEffectiveDate\"(PR.\"EmployeeNo\", :inputWorkMonth) ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputWorkMonth", inputWorkMonth);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> queryDept(int pfYear, int pfSeason, String inputDeptCode,int effectiveDateS,int effectiveDateE, TitaVo titaVo) {

		int inputWorkMonth1 = pfYear * 100 + (1 + (3 * (pfSeason - 1)));
		int inputWorkMonth2 = pfYear * 100 + (2 + (3 * (pfSeason - 1)));
		int inputWorkMonth3 = pfYear * 100 + (3 + (3 * (pfSeason - 1)));
		int inputWorkMonth4 = pfSeason == 4 ? pfYear * 100 + 13 : 0;
		int inputWorkSeason = pfYear * 10 + pfSeason;

		
		this.info("LP005ServiceImpl queryDept inputWorkMonth1 = " + inputWorkMonth1);
		this.info("LP005ServiceImpl queryDept inputWorkMonth2 = " + inputWorkMonth2);
		this.info("LP005ServiceImpl queryDept inputWorkMonth3 = " + inputWorkMonth3);
		this.info("LP005ServiceImpl queryDept inputWorkMonth4 = " + inputWorkMonth4);
		this.info("LP005ServiceImpl queryDept inputWorkSeason = " + inputWorkSeason);
		this.info("LP005ServiceImpl queryDept inputDeptCode = " + inputDeptCode);
		this.info("LP005ServiceImpl queryDept effectiveDateS = " + effectiveDateS);
		this.info("LP005ServiceImpl queryDept effectiveDateE = " + effectiveDateE);

		String sql = " ";
		sql += " WITH UTILBAL AS ( ";
		sql += "  SELECT PR.\"CustNo\" ";
		sql += "       , PR.\"FacmNo\" ";
		sql += "       , PR.\"EmployeeNo\" ";
		sql += "       , PR.\"PieceCode\"  "; 
		sql += "       , PR.\"ProdCode\"  "; 
		sql += "       , PR.\"WorkMonth\"  "; 
		sql += "       , SUM(NVL(PI.\"DrawdownAmt\",0)) AS \"UtilBal\" ";
		sql += "       , SUM(CNT)           AS  CNT "; 
		sql += "  FROM ";
		sql += "  ( SELECT \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += "          , \"BormNo\" ";
		sql += "          , \"EmployeeNo\" ";
		sql += "          , \"PieceCode\"  "; 
		sql += "          , \"ProdCode\"  "; 
		sql += "          , \"WorkMonth\"  "; 
		sql += "          , SUM(CNT)           AS  CNT "; 
		sql += "    FROM ( SELECT \"CustNo\" ";
		sql += "                 , \"FacmNo\" ";
		sql += "                 , \"BormNo\" ";
		sql += "                 , \"EmployeeNo\" ";
		sql += "                 , \"PieceCode\"  "; 
		sql += "                 , \"ProdCode\"  "; 
		sql += "                 , \"WorkMonth\"  "; 
		sql += "                 , 1           AS  CNT "; 
		sql += "           FROM \"PfRewardMedia\" a ";
		sql += "           WHERE \"WorkSeason\" = :inputWorkSeason ";
		sql += "             AND \"BonusType\" in (5) "; // 協辦
		sql += "             AND (\"AdjustBonus\" > 0  OR \"ManualFg\" = 1) ";
		sql += "           UNION ALL";
		sql += "           SELECT \"CustNo\" ";
		sql += "                 , \"FacmNo\" ";
		sql += "                 , \"BormNo\" ";
		sql += "                 , \"Coorgnizer\" AS \"EmployeeNo\" ";
		sql += "                 , \"PieceCode\"  "; 
		sql += "                 , \"ProdCode\"  "; 
		sql += "                 , \"WorkMonth\"  "; 
		sql += "                 , 0           AS  CNT "; 
		sql += "           FROM \"PfReward\" ";
		sql += "           WHERE \"WorkSeason\" = :inputWorkSeason ";
		sql += "         )  ";
		sql += "    GROUP BY \"CustNo\" ";
		sql += "           , \"FacmNo\" ";
		sql += "           , \"BormNo\" ";
		sql += "           , \"EmployeeNo\" ";
		sql += "           , \"PieceCode\"  "; 
		sql += "           , \"ProdCode\"  "; 
		sql += "           , \"WorkMonth\"  "; 
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
		sql += "         , PR.\"WorkMonth\" "; 
		sql += " ) ";
		sql += ", COOFFICER AS ( ";
		sql += "  SELECT \"EmpNo\"         ";
		sql += "       , \"DistItem\"      "; 
		sql += "       , \"AreaItem\"      ";  
		sql += "       , \"EmpClass\"      ";  
		sql += "       , ROW_NUMBER() OVER (Partition By \"EmpNo\"              ";
		sql += "    	                   	    ORDER BY \"EffectiveDate\" Desc      ";
		sql += "	                       ) AS ROWNUMBER                            ";
		sql += " FROM \"PfCoOfficer\" ";		
		sql += " WHERE \"DeptCode\" = :inputDeptCode ";
		sql += "   AND \"EffectiveDate\"  <= :effectiveDateE ";
		sql += "   AND \"IneffectiveDate\" >= :effectiveDateS ";
		sql += " ) ";
		sql += " SELECT PCO.\"DistItem\"         AS Dist        "; // -- F0 區部
		sql += "      , PCO.\"AreaItem\"         AS Area        "; // -- F1 單位
		sql += "      , \"Fn_GetEmpName\"(PCO.\"EmpNo\",0) ";
		sql += "                                 AS EmpName     "; // -- F2 員工姓名
		sql += "      , PCO.\"EmpNo\"            AS Coorgnizer  "; // -- F3 員工代號
		sql += "      , PCO.\"EmpClass\"         AS EmpClass    "; // -- F4 考核前職級
		sql += "      , PR.count1                AS count1      "; // -- F5 件數1
		sql += "      , TRUNC(PR.amt1 / 10000)   AS amt1        "; // -- F6 金額1
		sql += "      , PR.count2                AS count2      "; // -- F7 件數2
		sql += "      , TRUNC(PR.amt2 / 10000)   AS amt2        "; // -- F8 金額2
		sql += "      , PR.count3                AS count3      "; // -- F9 件數3
		sql += "      , TRUNC(PR.amt3 / 10000)   AS amt3        "; // -- F10 金額3
		sql += "      , PR.count4                AS count4      "; // -- F11 件數4
		sql += "      , TRUNC(PR.amt4 / 10000)   AS amt4        "; // -- F12 金額4
		sql += "      , PR.countTotal            AS countTotal  "; // -- F13 件數合計
		sql += "      , TRUNC(PR.amtTotal / 10000)  AS amtTotal    "; // -- F14 金額合計
		sql += " FROM COOFFICER PCO ";
		sql += " LEFT JOIN ( SELECT \"EmployeeNo\"                "; // -
		sql += "                  , SUM(CASE ";
		sql += "                          WHEN \"WorkMonth\" = :inputWorkMonth1 ";
		sql += "                          THEN CNT ";
		sql += "                        ELSE 0 END) AS count1     "; // -- 件數1
		sql += "                  , SUM(CASE  ";
		sql += "                          WHEN \"WorkMonth\" = :inputWorkMonth2 ";
		sql += "                          THEN CNT ";
		sql += "                        ELSE 0 END) AS count2     "; // -- 件數2
		sql += "                  , SUM(CASE  ";
		sql += "                          WHEN \"WorkMonth\" = :inputWorkMonth3 ";
		sql += "                          THEN CNT ";
		sql += "                        ELSE 0 END) AS count3     "; // -- 件數3
		sql += "                  , SUM(CASE  ";
		sql += "                          WHEN \"WorkMonth\" = :inputWorkMonth4 ";
		sql += "                          THEN CNT ";
		sql += "                        ELSE 0 END) AS count4     "; // -- 件數4
		sql += "                  , SUM(1)          AS countTotal "; // -- 件數合計
		sql += "                  , SUM(CASE ";
		sql += "                          WHEN \"WorkMonth\" = :inputWorkMonth1 ";
		sql += "                          THEN \"UtilBal\" ";
		sql += "                        ELSE 0 END )         AS amt1 "; // -- 金額1
		sql += "                  , SUM(CASE ";
		sql += "                          WHEN \"WorkMonth\" = :inputWorkMonth2 ";
		sql += "                          THEN \"UtilBal\" ";
		sql += "                        ELSE 0 END )         AS amt2 "; // -- 金額2
		sql += "                  , SUM(CASE ";
		sql += "                          WHEN \"WorkMonth\" = :inputWorkMonth3 ";
		sql += "                          THEN \"UtilBal\" ";
		sql += "                        ELSE 0 END )         AS amt3 "; // -- 金額3
		sql += "                  , SUM(CASE ";
		sql += "                          WHEN \"WorkMonth\" = :inputWorkMonth4 ";
		sql += "                          THEN \"UtilBal\" ";
		sql += "                        ELSE 0 END )         AS amt4 "; // -- 金額4
		sql += "                  , SUM(\"UtilBal\")  AS amtTotal "; // -- 金額合計
		sql += "             FROM UTILBAL ";
		sql += "             GROUP BY \"EmployeeNo\" ";
		sql += "           ) PR ON PR.\"EmployeeNo\" = PCO.\"EmpNo\" ";
		sql += " WHERE PCO.ROWNUMBER = 1 ";
		sql += " ORDER BY Dist ";
		sql += "        , Area ";
		sql += "        , Coorgnizer ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputWorkMonth1", inputWorkMonth1);
		query.setParameter("inputWorkMonth2", inputWorkMonth2);
		query.setParameter("inputWorkMonth3", inputWorkMonth3);
		query.setParameter("inputWorkMonth4", inputWorkMonth4);
		query.setParameter("inputWorkSeason", inputWorkSeason);
		query.setParameter("inputDeptCode", inputDeptCode);
		query.setParameter("effectiveDateS", effectiveDateS);
		query.setParameter("effectiveDateE", effectiveDateE);

		return this.convertToMap(query);
	}
}