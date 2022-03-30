package com.st1.ifx.file.item.sbctl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.st1.ifx.file.item.CUDItemWriter;
import com.st1.ifx.file.item.LineHeader;
import com.st1.ifx.service.SbctlService;

@Component
public class SbctlLineItemWriter implements CUDItemWriter<SbctlLine> {
	private SbctlService service;

	@Autowired
	public void setSbctlService(@Qualifier("sbctlService") SbctlService sbctlService) {
		this.service = sbctlService;
	}

	@Override
	public void write(SbctlLine line) {
		System.out.println("## write one sbctl");
		switch (line.getHeader().getAction()) {
		case LineHeader.ACTION_MERGE:
			service.save(line.getSbctl());
			break;
		case LineHeader.ACTION_DELETE:
			service.delete(line.getSbctl());
			break;
		case LineHeader.ACTION_REMOVE_ALL:
			service.removeAll();
			break;
		default:
			break;
		}
	}
}
