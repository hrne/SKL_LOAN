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
public class LM049ServiceImpl extends ASpringJpaParm implements InitializingBean {

	@Autowired
	private BaseEntityManager baseEntityManager;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	public List<Map<String, String>> findAll(TitaVo titaVo) throws Exception {

		this.info("lM049.findAll ");

		String sql = "";
		sql += " WITH CF AS ( ";
		sql += "   SELECT CF.\"CustNo\" AS \"CustNo\" ";
		sql += "        , CF.\"FacmNo\" AS \"FacmNo\" ";
		sql += "        , ROW_NUMBER() ";
		sql += "          OVER ( ";
		sql += "            PARTITION BY CF.\"CustNo\" ";
		sql += "                       , CF.\"FacmNo\" ";
		sql += "                       , CF.\"ClCode1\" ";
		sql += "                       , CF.\"ClCode2\" ";
		sql += "                       , CF.\"ClNo\" ";
		sql += "            ORDER BY NVL(CE_early.\"EvaDate\",0) DESC "; // 第1段. 最接近該額度核准日期，且擔保品鑑價日小於等於核准日期的那筆資料
		sql += "                   , NVL(CE_later.\"EvaDate\",0) "; // 第2段. 若第1段抓不到資料，才是改為抓鑑價日期最接近核准日期的那筆評估淨值
		sql += "          )                               AS \"Seq\" ";
		sql += "        , NVL(CE_early.\"EvaNetWorth\",NVL(CE_later.\"EvaNetWorth\",0)) ";
		sql += "                                          AS \"EvaNetWorth\" ";
		sql += "   FROM \"ClFac\" CF ";
		sql += "   LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = CF.\"CustNo\" ";
		sql += "                            AND FAC.\"FacmNo\" = CF.\"FacmNo\" ";
		sql += "   LEFT JOIN \"FacCaseAppl\" CAS ON CAS.\"ApplNo\" = CF.\"ApproveNo\" ";
		sql += "   LEFT JOIN \"ClEva\" CE_early ON CE_early.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                               AND CE_early.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                               AND CE_early.\"ClNo\" = CF.\"ClNo\" ";
		sql += "                               AND CE_early.\"EvaDate\" <= CAS.\"ApproveDate\" ";
		sql += "   LEFT JOIN \"ClEva\" CE_later ON CE_later.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                               AND CE_later.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                               AND CE_later.\"ClNo\" = CF.\"ClNo\" ";
		sql += "                               AND CE_later.\"EvaDate\" > CAS.\"ApproveDate\" ";
		sql += "                               AND NVL(CE_early.\"EvaDate\",0) = 0 "; // 若第1段串不到,才串第2段
		sql += " ) ";
		sql += " , \"CFSum\" AS ( ";
		sql += "   SELECT \"CustNo\" ";
		sql += "        , \"FacmNo\" ";
		sql += "        , SUM(NVL(\"EvaNetWorth\",0)) AS \"EvaNetWorth\" ";
		sql += "   FROM CF ";
		sql += "   WHERE \"Seq\" = 1 "; // 每個擔保品只取一筆
		sql += "   GROUP BY \"CustNo\" ";
		sql += "          , \"FacmNo\" ";
		sql += " )";
		sql += " SELECT S0.\"CusType\" "; // F0
		sql += "      , S0.\"CusSCD\" "; // F1
		sql += "      , S0.\"CusscdName\" "; // F2
		sql += "      , S0.\"CusID\" "; // F3
		sql += "      , S0.\"CusName\" "; // F4
		sql += "      , S0.\"STSName\" "; // F5 -- 缺表 STATUS
		sql += "      , S0.\"CusName2\" "; // F6
		sql += "      , S1.\"CustNo\" "; // F7
		sql += "      , S1.\"FacmNo\" "; // F8
		sql += "      , S1.\"DrawdownDate\" "; // F9
		sql += "      , S1.\"MaturityDate\" "; // F10
		sql += "      , S1.\"StoreRate\" "; // F11
		sql += "      , S1.\"EvaAmt\" "; // F12
		// -- 核貸成數 2021-08-12 與舜雯電話討論後 要改用放款餘額/評估淨值計算出當下的貸放成數
		// -- , S1.\"LoanToValue\"
		sql += "      , S1.\"LoanBal\" "; // F13
		sql += "      , CASE ";
		sql += "          WHEN S2.\"SameCollateralDifferentFacmNo\" > 0";
		sql += "          THEN 'Y' ";
		sql += "        ELSE 'N' END AS \"HasSameCollateralDifferentFacmNo\""; // F14
		sql += "      , S1.\"ClCode1\" || '-' || S1.\"ClCode2\" || '-' || S1.\"ClNo\" AS \"ClNo\""; // F15
		sql += " FROM ( ";
		// -- 第一段:金控公司負責人(經理以上)及大股東名單
		sql += "   SELECT '1'               AS \"CusType\" ";
		sql += "        , N'1'              AS \"CusSCD\" ";
		sql += "        , RSC.\"CusSName\"  AS \"CusscdName\" ";
		sql += "        , RS.\"CusId\"      AS \"CusID\" ";
		sql += "        , RS.\"CusName\"    AS \"CusName\" ";
		sql += "        , null              AS \"STSName\" "; // -- 缺表 STATUS
		sql += "        , null              AS \"CusName2\" ";
		sql += "   FROM \"RptRelationSelf\" RS ";
		sql += "   LEFT JOIN \"RptSubCom\" RSC ON RSC.\"CusSCD\" = RS.\"CusSCD\" ";
		sql += "   WHERE RS.\"CusSCD\" = '1' ";
		sql += "     AND RS.\"STSCD\" IN ('1','2','3','4','5','6','7','8','11','13','14','15','16','17','18','19','28','48','49') ";
		// -- RE201203455(13副董事長,14獨立董事,15獨立監察人,16關係企業)
		sql += "     AND RS.\"CusCCD\" IN ('1','3') ";
		sql += "   UNION ALL ";
		// -- 第二段:前者經營事業為董事長 "前者為獨資、合夥經營事業或擔任負責人或為代表人團體"
		sql += "   SELECT '2'               AS \"CusType\" ";
		sql += "        , RC.\"CusSCD\"     AS \"CusSCD\" ";
		sql += "        , RSC.\"CusSName\"  AS \"CusscdName\" ";
		sql += "        , RC.\"ComNo\"      AS \"CusID\" ";
		sql += "        , RC.\"ComName\"    AS \"CusName\" ";
		sql += "        , null              AS \"STSName\" "; // -- 缺表 STATUS
		sql += "        , RS.\"CusName\"    AS \"CusName2\" ";
		sql += "   FROM \"RptRelationCompany\" RC ";
		sql += "   LEFT JOIN \"RptSubCom\" RSC ON RSC.\"CusSCD\" = RC.\"CusSCD\" ";
		sql += "   LEFT JOIN \"RptRelationSelf\" RS ON RS.\"CusId\" = RC.\"CusId\" ";
		sql += "   WHERE RS.\"STSCD\" = '1' ";
		sql += "     AND RS.\"CusSCD\" = '1' ";
		sql += "     AND RS.\"CusCCD\" IN ('1','3') ";
		sql += "   UNION ALL ";
		// -- 第三段:cusccd = 2(法人類) "有半數以上董事與金控公司或其子公司相同之公司"
		sql += "   SELECT '2'               AS \"CusType\" ";
		sql += "        , RS.\"CusSCD\"     AS \"CusSCD\" ";
		sql += "        , RSC.\"CusSName\"  AS \"CusscdName\" ";
		sql += "        , RS.\"CusId\"      AS \"CusID\" ";
		sql += "        , RS.\"CusName\"    AS \"CusName\" ";
		sql += "        , null              AS \"STSName\" "; // -- 缺表 STATUS
		sql += "        , null              AS \"CusName2\" ";
		sql += "   FROM \"RptRelationSelf\" RS ";
		sql += "   LEFT JOIN \"RptSubCom\" RSC ON RSC.\"CusSCD\" = RS.\"CusSCD\" ";
		sql += "   WHERE RS.\"CusSCD\" = '2' ";
		sql += "     AND RS.\"STSCD\" IN ('1','2','3','4','5','6','7','8','11','13','14','15','16','17','18','19','28','48','49') ";
		// -- RE201203455(13副董事長,14獨立董事,15獨立監察人,16關係企業)
		sql += "     AND RS.\"CusCCD\" = '2' ";
		sql += "   UNION ALL ";
		// -- 第四段: ??? 可能是"金控公司子公司及其負責人、大股東"
		sql += "   SELECT '3'               AS \"CusType\" ";
		sql += "        , RS.\"CusSCD\"     AS \"CusSCD\" ";
		sql += "        , RSC.\"CusSName\"  AS \"CusscdName\" ";
		sql += "        , RS.\"CusId\"      AS \"CusID\" ";
		sql += "        , RS.\"CusName\"    AS \"CusName\" ";
		sql += "        , null              AS \"STSName\" "; // -- 缺表 STATUS
		sql += "        , null              AS \"CusName2\" ";
		sql += "   FROM \"RptRelationSelf\" RS ";
		sql += "   LEFT JOIN \"RptSubCom\" RSC ON RSC.\"CusSCD\" = RS.\"CusSCD\" ";
		sql += "   WHERE RS.\"CusSCD\" != '1' ";
		sql += "     AND RS.\"STSCD\" IN ('1','2','3','4','5','6','7','8','11','13','14','15','16','17','18','19','28','48','49') ";
		// -- RE201203455(13副董事長,14獨立董事,15獨立監察人,16關係企業)
		sql += "     AND RS.\"CusCCD\" IN ('1','3') ";
		sql += " ) S0 ";
		sql += " LEFT JOIN ( ";
		sql += "   SELECT CM.\"CustId\" ";
		sql += "        , FAC.\"CustNo\" ";
		sql += "        , FAC.\"FacmNo\" ";
		sql += "        , CLM.\"ClCode1\" ";
		sql += "        , CLM.\"ClCode2\" ";
		sql += "        , CLM.\"ClNo\" ";
		sql += "        , CASE ";
		sql += "            WHEN CLM.\"ClCode1\" IN (1,2) "; // -- 不動產
		sql += "            THEN CASE "; // -- 鑑價金額以評估淨值優先，其次取鑑估總值
		sql += "                   WHEN NVL(CS.\"EvaNetWorth\",0) > 0 THEN NVL(CS.\"EvaNetWorth\",0)  ";
		sql += "                 ELSE NVL(CLM.\"EvaAmt\",0) END ";
		sql += "            WHEN CLM.\"ClCode1\" IN (3,4)  ";// -- 股票
		sql += "            THEN CS.\"SettingBalance\" * CS.\"YdClosingPrice\" ";
		sql += "          ELSE NVL(CLM.\"EvaAmt\",0) END   AS \"EvaAmt\" ";
		sql += "        , NVL(LBM.\"DrawdownDate\",0)      AS \"DrawdownDate\" ";
		sql += "        , NVL(LBM.\"MaturityDate\",0)      AS \"MaturityDate\" ";
		sql += "        , NVL(LBM.\"StoreRate\",0)         AS \"StoreRate\" ";
		// -- 核貸成數 2021-08-12 與舜雯電話討論後 要改用放款餘額/評估淨值計算出當下的貸放成數
		// -- , NVL(IMM.\"LoanToValue\",0) AS \"LoanToValue\" ";
		sql += "        , NVL(LBM.\"LoanBal\",0)           AS \"LoanBal\" ";
		sql += "   FROM \"CustMain\" CM ";
		sql += "   LEFT JOIN \"FacMain\" FAC ON FAC.\"CustNo\" = CM.\"CustNo\" ";
		sql += "   LEFT JOIN ( SELECT \"CustNo\" ";
		sql += "                    , \"FacmNo\" ";
		sql += "                    , MAX(\"StoreRate\")    AS \"StoreRate\" ";
		sql += "                    , MIN(\"DrawdownDate\") AS \"DrawdownDate\" ";
		sql += "                    , MAX(\"MaturityDate\") AS \"MaturityDate\" ";
		sql += "                    , SUM(\"LoanBal\")      AS \"LoanBal\" ";
		sql += "               FROM \"LoanBorMain\" ";
		sql += "               WHERE \"LoanBal\" > 0 ";
		sql += "               GROUP BY \"CustNo\" ";
		sql += "                      , \"FacmNo\" ";
		sql += "             ) LBM ON LBM.\"CustNo\" = FAC.\"CustNo\" ";
		sql += "                  AND LBM.\"FacmNo\" = FAC.\"FacmNo\" ";
		sql += "   LEFT JOIN \"ClFac\" CF ON CF.\"CustNo\" = FAC.\"CustNo\" ";
		sql += "                       AND CF.\"FacmNo\" = FAC.\"FacmNo\" ";
		sql += "                       AND CF.\"MainFlag\" = 'Y' ";
		sql += "   LEFT JOIN \"ClMain\" CLM ON CLM.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                         AND CLM.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                         AND CLM.\"ClNo\" = CF.\"ClNo\" ";
		sql += "   LEFT JOIN \"CFSum\" CS ON CS.\"CustNo\" = FAC.\"CustNo\" ";
		sql += "                         AND CS.\"FacmNo\" = FAC.\"FacmNo\" ";
		sql += "   LEFT JOIN \"ClStock\" CS ON CS.\"ClCode1\" = CF.\"ClCode1\" ";
		sql += "                           AND CS.\"ClCode2\" = CF.\"ClCode2\" ";
		sql += "                           AND CS.\"ClNo\" = CF.\"ClNo\" ";
		sql += "   WHERE LBM.\"LoanBal\" > 0 ";
		sql += " ) S1 ON S1.\"CustId\" = TRIM(S0.\"CusID\") ";
		sql += " LEFT JOIN ( ";
		sql += "      SELECT CF1.\"ClCode1\" ";
		sql += "           , CF1.\"ClCode2\" ";
		sql += "           , CF1.\"ClNo\" ";
		sql += "           , CF1.\"CustNo\" ";
		sql += "           , CF1.\"FacmNo\" ";
		sql += "           , MAX(NVL(CF2.\"FacmNo\",0)) AS \"SameCollateralDifferentFacmNo\" ";
		sql += "      FROM \"ClFac\" CF1 ";
		sql += "      LEFT JOIN \"ClFac\" CF2 ON CF2.\"ClCode1\" = CF1.\"ClCode1\" ";
		sql += "                           AND CF2.\"ClCode2\" = CF1.\"ClCode2\" ";
		sql += "                           AND CF2.\"ClNo\" = CF1.\"ClNo\" ";
		sql += "                           AND CF2.\"MainFlag\" = 'Y' ";
		sql += "                           AND CF2.\"CustNo\" = CF1.\"CustNo\" ";
		sql += "                           AND CF2.\"FacmNo\" <> CF1.\"FacmNo\" ";
		sql += "      WHERE CF1.\"MainFlag\" = 'Y' ";
		sql += "      GROUP BY CF1.\"ClCode1\" ";
		sql += "             , CF1.\"ClCode2\" ";
		sql += "             , CF1.\"ClNo\" ";
		sql += "             , CF1.\"CustNo\" ";
		sql += "             , CF1.\"FacmNo\" ";
		sql += " ) S2 ON S2.\"CustNo\" = S1.\"CustNo\" ";
		sql += "     AND S2.\"FacmNo\" = S1.\"FacmNo\" ";
		sql += "     AND S2.\"ClCode1\" = S1.\"ClCode1\" ";
		sql += "     AND S2.\"ClCode2\" = S1.\"ClCode2\" ";
		sql += "     AND S2.\"ClNo\" = S1.\"ClNo\" ";
		sql += " WHERE S1.\"CustNo\" > 0 ";
		sql += " ORDER BY S0.\"CusType\" ";
		sql += "        , S0.\"CusSCD\" ";
		sql += "        , S0.\"CusID\" ";
		sql += "        , S1.\"FacmNo\" ";

		this.info("sql=" + sql);

		Query query;
		EntityManager em = this.baseEntityManager.getCurrentEntityManager(titaVo);
		query = em.createNativeQuery(sql);
		return this.convertToMap(query);
	}

}