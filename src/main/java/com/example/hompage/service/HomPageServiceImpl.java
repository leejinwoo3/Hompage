package com.example.hompage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hompage.dao.HomPageDao;
import com.example.hompage.dto.HomPage;


@Service
public class HomPageServiceImpl implements HomPageService {
	@Autowired
	private HomPageDao hompageMapper;

	@Override
	public int maxNum() throws Exception {
		return hompageMapper.maxNum();
	}

	@Override
	public void insertData(HomPage hompage) throws Exception {
		hompageMapper.insertData(hompage);
	}

	@Override
	public int getDataCount(String searchKey, String searchValue) throws Exception {
		return hompageMapper.getDataCount(searchKey, searchValue);
	}

	@Override
	public List<HomPage> getLists(String searchKey, String searchValue, int start, int end) throws Exception {
		return hompageMapper.getLists(searchKey, searchValue, start, end);
	}
	@Override
	public void updateHitCount(int num) throws Exception {
		hompageMapper.updateHitCount(num);
	}

	@Override
	public HomPage getReadData(int num) throws Exception {
		return hompageMapper.getReadData(num);
	}
}
