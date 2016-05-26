
package iterator.test.matchers.type.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface TestAnnotationWithoutValue {

    boolean[] booleanArrayProperty() default { true, false };

    boolean booleanProperty() default true;

    byte[] byteArrayProperty() default { 1, 2, 3 };

    byte byteProperty() default 4;

    char[] charArrayProperty() default { 'b', 'a', 'z' };

    char charProperty() default 'f';

    double[] doubleArrayProperty() default { 24.42, 42.24 };

    double doubleProperty() default 24.42D;

    float[] floatArrayProperty() default { 42.24F, 24.42F };

    float floatProperty() default 42.24F;

    int[] intArrayProperty() default { 2, 1, 3 };

    int intProperty() default 42;

    long[] longArrayProperty() default { 42, 24 };

    long longProperty() default 24;

    short[] shortArrayProperty() default { 3, 2, 1 };

    short shortProperty() default 2;

    String[] stringArrayProperty() default { "bar" };

    String stringProperty() default "foo";

    MetasyntacticVariable enumProperty() default MetasyntacticVariable.FOO;

    MetasyntacticVariable[] enumArrayProperty() default { MetasyntacticVariable.FOO };

    Class<?> classProperty() default Object.class;

    Class<?>[] classArrayProperty() default { Object.class };

    TestAnnotationWithValue annotationProperty() default @TestAnnotationWithValue("foo");

    TestAnnotationWithValue[] annotationArrayProperty() default { @TestAnnotationWithValue("foo") };

}
