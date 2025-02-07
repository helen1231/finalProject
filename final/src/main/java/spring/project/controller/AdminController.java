package spring.project.controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import google.analytics.reportingAPI.AnalyticsService;
import spring.project.model.CertiDateDTO;
import spring.project.model.CertiInfoDTO;
import spring.project.model.CertiRequirementDTO;
import spring.project.model.CertiScheduleDTO;
import spring.project.model.EmpBoardDTO;
import spring.project.model.EmpInfoDTO;
import spring.project.model.MemberInfoDTO;
import spring.project.pagination.PagingDTO;
import spring.project.pagination.PagingService;
import spring.project.service.AdminService;
import spring.project.service.MemberService;

@Controller
@RequestMapping("/admin/*")
public class AdminController {
	
	@Autowired
	private AdminService service;
	
	@Autowired
	private MemberService memService;
	
	@Autowired
	private PagingService pageService;
	
	@Autowired
	private AnalyticsService gaService;
	
	//자격증 등록 페이지 
	@RequestMapping("addCerti")
	public String addCerti() {
		return "admin/certi/addCerti";
	}
	
	@RequestMapping("addCertiPro")
	public String addCertiPro(CertiInfoDTO info, CertiRequirementDTO requirement, Model model) {
		System.out.println("====Controller:addCertiPro");	
		System.out.println(info);
		System.out.println(requirement);
		model.addAttribute("result", service.addCertiInfo(info, requirement));
		return "admin/certi/addCertiPro";
	}
	
	//등록된 자격증 목록
	@RequestMapping("certiList")
	public String getCertiList(String pageNum, String sort, String order, String category, Model model) {
		//DI 이용한 페이징 처리 - 원하는 페이지 사이즈를 정해서 pageNum과 같이 매개변수로 보내줌
		int pageSize = 30;
		PagingDTO page = pageService.getPaging(pageSize, pageNum);
		model.addAttribute("list", service.getCertList(page, sort, order, category));
		model.addAttribute("count", service.getCertCnt(category));
		
		model.addAttribute("page",page);
		model.addAttribute("sort", sort);
		model.addAttribute("order", order);
		model.addAttribute("category", category);
		return "admin/certi/certiList";
	}
	
	//자격증 목록 - 검색기능 (검색결과 페이지)
	@RequestMapping("search")
	public String searchList(String pageNum, String search, String keyword, Model model) {
		PagingDTO page = pageService.getPaging(30, pageNum);
		model.addAttribute("page", page);
		
		model.addAttribute("search", search);
		model.addAttribute("keyword", keyword);
		model.addAttribute("count", service.getSearchCnt(search, keyword));
		model.addAttribute("list", service.getSearchList(page, search, keyword));
	
		return "/admin/certi/searchList";
	}
	
	//자격증 상세정보 페이지 - 페이지 내에서 바로 수정 가능
	@RequestMapping("certiInfo")
	public String getCertiInfo(String cnum, Model model) {
		model.addAttribute("cnum", cnum);
		model.addAttribute("info", service.getCertiInfo(cnum));
		
		List<CertiRequirementDTO> reqList = service.getCertiReqList(cnum);
		model.addAttribute("reqList", reqList);
		model.addAttribute("reqCnt", reqList.size());
		return "admin/certi/certiInfo";
	}
	
	//자격증별 상세일정 목록
	@RequestMapping("certiDate")
	public String getcertiDateInfo(String cnum, Model model) {
		List<CertiDateDTO> dateList = null;
		if(cnum.substring(0, 1).equals("N")) {
			//큐넷에서 시행하는 시험이 아닌경우 certischedule을 거치지 않고 검색
			if(!service.searchCompany(cnum).equals("한국산업인력공단")) {
				dateList = service.searchPeriod(cnum);
				model.addAttribute("isNatAdd", 1);
			}else {
				dateList = service.searchNatPeriod(cnum);
			}
			
		}else {
			dateList = service.searchPeriod(cnum);
		}
	
		model.addAttribute("dateList", dateList);
		model.addAttribute("info",service.getCertiInfo(cnum));
		
		return "admin/certi/certiDate";
	}
	
