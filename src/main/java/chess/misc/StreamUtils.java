package chess.misc;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamUtils {
    public static <R> Stream<R> takeWhileEx(Stream<R> stream, Predicate<? super R> predicate) {
        var continueStream = new boolean[]{true};
        return stream.takeWhile(x -> {
            if (continueStream[0]) {
                continueStream[0] = predicate.test(x);
                return true;
            }

            return false;
        });
    }

    @SuppressWarnings({"unchecked", "UnusedReturnValue"})
    public static <R, X> Stream<R> mapEx(R seed, Stream<X> stream, BiFunction<R, X, ? super R> action) {
        var previousElement = new Object[]{seed};
        var collection = new ArrayList<>();
        for (X x : stream.collect(Collectors.toList())) {
            collection.add((previousElement[0] = action.apply((R)previousElement[0], x)));
        }
        return collection.stream().map(x -> (R)x);
        //return stream.map(x -> (R) (previousElement[0] = action.apply((R)previousElement[0], x)));
    }
}
