package org.movie.domain;

import java.util.List;

import lombok.Data;

@Data
public class MovieVO {
	private Long code;
	private int price;
	
	private String title, director, actor, synopsis;
	private List<MovieAttachVO> attachList;
}
