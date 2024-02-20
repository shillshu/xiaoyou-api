package cn.sibetech.core.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;

public class DateTimeValidator implements ConstraintValidator<DateFormat, String> {

    private DateFormat dateFormat;

    @Override
    public void initialize(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String format = dateFormat.format();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            simpleDateFormat.parse(value);
            return true;
        } catch (Exception e) {
        }
        return false;
    }
}
