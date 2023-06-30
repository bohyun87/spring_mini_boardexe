package com.example.boardExe.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.boardExe.dto.Board;
import com.example.boardExe.service.BoardService;
import com.example.boardExe.util.MyUtil;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	MyUtil myUtil;
	
	@RequestMapping(value = "/")
	public String index() {
		return "/index";
	}
	
	//게시글 작성 페이지 보여주는 메소드
	@RequestMapping(value = "/created", method = RequestMethod.GET)
	public String created() {
		return "bbs/created";
	}
	
	
	//게시글 작성 후 등록하는 메소드
	@RequestMapping(value = "/created", method = RequestMethod.POST)
	public String createdOK(Board board, HttpServletRequest request, Model model) {
		
		try {
			int maxNum = boardService.maxNum();
			board.setNum(maxNum + 1);
			
			boardService.insertData(board);
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "게시글 작성 중 에러가 발생했습니다.");
			return "bbs/created";
		}		
		return "redirect:/list";
	}
	
	
	//게시판 목록 보여주는 메소드
	@RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST} )
	public String list(Board board, HttpServletRequest request, Model model) {
		
		try {
			
		String pageNum = request.getParameter("pageNum");
		
		int currentPage = 1;
		
		if(pageNum != null) currentPage = Integer.parseInt(pageNum);
		
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		if(searchValue == null) {
			searchKey = "subject";
			searchValue = "";
		} else {
			if(request.getMethod().equalsIgnoreCase("GET")) {
								
				searchValue = URLDecoder.decode(searchValue, "UTF-8");				
			}
		}
		
		//1. 전체 게시물의 갯수 가져오기
		int dataCount = boardService.getDataCount(searchKey, searchValue);		
		
		//2. 페이징 처리(준비)
		int numPerPage = 5;
		int totalPage = myUtil.getPageCount(numPerPage, dataCount);
		
		if(currentPage > totalPage) currentPage = totalPage ;
		
		int start = (currentPage - 1) * numPerPage + 1;
		int end = currentPage * numPerPage;
		
		//3.전체 게시물 리스트 가져오기
		List<Board> lists = boardService.getLists(searchKey, searchValue, start, end);
		
		//4.페이징 처리
			String param = "";
			
			if(searchValue != null && !searchValue.equals("")) {
				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); 
			}
		
			String listUrl = "/list";
			
			if(!param.equals("")) listUrl += "?" + param;
			
			String pageIndexList = myUtil.pageIndexList(currentPage, totalPage, listUrl);
			String articleUrl = "/article?pageNum=" + currentPage;
			
			if(!param.equals("")) {
				articleUrl += "&" + param;
			}
			
			model.addAttribute("lists", lists);
			model.addAttribute("articleUrl", articleUrl);
			model.addAttribute("pageIndexList", pageIndexList);
			model.addAttribute("dataCount", dataCount);
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "리스트를 불러오는 중 에러가 발생했습니다.");
		}
		
		return "bbs/list";
	}
	
	
	//게시글 상세페이지 보여주는 메소드
	@RequestMapping(value = "/article", method = RequestMethod.GET)
	public String article(HttpServletRequest request, Model model) {
		
		try {
			int num = Integer.parseInt(request.getParameter("num"));
			String pageNum = request.getParameter("pageNum");
			String searchKey = request.getParameter("searchKey");
			String searchValue = request.getParameter("searchValue");
			
			if(searchValue != null) {
				searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}
			
			
			//1. 조회수 늘리기
			boardService.updateHitCount(num);			
			
			//2. 게시물 데이터 가져오기
			Board board = boardService.getReadData(num);
			
			if(board == null) {
				return "redirect:/list?pageNum=" + pageNum;
			}
			
			String param = "pageNum=" + pageNum;
			
			if(searchValue != null && !searchValue.equals("")) {
				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); 
			}
			
			model.addAttribute("board", board);
			model.addAttribute("params", param);
			model.addAttribute("pageNum", pageNum);			
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "게시글을 불러오는 중 에러가 발생했습니다.");
		}
			
		return "bbs/article";
	}
	
	
	//게시글 수정하는 페이지 보여주는 메소드
	@RequestMapping(value = "/updated", method = RequestMethod.GET)
	public String updated(HttpServletRequest request, Model model) {
		try {
			
			int num = Integer.parseInt(request.getParameter("num"));
			String pageNum = request.getParameter("pageNum");
			String searchKey = request.getParameter("searchKey");
			String searchValue = request.getParameter("searchValue");
			System.out.println(num);
			System.out.println(pageNum);
			System.out.println(searchKey);
			System.out.println(searchValue);
			if(searchValue != null) {
				searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}
			
			//전체 게시판 정보 가져오기
			Board board = boardService.getReadData(num);
			
			if(board == null) {
				return "redirect:/list?pageNum=" + pageNum;
			}
			
			String param = "pageNum=" + pageNum;
			
			if(searchValue != null && !searchValue.equals("")) {
				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); 
			}
			
			model.addAttribute("board", board);
			model.addAttribute("pageNum", pageNum);
			model.addAttribute("params", param);
			model.addAttribute("searchKey", searchKey);
			model.addAttribute("searchValue", searchValue);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "bbs/updated";
	}
	
	
	//게시글 수정 후 등록하는 메소드
	@RequestMapping(value = "/updated_ok", method = RequestMethod.POST)
	public String updatedOK(Board board, HttpServletRequest request, Model model) {
		
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		String param = "?pageNum=" + pageNum;		
		try {
			board.setContent(board.getContent().replaceAll("<br/>", "\r\n"));
			boardService.updateData(board);
			
			if(searchValue != null && !searchValue.equals("")) {
				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
			try {
				param += "&errorMessage=" + URLEncoder.encode("게시글 수정 중 에러가 발생했습니다.", "UTF-8");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}		
		return "redirect:/list" + param;
	}
	
	
	//게시글 삭제하는 메소드
	@RequestMapping(value = "/deleted_ok", method = RequestMethod.GET)
	public String deleteOK(HttpServletRequest request, Model model) {
		
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		String param = "?pageNum=" + pageNum;
		
		try {
			boardService.deleteData(num);
			
			if(searchValue != null && !searchValue.equals("")) {
				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
			try {
				param += "&errorMessage=" + URLEncoder.encode("게시글 삭제 중 에러가 발생했습니다.", "UTF-8");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
		return "redirect:/list" + param;
	}
}
