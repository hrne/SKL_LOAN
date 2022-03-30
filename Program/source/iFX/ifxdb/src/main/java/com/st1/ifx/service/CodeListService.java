package com.st1.ifx.service;

import java.util.List;
import java.util.Map;

import com.st1.ifx.domain.CodeList;
import com.st1.ifx.etc.Pair;
import com.st1.ifx.file.item.general.GeneralHLine;

public interface CodeListService {
	public CodeList save(CodeList codeList);

	public void removeHelp(String help);

	public void removeSegment(String help, String segment);

	public void removeKey(String help, String segment, String key);

	public List<CodeList> findBySegment(String help, String segment);

	public CodeList findByKey(String help, String segment, String key);

	public void updateList(List<GeneralHLine> list);

	public void updateByFlag(GeneralHLine generalHLine);

	@SuppressWarnings("rawtypes")
	public List getBySegment(String help, String segment);

	public Map<String, List<String>> getBySegmentAsMap(String help, String segment);

	public List<Pair<String, String>> findAllSegments();

	public String buildJS();

	public String buildJS_noCache(boolean pretty);

	public void evict();

	public int countSegment(String help, String segment);
}
