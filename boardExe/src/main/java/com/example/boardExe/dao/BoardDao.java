package com.example.boardExe.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.boardExe.dto.Board;

@Mapper
public interface BoardDao {
	
	public int maxNum() throws Exception;
	
	public void insertData(Board board) throws Exception;
	
	public int getDataCount(String searchKey, String searchValue) throws Exception;
	
	public List<Board> getLists(String searchKey, String searchValue, int start, int end) throws Exception;
	
	public void updateHitCount(int num) throws Exception;
	
	public Board getReadData(int num) throws Exception;
	
	public void updateData(Board board) throws Exception;
	
	public void deleteData(int num) throws Exception;
	
	
}
