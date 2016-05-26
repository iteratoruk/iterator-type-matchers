
package iterator.test.matchers.type.annotation;

import iterator.Reflection;

import java.lang.annotation.Annotation;

public class FieldAnnotationMatcher<A extends Annotation, T> extends AnnotationMatcher<A, T> {

    private final String fieldName;

    public FieldAnnotationMatcher(String fieldName, AnnotationMap<A> expected) {
        super(expected);
        this.fieldName = fieldName;
    }

    @Override
    protected A findAnnotation(Class<T> item) {
        return Reflection.findFieldAnnotation(item, fieldName, getExpected().getAnnotationClass());
    }

}
