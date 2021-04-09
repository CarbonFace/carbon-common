package cn.carbonface.carboncommon.validate.annotations;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.Arrays;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy =EnumValue.Validator.class)
public @interface EnumValue {

    String message() default "枚举值不存在";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] in() default {};

    /**
     *
     * implements
     */
    class Validator implements ConstraintValidator<EnumValue, Object> {

        private Class<? extends Enum<?>> enumClass;
        private String[] in;
        private  Class<?>[] groups;

        @Override
        public void initialize(EnumValue constraintAnnotation) {
            in = constraintAnnotation.in();
            groups = constraintAnnotation.groups();
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
            if(null==value){
                return false;
            }
            if(!(in.length>0)){
                return false;
            }
            if (Arrays.asList(in).contains((String)value)){
                return true;
            }
            return false;
        }
    }
}