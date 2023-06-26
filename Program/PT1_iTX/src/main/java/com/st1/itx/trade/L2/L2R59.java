package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.AcReceivableId;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2R59")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R59 extends TradeBuffer {

	@Autowired
	public AcReceivableService acReceivableService;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R59 ");
		this.totaVo.init(titaVo);

		// tita
		int iFunCd = parse.stringToInteger(titaVo.getParam("RimFunCd"));
		String iAcctCode = titaVo.getParam("RimAcctCode");
		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		String iRvNo = titaVo.getParam("RimRvNo");

		AcReceivable tAcReceivable = acReceivableService
				.findById(new AcReceivableId(iAcctCode, iCustNo, iFacmNo, iRvNo), titaVo);

		if (tAcReceivable == null) {
			switch (iFunCd) {
			case 2:
				// 若為修改，但資料不存在，拋錯
				throw new LogicException("E0003", "會計銷帳檔");// 修改資料不存在
			case 4:
				// 若為刪除，但資料不存在，拋錯
				throw new LogicException("E0004", "會計銷帳檔");// 刪除資料不存在
			case 5:
				// 若為查詢，但資料不存在，拋錯
				throw new LogicException("E0001", "會計銷帳檔");// 查詢資料不存在
			default:
				break;
			}
		} else {
			if (iFunCd == 1) {
				// 若為新增，新增資料已存在，拋錯
				throw new LogicException("E0002", "會計銷帳檔"); // 新增資料已存在
			} else {
//				String createDate = new SimpleDateFormat("yyy/MM/dd").format(tAcReceivable.getCreateDate());
//				String createTime = new SimpleDateFormat("HH:mm:ss").format(tAcReceivable.getCreateDate());
				this.totaVo.putParam("L2r59OpenAcDate", tAcReceivable.getOpenAcDate());// 起帳日期
				this.totaVo.putParam("L2r59AcctFee", tAcReceivable.getRvAmt());// 帳管費
				this.totaVo.putParam("L2r59SlipNote", tAcReceivable.getSlipNote());// 備註
				this.totaVo.putParam("L2r59CurCode", tAcReceivable.getCurrencyCode());// 幣別

				if (tAcReceivable.getClsFlag() == 1) {
					this.totaVo.putParam("L2r59AcDate", tAcReceivable.getLastAcDate());// 會計日
					this.totaVo.putParam("L2r59TlrNo", tAcReceivable.getTitaTlrNo());// 經辦
					this.totaVo.putParam("L2r59TxtNo", parse.IntegerToString(tAcReceivable.getTitaTxtNo(), 8));// 交易序號
				} else {
					this.totaVo.putParam("L2r59AcDate", 0);// 會計日
					this.totaVo.putParam("L2r59TlrNo", "");// 經辦
					this.totaVo.putParam("L2r59TxtNo", "");// 交易序號
				}
				this.totaVo.putParam("L2r59CreateDate", DbDateToRocDate(tAcReceivable.getCreateDate().toString())); // 作業日期
				this.totaVo.putParam("L2r59CreateTime", DbDateToRocTime(tAcReceivable.getCreateDate().toString())); // 作業時間);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private String DbDateToRocDate(String DbDate) {
		String SysDateY = DbDate.substring(0, 4);
		int RocDate9 = Integer.valueOf(SysDateY) - 1911;
		String RocDate = String.valueOf(RocDate9) + DbDate.substring(5, 7) + DbDate.substring(8, 10);

		return RocDate;
	}

	private String DbDateToRocTime(String DbDate) {
		String SysTime = DbDate.substring(11, 19);

		return SysTime;
	}
}