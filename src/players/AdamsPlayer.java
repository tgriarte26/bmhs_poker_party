package players;

import game.Player;

public class AdamsPlayer extends Player {

    public AdamsPlayer(String name) {
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
        int betOnTable = getGameState().getTableBet();

        int myCurrentBank = getBank();
        int currentGameStage = getGameState().getNumRoundStage();

        if(betOnTable > myCurrentBank * .25 || currentGameStage > 1) {
            return true;
        }

        return false;
    }

    @Override
    public boolean shouldCheck() {
        if(!isBetActive()) {
            return true;
        }
        return false;

    }

    @Override
    public boolean shouldCall() {
        int betOnTable = getGameState().getTableBet();
        int myCurrentBank = getBank();

        if(isBetActive() && betOnTable < myCurrentBank * .1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldRaise() {
        int handValue = evaluatePlayerHand().getValue();
        if(handValue >= 2) {

        }



        int potOnTable = getGameState().getTablePot();
        int betOnTable = getGameState().getTableBet();

        int myCurrentBank = getBank();
        int currentGameStage = getGameState().getNumRoundStage();



        return false;
    }

    @Override
    public boolean shouldAllIn() {
        return false;
    }
}