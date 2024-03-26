package game;

import java.util.List;
import java.util.Map;

public class GameState {

    private List<Card> tableCards; // The cards currently on the table.
    private List<Map<String, Integer>> listPlayersNameBankMap; // A list of maps, each representing a player's name and bank balance.

    // Various game state variables
    private int deckNumRemainingCards, numDecksUsed, numPlayersRemainingGame, numPlayersRemainingRound, tableAnteCountdown, tableAnteSmall, tableAnteBig, tablePot, tableBet, tableMinBet, activeBetNumberOfPlayersLeft, numTotalGames, numRoundStage, dealerIndex;

    private boolean activeBet; // Indicates if there is an active bet in the game.

    private Player dealer, small, big; // The dealer, small blind, and big blind players in the game.

    /**
     * Constructor for a GameState object.
     * Initializes the game state with the provided parameters.
     * @param tableCards The cards currently on the table.
     * @param listPlayersNameBankMap A list of maps, each representing a player's name and bank balance.
     * @param deckNumRemainingCards The number of cards remaining in the deck.
     * @param numPlayersRemainingGame The number of players remaining in the game.
     * @param numPlayersRemainingRound The number of players remaining in the current round.
     * @param tableAnteCountdown The number of rounds until the ante increases.
     * @param tableAnteSmall The small blind ante amount.
     * @param tableAnteBig The big blind ante amount.
     * @param tablePot The current pot amount.
     * @param tableBet The current bet amount.
     * @param tableMinBet The minimum bet amount.
     * @param activeBet Indicates if there is an active bet in the game.
     * @param activeBetNumberOfPlayersLeft The number of players left to act in the current betting round.
     * @param numTotalGames The total number of games played.
     * @param numRoundStage The current stage of the round.
     * @param dealer The dealer player.
     * @param small The small blind player.
     * @param big The big blind player.
     * @param dealerIndex The index of the dealer player.
     */
    public GameState(List<Card> tableCards, List<Map<String, Integer>> listPlayersNameBankMap, int deckNumRemainingCards, int numDecksUsed, int numPlayersRemainingGame, int numPlayersRemainingRound, int tableAnteCountdown, int tableAnteSmall, int tableAnteBig, int tablePot, int tableBet, int tableMinBet, boolean activeBet, int activeBetNumberOfPlayersLeft, int numTotalGames, int numRoundStage, Player dealer, Player small, Player big, int dealerIndex) {
        this.tableCards = tableCards;
        this.listPlayersNameBankMap = listPlayersNameBankMap;

        this.deckNumRemainingCards = deckNumRemainingCards;
        this.numDecksUsed = numDecksUsed;
        this.numPlayersRemainingGame = numPlayersRemainingGame;
        this.numPlayersRemainingRound = numPlayersRemainingRound;
        this.tableAnteCountdown = tableAnteCountdown;
        this.tableAnteSmall = tableAnteSmall;
        this.tableAnteBig = tableAnteBig;
        this.tablePot = tablePot;
        this.tableBet = tableBet;
        this.tableMinBet = tableMinBet;
        this.activeBetNumberOfPlayersLeft = activeBetNumberOfPlayersLeft;
        this.numTotalGames = numTotalGames;
        this.numRoundStage = numRoundStage;
        this.dealerIndex = dealerIndex;

        this.activeBet = activeBet;

        this.dealer = dealer;
        this.small = small;
        this.big = big;
    }

    /**
     * Get the cards currently on the table.
     * @return The list of cards currently on the table.
     */
    public List<Card> getTableCards() {
        return tableCards;
    }

    /**
     * Get the list of maps, each representing a player's name and bank balance.
     * @return The list of maps, each representing a player's name and bank balance.
     */
    public List<Map<String, Integer>> getListPlayersNameBankMap() {
        return listPlayersNameBankMap;
    }

    /**
     * Get the number of cards remaining in the deck.
     * @return The number of cards remaining in the deck.
     */
    public int getDeckNumRemainingCards() {
        return deckNumRemainingCards;
    }

    public int getNumDecksUsed() {
        return numDecksUsed;
    }

    /**
     * Get the number of players remaining in the game.
     * @return The number of players remaining in the game.
     */
    public int getNumPlayersRemainingGame() {
        return numPlayersRemainingGame;
    }

    /**
     * Get the number of players remaining in the current round.
     * @return The number of players remaining in the current round.
     */
    public int getNumPlayersRemainingRound() {
        return numPlayersRemainingRound;
    }

    /**
     * Get the number of rounds until the ante increases.
     * @return The number of rounds until the ante increases.
     */
    public int getTableAnteCountdown() {
        return tableAnteCountdown;
    }

    /**
     * Get the small blind ante amount.
     * @return The small blind ante amount.
     */
    public int getTableAnteSmall() {
        return tableAnteSmall;
    }

    /**
     * Get the big blind ante amount.
     * @return The big blind ante amount.
     */
    public int getTableAnteBig() {
        return tableAnteBig;
    }

    /**
     * Get the current pot amount.
     * @return The current pot amount.
     */
    public int getTablePot() {
        return tablePot;
    }

    /**
     * Get the current bet amount on the table.
     * @return The current bet amount on the table.
     */
    public int getTableBet() {
        return tableBet;
    }

    /**
     * Get the minimum bet amount on the table.
     * @return The minimum bet amount on the table.
     */
    public int getTableMinBet() {
        return tableMinBet;
    }

    /**
     * Check if there is an active bet in the game.
     * @return True if there is an active bet, false otherwise.
     */
    public boolean isActiveBet() {
        return activeBet;
    }

    /**
     * Get the total number of games played.
     * @return The total number of games played.
     */
    public int getNumTotalGames() {
        return numTotalGames;
    }

    /**
     * Get the current stage of the round.
     * @return The current stage of the round.
     */
    public int getNumRoundStage() {
        return numRoundStage;
    }

    /**
     * Get the index of the dealer player.
     * @return The index of the dealer player.
     */
    public int getDealerIndex() {
        return dealerIndex;
    }

    /**
     * Get the name of the dealer player.
     * @return The name of the dealer player.
     */
    public String getDealer() {
        return dealer.getName();
    }

    /**
     * Get the name of the small blind player.
     * @return The name of the small blind player.
     */
    public String getSmall() {
        return small.getName();
    }

    /**
     * Get the name of the big blind player.
     * @return The name of the big blind player.
     */
    public String getBig() {
        return big.getName();
    }
}
