package com.specmate.migration.h2;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.osgi.service.log.LogService;

import com.specmate.common.SpecmateException;

public abstract class SQLMapper {
	protected static final String SPECMATE_URL = "http://specmate.com/"; 
	protected Connection connection;
	protected String packageName;
	protected String sourceVersion;
	protected String targetVersion;
	protected LogService logService;
	
	public SQLMapper(Connection connection, LogService logService, String packageName, String sourceVersion, String targetVersion) {
		this.connection = connection;
		this.logService = logService;
		this.packageName = packageName;
		this.sourceVersion = sourceVersion;
		this.targetVersion = targetVersion;
	}
	
	protected List<String> insertExternalObjectReference(String objectName) throws SpecmateException {
		int id = SQLUtil.getIntResult("SELECT id FROM CDO_EXTERNAL_REFS ORDER BY id ASC LIMIT 1", 1, connection);
		
		List<String> queries = new ArrayList<>();
		String baseUri = SPECMATE_URL + targetVersion + "/" + packageName + "#//" + objectName;
		id = id - 1;
		queries.add(getInsertExternalReferenceQuery(baseUri, id));
		
		return queries;
	}

	protected List<String> insertExternalAttributeReferences(String objectName, List<String> attributeNames) throws SpecmateException {
		int id = SQLUtil.getIntResult("SELECT id FROM CDO_EXTERNAL_REFS ORDER BY id ASC LIMIT 1", 1, connection);
		
		List<String> queries = new ArrayList<>();
		String baseUri = SPECMATE_URL + targetVersion + "/" + packageName + "#//" + objectName;
		
		//queries.add(getInsertExternalReferenceQuery(baseUri, id));
		for (String name : attributeNames) {
			id = id - 1;
			String attributeUri = baseUri + "/" + name;
			queries.add(getInsertExternalReferenceQuery(attributeUri, id));
		}
		
		return queries;
	}
	
	protected String renameExternalReference(String objectName, String oldAttributeName, String newAttributeName) throws SpecmateException {
		String baseUri = SPECMATE_URL + targetVersion + "/" + packageName + "#//" + objectName + "/";
		String oldUri = baseUri + oldAttributeName;
		String newUri = baseUri + newAttributeName;
		return "UPDATE CDO_EXTERNAL_REFS SET uri = '" + newUri + "' WHERE uri = '" + oldUri + "'";
	}
	
	private String getInsertExternalReferenceQuery(String uri, int id) {
		Date now = new Date();
		return "INSERT INTO CDO_EXTERNAL_REFS (ID, URI, COMMITTIME) " +
				"VALUES (" + id + ", '" + uri + "', " + now.getTime() + ")";
	}
}
