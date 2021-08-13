package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdBcm;

import com.st1.itx.db.service.CdBcmService;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;
import com.st1.itx.util.common.data.L5051Vo;
import com.st1.itx.util.common.data.L5052Vo;
import com.st1.itx.util.common.data.L5053Vo;
import com.st1.itx.util.common.data.L5054Vo;

@Service("l5051ServiceImpl")
@Repository
public class L5051ServiceImpl extends ASpringJpaParm implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(L5051ServiceImpl.class);

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

	/**
	 * 
	 * @param Map    sql回傳的東西MAP String,String
	 * @param Str    使用哪個對應
	 * @param titaVo TitaVo
	 * @return 指定的VO
	 * @throws LogicException TEST
	 */
	public Object MapToVO(Map<String, String> Map, String Str, TitaVo titaVo) throws LogicException {
		logger.info("L5051Service Map =[" + Map.toString() + "]");
		Object obj = null;
		switch (Str) {
		case "L5051":
			L5051Vo L5051VO = new L5051Vo();
			L5051VO.setLastUpdateEmpNo(Map.get("F0"));// 經辦
			L5051VO.setBsOfficerName(Map.get("F1"));// 房貸專員名稱
			L5051VO.setCustNm(Map.get("F2"));// 戶名
			L5051VO.setCustNo(Map.get("F3"));// 戶號
			L5051VO.setFacmNo(Map.get("F4"));// 額度編號
			L5051VO.setBormNo(Map.get("F5"));// 撥款序號
			L5051VO.setDrawdownDate(Map.get("F6"));// 撥款日
			L5051VO.setProdCode(Map.get("F7"));// 商品代碼
			L5051VO.setPieceCode(Map.get("F8"));// 計件代碼
			L5051VO.setCntingCode(Map.get("F9"));// 是否計件
			L5051VO.setDrawdownAmt(Map.get("F10"));// 撥款金額
			L5051VO.setDeptCode(Map.get("F11"));// 單位代號
			L5051VO.setDistCode(Map.get("F12"));// 區部代號
			L5051VO.setUnitCode(Map.get("F13"));// 部室代號
			L5051VO.setDeptCodeX(Map.get("F14"));// 單位
			L5051VO.setDistCodeX(Map.get("F15"));// 區部
			L5051VO.setUnitCodeX(Map.get("F16"));// 部室
			L5051VO.setEmpNo(Map.get("F17"));// 員工代號
			L5051VO.setIntroducerName(Map.get("F18"));// 介紹人名稱
			L5051VO.setUnitManager(Map.get("F19"));// 處經理
			L5051VO.setDistManager(Map.get("F20"));// 區經理
			L5051VO.setDeptManager(Map.get("F21"));// 部經理
			L5051VO.setPerfCnt(Map.get("F22"));// 件數
			L5051VO.setPerfEqAmt(Map.get("F23"));// 換算業績
			L5051VO.setPerfReward(Map.get("F24"));// 業務報酬
			L5051VO.setPerfAmt(Map.get("F25"));// 業績金額
			L5051VO.setIntroducerBonus(Map.get("F26"));// 介紹獎金
			L5051VO.setPerfDate(Map.get("F27"));// 業績日期
			L5051VO.setBsOfficer(Map.get("F28"));// 房貸專員
			L5051VO.setIntroducer(Map.get("F29"));// 介紹人
			obj = L5051VO;
			break;
		case "L5052":
			L5052Vo L5052VO = new L5052Vo();
			L5052VO.setPerfDate(Map.get("F0"));// 業績日期
			L5052VO.setAreaCenter(Map.get("F1"));// 區域中心
			L5052VO.setAreaCenterX(Map.get("F2"));// 區域中心名稱
			L5052VO.setDeptCode(Map.get("F3"));// 部室別
			L5052VO.setDeptCodeX(Map.get("F4"));// 部室別名稱
			L5052VO.setBsOfficer(Map.get("F5"));// 房貸專員
			L5052VO.setBsOfficerName(Map.get("F6"));// 房貸專員名稱
			L5052VO.setCustNm(Map.get("F7"));// 戶名
			L5052VO.setCustNo(Map.get("F8"));// 戶號
			L5052VO.setFacmNo(Map.get("F9"));// 額度編號
			L5052VO.setBormNo(Map.get("F10"));// 撥款編號
			L5052VO.setDrawdownDate(Map.get("F11"));// 撥款日
			L5052VO.setProdCode(Map.get("F12"));// 商品代碼
			L5052VO.setPieceCode(Map.get("F13"));// 計件代碼
			L5052VO.setDrawdownAmt(Map.get("F14"));// 撥款金額 
			L5052VO.setWorkMonth(Map.get("F15"));// 工作月-民國
			L5052VO.setPerfCnt(Map.get("F16"));// 件數
			L5052VO.setPerfAmt(Map.get("F17"));// 業績金額
			L5052VO.setIntroduceDeptCode(Map.get("F18"));// 介紹人部室代號
			L5052VO.setIntroduceDistCode(Map.get("F19"));// 介紹人區部代號
			L5052VO.setIntroduceUnitCode(Map.get("F20"));// 介紹人單位代號
			L5052VO.setIntroducer(Map.get("F21"));// 介紹人員編
			L5052VO.setIntroducerName(Map.get("F22"));// 介紹人姓名
			obj = L5052VO;
			break;
		case "L5053":
			L5053Vo L5053VO = new L5053Vo();
			L5053VO.setPerfDate(Map.get("F0"));// 業績日期
			L5053VO.setCustNo(Map.get("F1"));// 戶號
			L5053VO.setFacmNo(Map.get("F2"));// 額度
			L5053VO.setBormNo(Map.get("F3"));// 撥款
			L5053VO.setRewardFullName(Map.get("F4"));// 姓名
			L5053VO.setBsOfficer(Map.get("F5"));// 房貸專員
			L5053VO.setOfficerName(Map.get("F6"));// 房貸專員名稱
			L5053VO.setIntroducer(Map.get("F7"));// 介紹人
			L5053VO.setIntroducerName(Map.get("F8"));// 介紹人名稱
			L5053VO.setIntroducerBonus(Map.get("F9"));// 介紹獎金
			L5053VO.setDrawdownDate(Map.get("F10"));// 撥款日/還款日
			L5053VO.setProdCode(Map.get("F11"));// 商品代碼
			L5053VO.setPieceCode(Map.get("F12"));// 計件代碼
			L5053VO.setDrawdownAmt(Map.get("F13"));// 撥款金額
			L5053VO.setLastUpdateEmpNo(Map.get("F14"));// 員工代號
			L5053VO.setLastUpdateEmpNoName(Map.get("F15"));// 員工名稱
			obj = L5053VO;
			break;
		case "L5054":
			L5054Vo L5054VO = new L5054Vo();
			L5054VO.setPerfDate(Map.get("F0"));// 業績日期-PfReward
			L5054VO.setCustNo(Map.get("F1"));// 戶號-PfReward
			L5054VO.setFacmNo(Map.get("F2"));// 額度-PfReward
			L5054VO.setBormNo(Map.get("F3"));// 撥款-PfReward
			L5054VO.setCustNm(Map.get("F4"));// 姓名-PfReward
			L5054VO.setBsOfficer(Map.get("F5"));// 房貸專員-PfBsDetail
			L5054VO.setBsOfficerName(Map.get("F6"));// 房貸專員名稱
			L5054VO.setDrawdownDate(Map.get("F7"));// 撥款日-PfBsDetail
			L5054VO.setProdCode(Map.get("F8"));// 商品代碼-PfBsDetail
			L5054VO.setPieceCode(Map.get("F9"));// 計件代碼-PfBsDetail
			L5054VO.setDrawdownAmt(Map.get("F10"));// 撥款金額 -PfBsDetail
			L5054VO.setIntroducer(Map.get("F11"));// 介紹人
			L5054VO.setIntroducerName(Map.get("F12"));// 介紹人名稱
			L5054VO.setInterviewerA(Map.get("F13"));// 晤談一-PfReward
			L5054VO.setInterviewerB(Map.get("F14"));// 晤談二-PfReward
			L5054VO.setCoorgnizer(Map.get("F15"));// 協辦人員-PfReward
			L5054VO.setCoorgnizerBonus(Map.get("F16"));// 協辦獎金-PfReward
			L5054VO.setIntroducerAddBonus(Map.get("F17"));// 介紹人加碼獎勵津貼-PfReward
			L5054VO.setCreateEmpNo(Map.get("F18"));// 建檔人員-PfReward
			L5054VO.setLastUpdateEmpNo(Map.get("F19"));// 經辦-PfReward
			obj = L5054VO;
			break;
		default:
			throw new LogicException(titaVo, "", "未預期的錯誤");
		}
		return obj;
	}

