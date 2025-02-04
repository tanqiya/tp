package teletubbies.logic.parser;

import static teletubbies.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static teletubbies.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static teletubbies.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static teletubbies.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static teletubbies.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static teletubbies.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static teletubbies.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static teletubbies.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static teletubbies.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static teletubbies.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static teletubbies.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static teletubbies.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static teletubbies.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static teletubbies.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static teletubbies.logic.commands.CommandTestUtil.UUID_DESC_AMY;
import static teletubbies.logic.commands.CommandTestUtil.UUID_DESC_BOB;
import static teletubbies.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static teletubbies.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static teletubbies.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static teletubbies.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static teletubbies.logic.parser.CommandParserTestUtil.assertParseFailure;
import static teletubbies.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static teletubbies.testutil.TypicalPersons.NO_ADDRESS_AMY;
import static teletubbies.testutil.TypicalPersons.NO_EMAIL_AMY;
import static teletubbies.testutil.TypicalPersons.NO_TAGS_AMY;
import static teletubbies.testutil.TypicalPersons.NO_TAGS_BOB;

import org.junit.jupiter.api.Test;

import teletubbies.logic.commands.AddCommand;
import teletubbies.model.person.Address;
import teletubbies.model.person.Email;
import teletubbies.model.person.Name;
import teletubbies.model.person.Person;
import teletubbies.model.person.Phone;
import teletubbies.testutil.PersonBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(NO_TAGS_BOB).build();

        // whitespace only preamble
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + UUID_DESC_BOB, new AddCommand(expectedPerson));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_AMY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + UUID_DESC_BOB, new AddCommand(expectedPerson));

        // multiple phones - last phone accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_AMY + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + UUID_DESC_BOB, new AddCommand(expectedPerson));

        // multiple emails - last email accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_AMY + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + UUID_DESC_BOB, new AddCommand(expectedPerson));

        // multiple addresses - last address accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_AMY
                + ADDRESS_DESC_BOB + UUID_DESC_BOB, new AddCommand(expectedPerson));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Person expectedPerson = new PersonBuilder(NO_TAGS_AMY).build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + UUID_DESC_AMY, new AddCommand(expectedPerson));

        // missing address
        Person expectedPerson1 = new PersonBuilder(NO_ADDRESS_AMY).build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + UUID_DESC_AMY,
                new AddCommand(expectedPerson1));

        // missing email
        Person expectedPerson2 = new PersonBuilder(NO_EMAIL_AMY).build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + ADDRESS_DESC_AMY + UUID_DESC_AMY,
                new AddCommand(expectedPerson2));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing name
        assertParseFailure(parser, PHONE_DESC_BOB + VALID_EMAIL_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing phone
        assertParseFailure(parser, NAME_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_ADDRESS_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB,
                Email.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC,
                Address.MESSAGE_CONSTRAINTS);


        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
