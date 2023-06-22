package com.example.hompage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.hompage.dto.HomPage;

@Mapper
public interface HomPageDao {

	public int maxNum() throws Exception;

	public void insertData(HomPage hompage) throws Exception;

	public int getDataCount(String searchKey, String searchValue) throws Exception;

	public List<HomPage> getLists(String searchKey, String searchValue, int start, int end) throws Exception;

	public void updateHitCount(int num) throws Exception;

	public HomPage getReadData(int num) throws Exception;

	public void updateData(HomPage hompage) throws Exception;

	public void deleteData(int num) throws Exception;

}
