package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_PARCELS_LISTED_OVERVIEW;
import static seedu.address.testutil.TypicalParcels.ELLE;
import static seedu.address.testutil.TypicalParcels.FIONA;
import static seedu.address.testutil.TypicalParcels.GEORGE;
import static seedu.address.testutil.TypicalParcels.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.parcel.NameContainsKeywordsPredicate;
import seedu.address.model.parcel.ReadOnlyParcel;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeywordsPredicate firstPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        NameContainsKeywordsPredicate secondPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("second"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different parcel -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noParcelFound() {
        String expectedMessage = String.format(MESSAGE_PARCELS_LISTED_OVERVIEW, 0);
        FindCommand command = prepareCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList());
    }

    @Test
    public void execute_multipleKeywords_multipleParcelsFound() {
        String expectedMessage = String.format(MESSAGE_PARCELS_LISTED_OVERVIEW, 2);
        FindCommand command = prepareCommand("Kunz Elle Best");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ELLE, FIONA, GEORGE), Arrays.asList(ELLE, FIONA),
                Arrays.asList(GEORGE));
    }

    /**
     * Parses {@code userInput} into a {@code FindCommand}.
     */
    private FindCommand prepareCommand(String userInput) {
        FindCommand command =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+"))));
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<ReadOnlyParcel>} is equal to {@code expectedList}<br>
     *     - the {@code AddressBook} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(FindCommand command, String expectedMessage, List<ReadOnlyParcel> expectedList,
                                      List<ReadOnlyParcel> expectedUndeliveredList,
                                      List<ReadOnlyParcel> expectedDeliveredList) {
        AddressBook expectedAddressBook = new AddressBook(model.getAddressBook());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredParcelList());
        assertEquals(expectedAddressBook, model.getAddressBook());
        assertEquals(expectedUndeliveredList, model.getFilteredUndeliveredParcelList());
        assertEquals(expectedDeliveredList, model.getFilteredDeliveredParcelList());
    }
}