//	public String FindL5051(String FunctionCd, String PerfDateFm, String PerfDateTo, String CustNo, String FacmNo) throws Exception {
	public List<Object> L5051FindData(int index, int limit, TitaVo titaVo, String PerfDateFm, String PerfDateTo, String CustNo, String FacmNo) throws Exception {
		logger.info("L5051FindData");
		String sql = "SELECT " + "I.\"LastUpdateEmpNo\" AS \"LastUpdateEmpNo\"," + // 經辦部門 0
				"E0.\"Fullname\" AS \"bsOfficerName\"," + // 房貸專員 1
				"C.\"CustName\" AS \"custNm\"," + // 戶名 2
				"I.\"CustNo\" AS \"custNo\"," + // 戶號 3
				"I.\"FacmNo\" AS \"facmNo\"," + // 額度編號 4
				"I.\"BormNo\" AS \"bormNo\"," + // 撥款序號 5
				"I.\"DrawdownDate\" AS \"drawdownDate\"," + // 撥款日 6
				"I.\"ProdCode\" AS \"prodCode\"," + // 商品代碼 7
				"I.\"PieceCode\" AS \"pieceCode\"," + // 計件代碼8
				"I.\"CntingCode\" AS \"cntingCode\"," + // 是否計件 9
				"I.\"DrawdownAmt\" AS \"drawdownAmt\"," + // 撥款金額 10
				"I.\"UnitCode\" AS \"unitCode\"," + // 單位代號 11
				"I.\"DistCode\" AS \"distCode\"," + // 區部代號 12
				"I.\"DeptCode\" AS \"deptCode\"," + // 部室名稱 13
				"B1.\"UnitItem\" AS \"deptCodeX\"," + // 單位名稱 14
				"B1.\"DistItem\" AS \"distCodeX\"," + // 區部名稱 15
				"B1.\"DeptItem\" AS \"unitCodeX\"," + // 部室名稱 16
				"C.\"EmpNo\" AS \"empNo\"," + // 員工名稱 17
				"E1.\"Fullname\" AS \"introducerName\"," + // 介紹人名稱 18
				"E2.\"Fullname\" AS \"unitManager\"," + // 處經理 19
				"E4.\"Fullname\" AS \"distManager\"," + // 區經理 20
				"E5.\"Fullname\" AS \"deptManager\"," + // 部經理 21
				"I.\"PerfCnt\" AS \"perfCnt\"," + // 件數 22
				"I.\"PerfEqAmt\" AS \"perfEqAmt\"," + // 換算業績 23
				"I.\"PerfReward\" AS \"perfReward\"," + // 業務報酬 24
				"I.\"PerfAmt\" AS \"perfAmt\"," + // 業績金額 25
				"R.\"IntroducerBonus\" AS \"introducerBonus\"," + // 介紹獎金 26
				"I.\"PerfDate\" AS \"perfDate\", " + // 業績日期 27
				"B.\"BsOfficer\" AS \"BsOfficer\", " + // 房貸專員 28
				"I.\"Introducer\" AS \"Introducer\", " + // 介紹人 29
				"I.\"RewardDate\", " + // 保費檢核日 30
				"I.\"MediaDate\" " + // 產出媒體檔日期 31
				"FROM \"PfItDetail\" I " + "LEFT JOIN \"PfBsDetail\" B ON B.\"PerfDate\" = I.\"PerfDate\" " + "AND B.\"CustNo\" = I.\"CustNo\" " + "AND B.\"FacmNo\" = I.\"FacmNo\" "
				+ "AND B.\"BormNo\" = I.\"BormNo\" " + "LEFT JOIN \"CustMain\" C " + "ON C.\"CustNo\" = I.\"CustNo\" " + "LEFT JOIN \"CdEmp\" E0 " + "ON E0.\"EmployeeNo\" = B.\"BsOfficer\" "
				+ "LEFT JOIN \"CdEmp\" E1 ON E1.\"EmployeeNo\" = I.\"Introducer\" " + "LEFT JOIN \"CdEmp\" E2 ON E2.\"EmployeeNo\" = I.\"UnitManager\" "
				+ "LEFT JOIN \"CdEmp\" E4 ON E4.\"EmployeeNo\" = I.\"DistManager\" " + "LEFT JOIN \"CdEmp\" E5 ON E5.\"EmployeeNo\" = I.\"DeptManager\" "
				+ "LEFT JOIN \"CdBcm\" B0 ON B0.\"UnitCode\" = B.\"DeptCode\"  " + "LEFT JOIN \"CdBcm\" B1 ON B1.\"UnitCode\" = I.\"UnitCode\" "
				+ "LEFT JOIN \"PfReward\" R ON R.\"PerfDate\" = I.\"PerfDate\" " + "AND R.\"CustNo\" = I.\"CustNo\" " + "AND R.\"FacmNo\" = I.\"FacmNo\" " + "AND R.\"BormNo\" = I.\"BormNo\" ";

		// 1:業績日期
		sql += "WHERE I.\"PerfDate\" BETWEEN :PerfDateFm AND :PerfDateTo ";

		// 2:戶號
		if (CustNo != null && Integer.parseInt(CustNo) != 0) {
			// 戶號
			// sql += "AND I.\"CustNo\"=" + Integer.parseInt(CustNo) + " ";
			sql += "AND I.\"CustNo\"= :CustNo ";
		}
		if (FacmNo != null && Integer.parseInt(FacmNo) != 0) {
			// 額度編號
			// sql += "AND I.\"FacmNo\"=" + Integer.parseInt(FacmNo) + " ";// 額度編號
			sql += "AND I.\"FacmNo\"= :FacmNo ";// 額度編號
		}
		sql += sqlRow;

		logger.info("FindL5051 sql=" + sql);

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;
		logger.info("L5051ServiceImpl sql=[" + sql + "]");
		logger.info("L5051ServiceImpl this.index=[" + this.index + "],this.limit=[" + this.limit + "]");

		Query query;
//			query = em.createNativeQuery(sql,L5051Vo.class);//進SQL 所以不要用.class (要用.class 就要使用HQL)
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);

		// 1:業績日期
		query.setParameter("PerfDateFm", RocToDc(PerfDateFm));
		query.setParameter("PerfDateTo", RocToDc(PerfDateTo));

		// 2:戶號
		if (CustNo != null && Integer.parseInt(CustNo) != 0) {
			// 戶號
			query.setParameter("CustNo", Integer.parseInt(CustNo));
		}
		if (FacmNo != null && Integer.parseInt(FacmNo) != 0) {
			// 額度編號
			query.setParameter("FacmNo", Integer.parseInt(FacmNo));
		}

		logger.info("L5051Service FindData=" + query);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		// query.setFirstResult(this.index*this.limit);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		// List<L5051Vo> L5051VoList =this.convertToMap(query.getResultList());

		@SuppressWarnings("unchecked")
		List<Object> lObject = this.convertToMap(query.getResultList());

		return lObject;
	}

	public String FindL5052(String FunctionCd, String PerfDateFm, String PerfDateTo, String CustNo, String FacmNo) throws Exception {
		logger.info("Sql FindL5052");
		String sql = "SELECT " + "bsd.\"PerfDate\" AS \"PerfDate\" " + // 業績日期
				",office.\"AreaCode\" AS \"AreaCode\" " + // 區域中心
				",bcm.\"UnitItem\" AS \"UnitItem\" " + // 區域中心名稱
				",dept.\"DeptCode\" AS \"DeptCode\" " + // 部室別
				",bcm1.\"DeptItem\" AS \"DeptItem\" " + // 部室別名稱
				",bsd.\"BsOfficer\" AS \"BsOfficer\" " + // 房貸專員
				",emp1.\"Fullname\" AS \"BsOfficerFullname\" " + // 房貸專員名稱
				",cm.\"CustName\" AS \"CustName\" " + // 戶名
				",bsd.\"CustNo\" AS \"CustNo\" " + // 戶號
				",bsd.\"FacmNo\" AS \"FacmNo\" " + // 額度編號
				",bsd.\"BormNo\" AS \"BormNo\" " + // 撥款編號
				",bsd.\"DrawdownDate\" AS \"DrawdownDate\" " + // 撥款日
				",bsd.\"ProdCode\" AS \"ProdCode\" " + // 商品代碼
				",bsd.\"PieceCode\" AS \"PieceCode\" " + // 計件代碼
				",bsd.\"DrawdownAmt\" AS \"DrawdownAmt\" " + // 撥款金額
				",bsd.\"WorkMonth\" AS \"WorkMonth\" " + // 工作月-民國
				",bsd.\"PerfCnt\" AS \"PerfCnt\" " + // 件數
				",bsd.\"PerfAmt\" AS \"PerfAmt\" " + // 業績金額
				",itd.\"DeptCode\" AS \"IntroducerDeptCode\" " + // 介紹人部室代號
				",itd.\"DistCode\" AS \"IntroducerDistCode\" " + // 介紹人區部代號
				",itd.\"UnitCode\" AS \"IntroducerUnitCode\" " + // 介紹人單位代號
				",itd.\"Introducer\" AS \"Introducer\" " + // 介紹人原編
				",emp.\"Fullname\" AS \"IntroducerFullname\" " + // 介紹人姓名
				"FROM \"PfBsDetail\" bsd " + "LEFT JOIN \"PfBsOfficer\" office " + "ON office.\"WorkMonth\"=bsd.\"WorkMonth\" " + "AND office.\"EmpNo\"=bsd.\"BsOfficer\" " + "LEFT JOIN \"CdBcm\" bcm "
				+ "ON bcm.\"UnitCode\"=office.\"AreaCode\" " + "LEFT JOIN \"CdAoDept\" dept " + "ON dept.\"EmployeeNo\"=bsd.\"BsOfficer\" " + "LEFT JOIN \"CdBcm\" bcm1 "
				+ "ON bcm1.\"UnitCode\"=dept.\"DeptCode\" " + "LEFT JOIN \"CustMain\" cm " + "ON cm.\"CustNo\" = bsd.\"CustNo\" AND cm.\"CustNo\"!=0 " + "LEFT JOIN \"PfItDetail\" itd "
				+ "ON itd.\"PerfDate\"=bsd.\"PerfDate\" " + "AND itd.\"CustNo\"=bsd.\"CustNo\" " + "AND itd.\"FacmNo\"=bsd.\"FacmNo\" " + "AND itd.\"BormNo\"=bsd.\"BormNo\" "
				+ "LEFT JOIN \"CdEmp\" emp " + "ON emp.\"EmployeeNo\" = itd.\"Introducer\" " + "LEFT JOIN \"CdEmp\" emp1 " + "ON emp1.\"EmployeeNo\" = bsd.\"BsOfficer\" "
				+ "WHERE bsd.\"PerfDate\" BETWEEN :PerfDateFm AND :PerfDateTo ";
		if (CustNo != null && Integer.parseInt(CustNo) != 0) {
			// 戶號
			// sql += "AND bsd.\"CustNo\"=" + Integer.parseInt(CustNo) + " ";
			sql += "AND bsd.\"CustNo\"= :CustNo ";
		}
		if (FacmNo != null && Integer.parseInt(FacmNo) != 0) {
			// 額度編號
			// sql += "AND bsd.\"FacmNo\"=" + Integer.parseInt(FacmNo) + " ";// 額度編號
			sql += "AND bsd.\"FacmNo\"= :FacmNo ";// 額度編號
		}
		sql += sqlRow;
		logger.info("FindL5052 sql=" + sql);
		return sql;
	}

	public String FindL5053(String FunctionCd, String PerfDateFm, String PerfDateTo, String CustNo, String FacmNo) throws Exception {
		logger.info("Sql FindL5053");
		String sql = "SELECT " + "w.\"PerfDate\" AS \"PerfDate\", " + // 業績日期
				"w.\"CustNo\" AS \"CustNo\", " + // 戶號
				"w.\"FacmNo\" AS \"FacmNo\", " + // 額度
				"w.\"BormNo\" AS \"BormNo\", " + // 撥款
				"c.\"CustName\" AS \"RewardFullName\", " + // 姓名
				"officer.\"BsOfficer\" AS \"BsOfficer\", " + // 房貸專員
				"officeremp.\"Fullname\" AS \"OfficerName\", " + // 房貸專員名稱
				"w.\"Introducer\" AS \"Introducer\", " + // 介紹人
				"introduceremp.\"Fullname\" AS \"IntroducerName\", " + // 介紹人名稱
				"w.\"IntroducerBonus\" AS \"IntroducerBonus\", " + // 介紹獎金
				"officer.\"DrawdownDate\" AS \"DrawdownDate\", " + // 撥款日/還款日
				"officer.\"ProdCode\" AS \"ProdCode\", " + // 商品代碼
				"officer.\"PieceCode\" AS \"PieceCode\", " + // 計件代碼
				"officer.\"DrawdownAmt\" AS \"DrawdownAmt\", " + // 撥款金額
				"w.\"LastUpdateEmpNo\" AS \"LastUpdateEmpNo\", " + // 員工代號
				"officeremp1.\"Fullname\" AS \"LastUpdateEmpNoName\", " + // 員工名稱
				"'-' AS \"Test\" " + "FROM \"PfReward\" w " + "LEFT JOIN \"CustMain\" c " + "ON c.\"CustNo\"=w.\"CustNo\" " + "LEFT JOIN \"PfBsDetail\" officer "
				+ "ON officer.\"PerfDate\"=w.\"PerfDate\" " + "AND officer.\"CustNo\"=w.\"CustNo\" " + "AND officer.\"FacmNo\"=w.\"FacmNo\" " + "AND officer.\"BormNo\"=w.\"BormNo\" "
				+ "LEFT JOIN \"CdEmp\" officeremp " + "ON officeremp.\"EmployeeNo\"=officer.\"BsOfficer\" " + "LEFT JOIN \"CdEmp\" introduceremp " + "ON introduceremp.\"EmployeeNo\"=w.\"Introducer\" "
				+ "LEFT JOIN \"CdEmp\" officeremp1 " + "ON officeremp1.\"EmployeeNo\"=w.\"LastUpdateEmpNo\" " + "WHERE 1=1 ";
		switch (FunctionCd) {
		case "1":
			// 1:業績日期
			// sql += "AND w.\"PerfDate\" BETWEEN " + RocToDc(PerfDateFm) + " AND " +
			// RocToDc(PerfDateTo) + " ";
			sql += "AND w.\"PerfDate\" BETWEEN :PerfDateFm AND :PerfDateTo ";
			break;
		case "2":
			// 2:戶號
			if (CustNo != null && Integer.parseInt(CustNo) != 0) {
				// 戶號
				// sql += "AND w.\"CustNo\"=" + Integer.parseInt(CustNo) + " ";
				sql += "AND w.\"CustNo\"= :CustNo ";
			}
			if (FacmNo != null && Integer.parseInt(FacmNo) != 0) {
				// 額度編號
				// sql += "AND w.\"FacmNo\"=" + Integer.parseInt(FacmNo) + " ";// 額度編號
				sql += "AND w.\"FacmNo\"= :FacmNo ";// 額度編號
			}
			break;
		default:

		}
		sql += sqlRow;
		logger.info("FindL5053 sql=" + sql);
		return sql;
	}

	public String FindL5054(String FunctionCd, String PerfDateFm, String PerfDateTo, String CustNo, String FacmNo) throws Exception {
		logger.info("Sql FindL5054");
		String sql = "SELECT " + "w.\"PerfDate\" AS \"PerfDate\", " + // 業績日期
				"w.\"CustNo\" AS \"CustNo\", " + // 戶號
				"w.\"FacmNo\" AS \"FacmNo\", " + // 額度
				"w.\"BormNo\" AS \"BormNo\", " + // 撥款
				"c.\"CustName\" AS \"RewardFullName\", " + // 姓名
				"officer.\"BsOfficer\" AS \"BsOfficer\", " + // 房貸專員
				"officeremp.\"Fullname\" AS \"OfficerName\", " + // 房貸專員名稱
				"officer.\"DrawdownDate\" AS \"DrawdownDate\", " + // 撥款日/還款日
				"officer.\"ProdCode\" AS \"ProdCode\", " + // 商品代碼
				"officer.\"PieceCode\" AS \"PieceCode\", " + // 計件代碼
				"officer.\"DrawdownAmt\" AS \"DrawdownAmt\", " + // 撥款金額
				"w.\"Introducer\" AS \"Introducer\", " + // 介紹人
				"introduceremp.\"Fullname\" AS \"IntroducerName\", " + // 介紹人名稱
				"w.\"InterviewerA\" AS \"InterviewerA\", " + // 晤談一
				"w.\"InterviewerB\" AS \"InterviewerB\", " + // 晤談二
				"w.\"Coorgnizer\" AS \"Coorgnizer\", " + // 協辦人員
				"w.\"CoorgnizerBonus\" AS \"CoorgnizerBonus\", " + // 協辦獎金
				"w.\"IntroducerAddBonus\" AS \"IntroducerAddBonus\", " + // 介紹人加碼獎勵津貼
				"w.\"CreateEmpNo\" AS \"CreateEmpNo\", " + // 建檔人員
				"w.\"LastUpdateEmpNo\" AS \"LastUpdateEmpNo\", " + // 經辦
				// "officeremp1.\"Fullname\" AS \"LastUpdateEmpNoName\", "+//經辦名稱
				"'-' AS \"Test\" " + "FROM \"PfReward\" w " + "LEFT JOIN \"CustMain\" c " + "ON c.\"CustNo\"=w.\"CustNo\" " + "LEFT JOIN \"PfBsDetail\" officer "
				+ "ON officer.\"PerfDate\"=w.\"PerfDate\" " + "AND officer.\"CustNo\"=w.\"CustNo\" " + "AND officer.\"FacmNo\"=w.\"FacmNo\" " + "AND officer.\"BormNo\"=w.\"BormNo\" "
				+ "LEFT JOIN \"CdEmp\" officeremp " + "ON officeremp.\"EmployeeNo\"=officer.\"BsOfficer\" " + "LEFT JOIN \"CdEmp\" introduceremp " + "ON introduceremp.\"EmployeeNo\"=w.\"Introducer\" "
				+ "LEFT JOIN \"CdEmp\" officeremp1 " + "ON officeremp1.\"EmployeeNo\"=officer.\"LastUpdateEmpNo\" " + "WHERE 1=1 ";
		switch (FunctionCd) {
		case "1":
			// 1:業績日期
			// sql += "AND w.\"PerfDate\" BETWEEN " + RocToDc(PerfDateFm) + " AND " +
			// RocToDc(PerfDateTo) + " ";
			sql += "AND w.\"PerfDate\" BETWEEN :PerfDateFm AND :PerfDateTo ";
			break;
		case "2":
			// 2:戶號
			if (CustNo != null && Integer.parseInt(CustNo) != 0) {
				// 戶號
				// sql += "AND w.\"CustNo\"=" + Integer.parseInt(CustNo) + " ";
				sql += "AND w.\"CustNo\"= :CustNo ";
			}
			if (FacmNo != null && Integer.parseInt(FacmNo) != 0) {
				// 額度編號
				// sql += "AND w.\"FacmNo\"=" + Integer.parseInt(FacmNo) + " ";// 額度編號
				sql += "AND w.\"FacmNo\"= :FacmNo ";// 額度編號
			}
			break;
		default:

		}
		sql += sqlRow;
		logger.info("FindL5054 sql=" + sql);
		return sql;
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

	/**
	 * 
	 * @param index      從第幾筆開始抓
	 * @param limit      每次抓幾筆
	 * @param sql        sql
	 * @param titaVo     titaVo
	 * @param FunctionCd FunctionCd
	 * @param PerfDateFm PerfDateFm
	 * @param PerfDateTo PerfDateTo
	 * @param CustNo     CustNo
	 * @param FacmNo     FacmNo
	 * @return list object
	 * @throws Exception excption
	 */
	public List<Object> FindData(int index, int limit, String sql, TitaVo titaVo, String FunctionCd, String PerfDateFm, String PerfDateTo, String CustNo, String FacmNo) throws Exception {
		logger.info("FindData");

		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;
		logger.info("L5051ServiceImpl sql=[" + sql + "]");
		logger.info("L5051ServiceImpl this.index=[" + this.index + "],this.limit=[" + this.limit + "]");

		Query query;
//		query = em.createNativeQuery(sql,L5051Vo.class);//進SQL 所以不要用.class (要用.class 就要使用HQL)
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("ThisIndex", index);
		query.setParameter("ThisLimit", limit);
		if ("1".equals(FunctionCd) || "3".equals(FunctionCd)) {
			// 1:業績日期
			query.setParameter("PerfDateFm", RocToDc(PerfDateFm));
			query.setParameter("PerfDateTo", RocToDc(PerfDateTo));
		}
		if ("2".equals(FunctionCd) || "3".equals(FunctionCd)) {
			// 2:戶號
			if (CustNo != null && Integer.parseInt(CustNo) != 0) {
				// 戶號
				query.setParameter("CustNo", Integer.parseInt(CustNo));
			}
			if (FacmNo != null && Integer.parseInt(FacmNo) != 0) {
				// 額度編號
				query.setParameter("FacmNo", Integer.parseInt(FacmNo));
			}
		}
		logger.info("L5051Service FindData=" + query);

		// *** 折返控制相關 ***
		// 設定從第幾筆開始抓,需在createNativeQuery後設定
		// query.setFirstResult(this.index*this.limit);
		query.setFirstResult(0);// 因為已經在語法中下好限制條件(筆數),所以每次都從新查詢即可

		// *** 折返控制相關 ***
		// 設定每次撈幾筆,需在createNativeQuery後設定
		query.setMaxResults(this.limit);

		// List<L5051Vo> L5051VoList =this.convertToMap(query.getResultList());

		@SuppressWarnings("unchecked")
		List<Object> lObject = this.convertToMap(query.getResultList());

		return lObject;
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
				logger.info("L5951 ChangeYM Original=" + YM);
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