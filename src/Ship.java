import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Ship extends JButton {
	/* PROPERTIES */
	private String name;
	private int length;
	
	/* DEFAULT OPTIONS */
	public boolean pressed;
	int counter = 1;
	
	/* CONSTRUCTOR */
	public Ship(String name, int length){
		super(name + " - " +length);
		this.name = name;
		this.length = length;
		pressed = false;
		
		this.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				counter++;
				if(counter % 2 == 0){
					pressed = true;
					setEnabled(false);
					Game.textarea.append("Please place your " + Ship.this.name + " to the field" + " \n");
				}
				else{
					pressed = false;
				}
			}
			
		});
	}
	
	/* METHODS / FUNCTIONALITY */
	/*--------------------------------------------------------*/
	
	public boolean isPressed(){
		return pressed;
	}
	
	/*--------------------------------------------------------*/
	
	public void setPressed(boolean state){
		pressed = state;
	}
	
	/*--------------------------------------------------------*/
	
	public String getName(){
		return name;
	}
	
	/*--------------------------------------------------------*/
	
	public int getLength(){
		return length;
	}
	
	/*--------------------------------------------------------*/
	
	@Override
	public String toString(){
		return name;
	}
	
	/*--------------------------------------------------------*/
}