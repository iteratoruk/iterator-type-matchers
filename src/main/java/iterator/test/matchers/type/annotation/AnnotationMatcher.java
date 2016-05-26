
package iterator.test.matchers.type.annotation;

import java.lang.annotation.Annotation;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public abstract class AnnotationMatcher<A extends Annotation, T> extends TypeSafeMatcher<Class<T>> {

    private final AnnotationMap<A> expected;

    protected AnnotationMatcher(AnnotationMap<A> expected) {
        this.expected = expected;
    }

    @Override
    public final void describeTo(Description description) {
        description.appendText(expected.toString());
    }

    @Override
    protected final void describeMismatchSafely(Class<T> item, Description mismatchDescription) {
        AnnotationMap<A> map = AnnotationMap.of(findAnnotation(item));
        mismatchDescription.appendText(String.valueOf(map));
    }

    protected abstract A findAnnotation(Class<T> item);

    protected AnnotationMap<A> getExpected() {
        return expected;
    }

    @Override
    protected final boolean matchesSafely(Class<T> item) {
        return expected.equals(AnnotationMap.of(findAnnotation(item)));
    }

}
