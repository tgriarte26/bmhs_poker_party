package players;

import game.HandRanks;
import game.Player;

import java.util.Scanner;

public class AdamsPlayer extends Player {
    Scanner kb;
    double random = 0.9;

    public AdamsPlayer(String name) {
        super(name);
        kb = new Scanner(System.in);
    }

    @Override
    protected void takePlayerTurn() {
        if (shouldFold()) {
            fold();
        } else if (shouldRaise()) {
            raise(getGameState().getTableMinBet());
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
        if(getGameState().isActiveBet()){
            // fold if high card in hand on third round
            if(getGameState().getNumRoundStage() == 2){
                boolean worstHand = evaluatePlayerHand().getValue() == HandRanks.HIGH_CARD.getValue();
                return worstHand;
            }
            // fold if opponent bet is greater than 75% of my bank
            boolean betIsLarge = getGameState().getTableBet() > getBank() * 0.75;
            return betIsLarge;
        }
        return false;
    }

    @Override
    protected boolean shouldCheck() {
        // check if there is no active bet
            if(!getGameState().isActiveBet()){
                return true;
            }
        return false;
    }

    @Override
    protected boolean shouldCall() {
        if(getGameState().isActiveBet()){
            // call if bet is less than 20% of my bank
            boolean betIsReasonable = getGameState().getTableBet() < getBank() * 0.2;
            return betIsReasonable;
        }
        return false;
    }

    @Override
    protected boolean shouldRaise() {
        if(getGameState().isActiveBet()){

            if(getGameState().getNumRoundStage() == 0){ // raise if pair in hand on first round
                boolean hasFineHand = evaluatePlayerHand().getValue() == HandRanks.PAIR.getValue();
                return hasFineHand;
            }
            if(getGameState().getNumRoundStage() == 1){ // raise if between two pair and straight in hand on second round
                boolean hasBetterHand = evaluatePlayerHand().getValue() >= HandRanks.TWO_PAIR.getValue() && evaluatePlayerHand().getValue() <= HandRanks.STRAIGHT.getValue();
                return hasBetterHand;
            }
            if(getGameState().getNumRoundStage() == 2){ // raise if flush or full house in hand on third round
                boolean amazingBetterHand = evaluatePlayerHand().getValue() >= HandRanks.FLUSH.getValue() && evaluatePlayerHand().getValue() <= HandRanks.FULL_HOUSE.getValue();
                return amazingBetterHand;
            }
            if(getGameState().getNumRoundStage() == 3){ // raise if four of a kind or straight flush in hand on last round
                boolean hasGreatHand = evaluatePlayerHand().getValue() >= HandRanks.FOUR_OF_A_KIND.getValue() && evaluatePlayerHand().getValue() <= HandRanks.STRAIGHT_FLUSH.getValue();
                return hasGreatHand;
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
        boolean hasPerfectHand = evaluatePlayerHand().getValue() >= HandRanks.ROYAL_FLUSH.getValue();
        return hasPerfectHand;
    }
}