import java.util.Scanner;

public class BattleMonster {
    // CLASS (NOT INSTANCE) VARIABLES
    private static Puppy puppy;
    private static Monster[] monsters = new Monster[5];
    private static int playerHealth = 100;
    private static int maxDmg = 100;

    public static void main(String[] args){
        // SETUP
        Scanner s = new Scanner(System.in);
        String input = "";
        
        // INTRO
        System.out.println("Your first choice: Fight or puppy?");
        
        // GAME LOOP
        do {
            // CHECK FOR MONSTER
            if(noMonsters()) makeMonster();

            // TAKE INPUT
            System.out.print("INPUT: ");
            input = s.nextLine().toLowerCase().trim();

            // OUR TURN
            if(input.equals("puppy") && puppy == null){
                puppy = new Puppy();
            }
            // ATTACK
            else if(input.equals("attack")) {
                // check if there's a puppy
                if(puppy != null){
                    // check if the puppy attacks
                    johnWick();
                }

                // if no dog and no dog attack, roll for damage

                // apply damage to first? monster
            }
            // HEAL

            // MONSTERS TURN
            

        } while(!input.equals("quit"));
    }
    
    /**
     * Checks for any monsters
     * @return true if no monsters are in the monsers array
     */
    public static boolean noMonsters() {
        // loop and check for monsters
        for(int i = 0; i < monsters.length; i++){
            if(monsters[i] != null) return false;
        }
        return true;
    }

    /**
     * Instantiates a monster at the first null location. Does nothing otherwise.
     */
    public static void makeMonster() {
        // loop and find the first free spot and set it equal to our "something"
        for(int i=0; i < monsters.length; i++){
            if (monsters[i] == null){
                monsters[i] = new Monster();
                return;
            }
        }
    }

    public static void johnWick() {
        // loop through all the monsters and the puppy destroys them
        for(Monster m : monsters){
            if(m != null) m.takeDmg(m.health());
        }
    }
}
