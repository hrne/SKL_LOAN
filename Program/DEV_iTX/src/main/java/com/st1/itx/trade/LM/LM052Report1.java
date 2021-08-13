package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM052ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component("lM052Report1")
@Scope("prototype")

public class LM052Report1 extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LM052Report1.class);

	@Autowired
	public LM052ServiceImpl lM052ServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> LM052List = null;
		List<Map<String, String>> LM052List1 = null;
		List<Map<String, String>> LM052List2 = null;
		try {

			LM052List = lM052ServiceImpl.findAll(titaVo);
//			LM052List1 = lM052ServiceImpl.findAll1(titaVo);
//			LM052List2 = lM052ServiceImpl.findAll2(titaVo);

			if (LM052List.size() > 0) {
				testExcel(titaVo, LM052List);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM052ServiceImpl.testExcel error = " + errors.toString());
		}
	}

	private void testExcel(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		this.info("===========in testExcel");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM052", "放款資產分類-會計部備呆計提", "LM052放款資產分類案件明細表_呆提存比率1.5%", "LM052放款資產分類案件明細表_呆提存比率1.5%.xlsx", "總表");

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
