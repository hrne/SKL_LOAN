package com.st1.itx.trade.L4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L4R14")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4R14 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4R14.class);

	/* DB服務注入 */
	@Autowired
	public AcReceivableService acReceivableService;

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R14 ");
		this.totaVo.init(titaVo);

		String acctCode = "TCK";
		int custNo = parse.stringToInteger(titaVo.getParam("RimRpCustNo"));
		String rvNo = titaVo.getParam("RimRpCheckAcctNo") + " " + titaVo.getParam("RimRpCheckNo");

		List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();

//		 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();
//		設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		Slice<AcReceivable> sAcReceivable = null;

		sAcReceivable = acReceivableService.acrvRvNoEq(acctCode, custNo, rvNo, this.index, this.limit);

		lAcReceivable = sAcReceivable == null ? null : sAcReceivable.getContent();

		if (lAcReceivable != null && lAcReceivable.size() != 0) {
			for (int i = 0; i < lAcReceivable.size(); i++) {
				if (i <= 5) {
					AcReceivable tAcReceivable = new AcReceivable();
					tAcReceivable = lAcReceivable.get(i);

					this.info("tAcReceivable : " + tAcReceivable.toString());

					int j = i + 1;

					this.totaVo.putParam("L4r14FacmNo" + j, tAcReceivable.getFacmNo());
					this.totaVo.putParam("L4r14Amt" + j, tAcReceivable.getRvAmt());
				} else {
					break;
				}
			}

//		少於5補空
			for (int j = lAcReceivable.size() + 1; j <= 5; j++) {
				this.totaVo.putParam("L4r14FacmNo" + j, 0);
				this.totaVo.putParam("L4r14Amt" + j, 0);
			}
		} else {
			throw new LogicException(titaVo, "E0001", "銷帳檔無此資料");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}