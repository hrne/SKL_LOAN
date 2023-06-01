package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.PfBsDetail;
import com.st1.itx.db.domain.PfItDetail;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.PfBsDetailService;
import com.st1.itx.db.service.PfItDetailService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L5R46")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L5R46 extends TradeBuffer {

	@Autowired
	public CustMainService custMainService;

	@Autowired
	public CdEmpService cdEmpService;

	@Autowired
	public PfItDetailService pfItDetailService;

	@Autowired
	public PfBsDetailService pfBsDetailService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5R46 ");
		this.totaVo.init(titaVo);

		long iLogNo = Long.valueOf(titaVo.getParam("LogNo").trim());

		PfItDetail pfItDetail = pfItDetailService.findById(iLogNo, titaVo);

		if (pfItDetail == null) {
			throw new LogicException("E0001", "業績資料");
		} else {
			this.totaVo.putParam("CustNo", pfItDetail.getCustNo());
			this.totaVo.putParam("FacmNo", pfItDetail.getFacmNo());
			this.totaVo.putParam("BormNo", pfItDetail.getBormNo());

			CustMain custMain = custMainService.custNoFirst(pfItDetail.getCustNo(), pfItDetail.getCustNo(), titaVo);
			if (custMain == null) {
				throw new LogicException("E0001", "客戶主檔");
			}
			this.totaVo.putParam("CustName", custMain.getCustName());

			this.totaVo.putParam("LogNo", pfItDetail.getLogNo());

			this.totaVo.putParam("Introducer", pfItDetail.getIntroducer());

			if (!"".equals(pfItDetail.getIntroducer().trim())) {
				CdEmp cdEmp = cdEmpService.findById(pfItDetail.getIntroducer(), titaVo);

				if (cdEmp == null) {
					this.totaVo.putParam("IntroducerName", "");
				} else {
					this.totaVo.putParam("IntroducerName", cdEmp.getFullname());
				}

			} else {
				this.totaVo.putParam("IntroducerName", "");
			}

			PfBsDetail pfBsDetail = pfBsDetailService.findByTxFirst(pfItDetail.getCustNo(), pfItDetail.getFacmNo(),
					pfItDetail.getBormNo(), pfItDetail.getPerfDate() + 19110000, pfItDetail.getRepayType(),
					pfItDetail.getPieceCode(), titaVo);
			if (pfBsDetail == null) {
				this.totaVo.putParam("BsOfficer", "");
				this.totaVo.putParam("BsOfficerName", "");
			} else {
				this.totaVo.putParam("BsOfficer", pfBsDetail.getBsOfficer().trim());
				if (!"".equals(pfBsDetail.getBsOfficer().trim())) {
					CdEmp cdEmp = cdEmpService.findById(pfBsDetail.getBsOfficer(), titaVo);

					if (cdEmp == null) {
						this.totaVo.putParam("BsOfficerName", "");
					} else {
						this.totaVo.putParam("BsOfficerName", cdEmp.getFullname());
					}
				} else {
					this.totaVo.putParam("BsOfficerName", "");
				}
			}

			this.totaVo.putParam("PieceCode", pfItDetail.getPieceCode());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}