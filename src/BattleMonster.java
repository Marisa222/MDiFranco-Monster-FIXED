import java.util.Scanner;

public class BattleMonster {

    public static void main(String[] args){
        Monster m = new Monster();
        Monster m2 = new Monster();

        Scanner s = new Scanner(System.in);
        String input = "";
        do {
            System.out.print("INPUT: ");
            input = s.nextLine();
        } while(!input.equals("quit"));
    }
    
}
