package com.specmate.migration.internal.services;

import java.sql.Connection;

import org.osgi.service.component.annotations.Component;

import com.specmate.common.SpecmateException;
import com.specmate.migration.api.IMigrator;
import com.specmate.migration.h2.AttributeToSQLMapper;

@Component(property = "sourceVersion=20180510", service = IMigrator.class)
public class Migrator20180510 extends BaseMigrator {
	
	@Override
	public String getSourceVersion() {
		return "20180510";
	}

	@Override
	public String getTargetVersion() {
		return "20180527";
	}

	@Override
	public void migrate(Connection connection) throws SpecmateException {
		AttributeToSQLMapper aMapper = new AttributeToSQLMapper(connection, logService, "model/user", 
				getSourceVersion(), getTargetVersion());
		
		aMapper.migrateRenameAttribute("UserSession", "accessRights", "PPMRights");
		aMapper.migrateNewIntegerAttribute("UserSession", "ALMRights", 0);
	}

	
}
