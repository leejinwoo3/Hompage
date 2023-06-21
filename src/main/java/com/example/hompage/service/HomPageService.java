package com.example.hompage.service;

import java.util.List;

import com.example.hompage.dto.HomPage;

public interface HomPageService {

	public int maxNum() throws Exception;

	public void insertData(HomPage hompage) throws Exception;

	public int getDataCount(String searchKey, String searchValue) throws Exception;

	public List<HomPage> getLists(String searchKey, String searchValue, int start, int end) throws Exception;

	public void updateHitCount(int num) throws Exception;

	public HomPage getReadData(int num) throws Exception;



}
