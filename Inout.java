import java.io.*;
import java.util.*;
//bowling scoreから賭けのプラスマイナスを表示するプログラム(Inout class)

class Inout/**/
{
	int num; //number of players
	int[] player_points; //for one game
	int[] handicap; //for one game
	Player[] player;
	Map <Integer, Pair<Integer, Integer> > team_points=new HashMap<Integer, Pair<Integer,Integer>>(); //<"team name", "team score, number of members">
	Inout(int num_p, Player[] p)/*{{{*/
	{
		player_points = new int[20];
		handicap = new int[20];
		num = num_p;
		player = p;
	}/*}}}*/
	boolean check(int count) throws IOException/*{{{*/
	{
		BufferedReader d=new BufferedReader(new InputStreamReader(System.in));
		String n;
		boolean rtn = true;
		System.out.printf("OK? (set handicap:[s])  [Y/n] ");
		n=d.readLine();
		if(n.equals("n")){
			rtn = false;
			for(int i=0; i<num; i++){ //remove latest score
				player[i].add_point(-player_points[i]);
				player[i].add_sum(-player_points[i]);
				team_points.get( player[i].get_team() ).first -= player_points[i];
				player_points[i] = 0;
				team_points.get( player[i].get_team() ).second -= 1;
			}
		}else if(n.equals("s")){
			set_handi(count);
			rtn = true;
		}else{
			rtn = true;
		}
		return rtn;
	}/*}}}*/
	void set_handi(int count)/*{{{*/
	{
		for(int i=0; i<num; i++){ //remove latest score
			player[i].add_point(-player_points[i]);
			team_points.get( player[i].get_team() ).first -= player_points[i];
			team_points.get( player[i].get_team() ).second -= 1;
		}
		calc_handi();
		for(int i=0; i<num; i++){
			player_points[i] += handicap[i];
			player[i].add_point(player_points[i]);
			team_points.get( player[i].get_team() ).first += player_points[i];
			team_points.get( player[i].get_team() ).second += 1;
		}
	}/*}}}*/
	void team_calc(int count) throws IOException/*{{{*/
	{
		int sum = 0; //sum points every team
		// Vector<int> team_name; //soted for num of mem;
		ArrayList<Integer> team_name = new ArrayList<Integer>(); //soted for num of mem;
		// Map<int, Pair<int, int> >::iterator it;
		// Map<int, Pair<int, int> >::iterator ite;
		boolean flag = false;
		while(!flag){
			for(int i=0; i<num; i++){ //set score to each team
				player_points[i] = player[i].input_point();
				player[i].add_sum(player_points[i]);
				Pair<Integer, Integer> pair = new Pair<Integer, Integer> (player_points[i], 1);
				if(team_points.containsKey( player[i].get_team() ) ){
					// Pair<Integer, Integer> pair = new Pair<Integer, Integer> (team_points.get( player[i].get_team() ).first + player_points[i], team_points.get( player[i].get_team() ).second + 1);
					pair.first = team_points.get( player[i].get_team() ).first + player_points[i];
					pair.second = team_points.get( player[i].get_team() ).second + 1;
				}
				team_points.put( player[i].get_team(), pair );
				// team_points.get( player[i].get_team() ).first += player_points[i];
				// team_points.get( player[i].get_team() ).second += 1;
			}
			flag = check(count);
		}
		for(int i=0; i<num; i++){ //set score to each team
			player[i].calc_ave(count);
		}
		// for(int key : team_points.keySet()){team_name.push_back(key);} //set team name
		for(Iterator<Integer> iterator = team_points.keySet().iterator(); iterator.hasNext();){team_name.add(iterator.next());} //set team name
		// for(it=team_points.begin(); it!=team_points.end(); it++){team_name.push_back(it->first);} //set team name
		sort_by_sn(team_points, team_name, 1); //score:0, num of men:1
		int num_teams = team_name.size(); //number of teams
		float ratio = 1.0f; //ratio of members
		for(int i=0; i<num_teams-1; i++){
			ratio = 1.0f*team_points.get(team_name.get(i)).second / team_points.get(team_name.get(i+1)).second;
			team_points.get(team_name.get(i+1)).first = (int)(ratio*team_points.get(team_name.get(i+1)).first.intValue()+0.5f);
			sum += team_points.get(team_name.get(i)).first;
		}
		sum += team_points.get(team_name.get(num_teams-1)).first;
		int ave = sum / num_teams;
		sum = 0;
		for(Pair<Integer,Integer> value : team_points.values()){ //calc each team's income and expenditure
			value.first -= ave;
			sum += value.first;
		}
		// for(it=team_points.begin(); it!=team_points.end(); it++){ //calc each team's income and expenditure
		// 	it->second.first -= ave;
		// 	sum += it->second.first;
		// }
		sort_by_sn(team_points, team_name, 0); //score:0, num of mem:1
		for(int i=0; i<num_teams-1; i++){
			if(sum==0){break;}
			sum--;
			team_points.get(team_name.get(i)).first -= 1;
		}
	}/*}}}*/
	void player_calc(int rate)/*{{{*/
	{
		int sign = 1;
		int score_abs = 0;
		int player_inout[] = {0,0,0,0,0,0,0,0}; //each player's income and expenditure
		// Map<int, Pair<int, int> >::iterator it;
		int n = 0; //use in for loop

		// my_sort<Player, int>(player, num, player_points, sign+1);
		// my_sort(player, num, player_points, (sign+1!=0));
		p_sort(player, num, player_points, (sign+1!=0));
		// for(it=team_points.begin(); it!=team_points.end(); it++){ //calc each player's income and expenditure
		for(Map.Entry<Integer, Pair<Integer,Integer>> entry : team_points.entrySet()){ //calc each player's income and expenditure
			score_abs = entry.getValue().first;
			sign = score_abs < 0 ? -1 : 1;
			score_abs *= sign;
			while( score_abs>0 ){
				for(int i=0; score_abs!=0 && i<num ; i++){
					n = sign+1!=0 ? i : num-1-i;
					if( player[n].get_team() == entry.getKey() ){
						score_abs -= 1;
						player_inout[n] += sign;
					}
				}
			}
		}
		for(int i=0;i<num;i++){
			player[i].add_income_expenditure(player_inout[i]*rate);
		}
		// my_sort<Player, int>(player, num, player_inout, true);
		p_sort(player, num, player_points, (sign+1!=0));
		// my_sort(player, num, player_points, sign+1);
		// for(int i=0;i<num;i++){//last result show
		// 	player[i].show_income_expenditure();
		// }
	}/*}}}*/
	void calc_handi()/*{{{*/
	{
		float ave = 0;
		float max = player[0].get_ave();
		for(int i=1;i<num;i++){
			ave = player[i].get_ave();
			if(max < ave){
				max = ave;
			}
		}
		max = (int)max - (int)max%10;
		for(int i=0; i<num; i++){
			ave = player[i].get_ave();
			ave += (10.0/3.0) * 3;
			ave = (int)ave - (int)ave %10;
			handicap[i] = (int)max - (int)ave;
			handicap[i] = handicap[i]<0 ? 0 : handicap[i];
		}
	}/*}}}*/

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
	void p_sort(Player[] player, int size, int[] points, boolean reverse)/*{{{*/
	{
		Player p;
		int point;
		int sign = reverse ? 1:-1; //true:DESCENDING, false:ASCENDING;
		for(int i=0; i<size; i++){
			for(int j=i; j<size; j++){
				if(sign * points[i] < sign * points[j]){
					p = player[i];
					point = points[i];

					player[i] = player[j];
					points[i] = points[j];

					player[j] = p;
					points[j] = point;
				}
			}
		}
	}/*}}}*/
	ArrayList<Integer> sort_by_sn(Map <Integer, Pair<Integer, Integer> > team_points, ArrayList<Integer> team_name, int s_n)//score_numOFmem:0_1/*{{{*/
	{
		int tmp; //sort swap tempolary
		boolean flag = false;
		// sort(team_name.begin(),team_name.end()); //sort in ascending order by name ([1]1,[2]2,[0]3,...)
		Collections.sort(team_name); //sort in ascending order by name ([1]1,[2]2,[0]3,...)
		int len = team_name.size();

		// srand((unsigned int)time(NULL));rand();

		ArrayList<Integer> t_name=new ArrayList<Integer>();
		for(int i=0;i<len;i++){t_name.add(team_name.get(i));}

		for(int i=0;i<len;i++){
		}
		switch(s_n){
			case 0: //sort in ASCENDING order by SCORE
				for(int i=0;i<len;i++){
					flag = false;
					for(int j=len-1;j>i;j--){
						if( team_points.get(team_name.get(i)).first >= team_points.get(team_name.get(j)).first ){
							flag = true;
							if(team_points.get(team_name.get(i)).first == team_points.get(team_name.get(j)).first){
								flag = ((int)(Math.random()*100000) % 2) != 0 ? true : false;
							}
						}
						if(flag){
							tmp = t_name.get(i);
							t_name.set(i, t_name.get(j));
							t_name.set(j, tmp); //swap
						}
					}
				}
				break;
			case 1: //sort in DESCENDING order by NUMBER OF MEMBERS
				for(int i=0;i<len;i++){
					flag = false;
					for(int j=len-1;j>i;j--){
						if( team_points.get(team_name.get(i)).second <= team_points.get(team_name.get(j)).second ){
							flag = true;
							if(team_points.get(team_name.get(i)).second == team_points.get(team_name.get(j)).second){
								// flag = rand()%2 ? true : false;
								flag = ((int)(Math.random()*100000) % 2) != 0 ? true : false;
							}
						}
						if(flag){
							tmp = t_name.get(i);  t_name.set(i, t_name.get(j));  t_name.set(j, tmp); //swap
						}
					}
				}
				break;
			default:
				System.out.println("error");
				break;
		};
		for(int i=0;i<len;i++){
			team_name.set(i, t_name.get(i));
		}
		return t_name;
	}/*}}}*/

};
