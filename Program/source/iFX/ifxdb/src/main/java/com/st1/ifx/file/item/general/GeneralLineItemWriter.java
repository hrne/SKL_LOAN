package com.st1.ifx.file.item.general;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.st1.ifx.domain.CodeList;
import com.st1.ifx.file.item.CUDItemWriter;
import com.st1.ifx.service.CodeListService;

@Component
public class GeneralLineItemWriter implements CUDItemWriter<GeneralLine> {
	private static final Logger logger = LoggerFactory.getLogger(GeneralLineItemWriter.class);
	private CodeListService codeListService;

	@Autowired
	public void setCodeListService(CodeListService codeListService) {
		this.codeListService = codeListService;
	}

	@Override
	public void write(GeneralLine g) {
		logger.info("==>" + g);
		logger.info("==opfg>" + g.opfg.isEmpty());
		logger.info("==key>" + g.ki.isEmpty());
		if (g.isCommandLine()) {
			logger.info("in isCommandLine!");
			if (g.opfg.equals("9")) {
				if (g.getHelp().equals(g.getId()))
					codeListService.removeHelp(g.getHelp());
				else { // remove segment
					codeListService.removeSegment(g.getHelp(), g.getId());
				}
			}
		} else {
			if (g.opfg.equals("9")) {
				codeListService.removeKey(g.help, g.id, g.ki);
			} else {
				codeListService.save(ConvertToCodeList(g));
			}
		}
	}

	private CodeList ConvertToCodeList(GeneralLine g) {
		CodeList c = new CodeList();
		c.setHelp(g.getHelp());
		c.setSegment(g.getId());
		c.setXey(g.getKi());
		c.setContent(g.getContent());
		c.setDisplayOrder(g.getDisplayOrder());
		return c;
	}
}
