package com.ott.blackjacktraining;

import static com.ott.blackjacktraining.R.drawable.*;
public enum Card
{
	club2('2', c2),
	club3('3', c3),
	club4('4', c4),
	club5('5', c5),
	club6('6', c6),
	club7('7', c7),
	club8('8', c8),
	club9('9', c9),
	club10('t', ct),
	clubJ('j', cj),
	clubQ('q', cq),
	clubK('k', ck),
	clubA('a', ca),
	diamond2('2', d2),
	diamond3('3', d3),
	diamond4('4', d4),
	diamond5('5', d5),
	diamond6('6', d6),
	diamond7('7', d7),
	diamond8('8', d8),
	diamond9('9', d9),
	diamond10('t', dt),
	diamondJ('j', dj),
	diamondQ('q', dq),
	diamondK('k', dk),
	diamondA('a', da),
	heart2('2', h2),
	heart3('3', h3),
	heart4('4', h4),
	heart5('5', h5),
	heart6('6', h6),
	heart7('7', h7),
	heart8('8', h8),
	heart9('9', h9),
	heart10('t', ht),
	heartJ('j', hj),
	heartQ('q', hq),
	heartK('k', hk),
	heartA('a', ha),
	spade2('2', s2),
	spade3('3', s3),
	spade4('4', s4),
	spade5('5', s5),
	spade6('6', s6),
	spade7('7', s7),
	spade8('8', s8),
	spade9('9', s9),
	spade10('t', st),
	spadeJ('j', sj),
	spadeQ('q', sq),
	spadeK('k', sk),
	spadeA('a', sa),
	back('b', b);
	
	private int value;
	private int draw;
	public boolean isAce()
	{
		return value == 11;
	}
	public int getValue()
	{
		return value;
	}
	public int getImageResource()
	{
		return draw;
	}
	Card(char file, int d)
	{
		draw = d;
		if (file >= '2' && file <= '9')
			value = file - '0';
		else if (file == 'a')
			value = 11;
		else if (file == 'b')
			value = 0;
		else
			value = 10;
	}
}
