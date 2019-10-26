package chess.tests.smoke;

import chess.base.Team;
import chess.unit.Unit;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.Objects;

public class MainTest {
    private static final Class<?>[] ARG_TYPES = new Class<?>[]{Team.class};
    private static final String PACKAGE_NAME = "chess.units";

    @Rule
    public final ErrorCollector collector = new ErrorCollector();

    @Test
    public void main() {
        new Reflections(PACKAGE_NAME).
                getSubTypesOf(Unit.class).stream().
                filter(x -> !Modifier.isAbstract(x.getModifiers())).
                map(
                        x -> {
                            try {
                                x.getConstructor(ARG_TYPES);
                                return null;
                            } catch (Throwable e) {
                                return e;
                            }
                        }
                ).
                filter(Objects::nonNull).
                forEach(collector::addError);
    }
}