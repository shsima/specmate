package com.specmate.connectors.api;

import java.util.List;

import com.specmate.export.api.IExporter;
import com.specmate.model.auth.IAuthProject;

public interface IProject {

	/**
	 * @return the id of the project
	 */
	String getID();

	/**
	 * @return the defined requirements source for the project, or
	 *         <code>null</code>.
	 */
	IRequirementsSource getConnector();

	/**
	 * @return the defined sink to which test information is exported, or
	 *         <code>null</code>.
	 */
	IExporter getExporter();

	/**
	 * @return the list of defined library folders for the project, or
	 *         <code>null</code>.
	 */
	List<String> getLibraryFolders();

	/**
	 * @return the authorization project defining the auth mechanism for this
	 *         project.
	 */
	IAuthProject getAuthProject();

}