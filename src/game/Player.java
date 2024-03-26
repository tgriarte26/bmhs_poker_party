package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Player {
    // The name of the player.
    private String name;

    // A list to hold the cards that are currently in the player's hand.
    private List<Card> handCards;

    // The player's current bank balance and the current bet amount.
    private int bank, bet;

    // Flags to indicate the player's status in the game.
    private boolean isDealer, isSmall, isBig, isFold, isAllIn, isBetActive, isBet, isActionRemaining;
    // Flags to indicate whether the player can call or check in the current turn.
    private boolean canCall, canCheck; 

    // The current state of the game, containing information about the game's progress.
    private GameState state;

    // The last action taken by the player.
    private PlayerActions playerAction;

    // An evaluator to assess the strength of the player's hand.
    private Evaluator evaluator;

    /**
     * Constructs a new Player with the specified name.
     * Initializes the player's hand, bank balance, and various status flags.
     * @param name The name of the player.
     */
    public Player(String name) {
        this.name = name;

        this.handCards = new ArrayList<>();

        this.bank = 1000; // Default starting bank balance.
        this.bet = 0; // Initial bet is zero.

        // Initially, the player is not in any special state.
        this.isDealer = false;
        this.isSmall = false;
        this.isBig = false;
        this.isFold = false;
        this.isAllIn = false;
        this.isBetActive = false;
        this.isActionRemaining = false;
        this.isBet = false;

        // By default, a player can call or check.
        this.canCall = true; 
        this.canCheck = true; 

        this.playerAction = null; // No action has been taken yet.

        this.evaluator = new Evaluator(); // Initialize the hand evaluator.
    }

    /**
     * Executes the fold action for the player.
     * If there is only one player remaining in the round, folding is illegal,
     * and the player is forced to check instead. Otherwise, the player's fold status is set to true,
     * and their last action is updated to FOLD.
     */
    protected void fold() {
        if(state.getNumPlayersRemainingRound() == 1) {
            System.out.println("###ILLEGAL FOLD, ONE PLAYER REMAINING, FORCING CHECK###");
            check(); // Force check if folding is not allowed.
        } else {
            isFold = true; // Set the player's fold status to true.
            playerAction = PlayerActions.FOLD; // Update the player's last action to FOLD.
        }
    }

    /**
     * Executes the check action for the player.
     * If there is an active bet and more than one player remaining in the round, checking is not allowed,
     * and the player is forced to call instead. If checking or calling is not possible, the player is forced to fold.
     * Otherwise, the player's last action is updated to CHECK.
     */
    protected void check() {
        if(state.isActiveBet() && state.getNumPlayersRemainingRound() != 1) {
            canCheck = false;
            if(canCall || canCheck) {
                System.out.println("###ILLEGAL CHECK, ACTIVE BET OR ONE PLAYER REMAINING, FORCING CALL###");
                call(); // Force call if checking is not allowed due to an active bet.
            } else {
                System.out.println("###UNABLE TO CHECK OR CALL, FORCING FOLD###");
                fold(); // Force fold if neither checking nor calling is possible.
            }
        } else {
            playerAction = PlayerActions.CHECK; // Update the player's last action to CHECK.
        }
    }

    /**
     * Executes the call action for the player.
     * If there is no active bet, the player attempts to raise to the table's minimum bet.
     * If the table bet is greater than the player's bank, calling is not possible, and the player is forced to check.
     * If neither calling nor checking is possible, the player is forced to fold.
     * Otherwise, the player matches the table bet, and their last action is updated to CALL.
     */
    protected void call() {
        if(!state.isActiveBet()) {
            System.out.println("###ILLEGAL CALL, ATTEMPTING RAISE###");
            raise(getGameState().getTableMinBet()); // Attempt to raise if there is no active bet.
        }
        else if(state.getTableBet() > bank) {
            canCall = false;
            if(canCall || canCheck) {
                System.out.println("###ILLEGAL CALL, FORCING CHECK###");
                check(); // Force check if calling is not possible due to insufficient bank.
            } else {
                System.out.println("###UNABLE TO CALL OR CHECK, FORCING FOLD###");
                fold(); // Force fold if neither calling nor checking is possible.
            }
        } else {
            if(bet > bank) {
                System.out.println("###ILLEGAL BET, bet = bank###");
                bet = bank; // Adjust bet to match the player's bank if it exceeds the bank.
            }
            bet = state.getTableBet(); // Match the table bet.
            playerAction = PlayerActions.CALL; // Update the player's last action to CALL.
        }
    }

    /**
     * Executes the raise action for the player.
     * If the player's bank is zero, raising is not possible, and the player is forced to check.
     * If the raise amount is less than the table's minimum bet or does not exceed the current table bet,
     * the raise is considered illegal, and the player's bet is set to the current table bet.
     * If the raise amount is more than the player's bank or combined with the table bet exceeds the player's bank,
     * the raise amount is adjusted to the player's total bank, effectively going all-in.
     * Otherwise, the player's bet is increased by the raise amount, and their last action is updated to RAISE.
     */
    protected void raise(int betAmount) {
        if(bank <= 0) {
            System.out.println("###ILLEGAL RAISE, ZERO BANK, FORCING CHECK###");
            check(); // Force check if raising is not possible due to zero bank.
        }
        else if(betAmount < state.getTableMinBet() || betAmount + state.getTableBet() <= state.getTableBet()) {
            System.out.println("###ILLEGAL RAISE, VALUE UNDER ACCEPTED LIMIT, value = state.getTableMinBet()###");
            bet = state.getTableMinBet(); // Set player's bet to current table bet if raise amount is under the limit.
            playerAction = PlayerActions.RAISE; // Update the player's last action to RAISE.
        }
        else if(betAmount > bank || betAmount + state.getTableBet() >= bank) {
            System.out.println("###ILLEGAL RAISE, VALUE OVER ACCEPTED LIMIT, value = bank###");
            betAmount = bank; // Adjust raise amount to total bank if it exceeds the player's bank.
            bet = betAmount; // Set player's bet to adjusted raise amount.
            playerAction = PlayerActions.RAISE; // Update the player's last action to RAISE.
        } else {
            if(bet > bank) {
                System.out.println("###ILLEGAL BET, bet = bank###");
                bet = bank; // Ensure player's bet does not exceed their bank.
            }
            bet = betAmount + state.getTableBet(); // Increase player's bet by the raise amount.
            playerAction = PlayerActions.RAISE; // Update the player's last action to RAISE.
        }
    }

    /**
     * Executes the all-in action for the player.
     * If the player's bank is zero, going all-in is not possible, and the player is forced to check.
     * If the player's bank is less than the table bet, going all-in is considered illegal.
     * Otherwise, the player bets their entire bank, sets their status to all-in, and updates their last action to ALL_IN.
     */
    protected void allIn() {
        if(bank <= 0) {
            System.out.println("###ILLEGAL ALLIN, ZERO BANK, FORCING CHECK###");
            check(); // Force check if going all-in is not possible due to zero bank.
        }
        if(bank < state.getTableBet()) {
            System.out.println("###ILLEGAL ALL IN, BANK LESS THAN TABLE BET###");
            // No action is taken if the player's bank is less than the table bet.
        } else {
            if(bet > bank) {
                System.out.println("###ILLEGAL BET, bet = bank###");
                bet = bank; // Adjust player's bet to their total bank.
            }
            bet = bank; // Player goes all-in with their entire bank.
            isAllIn = true; // Set player's status to all-in.
            playerAction = PlayerActions.ALL_IN; // Update the player's last action to ALL_IN.
        }
    }

    /**
     * Retrieves the last action taken by the player in the current game state.
     * This method updates the game state, executes the player's turn based on the updated state,
     * and then returns the action taken.
     * @param state The current state of the game.
     * @return The last action taken by the player.
     */
    PlayerActions getPlayerAction(GameState state) {
        playerAction = null;
        //printExampleStateInformation();
        updateGameState(state); // Update the player's game state to the current state.
        takePlayerTurn(); // Execute the player's turn based on the updated state.
        return playerAction; // Return the action taken by the player.
    }

    /**
     * Resets the player's status and hand for a new hand.
     * This includes resetting the bet to 0, clearing the player's hand cards,
     * and resetting all status flags to their default values.
     */
    void newHandReset() {
        bet = 0; // Reset the bet to 0 for the new hand.

        // Reset all status flags to their default values.
        isDealer = false;
        isSmall = false;
        isBig = false;
        isFold = false;
        isAllIn = false;
        //isBet = false;
        isBetActive = false;

        // By default, a player can call or check in the new hand.
        canCall = true;
        canCheck = true;

        handCards.clear(); // Clear the player's hand cards.
    }

    /**
     * Gets the player's name.
     * @return The name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the player's current bank balance.
     * @return The current bank balance of the player.
     */
    public int getBank() {
        return bank;
    }

    /**
     * Gets the current bet amount of the player.
     * @return The current bet amount.
     */
    public int getBet() {
        return bet;
    }

    public boolean isBet() {
        return isBet;
    }

    void setIsBet(boolean value) {
        isBet = value;
    }

    /**
     * Adjusts the player's bank balance by a specified value.
     * This can be used to add or subtract from the player's bank.
     * @param value The amount to adjust the player's bank by.
     */
    void adjustPlayerBank(int value) {
        bank += value; // Adjust the player's bank balance.
    }

    /**
     * Sets the dealer status for the player.
     * @param isDealer A boolean indicating if the player is the dealer.
     */
    void setDealer(boolean isDealer) {
        this.isDealer = isDealer;
    }

    /**
     * Sets the small blind status for the player.
     * @param isSmall A boolean indicating if the player is the small blind.
     */
    void setSmall(boolean isSmall) {
        this.isSmall = isSmall;
    }

    /**
     * Sets the big blind status for the player.
     * @param isBig A boolean indicating if the player is the big blind.
     */
    void setBig(boolean isBig) {
        this.isBig = isBig;
    }

    /**
     * Appends additional text to the player's name.
     * @param name The additional text to append to the player's name.
     */
    void appendPlayerName(String name) {
        this.name += " " + name; // Append the provided name to the existing player name.
    }

    /**
     * Adds a card to the player's hand.
     * @param card The card to be added to the hand.
     */
    void addCardToPlayerHand(Card card) {
        handCards.add(card); // Add the specified card to the player's hand.
    }

    /**
     * Checks if the player is the dealer.
     * @return True if the player is the dealer, false otherwise.
     */
    public boolean isDealer() {
        return isDealer;
    }

    /**
     * Checks if the player is the small blind.
     * @return True if the player is the small blind, false otherwise.
     */
    public boolean isSmall() {
        return isSmall;
    }

    /**
     * Checks if the player is the big blind.
     * @return True if the player is the big blind, false otherwise.
     */
    public boolean isBig() {
        return isBig;
    }

    /**
     * Checks if the player has folded.
     * @return True if the player has folded, false otherwise.
     */
    public boolean isFold() {
        return isFold;
    }

    /**
     * Sets the fold status for the player.
     * @param isFold A boolean indicating if the player has folded.
     */
    void setIsFold(boolean isFold) {
        this.isFold = isFold;
    }

    /**
     * Checks if the player has gone all-in.
     * @return True if the player is all-in, false otherwise.
     */
    public boolean isAllIn() {
        return isAllIn;
    }

    void setIsAllIn(boolean value) {
        isAllIn = value;
    }

    /**
     * Checks if there is an active bet for the player.
     * @return True if there is an active bet, false otherwise.
     */
    protected boolean isBetActive() {
        return isBetActive;
    }

    public boolean isActionRemaining() {
        return isActionRemaining;
    }

    void setActionRemaining (boolean value) {
        isActionRemaining = value;
    }

    /**
     * Retrieves the list of cards in the player's hand.
     * @return A list of cards in the player's hand.
     */
    protected List<Card> getHandCards() {
        return handCards;
    }

    /**
     * Retrieves the current game state.
     * @return The current GameState object.
     */
    protected GameState getGameState() {
        return state;
    }

    /**
     * Updates the game state with a new state.
     * This method also resets the ability to call or check to true.
     * @param state The new game state to update to.
     */
    void updateGameState(GameState state) {
        canCall = true;
        canCheck = true;
        this.state = state;
    }

    /**
     * Evaluates the player's hand based on the current game state and the cards in hand.
     * @return The rank of the player's hand.
     */
    public HandRanks evaluatePlayerHand() {
        return evaluator.evaluatePlayerHand(handCards, state.getTableCards());
    }

    /**
     * Abstract method that defines the logic for taking a player's turn.
     * Implementations of this method should determine the player's action based on the current game state.
     */
    protected abstract void takePlayerTurn();

    /**
     * Abstract method to determine if the player should fold.
     * @return True if the player should fold, false otherwise.
     */
    protected abstract boolean shouldFold();

    /**
     * Abstract method to determine if the player should check.
     * @return True if the player should check, false otherwise.
     */
    protected abstract boolean shouldCheck();

    /**
     * Abstract method to determine if the player should call.
     * @return True if the player should call, false otherwise.
     */
    protected abstract boolean shouldCall();

    /**
     * Abstract method to determine if the player should raise.
     * @return True if the player should raise, false otherwise.
     */
    protected abstract boolean shouldRaise();

    /**
     * Abstract method to determine if the player should go all-in.
     * @return True if the player should go all-in, false otherwise.
     */
    protected abstract boolean shouldAllIn();
     
    /**
     * Prints example game state information to demonstrate how to access and use data from the GameState object.
     * This method is designed to help students learn about the different pieces of information available in the game state
     * and how they might be used to inform game decisions.
     */
    protected void printExampleStateInformation() {
        // Check if the game state is null to prevent NullPointerException
        if (getGameState() == null) {
            System.out.println("Game state is not available.");
            return;
        }

        System.out.println("=== EXAMPLE GAME STATE INFORMATION ===");

        // Print information about the cards on the table
        System.out.print("Table Cards: ");
        for (Card card : getGameState().getTableCards()) {
            System.out.print(card.toString() + " ");
        }
        System.out.println("\n----------------------------------------");

        // Print information about the players' banks
        System.out.println("Player Banks:");
        for (Map<String, Integer> playerBankMap : getGameState().getListPlayersNameBankMap()) {
            for (Map.Entry<String, Integer> entry : playerBankMap.entrySet()) {
                System.out.println(entry.getKey() + " has $" + entry.getValue());
            }
        }
        System.out.println("----------------------------------------");

        // Demonstrating conditional logic with game state data
        if (getGameState().getNumPlayersRemainingGame() < 3) {
            System.out.println("Fewer than 3 players remaining in the game, consider aggressive play.");
        }

        // Print remaining cards in the deck
        System.out.println("Deck has " + getGameState().getDeckNumRemainingCards() + " remaining cards.");

        // Additional game state information
        System.out.println("Players left in game: " + getGameState().getNumPlayersRemainingGame());
        System.out.println("Players left in round: " + getGameState().getNumPlayersRemainingRound());
        System.out.println("Rounds until ante increase: " + getGameState().getTableAnteCountdown());
        System.out.println("Small ante: $" + getGameState().getTableAnteSmall());
        System.out.println("Big ante: $" + getGameState().getTableAnteBig());
        System.out.println("Current pot: $" + getGameState().getTablePot());
        System.out.println("Current bet: $" + getGameState().getTableBet());
        System.out.println("Minimum bet: $" + getGameState().getTableMinBet());
        System.out.println("Total games played: " + getGameState().getNumTotalGames());
        System.out.println("Current round stage: " + getGameState().getNumRoundStage());
        System.out.println("Dealer: " + getGameState().getDealer());
        System.out.println("Small blind: " + getGameState().getSmall());
        System.out.println("Big blind: " + getGameState().getBig());
        System.out.println("========================================");
    }
}
