package chess.ui.fxUI.application;

import chess.InitialPosition;
import chess.SaverUtils;
import chess.base.board.ChessState;
import chess.base.board.GameState;
import chess.misc.CastlingType;
import chess.misc.Point;
import chess.ui.UI;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.SystemUtils;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class ChessApplication extends Application {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private FxUI gui;

    @FXML
    private GridPane table;
    @FXML
    private Label gsLabel;
    @FXML
    private Button loadButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button undoButton;
    @FXML
    private TextField commandBar;

    public static void startFX() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //noinspection ConstantConditions
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ChessApplication.fxml"));
        primaryStage.setTitle("Chess Application");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    @FXML
    public void initialize() {
        this.gui = new FxUI();
    }

    @FXML
    public void applyLoad() {
        this.event = GuiEvents.LOAD;
    }

    @FXML
    public void applyUndo() {
        this.event = GuiEvents.UNDO;
    }

    @FXML
    public void applySave() {
        this.event = GuiEvents.SAVE;
    }

    @FXML
    public void ApplyMove() {
        this.event = GuiEvents.MOVE;
    }

    public class FxUI
    extends UI {
        private final AtomicBoolean finished = new AtomicBoolean(false);

        FxUI() {
            super(new ChessState(InitialPosition.get()));
            start();
        }

        @Override
        public void start() {
            var whitePlayer = new Thread(() -> this.play("White"));
            var blackPlayer = new Thread(() -> this.play("Black"));
            blackPlayer.setDaemon(true);
            blackPlayer.start();
            whitePlayer.setDaemon(true);
            whitePlayer.start();
        }

        private synchronized void play(String teamTag) {
            var running = true;
            do {
                try {
                    running = frame(teamTag);
                } catch (InterruptedException e) {
                    throw new IllegalStateException("interrupted");
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    ChessApplication.this.event = null;
                }
            } while (running);
            ChessApplication.this.loadButton.setDisable(true);
            ChessApplication.this.saveButton.setDisable(true);
            ChessApplication.this.undoButton.setDisable(true);
            ChessApplication.this.commandBar.setDisable(true);
        }

        private synchronized boolean frame(String teamTag) throws InterruptedException {
            if (!chessState.getCurrentTeam().getTeamTag().equals(teamTag)) {
                this.notify();
                this.wait();
            }
            if (this.finished.get()) {
                return false;
            }
            draw();
            if (chessState.getState() != GameState.RUNNING) {
                System.out.println(chessState.getState() + "!Program terminated");
                this.finished.set(true);
                this.notify();
                return false;
            }
            waitForEvent();
            switch (ChessApplication.this.event) {
                case SAVE: {
                    SaverUtils.saveState(this.chessState);
                    ChessApplication.this.event = null;
                    return true;
                }
                case LOAD: {
                    this.chessState = SaverUtils.loadState();
                    ChessApplication.this.event = null;
                    return true;
                }
                case UNDO: {
                    chessState.undo(1);
                    ChessApplication.this.event = null;
                    return true;
                }
                default: {
                    var command = ChessApplication.this.commandBar.getText();
                    ChessApplication.this.commandBar.clear();
                    if (command.matches("^0-0(-0)? [a-h][1-8]")) {
                        String[] temp = command.split("\\s");
                        chessState.makeCastling(CastlingType.parse(temp[0]), Point.parse(temp[1]));
                        ChessApplication.this.event = null;
                        return true;
                    }
                    if (!command.matches("^([a-h][1-8](\\s|$)){2}")) {
                        this.finished.set(true);
                        this.notify();
                        ChessApplication.this.event = null;
                        return false;
                    }
                    var pos = Arrays.stream(command.split("\\s")).map(Point::parse).toArray(Point[]::new);
                    chessState.move(pos[0], pos[1]);
                    ChessApplication.this.event = null;
                    return true;
                }
            }
        }

        private void draw() {
            Platform.runLater(this::drawInternal);
        }

        private void drawInternal() {
            //noinspection UnstableApiUsage
            var strings = Lists.reverse(
                    Streams.mapWithIndex
                            (
                                    Arrays.stream(chessState.getBoard().getBoard()),
                                    (x, i) -> Streams.mapWithIndex
                                            (
                                                    Arrays.stream(x), (y, j) ->
                                                            (char)((int)'a' + j) + String.valueOf(i + 1) + "(" + Arrays.stream(UnitSymbols.values()).
                                                                    filter(s -> s.match(y.getUnit())).
                                                                    findFirst().
                                                                    orElse(
                                                                            (i + j) % 2 == 0 ?
                                                                                SystemUtils.IS_OS_WINDOWS ? UnitSymbols.EvenCellNT : UnitSymbols.EvenCell :
                                                                                SystemUtils.IS_OS_WINDOWS ? UnitSymbols.OddCellNT : UnitSymbols.OddCell
                                                                    ).getSymbol() + ")"
                                            )
                            ).
                            map(x -> x.collect(Collectors.toList())).
                            collect(Collectors.toList())
            );
            for (int i = 0; i < strings.size(); i++) {
                for (int j = 0; j < strings.get(i).size(); j++) {
                    ((Label)ChessApplication.this.table.getChildren().get(i * 8 + j)).setText(strings.get(j).get(i));
                }
            }
            ChessApplication.this.gsLabel.setText(chessState.getState().toString());
        }
    }

    private volatile GuiEvents event;

    private void waitForEvent() {
        while (event == null) Thread.onSpinWait();
    }
}
