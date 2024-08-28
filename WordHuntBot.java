import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class WordHuntBot{
	static HashSet<String> dictionary;
	static HashSet<String> words;
	static String[][] grid;
	
	public static void main(String[] args){
		// create dictionary
		dictionary = new HashSet<>();
		try (BufferedReader br = new BufferedReader(new FileReader("dictionary.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                dictionary.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		// create words
		words = new HashSet<>();
		
		// create grid
		grid = new String[4][4];
		Scanner scanner = new Scanner(System.in);
		for(int row = 0; row < 4; row++){
			System.out.print("Enter row " + Integer.toString(row + 1) + " ");
			String s = scanner.nextLine();
			for(int ch = 0; ch < 4; ch++){
				grid[row][ch] = s.substring(ch, ch + 1);
			}
		}
		solve();
	}
	static void solve(){
		// begin at each letter
		for(int r = 0; r < 4; r++){
			for(int c = 0; c < 4; c++){
				dfs(createVisited(4), "", r, c, 0);
			}
		}
		
		// sort the words
		ArrayList<String> sorted = new ArrayList<String>();
		for (String word : words) {
            sorted.add(word);
        }
		Collections.sort(sorted, new CustomComparator());
		
		System.out.println(sorted);
	}
	static void dfs(boolean[][] visited, String curr, int row, int col, int len){
		visited[row][col] = true;
		
		// possible directions
		int[][] dir = {
			{0, 1},
			{0, -1},
			{1, 0},
			{-1, 0},
			{1, 1},
			{-1, -1},
			{1, -1},
			{-1, 1}
		};
		
		// check current word
		curr += grid[row][col];
		len += 1;
		if(len >= 3 && exists(curr)){
			words.add(curr);
		}
		
		// dfs all possible directions
		for(int i = 0; i < dir.length; i++){
			int newR = row + dir[i][0];
			int newC = col + dir[i][1];
			if(valid(visited, newR, newC)){
				dfs(visited, curr, newR, newC, len);
				visited[newR][newC] = false;
			}
		}
	}
	static boolean[][] createVisited(int l){
		// new visited array
		boolean[][] visited = new boolean[l][l];
		for(int i = 0; i < visited.length; i++){
			for(int j = 0; j < visited[0].length; j++){
				visited[i][j] = false;
			}
		}
		return visited;
	}
	static boolean valid(boolean[][] visited, int row, int col){
		// check if a position si valid
		return row >= 0 && row < visited.length && col >= 0 && col < visited[0].length && !visited[row][col];
	}
	static boolean exists(String s){
		// check if a word exists
		return dictionary.contains(s);
	}
	
	public static class CustomComparator implements Comparator<String> {
		@Override
		public int compare(String a, String b) {
			return b.length() - a.length();
		}
	}
}