package tools;

import java.io.BufferedWriter;

import mctsbot.actions.Action;
import mctsbot.actions.RaiseAction;
import mctsbot.gamestate.GameState;

import com.biotools.meerkat.Card;
import com.biotools.meerkat.Hand;
import com.biotools.meerkat.HandEvaluator;

public class SimpleBotHROMWekaFormat2 implements WekaFormat {
	
	private static final int HIGHEST_HIGH_CARD = 1276;
	private static final int HIGHEST_PAIR = 4136;
	private static final int HIGHEST_TWO_PAIRS = 4994;
	private static final int HIGHEST_THREE_OF_A_KIND = 5852;
	private static final int HIGHEST_STRAIGHT = 5862;
	private static final int HIGHEST_FLUSH = 7139;
	private static final int HIGHEST_FULL_HOUSE = 7295;
	private static final int HIGHEST_FOUR_OF_A_KIND = 7451;
	private static final int HIGHEST_STRAIGHT_FLUSH = 7461;
	
	private static final String HIGH_CARD = "high_card";
	private static final String ONE_PAIR = "one_pair";
	private static final String TWO_PAIRS = "two_pairs";
	private static final String THREE_OF_A_KIND = "three_of_a_kind";
	private static final String STRAIGHT = "straight";
	private static final String FLUSH = "flush";
	private static final String FULL_HOUSE = "full_house";
	private static final String FOUR_OF_A_KIND = "four_of_a_kind";
	private static final String STRAIGHT_FLUSH = "straight_flush";

	private static final String SIMPLEBOT = "SimpleBot";
	
	private static final String RELATION_TITLE = "SimpleBotHROM";

	public void write(GameRecord gameRecord, BufferedWriter out)
			throws Exception {
		
		if(!gameRecord.endedInShowdown()) return;
		
		final PlayerRecord player = gameRecord.getPlayer(SIMPLEBOT);
		final Hand table = gameRecord.getTable();
		
		// Actions.
		for(int i=GameState.PREFLOP; i<=GameState.RIVER; i++) {
			int raiseCount = 0;
			for(Action action: player.getActions(i))
				if(action instanceof RaiseAction) raiseCount++;
			
			if(raiseCount<1) out.write("c,");
			else if(raiseCount==1) out.write("r,");
			else if(raiseCount>1) out.write("rr,");
		}
		
		// Table cards.
		int[] ranks = new int[5];
		int[] suits = new int[5];
		
		// TODO: make this more efficient by using the array already stored in gameRecord.
		int[] cards = gameRecord.getTable().getCardArray();
		
		for(int j=0; j<5; j++) {
			ranks[j] = Card.getRank(cards[j+1]);
			suits[j] = Card.getSuit(cards[j+1]);
		}
		
		// Write card ranks
		for(int j=0; j<5; j++) {
			out.write(Card.getRankChar(ranks[j]) + ",");
		}
		
		// Write suits.
		int[] suitCounts = new int[4];
		
		// Flop.
		suitCounts[suits[0]]++;
		suitCounts[suits[1]]++;
		suitCounts[suits[2]]++;
		
		String numSuitedFlop = "1,";
		for(int j=0; j<4; j++) {
			if(suitCounts[j]==3) {
				numSuitedFlop = "3,";
				break;
			} else if(suitCounts[j]==2) {
				numSuitedFlop = "2,";
				break;
			} 
		}
		out.write(numSuitedFlop);
		
		// Turn.
		suitCounts[suits[3]]++;
		
		String numSuitedTurn = "1,";
		for(int j=0; j<4; j++) {
			if(suitCounts[j]==4) {
				numSuitedTurn = "4,";
				break;
			} else if(suitCounts[j]==3) {
				numSuitedTurn = "3,";
				break;
			} else if(suitCounts[j]==2) {
				for(int k=0; k<4; k++) {
					if(k!=j && suitCounts[k]==2) {
						numSuitedTurn = "22,";
					} else if(suitCounts[k]==1) {
						numSuitedTurn = "2,";
					}
				}
			} 
		}
		out.write(numSuitedTurn);
		
		// River.
		suitCounts[suits[4]]++;
		
		String numSuitedRiver = "0,";
		for(int j=0; j<4; j++) {
			if(suitCounts[j]==5) {
				numSuitedRiver = "5,";
				break;
			} else if(suitCounts[j]==4) {
				numSuitedRiver = "4,";
				break;
			} else if(suitCounts[j]==3) {
				numSuitedRiver = "3,";
				break;
			}
		}
		out.write(numSuitedRiver);
		
		
		final int handRank = HandEvaluator.rankHand(player.getC1(), player.getC2(), table);
		
		out.write(handRank + "\r");
		
		/*
		if(handRank<HIGHEST_HIGH_CARD) out.write(HIGH_CARD);
		else if(handRank<HIGHEST_PAIR) out.write(ONE_PAIR);
		else if(handRank<HIGHEST_TWO_PAIRS) out.write(TWO_PAIRS);
		else if(handRank<HIGHEST_THREE_OF_A_KIND) out.write(THREE_OF_A_KIND);
		else if(handRank<HIGHEST_STRAIGHT) out.write(STRAIGHT);
		else if(handRank<HIGHEST_FLUSH) out.write(FLUSH);
		else if(handRank<HIGHEST_FULL_HOUSE) out.write(FULL_HOUSE);
		else if(handRank<HIGHEST_FOUR_OF_A_KIND) out.write(FOUR_OF_A_KIND);
		else if(handRank<HIGHEST_STRAIGHT_FLUSH) out.write(STRAIGHT_FLUSH);
		else throw new RuntimeException("invalid rank: " + handRank);
		
		out.write("\r");
		*/
		
		out.flush();

	}

	public void writeHeader(BufferedWriter out) throws Exception {
		out.write("@RELATION " + RELATION_TITLE + "\r");
		out.write("\r");
		out.write("@ATTRIBUTE preflop_actions {c,r,rr}\r");
		out.write("@ATTRIBUTE flop_actions {c,r,rr}\r");
		out.write("@ATTRIBUTE turn_actions {c,r,rr}\r");
		out.write("@ATTRIBUTE river_actions {c,r,rr}\r");
		out.write("@ATTRIBUTE c_card_1_rank {2,3,4,5,6,7,8,9,T,J,Q,K,A}\r");
		out.write("@ATTRIBUTE c_card_2_rank {2,3,4,5,6,7,8,9,T,J,Q,K,A}\r");
		out.write("@ATTRIBUTE c_card_3_rank {2,3,4,5,6,7,8,9,T,J,Q,K,A}\r");
		out.write("@ATTRIBUTE c_card_4_rank {2,3,4,5,6,7,8,9,T,J,Q,K,A}\r");
		out.write("@ATTRIBUTE c_card_5_rank {2,3,4,5,6,7,8,9,T,J,Q,K,A}\r");
		out.write("@ATTRIBUTE num_suited_flop {1,2,3}\r");
		out.write("@ATTRIBUTE num_suited_turn {1,2,3,4,22}\r");
		out.write("@ATTRIBUTE num_suited_river {0,3,4,5}\r");
		out.write("@ATTRIBUTE hand_rank NUMERIC\r");
		out.write("\r");
		out.write("@DATA\r");
		out.flush();
	}

}