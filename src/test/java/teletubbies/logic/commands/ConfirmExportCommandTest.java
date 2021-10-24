package teletubbies.logic.commands;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import teletubbies.commons.core.Messages;
import teletubbies.logic.commands.exceptions.CommandException;
import teletubbies.model.AddressBook;
import teletubbies.model.Model;
import teletubbies.model.ModelManager;
import teletubbies.model.UserPrefs;
import teletubbies.testutil.Assert;
import teletubbies.testutil.TypicalPersons;

class ConfirmExportCommandTest {
    private Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
    private ConfirmExportCommand confirmExportCommand = new ConfirmExportCommand();

    @Test
    public void execute_noPendingExport_throwsException() {
        CommandTestUtil.assertCommandFailure(confirmExportCommand, model, Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void saveAddressBookToPath_emptyPath_throwsException() {
        AddressBook original = TypicalPersons.getTypicalAddressBook();
        Assert.assertThrows(CommandException.class, () -> confirmExportCommand.saveAddressBookToPath(original, ""));
    }

    @Test
    public void includeDotWithJson_addJson_true() {
        String s = "hello";
        Assertions.assertEquals(confirmExportCommand.includeDotJson(s), s + ".json");
    }

    @Test
    public void includeDotWithJson_addJson_false() {
        String s = "hello.json";
        Assertions.assertEquals(confirmExportCommand.includeDotJson(s), s);
    }

    @Test
    public void equals() {
        final ConfirmExportCommand confirmExportFirstCommand = new ConfirmExportCommand();
        final ConfirmExportCommand confirmExportSecondCommand = new ConfirmExportCommand();

        // same object -> returns true
        Assertions.assertTrue(confirmExportFirstCommand.equals(confirmExportFirstCommand));

        // same values -> returns true
        Assertions.assertTrue(confirmExportFirstCommand.equals(confirmExportSecondCommand));

        // different types -> returns false
        Assertions.assertFalse(confirmExportFirstCommand.equals(1));

        // null -> returns false
        Assertions.assertFalse(confirmExportFirstCommand.equals(null));
    }
}
