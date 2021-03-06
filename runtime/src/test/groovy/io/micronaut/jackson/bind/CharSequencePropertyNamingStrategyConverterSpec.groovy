/*
 * Copyright 2017-2018 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.micronaut.jackson.bind


import com.fasterxml.jackson.databind.PropertyNamingStrategy
import io.micronaut.core.convert.ArgumentConversionContext
import io.micronaut.core.convert.ConversionContext
import io.micronaut.core.convert.ConversionError
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class CharSequencePropertyNamingStrategyConverterSpec extends Specification {
    @Shared
    CharSequencePropertyNamingStrategyConverter converter = new CharSequencePropertyNamingStrategyConverter()

    @Unroll
    void 'test configuring #propertyNaminStrategyString converts to correct PropertyNamingStrategy'() {
        when:
        Optional<PropertyNamingStrategy> actualPropertyNamingStrategy = converter.convert(
                propertyNaminStrategyString, PropertyNamingStrategy)

        then:
        actualPropertyNamingStrategy.isPresent()
        actualPropertyNamingStrategy.get() == expectedPropertyNamingStrategy

        where:
        propertyNaminStrategyString | expectedPropertyNamingStrategy
        'SNAKE_CASE'                | PropertyNamingStrategy.SNAKE_CASE
        'UPPER_CAMEL_CASE'          | PropertyNamingStrategy.UPPER_CAMEL_CASE
        'LOWER_CAMEL_CASE'          | PropertyNamingStrategy.LOWER_CAMEL_CASE
        'LOWER_CASE'                | PropertyNamingStrategy.LOWER_CASE
        'KEBAB_CASE'                | PropertyNamingStrategy.KEBAB_CASE
    }

    @Unroll
    void 'test invalid String #invalidString throws IllegalArgumentException'() {
        when:
        ConversionContext ctx = ArgumentConversionContext.of(CharSequence)
        converter.convert(invalidString, PropertyNamingStrategy, ctx)
        ConversionError conversionError = ctx.last()

        then:
        conversionError.cause instanceof IllegalArgumentException
        conversionError.cause.message == "Unable to convert '$invalidString' to a com.fasterxml.jackson.databind.PropertyNamingStrategy"

        where:
        invalidString | _
        null          | _
        ''            | _
        'CASE'        | _
    }
}
