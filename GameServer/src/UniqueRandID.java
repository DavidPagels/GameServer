import java.util.ArrayList;
import java.util.Random;


public class UniqueRandID {
	private ArrayList<Integer> IDs;

	public UniqueRandID(){
		IDs = new ArrayList<Integer>();
	}

	public int generateID(){
		int retID = new Random().nextInt();
		boolean unique;
		do{
			unique = true;
			for(Integer a: IDs){
				if(retID == a){
					retID = new Random().nextInt();
					unique = false;
					break;
				}
			}
		}while(unique == false);
		
		IDs.add(retID);
		return retID;
	}
}
