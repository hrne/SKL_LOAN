package com.st1.itx.trade.LP;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LP001ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;

@Component("LP001Report1")
@Scope("prototype")

public class LP001Report1 extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LP001Report1.class);

	@Autowired
	public LP001ServiceImpl LP001ServiceImpl;

	@Autowired
	DateUtil dDateUtil;

	@Override
	public void printHeader() {

		// 明細起始列(自訂亦必須)
		this.setBeginRow(6);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(50);

	}

//	public void printHeaderL() {
//		}
	@Override
	public void printTitle() {
		this.setFontSize(8);
		this.print(1, 140, "機密等級:密");
		this.print(1, 3, "108年第1~5工作月 放款區域中心業績累計明細表");

	}

	public void exec(TitaVo titaVo) throws LogicException {

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LP001", "工作月區域中心業績累計", "密", "", "L");

		try {
//			List<Map<String, String>> LP001List = LP001ServiceImpl.findAll();				
			this.print(1, 3, "┌──┬──┬──────────┬──────────┬──────────┬──────────┬──────────┬──────────┬──────────┐");
			this.print(1, 3, "│　　│　　│　　第１工作月　　　│　　第２工作月　　　│　　第３工作月　　　│　　第４工作月　　　│　　第５工作月　　　│　　第６工作月　　　│　　第７工作月　　　│");
			this.print(1, 3, "│區域│經理├──┬───────┼──┬───────┼──┬───────┼──┬───────┼──┬───────┼──┬───────┼──┬───────┤");
			this.print(1, 3, "│　　│　　│房貸│　　房貸　　　│房貸│　　房貸　　　│房貸│　　房貸　　　│房貸│　　房貸　　　│房貸│　　房貸　　　│房貸│　　房貸　　　│房貸│　　房貸　　　│");
			this.print(1, 3, "│　　│　　│件數│　撥款金額　　│件數│　撥款金額　　│件數│　撥款金額　　│件數│　撥款金額　　│件數│　撥款金額　　│件數│　撥款金額　　│件數│　撥款金額　　│");
			this.print(1, 3, "├──┼──┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┤");
			this.print(1, 3, "│　　│　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│");
			this.print(1, 3, "│北部│　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│");
			this.print(0, 3, "              36    123,947,891   42    113,800,222   41   113,064,062   42    104,329,317   29     95,634,846");
			this.print(1, 3, "│　　│　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│");
			this.print(1, 3, "├──┼──┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┤");
			this.print(1, 3, "│　　│　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│");
			this.print(1, 3, "│中部│　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│");
			this.print(0, 3, "              16     46,134,385   22     59,689,081   17    57,496,534   19     58,908,955   23     43,382,644");
			this.print(1, 3, "│　　│　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│");
			this.print(1, 3, "├──┼──┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┤");
			this.print(1, 3, "│　　│　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│");
			this.print(1, 3, "│南部│　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│");
			this.print(0, 3, "              18     53,498,541   21     36,750,000   28    46,490,000   24     54,230,000   23     55,700,000");
			this.print(1, 3, "│　　│　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│");
			this.print(1, 3, "├──┴──┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┤");
			this.print(1, 3, "│　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│");
			this.print(1, 3, "│　總計　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│");
			this.print(0, 3, "              70    223,580,817   85    210,239,303   86   217,050,696   85    217,468,272   75    194,717,490  0.0              0  0.0              0");
			this.print(1, 3, "└─────┴──┴───────┴──┴───────┴──┴───────┴──┴───────┴──┴───────┴──┴───────┴──┴───────┘");

			this.print(1, 3, "");
			this.print(1, 3, "");
			this.print(1, 3, "┌──┬──┬──────────┬──────────┬──────────┬──────────┬──────────┬──────────┬──────────┐");
			this.print(1, 3, "│　　│　　│　　第８工作月　　　│　　第９工作月　　　│　　第１０工作月　　│　　第１１工作月　　│　　第１２工作月　　│　　第１３工作月　　│　第１～５工作月累計│");
			this.print(1, 3, "│區域│經理├──┬───────┼──┬───────┼──┬───────┼──┬───────┼──┬───────┼──┬───────┼──┬───────┤");
			this.print(1, 3, "│　　│　　│房貸│　　房貸　　　│房貸│　　房貸　　　│房貸│　　房貸　　　│房貸│　　房貸　　　│房貸│　　房貸　　　│房貸│　　房貸　　　│房貸│　　房貸　　　│");
			this.print(1, 3, "│　　│　　│件數│　撥款金額　　│件數│　撥款金額　　│件數│　撥款金額　　│件數│　撥款金額　　│件數│　撥款金額　　│件數│　撥款金額　　│件數│　撥款金額　　│");
			this.print(1, 3, "├──┼──┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┤");
			this.print(1, 3, "│　　│　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│");
			this.print(1, 3, "│北部│　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│");
			this.print(0, 3, "                                                                                                                                   190.0   550,776,338");
			this.print(1, 3, "│　　│　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│");
			this.print(1, 3, "├──┼──┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┤");
			this.print(1, 3, "│　　│　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│");
			this.print(1, 3, "│中部│　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│");
			this.print(0, 3, "                                                                                                                                    97.0   265,611,599");
			this.print(1, 3, "│　　│　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│");
			this.print(1, 3, "├──┼──┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┤");
			this.print(1, 3, "│　　│　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│");
			this.print(1, 3, "│南部│　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│");
			this.print(0, 3, "                                                                                                                                   114.0   246,668,541");
			this.print(1, 3, "│　　│　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│");
			this.print(1, 3, "├──┴──┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┼──┼───────┤");
			this.print(1, 3, "│　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│");
			this.print(1, 3, "│　總計　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│");
			this.print(0, 3, "             0.0              0  0.0              0  0.0              0  0.0              0  0.0              0  0.0             0 401.0 1,063,056,478 ");
			this.print(1, 3, "│　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│　　│　　　　　　　│");
			this.print(1, 3, "└─────┴──┴───────┴──┴───────┴──┴───────┴──┴───────┴──┴───────┴──┴───────┴──┴───────┘");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LD005ServiceImpl.findAll error = " + errors.toString());
		}
		long sno = this.close();
		this.toPdf(sno);
	}
}
