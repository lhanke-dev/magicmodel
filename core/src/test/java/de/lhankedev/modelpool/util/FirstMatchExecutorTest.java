package de.lhankedev.modelpool.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

class FirstMatchExecutorTest {


    @Test
    void testFirstMatchValueIsReturned() {
        final Function<String, String> mapOnSuccess = input -> format("%s-Success", input);

        final FirstMatchExecutor<String, String> cutReturnFirst = new FirstMatchExecutor<>("a");
        cutReturnFirst.addAlternative(input -> input.equals("a"), mapOnSuccess);
        cutReturnFirst.addAlternative(input -> input.equals("b"), mapOnSuccess);
        assertThat(cutReturnFirst.perform())
                .isPresent()
                .contains(mapOnSuccess.apply("a"));

        final FirstMatchExecutor<String, String> cutReturnSecond = new FirstMatchExecutor<>("b");
        cutReturnSecond.addAlternative(input -> input.equals("a"), mapOnSuccess);
        cutReturnSecond.addAlternative(input -> input.equals("b"), mapOnSuccess);
        assertThat(cutReturnSecond.perform())
                .isPresent()
                .contains(mapOnSuccess.apply("b"));
    }

    @Test
    void testOnlyFirstMapFunctionIsExecuted() {
        final AtomicInteger firstExecuted = new AtomicInteger(0);
        final AtomicInteger secondExecuted = new AtomicInteger(0);

        final FirstMatchExecutor<String, Integer> cut = new FirstMatchExecutor<>("a");
        cut.addAlternative(input -> input.equals("a"), input -> firstExecuted.incrementAndGet());
        cut.addAlternative(input -> input.equals("b"), input -> secondExecuted.incrementAndGet());

        cut.perform();

        assertThat(firstExecuted.get())
                .isEqualTo(1);
        assertThat(secondExecuted.get())
                .isEqualTo(0);
    }

    @Test
    void testNoMatchNothingIsExecuted() {
        final AtomicInteger firstExecuted = new AtomicInteger(0);
        final AtomicInteger secondExecuted = new AtomicInteger(0);

        final FirstMatchExecutor<String, Integer> cut = new FirstMatchExecutor<>("a");
        cut.addAlternative(input -> input.equals("b"), input -> firstExecuted.incrementAndGet());
        cut.addAlternative(input -> input.equals("b"), input -> secondExecuted.incrementAndGet());

        cut.perform();

        assertThat(firstExecuted.get())
                .isEqualTo(0);
        assertThat(secondExecuted.get())
                .isEqualTo(0);
    }


}