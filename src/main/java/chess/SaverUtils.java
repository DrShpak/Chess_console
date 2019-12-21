package chess;

import chess.base.board.ChessState;
import org.javatuples.Triplet;
import xml.XmlDeserializer;
import xml.XmlSerializer;
import xml.XmlSerializerRegistry;

public class SaverUtils {
    private static final XmlSerializerRegistry registry;

    static {
        registry = new XmlSerializerRegistry();
        try {
            registry.addClass(
                    Triplet.class,
                    () -> Triplet.with(new Object(), new Object(), new Object()),
                    Triplet.class.getDeclaredField("val0"),
                    Triplet.class.getDeclaredField("val1"),
                    Triplet.class.getDeclaredField("val2")
            );
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void saveState(ChessState chessState) {
        XmlSerializer.saveXml(chessState, "board.xml", registry);
    }

    public static ChessState loadState() {
        return (ChessState) XmlDeserializer.loadXml("board.xml", registry);
    }
}
