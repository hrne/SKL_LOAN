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
public class LP001ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> wkSsn(TitaVo titaVo) {

		String iENTDY = String.valueOf(titaVo.getEntDyI() + 19110000);
		this.info("lP001.wkSsn ENTDY=" + iENTDY);

		// 總表 f0:本日之工作年,f1:本日之工作月
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
		return this.convertToMap(query);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findAll(TitaVo titaVo, Map<String, String> wkVo) {

		String iENTDY = String.valueOf(Integer.valueOf(titaVo.get("ENTDY")) + 19110000);

		int iYEAR = 0;
		int iMM = 0;
		if (Integer.parseInt(wkVo.get("F1")) == 1) {
			iYEAR = Integer.parseInt(wkVo.get("F0")) - 1;
			iMM = 13;
		} else {
			iYEAR = Integer.parseInt(wkVo.get("F0"));
			iMM = Integer.parseInt(wkVo.get("F1"));
		}
		this.info("lP001.findAll ENTDY=" + iENTDY + ",iyear=" + iYEAR + ", imm=" + iMM);
		String sql = "SELECT CDD.\"Fullname\"     AS F0 "; // 專案經理
		sql += "           , RSS.\"WorkMonth\"    AS F1 "; // 工作月
		sql += "           , SUM(RSS.\"PerfCnt\") AS F2 "; // 房貸件數
		sql += "           , SUM(RSS.\"PerfAmt\") AS F3 "; // 房貸撥款金額
		sql += "           , RSS.\"AreaCode\"     AS F4 "; // 房貸專員所屬區域中心
		sql += "      FROM ( SELECT PO.\"AreaCode\" "; // 房貸專員所屬區域中心
		sql += "                  , PD.\"WorkMonth\" "; // 工作月
		sql += "                  , SUM(PD.\"PerfCnt\") AS \"PerfCnt\" ";
		sql += "                  , SUM(PD.\"PerfAmt\") AS \"PerfAmt\" ";
		sql += "             FROM \"PfBsDetail\" PD";
		sql += "             LEFT JOIN \"PfBsOfficer\" PO ON PO.\"EmpNo\" = PD.\"BsOfficer\" ";
		sql += "                                         AND PO.\"WorkMonth\" = PD.\"WorkMonth\" ";
		sql += "             WHERE NVL(PO.\"AreaCode\",' ') IN ('10HC00','10HJ00','10HL00') ";
		sql += "               AND TRUNC(PD.\"WorkMonth\" / 100) = :iyear ";
		sql += "               AND PD.\"PerfAmt\" >= 0 "; 
		// sql += " AND PD.\"PerfDate\" <= :iday";
		sql += "             GROUP BY PO.\"AreaCode\" ";
		sql += "                    , PD.\"WorkMonth\" ";
		sql += "             UNION ALL ";
		// 補零
		sql += "             SELECT B.\"UnitCode\"                 AS \"AreaCode\" ";
		sql += "                  , W.\"Year\" * 100 + W.\"Month\" AS \"WorkMonth\" ";
		sql += "                  , 0                              AS \"PerfCnt\" ";
		sql += "                  , 0                              AS \"PerfAmt\" ";
		sql += "             FROM \"CdBcm\" B ";
		sql += "             CROSS JOIN \"CdWorkMonth\" W ";
		sql += "             WHERE B.\"UnitCode\" IN ('10HC00','10HJ00','10HL00') ";
		sql += "               AND W.\"Year\" = :iyear ";
		sql += "               AND W.\"Month\" >= 1 ";
		sql += "               AND W.\"Month\" <= :imm ";
		sql += "           ) RSS ";
		sql += "      LEFT JOIN ( SELECT \"Fullname\" ";
		sql += "                       , \"UnitCode\" ";
		sql += "                  FROM \"CdBcm\" CBB ";
		sql += "                  LEFT JOIN \"CdEmp\" CEE ON CEE.\"EmployeeNo\" = CBB.\"UnitManager\" ";
		sql += "                  WHERE CBB.\"UnitCode\" IN ('10HC00','10HJ00','10HL00') ";
		sql += "                ) CDD ON CDD.\"UnitCode\" = RSS.\"AreaCode\" ";
		sql += "      GROUP BY CDD.\"Fullname\" ";
		sql += "             , RSS.\"WorkMonth\" ";
		sql += "             , RSS.\"AreaCode\" ";
		sql += "      ORDER BY F1 ";
		sql += "             , F4 ";

		this.info("sql=" + sql);

		Query query;

		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);

		query = em.createNativeQuery(sql);
		query.setParameter("iyear", iYEAR);
		// query.setParameter("iday", iENTDY);
		query.setParameter("imm", iMM);
		return this.convertToMap(query);
	}
}