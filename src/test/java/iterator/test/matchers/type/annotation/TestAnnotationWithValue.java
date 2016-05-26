
package iterator.test.matchers.type.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

// TODO nested annotation types
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface TestAnnotationWithValue {

    String value();

    String anotherProperty() default "bar";

}
