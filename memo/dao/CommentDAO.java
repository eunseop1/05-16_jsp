package kr.human.memo.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import kr.human.memo.vo.CommentVO;
/*
DB를 건드린다
 */
public interface CommentDAO {
	
	// 1. 전체 개수를 세자
	int selectCount(Connection conn) throws SQLException;
	// 2. 1개 얻기
	CommentVO selectByIdx(Connection conn, int idx) throws SQLException;
	// 3. 1페이지 분량 얻기
	List<CommentVO> selectList(Connection conn, int startNo, int pageSize) throws SQLException;
	
	// 4. 저장하기
	int insert(Connection conn, CommentVO vo) throws SQLException;
	// 5. 수정하기
	int update(Connection conn, CommentVO vo) throws SQLException;
	// 6. 삭제하기
	int delete(Connection conn, int idx) throws SQLException;
}
