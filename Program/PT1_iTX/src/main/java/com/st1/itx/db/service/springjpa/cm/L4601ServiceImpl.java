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
public class L4601ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}
	public List<Map<String, String>> findNoMediaTemp(TitaVo titaVo) throws Exception {
		// 有續約檔無詢價媒體
		this.info("L4601ServiceImpl findNoMediaTemp start");

		int inputYearMonth = Integer.parseInt(titaVo.getParam("InsuEndMonth")) + 191100;
		this.info("L4601ServiceImpl inputYearMonth = " + inputYearMonth);

		String sql = " ";
		sql += "     SELECT IR.\"InsuYearMonth\"       AS \"InsuYearMonth\" "; // -- 到期年月
		sql += "          , IR.\"CustNo\"              AS \"CustNo\"        "; // -- 戶號
		sql += "          , IR.\"FacmNo\"              AS \"FacmNo\"        "; // -- 額度
		sql += "          , IR.\"PrevInsuNo\"          AS \"PrevInsuNo\"    "; // -- 保單號碼
		sql += "          , IR.\"ClCode1\"             AS \"ClCode1\"    "; // -- 押品別1
		sql += "          , IR.\"ClCode2\"             AS \"ClCode2\"    "; // -- 押品別2
		sql += "          , IR.\"ClNo\"                AS \"ClNo\"    "; // -- 押品號碼
		sql += "     FROM \"InsuRenew\" IR ";
		sql += "     LEFT JOIN \"InsuRenewMediaTemp\" IM ON IM.\"CustNo\" = IR.\"CustNo\" ";
		sql += "                                        AND IM.\"FireInsuMonth\" = IR.\"InsuYearMonth\" ";
		sql += "                                        AND IM.\"InsuNo\" = IR.\"PrevInsuNo\" ";
		sql += "     WHERE IR.\"InsuYearMonth\" = :inputYearMonth ";
		sql += "      AND  IR.\"RenewCode\" = 2 ";
		sql += "      AND  NVL(IM.\"CustNo\",0) = 0 ";

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputYearMonth", inputYearMonth);

		return this.convertToMap(query);
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {
		this.info("L4601ServiceImpl findAll start");

		int inputYearMonth = Integer.parseInt(titaVo.getParam("InsuEndMonth")) + 191100;
		this.info("L4601ServiceImpl inputYearMonth = " + inputYearMonth);

		String sql = " ";
		sql += " WITH \"Data\" AS ( ";
		sql += "     SELECT IR.\"InsuYearMonth\"       AS \"InsuYearMonth\" "; // -- 到期年月
		sql += "          , IR.\"CustNo\"              AS \"CustNo\"        "; // -- 戶號
		sql += "          , IR.\"FacmNo\"              AS \"FacmNo\"        "; // -- 額度
		sql += "          , CM.\"CustName\"            AS \"CustName\"      "; // -- 姓名
		sql += "          , IR.\"PrevInsuNo\"          AS \"PrevInsuNo\"    "; // -- 保單號碼
		sql += "          , IR.\"InsuStartDate\"       AS \"InsuStartDate\" "; // -- 保險起日
		sql += "          , IR.\"InsuEndDate\"         AS \"InsuEndDate\"   "; // -- 保險迄日
		sql += "          , IR.\"FireInsuCovrg\"       AS \"FireInsuCovrg\" "; // -- 火險保額
		sql += "          , IR.\"EthqInsuCovrg\"       AS \"EthqInsuCovrg\" "; // -- 地震險保額
		sql += "          , IR.\"ClCode1\"          AS \"ClCode1\"    "; // -- 押品別1
		sql += "          , IR.\"ClCode2\"          AS \"ClCode2\"    "; // -- 押品別2
		sql += "          , IR.\"ClNo\"          AS \"ClNo\"    "; // -- 押品號碼
		sql += "          , \"Fn_GetAdviseFireInsuCovrg\"(IR.\"ClCode1\",IR.\"ClCode2\",IR.\"ClNo\",1) ";
		sql += "                                     AS \"TotalArea\" "; // -- 總坪數
		sql += "          , \"Fn_GetAdviseFireInsuCovrg\"(IR.\"ClCode1\",IR.\"ClCode2\",IR.\"ClNo\",2)  ";
		sql += "                                     AS \"AdviseFireInsuCovrg\" "; // -- 建議火險保額
		sql += "     FROM \"InsuRenew\" IR ";
		sql += "     LEFT JOIN \"CustMain\" CM ON CM.\"CustNo\" = IR.\"CustNo\" ";
		sql += "     WHERE \"InsuYearMonth\" = :inputYearMonth ";
		sql += " ) ";
		sql += " , \"Condition\" AS ( ";
		sql += "     SELECT \"InsuYearMonth\"  ";// -- 到期年月
		sql += "          , \"CustNo\"         ";// -- 戶號
		sql += "          , \"FacmNo\"         ";// -- 額度
		sql += "          , \"CustName\"       ";// -- 姓名
		sql += "          , \"PrevInsuNo\"     ";// -- 保單號碼
		sql += "          , \"InsuStartDate\"  ";// -- 保險起日
		sql += "          , \"InsuEndDate\"    ";// -- 保險迄日
		sql += "          , \"FireInsuCovrg\"  ";// -- 火險保額
		sql += "          , \"EthqInsuCovrg\"  ";// -- 地震險保額
		sql += "          , \"TotalArea\"      ";// -- 總坪數
		sql += "          , \"AdviseFireInsuCovrg\" "; // -- 建議火險保額
		sql += "          , CASE ";
		sql += "              WHEN \"FireInsuCovrg\" < \"AdviseFireInsuCovrg\" ";
		sql += "              THEN '火險保額<建議火險保額' ";
		// -- 2.有火險無地震險
		// -- (須排除商業單的檢核：保單號碼內含FAP、FIP、FYP者為商業單，可以不用保地震險)
		sql += "              WHEN \"FireInsuCovrg\" > 0 ";
		sql += "                   AND \"EthqInsuCovrg\" = 0 ";
		sql += "                   AND \"PrevInsuNo\" LIKE '%FAP%' ";
		sql += "              THEN NULL ";
		sql += "              WHEN \"FireInsuCovrg\" > 0 ";
		sql += "                   AND \"EthqInsuCovrg\" = 0 ";
		sql += "                   AND \"PrevInsuNo\" LIKE '%FIP%' ";
		sql += "              THEN NULL ";
		sql += "              WHEN \"FireInsuCovrg\" > 0 ";
		sql += "                   AND \"EthqInsuCovrg\" = 0 ";
		sql += "                   AND \"PrevInsuNo\" LIKE '%FYP%' ";
		sql += "              THEN NULL ";
		sql += "              WHEN \"FireInsuCovrg\" > 0 ";
		sql += "                   AND \"EthqInsuCovrg\" = 0 ";
		sql += "              THEN '有火險無地震險' ";
		sql += "              WHEN \"FireInsuCovrg\" = 0 ";
		sql += "                   AND \"EthqInsuCovrg\" > 0 ";
		sql += "              THEN '有地震險無火險' ";
		sql += "            ELSE NULL END AS \"Remark\" "; // -- 備註
		sql += "          , \"ClCode1\"   "; // -- 押品別1
		sql += "          , \"ClCode2\"       "; // -- 押品別2
		sql += "          , \"ClNo\"      "; // -- 押品號碼
		sql += "     FROM \"Data\" ";
		sql += " ) ";
		sql += " SELECT \"InsuYearMonth\" "; // -- 到期年月
		sql += "      , \"CustNo\"        "; // -- 戶號
		sql += "      , \"FacmNo\"        "; // -- 額度
		sql += "      , \"CustName\"      "; // -- 姓名
		sql += "      , \"PrevInsuNo\"    "; // -- 保單號碼
		sql += "      , \"InsuStartDate\" "; // -- 保險起日
		sql += "      , \"InsuEndDate\"   "; // -- 保險迄日
		sql += "      , \"FireInsuCovrg\" "; // -- 火險保額
		sql += "      , \"EthqInsuCovrg\" "; // -- 地震險保額
		sql += "      , \"TotalArea\"     "; // -- 總坪數
		sql += "      , \"AdviseFireInsuCovrg\" "; // -- 建議火險保額
		sql += "      , \"Remark\" "; // -- 備註
		sql += "      , \"ClCode1\""; // -- 押品別1
		sql += "      , \"ClCode2\""; // -- 押品別2
		sql += "      , \"ClNo\""; // -- 押品號碼
		sql += " FROM \"Condition\" ";
		sql += " WHERE NVL(\"Remark\",' ') != ' ' ";
		;

		this.info("sql=" + sql);
		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		query.setParameter("inputYearMonth", inputYearMonth);

		return this.convertToMap(query);
	}

}