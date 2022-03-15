package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankRelationCompany;
import com.st1.itx.db.service.BankRelationCompanyService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L2R12")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R12 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L2R12.class);

	@Autowired
	public BankRelationCompanyService bankRelationCompanyService;

	private String wkIsLimit = "N";
	private String wkIsRelated = "N";
	private String wkIsLnrelNear = "N";
	private String iCustId;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R12 ");
		this.totaVo.init(titaVo);

		// tita
		iCustId = titaVo.getParam("CustId").trim();

		BankRelationCompany tBankRelationCompany = bankRelationCompanyService.custIdFirst(iCustId, titaVo);
//		LAW001 金控法第44條       Y->利害關係人=Y
//		LAW002 金控法第44條(列項) Y->利害關係人=Y
//		LAW003 金控法第45條       Y->利害關係人=Y
//		LAW005 保險法(放款)       Y->授信限制對象=Y
//		LAW008 準利害關係人       Y->準利害關係人=Y
		if (tBankRelationCompany != null) {
			if ("Y".equals(tBankRelationCompany.getLAW005())) {
				wkIsLimit = "Y"; // 是否為授信限制對象
			}
			if ("Y".equals(tBankRelationCompany.getLAW001()) || "Y".equals(tBankRelationCompany.getLAW002()) || "Y".equals(tBankRelationCompany.getLAW003())) {
				wkIsRelated = "Y"; // 是否為利害關係人
			}
			if ("Y".equals(tBankRelationCompany.getLAW008())) {
				wkIsLnrelNear = "Y"; // 是否為準利害關係人
			}
		}

		this.totaVo.putParam("L2r12IsLimit", wkIsLimit);
		this.totaVo.putParam("L2r12IsRelated", wkIsRelated);
		this.totaVo.putParam("L2r12IsLnrelNear", wkIsLnrelNear);

		this.addList(this.totaVo);
		return this.sendList();
	}
}