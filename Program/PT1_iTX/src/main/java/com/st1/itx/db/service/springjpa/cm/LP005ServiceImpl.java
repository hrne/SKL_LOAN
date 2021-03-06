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
		sql += "      , PR.\"CoorgnizerBonus\"  AS CoorgnizerBonus "; // -- F3 車馬費發放額
		sql += "      , PR.\"Coorgnizer\"       AS Coorgnizer      "; // -- F4 介紹人
		sql += "      , \"Fn_GetEmpName\"(PR.\"Coorgnizer\",0) ";
		sql += "                                AS EmpName         "; // -- F5 員工姓名
		sql += "      , PCO.\"DeptItem\"        AS Dept            "; // -- F6 部室
		sql += "      , PCO.\"DistItem\"        AS Dist            "; // -- F7 區部
		sql += "      , PCO.\"AreaItem\"        AS Area            "; // -- F8 單位
		sql += " FROM \"PfReward\" PR ";
		sql += " LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = PR.\"CustNo\" ";
		sql += " LEFT JOIN \"PfCoOfficer\" PCO ON PCO.\"EmpNo\" = PR.\"Coorgnizer\" ";
		sql += "                              AND TRUNC(PCO.\"EffectiveDate\" / 100) <= :inputWorkMonth ";
		sql += "                              AND TRUNC(PCO.\"IneffectiveDate\" / 100) >= :inputWorkMonth ";
		sql += " WHERE PR.\"CoorgnizerBonus\" > 0 ";