	//자격증 상세일정 추가 페이지 - Form
	@RequestMapping("certi/addDate")
	public String addDate(String cnum, Model model) {
		model.addAttribute("info", service.getCertiInfo(cnum));
		return "admin/certi/addDate";
	}
	//자격증 상세일정 등록 - Pro
	@RequestMapping("certi/addDatePro")
	public String addDate(HttpServletRequest request, String cnum, String cname, String clevel, Integer count, Model model) {
		int result = 0;
		
		//하나의 자격증에 한번에 여러개의 상세일정 등록 가능 
		for(int i=1;i<=count;i++) {
			int cyear = Integer.parseInt(request.getParameter("cyear"+i));
			int cround = Integer.parseInt(request.getParameter("cround"+i));
			CertiDateDTO dto = new CertiDateDTO(
						cnum,
						cyear,
						cround,
						clevel,
						request.getParameter("docRegStart1"+i),
						request.getParameter("docRegEnd1"+i),
						request.getParameter("docRegStart2"+i),
						request.getParameter("docRegEnd2"+i),
						request.getParameter("docTestStart"+i),
						request.getParameter("docTestEnd"+i),
						request.getParameter("docResultStart"+i),
						request.getParameter("docResultEnd"+i),
						request.getParameter("docSubmitStart"+i),
						request.getParameter("docSubmitEnd"+i),
						request.getParameter("pracRegStart1"+i),
						request.getParameter("pracRegEnd1"+i),
						request.getParameter("pracRegStart2"+i),
						request.getParameter("pracRegEnd2"+i),
						request.getParameter("pracTestStart"+i),
						request.getParameter("pracTestEnd"+i),
						request.getParameter("pracResStart"+i),
						request.getParameter("pracResEnd"+i)
					);
			result += service.addCertiDate(dto);
		}
		
		model.addAttribute("result", result);
		model.addAttribute("cnum", cnum);
		model.addAttribute("cname", cname);
		return "admin/certi/addDatePro";
	}
	
	//자격증 상세일정 form - 일정 등록 페이지에서 버튼 클릭시 상세일정form이 추가됨
	@RequestMapping("certi/addDateTbl")
	public String ajaxDateTbl(String count, Model model) {
		model.addAttribute("count", count);
		return "admin/ajax/addDateTbl";
	}
	
	//자격증 상세일정 삭제
	@RequestMapping("certi/deleteDate")
	public String deleteDate(String cnum, String[] dateList, Model model){
		//국가자격증인 경우 컨펌 
		if(cnum.startsWith("N")) {
			//국가자격증인 경우 회차정보 (CertiSchedule) 삭제
			model.addAttribute("result", service.deleteCertiNatDate(dateList, cnum));
		}else {
			//공인민간, 어학자격증 상세일정 (CertiDate) 삭제 (datePK 사용) 
			model.addAttribute("result",service.deleteCertiDate(dateList));
		}
		
		model.addAttribute("cnum", cnum);
		
		return "admin/certi/deleteDate";
	}
	
	
	@RequestMapping("certi/modDate")
	public String modifyDate(Integer datepk, String cnum, Model model) {
		if(datepk ==null) return "admin/certi/certiDate?cnum="+cnum;
		
		if(cnum.startsWith("N")) model.addAttribute("count", service.getNatSameCnt(datepk));
		model.addAttribute("dto", service.getCertiDate(datepk));
		model.addAttribute("cnum", cnum);
		return "admin/certi/modDate";
	}
	@RequestMapping("certi/sameSchedule")
	public String getSameSchedule(Integer datepk, String pageNum, Model model) {
		PagingDTO page = pageService.getPaging(20, pageNum);
		model.addAttribute("page", page);
		model.addAttribute("natList", service.getNatSameScheduleList(datepk,page));
		model.addAttribute("count", service.getNatSameCnt(datepk));
		model.addAttribute("datepk", datepk);
		return "admin/certi/sameSchedule";
	}
	
