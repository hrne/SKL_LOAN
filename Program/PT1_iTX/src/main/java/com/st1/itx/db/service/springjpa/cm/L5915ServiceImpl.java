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
		sql += "      , PR.\"BonusDate\"        AS \"BonusDate\" "; // 車馬費發放日期
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
		sql += "      , PR.\"BonusDate\"        AS \"BonusDate\" "; // 車馬費發放日期
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
		sql += " WITH UTIL AS (                                               ";
		sql += "  SELECT                                                      ";
		sql += "            \"CustNo\"                          AS \"CustNo\",    ";
		sql += "            \"FacmNo\"                          AS \"FacmNo\",    ";
		sql += "            \"BormNo\"                          AS \"BormNo\",    ";
		sql += "            \"PieceCode\"                       AS \"PieceCode\", ";
		sql += "            \"EmployeeNo\"                      AS \"Coorgnizer\" ";
		sql += "   FROM     \"PfRewardMedia\"                                   ";
		sql += "   WHERE    \"BonusType\" in (1,5)                              ";
		sql += "    AND     \"WorkMonth\" = :inputWorkMonth                     ";
		sql += "    AND     \"AdjustBonus\" > 0                                 ";
		sql += "    AND     \"ManualFg\" = 1                                    ";
		sql += "    union                                                       ";
		sql += "  SELECT                                                      ";
		sql += "            pi.\"CustNo\"                       AS \"CustNo\",    ";
		sql += "            pi.\"FacmNo\"                       AS \"FacmNo\",    ";
		sql += "            pi.\"BormNo\"                       AS \"BormNo\",    ";
		sql += "            pi.\"PieceCode\"                    AS \"PieceCode\", ";
		sql += "            nvl(pr.\"Coorgnizer\", ' ')         AS \"EmpNo\"      ";
		sql += "   FROM \"PfItDetail\" pi                                       ";
		sql += "   LEFT JOIN \"PfReward\"     pr ON pr.\"CustNo\" = pi.\"CustNo\"   ";
		sql += "                              AND pr.\"FacmNo\" = pi.\"FacmNo\"   ";
		sql += "                              AND pr.\"BormNo\" = pi.\"BormNo\"   ";
		sql += "                              AND pr.\"RepayType\" = 0          ";
		sql += "   WHERE                                                      ";
		sql += "            pi.\"WorkMonth\" = :inputWorkMonth                  ";
		sql += "  )                                                           ";
		sql += " SELECT *                                                     ";
		sql += " FROM                                                         ";
		sql += "     (                                                        ";
		sql += "        SELECT                                                ";
		sql += "             pi.\"CustNo\"                                AS \"CustNo\",      ";
		sql += "             pi.\"FacmNo\"                                AS \"FacmNo\",      ";
		sql += "             SUM(pi.\"DrawdownAmt\")                      AS \"DrawdownAmt\", ";
		sql += "             pi.\"PieceCode\"                             AS \"PieceCode\",   ";
		sql += "             nvl(pr.\"Coorgnizer\", ' ')                  AS \"EmpNo\",       ";
		sql += "             \"Fn_GetEmpName\"(pr.\"Coorgnizer\", 0)        AS \"EmpName\",     ";
		sql += "             nvl(pco.\"DeptItem\", ' ')                   AS \"Dept\",        ";
		sql += "             nvl(pco.\"DistItem\", ' ')                   AS \"Dist\",        ";
		sql += "             nvl(pco.\"AreaItem\", ' ')                   AS \"Unit\"         ";
 		sql += "        FROM UTIL pr                                                      ";
		sql += "             LEFT JOIN \"PfItDetail\"   pi on pi.\"CustNo\" = pr.\"CustNo\"     ";
		sql += "                                      and pi.\"FacmNo\" = pr.\"FacmNo\"       ";
		sql += "                                      and pi.\"BormNo\" = pr.\"BormNo\"       ";
		sql += "                                      and pi.\"RepayType\" = 0              ";                         
		sql += "             LEFT JOIN \"PfCoOfficer\"  pco ON pco.\"EmpNo\" = pr.\"Coorgnizer\" ";
		sql += "                                         AND pco.\"EffectiveDate\" = \"Fn_GetPfCoOfficeEffectiveDate\"(pr.\"Coorgnizer\", :inputWorkMonth) ";
		sql += "        WHERE                                                             ";
		sql += "             nvl(pi.\"CustNo\",0) > 0                                       ";
		sql += "            and pr.\"PieceCode\" IN ('1','2','A','B','8','9')               ";
		sql += "            AND pi.\"ProdCode\" NOT IN ('TB')                               ";
		sql += "            AND pi.\"RepayType\" = 0                                        ";
		sql += "        GROUP BY                                                            ";
		sql += "            pi.\"CustNo\",                                                  ";
		sql += "            pi.\"FacmNo\",                                                  ";
		sql += "            pi.\"PieceCode\",                                               ";
		sql += "            nvl(pr.\"Coorgnizer\", ' '),                                    ";
		sql += "            \"Fn_GetEmpName\"(pr.\"Coorgnizer\", 0),                        ";
		sql += "            nvl(pco.\"DeptItem\", ' '),                                     ";
		sql += "            nvl(pco.\"DistItem\", ' '),                                     ";
		sql += "            nvl(pco.\"AreaItem\", ' ')                                      ";
		sql += "    )                                                                       ";
		sql += " ORDER BY                                                                   ";
		sql += "    CASE                                                                    ";
		sql += "        WHEN \"EmpNo\" = ' ' THEN    1                                      ";
		sql += "        ELSE            0                                                   ";
		sql += "    END,                                                                    ";
		sql += "    substr(\"EmpNo\", 1, 1),                                                ";
		sql += "    CASE                                                                    ";
		sql += "            WHEN substr(\"EmpNo\", 2, 1) BETWEEN '0' AND '9' THEN 'B'       ";
		sql += "            ELSE        'A'                                                 ";
		sql += "        END,                                                                ";
		sql += "    substr(\"EmpNo\", 3, 4),                                                ";
		sql += "    \"CustNo\",                                                             ";
		sql += "    \"FacmNo\"                                                              ";	
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputWorkMonth", inputWorkMonth);

		return this.convertToMap(query);
	}
}
