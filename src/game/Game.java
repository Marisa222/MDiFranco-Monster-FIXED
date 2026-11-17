package game;
import java.util.ArrayList;

import gui.MonsterBattleGUI;


public class Game {
    
    // The window. You call its methods; you never need to open src/gui.
    private MonsterBattleGUI gui;
    
    // Game state: change these, then tell the gui.
    private ArrayList<Monster> monsters;
    private ArrayList<Item> inventory;
    private int playerHealth;
    private int shield = 0;
    private int SHIELD_UP = 20;
    private int maxHealth;
    
    public static void main(String[] args) {
        // main is static, so it makes one Game object to play (Unit 3.7).
        Game game = new Game();
        game.play();
    }
    
    public void play() {
        setupGame();
        gameLoop();
    }

    // ACCESORS
    public MonsterBattleGUI getGUI() { return gui; }
    public ArrayList<Monster> getMonsters() { return monsters; }
    
    
    private void setupGame() {
        // Create the GUI
        gui = new MonsterBattleGUI("Marisa's Monster Battle");
        

        int numMonsters = chooseDifficulty();

        maxHealth = 100;
        playerHealth = 75;
        gui.setPlayerMaxHealth(maxHealth);
        gui.updatePlayerHealth(playerHealth);
        
        monsters = new ArrayList<>();
        for (int i = 0; i < numMonsters; i++) {
            monsters.add(new Monster(this));
            monsters.add(new Monster (this, "pfundstein"));
            monsters.add(new Monster (this, "Quaffle"));
        }
        gui.updateMonsters(monsters);
        
        // TODO: Create starting items (GameDemo's addBomb() shows the shape)
        inventory = new ArrayList<>();
        gui.updateInventory(inventory);
        
        String[] buttons = {"Attack", "Defend", "Heal", "Use Item"};
        gui.setActionButtons(buttons);
        
        // Welcome message
        gui.displayMessage("Battle Start! Choose your action.");
    }
    
    // Difficulty

    private int chooseDifficulty(){
        String[] difficulties = {"Easy (2)", "Medium (3)", "Hard (4)", "Extreme (5)"};
        gui.setActionButtons(difficulties);

        gui.displayMessage("CHOOSE YOUR DIFFICULTY:");

        int choice = gui.waitForAction();

        int numMonsters = 1 + choice;

        gui.displayMessage("Difficulty selected: " + difficulties[choice] + " monsters!");
        gui.pause(1500);
        
        return numMonsters;
    }


    // Player turn, then monster turn, until one side is gone.
    private void gameLoop() {
        // Keep playing while monsters alive and player alive
        while (countLivingMonsters() > 0 && playerHealth > 0) {
            
            // PLAYER'S TURN
            gui.displayMessage("Your turn! HP: " + playerHealth);
            int action = gui.waitForAction();  // Wait for button click (0-3)
            handlePlayerAction(action);
            gui.updateMonsters(monsters);
            gui.pause(500);
            
            // MONSTER'S TURN (if any alive and player alive)
            if (countLivingMonsters() > 0 && playerHealth > 0) {
                monsterAttack();
                gui.updateMonsters(monsters);
                gui.pause(500);
            }
        }
        
        // Game over!
        if (playerHealth <= 0) {
            gui.displayMessage("💀 DEFEAT! You have been defeated...");
        } else {
            gui.displayMessage("🎉 VICTORY! You defeated all monsters!");
        }
    }
    
    // The four buttons, left to right, are 0 to 3.
    private void handlePlayerAction(int action) {
        switch (action) {
            case 0: // Attack button
                attackMonster();
                break;
            case 1: // Defend button
                defend();
                break;
            case 2: // Heal button
                heal();
                break;
            case 3: // Use Item button
                useItem();
                break;
        }
    }
    
    
    private void attackMonster() {
        Monster target = getRandomLivingMonster();

        // determine my damage
        int damage = (int)(Math.random() * 10 + 10);
        // apply damage
        target.takeDamage(damage);
        // show message
        gui.displayMessage("Hit for " + damage);
        
                
            }
        
    
    private void defend() {
        shield += SHIELD_UP;
        // EXTRA CREDIT: show shield on the GUI
        gui.displayMessage("SHIELD UP! Current Shield: " + shield);
    }
    
    private void heal() {
        int healNumber = 20; // WRONG
        playerHealth += healNumber;
        // SUPER HEAL?
        if(playerHealth > maxHealth){
            maxHealth = playerHealth;
            gui.setPlayerMaxHealth(maxHealth);
        }
        gui.updatePlayerHealth(playerHealth);
        gui.displayMessage("You healed for " + healNumber + " points!");
    }
    
    // Uses the first item in the inventory.
    private void useItem() {
        if (inventory.isEmpty()) {
            gui.displayMessage("No items in inventory!");
            return;
        }
        
        // Use first item
        Item item = inventory.remove(0);
        gui.updateInventory(inventory);
        item.use();  // The item knows what to do!
    }
    
    
    private void monsterAttack() {
        Monster monster = getRandomLivingMonster();
        double incomingDamage = monster.damage();
        if(shield > 0){
            // reduce the incoming damage by shield
            if(shield > incomingDamage){
                gui.displayMessage("Your shield blocked the attack.");
            }else {
                incomingDamage -= shield;
                gui.displayMessage("Your shield took " + incomingDamage);
            }
        }
        playerHealth -= incomingDamage;
        gui.displayMessage("Monster hits for " + incomingDamage);
        gui.updatePlayerHealth(playerHealth);

    }

    
    // Helpers. Add your own below these two.
    private int countLivingMonsters() {
        int count = 0;
        for (Monster m : monsters) {
            if (m.health() > 0) count++;
        }
        return count;
    }
    
    private Monster getRandomLivingMonster() {
        ArrayList<Monster> alive = new ArrayList<>();
        for (Monster m : monsters) {
            if (m.health() > 0) alive.add(m);
        }
        if (alive.isEmpty()) return null;
        return alive.get((int)(Math.random() * alive.size()));
    }
}