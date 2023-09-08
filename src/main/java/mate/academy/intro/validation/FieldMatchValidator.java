package mate.academy.intro.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String matchedFieldName;
    private String fieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        matchedFieldName = constraintAnnotation.matchedFieldName();
        fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object objectValue = field.get(object);
            Field matchedField = object.getClass().getDeclaredField(matchedFieldName);
            matchedField.setAccessible(true);
            Object matchedObjectValue = matchedField.get(object);
            return objectValue == null && matchedObjectValue == null || objectValue != null
                    && objectValue.equals(matchedObjectValue);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            return false;
        }
    }
}
