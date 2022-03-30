package com.st1.ifx.service;

import java.util.List;

import com.st1.ifx.domain.MsgBox;

public interface MsgService {
	public void save(List<MsgBox> msgBoxes);

	public void save(MsgBox msgBox);

	public List<MsgBox> get(String brno, String tlrno, int option, String fdate);

	public MsgBox updateMsgBox(Long id);

}
