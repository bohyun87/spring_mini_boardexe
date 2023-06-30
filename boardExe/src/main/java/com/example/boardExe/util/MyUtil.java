package com.example.boardExe.util;

import org.springframework.stereotype.Service;

@Service
public class MyUtil {
	
	//전체페이지 갯수 구하기
	public int getPageCount(int numPerPage, int dataCount) {
		int pageCount = 0;
		pageCount = dataCount / numPerPage;
		
		if(dataCount % numPerPage != 0) {
			pageCount++;
		}
		
		return pageCount;		
	}
	
	public String pageIndexList(int currentPage, int totalPage, String listUrl) {
		StringBuffer sb = new StringBuffer();
		int numPerBlock = 5;
		int currentPageSetup;
		int page;
		
		if(currentPage == 0 || totalPage == 0) return "";
		
		if(listUrl.indexOf("?") != -1) {
			listUrl += "&";
		} else {
			listUrl += "?";
		}
		
		// ① ◀이전 버튼 만들기
		
		currentPageSetup = (currentPage / numPerBlock) * numPerBlock;
		
		if(currentPage % numPerBlock == 0) {
			currentPageSetup = currentPageSetup - numPerBlock;
		}
		
		if(totalPage > numPerBlock && currentPageSetup > 0) {
			sb.append("<a href=\"" + listUrl + "pageNum=" + currentPageSetup + "\">◀이전</a>&nbsp");					
		}
		
		// ② 6 7 8 9 10 그냥 페이지 이동 버튼 만들기
		page = currentPageSetup + 1;
		
		while(page <= totalPage && page <= (currentPageSetup + numPerBlock)) {
			if(page == currentPage) {
				sb.append("<font color=\"red\">" + page + "</font> &nbsp;");
			} else {
				sb.append("<a href=\"" + listUrl + "pageNum=" + page + "\">" + page + "</a>&nbsp;");
			}
			
			page++;
		}
		
		
		// ③ 다음▶ 버튼 만들기
		if(totalPage - currentPageSetup > numPerBlock) {
			sb.append("<a href=\"" + listUrl + "pageNum=" + currentPageSetup + "\">다음▶</a>&nbsp" );
		}
		
		
		// ④ 버튼 합쳐서 문자열 리턴
		System.out.println(sb.toString());
		
		return sb.toString();
	}
}


