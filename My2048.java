import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class My2048 {
	public static void main(String[] args) {
		new MainFrame();
	}
}

class MainFrame extends JFrame implements KeyListener{
	
	public class GridPosition {
		public int i;
		public int j;
		public GridPosition(int i, int j) {
			this.i = i;
			this.j = j;
		}
	}
	
	public JLabel[][] grid = new JLabel[4][4];;
	
	public int[][] states = {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
	
	public boolean isMerged = false;
	
	public MainFrame() {
		this.setComponentsUI();
		this.setMainFrameUI();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.initGame();
		this.addKeyListener(this);
	}
	
	public void setComponentsUI() {
		int i, j;
		for(i = 0; i < 4; i++)
		{
			for(j = 0; j < 4; j++)
			{
				grid[i][j] = new JLabel("", SwingConstants.CENTER);
				grid[i][j].setBounds(j * 96, i * 96, 96, 96);
				grid[i][j].setOpaque(true);
				grid[i][j].setFont(new Font("宋体", 1, 36));
				grid[i][j].setForeground(Color.WHITE);
				grid[i][j].setBackground(Color.WHITE);
				grid[i][j].setBorder(BorderFactory.createLineBorder(Color.RED));
				grid[i][j].setVisible(true);
				this.add(grid[i][j]);
			}
		}
	}
	
	public void setMainFrameUI() {		
		this.setBounds(200, 200, 96 * 4, 96 * 4 + 22);
		this.setLayout(null);
		this.setTitle("My 2048");
		this.setVisible(true);
	}
	
	public boolean isFull() {
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				if(getGridValue(i, j) == 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean isMoveable() {
		
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 3; j++) {
				if(getGridValue(i, j) == getGridValue(i, j + 1)) {
					return true;
				}
			}
		}
		
		for(int j = 0; j < 4; j++) {
			for(int i = 0; i < 3; i++) {
				if(getGridValue(i, j) == getGridValue(i + 1, j)) {
					return true;
				}
			}
		}
		
		return !isFull();
	}
	
	public void failedJudge() {
		
		if(isMoveable() == false) {
			String[] option = {"Retry", "Exit"};
			int result = JOptionPane.showOptionDialog(this, "You failed!", "Game Over", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, option, option[0]);
			switch (result) {
				case 0: this.initGame(); break;
				case 1: System.exit(0); break;
				case JOptionPane.CLOSED_OPTION: System.exit(0); break;
			}
		}
	}
	
	public boolean isZeroGrid(int i, int j) {
		String text = grid[i][j].getText();
		if(text == "") {
			return true;
		}
		return false;
	}
	
	public int getGridValue(int i, int j) {
		String text = grid[i][j].getText();
		if(text == "") {
			return 0;
		}
		
		return Integer.parseInt(text);
	}

	public boolean setGridValue(int i, int j, int value) {
		if(value == 0) {
			grid[i][j].setText("");
		} else {
			grid[i][j].setText(Integer.toString(value));
		}
		
		//switch...log(2, X)
		int n = (int)(Math.log((double)value) / Math.log((double)2));
		switch (n) {
			case 1: grid[i][j].setBackground(Color.GREEN); break;
			case 2: grid[i][j].setBackground(Color.CYAN); break;
			case 3: grid[i][j].setBackground(Color.MAGENTA); break;
			case 4: grid[i][j].setBackground(Color.BLUE); break;
			case 5: grid[i][j].setBackground(Color.DARK_GRAY); break;
			case 6: grid[i][j].setBackground(Color.YELLOW); break;
			case 7: grid[i][j].setBackground(Color.DARK_GRAY); break;
			case 8: grid[i][j].setBackground(Color.PINK); break;
			case 9: grid[i][j].setBackground(Color.ORANGE); break;
			case 10: grid[i][j].setBackground(Color.BLUE); break;
			case 11: grid[i][j].setBackground(Color.RED); return true;
			default: grid[i][j].setBackground(Color.WHITE); break;
		}
		
		return false;
	}
	
	// return the position which is not include the existed part.
	public GridPosition getRandomGridPosition() {
		Random rand = new Random(System.nanoTime());
		int n, i, j;
		do {
			n = rand.nextInt() % 16;
			n = Math.abs(n);
			i = n / 4;
			j = n % 4;
		}while(isZeroGrid(i, j) == false);
		
		return new GridPosition(i, j);
	}
	
	// return number 2 or 4.
	public int getRandomNumber() {
		Random rand = new Random(System.nanoTime());
		if (Math.abs(rand.nextInt()) % 2 == 0) {
			return 2;
		} else {
			return 4;
		}
	}
	
	public void initGame() {
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				setGridValue(i, j, 0);
			}
		}
		
