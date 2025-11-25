# Race Raft

## Playing Race to the Raft

There are three steps to each round in Race to the Raft:

1. Draw pathway cards;
2. Play or discard pathway cards to make pathways or move cats; and
3. Rest.

There is no fixed number of rounds in this game: the game continues until you win or lose.

### Step 1: Drawing Pathway Cards

The player draws six pathway cards from among the four decks. The player can draw these cards in any combination, from
any decks they wish. However, you are not allowed to look at the cards you have drawn until you have drawn all
six cards.

Decks are not replaced in this game. If a deck has no cards, you can no longer draw from that deck.
The game is lost if a player is unable to draw a card because all decks are empty.

### Step 2: Playing Pathway Cards

The player plays cards to move cats, or create paths to the island. All cards in their hand must be played before
the next step.

#### Action: Add Pathway

A player can place one pathway card from their hand onto the island. This placement must be consistent with
the [placement rules](#placing-cards-and-fire-tiles).

Afterwards, the player places a random fire tile on the island, also following the placement rules.

#### Placing Cards and Fire Tiles

- Pathway cards and fire tiles cannot have any of their squares hanging off the edge of the island.
- The squares of pathway cards and fire tiles must line up with the squares of the island.
- Pathway cards and fire tiles can be rotated in any orientation. Fire tiles can also be flipped, both horizontally and
  vertically.
- Pathway cards and fire tiles can fully or partially overlap other pathway cards.
- Pathway cards and fire tiles cannot overlap any squares with fire or any square belonging to a raft card.
- Pathway cards and fire tiles cannot overlap any squares with cats on them.
- Pathway cards and fire tiles cannot be placed under other pathway cards or fire tiles.
- Fire tiles must be placed next to existing fire. That is, at least one square of a fire tile being placed must touch
  at least one square of existing fire. Note that diagonals are not considered adjacent.
- Pathway cards do not have any adjacency requirements like fire tiles.

#### Action: Move a Cat

To move a cat:

1. Discard one card from your hand, or two cards if the cat is exhausted. Discarded cards are removed from
   the game, so we do not track the discard pile.
2. Move the cat along any number of orthogonally adjacent* squares of the same colour as
   the cat.
3. When you have finished moving the cat, the cat becomes exhausted for the rest of this round. If the cat was
   already exhausted, it remains exhausted.

Cats can move through squares occupied by other cats, but no two cats can occupy the same square.

*Given a square `s`, its orthogonally adjacent squares are the squares next to `s` and not diagonally adjacent to `s`.

### Step 3: Resting

All cards must be played from the player's hand. Once all cards have been played, any exhausted cats are no longer
exhausted and the player starts a new round, returning to Step 1.

### End of Game

#### Winning

You win if all the cats are on the raft card.

#### Losing

There are several ways that the game can be lost:

- If a player cannot place a fire tile on the island in a valid way according to the game rules;
- If there is no way for a cat to reach the raft;
- If a there are no more fire tiles remaining AND the player is required to draw one; or
- If there are no more pathway cards in any of the decks AND the player is required to draw a card.
