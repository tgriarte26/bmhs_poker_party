package players;

import game.HandRanks;
import game.Player;

import java.util.Random;

public class AidanPlayer extends Player {
    double random = 0.9;
    double maxRaiseMultiplier = 20.0;
    Random factor = new Random();

    boolean hasDecentHand;
    public AidanPlayer(String name) {
        super(name);
    }



    @Override
    protected void takePlayerTurn() {
        if (shouldFold()) {
            fold();
        } else if (shouldRaise()) {
            if (getGameState().getNumRoundStage() == 0){
                double randomFactor = 1.0 + factor.nextDouble() * (maxRaiseMultiplier - 16.0); // Random factor between 1.0 and maxRaiseMultiplier
                raise((int) (getGameState().getTableMinBet() * randomFactor));
            }
            if (getGameState().getNumRoundStage() == 1){
                double randomFactor = 1.0 + factor.nextDouble() * (maxRaiseMultiplier - 12.0); // Random factor between 1.0 and maxRaiseMultiplier
                raise((int) (getGameState().getTableMinBet() * randomFactor));
            }
            if (getGameState().getNumRoundStage() == 2){
                double randomFactor = 1.0 + factor.nextDouble() * (maxRaiseMultiplier - 8.0); // Random factor between 1.0 and maxRaiseMultiplier
                raise((int) (getGameState().getTableMinBet() * randomFactor));
            }
            if (getGameState().getNumRoundStage() == 3){
                double randomFactor = 1.0 + factor.nextDouble() * (maxRaiseMultiplier - 4.0); // Random factor between 1.0 and maxRaiseMultiplier
                raise((int) (getGameState().getTableMinBet() * randomFactor));
            }
        } else if (shouldCall()) {
            call();
        } else if (shouldCheck()) {
            check();
        } else if (shouldAllIn()) {
            allIn();
        }
    }

    @Override
    protected boolean shouldFold() {
        //if I have a pair midway through the game I fold and if the pot is 50% of my bank without a good hand
        if((hasDecentHand == evaluatePlayerHand().getValue() <= HandRanks.PAIR.getValue() && (getGameState().getNumRoundStage() == 2)) || (hasDecentHand == evaluatePlayerHand().getValue() <= HandRanks.TWO_PAIR.getValue() && getGameState().getTableBet() >= getBank() * 0.5)) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean shouldCheck() {
        // if there is no bet and I have less than a pair I check
        if(!getGameState().isActiveBet() || hasDecentHand == evaluatePlayerHand().getValue() <= HandRanks.HIGH_CARD.getValue()) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean shouldCall() {
        // checks if there is an active bet and that it is less than 20% of my current bank
        if(getGameState().isActiveBet() && getGameState().getTableBet() < getBank() * 0.2) {
            // if I have a full house midway through the game or if I have a 2 pair at the beginning of the game
            if((hasDecentHand == evaluatePlayerHand().getValue() <= HandRanks.FULL_HOUSE.getValue() && getGameState().getNumRoundStage() == 2) || (hasDecentHand == evaluatePlayerHand().getValue() <= HandRanks.TWO_PAIR.getValue() && getGameState().getNumRoundStage() == 1)) {
                shouldRaise();
            }
            // if I have less than a 2 pair at any stage, I check
            else if(hasDecentHand == evaluatePlayerHand().getValue() >= HandRanks.TWO_PAIR.getValue()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean shouldRaise() {
        //if I have a 2 pair, I raise
        if(hasDecentHand == evaluatePlayerHand().getValue() >= HandRanks.TWO_PAIR.getValue() || (hasDecentHand == evaluatePlayerHand().getValue() >= HandRanks.PAIR.getValue() && getGameState().getNumRoundStage() == 1)) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    protected boolean shouldAllIn() {
        //if I have a full house at the end of the game, I go all in
        if(evaluatePlayerHand().getValue() >= HandRanks.FULL_HOUSE.getValue() && getGameState().getNumRoundStage() == 3) {
            return true;
        }
        return false;
    }
}