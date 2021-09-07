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
public class LP002ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> wkSsn(TitaVo titaVo) {

		String iENTDY = String.valueOf(titaVo.getEntDyI() + 19110000);
		this.info("lp002.wkSsn ENTDY=" + iENTDY);

		String sql = " ";
		sql += " SELECT W.\"Year\"  AS F0 ";
		sql += "      , W.\"Month\" AS F1 ";
		sql += " FROM \"CdWorkMonth\" W ";
		sql += " WHERE W.\"StartDate\" <= :iday ";
		sql += "   AND W.\"EndDate\" >= :iday ";
		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iday", iENTDY);
		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings({ "unchecked" })
	// 部室
	public List<Map<String, String>> findDept(TitaVo titaVo, Map<String, String> wkVo) {

		String iENTDY = String.valueOf(titaVo.getEntDyI() + 19110000);
//        String iYEAR = String.valueOf((Integer.valueOf((titaVo.get("ENTDY")) + 19110000) / 10000));
//        String iMM = String.valueOf((Integer.valueOf((titaVo.get("ENTDY").substring(0, 6)) + 191100) % 100));

		int iYEAR = Integer.parseInt(wkVo.get("F0"));
		int iMM = Integer.parseInt(wkVo.get("F1"));

		if (Integer.parseInt(wkVo.get("F1")) == 1) {
			iYEAR = Integer.parseInt(wkVo.get("F0")) - 1;
			iMM = 13;
		} else {
			iYEAR = Integer.parseInt(wkVo.get("F0"));
			iMM = Integer.parseInt(wkVo.get("F1")) - 1;
		}

		this.info("lp002.findDept ENTDY=" + iENTDY + ",iyear=" + iYEAR + ", imm=" + iYEAR * 100 + iMM);
		String sql = " ";
		sql += " SELECT B.\"UnitItem\"  AS F0 ";
		sql += "      , E.\"Fullname\"  AS F1 ";
		sql += "      , D.\"Fullname\"  AS F2 ";
		sql += "      , I.\"WorkMonth\" AS F3 ";
		sql += "      , I.\"PerfCnt\"   AS F4 ";
		sql += "      , I.\"PerfAmt\"   AS F5 ";
		sql += "      , I.\"DeptCode\"  AS F6 ";
		sql += " FROM ( SELECT I.\"DeptCode\" ";
		sql += "             , I.\"WorkMonth\" ";
		sql += "             , SUM(I.\"PerfCnt\") AS \"PerfCnt\" ";
		sql += "             , SUM(I.\"PerfAmt\") AS \"PerfAmt\" ";
		sql += "        FROM ( SELECT I.\"DeptCode\" ";
		sql += "                    , I.\"WorkMonth\" ";
		sql += "                    , I.\"PerfCnt\" ";
		sql += "                    , I.\"PerfAmt\" ";
		sql += "               FROM \"PfItDetail\" I ";
		sql += "               WHERE TRUNC(I.\"WorkMonth\" / 100) = :iyear ";
		sql += "                 AND I.\"PerfDate\" <= :iday ";
		sql += "                 AND I.\"DeptCode\" IS NOT NULL ";
		sql += "               UNION ALL";
		sql += "               SELECT B.\"DeptCode\"                 AS \"DeptCode\" ";
		sql += "                    , W.\"Year\" * 100 + W.\"Month\" AS \"WorkMonth\" ";
		sql += "                    , 0                              AS \"PerfCnt\" ";
		sql += "                    , 0                              AS \"PerfAmt\" ";
		sql += "               FROM \"CdBcm\" B ";
		sql += "               CROSS JOIN \"CdWorkMonth\" W ";
		sql += "               WHERE B.\"DeptCode\" IN ('A0B000','A0F000','A0E000','A0M000') ";
		sql += "                 AND W.\"Year\" = :iyear ";
		sql += "             ) I ";
		sql += "        GROUP BY I.\"DeptCode\" ";
		sql += "               , I.\"WorkMonth\" ";
		sql += "      ) I ";
		sql += " LEFT JOIN ( SELECT \"EmpNo\"";
		sql += "                  , \"Fullname\"";
		sql += "                  , \"DeptCode\"";
		sql += "             FROM \"PfBsOfficer\"";
		sql += "             WHERE \"WorkMonth\" = :wkm ";
		sql += "               AND \"DistCode\" IS NULL ";
		sql += "               AND \"DistItem\" LIKE '房貸%' ";
		sql += "             GROUP BY \"EmpNo\" ";
		sql += "                    , \"Fullname\" ";
		sql += "                    , \"DeptCode\" ";
		sql += "           ) D ON D.\"DeptCode\" = I.\"DeptCode\" ";
		sql += " LEFT JOIN \"CdBcm\" B ON B.\"UnitCode\" = I.\"DeptCode\" ";
		sql += " LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = B.\"UnitManager\" ";
		sql += " WHERE I.\"DeptCode\" IN ('A0B000','A0F000','A0E000','A0M000') ";
		sql += "   AND I.\"WorkMonth\" <= :wkm ";
		sql += " ORDER BY I.\"DeptCode\" ";
		sql += "        , I.\"WorkMonth\" ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iyear", iYEAR);
		query.setParameter("iday", iENTDY);
		query.setParameter("wkm", iYEAR * 100 + iMM);

		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings({ "unchecked" })
	// 區部
	public List<Map<String, String>> findDist(TitaVo titaVo, Map<String, String> wkVo) {

		String iENTDY = String.valueOf(titaVo.getEntDyI() + 19110000);
		int iYEAR = Integer.parseInt(wkVo.get("F0"));
		int iMM = Integer.parseInt(wkVo.get("F1"));

		if (Integer.parseInt(wkVo.get("F1")) == 1) {
			iYEAR = Integer.parseInt(wkVo.get("F0")) - 1;
			iMM = 13;
		} else {
			iYEAR = Integer.parseInt(wkVo.get("F0"));
			iMM = Integer.parseInt(wkVo.get("F1")) - 1;
		}
		this.info("lp002.findDist ENTDY=" + iENTDY + ",iyear=" + iYEAR + ", imm=" + iMM);

		String sql = " ";
		sql += " SELECT B.\"DeptItem\"      AS F0 ";
		sql += "      , B.\"UnitItem\"      AS F1 ";
		sql += "      , E.\"Fullname\"      AS F2 ";
		sql += "      , D.\"DepartOfficer\" AS F3 ";
		sql += "      , I.\"WorkMonth\"     AS F4 ";
		sql += "      , I.\"PerfCnt\"       AS F5 ";
		sql += "      , I.\"PerfAmt\"       AS F6 ";
		sql += "      , I.\"DistCode\"      AS F7 ";
		sql += " FROM ( SELECT I.\"DistCode\"";
		sql += "             , I.\"WorkMonth\"";
		sql += "             , SUM(I.\"PerfCnt\") AS \"PerfCnt\" ";
		sql += "             , SUM(I.\"PerfAmt\") AS \"PerfAmt\" ";
		sql += "        FROM ( SELECT I.\"DistCode\" ";
		sql += "                    , I.\"WorkMonth\" ";
		sql += "                    , I.\"PerfCnt\" ";
		sql += "                    , I.\"PerfAmt\" ";
		sql += "               FROM \"PfItDetail\" I ";
		sql += "               WHERE TRUNC(I.\"WorkMonth\" / 100) = :iyear ";
		sql += "               UNION ALL";
		sql += "               SELECT B.\"UnitCode\"                 AS \"DistCode\" ";
		sql += "                    , W.\"Year\" * 100 + W.\"Month\" AS \"WorkMonth\" ";
		sql += "                    , 0                              AS \"PerfCnt\"";
		sql += "                    , 0                              AS \"PerfAmt\"";
		sql += "               FROM \"CdBcm\" B ";
		sql += "               CROSS JOIN \"CdWorkMonth\" W ";
		sql += "               WHERE B.\"DeptCode\" IN ('A0B000','A0F000','A0E000','A0M000') ";
		sql += "                 AND B.\"UnitCode\" = B.\"DistCode\" ";
		sql += "                 AND W.\"Year\" = :iyear ";
		sql += "                 AND W.\"Month\" >= 1 ";
		sql += "                 AND W.\"Month\" <= :imm ";
		sql += "             ) I ";
		sql += "        GROUP BY I.\"DistCode\",I.\"WorkMonth\" ";
		sql += "      ) I ";
		// sql += " LEFT JOIN(SELECT \"DistCode\"";
		// sql += " ,ROW_NUMBER() OVER ( ORDER BY SUM(\"PerfAmt\") DESC) AS SEQ";
		// sql += " FROM(SELECT I.\"DistCode\"";
		// sql += " ,I.\"PerfAmt\"";
		// sql += " FROM \"PfItDetail\" I";
		// sql += " WHERE TRUNC(I.\"WorkMonth\" / 100) = :iyear";
		// sql += " AND I.\"PerfDate\" <= :iday";
		// sql += " UNION ALL";
		// sql += " SELECT B.\"DistCode\"";
		// sql += " ,0 \"PerfAmt\"";
		// sql += " FROM \"CdBcm\" B";
		// sql += " WHERE B.\"DeptCode\" IN('A0B000','A0F000','A0E000','A0M000')";
		// sql += " AND B.\"DistCode\" = B.\"UnitCode\")";
		// sql += " GROUP BY \"DistCode\") S";
		// sql += " ON S.\"DistCode\" = I.\"DistCode\"";
		sql += " LEFT JOIN \"CdBcm\" B ON B.\"UnitCode\" = B.\"DistCode\" ";
		sql += "                       AND B.\"UnitCode\" = I.\"DistCode\"";
		sql += " LEFT JOIN ( SELECT * ";
		sql += "             FROM \"PfDeparment\" ";
		sql += "             WHERE \"DepartOfficer\" IS NOT NULL ";
		sql += "               AND \"DistItem\" IS NOT NULL ";
		sql += "           ) D ON D.\"DistCode\" = B.\"DistCode\" ";
		sql += " LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = B.\"UnitManager\" ";
		sql += " WHERE B.\"DeptCode\" IN ('A0B000','A0F000','A0E000','A0M000') ";
		sql += "   AND B.\"DistCode\" = B.\"UnitCode\" ";
		sql += "   AND E.\"Fullname\" IS NOT NULL ";
		sql += " ORDER BY B.\"DeptItem\" ";
		sql += "        , I.\"DistCode\" ";
		sql += "        , I.\"WorkMonth\" ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iyear", iYEAR);

