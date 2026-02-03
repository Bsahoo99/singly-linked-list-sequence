// This is an assignment for students to complete after reading Chapter 4 of
// "Data Structures and Other Objects Using Java" by Michael Main.

package edu.uwm.cs351;

import java.util.function.Consumer;


/******************************************************************************
 * This class is a homework assignment;
 * A Sequence is an aggregate class with a cursor (not an iterator)
 * The sequence can have a special "current element," which is specified and 
 * accessed through four methods
 * (start, getCurrent, advance and hasCurrent).
 *
 ******************************************************************************/
public class LinkedSequence<E> implements Cloneable
{
	private static Consumer<String> reporter = (s) -> System.out.println("Invariant error: "+ s);
	
	/**
	 * Used to report an error found when checking the invariant.
	 * By providing a string, this will help debugging the class if the invariant should fail.
	 * @param error string to print to report the exact error found
	 * @return false always
	 */
	private boolean report(String error) {
		reporter.accept(error);
		return false;
	}

	// TODO: Declare the private static generic Node class with fields data and next.
	// The class should be private, static and generic.
	// Please use a different name for its generic type parameter.
	// It should have a constructor or two (at least the default constructor) but no methods.
	// The no-argument constructor can construct a dummy node if you would like.
	// The fields of Node should have "default" access (neither public, nor private)

	private static class Node<T> {
		T data;
		Node<T> next;
		Node() {}
		Node(T data, Node<T> next) {
			this.data = data;
			this.next = next;
		}
	}

	// TODO: Declare the private fields of Sequences:
	// One for the tail, one for the size and one for the precursor.
	// Do not declare any other fields.
	// In particular do *NOT* declare a "dummy" field.  The dummy should be a model field.

	private Node<E> tail;
	private Node<E> precursor;
	private int size;
	
	/// Model fields:
	// These are the conceptual fields that are computed from the concrete fields.
	// The getters are private: not for clients.  Don't assert invariant.
	// They are allowed to simply crash if the data structure is inconsistent.

	/** Return the head node from the data structure since we do not have a head field. */
	private Node<E> getHead() {
		return getDummy().next;
	}

	/** Return the dummy node from the data structure since we do not have a dummy field. */
	private Node<E> getDummy() {
		return tail.next;
	}

	/** Return the cursor from the data structure since we do not have a cursor field. */
	private Node<E> getCursor() {
		return precursor.next;
	}
	
	
	/**
	 * Check the invariant.  Report any problem precisely once.
	 * Return false if any problem is found.  Returning an informative
	 * {@link #report(String)} will make it easier to debug invariant problems.
	 * @return whether invariant is currently true
	 */
	private boolean wellFormed() {
		// Invariant:
		// 1. tail node is not null, and the dummy (next after tail) should not be null either.
		// 2. The dummy node's data should be itself.
		// 3. list must be in the form of a cycle from tail back to tail
		// 4. size is number of nodes in list, other than the dummy
		// 5. precursor points to a node in the list (possibly the dummy).
		
		// Implementation:
		// Do multiple checks: each time returning false if a problem is found.
		// We recommend Floyd's tortoise and hare algorithm for detecting cycles.
		// (You need to modify it so that the hare doesn't go past the tail.)
		
		// 1. tail is not null and dummy (tail.next) is not null
		if (tail == null) return report("tail is null");
		if (tail.next == null) return report("dummy (tail.next) is null");
		Node<E> dummy = tail.next;
		// 2. dummy's data should be itself (self-referencing)
		if (dummy.data != (Object)dummy) return report("dummy data is not itself");
		// 3. list must be cyclic and well-formed:
		//    Walk from dummy, counting nodes until we return to dummy.
		//    The walk must eventually come back to dummy (through tail).
		//    Tail must be the last node before dummy.
		int count = 0;
		Node<E> p = dummy.next;
		Node<E> last = dummy;
		// Walk with a limit to avoid infinite loops on bad data
		while (p != dummy && count <= size + 1) {
			if (p == null) return report("null node encountered in list");
			last = p;
			p = p.next;
			count++;
		}
		if (p == null) return report("null encountered before closing cycle");
		if (p != dummy) return report("cycle does not close back to dummy within expected size");
		if (last != tail) return report("tail does not point to the last node before dummy");
		// 4. size is number of nodes other than dummy
		if (count != size) return report("size is incorrect: expected " + count + " but was " + size);
		// 5. precursor points to a node in the list (possibly dummy)
		boolean foundPrecursor = (precursor == dummy);
		p = dummy.next;
		while (p != dummy && !foundPrecursor) {
			if (p == precursor) foundPrecursor = true;
			p = p.next;
		}
		if (!foundPrecursor) return report("precursor not in the list");
		return true;
	}

	private LinkedSequence(boolean doNotUse) {} // only for purposes of testing, do not change
	
	/**
	 * Create an empty sequence.
	 * @param - none
	 * @postcondition
	 *   This sequence is empty 
	 **/   
	@SuppressWarnings("unchecked")
	public LinkedSequence( )
	{
		// Create a dummy node: data = itself, next = itself (circular self-loop)
		Node<E> dummy = new Node<E>();
		dummy.data = (E)(Object)dummy;
		dummy.next = dummy;
		tail = dummy;
		precursor = dummy;
		size = 0;
		assert wellFormed() : "invariant failed in constructor";
	}