	@RequestMapping("certi/modDatePro")
	public String modifyDatePro(CertiDateDTO dto, Model model) {
		System.out.println(dto);
		model.addAttribute("result", service.modCertiDate(dto));
		model.addAttribute("cnum", dto.getCnum());
		return "admin/certi/modDatePro";
	}
	
	//자격증 삭제(update status)
	@RequestMapping("certi/deleteForm")
	public String deleteForm(String cnum, HttpSession session, Model model) {
		String empid = (String) session.getAttribute("sid");
		model.addAttribute("empInfo", service.getMemberInfo(empid));
		model.addAttribute("dto", service.getCertiInfo(cnum));
		return "admin/certi/deleteForm";
	}
	@RequestMapping("deletePro")
	public String deletePro(String cnum, String name, MemberInfoDTO dto, Model model) {
		//입력한 id가 사원인지 체크
		if(service.checkIfEmp(dto.getMemid())==1) {
			//id, pw 체크
			if(memService.userCheck(dto).getCnt()==1) {
				System.out.println(dto);
				System.out.println(memService.userCheck(dto).getCnt());
				model.addAttribute("result", service.delCerti(cnum, name));
			}
		}else {
			return "/admin/certi/certiList";
		}
		return "admin/certi/deletePro";
	}
	
	@RequestMapping("modCertiPro")
	public String modCerti(CertiInfoDTO info, Model model) {
		model.addAttribute("result", service.modCerti(info));
		model.addAttribute("cnum", info.getCnum());
		return "/admin/certi/modCertiPro";
	}
	
	@RequestMapping("member/list")
	public String getMemberList(String pageNum, Integer status, Model model) {
		PagingDTO page = pageService.getPaging(30, pageNum);
		model.addAttribute("page",page);
		model.addAttribute("count",service.getMemberCnt(status));
		model.addAttribute("list", service.getMemberList(page, status));
		model.addAttribute("status", status);
		
		if(status!=null)
			model.addAttribute("status_name", service.getMemStatusName(status));
		
		return "/admin/member/list";
	}
	
	@RequestMapping("member/info")
	public String getMemberInfo(String memid, Model model) {
		model.addAttribute("dto", service.getMemberInfo(memid));
		model.addAttribute("age", service.getMemberAge(memid));
		model.addAttribute("obtained", service.getMemberCertList(memid));
		model.addAttribute("liked", service.getMemberLikeList(memid));
		return "/admin/member/memberInfo";
	}
	
	@RequestMapping("member/search")
	public String getMemberSearchList(String search, String keyword, String pageNum, Model model) {
		PagingDTO page = pageService.getPaging(20, pageNum);
		model.addAttribute("page", page);
		model.addAttribute("list", service.getMemberSearchList(search, keyword, page));
		model.addAttribute("count", service.getMemberSearchCnt(search, keyword));
		model.addAttribute("search", search);
		model.addAttribute("keyword", keyword);
		return "/admin/member/searchList";
	}
	@RequestMapping("member/reportList")
	public String getMemberReport(Integer status, String pageNum, Model model) {
		model.addAttribute("status", status);
		if(status!=null)
			model.addAttribute("status_name", service.getMemStatusName(status));
		
		PagingDTO page = pageService.getPaging(10, pageNum);
		model.addAttribute("page", page);
		model.addAttribute("list", service.getReportMemList(status, page));
		model.addAttribute("count", service.getReportMemCnt(status));
		return "/admin/member/reportList";
	}
	
	@RequestMapping("member/reportMemInfo")
	public String getReportMemInfo(String memid, String reportCnt, Model model) {
		model.addAttribute("memid", memid);
		model.addAttribute("reportCnt", reportCnt);
		
		//?��고당?�� ?��?�� ?���?
		model.addAttribute("dto", service.getMemberInfo(memid));
		//?��고당?�� ?��?��?�� ?��고된 �? 목록 조회
		model.addAttribute("postList", service.getReportMemPosting(memid));
		model.addAttribute("postingCnt", service.getReportMemPostingCnt(memid));
		//?��고당?�� ?��?��?�� ?��고된 ?���? 목록 조회
		model.addAttribute("commList", service.getReportMemComment(memid));
		model.addAttribute("commCnt", service.getReportMemCommCnt(memid));
		return "/admin/member/reportMemInfo";
	}
	
