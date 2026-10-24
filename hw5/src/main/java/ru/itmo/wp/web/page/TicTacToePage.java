package ru.itmo.wp.web.page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class TicTacToePage {
    private static final int FIELD_SIZE = 3;
    private static final String STATE_ATTRIBUTE = TicTacToePage.class.getName() + ".state";

    private void action(HttpServletRequest request, Map<String, Object> view) {
        view.put("state", getState(request));
    }

    private void onMove(HttpServletRequest request, Map<String, Object> view) {
        State state = getState(request);
        int[] cell = getSelectedCell(request);
        if (cell != null) {
            state.makeMove(cell[0], cell[1]);
        }
        view.put("state", state);
    }

    private void newGame(HttpServletRequest request, Map<String, Object> view) {
        State state = new State();
        request.getSession().setAttribute(STATE_ATTRIBUTE, state);
        view.put("state", state);
    }

    private State getState(HttpServletRequest request) {
        HttpSession session = request.getSession();
        State state = (State) session.getAttribute(STATE_ATTRIBUTE);
        if (state == null) {
            state = new State();
            session.setAttribute(STATE_ATTRIBUTE, state);
        }
        return state;
    }

    private int[] getSelectedCell(HttpServletRequest request) {
        String selectedCell = null;
        for (String name : request.getParameterMap().keySet()) {
            if (name.startsWith("cell_")) {
                selectedCell = name;
                break;
            }
        }

        if (selectedCell == null || selectedCell.length() != 7) {
            return null;
        }

        char row = selectedCell.charAt(5);
        char col = selectedCell.charAt(6);
        if (row < '0' || row >= '0' + FIELD_SIZE || col < '0' || col >= '0' + FIELD_SIZE) {
            return null;
        }

        return new int[]{row - '0', col - '0'};
    }

    public static final class State {
        private final String[][] cells = new String[FIELD_SIZE][FIELD_SIZE];
        private boolean crossesMove = true;
        private String phase = "RUNNING";

        public int getSize() {
            return FIELD_SIZE;
        }

        public String[][] getCells() {
            return cells;
        }

        public boolean isCrossesMove() {
            return crossesMove;
        }

        public String getPhase() {
            return phase;
        }

        private synchronized void makeMove(int row, int col) {
            if (!"RUNNING".equals(phase) || cells[row][col] != null) {
                return;
            }

            String mark = crossesMove ? "X" : "O";
            cells[row][col] = mark;

            if (hasWon(mark)) {
                phase = crossesMove ? "WON_X" : "WON_O";
            } else if (isFull()) {
                phase = "DRAW";
            } else {
                crossesMove = !crossesMove;
            }
        }

        private boolean hasWon(String mark) {
            for (int i = 0; i < FIELD_SIZE; i++) {
                if (mark.equals(cells[i][0]) && mark.equals(cells[i][1]) && mark.equals(cells[i][2])) {
                    return true;
                }
                if (mark.equals(cells[0][i]) && mark.equals(cells[1][i]) && mark.equals(cells[2][i])) {
                    return true;
                }
            }

            return mark.equals(cells[0][0]) && mark.equals(cells[1][1]) && mark.equals(cells[2][2])
                    || mark.equals(cells[0][2]) && mark.equals(cells[1][1]) && mark.equals(cells[2][0]);
        }

        private boolean isFull() {
            for (String[] row : cells) {
                for (String cell : row) {
                    if (cell == null) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}