	/**
	 * Determine the number of elements in this sequence.
	 * @param - none
	 * @return
	 *   the number of elements in this sequence
	 **/ 
	public int size( )
	{
		assert wellFormed() : "invariant wrong at start of size()";
		return size;
	}

	/**
	 * Set the current element at the front of this sequence.
	 * @postcondition
	 *   The front element of this sequence is now the current element
	 *   (but if this sequence has no elements at all, then there is no current element).
	 */
	public void start() {
		assert wellFormed() : "invariant wrong at start of start()";
		precursor = getDummy();
		assert wellFormed() : "invariant wrong at end of start()";
	}

	/**
	 * Accessor method to determine whether this sequence has a current element.
	 * @return true if there is a current element, false otherwise
	 */
	public boolean isCurrent() {
		assert wellFormed() : "invariant wrong at start of isCurrent()";
		return getCursor() != getDummy();
	}

	/**
	 * Accessor method to get the current element of this sequence.
	 * @precondition isCurrent() returns true.
	 * @return the current element of this sequence
	 * @exception IllegalStateException if there is no current element
	 */
	public E getCurrent() {
		assert wellFormed() : "invariant wrong at start of getCurrent()";
		if (!isCurrent()) throw new IllegalStateException("There is no current element");
		return getCursor().data;
	}

	/**
	 * Move forward, so that the current element is now the next element in this sequence.
	 * @precondition isCurrent() returns true.
	 * @postcondition If the current element was already the end element, then there is
	 *   no longer any current element. Otherwise, the new element is the element
	 *   immediately after the original current element.
	 * @exception IllegalStateException if there is no current element
	 */
	public void advance() {
		assert wellFormed() : "invariant wrong at start of advance()";
		if (!isCurrent()) throw new IllegalStateException("There is no current element");
		precursor = getCursor();
		assert wellFormed() : "invariant wrong at end of advance()";
	}

	/**
	 * Remove the current element from this sequence.
	 * @precondition isCurrent() returns true.
	 * @postcondition The current element has been removed and the following element
	 *   (if any) is now the new current element. If there was no following element,
	 *   then there is now no current element.
	 * @exception IllegalStateException if there is no current element
	 */
	public void removeCurrent() {
		assert wellFormed() : "invariant wrong at start of removeCurrent()";
		if (!isCurrent()) throw new IllegalStateException("There is no current element");
		Node<E> cursor = getCursor();
		precursor.next = cursor.next;
		if (cursor == tail) {
			tail = precursor;
		}
		size--;
		assert wellFormed() : "invariant wrong at end of removeCurrent()";
	}

	/**
	 * Add a new element to this sequence, before the current element (if any).
	 * @param element the new element being added
	 * @postcondition A new copy of the element has been added. If there was a current
	 *   element, the new element is placed before it. If there was no current element,
	 *   the new element is placed at the end. The new element becomes the current element.
	 */
	public void insert(E element) {
		assert wellFormed() : "invariant wrong at start of insert()";
		Node<E> newNode = new Node<E>(element, precursor.next);
		precursor.next = newNode;
		if (precursor == tail) {
			tail = newNode;
		}
		size++;
		assert wellFormed() : "invariant wrong at end of insert()";
	}

	/**
	 * Place the contents of another sequence into this sequence before the current element (if any).
	 * @param addend a sequence whose contents will be placed into this sequence, must not be null
	 * @postcondition The elements from addend have been placed into this sequence.
	 *   The current element of this sequence (if any) is unchanged. The addend is unchanged.
	 * @exception NullPointerException if addend is null
	 */
	public void insertAll(LinkedSequence<E> addend) {
		assert wellFormed() : "invariant wrong at start of insertAll()";
		if (addend == null) throw new NullPointerException("addend is null");
		if (addend.size == 0) {
			assert wellFormed() : "invariant wrong at end of insertAll()";
			return;
		}
		LinkedSequence<E> copy = addend.clone();
		// copy's list: copy.tail -> copy.dummy -> copy.head -> ... -> copy.tail
		// We want to splice copy.head ... copy.tail between precursor and precursor.next
		Node<E> copyDummy = copy.getDummy();
		Node<E> copyHead = copyDummy.next;
		Node<E> copyTail = copy.tail;
		// Splice: precursor -> copyHead -> ... -> copyTail -> (old precursor.next)
		Node<E> oldNext = precursor.next;
		precursor.next = copyHead;
		copyTail.next = oldNext;
		if (precursor == tail) {
			tail = copyTail;
		}
		precursor = copyTail;
		size += copy.size;
		assert wellFormed() : "invariant wrong at end of insertAll()";
	}

