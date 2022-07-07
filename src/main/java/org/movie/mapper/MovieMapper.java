package org.movie.mapper;

import java.util.List;

import org.movie.domain.MovieVO;

public interface MovieMapper {
	public List<MovieVO> selectAllMovie();
	
	public void insertMovie(MovieVO mVO);
	
	public MovieVO selectProductByCode(Long code);
	
	public int deleteMovie(Long code);	
}
