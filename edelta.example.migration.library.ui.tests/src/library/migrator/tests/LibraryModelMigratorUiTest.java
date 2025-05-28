package persons.migrator.tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import edelta.ui.testutils.EdeltaUiTestUtils;

@RunWith(SWTBotJunit4ClassRunner.class)
public class LibraryModelMigratorUiTest {

	private static SWTWorkbenchBot bot;

	@BeforeClass
	public static void beforeClass() {
		bot = new SWTWorkbenchBot();
		EdeltaUiTestUtils.closeWelcomePage();
		EdeltaUiTestUtils.openProjectExplorer();
	}

	@AfterClass
	public static void afterClass() {
		bot.resetWorkbench();
	}

	@After
	public void runAfterEveryTest() throws Exception {
		EdeltaUiTestUtils.cleanWorkspace();
	}

	@Test
	public void testMigrationOnWorkspace() throws Exception {
		// import the first project
		EdeltaUiTestUtils.importProject("test-projects/firstproject");
		// verify the pop-up dialog shows the migrated models of the first project
		SWTBotShell dialog = bot.shell("Migration").activate();
		String dialogText = dialog.bot().label(1).getText();
		assertThat(dialogText)
			.contains(
				"The following models have been migrated:",
				"firstproject/MyBookDatabase1.books",
				"firstproject/MyLibrary1.library");
		dialog.bot().button("OK").click();
		// import the second project
		EdeltaUiTestUtils.importProject("test-projects/secondproject");
		// verify the pop-up dialog shows the migrated models of the second project
		dialog = bot.shell("Migration").activate();
		dialogText = dialog.bot().label(1).getText();
		assertThat(dialogText)
			.contains(
				"The following models have been migrated:",
				"secondproject/MyBookDatabase2.books",
				"secondproject/MyLibrary2.library")
			.doesNotContain("firstproject");
		dialog.bot().button("OK").click();
	}

}
