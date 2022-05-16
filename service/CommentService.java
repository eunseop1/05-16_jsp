package kr.human.service;

import java.util.List;

import kr.human.memo.vo.CommentVO;
import kr.human.memo.vo.PagingVO;

// 실제 로직을 처리하는 기능들... 메서드 1개당 -> 로직 1개
public interface CommentService {
	//1. 목록 보기
	//List<CommentVO> selectAll();
	PagingVO<CommentVO> selectList(int currentPage, int pageSize, int blockSize);
	//2. 저장하기
	int insert(CommentVO vo);
	//3. 수정하기
	int update(CommentVO vo);
	//4. 삭제하기
	int delete(CommentVO vo);
}
