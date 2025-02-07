package gameset.cards;

/* TODO remove me after tests done
player receives 200 (MoneyReceive)
player pays 50 (MoneyPay)
get out of jail free (GetOutOfJailCard)
go to jail (DirectMove)
player receives 50 from all players (MoneyReceiveAll)
Pay 40 per house, 115 per hotel (OwnedPropertyPay)
Pay 25 per house, 100 per hotel (OwnedPropertyPay)
Advance to GO (Advance)
Advance to Util, if owned pay 10x amount rolled (AdvanceConditional)
Advance to railroad, if owned pay 2x rent (AdvanceConditional)
move back 3 spaces (DirectMove)
 */
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
