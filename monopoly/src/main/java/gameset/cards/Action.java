package gameset.cards;

public enum Action {
    Advance,            // Could pass go (move player around board to destination)
    AdvanceConditional, // For advance to railroad or utility (tracks conditional rent change)
    DirectMove,         // Does not pass GO (move player directly to destination)
    MoneyReceive,       // Player receives money
    MoneyReceiveAll,    // Player receives money from all players
    MoneyPay,           // Player pays money
    MoneyPayAll,        // Player pays money to all players
    GetOutOfJailCard,   // Player receives a get out of jail card
    OwnedPropertyPay,   // Player pays money for each owned house & hotel
}
