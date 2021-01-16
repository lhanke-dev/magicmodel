package de.lhankedev.modelpool.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@RequiredArgsConstructor
public class FirstMatchExecutor<I, R> {

    @RequiredArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @ToString
    @Getter
    class Alternative {
        final Predicate<I> condition;
        final Function<I, R> action;
    }

    final I inputValue;

    List<Alternative> alternatives = new ArrayList<>();

    public FirstMatchExecutor<I, R> addAlternative(final Predicate<I> predicate, final Function<I, R> action) {
        this.alternatives.add(new Alternative(predicate, action));
        return this;
    }

    public Optional<R> perform() {
        return alternatives.stream()
                .filter(alternative -> alternative.getCondition().test(inputValue))
                .findFirst()
                .map(firstMatchingAlternative -> firstMatchingAlternative.getAction().apply(inputValue));
    }
}
