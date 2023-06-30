package com.example.boardExe.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Board {
	private int num;				//게시글번호
	private String subject;			//제목
	private String name;			//작성자
	private String phone;			//전화번호
	private String sns;				//sns 아이디
	private String hikingDate;		//산행일
	private String course;			//산행코스
	private String content;			//내용
	private String pwd;				//비밀번호
	private String regDate;			//등록일
	private int hitCount;			//조회수
}
