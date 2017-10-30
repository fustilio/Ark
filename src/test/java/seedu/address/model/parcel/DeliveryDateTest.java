package seedu.address.model.parcel;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DeliveryDateTest {

    @Test
    public void isValidDate() {
        // invalid dates
        assertFalse(DeliveryDate.isValidDate("")); // empty string
        assertFalse(DeliveryDate.isValidDate(" ")); // spaces only
        assertFalse(DeliveryDate.isValidDate("91")); // less than 3 numbers
        assertFalse(DeliveryDate.isValidDate("date")); // non-numeric
        assertFalse(DeliveryDate.isValidDate("9011p041")); // alphabets within digits
        assertFalse(DeliveryDate.isValidDate("9312 1534")); // spaces within digits
        assertFalse(DeliveryDate.isValidDate("32-05-1995")); // too many days in a month
        assertFalse(DeliveryDate.isValidDate("05-13-1995")); // too many months in a year
        assertFalse(DeliveryDate.isValidDate("32/05/1995")); // too many days in a month
        assertFalse(DeliveryDate.isValidDate("05/13/1995")); // too many months in a year
        assertFalse(DeliveryDate.isValidDate("32.05.1995")); // too many days in a month
        assertFalse(DeliveryDate.isValidDate("05.13.1995")); // too many months in a year
        assertFalse(DeliveryDate.isValidDate("29.02.2001")); // Not a leap year
        assertFalse(DeliveryDate.isValidDate("0.02.2001")); // single digit but wrong day
        assertFalse(DeliveryDate.isValidDate("29.0.2001")); // single digit but wrong month


        // valid dates
        assertTrue(DeliveryDate.isValidDate("20-12-1990")); // exactly 3 numbers
        assertTrue(DeliveryDate.isValidDate("01-01-2001"));
        assertTrue(DeliveryDate.isValidDate("01/01/2001"));
        assertTrue(DeliveryDate.isValidDate("01.01.2001"));
        assertTrue(DeliveryDate.isValidDate("31.1.2001")); // single digit month
        assertTrue(DeliveryDate.isValidDate("1.01.2001")); // single digit day
        assertTrue(DeliveryDate.isValidDate("1.1.2001")); // single digit day and month
        assertTrue(DeliveryDate.isValidDate("29.02.2004")); // is leap year



    }
}
