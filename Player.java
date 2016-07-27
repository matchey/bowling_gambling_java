import java.io.*;
//bowling scoreから賭けのプラスマイナスを表示するプログラム(Player class)
class Player
{
	String name;//player name
	int points;//player's latest score
	int team;//player's team
	float income_expenditure;//income and expenditure
	float average; //player's score average(include handicap)
	int sum; //player's score sum
	Player(){ points = 0; sum = 0; };
	String get_name(){return name;};
	int get_points(){return points;};
	int get_team(){return team;};
	float get_ave(){return average;};
	int get_sum(){return sum;};
	void set_name(String x){ name = x; };
	void set_team(int x){ team = x;};
	void set_team_manual() throws IOException/*{{{*/
	{
		BufferedReader d=new BufferedReader(new InputStreamReader(System.in));
		int team_num;
		while(true){
			System.out.printf("Type "+name+"'s team number: ");
			String s;
			s=d.readLine();
			try{
				team_num=Integer.parseInt(s);
			}
			catch(java.lang.NumberFormatException e){
				System.out.println("team number must be in 0~20");
				continue;
			}
			if(!(team_num<0||team_num>20)) break;
		}
		team = team_num;
	}/*}}}*/
	void add_income_expenditure(int x){ income_expenditure += x; };
	void add_point(int x){ points += x; };
	void add_sum(int x){ sum += x; };
	int input_point() throws IOException/*{{{*/
	{
		BufferedReader d=new BufferedReader(new InputStreamReader(System.in));
		int point;
		while(true){
			System.out.printf("Type "+name+"'s score : ");
			String s;
			s=d.readLine();
			try{
				point=Integer.parseInt(s);
			}
			catch(java.lang.NumberFormatException e){
				System.out.println("team number must be in 0~300");
				continue;
			}
			if(!(point<0||point>300)) break;
		}
		points = point;
		return points;
	}/*}}}*/
	void show_score()/*{{{*/
	{
		System.out.println(name+" has "+points+" points");
	}/*}}}*/
	void show_income_expenditure()/*{{{*/
	{
		System.out.printf(name+": ");
		System.out.printf("%5d yen\n",(int)income_expenditure);
	}/*}}}*/
	void show_ave()/*{{{*/
	{
		System.out.printf(name+"'s average: ");
		System.out.printf("%4.1f\n", average);
	}/*}}}*/
	float calc_ave(int count)/*{{{*/
	{
		average = ( (count -1.0f ) * average + points ) / count;
		return average;
	}/*}}}*/
};
