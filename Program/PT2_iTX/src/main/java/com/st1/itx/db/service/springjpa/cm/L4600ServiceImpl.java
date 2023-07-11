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
public class L4600ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findL4600(int iInsuEndMonth, TitaVo titaVo) throws Exception {
		this.info("L4600ServiceImpl findL4600 start");

		String sql = " ";
		sql += "SELECT  * ";
		sql += "FROM (";
		sql += "      SELECT FA.\"CustNo\"             AS \"CustNo\"        "; // -- 戶號
		sql += "          , FA.\"FacmNo\"              AS \"FacmNo\"        "; // -- 額度
		sql += "          , FA.\"RepayCode\"           AS \"RepayCode\"     "; // -- 繳款方式
		sql += "          , IO.\"ClCode1\"             AS \"ClCode1\"       "; // -- 押品別1
		sql += "          , IO.\"ClCode2\"             AS \"ClCode2\"       "; // -- 押品別1
		sql += "          , IO.\"ClNo\"                AS \"ClNo\"          "; // -- 押品號碼
		sql += "          , IO.\"OrigInsuNo\"          AS \"NowInsuNo\"     "; // -- 保單號碼
		sql += "          , IO.\"InsuEndDate\"         AS \"InsuEndDate\"   "; // -- 保險迄日
		sql += "          , IO.\"InsuCompany\"         AS \"InsuCompany\"   "; // -- 保險公司
		sql += "          , IO.\"InsuTypeCode\"        AS \"InsuTypeCode\"  "; // -- 保險類別
		sql += "          , NVL(IRO.\"OrigInsuNo\",'') AS \"OrigInsuNo\"    "; // -- 原始保險單號碼
		sql += "          , LM.\"Status\"              AS \"Status\"        ";  
		sql += "          , LM.\"MaturityDate\"        AS \"MaturityDate\"  ";  
        sql += "          , row_number() over (partition by IO.\"OrigInsuNo\" order by FA.\"CustNo\",FA.\"FacmNo\",IO.\"ClCode1\", IO.\"ClCode2\", IO	.\"ClNo\" ) as ROWNUMBER ";
		sql += "     FROM \"InsuOrignal\" IO ";
		sql += "     LEFT JOIN \"ClFac\" CF ON CF.\"ClCode1\" = IO.\"ClCode1\" ";
		sql += "                           AND CF.\"ClCode2\" = IO.\"ClCode2\" ";
		sql += "                           AND CF.\"ClNo\"  = IO.\"ClNo\" ";
		sql += "     LEFT JOIN \"FacMain\" FA ON FA.\"CustNo\" = CF.\"CustNo\" ";
		sql += "                             AND FA.\"FacmNo\" = CF.\"FacmNo\" ";
		sql += "     LEFT JOIN \"LoanBorMain\" LM ON LM.\"CustNo\" = FA.\"CustNo\" ";
		sql += "                                 AND LM.\"FacmNo\" = FA.\"FacmNo\" ";
		sql += "     LEFT JOIN \"InsuRenew\" IR ON IR.\"PrevInsuNo\" = IO.\"OrigInsuNo\" ";
		sql += "                               AND IR.\"InsuYearMonth\" = :inputYearMonth ";
		sql += "     LEFT JOIN \"InsuRenew\" IRO ON NVL(IRO.\"NowInsuNo\",' ') = IO.\"OrigInsuNo\" ";
		sql += "     WHERE (IO.\"InsuEndDate\") BETWEEN :inputStartDate AND :inputEndDate ";  
		sql += "       AND TRIM(IO.\"EndoInsuNo\") is NUll ";   
		sql += "       AND CASE WHEN NVL(LM.\"Status\", -1)  IN (0) AND LM.\"MaturityDate\" > :inputEndDate THEN 1 "; // 0:正常戶、到期日 > 續約年月         
		sql += "                WHEN NVL(LM.\"Status\", -1)  IN (2, 7) THEN 2 ";   // 2:催收戶 7:部分轉呆戶     
		sql += "                ELSE 0                                        ";   // 排除結案戶、呆帳戶、未撥款戶、續約年月已到期戶        
		sql += "           END > 0                                            ";                    
		sql += "       AND NVL(IR.\"RenewCode\", 0)  <> 1                     ";   //		排除已自保件
		sql += " ) ";
		sql += " WHERE ROWNUMBER = 1";
		;

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputYearMonth", iInsuEndMonth);
		query.setParameter("inputStartDate", (iInsuEndMonth * 100) + 1);
		query.setParameter("inputEndDate", (iInsuEndMonth * 100) + 31);

		return this.convertToMap(query);
	}
}