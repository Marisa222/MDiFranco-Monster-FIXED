package gui;

import javax.swing.*;
import game.Monster;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

/**
 * MonsterDisplayPanel - Displays monster tiles in a responsive grid
 * Styled with a glowing jellyfish / bioluminescent deep-sea palette.
 */
public class MonsterDisplayPanel extends JPanel {
    
    private ArrayList<Monster> monsters;
    private ArrayList<MonsterTile> tiles;
    private int highlightedIndex = -1;
    
    // Jellyfish Theme - Abyssal Navy Background & Bioluminescent Cyan Accent
    private static final Color BACKGROUND_COLOR = new Color(11, 19, 43); // Deep ocean abyssal navy
    private static final Color BORDER_GLOW = new Color(0, 245, 212);     // Bioluminescent cyan/mint
    private static final int TILE_SPACING = 12;
    
    /**
     * Constructor
     */
    public MonsterDisplayPanel() {
        monsters = new ArrayList<>();
        tiles = new ArrayList<>();
        setBackground(BACKGROUND_COLOR);
        
        // Styled title border with bioluminescent cyan text and outline
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_GLOW, 2),
            " 🪼 MONSTERS ",
            0, 0, new Font("Segoe UI", Font.BOLD, 16), BORDER_GLOW));
    }
    
    /**
     * Set the monsters to display
     * @param monsters The list of monsters
     */
    public void setMonsters(ArrayList<Monster> monsters) {
        this.monsters = monsters;
        this.highlightedIndex = -1;
        createTiles();
    }
    
    /**
     * Highlight a specific monster
     * @param index The index to highlight (-1 for none)
     */
    public void highlightMonster(int index) {
        this.highlightedIndex = index;
        repaint();
    }
    
    /**
     * Create tiles for all monsters
     */
    private void createTiles() {
        removeAll();
        tiles.clear();
        
        if (monsters == null || monsters.isEmpty()) {
            JLabel emptyLabel = new JLabel("No monsters drifting in the abyss...");
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            emptyLabel.setForeground(new Color(144, 224, 239));
            add(emptyLabel);
            revalidate();
            repaint();
            return;
        }
        
        // Determine grid layout
        int monsterCount = monsters.size();
        int cols = (int) Math.ceil(Math.sqrt(monsterCount));
        int rows = (int) Math.ceil((double) monsterCount / cols);
        
        setLayout(new GridLayout(rows, cols, TILE_SPACING, TILE_SPACING));
        
        // Create a tile for each monster
        for (int i = 0; i < monsters.size(); i++) {
            MonsterTile tile = new MonsterTile(monsters.get(i), i);
            tiles.add(tile);
            add(tile);
        }
        
        revalidate();
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Update highlighting
        for (int i = 0; i < tiles.size(); i++) {
            tiles.get(i).setHighlighted(i == highlightedIndex);
        }
    }
    
    /**
     * MonsterTile - Individual monster display card
     */
    // Static inner class for Java 8 compatibility
    private static class MonsterTile extends JPanel {
        private Monster monster;
        private int index;
        private boolean highlighted = false;
        
        // Jellyfish Theme Palette
        private static final Color TILE_BG_TOP = new Color(28, 37, 65);       // Deep twilight ocean
        private static final Color TILE_BG_BOTTOM = new Color(15, 23, 42);    // Midnight abyss
        private static final Color TILE_BORDER = new Color(72, 149, 239, 150); // Soft jellyfish violet border
        private static final Color HIGHLIGHT_COLOR = new Color(247, 37, 133);  // Electric neon pink glow
        private static final Color DEAD_COLOR_TOP = new Color(40, 20, 35);    // Faded deep crimson twilight
        private static final Color DEAD_COLOR_BOTTOM = new Color(20, 10, 20); // Faded sunken obsidian
        
        // Stats colors
        private static final Color NAME_COLOR = new Color(224, 251, 252);     // Bioluminescent ice white
        private static final Color HP_TEXT_COLOR = new Color(114, 9, 183);    // Deep jellyfish purple
        private static final Color DMG_TEXT_COLOR = new Color(247, 37, 133);   // Bright neon pink
        private static final Color SPD_TEXT_COLOR = new Color(76, 201, 240);   // Electric aqua
        private static final Color SPECIAL_COLOR = new Color(255, 183, 3);    // Soft amber glow
        
        // Health bar gradient colors
        private static final Color HEALTH_BAR_BG = new Color(10, 14, 26);
        private static final Color HEALTH_FULL = new Color(0, 245, 212);       // Electric cyan
        private static final Color HEALTH_MED = new Color(114, 9, 183);        // Deep bioluminescent purple
        private static final Color HEALTH_LOW = new Color(247, 37, 133);       // Hot neon pink
        
        public MonsterTile(Monster monster, int index) {
            this.monster = monster;
            this.index = index;
            setLayout(new BorderLayout(8, 8));
            setOpaque(false); // Enable custom paintComponent with smooth rounded gradients
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TILE_BORDER, 2),
                BorderFactory.createEmptyBorder(6, 6, 6, 6)
            ));
            createLayout();
        }
        
        private void createLayout() {
            // Monster info panel
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setOpaque(false);
            
            // Monster number/name
            JLabel numberLabel = new JLabel("Monster #" + index);
            numberLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            numberLabel.setForeground(NAME_COLOR);
            numberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Stats
            JLabel healthLabel = new JLabel("HP: " + monster.health());
            healthLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            healthLabel.setForeground(HP_TEXT_COLOR.brighter());
            healthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel damageLabel = new JLabel("DMG: " + monster.damage());
            damageLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            damageLabel.setForeground(DMG_TEXT_COLOR);
            damageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel speedLabel = new JLabel("SPD: " + monster.speed());
            speedLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            speedLabel.setForeground(SPD_TEXT_COLOR);
            speedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Special ability if exists
            String special = monster.special();
            if (special != null && !special.isEmpty()) {
                Font specialFont = new Font("Segoe UI", Font.ITALIC, 11);
                JLabel specialLabel = new JLabel(Emoji.fit("✨ " + special, specialFont));
                specialLabel.setForeground(SPECIAL_COLOR);
                specialLabel.setFont(specialFont);
                specialLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                infoPanel.add(specialLabel);
                infoPanel.add(Box.createVerticalStrut(2));
            }
            
            infoPanel.add(numberLabel);
            infoPanel.add(Box.createVerticalStrut(4));
            infoPanel.add(healthLabel);
            infoPanel.add(damageLabel);
            infoPanel.add(speedLabel);
            
            add(infoPanel, BorderLayout.CENTER);
            
            // Health bar at bottom
            add(new HealthBar(), BorderLayout.SOUTH);
        }
        
        public void setHighlighted(boolean highlighted) {
            if (this.highlighted != highlighted) {
                this.highlighted = highlighted;
                Color currentBorder = highlighted ? HIGHLIGHT_COLOR : TILE_BORDER;
                int thickness = highlighted ? 3 : 2;
                
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(currentBorder, thickness),
                    BorderFactory.createEmptyBorder(6, 6, 6, 6)
                ));
                repaint();
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            boolean isAlive = monster.health() > 0;
            Color topColor = isAlive ? TILE_BG_TOP : DEAD_COLOR_TOP;
            Color bottomColor = isAlive ? TILE_BG_BOTTOM : DEAD_COLOR_BOTTOM;
            
            // Vertical abyssal ocean gradient fill
            GradientPaint bgGradient = new GradientPaint(
                0, 0, topColor,
                0, getHeight(), bottomColor
            );
            g2.setPaint(bgGradient);
            g2.fillRect(0, 0, getWidth(), getHeight());
            
            g2.dispose();
            super.paintComponent(g);
        }
        
        /**
         * Visual health bar - Bioluminescent Glow Style
         */
        private class HealthBar extends JPanel {
            private static final int BAR_HEIGHT = 16;
            
            public HealthBar() {
                setPreferredSize(new Dimension(0, BAR_HEIGHT));
                setOpaque(false);
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                
                // Draw health bar background channel
                g2.setColor(HEALTH_BAR_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, width, height, 8, 8));
                
                // Calculate health percentage
                double healthPercent = Math.max(0, Math.min(100, monster.health())) / 100.0;
                int barWidth = (int) (width * healthPercent);
                
                if (barWidth > 0) {
                    // Pick gradient color scheme based on health remaining
                    Color mainColor;
                    Color highlightColor;
                    if (healthPercent > 0.5) {
                        mainColor = HEALTH_FULL;
                        highlightColor = new Color(180, 255, 240);
                    } else if (healthPercent > 0.25) {
                        mainColor = HEALTH_MED;
                        highlightColor = new Color(200, 150, 255);
                    } else {
                        mainColor = HEALTH_LOW;
                        highlightColor = new Color(255, 150, 200);
                    }
                    
                    // Bioluminescent bar gradient fill
                    GradientPaint barGradient = new GradientPaint(
                        0, 0, highlightColor,
                        0, height, mainColor
                    );
                    g2.setPaint(barGradient);
                    g2.fill(new RoundRectangle2D.Float(0, 0, barWidth, height, 8, 8));
                }
                
                // Outer glow stroke around the health bar
                g2.setColor(new Color(255, 255, 255, 40));
                g2.draw(new RoundRectangle2D.Float(0, 0, width - 1, height - 1, 8, 8));
                
                g2.dispose();
            }
        }
    }
}