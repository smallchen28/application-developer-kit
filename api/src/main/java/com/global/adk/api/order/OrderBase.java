package com.global.adk.api.order;

import com.global.common.lang.validator.YJFValidatorFactory;
import com.global.common.service.OrderCheckException;
import com.global.common.service.Validatable;

import javax.validation.ConstraintViolation;
import java.io.Serializable;
import java.util.Set;

/**
 * @author hasulee<ligen@yiji.com>
 * @version 1.0.0
 * @see
 * @since 16/12/6
 */
public class OrderBase implements Validatable,Serializable {
    private static final long serialVersionUID = 4214828300891828570L;

    public void checkWithGroup(Class<?>... groups) {
        Set<ConstraintViolation<OrderBase>> constraintViolations = YJFValidatorFactory.INSTANCE.getValidator()
                .validate(this, groups);
        validate(constraintViolations);
    }

    private <T> void validate(Set<ConstraintViolation<T>> constraintViolations) {
        OrderCheckException exception = null;
        if (constraintViolations != null && !constraintViolations.isEmpty()) {
            exception = new OrderCheckException();
            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                exception.addError(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
            }
        }
        if (exception != null) {
            throw exception;
        }
    }
}
