package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STATUS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TRACKING_NUMBER_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalParcels.getTypicalAddressBook;

import java.util.List;

import org.junit.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditParcelDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.parcel.Parcel;
import seedu.address.model.parcel.ReadOnlyParcel;
import seedu.address.testutil.EditParcelDescriptorBuilder;
import seedu.address.testutil.ParcelBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class MaintainSortedMechanismSystemTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_maintainSorted_success() throws Exception {
        Index indexLastParcel = Index.fromOneBased(model.getFilteredParcelList().size());
        ReadOnlyParcel lastParcel = model.getFilteredParcelList().get(indexLastParcel.getZeroBased());

        ParcelBuilder parcelInList = new ParcelBuilder(lastParcel);
        Parcel editedParcel = parcelInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).withTrackingNumber(VALID_TRACKING_NUMBER_BOB)
                .withStatus(VALID_STATUS_BOB).build();

        EditParcelDescriptor descriptor = new EditParcelDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).withTrackingNumber(VALID_TRACKING_NUMBER_BOB)
                .withStatus(VALID_STATUS_BOB).build();
        EditCommand editCommand = prepareCommand(indexLastParcel, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PARCEL_SUCCESS, editedParcel);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateParcel(lastParcel, editedParcel);
        expectedModel.maintainSorted();

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
        assertTrue(checkSortedLinear(model));
    }

    /**
     * Method to retrieve list of parcels from input model and checks if the list is in sorted order.
     */
    private boolean checkSortedLinear(Model inputModel) {
        ObservableList<ReadOnlyParcel> listToCheck = inputModel.getFilteredParcelList();
        return checkSorted(listToCheck);
    }

    /**
     * Iterates recursively through the input list to check whether each element is in sorted order.
     */
    private boolean checkSorted(List listToCheck) {
        if (listToCheck.size() == 0 || listToCheck.size() == 1) {
            return true;
        } else {
            return compareParcels((Parcel) listToCheck.get(0), (Parcel) listToCheck.get(1))
                    && checkSorted(listToCheck.subList(1, listToCheck.size() - 1));
        }
    }

    /**
     * Compares two parcels, returns true if first Parcel should come before second Parcel
     * @param parcelOne
     * @param parcelTwo
     * @return true when ParcelOne compared to ParcelTwo returns less than 0;
     */
    private boolean compareParcels(ReadOnlyParcel parcelOne, ReadOnlyParcel parcelTwo) {
        int result = parcelOne.compareTo(parcelTwo);
        if (result < 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns an {@code EditCommand} with parameters {@code index} and {@code descriptor}
     */
    private EditCommand prepareCommand(Index index, EditParcelDescriptor descriptor) {
        EditCommand editCommand = new EditCommand(index, descriptor);
        editCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return editCommand;
    }
}
