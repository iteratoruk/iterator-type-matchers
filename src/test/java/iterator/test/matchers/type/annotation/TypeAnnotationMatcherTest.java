
package iterator.test.matchers.type.annotation;

import static org.junit.Assert.assertThat;

import java.lang.annotation.Annotation;

import org.hamcrest.Matcher;
import org.junit.Test;

@TestAnnotationWithValue("foo")
public class TypeAnnotationMatcherTest {

    // class under test
    private static <A extends Annotation, T> Matcher<Class<T>> hasTypeAnnotation(AnnotationMap<A> expected) {
        return new TypeAnnotationMatcher<>(expected);
    }

    @Test
    public void shouldMatchSimpleAnnotationWithValue() throws Exception {
        assertThat(TypeAnnotationMatcherTest.class, hasTypeAnnotation(AnnotationMap.from(TestAnnotationWithValue.class).set("value", "foo")));
    }

    @Test(expected = AssertionError.class)
    public void shouldNotMatchSimpleAnnotationWithValue() throws Exception {
        assertThat(TypeAnnotationMatcherTest.class, hasTypeAnnotation(AnnotationMap.from(TestAnnotationWithValue.class).set("value", "bar")));
    }

    @Test(expected = AssertionError.class)
    public void shouldNotMatchUnannotatedField() throws Exception {
        assertThat(FieldAnnotationMatcherTest.class, hasTypeAnnotation(AnnotationMap.from(TestAnnotationWithValue.class)));
    }

}
