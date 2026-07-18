package kopo.poly.mapper;

import kopo.poly.dto.MovieDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IMovieMapper {


    int insertMovieInfo(MovieDTO pDTO) throws Exception;


    int deleteMovieInfo(MovieDTO pDTO) throws Exception;


    List<MovieDTO> getMovieInfo(MovieDTO pDTO) throws Exception;
}
