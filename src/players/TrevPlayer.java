package players;

import game.HandRanks;
import game.Player;
import java.util.Random;

import java.util.Scanner;

public class TrevPlayer extends Player {
    Scanner kb;
    double random = 0.9;
    double maxRaiseMultiplier = 20.0;
    Random factor = new Random();

    public TrevPlayer(String name) {
        super(name);
        kb = new Scanner(System.in);
    }

    @Override
    protected void takePlayerTurn() {
        if (shouldFold()) {
            fold();
        } else if (shouldRaise()) {
            if (getGameState().getNumRoundStage() == 0) {
                double randomFactor = 1.0 + factor.nextDouble() * (maxRaiseMultiplier - 16.0); // Random factor between 1.0 and maxRaiseMultiplier
                raise((int) (getGameState().getTableMinBet() * randomFactor));
            }
            if (getGameState().getNumRoundStage() == 1) {
                double randomFactor = 1.0 + factor.nextDouble() * (maxRaiseMultiplier - 12.0); // Random factor between 1.0 and maxRaiseMultiplier
                raise((int) (getGameState().getTableMinBet() * randomFactor));
            }
            if (getGameState().getNumRoundStage() == 2) {
                double randomFactor = 1.0 + factor.nextDouble() * (maxRaiseMultiplier - 8.0); // Random factor between 1.0 and maxRaiseMultiplier
                raise((int) (getGameState().getTableMinBet() * randomFactor));
            }
            if (getGameState().getNumRoundStage() == 3) {
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
        if (getGameState().isActiveBet()) {
            // fold if high card in hand on third round
            if (getGameState().getNumRoundStage() == 2) {
                boolean worstHand = evaluatePlayerHand().getValue() == HandRanks.HIGH_CARD.getValue();
                return worstHand;
            }
            // fold if opponent bet is greater than 50% of my bank
            boolean betIsLarge = getGameState().getTableBet() > getBank() * 0.50;
            return betIsLarge;
        }
        return false;
    }

    @Override
    protected boolean shouldCheck() {
        // check if there is no active bet
        if (!getGameState().isActiveBet()) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean shouldCall() {
        if (getGameState().isActiveBet()) {
            if (evaluatePlayerHand().getValue() != HandRanks.HIGH_CARD.getValue()) { // call if there is a bet and no high card
                // call if bet is less than or equal to 40% of my bank
                boolean betIsReasonable = getGameState().getTableBet() <= getBank() * 0.50;
                return betIsReasonable;
            } else if (evaluatePlayerHand().getValue() == HandRanks.HIGH_CARD.getValue()) {
                // if high card in third or fourth round, fold
                if(getGameState().getNumRoundStage() == 2 || getGameState().getNumRoundStage() == 3){
                    fold();
                // if high card in first or second round, raise by table min bet
                } else if(getGameState().getNumRoundStage() == 0 || getGameState().getNumRoundStage() == 1){
                    raise(getGameState().getTableMinBet());
                }

            }
        }
        return false;
    }

    @Override
    protected boolean shouldRaise() {
        // raise if pair or two pair in hand on first round or second round
        if (getGameState().getNumRoundStage() == 0 || getGameState().getNumRoundStage() == 1) {
            boolean hasPairHand = evaluatePlayerHand().getValue() == HandRanks.PAIR.getValue();
            boolean hasTwoPairHand = evaluatePlayerHand().getValue() == HandRanks.TWO_PAIR.getValue();
            return hasPairHand || hasTwoPairHand;
        }
        // raise if four of a kind or straight flush in hand on last round
        if (getGameState().getNumRoundStage() == 3) {
            boolean handValue = evaluatePlayerHand().getValue() >= HandRanks.FOUR_OF_A_KIND.getValue() && evaluatePlayerHand().getValue() <= HandRanks.STRAIGHT_FLUSH.getValue();
            return handValue;
        }
        // for three or more players remaining in round
        if (getGameState().getNumPlayersRemainingRound() >= 3) {
            // raise if straight or flush in hand on second round
            if (getGameState().getNumRoundStage() == 1) {
                boolean hasStraightHand = evaluatePlayerHand().getValue() >= HandRanks.STRAIGHT.getValue();
                boolean hasFlushHand = evaluatePlayerHand().getValue() >= HandRanks.FLUSH.getValue();
                return hasStraightHand || hasFlushHand;
            }
            // raise if full house in hand on third round
            if (getGameState().getNumRoundStage() == 2) {
                boolean hasFullHouse = evaluatePlayerHand().getValue() >= HandRanks.FULL_HOUSE.getValue();
                return hasFullHouse;
            }
        } else if (getGameState().getNumPlayersRemainingRound() == 2) { // for two players remaining in round
            // raise if straight, flush, or full house in hand on second and third round
            if (getGameState().getNumRoundStage() == 1 || getGameState().getNumRoundStage() == 2) {
                boolean hasStraightHand = evaluatePlayerHand().getValue() >= HandRanks.STRAIGHT.getValue();
                boolean hasFlushHand = evaluatePlayerHand().getValue() >= HandRanks.FLUSH.getValue();
                boolean hasFullHouse = evaluatePlayerHand().getValue() >= HandRanks.FULL_HOUSE.getValue();
                return hasStraightHand || hasFlushHand || hasFullHouse;
            }
        }
        return false;
    }

    @Override
    protected boolean shouldAllIn() {
        //10% chance of bluff when there is an active bet
        if(getGameState().isActiveBet()){
            if(evaluatePlayerHand().getValue() == HandRanks.HIGH_CARD.getValue()){
                double bluff = Math.random();
                return bluff > random; // 0.9
            }
        }

        // royal flush = all in
        if(evaluatePlayerHand().getValue() == HandRanks.ROYAL_FLUSH.getValue()){
            return true;
        }
        return false;
    }
}