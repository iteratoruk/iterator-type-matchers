
package iterator.test.matchers.type.annotation;

import static org.junit.Assert.assertThat;

import java.lang.annotation.Annotation;

import org.hamcrest.Matcher;
import org.junit.Test;

public class FieldAnnotationMatcherTest {

    // class under test
    private static <A extends Annotation, T> Matcher<Class<T>> hasFieldAnnotation(String fieldName, AnnotationMap<A> expected) {
        return new FieldAnnotationMatcher<>(fieldName, expected);
    }

    @TestAnnotationWithValue("foo")
    private Object matchingSimpleAnnotation;

    @Test
    public void shouldMatchSimpleAnnotationWithValue() throws Exception {
        assertThat(FieldAnnotationMatcherTest.class, hasFieldAnnotation("matchingSimpleAnnotation", AnnotationMap.from(TestAnnotationWithValue.class).set("value", "foo")));
    }

    @Test(expected = AssertionError.class)
    public void shouldNotMatchSimpleAnnotationWithValue() throws Exception {
        assertThat(FieldAnnotationMatcherTest.class, hasFieldAnnotation("matchingSimpleAnnotation", AnnotationMap.from(TestAnnotationWithValue.class).set("value", "bar")));
    }

}