//		sql += "   AND PR.\"CoorgnizerBonusDate\" > 0 ";
		sql += "   AND PR.\"WorkMonth\" = :inputWorkMonth ";
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
		sql += " SELECT PD.\"CustNo\"            AS CustNo      "; // -- F0 戶號
		sql += "      , PD.\"FacmNo\"            AS FacmNo      "; // -- F1 額度
		sql += "      , PD.\"ComputeCoBonusAmt\" AS DrawdownAmt "; // -- F2 撥款金額
		sql += "      , PD.\"PieceCode\"         AS PieceCode   "; // -- F3 計件代碼
		sql += "      , PD.\"Coorgnizer\"        AS Coorgnizer  "; // -- F4 員工代號
		sql += "      , \"Fn_GetEmpName\"(PD.\"Coorgnizer\",0) ";
		sql += "                                 AS EmpName     "; // -- F5 員工姓名
		sql += "      , PCO.\"DeptItem\"         AS Dept        "; // -- F6 部室
		sql += "      , PCO.\"DistItem\"         AS Dist        "; // -- F7 區部
		sql += "      , PCO.\"AreaItem\"         AS Area        "; // -- F8 單位
		sql += " FROM \"PfDetail\" PD ";
		sql += " LEFT JOIN \"PfCoOfficer\" PCO ON PCO.\"EmpNo\" = PD.\"Coorgnizer\" ";
		sql += "                              AND TRUNC(PCO.\"EffectiveDate\" / 100) <= :inputWorkMonth ";
		sql += "                              AND TRUNC(PCO.\"IneffectiveDate\" / 100) >= :inputWorkMonth ";
		sql += " WHERE PD.\"ComputeCoBonusAmt\" > 0 ";
		sql += "   AND PD.\"WorkMonth\" = :inputWorkMonth ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputWorkMonth", inputWorkMonth);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> queryDept(int pfYear, int pfSeason, String inputDeptCode, TitaVo titaVo) {

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

		String sql = " ";
		sql += " SELECT PCO.\"DistItem\"         AS Dist        "; // -- F0 區部
		sql += "      , PCO.\"AreaItem\"         AS Area        "; // -- F1 單位
		sql += "      , \"Fn_GetEmpName\"(PCO.\"EmpNo\",0) ";
		sql += "                                 AS EmpName     "; // -- F2 員工姓名
		sql += "      , PCO.\"EmpNo\"            AS Coorgnizer  "; // -- F3 員工代號
		sql += "      , PCO.\"EmpClass\"         AS EmpClass    "; // -- F4 考核前職級
		sql += "      , PR.count1                AS count1      "; // -- F5 件數1
		sql += "      , PD.amt1                  AS amt1        "; // -- F6 金額1
		sql += "      , PR.count2                AS count2      "; // -- F7 件數2
		sql += "      , PD.amt2                  AS amt2        "; // -- F8 金額2
		sql += "      , PR.count3                AS count3      "; // -- F9 件數3
		sql += "      , PD.amt3                  AS amt3        "; // -- F10 金額3
		sql += "      , PR.count4                AS count4      "; // -- F11 件數4
		sql += "      , PD.amt4                  AS amt4        "; // -- F12 金額4
		sql += "      , PR.countTotal            AS countTotal  "; // -- F13 件數合計
		sql += "      , PD.amtTotal              AS amtTotal    "; // -- F14 金額合計
		sql += " FROM \"PfCoOfficer\" PCO ";
		sql += " LEFT JOIN ( SELECT \"Coorgnizer\"    AS Coorgnizer "; // -- 介紹人
		sql += "                  , SUM(CASE ";
		sql += "                          WHEN \"WorkMonth\" = :inputWorkMonth1 ";
		sql += "                          THEN 1 ";
		sql += "                        ELSE 0 END) AS count1     "; // -- 件數1
		sql += "                  , SUM(CASE  ";
		sql += "                          WHEN \"WorkMonth\" = :inputWorkMonth2 ";
		sql += "                          THEN 1 ";
		sql += "                        ELSE 0 END) AS count2     "; // -- 件數2
		sql += "                  , SUM(CASE  ";
		sql += "                          WHEN \"WorkMonth\" = :inputWorkMonth3 ";
		sql += "                          THEN 1 ";
		sql += "                        ELSE 0 END) AS count3     "; // -- 件數3
		sql += "                  , SUM(CASE  ";
		sql += "                          WHEN \"WorkMonth\" = :inputWorkMonth4 ";
		sql += "                          THEN 1 ";
		sql += "                        ELSE 0 END) AS count4     "; // -- 件數4
		sql += "                  , SUM(1)          AS countTotal "; // -- 件數合計
		sql += "             FROM \"PfReward\" ";
		sql += "             WHERE \"CoorgnizerBonus\" > 0 ";
		sql += "               AND \"WorkSeason\" = :inputWorkSeason ";
		sql += "             GROUP BY \"Coorgnizer\" ";
		sql += "           ) PR ON PR.Coorgnizer = PCO.\"EmpNo\" ";
		sql += " LEFT JOIN ( SELECT \"Coorgnizer\"             AS Coorgnizer  "; // -- 介紹人
		sql += "                  , SUM(CASE ";
		sql += "                          WHEN \"WorkMonth\" = :inputWorkMonth1 ";
		sql += "                          THEN \"ComputeCoBonusAmt\" ";
		sql += "                        ELSE 0 END )         AS amt1 "; // -- 金額1
		sql += "                  , SUM(CASE ";
		sql += "                          WHEN \"WorkMonth\" = :inputWorkMonth2 ";
		sql += "                          THEN \"ComputeCoBonusAmt\" ";
		sql += "                        ELSE 0 END )         AS amt2 "; // -- 金額2
		sql += "                  , SUM(CASE ";
		sql += "                          WHEN \"WorkMonth\" = :inputWorkMonth3 ";
		sql += "                          THEN \"ComputeCoBonusAmt\" ";
		sql += "                        ELSE 0 END )         AS amt3 "; // -- 金額3
		sql += "                  , SUM(CASE ";
		sql += "                          WHEN \"WorkMonth\" = :inputWorkMonth4 ";
		sql += "                          THEN \"ComputeCoBonusAmt\" ";
		sql += "                        ELSE 0 END )         AS amt4 "; // -- 金額4
		sql += "                  , SUM(\"ComputeCoBonusAmt\") AS amtTotal "; // -- 金額合計
		sql += "             FROM \"PfDetail\"  ";
		sql += "             WHERE \"ComputeCoBonusAmt\" > 0 ";
		sql += "               AND \"WorkSeason\" = :inputWorkSeason ";
		sql += "             GROUP BY \"Coorgnizer\" ";
		sql += "           ) PD ON PD.Coorgnizer = PCO.\"EmpNo\" ";
		sql += " WHERE PCO.\"DeptCode\" = :inputDeptCode ";
		sql += "   AND TRUNC(PCO.\"EffectiveDate\" / 100) <= :inputWorkMonth1 ";
		sql += "   AND TRUNC(PCO.\"IneffectiveDate\" / 100) >= :inputWorkMonth1 ";
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

		return this.convertToMap(query);
	}
}