package kr.human.memo.vo;

import java.util.Date;

import lombok.Data;

/*
VO는 데이터가 저장될 객체
테이블과 1:1 대응 되도록 만든다
 */
@Data
public class CommentVO {
	private int idx;
	private String name;
	private String password;
	private String content;
	private Date regDate;
	private String ip;
	
	//저장/수정/삭제 수분하기 위한 필드 -> DB와는 관계 없다
	private String mode;
}