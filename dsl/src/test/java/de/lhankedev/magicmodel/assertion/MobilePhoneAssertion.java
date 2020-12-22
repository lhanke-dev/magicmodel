package de.lhankedev.magicmodel.assertion;

import de.lhankedev.magicmodel.model.directaccess.MobilePhone;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class MobilePhoneAssertion extends AbstractAssert<MobilePhoneAssertion, MobilePhone> {

    private MobilePhoneAssertion(final MobilePhone actual) {
        super(actual, MobilePhoneAssertion.class);
    }

    public static MobilePhoneAssertion assertThat(final MobilePhone actual) {
        return new MobilePhoneAssertion(actual);
    }

    public MobilePhoneAssertion hasManufacturer(final String expected) {
        Assertions.assertThat(actual.getManufacturer())
                .isEqualTo(expected);
        return this;
    }

    public MobilePhoneAssertion hasScreenSize(final double expected) {
        Assertions.assertThat(actual.getScreenSize())
                .isEqualTo(expected);
        return this;
    }

    public MobilePhoneAssertion hasWeight(final int expected) {
        Assertions.assertThat(actual.getWeight())
                .isEqualTo(expected);
        return this;
    }


}
