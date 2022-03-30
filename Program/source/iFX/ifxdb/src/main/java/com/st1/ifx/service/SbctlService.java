package com.st1.ifx.service;

import com.st1.ifx.domain.Sbctl;

public interface SbctlService {
	public Sbctl save(Sbctl sbctl);

	public void delete(Sbctl sbctl);

	public Sbctl findById(int type, String brno, String tlrno, String sbtyp);

	public void removeAll();
}
