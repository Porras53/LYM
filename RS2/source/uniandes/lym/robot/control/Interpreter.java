package uniandes.lym.robot.control;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import uniandes.lym.robot.kernel.*;



/**
 * Receives commands and relays them to the Robot. 
 */

public class Interpreter   {
	
	/**
	 * Robot's world
	 */
	private RobotWorldDec world;   
	
	private int cont=0;
	
	private String[] variables;
	private int[] valores;
	
	
	public Interpreter()
	  {
	  }


    /**
	 * Creates a new interpreter for a given world
	 * @param world 
	 */


	public Interpreter(RobotWorld mundo)
      {
		this.world =  (RobotWorldDec) mundo;
		
	  }
	
	
	/**
	 * sets a the world
	 * @param world 
	 */

	public void setWorld(RobotWorld m) 
	{
		world = (RobotWorldDec) m;
		
	}
	  
	
	
	/**
	 *  Processes a sequence of commands. A command is a letter  followed by a ";"
	 *  The command can be:
	 *  M:  moves forward
	 *  R:  turns right
	 *  
	 * @param input Contiene una cadena de texto enviada para ser interpretada
	 */
	
	public String process(String input) throws Error
     {   
		
		 
		StringBuffer output=new StringBuffer("SYSTEM RESPONSE: -->\n");	
		
	  
	    try	    {
	    	String[] str=input.split(" ");
	    	
	    	
	    	switch (str[0]) {
	    	
	    		case "ROBOT_R":
	    			if(cont==0)
	    			{
	    				output.append("Routine has been incializated... \n");
	    				output.append("Waiting declaration of variables...  \n");
	    				cont++;
	    			}
	    			else
	    			{
	    				output.append("Expected another command...  \n");
	    			}
	    			break;
	    			
	    		case "VARS": 
	    			if(cont==1)
	    			{  
	    				if(str[1].contains(","))
	    				{
	    					String[] var=str[1].split(",");
	    					variables=var;
	    					valores=new int[variables.length];
	    					for(int i=0;i<valores.length-1;i++)
	    					{
	    					valores[i]=0;
	    					}
	    					 output.append("The variables has been inicializated... \n");
	    					
	    				
	    				}
	    				else if(!str[1].contains(","))
	    				{
	    					variables= new String[1];
	    					variables[0]= str[1];
	    					valores= new int[variables.length];
	    					valores[0]=0;
	    					 output.append("The variable has been inicializated... \n");
	    					 
	    				}
	    				 cont++;
	    				
	    				
	    			}
	    			else if(str.length<2)
	    				{
	    					output.append("Miss variables 'VARS a,b,c,d...' \n");
	    				}
	    			else
	    			{
	    				output.append("Expected another command...  \n");
	    			}
	    			break;
	    			
	    		case "BEGIN":
	    			if(cont==2)
	    			{
	    				
	    				output.append("Waiting declaration of commands... \n");
	    				cont++;
	    			}
	    			else
	    			{
	    				output.append("Expected another command...  \n");
	    			}
	    			break;
	    		
	    		case "END":
	    			if(cont>=2)
	    			{
	    				output.append("The routine is finished, write again the structure... \n");
	    				cont=0;
	    			}
	    			else
	    			{
	    				output.append("Expected another command...  \n");
	    			}
	    			break;
	    		
	    		case "assign:":
	    			if(cont>=2)
	    			{
	    				if(str[2].equals("to:"))
	    				{
	    					
	    					try
	    					{
	    					int num= Integer.parseInt(str[1]);
	    					int pos= darPosVariables(str[3]);
	    					valores[pos]=num;
	    					output.append("Se asignó el valor "+ num+" a la variable "+ str[3]+" \n");
	    					
	    					}
	    					catch(Exception e)
	    					{
	    						output.append("Something is wrong with the command, the command expected (Example) = assign: 3 to: 'variable declarated'...  \n");
	    					}
	    				}
	    			}
	    			else
	    			{
	    				output.append("Expected another command...  \n");
	    			}
	    		break;
	    		
	    		case "move:":
	    		if(cont>=2){
	    			if(str.length==2 )
	    			{
	    				int num=0;
	    				try{
	    				num= Integer.parseInt(str[1]);
	    				}
	    				catch(Exception e)
	    				{
	    				int pos=darPosVariables(str[1]);
	    				num=valores[pos];
	    				}
	    				world.moveForward(num);
	    				output.append("move aplicated... \n");
	    			}
	    			
	    			else if(str.length==4)
	    			{
	    				int num=0;
	    				try{
		    				num= Integer.parseInt(str[1]);
		    				}
		    				catch(Exception e)
		    				{
		    				int pos=darPosVariables(str[1]);
		    				num=valores[pos];
		    				}
	    				if(str[2].equals("toThe:"))
	    				{
	    					if(str[3].equals("front"))
	    					{
	    						world.moveVertically(-num);
	    					}
	    					else if(str[3].equals("right"))
	    					{
	    						world.moveHorizontally(num);
	    					}
	    					else if(str[3].equals("left"))
	    					{
	    						world.moveHorizontally(-num);
	    					}
	    					else if(str[3].equals("back"))
	    					{
	    						world.moveVertically(num);
	    					}
	    					else
	    					{
	    						output.append("Expected values front, right, left or back...  \n");
	    					}
	    				}
	    				else if(str[2].equals("inDir:"))
	    				{
	    					if(str[3].equals("north"))
	    					{
	    						while(!(world.getOrientacion()==0)){
	    						world.turnRight();
	    						}
	    						world.moveForward(num);
	    					}
	    					else if(str[3].equals("south"))
	    					{
	    						while(!(world.getOrientacion()==1)){
		    						world.turnRight();
		    						}
	    						world.moveForward(num);
	    					}
	    					else if(str[3].equals("east"))
	    					{
	    						while(!(world.getOrientacion()==2)){
		    						world.turnRight();
		    						}
	    						world.moveForward(num);
	    					}
	    					else if(str[3].equals("west"))
	    					{
	    						while(!(world.getOrientacion()==3)){
		    						world.turnRight();
		    						}
	    						world.moveForward(num);
	    					}
	    					else
	    					{
	    						output.append("Expected values like west, east,south or north...  \n");
	    					}
	    					
	    				}
	    				else
	    				{
	    					output.append("Expected toThe: or inDir: ...  \n");
	    				}
	    			}
	    			else
	    			{
	    				output.append("Expected move: 'number or variable' or move: 'number or variable' toThe: 'Direction' or inDir: 'Direction'... \n");
	    			}
	    			}
	    		else
	    		{
	    			output.append("Expected another command...  \n");
	    		}
	    			break;
	    			
	    		case "turn:":
	    			if(cont>=2)
	    			{
	    				if(str[1].equals("right"))
	    				{
	    					world.turnRight();
	    					output.append("Turns right \n");
	    				}
	    				else if (str[1].equals("left"))
	    				{
	    					if(world.getOrientacion()==0)
	    					{
	    						while(!(world.getOrientacion()==3)){
	    						world.turnRight();
	    						}
	    					}
	    					else if(world.getOrientacion()==1)
	    					{
	    						while(!(world.getOrientacion()==2)){
		    						world.turnRight();
		    						}
	    					}
	    					else if(world.getOrientacion()==2)
	    					{
	    						while(!(world.getOrientacion()==0)){
		    						world.turnRight();
		    						}
	    					}
	    					else if(world.getOrientacion()==3)
	    					{
	    						while(!(world.getOrientacion()==1)){
		    						world.turnRight();
		    						}
	    					}
	    					output.append("Turns left \n");
	    				}
	    				else if(str[1].equals("around"))
	    				{
	    					
	    				}
	    				else
	    				{
	    					output.append("Expected values like left, right or around...  \n");
	    				}
	    			}
	    			else
	    			{
	    				output.append("Expected another command...  \n");
	    			}
	    			
	    		case "face:":
	    			
	    			if(cont>=2)
	    			{
	    				if(str[1].equals("north"))
    					{
    						while(!(world.getOrientacion()==0)){
    						world.turnRight();
    						}
    					}
    					else if(str[1].equals("south"))
    					{
    						while(!(world.getOrientacion()==1)){
	    						world.turnRight();
	    						}
    					}
    					else if(str[1].equals("east"))
    					{
    						while(!(world.getOrientacion()==2)){
	    						world.turnRight();
	    						}
    					}
    					else if(str[1].equals("west"))
    					{
    						while(!(world.getOrientacion()==3)){
	    						world.turnRight();
	    						}
    					}
    					else
    					{
    						output.append("Expected values like north, south, east or west...  \n");
    					}
	    			}
	    			else
	    			{
	    				output.append("Expected another command...  \n");
	    			}
	    			
	    			break;
	    			
	    		case "put:":
	    			if(cont>=2)
	    			{
	    				if(str[2].equals("of:"))
	    				{
	    					int num=0;
		    				try{
			    				num= Integer.parseInt(str[1]);
			    				}
			    				catch(Exception e)
			    				{
			    				int pos=darPosVariables(str[1]);
			    				num=valores[pos];
			    				}
		    				if(str[3].equals("Balloons"))
		    				{
		    					world.putBalloons(num);
		    					output.append(" Put "+num+" Balloons \n");
		    				}
		    				else if(str[3].equals("Chips"))
		    				{
		    					world.putChips(num);
		    					output.append(" Put "+num+" chips \n");

		    				}
	    				}
	    				else
	    				{
	    					output.append("Expected put: 'value' of: 'Object' ... \n");
	    				}
    				}
	    			else
	    			{
	    				output.append("Expected another command...  \n");
	    			}
	    			break;
	    			
	    		case "pick:":
	    			if(cont>=2)
	    			{
	    				if(str[2].equals("of:"))
	    				{
	    					int num=0;
		    				try{
			    				num= Integer.parseInt(str[1]);
			    				}
			    				catch(Exception e)
			    				{
			    				int pos=darPosVariables(str[1]);
			    				num=valores[pos];
			    				}
		    				if(str[3].equals("Balloons"))
		    				{
		    					world.grabBalloons(num);
		    					output.append(" Grab "+num+" Balloons \n");
		    				}
		    				else if(str[3].equals("Chips"))
		    				{
		    					world.pickChips(num);
		    					output.append(" Grab "+num+" chips \n");

		    				}
		    				
		    				else
		    				{
		    					output.append("Expected values like Balloons or Chips...  \n");
		    				}
		    				
	    					
	    					
	    				}
	    				else
	    				{
	    					output.append("Expected pick: 'value' of: 'value'...  \n");
	    				}
    				}
	    			else
	    			{
	    				output.append("Expected another command...  \n");
	    			}
	    			break;
	    			
	    		
	    			
	    			
	    			
	    		default: output.append(" Unrecognized command:  \n"); 
	    		
	          }
	
	    	
	    	
	    			 try {
	    			        Thread.sleep(1000);
	    			    } catch (InterruptedException e) {
	    			        System.err.format("IOException: %s%n", e);
	    			    }
	    			 
	    		 
	    		
	    	 
	    	 
	    
	    
	    
	    
	    }
	    
	 catch (Error e ){
	 output.append("Error!!!  "+e.getMessage());
	 
 }
	    return output.toString();
	    }
	
	public int darPosVariables(String var)
	{
		int posicion=-1;
		for(int i=0;i<variables.length-1;i++)
		{
			String actual= variables[i];
			if(actual.compareTo(var)==0)
			{
				posicion=i;
			}
		}
		return posicion;
	}


	
}
	    