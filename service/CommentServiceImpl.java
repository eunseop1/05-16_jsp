package kr.human.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import kr.human.jdbc.JDBCUtill;
import kr.human.memo.dao.CommentDAO;
import kr.human.memo.dao.CommentDAOImpl;
import kr.human.memo.vo.CommentVO;
import kr.human.memo.vo.PagingVO;

public class CommentServiceImpl implements CommentService{
	//-------------------------------------------------------------------------
	//싱글톤 클래스로 만들자
	private static CommentService instance = new CommentServiceImpl();
	private CommentServiceImpl() {
		;
	}
	public static CommentService getInstance() {
		return instance;
	}
	//-------------------------------------------------------------------------
	
	
	

	@Override
	public int insert(CommentVO vo) {
		int count = 0;
		try(Connection conn = JDBCUtill.getConnection()){
			// 데이터가 유효할때만(null이 아닐때만) 저장한다
			if(vo != null) { //객체가 있고
				if(vo.getName() != null && vo.getName().trim().length() > 0) { //이름이 존재하고
					if(vo.getPassword() != null && vo.getPassword().trim().length() > 0) { //비번이 존재하고
						if(vo.getContent() != null && vo.getContent().trim().length() > 0) { //내용도 존재하면
							count = CommentDAOImpl.getInstance().insert(conn, vo);
						}
					}
				}
			}
		//그런데 만약 자바스크립트에서 폼에 모두 입력해야 넘어간다면 이렇게 할 필요는 없다.
		//단지 안전하게 쓰려고 이와 같이 번거롭게 했다
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int update(CommentVO vo) {
		int count = 0;
		try(Connection conn = JDBCUtill.getConnection()){
			// 데이터가 유효할때만(null이 아닐때만) 수정한다
			if(vo != null) { //객체가 있고
				//수정할때는 이름이 필요없다
				if(vo.getPassword() != null && vo.getPassword().trim().length() > 0) { //비번이 존재하고
					if(vo.getContent() != null && vo.getContent().trim().length() > 0) { //내용도 존재하면
						//DB에 있는 암호와 넘어온 암호가 일치 할때만 수정한다
						CommentVO dbVO = CommentDAOImpl.getInstance().selectByIdx(conn, vo.getIdx());
						if(dbVO != null && dbVO.getPassword().equals(vo.getPassword())) {
							count = CommentDAOImpl.getInstance().update(conn, vo);
						}
					}
				}
			}
		//그런데 만약 자바스크립트에서 폼에 모두 입력해야 넘어간다면 이렇게 할 필요는 없다.
		//단지 안전하게 쓰려고 이와 같이 번거롭게 했다
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int delete(CommentVO vo) {
		int count = 0;
		try(Connection conn = JDBCUtill.getConnection()){
			// 데이터가 유효할때만(null이 아닐때만) 삭제한다
			if(vo != null) { //객체가 있고
				//DB에 있는 암호와 넘어온 암호가 일치 할때만 삭제한다
				CommentVO dbVO = CommentDAOImpl.getInstance().selectByIdx(conn, vo.getIdx());
				if(dbVO != null && dbVO.getPassword().equals(vo.getPassword())) {
					count = CommentDAOImpl.getInstance().delete(conn, vo.getIdx());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	@Override
	public PagingVO<CommentVO> selectList(int currentPage, int pageSize, int blockSize) {
		PagingVO<CommentVO> pagingVO = null;
		Connection conn = null;
		try{
			conn = JDBCUtill.getConnection();
			conn.setAutoCommit(false);
			int totalCount = CommentDAOImpl.getInstance().selectCount(conn);//전체 개수를 DB에서 가져온다
			//객체를 생성하는 순간 -> 모든 페이지 계산이 끝난다
			pagingVO = new PagingVO<>(totalCount, currentPage, pageSize, blockSize);
			
			//글 목록을 가져온다
			List<CommentVO> list = CommentDAOImpl.getInstance().selectList(conn, pagingVO.getStartNo(), pagingVO.getPageSize());
			
			//글을 VO에 넣어준다
			pagingVO.setList(list);
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return pagingVO;
	}
	
}
