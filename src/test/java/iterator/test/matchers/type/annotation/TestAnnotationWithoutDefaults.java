
package iterator.test.matchers.type.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface TestAnnotationWithoutDefaults {

    boolean[] booleanArrayProperty();

    boolean booleanProperty();

    byte[] byteArrayProperty();

    byte byteProperty();

    char[] charArrayProperty();

    char charProperty();

    double[] doubleArrayProperty();

    double doubleProperty();

    float[] floatArrayProperty();

    float floatProperty();

    int[] intArrayProperty();

    int intProperty();

    long[] longArrayProperty();

    long longProperty();

    short[] shortArrayProperty();

    short shortProperty();

    String[] stringArrayProperty();

    String stringProperty();

    MetasyntacticVariable enumProperty();

    MetasyntacticVariable[] enumArrayProperty();

    Class<?> classProperty();

    Class<?>[] classArrayProperty();

    TestAnnotationWithValue annotationProperty();

    TestAnnotationWithValue[] annotationArrayProperty();

}
