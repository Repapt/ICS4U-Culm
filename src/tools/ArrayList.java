package tools;

public class ArrayList {
	private Node root;
	private int size;
	
	
	public ArrayList() {
		
	}
	
	public void add(Object value) {
		Node checker = root;
		if(checker == null) {
			root = new Node(value, null);
		} else {
			for(int i=0;i<size;i++) {
				if(checker.pointer == null) {
					break;
				} else {
					checker = checker.pointer;
				}
			}
			checker.pointer = new Node(value, null);
			size ++;
		}
	}
	
	
	private static class Node { //working
		public Object value;
		public Node pointer;
		public Node(Object value, Node pointer) {
			this.value = value;
			this.pointer = pointer;
		}
	}
}
