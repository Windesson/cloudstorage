package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.FileModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE userid = #{userid}")
    List<FileModel> getFiles(Integer userid);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES(#{filename}, #{contentType}, #{filesize}, #{userId}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    Integer insert(FileModel fileModel);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId} AND userid = #{userId}")
    Integer delete(Integer fileId, Integer userId);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId} AND userid = #{userId} LIMIT 1")
    FileModel getFile(Integer fileId, Integer userId);
}
