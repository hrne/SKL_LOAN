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
import com.st1.itx.util.parse.Parse;

@Service
@Repository
public class L6970ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Autowired
	Parse parse;
	private Query query;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(int index, int limit, TitaVo titaVo) throws Exception {
		this.info("L6970ServiceImpl.findAll");
		// *** 折返控制相關 ***
		this.index = index;
		// *** 折返控制相關 ***
		this.limit = limit;

		int batchResultCode = parse.stringToInteger(titaVo.getParam("BatchResultCode"));
		int inputStartDate = parse.stringToInteger(titaVo.getParam("InputStartDate")) + 19110000;
		int inputEndDate = parse.stringToInteger(titaVo.getParam("InputEndDate")) + 19110000;
		int errorTypeCode = parse.stringToInteger(titaVo.getParam("ErrorTypeCode"));

		// check titaVo for input values
		String sql = "";
		sql += " SELECT I.* ";
		sql += " ,CASE WHEN NVL(E.\"JobTxSeq\",' ') = ' '  THEN 'N' ELSE 'Y' END  AS　\"HaveUspErrorLog\"";
		sql += " FROM \"JobDetail\" I";
		sql += " LEFT JOIN  \"UspErrorLog\" E ON　I.\"Status\" = 'F' ";
		sql += "                             AND  E.\"JobTxSeq\" = I.\"TxSeq\"";
		sql += " WHERE I.\"JobCode\" = 'eodFlow' ";
		sql += "   AND I.\"ExecDate\" BETWEEN :inputStartDate AND :inputEndDate";
		switch (batchResultCode) {
		case 0: // 全部
			break;
		case 1: // 成功
			sql += "   AND I.\"Status\" = 'S' ";
			break;
		case 2: // 失敗
			sql += "   AND I.\"Status\" = 'F' ";
			break;
		}
		switch (errorTypeCode) {
		case 0: // 全部
			break;
		case 1: //  影響換日
			sql += "   AND I.\"NestJobCode\" NOT LIKE '%RptFlow'  ";
			break;
		case 2: // 不影響換日
			sql += "   AND I.\"NestJobCode\"  LIKE '%RptFlow'  ";
			break;
		}
		sql += " ORDER BY I.\"StepStartTime\" DESC ";

		this.info("sql=" + sql);

 		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputStartDate", inputStartDate);
		query.setParameter("inputEndDate", inputEndDate);

		return switchback(query);

	
	}

}