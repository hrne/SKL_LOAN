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

@Service("l1908ServiceImpl")
@Repository
public class L1908ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> FindData(TitaVo titaVo, int custNo, int facmNo) throws Exception {
		this.info("L1908FindData");

		String sql = "";
		sql += " SELECT A.\"FormNo\" ";
		sql += " ,A.\"FormName\" ";
		sql += " ,A.\"SendCode\" ";
		sql += " ,A.\"LetterFg\" ";
		sql += " ,A.\"MessageFg\" ";
		sql += " ,A.\"EmailFg\" ";
		sql += " ,NVL(B.\"PaperNotice\",'Y') AS \"PaperNotice\" ";
		sql += " ,NVL(B.\"MsgNotice\",'Y')   AS \"MsgNotice\" ";
		sql += " ,NVL(B.\"EmailNotice\",'Y') AS \"EmailNotice\" ";
		sql += " ,CASE ";
		sql += "    WHEN NVL(B.\"ApplyDate\"-19110000,0) != 0 ";
		sql += "    THEN NVL(B.\"ApplyDate\"-19110000,0) ";
		sql += "    WHEN TO_NUMBER(TO_CHAR(B.\"CreateDate\",'YYYYMMDD')) != 0 ";
		sql += "    THEN TO_NUMBER(TO_CHAR(B.\"CreateDate\",'YYYYMMDD'))-19110000 ";
		sql += "    WHEN TO_NUMBER(TO_CHAR(A.\"CreateDate\",'YYYYMMDD')) != 0 ";
		sql += "    THEN TO_NUMBER(TO_CHAR(A.\"CreateDate\",'YYYYMMDD'))-19110000 ";
		sql += "  ELSE 0 END AS \"ApplyDate\" ";
		sql += " ,NVL(B.\"LastUpdate\",A.\"LastUpdate\") AS \"LastUpdate\" ";
		sql += " ,NVL(B.\"LastUpdateEmpNo\",A.\"LastUpdateEmpNo\") AS \"LastUpdateEmpNo\" ";
		sql += " ,C.\"Fullname\"";
		sql += "FROM \"CdReport\" A ";
		sql += "LEFT JOIN \"CustNotice\" B ON B.\"CustNo\"=:CustNo AND B.\"FacmNo\"=:FacmNo AND B.\"FormNo\"=A.\"FormNo\" ";
		sql += "LEFT JOIN \"CdEmp\" C ON C.\"EmployeeNo\"=NVL(B.\"LastUpdateEmpNo\",A.\"LastUpdateEmpNo\") ";
		sql += "WHERE A.\"SendCode\" > 0 ";
		sql += "ORDER BY A.\"FormNo\" ";

		this.info("Find L1908 sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);

		query.setParameter("CustNo", custNo);
		query.setParameter("FacmNo", facmNo);

		this.info("Find L1908 FindData=" + query);

		return this.convertToMap(query);
	}
}