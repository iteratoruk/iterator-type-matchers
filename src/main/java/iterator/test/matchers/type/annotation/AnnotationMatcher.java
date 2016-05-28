/**
 * Copyright (C) 2016 Iterator Ltd. (iteratoruk@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
