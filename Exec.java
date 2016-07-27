import java.io.*;
//bowling scoreから賭けのプラスマイナスを表示するプログラム(exe class)
class Exec
{
	int set_player_num() throws IOException/*{{{*/
	{
		BufferedReader d=new BufferedReader(new  InputStreamReader(System.in));
		int num = 1;
		while(true){
			System.out.printf("input number of players: ");
			String s;
			s=d.readLine();
			try{
				num = Integer.parseInt(s);
			}
			catch(java.lang.NumberFormatException e){
				System.out.println("number of players must be in 1~20");
				continue;
			}
			if(!(num<1||num>20)) break;
		}
		return num;
	}/*}}}*/

	void set_each_name(int num, Player[] player) throws IOException/*{{{*/
	{
		BufferedReader d=new BufferedReader(new InputStreamReader(System.in));
		String tmp;
		for(int i=0;i<num;i++){
			System.out.printf("type player"+(i+1)+"'s name: ");
			tmp=d.readLine();
			player[i].set_name(tmp);
		}
	}/*}}}*/

	// template<class T> void shuffle(T ary[], int size)
	public static <T> void shuffle(T ary[], int size)/*{{{*/
	{
		int j = 0;
		T t;
		// srand((unsigned int)time(NULL));
		for(int i=0; i<size; i++){
			j = (int)(Math.random()*1000000000)%size;
			t = ary[i];
			ary[i] = ary[j];
			ary[j] = t;
		}
	}/*}}}*/

	// template<class KEY, class VALUE> void my_sort(KEY key[], int size, VALUE value[], bool reverse)
	// public static <KEY, VALUE> void my_sort(KEY key[], int size, VALUE value[], boolean reverse)#<{(|{{{|)}>#
	// {
	// 	KEY k;
	// 	VALUE v;
	// 	int sign = reverse ? 1:-1; //true:DESCENDING, false:ASCENDING;
	// 	for(int i=0; i<size; i++){
	// 		for(int j=i; j<size; j++){
	// 			if(sign * value[i] < sign * value[j]){
	// 				k = key[i];
	// 				v = value[i];
    //
	// 				key[i] = key[j];
	// 				value[i] = value[j];
    //
	// 				key[j] = k;
	// 				value[j] = v;
	// 			}
	// 		}
	// 	}
	// }#<{(|}}}|)}>#

	void set_team_auto(int num, Player[] player) throws IOException/*{{{*/
	{
		BufferedReader d=new BufferedReader(new  InputStreamReader(System.in));
		int team_num = 1;//number of teams
		if(num > 1){
			while(true){
				System.out.printf("Type a number of teams: ");
				String s;
				s=d.readLine();
				try{
					team_num = Integer.parseInt(s);
				}
				catch(java.lang.NumberFormatException e){
					System.out.println("team number must be in 1~20");
					continue;
				}
				if(!(team_num<1||team_num>num)) break;
			}
		}
		int team = 1;
		for(int i=0;i<team_num;i++){
			System.out.printf("team%d:         ",i+1);
		}
		System.out.println();
			shuffle(player, num);
			// shuffle<Player>(player, num);
			for(int i=0;i<num;i++){
				player[i].set_team(team);
				System.out.printf("%-15s",player[i].get_name());
				// System.out.printf("%-15s",player[i].get_name().c_str());
				team += 1;
				if(team > team_num){
					System.out.println();
						team = 1;
				}
			}
			System.out.println();
	}/*}}}*/

