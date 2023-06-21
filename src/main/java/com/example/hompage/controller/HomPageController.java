package com.example.hompage.controller;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.hompage.dto.HomPage;
import com.example.hompage.service.HomPageService;
import com.example.hompage.util.MyUtil;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class HomPageController {
	@Autowired
	private HomPageService homPageService;
	
	@Autowired
	MyUtil myUtil;
	@RequestMapping(value = "/") // localhost로 접속
	public String index() {
		return "index";
	}
	@RequestMapping(value = "/created", method = RequestMethod.GET) // localhost로 접속
	public String created() {
		return "bbs/created";
	}
	@RequestMapping(value = "/created", method = RequestMethod.POST) // localhost로 접속
	public String createdOK(HomPage hompage, HttpServletRequest request, Model model) {
		try {
			int maxNum = homPageService.maxNum();

			hompage.setNum(maxNum + 1); // num컬럼에 들어갈 값을 1 증가시켜준다.
			hompage.setIpAddr(request.getRemoteAddr()); // 클라이언트의 ip주소를 구해준다
			homPageService.insertData(hompage);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "게시글을 불러오는중 에러가 발생했습니다.");
			return "bbs/created";
		}
		return "redirect:/list";
	}
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST }) // localhost로 접속
	public String list(HomPage hompage, HttpServletRequest request, Model model) {

		try {

			String pageNum = request.getParameter("pageNum"); // 바뀌는 페이지 번호

			int currentPage = 1; // 현재 페이지 번호(디폴트는 1)

			if (pageNum != null)
				currentPage = Integer.parseInt(pageNum);

			String searchKey = request.getParameter("searchKey"); // 검색 키워드
			String searchValue = request.getParameter("searchValue"); // 검색어

			if (searchValue == null) {
				searchKey = "subject"; // 검색 키워드의 디폴트는 subject
				searchValue = ""; // 검색어의 디폴트는 빈문자열
			} else {
				if (request.getMethod().equalsIgnoreCase("GET")) {
					// get 방식으로 request가 왔다면
					// 쿼리 파라메터의 값(searchValue)을 디코딩해준다.
					searchValue = URLDecoder.decode(searchValue, "UTF-8");

				}
			}

			// 1. 전체 게시물의 갯수를 가져온다. (페이징 처리에 필요)
			int dataCount = homPageService.getDataCount(searchKey, searchValue);

			// 2. 페이징 처리를 한다(준비 단계)
			int numPerPage = 5; // 페이지당 보여줄 데이터의 갯수
			int totalPage = myUtil.getPageCount(numPerPage, dataCount); // 페이지의 전체 갯수를 구한다

			if (currentPage > totalPage)
				currentPage = totalPage; // totalPage보다 크면 안된다

			int start = (currentPage - 1) * numPerPage + 1; // 1 6 11 16 ....
			int end = currentPage * numPerPage; // 5 10 15 20..

			// 3. 전체 게시물 리스트를 가져온다.
			List<HomPage> lists = homPageService.getLists(searchKey, searchValue, start, end);

			// 4. 페이징 처리를 한다
			String param = "";

			if (searchValue != null && !searchValue.equals("")) {
				// 검색어가 있다면
				param = "searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); // 컴퓨터의 언어
			}

			String listUrl = "/list";

			// /list?searchKey=name&searchValue=춘식
			if (!param.equals(""))
				listUrl += "?" + param;

			// ◀이전 1 2 3 4 5 다음▶
			String pageIndexList = myUtil.pageIndexList(currentPage, totalPage, listUrl);

			String articleUrl = "article?pageNum=" + currentPage;

			if (!param.equals("")) {
				articleUrl += "&" + param;
				// article?pageNum=1&searchKey=subject&searchValue=춘식
			}

			model.addAttribute("lists", lists); // DB에서 가져온 전체 게시물
			model.addAttribute("articleUrl", articleUrl); // 상세페이지로 이동하기 위한 url
			model.addAttribute("pageIndexList", pageIndexList); // ◀이전 1 2 3 4 5
			model.addAttribute("dataCount", dataCount); // 전체 게시물의 갯수

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "게시글을 불러오는중 에러가 발생했습니다.");
		}

		return "bbs/list";
	}
	@RequestMapping(value = "/article", method = RequestMethod.GET) // localhost로 접속
	public String article(HttpServletRequest request, Model model) {
		try {
			int num = Integer.parseInt(request.getParameter("num"));
			String pageNum = request.getParameter("pageNum");
			String searchKey = request.getParameter("searchKey");
			String searchValue = request.getParameter("searchValue");

			if (searchValue != null) {
				searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}

			// 1. 조회수 늘리기
			homPageService.updateHitCount(num);

			// 2. 게시물 데이터 가져오기
			HomPage hompage = homPageService.getReadData(num);

			if (hompage == null) {
				return "redirect:/list?pageNum=" + pageNum;
			}

			// 게시글의 라인수를 구한다
			int lineSu = hompage.getContent().split("\n").length;

			String param = "pageNum=" + pageNum;

			if (searchValue != null && !searchValue.equals("")) {
				// 검색어가 있다면
				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); // 컴퓨터의 언어
			}

			model.addAttribute("hompage", hompage);
			model.addAttribute("params", param);
			model.addAttribute("lineSu", lineSu);
			model.addAttribute("pageNum", pageNum);

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "게시글을 불러오는중 에러가 발생했습니다.");

		}
		return "bbs/article";
	}
}
