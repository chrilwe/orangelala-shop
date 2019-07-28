package com.orangelala.service.cms.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.orangelala.framework.model.cms.CmsSite;
@Repository
public interface CmsSiteDao extends MongoRepository<CmsSite, String> {

}
