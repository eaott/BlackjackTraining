package com.ott.blackjacktraining;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ott.blackjacktraining.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity
{
	private List<ImageView> dealerImageViewList;
	private List<ImageView> userImageViewList;
	private Card[] dealerCards;
	private Card[] userCards;
	private List<Card> decks;
	
	private int curDeckCard;
	private int curDealerCard;
	private int curUserCard;
	private int curCount;
	private int correct;
	private int decisions;
	private int games;
	private int wins;
	
	private View buttonHit;
	private View buttonStay;
	private View buttonSplit;
	private View buttonDouble;
	
	private TextView userScore;
	private TextView userChoice;
	private TextView deckView;
	private TextView countView;
	private TextView accView;
	private TextView winView;
	
	public static final int NUM_DECKS = 8;
	public static final byte HIT = 1 << 0;
	public static final byte DOUBLE = 1 << 1;
	public static final byte SPLIT = 1 << 2;
	public static final byte STAY = 1 << 3;
	
	public static final String SCORE = "score";
	public static final String DECK = "deck";
	public static final String COUNT = "count";
	public static final String ACCURACY = "accuracy";
	public static final String WIN = "win";
	public static final String PREF_FILE = "blackjackPrefs";
	
	private static final String WINS = "WINS";
	private static final String DECISIONS = "DECISIONS";
	private static final String CORRECT = "CORRECT";
	private static final String GAMES = "GAMES";
	
	private boolean showScores;
	private boolean showDeck;
	private boolean showCount;
	private boolean showAccuracy;
	private boolean showWin;
	
	private boolean init;
	
	@Override
	protected void onResume()
	{
		super.onResume();
		if (init)
			updateSettings();
	}
	
	private void updateSettings()
	{
		SharedPreferences settings = getSharedPreferences(PREF_FILE, 0);
		showScores = settings.getBoolean(SCORE, true);
		if (showScores && userCards != null)
		{
			userScore.setText("" + sum(userCards));
			((TextView)findViewById(R.id.dealerScore)).setText("" + sum(dealerCards));
		}
		else if (userCards != null)
		{
			userScore.setText("");
			((TextView)findViewById(R.id.dealerScore)).setText("");
		}
		showDeck = settings.getBoolean(DECK, true);
		if (showDeck && deckView != null)
			deckView.setText(String.format("decks:%.1f", (52.0 * NUM_DECKS - curDeckCard) / 52.0));
		else if (deckView != null)
			deckView.setText("");
		showCount = settings.getBoolean(COUNT, true);
		if (showCount && countView != null)
			countView.setText(String.format("count:%+d", curCount));
		else if (countView != null)
			countView.setText("");
		showAccuracy = settings.getBoolean(ACCURACY, true);
		if (showAccuracy && accView != null)
			accView.setText(decisions == 0 ? "" : String.format("accuracy:%.1f%%", 100*((double) correct) / decisions));
		else if (accView != null)
			accView.setText("");
		showWin = settings.getBoolean(WIN, true);
		if (showWin && winView != null)
			winView.setText(games == 0 ? "" : String.format("win percent:%.1f%%", 100*((double) wins) / games));
		else if (winView != null)
			winView.setText("");
	}

	public static final byte[][] hard = new byte[][]{{},//0
		{},//1
		{},//2
		{}, //3
		{}, //4
		{0, 0, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT},//5
		{0, 0, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT},//6
		{0, 0, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT, HIT}, //7
		{0, 0, HIT, HIT, HIT, DOUBLE|HIT, DOUBLE|HIT, HIT, HIT, HIT, HIT, HIT}, //8
		{0, 0, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, HIT, HIT, HIT, HIT, HIT},//9
		{0, 0, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, HIT, HIT},//10
		{0, 0, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT}, //11
		{0, 0, HIT, HIT, STAY, STAY, STAY, HIT, HIT, HIT, HIT, HIT}, //12
		{0, 0, STAY, STAY, STAY, STAY, STAY, HIT, HIT, HIT, HIT, HIT},//13
		{0, 0, STAY, STAY, STAY, STAY, STAY, HIT, HIT, HIT, HIT, HIT},//14
		{0, 0, STAY, STAY, STAY, STAY, STAY, HIT, HIT, HIT, HIT, HIT}, //15
		{0, 0, STAY, STAY, STAY, STAY, STAY, HIT, HIT, HIT, HIT, HIT}, //16
		{0, 0, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY},//17
		{0, 0, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY},//18
		{0, 0, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY}, //19
		{0, 0, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY}, //20
		{0, 0, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY}};
	public static final byte[][] soft = new byte[][]{
		{}, //0
		{}, //1
		{0, 0, HIT, HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, HIT, HIT, HIT, HIT, HIT}, //2
		{0, 0, HIT, HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, HIT, HIT, HIT, HIT, HIT}, //3
		{0, 0, HIT, HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, HIT, HIT, HIT, HIT, HIT}, //4
		{0, 0, HIT, HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, HIT, HIT, HIT, HIT, HIT}, //5
		{0, 0, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, HIT, HIT, HIT, HIT, HIT}, //6
		{0, 0, STAY, DOUBLE|STAY, DOUBLE|STAY, DOUBLE|STAY, DOUBLE|STAY, STAY, STAY, HIT, HIT, STAY}, //7
		{0, 0, STAY, STAY, STAY, STAY, DOUBLE|STAY, STAY, STAY, STAY, STAY, STAY}, //8
		{0, 0, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY}, //9
		{0, 0, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY}, //10
	};
	public static final byte[][] pair = new byte[][]{
		{}, //0
		{}, //1
		{0, 0, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, HIT, HIT, HIT, HIT}, //2
		{0, 0, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, HIT, HIT, HIT}, //3
		{0, 0, HIT, HIT, SPLIT, SPLIT, SPLIT, HIT, HIT, HIT, HIT, HIT}, //4
		{0, 0, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, DOUBLE|HIT, HIT, HIT}, //5
		{0, 0, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, HIT, HIT, HIT, HIT}, //6
		{0, 0, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, HIT, STAY, HIT}, //7
		{0, 0, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT}, //8
		{0, 0, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, STAY, SPLIT, SPLIT, STAY, STAY}, //9
		{0, 0, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY, STAY}, //10
		{0, 0, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT, SPLIT} //11
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null)
		{
			correct = savedInstanceState.getInt(CORRECT, 0);
			decisions = savedInstanceState.getInt(DECISIONS, 0);
			games = savedInstanceState.getInt(GAMES, 0);
			wins = savedInstanceState.getInt(WINS, 0);
		}
		setContentView(R.layout.activity_main);
		updateSettings();
		dealerImageViewList = new ArrayList<ImageView>();
		userImageViewList = new ArrayList<ImageView>();
		dealerCards = new Card[8];
		userCards = new Card[8];
		curDealerCard = 0;
		curUserCard = 0;

		dealerImageViewList.add((ImageView)findViewById(R.id.dealer1));
		dealerImageViewList.add((ImageView)findViewById(R.id.dealer2));
		dealerImageViewList.add((ImageView)findViewById(R.id.dealer3));
		dealerImageViewList.add((ImageView)findViewById(R.id.dealer4));
		dealerImageViewList.add((ImageView)findViewById(R.id.dealer5));
		dealerImageViewList.add((ImageView)findViewById(R.id.dealer6));
		dealerImageViewList.add((ImageView)findViewById(R.id.dealer7));
		dealerImageViewList.add((ImageView)findViewById(R.id.dealer8));
		userImageViewList.add((ImageView)findViewById(R.id.user1));
		userImageViewList.add((ImageView)findViewById(R.id.user2));
		userImageViewList.add((ImageView)findViewById(R.id.user3));
		userImageViewList.add((ImageView)findViewById(R.id.user4));
		userImageViewList.add((ImageView)findViewById(R.id.user5));
		userImageViewList.add((ImageView)findViewById(R.id.user6));
		userImageViewList.add((ImageView)findViewById(R.id.user7));
		userImageViewList.add((ImageView)findViewById(R.id.user8));
		
		deckView = (TextView)findViewById(R.id.decks);
		countView = (TextView)findViewById(R.id.count);
		accView = (TextView)findViewById(R.id.acc);
		winView = (TextView)findViewById(R.id.win);
		
		decks = new ArrayList<Card>(52 * NUM_DECKS);
		for (int i = 0; i < NUM_DECKS; i++)
		{
			for (Card c : Card.values())
				if (c.getImageResource() != R.drawable.b)
					decks.add(c);
		}
		buttonDouble = findViewById(R.id.buttonDouble);
		buttonHit = findViewById(R.id.buttonHit);
		buttonSplit = findViewById(R.id.buttonSplit);
		buttonStay = findViewById(R.id.buttonStay);
		userScore = (TextView)findViewById(R.id.userScore);
		userChoice = (TextView)findViewById(R.id.decision);
		findViewById(R.id.buttonNew).setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				deal();
			}
		});
		buttonDouble.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				short check = check();
				decisions++;
				if ((check & DOUBLE) > 0)
				{
					userChoice.setText(R.string.correct);
					correct++;
				}
				else
					userChoice.setText(generateCorrect(check));
				buttonDouble.setEnabled(false);
				buttonHit.setEnabled(false);
				buttonSplit.setEnabled(false);
				buttonStay.setEnabled(false);
				dealUserCard();
				if (showScores)
					userScore.setText("" + sum(userCards));
				if (sum(userCards) > 21)
				{
					userScore.setText("bust!");
				}
				if (showAccuracy)
					accView.setText(decisions == 0 ? "" : String.format("accuracy:%.1f%%", 100*((double) correct) / decisions));
				else
					accView.setText("");
				dealDealerOut();
			}
		});
		buttonHit.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				short check = check();
				decisions++;
				boolean shouldHaveDoubled = curUserCard == 2 && (check & DOUBLE) > 0;
				if ((check & HIT) > 0 && !shouldHaveDoubled)
				{
					userChoice.setText(R.string.correct);
					correct++;
				}
				else
					userChoice.setText(generateCorrect(check));
				buttonDouble.setEnabled(false);
				buttonSplit.setEnabled(false);
				dealUserCard();
				if (showScores)
					userScore.setText("" + sum(userCards));
				int sum = sum(userCards);
				if (sum > 21)
				{
					userScore.setText("bust!");
				}
				if (showAccuracy)
					accView.setText(decisions == 0 ? "" : String.format("accuracy:%.1f%%", 100*((double) correct) / decisions));
				else
					accView.setText("");
				if (sum >= 21)
				{
					buttonHit.setEnabled(false);
					buttonStay.setEnabled(false);
					dealDealerOut();
				}	
			}
		});
		buttonSplit.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				short check = check();
				decisions++;
				if ((check & SPLIT) > 0)
				{
					userChoice.setText(R.string.correct);
					correct++;
				}
				else
					userChoice.setText(generateCorrect(check));
				curUserCard--;
				dealUserCard();
				if (userCards[0].getValue() != userCards[1].getValue())
					buttonSplit.setEnabled(false);
				// impossible to bust on split
				int sum = sum(userCards);
				if (showScores)userScore.setText("" + sum);
				if (sum > 21)
				{
					userScore.setText("bust!");
				}
				if (showAccuracy)
					accView.setText(decisions == 0 ? "" : String.format("accuracy:%.1f%%", 100*((double) correct) / decisions));
				else
					accView.setText("");
				if(sum >= 21)
				{
					buttonDouble.setEnabled(false);
					buttonSplit.setEnabled(false);
					buttonHit.setEnabled(false);
					buttonStay.setEnabled(false);
					dealDealerOut();
				}
			}
		});
		buttonStay.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				short check = check();
				decisions++;
				boolean shouldHaveDoubled = curUserCard == 2 && (check & DOUBLE) > 0;
				if ((check & STAY) > 0 && !shouldHaveDoubled)
				{
					userChoice.setText(R.string.correct);
					correct++;
				}
				else
					userChoice.setText(generateCorrect(check));
				if (showAccuracy)
					accView.setText(decisions == 0 ? "" : String.format("accuracy:%.1f%%", 100*((double) correct) / decisions));
				else
					accView.setText("");
				// check the matrix, deal the dealer
				buttonDouble.setEnabled(false);
				buttonSplit.setEnabled(false);
				buttonHit.setEnabled(false);
				buttonStay.setEnabled(false);
				dealDealerOut();
			}
		});
		curDeckCard = decks.size();
		deal();
		init = true;
	}
	@Override
	protected void onSaveInstanceState(Bundle b)
	{
		super.onSaveInstanceState(b);
		b.putInt(CORRECT, correct);
		b.putInt(DECISIONS, decisions);
		b.putInt(GAMES, games);
		b.putInt(WINS, wins);
	}
	@Override
	public void onBackPressed()
	{
		//left empty
	}
	protected String generateCorrect(short check)
	{
		final String incorrect = "Incorrect: ";
		StringBuilder sb = new StringBuilder(incorrect);
		if ((check & DOUBLE) > 0)
			sb.append("dbl");
		if ((check & SPLIT) > 0)
			if (sb.length() != incorrect.length())
				sb.append(" or split");
			else
				sb.append("split");
		if ((check & HIT) > 0 && ! ((check & DOUBLE) > 0))
			if (sb.length() != incorrect.length())
				sb.append(" or hit");
			else
				sb.append("hit");
		if ((check & STAY) > 0  && ! ((check & DOUBLE) > 0))
			if (sb.length() != incorrect.length())
				sb.append(" or stay");
			else
				sb.append("stay");
		return sb.toString();
	}

	private byte check()
	{
		int userScore = sum(userCards);
		int dealerScore = sum(dealerCards);
		if (curUserCard == 2 && userCards[0].getValue() == userCards[1].getValue())
			return checkPair(userScore, dealerScore);
		if (soft(userCards))
			return checkSoft(userScore, dealerScore);
		return checkHard(userScore, dealerScore);	
	}
	private byte checkHard(int uS, int dS)
	{
		return hard[uS][dS];
	}
	private byte checkSoft(int uS, int dS)
	{
		return soft[uS - 11][dS];
	}
	private byte checkPair(int uS, int dS)
	{
		return pair[userCards[0].getValue()][dS];
	}

	private void dealDealerOut()
	{
		int dealerSum = sum(dealerCards);
		boolean soft = soft(dealerCards);
		while (dealerSum <= 16 || (dealerSum == 17 && soft))
		{
			dealerCards[curDealerCard] = decks.get(curDeckCard++);
			updateCount(dealerCards[curDealerCard]);
			dealerImageViewList.get(curDealerCard).setImageResource(dealerCards[curDealerCard].getImageResource());
			curDealerCard++;
			dealerSum = sum(dealerCards);
			soft = soft(dealerCards);
		}
		((TextView)findViewById(R.id.dealerScore)).setText((dealerSum <= 21) ? (showScores ? "" + dealerSum : "") : "bust!");
		if (showDeck)
			deckView.setText(String.format("decks:%.1f", (52.0 * NUM_DECKS - curDeckCard) / 52.0));
		else
			deckView.setText("");
		if (showCount)
			countView.setText(String.format("count:%+d", curCount));
		else
			countView.setText("");
		int userSum = sum(userCards);
		if (userSum <= 21)
		{
			games++;
			if (dealerSum <= 21)
			{
				if (userSum > dealerSum)
					wins++;
				else if (userSum == dealerSum)	// don't count ties
					games--;
			}
			else
				wins++;
		}
		else
		{
			if (dealerSum <= 21)
			{
				games++;
			}	// don't count ties
		}
		if (showWin)
			winView.setText(games == 0 ? "" : String.format("win percent:%.1f%%", 100*((double) wins) / games));
		else
			winView.setText("");
	}

	private boolean soft(Card[] cards)
	{
		int ret = 0;
		int aces = 0;
		for (Card c : cards)
		{
			ret += c.getValue();
			if (c.isAce())
				aces++;
		}
		while (ret > 21 && aces > 0)
		{
			ret -= 10;
			aces--;
		}
		return aces > 0;
	}

	private void dealUserCard()
	{
		userCards[curUserCard] = decks.get(curDeckCard++);
		updateCount(userCards[curUserCard]);
		userImageViewList.get(curUserCard).setImageResource(userCards[curUserCard].getImageResource());
		curUserCard++;
		if (showDeck)
			deckView.setText(String.format("decks:%.1f", (52.0 * NUM_DECKS - curDeckCard) / 52.0));
		else
			deckView.setText("");
		if (showCount)
			countView.setText(String.format("count:%+d", curCount));
		else
			countView.setText("");
	}

	public int sum(Card[] cards)
	{
		int ret = 0;
		int aces = 0;
		for (Card c : cards)
		{
			ret += c.getValue();
			if (c.isAce())
				aces++;
		}
		while (ret > 21 && aces > 0)
		{
			ret -= 10;
			aces--;
		}
		return ret;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.settings)
		{
			startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
		}
		if (item.getItemId() == R.id.reset)
		{
			correct = 0;
			decisions = 0;
			wins = 0;
			games = 0;
			if (accView != null)
				accView.setText("");
			if (winView != null)
				winView.setText("");
			shuffle();
			deal();
		}
		return true;
	}
	
	private void shuffle()
	{
		curCount = 0;
		for (int i = 0; i < 20; i++)
			Collections.shuffle(decks);
		curDeckCard = 0;
	}

	private void deal()
	{
		userScore.setText("");
		userChoice.setText("");
		for (int i = 0; i < dealerImageViewList.size(); i++)
		{
			dealerImageViewList.get(i).setImageResource(R.drawable.b);
			dealerCards[i] = Card.back;
			userImageViewList.get(i).setImageResource(R.drawable.b);
			userCards[i] = Card.back;
		}
		if (decks.size() - 13 <= curDeckCard)
			shuffle();
		dealerCards[0] = decks.get(curDeckCard++);
		userCards[0] = decks.get(curDeckCard++);
		userCards[1] = decks.get(curDeckCard++);
		updateCount(dealerCards[0]);
		updateCount(userCards[0]);
		updateCount(userCards[1]);
		dealerImageViewList.get(0).setImageResource(dealerCards[0].getImageResource());
		userImageViewList.get(0).setImageResource(userCards[0].getImageResource());
		userImageViewList.get(1).setImageResource(userCards[1].getImageResource());
		curDealerCard = 1;
		curUserCard = 2;
		
		if (userCards[0].getValue() != userCards[1].getValue())
			buttonSplit.setEnabled(false);
		else
			buttonSplit.setEnabled(true);
		buttonDouble.setEnabled(true);
		buttonStay.setEnabled(true);
		buttonHit.setEnabled(true);
		int sum = sum(userCards);
		if (showScores)
			userScore.setText("" + sum);
		if (sum == 21)
		{
			userScore.setText("Blackjack!");
			buttonHit.setEnabled(false);
			buttonDouble.setEnabled(false);
			buttonStay.setEnabled(false);
			buttonSplit.setEnabled(false);
			dealDealerOut();
		}
		((TextView)findViewById(R.id.dealerScore)).setText("");
		if (showScores)
			((TextView)findViewById(R.id.dealerScore)).setText("" + sum(dealerCards));
		if (showDeck)
			deckView.setText(String.format("decks:%.1f", (52.0 * NUM_DECKS - curDeckCard) / 52.0));
		else
			deckView.setText("");
		if (showCount)
			countView.setText(String.format("count:%+d", curCount));
		else
			countView.setText("");
	}

	private void updateCount(Card card)
	{
		if (card.getValue() >= 2 && card.getValue() <= 6)
			curCount++;
		else if (card.getValue() >= 10)
			curCount--;
	}
}
