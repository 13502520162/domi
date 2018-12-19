package com.domi.mapper;

import java.util.LinkedList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.domi.model.Version;


@Repository
public interface VersionMapper {

		public int insert(Version version);
		public int update(Version version);
		public LinkedList<Version> queryAll();
		public List<Version> queryByType(@Param("type")int type);
		
		public Version queryNewVersionByType(@Param("type")int type);
}
