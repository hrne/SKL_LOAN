package com.st1.ifx.file.item.rim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.st1.ifx.file.item.CUDItemWriter;
import com.st1.ifx.service.LocalRimService;

@Component
public class RimLineItemWriter implements CUDItemWriter<RimLine> {
	private LocalRimService localRimService;

	@Autowired
	public void setLocalRimService(LocalRimService localRimService) {
		this.localRimService = localRimService;
	}

	@Override
	public void write(RimLine r) {
		this.localRimService.updateByFlag(r);
	}

}
