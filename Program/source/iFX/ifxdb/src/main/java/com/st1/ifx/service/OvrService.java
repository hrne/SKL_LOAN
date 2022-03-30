package com.st1.ifx.service;

import com.st1.ifx.domain.Ovr;

public interface OvrService {
	public Ovr get(Long id, boolean bLazy);

	public Ovr save(Ovr ovr, String content);

	public Ovr update(Long id, String supBrn, String supNo, int status, String message);

}
