package com.specmate.export.internal.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections4.iterators.IteratorChain;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.log.LogService;

import com.specmate.auth.api.ISessionListener;
import com.specmate.auth.api.ISessionService;
import com.specmate.common.exception.SpecmateAuthorizationException;
import com.specmate.common.exception.SpecmateException;
import com.specmate.common.exception.SpecmateValidationException;
import com.specmate.export.api.ITestExporter;
import com.specmate.model.testspecification.TestProcedure;
import com.specmate.model.testspecification.TestSpecification;
import com.specmate.model.testspecification.TestSpecificationSkeleton;
import com.specmate.usermodel.UserSession;

/**
 * Service that manages export services and allows accessing them via a single
 * interface.
 */
@Component(immediate = true, service = ExportManagerService.class)
public class ExportManagerService {

	/** Collects all exporters that can export test specifications */
	private Map<String, ITestExporter> testSpecificationExporters = new HashMap<String, ITestExporter>();
	private Map<String, ITestExporter> testProcedureExporters = new HashMap<String, ITestExporter>();
	private LogService logService;
	private ISessionService sessionService;

	@Activate
	public void activate() {
		sessionService.registerSessionListener(new ISessionListener() {

			@Override
			public void sessionDeleted(UserSession session) {
				// nothing to do
			}

			@Override
			public void sessionCreated(UserSession session, String userName, String password) {
				Set<String> allowedExporters = new HashSet<String>();
				IteratorChain<ITestExporter> allExporters = new IteratorChain<ITestExporter>(
						testProcedureExporters.values().iterator(), testSpecificationExporters.values().iterator());
				while (allExporters.hasNext()) {
					ITestExporter exporter = allExporters.next();
					if (exporter.getProjectName() == null
							|| Pattern.matches(session.getAllowedPathPattern(), exporter.getProjectName())) {
						if (exporter.isAuthorizedToExport(userName, password)) {
							allowedExporters.add(exporter.getLanguage().toLowerCase());
						}
					}
				}
				session.getExporters().addAll(allowedExporters);
			}
		});
	}

	public Optional<TestSpecificationSkeleton> export(Object object, String language, String userToken)
			throws SpecmateException {
		List<String> allowedExporters = sessionService.getExporters(userToken);
		if (!allowedExporters.contains(language.toLowerCase())) {
			throw new SpecmateAuthorizationException("Export to " + language + " is not allowed.");
		}
		String languageKey = language.toLowerCase();
		ITestExporter exporter = null;
		if (object instanceof TestSpecification) {
			exporter = testSpecificationExporters.get(languageKey);
		} else if (object instanceof TestProcedure) {
			exporter = testProcedureExporters.get(languageKey);
		}
		if (exporter == null) {
			throw new SpecmateValidationException("Exporter for language " + language + " does not exist.");
		}
		return exporter.export(object);
	}

	public List<String> getExporters(Object object, String userToken) {
		List<String> allowedExporters;
		try {
			allowedExporters = sessionService.getExporters(userToken);
		} catch (SpecmateException e) {
			logService.log(LogService.LOG_ERROR, "Exception occured when retrieving allowed exporters.", e);
			allowedExporters = new ArrayList<>();
		}
		final List<String> _allowedExporters = allowedExporters;
		if (object instanceof TestSpecification) {
			return testSpecificationExporters.entrySet().stream().filter(e -> _allowedExporters.contains(e.getKey()))
					.map(e -> e.getValue().getLanguage()).collect(Collectors.toList());
		}
		if (object instanceof TestProcedure) {
			return testProcedureExporters.entrySet().stream().filter(e -> _allowedExporters.contains(e.getKey()))
					.map(e -> e.getValue().getLanguage()).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	public void addTestSpecificationExporter(ITestExporter exporter) {
		if (exporter.canExportTestProcedure()) {
			addToExporterCollection(exporter, testProcedureExporters);
		}
		if (exporter.canExportTestSpecification()) {
			addToExporterCollection(exporter, testSpecificationExporters);
		}
	}

	public void removeTestSpecificationExporter(ITestExporter exporter) {
		String languageKey = exporter.getLanguage().toLowerCase();
		ITestExporter existing = testSpecificationExporters.get(languageKey);
		if (existing == exporter) {
			testSpecificationExporters.remove(languageKey);
		}
	}

	private void addToExporterCollection(ITestExporter exporter, Map<String, ITestExporter> exporterMap) {
		String languageKey = exporter.getLanguage().toLowerCase();
		if (exporterMap.containsKey(languageKey)) {
			logService.log(LogService.LOG_WARNING, "Test exporter for langugae " + exporter.getLanguage()
					+ " already exists. Ignoring: " + exporter.getClass().getName());
		}
		exporterMap.put(languageKey, exporter);
	}

	@Reference
	public void setLogService(LogService logService) {
		this.logService = logService;
	}

	@Reference
	public void setSessionService(ISessionService sessionService) {
		this.sessionService = sessionService;
	}

}