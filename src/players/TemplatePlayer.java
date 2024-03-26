package players;

import game.Player;

public class TemplatePlayer extends Player {

    public TemplatePlayer(String name) {
        super(name);
    }

    @Override
    public void takePlayerTurn() {
        if(shouldFold()) {
            fold();
        }
        else if(shouldCheck()) {
            check();
        }
        else if(shouldCall()) {
            call();
        }
        else if(shouldRaise()) {
            raise(getGameState().getTableMinBet());
        }
        else if(shouldAllIn()) {
            allIn();
        }

    }

    @Override
    public boolean shouldFold() {
        return true;
    }

    @Override
    public boolean shouldCheck() {
        return false;
    }

    @Override
    public boolean shouldCall() {
        return false;
    }

    @Override
    public boolean shouldRaise() {
        return true;
    }

    @Override
    public boolean shouldAllIn() {
        return false;
    }
}