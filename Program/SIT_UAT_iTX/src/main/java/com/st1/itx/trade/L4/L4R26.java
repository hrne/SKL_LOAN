package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.InsuOrignal;
import com.st1.itx.db.domain.InsuRenew;
import com.st1.itx.db.domain.InsuRenewId;
import com.st1.itx.db.service.InsuOrignalService;
import com.st1.itx.db.service.InsuRenewService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L4R26")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4R26 extends TradeBuffer {

	@Autowired
	public InsuOrignalService insuOrignalService;

	@Autowired
	public InsuRenewService insuRenewService;

	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R26 ");
		this.totaVo.init(titaVo);

//		L4611新增續保調rim

		int iClCode1 = parse.stringToInteger(titaVo.getParam("RimClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("RimClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("RimClNo"));
		String iPrevInsuNo = titaVo.getParam("RimPrevInsuNo");
		String iRimEndoInsuNo = titaVo.getParam("RimEndoInsuNo");

		if ("".equals(iRimEndoInsuNo)) {
			iRimEndoInsuNo = " ";
		}

		BigDecimal iFireInsuCovrg = new BigDecimal("0");
		BigDecimal iEthqInsuCovrg = new BigDecimal("0");
		BigDecimal iFireInsuPrem = new BigDecimal("0");
		BigDecimal iEthqInsuPrem = new BigDecimal("0");

		// 續保
		InsuRenew tInsuRenew = new InsuRenew();
		InsuRenewId tInsuRenewId = new InsuRenewId();
		tInsuRenewId.setClCode1(iClCode1);
		tInsuRenewId.setClCode2(iClCode2);
		tInsuRenewId.setClNo(iClNo);
		tInsuRenewId.setEndoInsuNo(iRimEndoInsuNo); // 批單
		tInsuRenewId.setPrevInsuNo(iPrevInsuNo); // 原保單號碼

		tInsuRenew = insuRenewService.findById(tInsuRenewId, titaVo);

		if (tInsuRenew != null) {
			throw new LogicException("E0012", "續約保單資料維護"); // 該筆資料已存在
		}

		// 新保
		Slice<InsuOrignal> slInsuOrignal = null;
		List<InsuOrignal> lInsuOrignal = new ArrayList<InsuOrignal>();

		slInsuOrignal = insuOrignalService.findOrigInsuNoEq(iClCode1, iClCode2, iClNo, iPrevInsuNo, index, limit, titaVo);

		lInsuOrignal = slInsuOrignal == null ? null : slInsuOrignal.getContent();

		if (lInsuOrignal != null) {
			for (InsuOrignal t : lInsuOrignal) {
				iFireInsuCovrg = iFireInsuCovrg.add(t.getFireInsuCovrg());
				iEthqInsuCovrg = iEthqInsuCovrg.add(t.getEthqInsuCovrg());
				iFireInsuPrem = iFireInsuPrem.add(t.getFireInsuPrem());
				iEthqInsuPrem = iEthqInsuPrem.add(t.getEthqInsuPrem());
			}
		} else {
			throw new LogicException("E0001", "原保單號碼"); // 查詢資料不存在
		}

		this.totaVo.putParam("L4r26FireInsuCovrg", iFireInsuCovrg);
		this.totaVo.putParam("L4r26EthqInsuCovrg", iEthqInsuCovrg);
		this.totaVo.putParam("L4r26FireInsuPrem", iFireInsuPrem);
		this.totaVo.putParam("L4r26EthqInsuPrem", iEthqInsuPrem);

		this.addList(this.totaVo);
		return this.sendList();
	}
}