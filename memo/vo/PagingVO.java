package kr.human.memo.vo;

import java.util.List;
//페이지 계산을 하고 한 페이지 분량의 글 목록을 가지는 VO
public class PagingVO<T> {
	//페이지 게산하는 방법은 똑같다
	private List<T> list; // 글목록 -> 유일하게 setter가 필요하다
	
	//4가지는 넘겨서 받자	
	private int totalCount;//전체 개수는 DB에서 받아서 쓰자
	private int currentPage; //현재 페이지
	private int pageSize;//페이지당 글 수
	private int blockSize;//하단의 페이지 번호 개수
	
	//나머지는 계산해서 넣자
	private int totalPage;//전체 페이지 수
	private int startNo;//시작 글번호
	private int endNo;//마지막 글번호(오라클에서만 사용)
	private int startPage;//시작 페이지 번호
	private int endPage;//마지막 페이지 번호
	
	//4개의 값을 넘겨 받는 생성자
	public PagingVO(int totalCount, int currentPage, int pageSize, int blockSize) {
		super();
		this.totalCount = totalCount;
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.blockSize = blockSize;
		
		calc();
	}

	private void calc() {
		if(totalCount > 0) {
			if(currentPage <= 0) currentPage = 1;
			if(pageSize <= 1) pageSize = 10;
			if(blockSize <= 1) blockSize = 10;
			
			//전체 페이지 수 = (전체 개수 -1)/페이지당 글개수 + 1
			// 총 페이지수 123, 페이지당 10개 라면? -> 13페이지가 나온다
			// (123 - 1) / 10 + 1 = 13
			
			totalPage = (totalCount - 1)/pageSize + 1;
			
			//페이지수에 대해서 계산할때는 
			//mySQL은 0부터 세기에 (현재페이지-1)*페이지당 글수
			//1페이지 (1-1)*10=0 ->(0~9를 담는다)
			//2페이지 (2-1)*10=10 ->(10~19)
			//7페이지 (7-1)*10=60 -> (60~66)
			startNo = (currentPage - 1) * pageSize;
			
			//끝 글번호 = 시작글번호 + 페이지당 개수 - 1
			endNo = startNo + pageSize - 1;
			//끝 글번호는 전체 개수를 넘기면 안된다 (오라클의 경우, 조건을 >로)
			if(endNo >= totalCount) {
				endNo = totalCount - 1; //오라클의 경우 -1을 안한다
			}
			
			//시작 페이지 번호 = (현재 페이지 - 1)/페이지 목록 개수 * 페이지 목록 개수 + 1
			// 현재 7페이지를 본다면? -> 시작페이지번호는 1~10페이지까지 보이고
			// 현재 13페이지를 본다면? -> 11부터 20까지의 페이지가 보여야 한다
			startPage = (currentPage - 1)/blockSize * blockSize + 1;
			
			//끝 페이지 = 시작페이지 + 페이지 목록 개수 - 1
			//끝페이지는 전체 페이지를 넘을 수 없다
			endPage = startPage + blockSize - 1;
			if(endPage > totalPage) endPage = totalPage;
			
			//setter을 만들 필요가 없다 -> 한번 계산한건 변함이 없다
			
			
		}
	}


	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public int getStartNo() {
		return startNo;
	}

	public int getEndNo() {
		return endNo;
	}

	public int getStartPage() {
		return startPage;
	}

	public int getEndPage() {
		return endPage;
	}
	//메서드 2개 추가
	public String getPageInfo() {
		//전체: 5개(?/? 페이지)
		String pageInfo = "전체: " + totalCount + "개";
		if(totalCount >= 0) {
			pageInfo += "(" + currentPage + "/" + totalPage + "page)";
		}
		return pageInfo;
	}
	public String getPageList() {
		String pageList = "";
		pageList += "  <ul class='pagination pagination-sm justify-content-center'>";
		// 시작페이지가 1이라면 이전은 없다.
		if(startPage>1) {
			pageList += "    <li class='page-item'>";
			pageList += "      <a class='page-link' href='?p="+(startPage-1)+"&s="+pageSize+"&b="+blockSize+"' aria-label='Previous'>";
			pageList += "        <span aria-hidden='true'>&laquo;</span>";
			pageList += "      </a>";
			pageList += "    </li>";
		}
		// 페이지 번호 출력
		for(int i=startPage;i<=endPage;i++) {
			if(currentPage==i)
				pageList += "   <li class='page-item active'><a class='page-link' href='?p=" + i +"&s="+pageSize+"&b="+blockSize+"'>" + i + "</a></li>";
			else
				pageList += "   <li class='page-item'><a class='page-link' href='?p=" + i +"&s="+pageSize+"&b="+blockSize+"'>" + i + "</a></li>";
		}
		// 마지막페이지가 전체페이지수 보다 적을 경우에만 다음이 있다
		if(endPage<totalPage) {
			pageList += "	<li class='page-item'>";
			pageList += "      <a class='page-link' href='?p="+(endPage+1)+"&s="+pageSize+"&b="+blockSize+"' aria-label='Next'>";
			pageList += "        <span aria-hidden='true'>&raquo;</span>";
			pageList += "      </a>";
			pageList += "    </li>";
		}
		pageList += "  </ul>";

		return pageList;
	}
	
	@Override
	public String toString() {
		return "PagingVO [list=" + list + ", totalCount=" + totalCount + ", currentPage=" + currentPage + ", pageSize="
				+ pageSize + ", blockSize=" + blockSize + ", totalPage=" + totalPage + ", startNo=" + startNo
				+ ", endNo=" + endNo + ", startPage=" + startPage + ", endPage=" + endPage + "]";
	}
	
}
