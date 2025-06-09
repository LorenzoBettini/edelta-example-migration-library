package edelta.example.migration.library.migrator.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import edelta.example.migration.library.migrator.LibraryModelMigrator;
import edelta.lib.EdeltaResourceUtils;
import library.presentation.LibraryEditorPlugin;

public class LibraryMigratorHandler implements IStartup {

	@Override
	public void earlyStartup() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		// Process existing projects in the workspace
		IProject[] projects = workspace.getRoot().getProjects();
		processProjects(projects);
		// Register listener for new projects (opened/imported)
		workspace.addResourceChangeListener(new ProjectListener(), IResourceChangeEvent.POST_CHANGE);
	}

	private void processProjects(IProject... projects) {
		List<String> migratedResources = new ArrayList<>();
		for (IProject project : projects) {
			if (project.isOpen()) {
				LibraryEditorPlugin.INSTANCE.log("Processing project: " + project.getName());
				String projectPath = project.getLocation().toString();
				LibraryModelMigrator libraryMigrator = new LibraryModelMigrator();
				try {
					Collection<Resource> migrated = libraryMigrator.execute(projectPath);
					migratedResources.addAll(migrated.stream()
						.map(m -> EdeltaResourceUtils.getRelativePath(m, projectPath))
						.toList());
				} catch (Exception e) {
					LibraryEditorPlugin.INSTANCE.log(e);
				}
			}
		}
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		workbench.getDisplay().asyncExec(() -> {
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			if (window != null && !migratedResources.isEmpty()) {
				MessageDialog.openInformation(window.getShell(), "Migration",
					"The following models have been migrated:\n\n" +
						migratedResources.stream()
							.collect(Collectors.joining("\n")));
			}
		});
	}

	/**
	 * Listener for project changes
	 */
	private class ProjectListener implements IResourceChangeListener {
		@Override
		public void resourceChanged(IResourceChangeEvent event) {
			if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
				try {
					event.getDelta().accept(delta -> {
						if (delta.getResource() instanceof IProject project
								&& (delta.getKind() == IResourceDelta.ADDED
								|| (delta.getKind() == IResourceDelta.CHANGED
										&& (delta.getFlags() & IResourceDelta.OPEN) != 0))) {

							if (project.isOpen()) {
								processProjects(project);
							}
							return false; // don't visit children
						}
						return true; // keep visiting
					});
				} catch (CoreException e) {
					LibraryEditorPlugin.INSTANCE.log(e);
				}
			}
		}
	}
}