		for(int i = 0; i < 2; i++) {
			performRandomGrid();
		}
	}
	

	private boolean isGridStateChange() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (states[i][j] != getGridValue(i, j)) {
					return true;
				}
			}
		}
		return false;
	}

	private void storeGridState() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				states[i][j] = getGridValue(i, j);
			}
		}
	}

	
	public void performRandomGrid() {
		GridPosition pos = this.getRandomGridPosition();
		this.setGridValue(pos.i, pos.j, this.getRandomNumber());
	}
	
	private void performRight() {
		
		// clean up blank
		for (int n = 0; n < 4; n++) {
			for (int i = 0; i < 4; i++) {
				for (int j = 3; j > 0; j--) {
					if (getGridValue(i, j) == 0) {
						setGridValue(i, j, getGridValue(i, j - 1));
						setGridValue(i, j - 1, 0);
						continue;
					}
				}
			}
		}
		
		for (int i = 0; i < 4; i++) {
			for (int j = 3; j > 0; j--) {
				if (getGridValue(i, j - 1) == getGridValue(i, j) ) {
					setGridValue(i, j, getGridValue(i, j) * 2);
					setGridValue(i, j - 1, 0);
				}
			}
		}
		
		// clean up blank
		for (int n = 0; n < 4; n++) {
			for (int i = 0; i < 4; i++) {
				for (int j = 3; j > 0; j--) {
					if (getGridValue(i, j) == 0) {
						setGridValue(i, j, getGridValue(i, j - 1));
						setGridValue(i, j - 1, 0);
						continue;
					}
				}
			}
		}
	}

	private void performLeft() {
		
		// clean up blank
		for (int n = 0; n < 4; n++) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 3; j++) {
					if (getGridValue(i, j) == 0) {
						setGridValue(i, j, getGridValue(i, j + 1));
						setGridValue(i, j + 1, 0);
						continue;
					}
				}
			}
		}
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				if (getGridValue(i, j + 1) == getGridValue(i, j) ) {
					setGridValue(i, j, getGridValue(i, j) * 2);
					setGridValue(i, j + 1, 0);
				}
			}
		}
		
		// clean up blank
		for (int n = 0; n < 4; n++) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 3; j++) {
					if (getGridValue(i, j) == 0) {
						setGridValue(i, j, getGridValue(i, j + 1));
						setGridValue(i, j + 1, 0);
						continue;
					}
				}
			}
		}
	}

	private void performDown() {
		
		// clean up blank
		for (int n = 0; n < 4; n++) {
			for (int j = 0; j < 4; j++) {
				for (int i = 3; i > 0; i--) {
					if (getGridValue(i, j) == 0) {
						setGridValue(i, j, getGridValue(i - 1, j));
						setGridValue(i - 1, j, 0);
						continue;
					}
				}
			}
		}
		
		for (int j = 0; j < 4; j++) {
			for (int i = 3; i > 0; i--) {
				if (getGridValue(i - 1, j) == getGridValue(i, j) ) {
					setGridValue(i, j, getGridValue(i, j) * 2);
					setGridValue(i - 1, j, 0);
				}
			}
		}
		
		// clean up blank
		for (int n = 0; n < 4; n++) {
			for (int j = 0; j < 4; j++) {
				for (int i = 3; i > 0; i--) {
					if (getGridValue(i, j) == 0) {
						setGridValue(i, j, getGridValue(i - 1, j));
						setGridValue(i - 1, j, 0);
						continue;
					}
				}
			}
		}
	}

	private void performUp() {
		
		// clean up blank
		for (int n = 0; n < 4; n++) {
			for (int j = 0; j < 4; j++) {
				for (int i = 0; i < 3; i++) {
					if (getGridValue(i, j) == 0) {
						setGridValue(i, j, getGridValue(i + 1, j));
						setGridValue(i + 1, j, 0);
						continue;
					}
				}
			}
		}
		
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < 3; i++) {
				if (getGridValue(i + 1, j) == getGridValue(i, j) ) {
					setGridValue(i, j, getGridValue(i, j) * 2);
					setGridValue(i + 1, j, 0);
				}
			}
		}
		
		// clean up blank
		for (int n = 0; n < 4; n++) {
			for (int j = 0; j < 4; j++) {
				for (int i = 0; i < 3; i++) {
					if (getGridValue(i, j) == 0) {
						setGridValue(i, j, getGridValue(i + 1, j));
						setGridValue(i + 1, j, 0);
						continue;
					}
				}
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		int keycode = arg0.getKeyCode();
		switch(keycode) {
			case KeyEvent.VK_UP:
				storeGridState();
				performUp();
				if (isGridStateChange()) {
					performRandomGrid();
				}
				break;
				
			case KeyEvent.VK_DOWN:
				storeGridState();
				performDown();
				if (isGridStateChange()) {
					performRandomGrid();
				}
				break;
				
			case KeyEvent.VK_LEFT:
				storeGridState();
				performLeft();
				if (isGridStateChange()) {
					performRandomGrid();
				}
				break;
				
			case KeyEvent.VK_RIGHT:
				storeGridState();
				performRight();
				if (isGridStateChange()) {
					performRandomGrid();
				}
				break;
				
			default:
				break;
		}
		
		failedJudge();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}
}