	@RequestMapping("member/delComment")
	public String delReportComment(Integer[] comm_num, String reportCnt, String memid, Model model) {
		model.addAttribute("result", service.delReportComment(comm_num));
		model.addAttribute("reportCnt", reportCnt);
		model.addAttribute("memid", memid);
		return "/admin/member/delCommentPro";
	}
	
	@RequestMapping("member/commReport")
	public String getCommReportDetails(String memid, Integer pnum, Model model) {
		model.addAttribute("commNum", pnum);
		model.addAttribute("list", service.getCommReportDetails(pnum));
		return "/admin/member/commRepDetails";
	}
	
	@RequestMapping("member/memReportPro")
	public String modReportMember(String memid, String status, Model model) {
		model.addAttribute("result", service.updateRepMemStatus(memid, status));
		return "/admin/member/memReportPro";
	}
	
	@RequestMapping("board/reportDetails")
	public String getReportReason(int pnum, Model model) {
		model.addAttribute("list", service.getReportReasonList(pnum));
		return "/admin/board/reportReason";
	}
	
	//관리자 - 전체 게시글 목록(사용자게시판)
	@RequestMapping("board/list")
	public String getBoardList(String pageNum, Integer status, 
							Integer board_type, Model model) {
		PagingDTO page = pageService.getPaging(20, pageNum);
		model.addAttribute("list", service.getBoardList(page, status, board_type));
		model.addAttribute("count", service.getBoardCnt(status, board_type));
		model.addAttribute("page", page);
		
		model.addAttribute("status", status);
		model.addAttribute("board_type", board_type);
		return "/admin/board/list";
	}
	
	//관리자 - 전체 게시글 목록(검색결과)
	@RequestMapping("board/search")
	public String getBoardSearchList(String pageNum, Integer board_type, String search, String keyword, Model model) {
		PagingDTO page = pageService.getPaging(20, pageNum);
		
		String board_name = "전체";
		if(board_type != null) board_name = service.getBoardName(board_type);
		model.addAttribute("board_name", board_name);
		model.addAttribute("search", search);
		model.addAttribute("keyword", keyword);
		
		model.addAttribute("list", service.getBoardSearchList(page, board_type, search, keyword));
		model.addAttribute("count",service.getBoardSearchCnt(board_type, search, keyword));
		model.addAttribute("page", page);
		return "/admin/board/search";
	}
	
	@RequestMapping("board/request")
	public String getUserRequestList(String PageNum, Model model) {
		PagingDTO page = pageService.getPaging(20, PageNum);
		model.addAttribute("page",page);
		model.addAttribute("count",service.getNewRequestCnt());
		model.addAttribute("list", service.getNewRequestList(page));
		return "/admin/board/request";
	}
	
	
	@RequestMapping("main")
	public String adminMain(Model model) {
		model.addAttribute("newCertCnt", service.getNewCertiCnt());
		model.addAttribute("newReportCnt",service.getNewReportCnt());
		
		PagingDTO page = pageService.getPaging(5, null);
		model.addAttribute("certList",service.getCertList(page, "registDate", "desc", null));
		model.addAttribute("memReportList", service.getReportMemList(0,page));
		List<EmpBoardDTO> empNoticeList = service.getEmpNoticeList(page);
		model.addAttribute("empNotice", empNoticeList);
		model.addAttribute("empNoticeCnt", empNoticeList.size());
		
		//회원상태 조정
		service.updateMemberStatus();
		return "/admin/main";
	}
	@RequestMapping("ajax/newMember")
	public String adminMainMember(Model model) {
		model.addAttribute("map", service.getNewMemberData());
		
		return "/admin/ajax/newMember";
	}
	@RequestMapping("ajax/newRequest")
	public String adminMainRequest(Model model) {
		PagingDTO page = pageService.getPaging(5, null);
		model.addAttribute("reqList",service.getNewRequestList(page));
		model.addAttribute("count", service.getNewRequestCnt());
		return "/admin/ajax/newRequest";
	}
	
