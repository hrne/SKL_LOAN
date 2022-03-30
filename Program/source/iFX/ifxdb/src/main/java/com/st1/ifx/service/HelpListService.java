package com.st1.ifx.service;

import java.util.List;
import java.util.Optional;

import com.st1.ifx.domain.HelpList;
import com.st1.ifx.etc.Pair;

public interface HelpListService {

	public HelpList save(HelpList helpList);

	public void delete(HelpList helpList);

	public Optional<HelpList> findById(Long id);

	public List<Pair<String, String>> findAllSegments();

	public String findJson(String help, String segment);

	public List<Pair<String, String>> findList(String help, String segment, String valueTag, String labelTag);

	public String[] findValues(String help, String segment, int colIndex);

	public void evict();
}
