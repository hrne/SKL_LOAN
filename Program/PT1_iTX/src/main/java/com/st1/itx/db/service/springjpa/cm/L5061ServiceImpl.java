package com.st1.itx.db.service.springjpa.cm;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.ASpringJpaParm;
import com.st1.itx.db.transaction.BaseEntityManager;

@Service("l5061ServiceImpl")
public class L5061ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 創建程式碼後,檢查初始值
		// org.junit.Assert.assertNotNull(sPfItDetailService);
	}

	public List<Map<String, String>> FindData(String DateS, String DateE, int CustNoS, int CustNoE, int OptionCode, TitaVo titaVo) throws Exception {
		String sql = "select c.\"CustNo\"" + ",c.\"FacmNo\"" + ",n.\"CustName\"" + ",l.\"Status\"" + ",l.\"PrinBalance\"" + ",l.\"PrevIntDate\"" + ",l.\"OvduTerm\"";
		switch (OptionCode) {
		case 1:
			sql += ",c.\"TelDate\"" + ",c.\"TelTime\"" + ",c.\"ResultCode\"" + ",l.\"AccCollPsn\"" + ",l.\"LegalPsn\" " + "from \"CollTel\" c "
					+ "left join \"CollList\" l on l.\"CustNo\" = c.\"CustNo\" and l.\"FacmNo\" = c.\"FacmNo\" " + " left join \"CustMain\" n on n.\"CustNo\" = c.\"CustNo\" ";
			break;
		case 2:
			sql += ",c.\"MeetDate\"" + ",c.\"MeetTime\"" + ",c.\"Remark\"" + ",l.\"AccCollPsn\"" + ",l.\"LegalPsn\" " + " from \"CollMeet\" c "
					+ "left join \"CollList\" l on l.\"CustNo\" = c.\"CustNo\" and l.\"FacmNo\" = c.\"FacmNo\" " + " left join \"CustMain\" n on n.\"CustNo\" = c.\"CustNo\" ";
			break;
		case 3:
			sql += ",c.\"MailDate\"" + ",c.\"MailTypeCode\"" + ",l.\"AccCollPsn\"" + ",l.\"LegalPsn\" " + "from \"CollLetter\" c "
					+ "left join \"CollList\" l on l.\"CustNo\" = c.\"CustNo\" and l.\"FacmNo\" = c.\"FacmNo\" " + " left join \"CustMain\" n on n.\"CustNo\" = c.\"CustNo\" ";
			break;
		case 5:
			break;
		}
		if (CustNoS != 0 && CustNoE != 0) {
			sql += " where c.\"CustNo\" between " + CustNoS + " and " + CustNoE;
		} else {
			// 有輸入催收日期
			switch (OptionCode) {
			case 1:
				sql += "where c.\"TelDate\" between ";
				break;
			case 2:
				sql += "where c.\"MeetDate\" between ";
				break;
			case 3:
				sql += "where c.\"MailDate\" between ";
				break;
			case 5:
				break;
			}
			sql += String.valueOf(Integer.valueOf(DateS) + 19110000) + " and " + String.valueOf(Integer.valueOf(DateE) + 19110000);
		}
		// 逾期天數>0
		if (!DateS.equals("0000000") && !DateE.equals("0000000") && CustNoS != 0 && CustNoE != 0) {
			sql += " and l.\"OvduTerm\" > 0";
		} else if (!DateS.equals("0000000") && !DateE.equals("0000000")) {
			sql += " and l.\"OvduTerm\" > 0";
		} else if (CustNoS != 0 && CustNoE != 0) {
			sql += " and l.\"OvduTerm\" > 0";
		} else {
			sql += " where l.\"OvduTerm\" > 0";
		}
		// 排序
		switch (OptionCode) {
		case 1:
			sql += " ORDER BY c.\"TelDate\" DESC, c.\"TelTime\" DESC, c.\"CustNo\" ";
			break;
		case 2:
			sql += " ORDER BY c.\"MeetDate\" DESC, c.\"MeetTime\" DESC, c.\"CustNo\" ";
			break;
		case 3:
			sql += " ORDER BY c.\"MailDate\" DESC, c.\"CustNo\" ";
			break;
		case 5:
			break;
		}

		this.info("sql = " + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		this.info("L5061Service FindData=" + query.toString());
		return this.convertToMap(query);
	}
}
