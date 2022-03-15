package com.st1.itx.trade.batch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.domain.LoanCheque;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.db.service.LoanChequeService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.BatchBase;
import com.st1.itx.util.MySpring;

@Service("L9DailyReport")
@Scope("step")
/**
 * (日終批次) 發動需預設參數的日報表
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
public class L9DailyReport extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LoanChequeService sLoanChequeService;

	@Autowired
	CdWorkMonthService sCdWorkMonthService;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.titaVo.putParam(ContentName.empnot, "BAT001");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		// 第二個參數
		// D=日批
		// M=月批
		return this.exec(contribution, "D");
	}

	@Override
	public void run() throws LogicException {
		this.info("active L9DailyReport");

		// 帳務日
		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();

		this.info("L9DailyReport tbsdyf : " + tbsdyf);

		// 需要傳入預設參數的才需要加在這裡
		// LD004 企金戶還本收據及繳息收據
		execLD004(tbsdyf);

		// LD005 暫收支票收據列印（個人戶）
		execLD005(tbsdyf);

		// LD006 三階放款明細統計（T9410051）
		execLD006(tbsdyf);

		// LD007 放款專員明細統計（T9410052）
		execLD007(tbsdyf);

		// LD008 放款餘額總表
		execLD008(tbsdyf);

		this.info("L9DailyReport exit.");
	}

	/**
	 * LD004 企金戶還本收據及繳息收據
	 * 
	 * @param tbsdyf 帳務日
	 * @throws LogicException
	 */
	private void execLD004(int tbsdyf) throws LogicException {

		TitaVo tmpTitaVo = (TitaVo) this.titaVo.clone();

		tmpTitaVo.putParam("inputAcDate", tbsdyf);
		tmpTitaVo.putParam("inputSlipNoStart", 0);
		tmpTitaVo.putParam("inputSlipNoEnd", 999999);
		tmpTitaVo.putParam("inputTitaTxtNoStart", 0);
		tmpTitaVo.putParam("inputTitaTxtNoEnd", 99999999);

		// 各產一次 1:還本收據;2:繳息收據
		tmpTitaVo.putParam("inputOption", 1);

		MySpring.newTask("LD004p", this.txBuffer, tmpTitaVo);

		// 各產一次 1:還本收據;2:繳息收據
		tmpTitaVo.putParam("inputOption", 2);

		MySpring.newTask("LD004p", this.txBuffer, tmpTitaVo);
	}

	/**
	 * LD005 暫收支票收據列印（個人戶）
	 * 
	 * @param tbsdyf 帳務日
	 * @throws LogicException
	 */
	private void execLD005(int tbsdyf) throws LogicException {

		List<String> statusCode = new ArrayList<>();

		statusCode.add("0"); // 未處理
		statusCode.add("1"); // 兌現入帳
		statusCode.add("2"); // 退票
		statusCode.add("3"); // 抽票
		statusCode.add("4"); // 兌現未入帳
		statusCode.add("5"); // 即期票

		Slice<LoanCheque> sLoanCheque = sLoanChequeService.receiveDateRange(tbsdyf, tbsdyf, statusCode, 0, Integer.MAX_VALUE, titaVo);

		List<LoanCheque> listLoanCheque = sLoanCheque == null ? new ArrayList<>() : new ArrayList<>(sLoanCheque.getContent());

		Map<Integer, Integer> custNoMap = new HashMap<>();

		for (LoanCheque loanCheque : listLoanCheque) {

			int custNo = loanCheque.getCustNo();

			if (!custNoMap.containsKey(custNo)) {
				TitaVo tmpTitaVo = (TitaVo) this.titaVo.clone();

				tmpTitaVo.putParam("inputCustNo", custNo);
				tmpTitaVo.putParam("inputDate", tbsdyf);

				MySpring.newTask("LD005p", this.txBuffer, tmpTitaVo);

				custNoMap.put(custNo, 0);
			}
		}
	}

	/**
	 * LD006 三階放款明細統計（T9410051）
	 * 
	 * @param tbsdyf 帳務日
	 * @throws LogicException
	 */
	private void execLD006(int tbsdyf) throws LogicException {

		CdWorkMonth cdWorkMonth = sCdWorkMonthService.findDateFirst(tbsdyf, tbsdyf, titaVo);

		int year = cdWorkMonth.getYear();
		int month = cdWorkMonth.getMonth();

		TitaVo tmpTitaVo = (TitaVo) this.titaVo.clone();

		tmpTitaVo.putParam("inputYearStart", year);
		tmpTitaVo.putParam("inputYearEnd", year);
		tmpTitaVo.putParam("inputMonthStart", month);
		tmpTitaVo.putParam("inputMonthEnd", month);

		MySpring.newTask("LD006p", this.txBuffer, tmpTitaVo);
	}

	/**
	 * LD007 放款專員明細統計（T9410052）
	 * 
	 * @param tbsdyf 帳務日
	 * @throws LogicException
	 */
	private void execLD007(int tbsdyf) throws LogicException {

		CdWorkMonth cdWorkMonth = sCdWorkMonthService.findDateFirst(tbsdyf, tbsdyf, titaVo);

		int year = cdWorkMonth.getYear();
		int month = cdWorkMonth.getMonth();

		TitaVo tmpTitaVo = (TitaVo) this.titaVo.clone();

		tmpTitaVo.putParam("inputYearStart", year);
		tmpTitaVo.putParam("inputYearEnd", year);
		tmpTitaVo.putParam("inputMonthStart", month);
		tmpTitaVo.putParam("inputMonthEnd", month);

		MySpring.newTask("LD007p", this.txBuffer, tmpTitaVo);
	}

	/**
	 * LD008 放款餘額總表
	 * 
	 * @param tbsdyf 帳務日
	 * @throws LogicException
	 */
	private void execLD008(int tbsdyf) throws LogicException {

		TitaVo tmpTitaVo = (TitaVo) this.titaVo.clone();

		// 各產一張 0:放款餘額總表;1:關係人放款餘額總表
		tmpTitaVo.putParam("inputShowType", 0);

		MySpring.newTask("LD008p", this.txBuffer, tmpTitaVo);

		// 各產一張 0:放款餘額總表;1:關係人放款餘額總表
		tmpTitaVo.putParam("inputShowType", 1);

		MySpring.newTask("LD008p", this.txBuffer, tmpTitaVo);
	}
}