	void set_team_select(int num, Player[] player) throws IOException/*{{{*/
	{
		BufferedReader d=new BufferedReader(new InputStreamReader(System.in));
		int select_mode = 1;
		if(num > 1){
			System.out.println("team set  AUTO:1   or   MANUAL:2 ");
			System.out.printf("select: ");
			while(true){
				String s;
				s=d.readLine();
				try{
					select_mode=Integer.parseInt(s);
				}
				catch(java.lang.NumberFormatException e){
					System.out.println("select 1 or 2");
					System.out.printf("Type mode(1~2):");
					continue;
				}
				if(!(select_mode<1||select_mode>2)) break;
			}
		}
		switch(select_mode){
			case 1:
				set_team_auto(num, player);
				break;
			case 2:
				for(int i=0;i<num;i++){
					player[i].set_team_manual();
				}
				System.out.println();
				break;
			default:
				set_team_auto(num, player);
				System.out.println("error");
				break;
		};
	}/*}}}*/

	int set_rate(int base_rate)/*{{{*/
	{
		int rate = base_rate;
		boolean flag = false;
		if(Math.random()*10<3){
			flag = true;
		}
		if(flag){
			rate += 10 * Math.floor((Math.random()*2)+1);
		}
		return rate;
	}/*}}}*/

	int change_rate() throws IOException/*{{{*/
	{
		BufferedReader d=new BufferedReader(new InputStreamReader(System.in));
		int rate = 1;
		// System.out.println("team set  AUTO:1   or   MANUAL:2 ");
		// System.out.printf("select: ");
		System.out.printf("input rate: ");
		while(true){
			String s;
			s=d.readLine();
			try{
				rate=Integer.parseInt(s);
			}
			catch(java.lang.NumberFormatException e){
				System.out.println("rate must be in 1~100");
				System.out.printf("Type number of rate(1~100):");
				continue;
			}
			if(!(rate<1||rate>100)) break;
		}
		return rate;
	}/*}}}*/

	// typedef Pair<Integer, Integer> ass_arr;#<{(|{{{|)}>#
	// bool sort_less(ass_arr left,ass_arr right){
	// 	return left.second < right.second;
	// }
	// bool sort_greater(ass_arr left,ass_arr right){
	// 	return left.second > right.second;
	// }#<{(|}}}|)}>#

	boolean select_cntn(Integer[] rate) throws IOException/*{{{*/
	{
		BufferedReader d=new BufferedReader(new InputStreamReader(System.in));
		String n;
		boolean rtn = true;
		System.out.printf("Continue? (change rate:[c]) [Y/n]");
		n=d.readLine();
		if(n.equals("n")){
			rtn = false;
		}else if(n.equals("c")){
			rate[0] = change_rate();
			rtn = true;
		}else{
			rtn = true;
		}
		return rtn;
	}/*}}}*/

	void show_result(int num, Player[] player, int count)/*{{{*/
	{
		float ave = 0.0f;
		for(int i=0;i<num;i++){//show last average
			// player[i].show_ave();
			ave = player[i].get_sum() / (count - 1.0f);
			System.out.printf(player[i].get_name()+"'s average: ");
			System.out.printf("%4.1f\n", ave);
		}
		System.out.println();
		for(int i=0;i<num;i++){//show latest income and expenditure
			player[i].show_income_expenditure();
		}
	}/*}}}*/

	void execute(int num, Player[] player, Integer[] gam_rate, int count) throws IOException/*{{{*/
	{
		int rate = gam_rate[0].intValue();
		set_team_select(num, player);
		rate = set_rate(rate);
		System.out.println("===== x"+rate+" ====");
		Inout in_out = new Inout(num, player);
		in_out.team_calc(count);
		in_out.player_calc(rate);
	}/*}}}*/

	void spin() throws IOException
	{
		int num = set_player_num();
		Integer[] rate = {10};
		int count = 1;
		boolean flag = true;
		Player[] player = new Player[num];
		for(int i=0;i<num;i++) player[i] = new Player();
		set_each_name(num, player);
		while(flag){
			execute(num, player, rate, count);
			flag = select_cntn(rate);
			count++;
		}
		show_result(num, player, count);
		// delete [] player;
	}

	public static void main(String args[]) throws IOException
	{
		Exec exe = new Exec();
		exe.spin();
	}
};
