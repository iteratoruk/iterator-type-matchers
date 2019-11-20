/**
 * Copyright © 2016 Iterator Ltd. (iteratoruk@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package iterator.test.matchers.type.annotation;

import iterator.Reflection;

import java.lang.annotation.Annotation;

public class TypeAnnotationMatcher<A extends Annotation, T> extends AnnotationMatcher<A, T> {

  public TypeAnnotationMatcher(AnnotationMap<A> expected) {
    super(expected);
  }

  @Override
  protected A findAnnotation(Class<T> item) {
    return Reflection.findTypeAnnotation(item, getExpected().getAnnotationClass());
  }
}
