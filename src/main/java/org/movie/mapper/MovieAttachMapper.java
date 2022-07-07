package org.movie.mapper;

import java.util.List;

import org.movie.domain.MovieAttachVO;

public interface MovieAttachMapper {
	public void insert(MovieAttachVO vo);
	
	public List<MovieAttachVO> findByCode(Long code);
	
	public List<MovieAttachVO> getOldFiles(Long code);
	
	public void delete(String uuid);
	
	public void deleteAll(Long code);
	
}
