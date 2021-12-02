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
import com.st1.itx.db.domain.CdBcm;

import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("l5052ServiceImpl")
@Repository
public class L5052ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;
	@Autowired
	private CdBcmService sCdBcmService;

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

	public List<Map<String, String>> FindData(TitaVo titaVo, int index, int limit) throws Exception {
		this.info("L5052FindData");

		int WorkMonthFm = Integer.valueOf(titaVo.getParam("WorkMonthFm").trim()); // 工作月From
		if (WorkMonthFm > 0) {
			WorkMonthFm += 191100;
		}
		int WorkMonthTo = Integer.valueOf(titaVo.getParam("WorkMonthTo").trim()); // 工作月To
		if (WorkMonthTo > 0) {
			WorkMonthTo += 191100;
		}
		String PerfDateFm = titaVo.getParam("PerfDateFm").trim(); // 業績日期From
		String PerfDateTo = titaVo.getParam("PerfDateTo").trim(); // 業績日期To
		String CustNo = titaVo.getParam("CustNo").trim(); // 戶號
		String FacmNo = titaVo.getParam("FacmNo").trim(); // 額度編號
		String SumByFacm = titaVo.getParam("SumByFacm").trim();
		
		String sql = "SELECT A.\"LogNo\",";
		sql += "E1.\"UnitItem\" AS \"BsDeptName\",";
		sql += "F1.\"Fullname\" AS \"OfficerName\",";
		sql += "A.\"BsOfficer\",";
		sql += "A.\"PerfCnt\",";
		sql += "A.\"PerfAmt\",";
		sql += "C.\"CustName\",";
		sql += "A.\"CustNo\",";
		sql += "A.\"FacmNo\",";
		sql += "A.\"BormNo\",";
		sql += "A.\"PerfDate\" - 19110000 as \"PerfDate\",";
		sql += "A.\"ProdCode\",";
		sql += "A.\"PieceCode\",";
		sql += "A.\"DrawdownAmt\",";
		sql += "A.\"RepayType\",";
		sql += "A.\"WorkMonth\" - 191100 as \"WorkMonth\",";
		sql += "E2.\"UnitItem\" AS \"ItDeptName\",";
		sql += "E3.\"UnitItem\" AS \"ItDistName\",";
		sql += "E4.\"UnitItem\" AS \"ItUnitName\",";
		sql += "B.\"Introducer\",";
		sql += "F2.\"Fullname\" AS \"IntroducerName\",";
		sql += "NVL(D.\"LogNo\",0) AS \"AdjLogNo\",";
		sql += "D.\"AdjPerfCnt\",";
		sql += "D.\"AdjPerfAmt\" ";
		sql += "FROM \"PfBsDetail\" A ";
		sql += "LEFT JOIN \"PfItDetail\" B ON B.\"CustNo\"=A.\"CustNo\" AND B.\"FacmNo\"=A.\"FacmNo\" AND B.\"BormNo\"=A.\"BormNo\" AND B.\"PerfDate\"=A.\"PerfDate\" AND B.\"RepayType\"=A.\"RepayType\" AND B.\"PieceCode\"=A.\"PieceCode\" AND B.\"DrawdownAmt\">0 ";
		sql += "LEFT JOIN \"CustMain\" C ON C.\"CustNo\"=A.\"CustNo\" ";
		sql += "LEFT JOIN \"PfBsDetailAdjust\" D ON D.\"CustNo\"=A.\"CustNo\" AND D.\"FacmNo\"=A.\"FacmNo\"  AND D.\"WorkMonth\"=A.\"WorkMonth\" ";
		sql += "LEFT JOIN \"CdBcm\" E1 ON E1.\"UnitCode\"=A.\"DeptCode\" ";
		sql += "LEFT JOIN \"CdBcm\" E2 ON E2.\"UnitCode\"=B.\"DeptCode\" ";
		sql += "LEFT JOIN \"CdBcm\" E3 ON E3.\"UnitCode\"=B.\"DistCode\" ";
		sql += "LEFT JOIN \"CdBcm\" E4 ON E4.\"UnitCode\"=B.\"UnitCode\" ";
		sql += "LEFT JOIN \"CdEmp\" F1 ON F1.\"EmployeeNo\"=A.\"BsOfficer\" ";
		sql += "LEFT JOIN \"CdEmp\" F2 ON F2.\"EmployeeNo\"=B.\"Introducer\" ";
		sql += "WHERE A.\"DrawdownAmt\" > 0 ";
//		sql += "AND A.\"RepayType\" = 0 ";
		if (WorkMonthFm > 0) {
			sql += "AND A.\"WorkMonth\" BETWEEN :WorkMonthFm AND :WorkMonthTo ";
		} else {
			sql += "AND A.\"PerfDate\" BETWEEN :PerfDateFm AND :PerfDateTo ";
		}
		if (CustNo != null && Integer.parseInt(CustNo) != 0) {
			// 戶號
			// sql += "AND bsd.\"CustNo\"=" + Integer.parseInt(CustNo) + " ";
			sql += "AND A.\"CustNo\"= :CustNo ";
		}
		if (FacmNo != null && Integer.parseInt(FacmNo) != 0) {
			// 額度編號
			// sql += "AND bsd.\"FacmNo\"=" + Integer.parseInt(FacmNo) + " ";// 額度編號
			sql += "AND A.\"FacmNo\"= :FacmNo ";// 額度編號
		}
		sql += "ORDER BY A.\"BsOfficer\",A.\"CustNo\",A.\"FacmNo\",A.\"BormNo\" ";
//		if ("Y".equals(SumByFacm)) {
//			sql += "ORDER BY A.\"BsOfficer\",A.\"CustNo\",A.\"FacmNo\",A.\"BormNo\" ";
//		} else {
//			sql += "ORDER BY A.\"PerfDate\",A.\"CustNo\",A.\"FacmNo\",A.\"BormNo\" ";
//		}
		sql += sqlRow;
		this.info("FindL5052 sql=" + sql);

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;
//		this.info("L5051ServiceImpl sql=[" + sql + "]");
//		this.info("L5051ServiceImpl this.index=[" + this.index + "],this.limit=[" + this.limit + "]");

		Query query;
//		query = em.createNativeQuery(sql,L5051Vo.class);//進SQL 所以不要用.class (要用.class 就要使用HQL)
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		if (WorkMonthFm > 0) {
			// 1:工作月
			query.setParameter("WorkMonthFm", WorkMonthFm);
			query.setParameter("WorkMonthTo", WorkMonthTo);
		} else {
			// 1:業績日期
			query.setParameter("PerfDateFm", RocToDc(PerfDateFm));
			query.setParameter("PerfDateTo", RocToDc(PerfDateTo));
		}
		if (CustNo != null && Integer.parseInt(CustNo) != 0) {
			// 戶號
			query.setParameter("CustNo", Integer.parseInt(CustNo));
		}
		if (FacmNo != null && Integer.parseInt(FacmNo) != 0) {
			// 額度編號
			query.setParameter("FacmNo", Integer.parseInt(FacmNo));
		}

		this.info("L5051Service FindData=" + query);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		// query.setFirstResult(this.index*this.limit);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		// List<L5051Vo> L5051VoList =this.convertToMap(query.getResultList());

		return this.convertToMap(query);
	}

	/**
	 * 民國年轉西元年
	 * 
	 * @param str 民國年
	 * @return 西元年
	 */
	public String RocToDc(String str) {
		if (str != null) {
			int strL = str.length();
			if (strL == 7 || strL == 6) {
				str = String.valueOf(Integer.parseInt(str) + 19110000);
			} else if (strL == 8) {
				return str;
			} else {
				str = "0";
			}
		} else {
			str = "0";
		}
		return str;
	}

	public int ChangeYM(String YM) {
		int YYYYmm = 0;
		if (YM != null) {
			int YML = YM.length();
			if (YML == 6) {
				// DC
				YYYYmm = Integer.parseInt(YM);
			} else if (YML == 5) {
				// ROC
				YYYYmm = (Integer.parseInt(YM.substring(0, 3)) + 1911) * 100 + Integer.parseInt(YM.substring(3, 5));
			} else {
				// UnKnow
				this.info("L5951 ChangeYM Original=" + YM);
			}
		}
		return YYYYmm;
	}

	public String FindCdBcm(TitaVo titaVo, String str) {
		String Data = "";
		if (str != null) {
			CdBcm CdBcmVO = new CdBcm();
			CdBcmVO = sCdBcmService.findById(str, titaVo);
			if (CdBcmVO != null) {
				Data = CdBcmVO.getUnitItem();
			}
		}
		return Data;
	}

	public String Format(String str, int len, String Side, String AddStr) {
		if (str != null) {

		} else {
			str = "";
		}
		int strL = str.length();
		if ("R".equals(Side)) {
			for (int i = strL; i < len; i++) {
				str = AddStr + str;
			}
		} else if ("L".equals(Side)) {
			for (int i = strL; i < len; i++) {
				str = str + AddStr;
			}
		}
		return str;
	}

	public String PerfDateToYM(String str) {
		if (str != null) {
			int strL = str.length();
			if (strL == 8 || strL == 6) {
				str = str.substring(0, 4) + "/" + str.substring(4, 6);
			} else if (strL == 7) {
				str = String.valueOf(Integer.parseInt(str.substring(0, 3)) + 1911) + "/" + str.substring(3, 5);
			} else {
				str = "0000/00";
			}
		} else {
			str = "0000/00";
		}
		return str;
	}
}