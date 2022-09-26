package com.st1.itx.trade.L4;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.InsuOrignal;
import com.st1.itx.db.domain.InsuRenew;
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
		String iRimEndoInsuNo = titaVo.getParam("RimEndoInsuNo"); // 批單
		String iPrevInsuNo = titaVo.getParam("RimPrevInsuNo"); // 保單號碼

		if ("".equals(iRimEndoInsuNo)) {
			iRimEndoInsuNo = " ";
		}

		BigDecimal iFireInsuCovrg = new BigDecimal("0");
		BigDecimal iEthqInsuCovrg = new BigDecimal("0");
		BigDecimal iFireInsuPrem = new BigDecimal("0");
		BigDecimal iEthqInsuPrem = new BigDecimal("0");
		Boolean isFind = false;
//		checkOrignal();

		// 新保
		Slice<InsuOrignal> slInsuOrignal = insuOrignalService.findOrigInsuNoEq(iClCode1, iClCode2, iClNo, iPrevInsuNo,
				0, Integer.MAX_VALUE, titaVo);

		if (slInsuOrignal != null) {
			for (InsuOrignal t : slInsuOrignal.getContent()) {
				iFireInsuCovrg = iFireInsuCovrg.add(t.getFireInsuCovrg());
				iEthqInsuCovrg = iEthqInsuCovrg.add(t.getEthqInsuCovrg());
				iFireInsuPrem = iFireInsuPrem.add(t.getFireInsuPrem());
				iEthqInsuPrem = iEthqInsuPrem.add(t.getEthqInsuPrem());
			}
			isFind = true;
		}
		// 續保
		if (!isFind) {
			Slice<InsuRenew> slInsuRenew = insuRenewService.findNowInsuNoEq(iClCode1, iClCode2, iClNo, iPrevInsuNo, 0,
					Integer.MAX_VALUE, titaVo);
			if (slInsuRenew != null) {
				iFireInsuCovrg = slInsuRenew.getContent().get(0).getFireInsuCovrg();
				iEthqInsuCovrg = slInsuRenew.getContent().get(0).getEthqInsuCovrg();
				iFireInsuPrem = slInsuRenew.getContent().get(0).getFireInsuPrem();
				iEthqInsuPrem = slInsuRenew.getContent().get(0).getEthqInsuPrem();
				isFind = true;
			}
		}

		if (!isFind) {
			throw new LogicException("E0001", "原保單號碼"); // 查詢資料不存在
		}
		int seq = 0;
		Slice<InsuRenew> slInsuRenew = insuRenewService.findPrevInsuNoEq(iClCode1, iClCode2, iClNo, iPrevInsuNo, 0,
				Integer.MAX_VALUE, titaVo);
		if (slInsuRenew != null) {
			for (InsuRenew t : slInsuRenew.getContent()) {
				if (t.getRenewCode() == 2 && parse.isNumeric(t.getEndoInsuNo())) {
					int endoInsuNo = parse.stringToInteger(t.getEndoInsuNo());
					if (endoInsuNo < 9 && endoInsuNo > seq) {
						seq = endoInsuNo;
					}
				}
			}
			seq = seq + 1;
		}

		this.totaVo.putParam("L4r26EndoInsuNo", " ");
		if (seq != 0) {
			this.totaVo.putParam("L4r26EndoInsuNo", seq);
		}
		this.totaVo.putParam("L4r26FireInsuCovrg", iFireInsuCovrg);
		this.totaVo.putParam("L4r26EthqInsuCovrg", iEthqInsuCovrg);
		this.totaVo.putParam("L4r26FireInsuPrem", iFireInsuPrem);
		this.totaVo.putParam("L4r26EthqInsuPrem", iEthqInsuPrem);

		this.addList(this.totaVo);
		return this.sendList();
	}

	public void doRptA(TitaVo titaVo) throws LogicException {

	}
}