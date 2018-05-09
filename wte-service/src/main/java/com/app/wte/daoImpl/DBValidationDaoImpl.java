package com.app.wte.daoImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.app.wte.dao.DBValidationDao;
import com.app.wte.model.DBValidationResponse;

@Repository
public class DBValidationDaoImpl implements DBValidationDao {

	@Override
	public DBValidationResponse getStageOfSourceFile(String str) {
		// TODO Auto-generated method stub
		return null;
	}

	/*@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public DBValidationResponse getStageOfSourceFile(String str) {
		System.out.println("Entered DBValidationDao "+str);
		//List<Result> resultList= new ArrayList<Result>();
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>();
		DBValidationResponse dbValidationResponse = new DBValidationResponse();

		try {						
					if (str != null) {
						String qry="select FILE_NAME, EFID, CLIENT_NAME, SOURCE_PATH, STAGE_NAME, STATUS, TOTAL_RECORDS_COUNT, "
								+ "VALID_RECORDS_COUNT, INVALID_RECORDS_COUNT, EDGE_NODE_FILE_ARRIVAL_TIMESTAMP, RULE_VERSION, "
								+ "IS_COMPRESSED, PROCESSED_TIMESTAMP, PROCESSED_USER_ID from TBL_AUDIT_UHG where FILE_NAME='"+ str + "'";
						resultList=namedParameterJdbcTemplate.queryForList(qry,Collections.EMPTY_MAP);
					}
					dbValidationResponse.setResultList(resultList);
		} catch (Exception e) {
			dbValidationResponse.setCode(-100);
			dbValidationResponse.setMessage(e.getMessage());
			 
		}
		return dbValidationResponse;

	}*/

	
}