	@RequestMapping("ajax/visitor")
	public String googleTest(Model model) throws Exception {
		String start = "7daysAgo";
		String today = "today";
		//구글 analytics 방문자 통계 가져와서 view로 보내기
		model.addAttribute("usersToday", gaService.getUsersStats(today, today));
		model.addAttribute("users7Days", gaService.getUsersStats(start, today));
		return "/admin/ajax/visitorStats";
	}
	
	
	//관리자 - 직원게시판 - 직원공지목록
	@RequestMapping("emp/noticeList")
	public String getEmpNoticeList(String pageNum, Model model) {
		PagingDTO page = pageService.getPaging(10, pageNum);
		model.addAttribute("page", page);
		model.addAttribute("list", service.getEmpNoticeList(page));
		model.addAttribute("count", service.getEmpNoticeCnt());
		return "/admin/emp/board/noticeList";
	}
	
	//직원 공지 등록 - 매니저 이상만 가능!
	@RequestMapping("emp/addNotice")
	public String addEmpNotice(HttpSession session, Model model){
		String empid = (String)session.getAttribute("sid");
		model.addAttribute("empid", empid);
		model.addAttribute("check", service.checkifMgr(empid));
		return "/admin/emp/board/noticeForm";
	}
	@RequestMapping("emp/addNoticePro")
	public String addEmpNoticePro(EmpBoardDTO dto, Model model){
		model.addAttribute("result", service.addEmpNotice(dto));
		return "/admin/emp/board/noticePro";
	}
	
	//직원공지
	@RequestMapping("emp/notice")
	public String getEmpNotice(int ebnum, HttpSession session, Model model) {
		model.addAttribute("id", session.getAttribute("sid"));
		service.updateReadCnt(ebnum);  //조회수 +1
		model.addAttribute("dto",service.getEmpNotice(ebnum));
		return "/admin/emp/board/notice";
	}
	
	//직원공지 수정 - 매니저이상만 가능
	@RequestMapping("emp/modNotice")
	public String modEmpNotice(int ebnum, HttpSession session, Model model) {
		//dto가 null이 아닌 경우만 noticeForm이 수정 폼으로 변경
		model.addAttribute("dto",service.getEmpNotice(ebnum));
		
		String empid = (String)session.getAttribute("sid");
		model.addAttribute("empid", empid);
		model.addAttribute("check", service.checkifMgr(empid));
		return "/admin/emp/board/noticeForm";
	}
	@RequestMapping("emp/modNoticePro")
	public String modEmpNoticePro(EmpBoardDTO dto, Model model) {
		model.addAttribute("result", service.modEmpNotice(dto));
		return "/admin/emp/board/noticePro";
	}
	@RequestMapping("emp/delNotice")
	public String delEmpNoticePro(int ebnum, Model model) {
		model.addAttribute("result", service.delEmpNotice(ebnum));
		return "/admin/emp/board/delNotice";
	}
	
