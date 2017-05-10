import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class NodeArray<U> implements Iterable<NodeArray.Node<U>> {

	private Node<U> rootElement;
	private Stack<Integer> path;
	private boolean iteratorStarted;
	
	static class Node<V> {
		private Node<V> parent;
		private Node<V> next;
		private Node<V> previous;
		private List<Node<V>> children;
		private V data;
		private int level;
		private int position;
		
		Node(V data)
		{
			this.parent = null;
			this.next = null;
			this.previous = null;
			this.children = new ArrayList<>();
			this.data = data;
			this.level = 0;
			this.position = 0;
		}
		
		public Node<V> addChild(Node<V> node)
		{
			if (this.children.size() == 0)
			{
				this.children.add(this.children.size(), node);
				node.parent = this;
				node.level = node.parent.level+1;
			}
			else
			{
				this.children.add(this.children.size(), node);
				node.parent = this;
				node.level = node.parent.level+1;
				node.previous = this.children.get(this.children.size()-2);
				node.position = node.previous.position+1;
				this.children.get(this.children.size()-2).next = node;
			}
			return node;
		}
		
		public void addChildren(Collection<? extends Node<V>> e)
		{
			for (Node<V> p : e)
				addChild(p);
		}
		
		public String toString()
		{
			return data.toString();
		}
		
		public int getLevel()
		{
			return level;
		}
		
		public Node<V> getParent()
		{
			return parent;
		}
		
		public Node<V> getNext()
		{
			return next;
		}
		
		public Node<V> getPrevious()
		{
			return previous;
		}
		
		public Node<V> getChild(int i)
		{
			Node<V> node = null;
			try
			{
				node =  children.get(i);
			}
			catch (Exception e)
			{
				throw e;
			}
			return node;
		}
		
		public V getData()
		{
			return data;
		}
		
	}
	
	NodeArray()
	{
		rootElement = new Node<U>(null);
		iteratorStarted = false;
	}
	
	public Node<U> addChild(Node<U> node)
	{
		rootElement.addChild(node);
		return node;
	}
	
	public void addChildren(Collection<? extends Node<U>> e)
	{
		rootElement.addChildren(e);
	}
	
	public Node<U> getChild(int i)
	{
		return rootElement.getChild(i);
	}
	
	public Iterator<Node<U>> iterator()
	{
		return new Iterator<Node<U>>(){
			
			Node<U> currentElement;
			
			private void initialConfig()
			{
				iteratorStarted = true;
				currentElement = rootElement;
				path = new Stack<>();
			}

			@Override
			public boolean hasNext() {
				if (!iteratorStarted)
					initialConfig();
				
				@SuppressWarnings("unchecked")
				Stack<Integer> lc_path = (Stack<Integer>) path.clone();
				Node<U> lc_currentElement = currentElement;
				if (lc_currentElement.children.size() > 0)
					return true;
				else if (lc_currentElement.next != null)
					return true;
				else if (!lc_path.isEmpty())
				{
					while (!lc_path.isEmpty())
					{
						if (lc_currentElement.parent.next != null)
							return true;
						else
						{
							lc_currentElement = lc_currentElement.parent;
							lc_path.pop();
						}
					}
					return false;
				}
				else
					return false;
			}

			@Override
			public Node<U> next() {
				if (!iteratorStarted)
					initialConfig();
				if (currentElement.children.size() > 0)
				{
					path.push(0);
					currentElement = currentElement.children.get(0);
				}
				else if (currentElement.next != null)
				{
					currentElement = currentElement.next;
				}
				else if (!path.isEmpty())
				{
					while (!path.isEmpty())
					{
						if (currentElement.parent.next != null)
						{
							currentElement = currentElement.parent.next;
							path.pop();
							break;
						}
						else
						{
							currentElement = currentElement.parent;
							path.pop();
						}
					}
				}
				else
				{
					currentElement = null;
				}
				return currentElement;
			}
		};
	}
}

