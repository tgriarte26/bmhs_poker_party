package players;

import game.HandRanks;
import game.Player;

public class SimpleNPCPlayer extends Player {

    public SimpleNPCPlayer(String name) {
        super(name);
    }

    @Override
    protected void takePlayerTurn() {
        if (shouldFold()) {
            fold();
        } else if (shouldCheck()) {
            check();
        } else if (shouldCall()) {
            call();
        } else if (shouldRaise()) {
            raise(getGameState().getTableMinBet()); // Example: always raises the minimum bet
        } else if (shouldAllIn()) {
            allIn();
        }        
    }

    @Override
    protected boolean shouldFold() {
        // Example logic: fold if the bet is more than 10% of the bank
        return getGameState().getTableBet() > getBank() * 0.1;
    }

    @Override
    protected boolean shouldCheck() {
        // Example logic: check if no active bet
        return !getGameState().isActiveBet();
    }

    @Override
    protected boolean shouldCall() {
        // Call only if the bet is very small compared to the bank, making it less likely to call on larger bets
        return getGameState().isActiveBet() && getGameState().getTableBet() < getBank() * 0.03;
    }

    @Override
    protected boolean shouldRaise() {
        // Expand raise conditions to include scenarios where previously a call might have been made
        // Example: raise if the hand is decent (lower than before) or if the bet is a small percentage of the bank
        boolean hasDecentHand = evaluatePlayerHand().getValue() > HandRanks.HIGH_CARD.getValue();
        boolean betIsSmallPercentageOfBank = getGameState().getTableBet() < getBank() * 0.05;
        return hasDecentHand || betIsSmallPercentageOfBank;
    }

    @Override
    protected boolean shouldAllIn() {
        // Example logic: all in if the hand is very good
        return evaluatePlayerHand().getValue() >= HandRanks.FULL_HOUSE.getValue();
    }
}