	//관리자 - 직원게시판
	//사원 목록
	@RequestMapping("emp/empList")
	public String getEmpList(String pageNum, String sort, String order, String empjob, String status, 
							HttpSession session, Model model) {
		
		//sessionId가 매니저 이상인지 체크
		String empid = (String)session.getAttribute("sid");
		if(empid == null) return "/member/loginForm";
		int checkIfMgr = service.checkifMgr(empid);
		model.addAttribute("checkIfMgr", checkIfMgr);
		
		//?��?��?�� ?��보는 매니??�? ?��?���? 조회�??��
		if(status != null && status.equals("일반")){
			if(checkIfMgr != 1) {
				return "/admin/warning";
			}
		} 
		
		model.addAttribute("empjob", empjob);
		model.addAttribute("status", status);
		model.addAttribute("sort", sort);
		model.addAttribute("order",order);
		
		PagingDTO page = pageService.getPaging(10, pageNum);
		model.addAttribute("page",page);
		model.addAttribute("list", service.getEmpList(page, empjob, status, sort, order));
		model.addAttribute("totalCnt", service.getEmpCnt(empjob, status));
		model.addAttribute("quitCnt", service.getQuitCnt(empjob));
		
		//jobList - DB?��록된 직업리스?�� 보내�?
		model.addAttribute("jobList", service.getEmpjobList());
		//statusList - DB?��록된 직급리스?�� 보내�? 
		model.addAttribute("statusList", service.getEmpStatusList());
		
		return "/admin/emp/info/empList";
	}
	//?��?��목록 - �??��결과
	@RequestMapping("emp/searchList")
	public String getEmpSearchList(String pageNum, String search, String keyword, HttpSession session, Model model) {
		model.addAttribute("keyword", keyword);
		
		PagingDTO page = pageService.getPaging(10, pageNum);
		model.addAttribute("page", page);
		
		model.addAttribute("list", service.getEmpSearchList(page, search, keyword));
		model.addAttribute("totalCnt", service.getEmpSearchCnt(search, keyword));
		model.addAttribute("quitCnt", service.getQuitCnt_search(search, keyword));
		
		String empid = (String) session.getAttribute("sid");
		model.addAttribute("checkIfMgr", service.checkifMgr(empid));
		return "/admin/emp/info/searchEmpList";
	}
	//사원 상세정보 조회 
	@RequestMapping("emp/empInfo")
	public String getEmpInfo(String empid, HttpSession session, Model model) {
		String sessionId = (String)session.getAttribute("sid");
		model.addAttribute("sessionId", sessionId);
		model.addAttribute("dto",service.getEmpInfo(empid));
		model.addAttribute("age", service.getMemberAge(empid));
		model.addAttribute("checkIfMgr", service.checkifMgr(sessionId));
		return "/admin/emp/info/empInfo";
	}
	//사원 등록
	@RequestMapping("emp/addEmp")
	public String addEmpForm(String memid, Model model) {
		model.addAttribute("memid", memid);
		
		//memberinfo에 등록된 id인지 체크
		if(memid!=null)
			model.addAttribute("dto", service.getMemberInfo(memid));
		
		//입사일 초기값(오늘 날짜)
		model.addAttribute("currentDate", service.getCurrentDate());
		return "/admin/emp/info/addEmp";
	}
	@RequestMapping("emp/addEmpPro")
	public String addEmpPro(EmpInfoDTO dto, Model model) {
		model.addAttribute("result", service.addEmpInfo(dto));
		return "/admin/emp/info/addEmpPro";
	}
	//?��?��?���? ?��?��
	@RequestMapping("emp/modEmp")
	public String modEmpInfo(String empid, HttpSession session, Model model) {
		//model.addAttribute("sessionId", session.getAttribute("empid"));
		model.addAttribute("dto", service.getEmpInfo(empid));
		return "/admin/emp/info/modEmpForm";
	}
	@RequestMapping("emp/modEmpPro")
	public String modEmpPro(EmpInfoDTO dto, Model model) {
		model.addAttribute("result", service.modEmpInfo(dto));
		model.addAttribute("empid", dto.getEmpid());
		return "/admin/emp/info/modEmpPro";
	}
	//?��?��?���? ?��?��
	@RequestMapping("emp/delEmp")
	public String delEmpInfo(String empid, Model model) {
		model.addAttribute("dto", service.getEmpInfo(empid));
		return "/admin/emp/info/delEmpForm";
	}
	@RequestMapping("emp/delEmpPro")
	public String delEmpPro(String empid, String reason, String reason2, Model model) {
		String leavingReason = reason+"-"+reason2;
		model.addAttribute("result", service.delEmpInfo(empid, leavingReason));
		return "/admin/emp/info/delEmpPro";
	}
	
	@RequestMapping("emp/myPage")
	public String empMypage(Model model) {
		//?��?�� id 체크 
		return "/admin/emp/info/empInfo";
	}
	
	@RequestMapping("realpath")
	public void realPath(HttpServletRequest request) {
		ServletContext servletContext = request.getSession().getServletContext();
		String realPath = servletContext.getRealPath("/");
		System.out.println(realPath);
	}
}