		query.setParameter("imm", iMM);

		return this.convertToMap(query.getResultList());
	}

	@SuppressWarnings({ "unchecked" })
	// 營管 營推 業推 業開
	public List<Map<String, String>> findUnit(TitaVo titaVo, Map<String, String> wkVo, String unitCode) {

		String iENTDY = String.valueOf(titaVo.getEntDyI() + 19110000);
		int iYEAR = Integer.parseInt(wkVo.get("F0"));
		int iMM = Integer.parseInt(wkVo.get("F1"));

		if (Integer.parseInt(wkVo.get("F1")) == 1) {
			iYEAR = Integer.parseInt(wkVo.get("F0")) - 1;
			iMM = 13;
		} else {
			iYEAR = Integer.parseInt(wkVo.get("F0"));
			iMM = Integer.parseInt(wkVo.get("F1")) - 1;
		}
		this.info("lP002.findUnit ENTDY=" + iENTDY + ",iyear=" + iYEAR + ", imm=" + iMM);

		String sql = " ";
		sql += " SELECT B.\"DistItem\"  AS F0 ";
		sql += "      , B.\"UnitItem\"  AS F1 ";
		sql += "      , E.\"Fullname\"  AS F2 ";
		sql += "      , I.\"WorkMonth\" AS F3 ";
		sql += "      , I.\"PerfCnt\"   AS F4 ";
		sql += "      , I.\"PerfAmt\"   AS F5 ";
		sql += "      , B.\"DeptCode\"  AS F6 ";
		sql += "      , I.\"UnitCode\"  AS F7 ";
		sql += " FROM ( SELECT I.\"UnitCode\" ";
		sql += "             , I.\"WorkMonth\" ";
		sql += "             , SUM(I.\"PerfCnt\") AS \"PerfCnt\" ";
		sql += "             , SUM(I.\"PerfAmt\") AS \"PerfAmt\" ";
		sql += "        FROM ( SELECT I.\"UnitCode\" ";
		sql += "                    , I.\"WorkMonth\" ";
		sql += "                    , I.\"PerfCnt\" ";
		sql += "                    , I.\"PerfAmt\" ";
		sql += "               FROM \"PfItDetail\" I ";
		sql += "               WHERE TRUNC(I.\"WorkMonth\" / 100) = :iyear ";
		sql += "                 AND I.\"PerfDate\" <= :iday ";
		sql += "               UNION ALL ";
		sql += "               SELECT B.\"UnitCode\"                 AS \"DeptCode\" ";
		sql += "                    , W.\"Year\" * 100 + W.\"Month\" AS \"WorkMonth\" ";
		sql += "                    , 0                              AS \"PerfCnt\" ";
		sql += "                    , 0                              AS \"PerfAmt\" ";
		sql += "               FROM \"CdBcm\" B ";
		sql += "               CROSS JOIN \"CdWorkMonth\" W ";
		sql += "               WHERE B.\"DeptCode\" IN ('A0B000','A0F000','A0E000','A0M000') ";
		sql += "                 AND B.\"DistCode\" <> B.\"UnitCode\" ";
		sql += "                 AND W.\"Year\" = :iyear ";
		sql += "                 AND W.\"Month\" >= 1 ";
		sql += "                 AND W.\"Month\" <= :imm ";
		sql += "             ) I ";
		sql += "        GROUP BY I.\"UnitCode\", I.\"WorkMonth\" ";
		sql += "      ) I ";
		// sql += " LEFT JOIN(SELECT \"DeptCode\"";
		// sql += " ,\"UnitCode\"";
		// sql += " ,ROW_NUMBER() OVER (PARTITION BY \"DeptCode\" ORDER BY
		// SUM(\"PerfAmt\") DESC)AS SEQ";
		// sql += " FROM(SELECT I.\"DeptCode\"";
		// sql += " ,I.\"UnitCode\"";
		// sql += " ,I.\"PerfAmt\"";
		// sql += " FROM \"PfItDetail\" I";
		// sql += " WHERE TRUNC(I.\"WorkMonth\" / 100) = :iyear";
		// sql += " AND I.\"PerfDate\" <= :iday";
		// sql += " UNION ALL";
		// sql += " SELECT B.\"DeptCode\"";
		// sql += " ,B.\"UnitCode\"";
		// sql += " ,0 \"PerfAmt\"";
		// sql += " FROM \"CdBcm\" B";
		// sql += " WHERE B.\"DeptCode\" IN ('A0B000','A0F000','A0E000','A0M000')";
		// sql += " AND B.\"DistCode\" <> B.\"UnitCode\")";
		// sql += " GROUP BY \"DeptCode\", \"UnitCode\") S";
		// sql += " ON S.\"UnitCode\" = I.\"UnitCode\"";
		sql += " LEFT JOIN \"CdBcm\" B ON B.\"UnitCode\" = I.\"UnitCode\" ";
		sql += " LEFT JOIN \"CdEmp\" E ON E.\"EmployeeNo\" = B.\"UnitManager\" ";
		sql += " WHERE B.\"DeptCode\" IN ('" + unitCode + "') ";
		sql += "   AND B.\"UnitManager\" IS NOT NULL ";
		sql += " ORDER BY B.\"DeptCode\" ";
		sql += "        , I.\"UnitCode\" ";
		sql += "        , I.\"WorkMonth\" ";
		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("iyear", iYEAR);
		query.setParameter("iday", iENTDY);
		query.setParameter("imm", iMM);

		return this.convertToMap(query.getResultList());
	}
}