	/**
	 * Generate a copy of this sequence.
	 * @param - none
	 * @return
	 *   The return value is a copy of this sequence. Subsequent changes to the
	 *   copy will not affect the original, nor vice versa.
	 *   Whatever was current in the original object is now current in the clone.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for creating the clone.
	 **/ 
	@SuppressWarnings("unchecked")
	public LinkedSequence<E> clone( )
	{  	 
		assert wellFormed() : "invariant wrong at start of clone()";

		LinkedSequence<E> result;

		try
		{
			result = (LinkedSequence<E>) super.clone( );
		}
		catch (CloneNotSupportedException e)
		{  
			// This exception should not occur. But if it does, it would probably
			// indicate a programming error that made super.clone unavailable.
			// The most common error would be forgetting the "Implements Cloneable"
			// clause at the start of this class.
			throw new RuntimeException
			("This class does not implement Cloneable");
		}

		// Deep copy the circular linked list
		// Create a new dummy for the result
		@SuppressWarnings("unchecked")
		Node<E> newDummy = new Node<E>();
		newDummy.data = (E)(Object)newDummy;
		Node<E> oldDummy = getDummy();
		Node<E> oldCur = oldDummy;
		Node<E> newCur = newDummy;
		// Iterate through the original list copying nodes
		while (oldCur.next != oldDummy) {
			Node<E> oldNext = oldCur.next;
			Node<E> newNext = new Node<E>(oldNext.data, null);
			newCur.next = newNext;
			if (oldNext == tail) result.tail = newNext;
			if (oldNext == precursor) result.precursor = newNext;
			newCur = newNext;
			oldCur = oldNext;
		}
		// Close the cycle: last new node points back to newDummy
		newCur.next = newDummy;
		// If the sequence was empty, tail is the dummy and precursor is the dummy
		if (size == 0) {
			result.tail = newDummy;
			result.precursor = newDummy;
		} else if (precursor == oldDummy) {
			result.precursor = newDummy;
		}
		assert wellFormed() : "invariant wrong at end of clone()";
		assert result.wellFormed() : "invariant wrong for result of clone()";
		return result;
	}

	
	/**
	 * Class to assist internal testing of the data structure.
	 * Do not change this class!
	 * @param T the element type to use
	 */
	public static class Spy<T> {
		/**
		 * A public version of the data structure's internal node class.
		 * This class is only used for testing.
		 */
		public static class Node<U> extends LinkedSequence.Node<U> 
{
			/**
			 * Create a node with self data and next fields.
			 */
			@SuppressWarnings("unchecked")
			public Node() {
				this(null, null);
				this.data = (U)this;
				this.next = this;
			}
			/**
			 * Create a node with the given values
			 * @param d data for new node, may be null
			 * @param n next for new node, may be null
			 */
			public Node(U d, Node<U> n) {
				super();
				this.data = d;
				this.next = n;
			}
		}
		
		/**
		 * Create a node for testing.
		 * @param d data for new node, may be null
		 * @param n next for new node, may be null
		 * @return newly created test node
		 */
		public Node<T> newNode(T d, Node<T> n) {
			return new Node<T>(d, n);
		}
		
		/**
		 * Create a node with a self data and next fields for testing.
		 * @return newly created test node
		 */
		public Node<T> newNode() {
			return new Node<T>();
		}
		
		/**
		 * Change a node's data field
		 * @param n1 node to change, must not be null
		 * @param x value to set data field to
		 */
		@SuppressWarnings("unchecked")
		public void setData(Node<T> n1, Object x) {
			n1.data = (T)x;
		}
		
		/**
		 * Change a node's next field
		 * @param n1 node to change, must not be null
		 * @param n2 node to point to, may be null
		 */
		public void setNext(Node<T> n1, Node<T> n2) {
			n1.next = n2;
		}
		
		/**
		 * Return the sink for invariant error messages
		 * @return current reporter
		 */
		public Consumer<String> getReporter() {
			return reporter;
		}

		/**
		 * Change the sink for invariant error messages.
		 * @param r where to send invariant error messages.
		 */
		public void setReporter(Consumer<String> r) {
			reporter = r;
		}

		/**
		 * Create a testing instance of the ADT with the given
		 * data structure.
		 * @param t the tail node
		 * @param p the precursor
		 * @param s the size
		 * @return a new testing linked sequence with this data structure.
		 */
		public LinkedSequence<T> newInstance(Node<T> t, Node<T> p, int s) {
			LinkedSequence<T> result = new LinkedSequence<T>(false);
			result.tail = t;
			result.precursor = p;
			result.size = s;
			return result;
		}
			
		/**
		 * Check the invariant on the given dynamic array robot.
		 * @param r robot to check, must not be null
		 * @return whether the invariant is computed as true
		 */
		public boolean wellFormed(LinkedSequence<?> r) {
			return r.wellFormed();
		}
		
		/** 
		 * Return the head of the testing data structure.
		 */
		public Object getHead(LinkedSequence<?> r) {
			return r.getHead();
		}
		
		/** 
		 * Return the head of the testing data structure.
		 */
		public Object getDummy(LinkedSequence<?> r) {
			return r.getDummy();
		}
		
		/** 
		 * Return the head of the testing data structure.
		 */
		public Object getCursor(LinkedSequence<?> r) {
			return r.getCursor();
		}
	}
}

