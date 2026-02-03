import java.util.function.Consumer;
import java.util.function.Supplier;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.LinkedSequence;


public class TestInternals extends LockedTestCase {

	protected LinkedSequence.Spy<String> spy;
	
	protected LinkedSequence<String> self;
	
	protected LinkedSequence.Spy.Node<String> d0, d1, d2;
	protected LinkedSequence.Spy.Node<String> n0, n1, n2, n3, n4, n5;
	protected LinkedSequence.Spy.Node<String> n0a, n1a, n2a, n3a, n4a, n5a;
	
	protected int reports = 0;
	
	protected void assertReporting(boolean expected, Supplier<Boolean> test) {
		reports = 0;
		Consumer<String> savedReporter = spy.getReporter();
		try {
			spy.setReporter((String message) -> {
				++reports;
				if (message == null || message.trim().isEmpty()) {
					assertFalse("Uninformative report is not acceptable", true);
				}
				if (expected) {
					assertFalse("Reported error incorrectly: " + message, true);
				}
			});
			assertEquals(expected, test.get().booleanValue());
			if (!expected) {
				assertEquals("Expected exactly one invariant error to be reported", 1, reports);
			}
			spy.setReporter(null);
		} finally {
			spy.setReporter(savedReporter);
		}
	}
	
	protected void assertWellFormed(boolean expected, LinkedSequence<?> r) {
		assertReporting(expected, () -> spy.wellFormed(r));
	}

	@Override
	protected void setUp() {
		spy = new LinkedSequence.Spy<>();
		d0 = spy.newNode();
		d1 = spy.newNode();
		d2 = spy.newNode();
		n0 = spy.newNode(null, null);
		n1 = spy.newNode("one", null);
		n2 = spy.newNode("two", null);
		n3 = spy.newNode("three", null);
		n4 = spy.newNode("four", null);
		n5 = spy.newNode("five", null);
		n0a = spy.newNode(null, null);
		n1a = spy.newNode("one", null);
		n2a = spy.newNode("two", null);
		n3a = spy.newNode("three", null);
		n4a = spy.newNode("four", null);
		n5a = spy.newNode("five", null);
	}

	
	/// Locked tests:
	
	private void ignore(String ignored) {}
	private void ignore(int val) {}
	
	public void testL0() {
		// Assume the Node class has "data" and "next" fields.
		// Assume we have fields "tail", "manyItems" and "precursor" in the data structure.
		// Don't use "this", spaces or parens in your answers.
		ignore(Ts(1879723487)); // How do we get the dummy node? 
		ignore(Ts(210447483)); // How do we get the first element of the Sequence (assuming it exists)?
		ignore(Ts(1194659650)); // How do we get the current data, assuming it exists?
	}
	
	public void testL1() {
		ignore(Ti(659926051)); // If we have ten elements in the sequence, how many nodes are in the data structure?
	}
	
	public void testL2() {
		// Assume that Node.toString is overridden to return the string
		// "Node(D)" where "D" is replaced with "Null" if the data is null,
		// and otherwise with the name of the class of the data (e.g. "Integer" or "Node").
		// Assume we representing the Sequence [1,*2,3] -- three elements 1, 2, and 3 where the middle element is current
		ignore(Ts(1449661744)); // What is tail.toString() ?
		ignore(Ts(910927874)); // What is precursor.data.toString() ?
		ignore(Ts(1023092366)); // What is tail.next.toString() ?
	}
	
	
	public void testA0() {
		self = spy.newInstance(d0, d0, 0);
		assertWellFormed(true, self);
	}
	
	public void testA1() {
		self = spy.newInstance(d0, null, 0);
		assertWellFormed(false, self);
	}
	
	public void testA2() {
		self = spy.newInstance(d0, n1, 0);
		spy.setNext(n1, d0);
		assertWellFormed(false, self);
	}
	
	public void testA3() {
		self = spy.newInstance(null, d0, 0);
		assertWellFormed(false, self);
	}
	
	public void testA4() {
		self = spy.newInstance(d0, d0, 0);
		spy.setNext(d0, null);
		assertWellFormed(false, self);
	}
	
	public void testA5() {
		self = spy.newInstance(n0, n0, 0);
		spy.setNext(n0, n0);
		assertWellFormed(false, self);
	}
	
	public void testA6() {
		self = spy.newInstance(n1, n1, 0);
		spy.setNext(n1, n1);
		assertWellFormed(false, self);
	}
	
	public void testA7() {
		self = spy.newInstance(d0, d0, 0);
		spy.setData(d0, d1);
		assertWellFormed(false, self);
	}
	
	public void testA8() {
		self = spy.newInstance(d0, d1, 0);
		assertWellFormed(false, self);
	}
	
	public void testA9() {
		self = spy.newInstance(d0, d1, 0);
		spy.setData(d1, d0);
		assertWellFormed(false, self);
	}

	public void testB0() {
		self = spy.newInstance(d0, d0, 1);
		assertWellFormed(false, self);
	}

	public void testB1() {
		self = spy.newInstance(d0, d0, -1);
		assertWellFormed(false, self);
	}
	
	public void testB2() {
		self = spy.newInstance(n0, n0, 1);
		spy.setNext(d0, n0);
		spy.setNext(n0, d0);
		assertWellFormed(true, self);
	}
	
	public void testB3() {
		self = spy.newInstance(n0, n0, 0);
		spy.setNext(d0, n0);
		spy.setNext(n0, d0);
		assertWellFormed(false, self);
	}
	
	public void testB4() {
		self = spy.newInstance(n0, n0, 2);
		spy.setNext(d0, n0);
		spy.setNext(n0, d0);
		assertWellFormed(false, self);
	}
	
	public void testB5() {
		self = spy.newInstance(d0, d0, 1);
		spy.setNext(d0, n0);
		spy.setNext(n0, d0);
		assertWellFormed(false, self);
	}
	
	/* Don't check this:
	public void testB6() {
		self = spy.newInstance(d0, d0, 1);
		spy.setNext(d0, d1);
		spy.setNext(d1, d0);
		assertWellFormed(false, self);
	}*/
	
	public void testB6() {
		self = spy.newInstance(n0, n0, 1);
		spy.setNext(n0, d0);
		spy.setNext(d0, null);
		assertWellFormed(false, self);
	}
	
	public void testB7() {
		self = spy.newInstance(n1, n1, 1);
		spy.setNext(n1, n0);
		spy.setNext(n0, n1);
		assertWellFormed(false, self);
	}
	
	public void testB8() {
		self = spy.newInstance(n0, n0, 1);
		spy.setNext(d0, n0);
		spy.setNext(n0, d0);
		spy.setData(d0, n0);
		assertWellFormed(false, self);
	}
	
	public void testB9() {
		self = spy.newInstance(n1, n1, 1);
		spy.setNext(n1, d1);
		assertWellFormed(false, self);
	}
	
	public void testC0() {
		self = spy.newInstance(n1, n1, 1);
		spy.setNext(n1, d1);
		spy.setNext(d1, n1a);
		spy.setNext(n1a, d1);
		assertWellFormed(false, self);
	}

	public void testC1() {
		self = spy.newInstance(n1, d1, 1);
		spy.setNext(n1, d1);
		spy.setNext(d1, n1);
		assertWellFormed(true, self);
	}

	public void testC2() {
		self = spy.newInstance(n1, n1a, 1);
		spy.setNext(n1, d1);
		spy.setNext(d1, n1);
		spy.setNext(n1a, d1);
		assertWellFormed(false, self);
	}
	
	public void testC3() {
		self = spy.newInstance(n1, d2, 1);
		spy.setNext(n1, d1);
		spy.setNext(d1, n1);
		spy.setNext(d2, n1);
		assertWellFormed(false, self);
	}
	
	public void testC4() {
		self = spy.newInstance(n1, d2, 1);
		spy.setNext(n1, d1);
		spy.setNext(d1, n1);
		spy.setNext(d2, n1);
		spy.setData(d2, d1);
		assertWellFormed(false, self);
	}

	public void testC5() {
		self = spy.newInstance(n0, null, 1);
		spy.setNext(n0, d0);
		spy.setNext(d0, n0);
		assertWellFormed(false, self);
	}

	public void testC6() {
		self = spy.newInstance(n0, null, 1);
		spy.setNext(n0, d0);
		spy.setNext(d0, null);
		assertWellFormed(false, self);
	}

	public void testD0() {
		self = spy.newInstance(n2, n2, 2);
		spy.setNext(n2, d0);
		spy.setNext(d0, n1);
		spy.setNext(n1, n2);
		assertWellFormed(true, self);
	}

	public void testD1() {
		self = spy.newInstance(n2, n2, 2);
		spy.setNext(n2, d0);
		spy.setNext(d0, n1);
		spy.setNext(n1, null);
		assertWellFormed(false, self);
	}

	public void testD2() {
		self = spy.newInstance(n2, n2, 2);
		spy.setNext(n2, d0);
		spy.setNext(d0, null);
		assertWellFormed(false, self);
	}

	public void testD3() {
		self = spy.newInstance(n2, n2, 2);
		spy.setNext(n2, null);
		assertWellFormed(false, self);
	}

	public void testD4() {
		self = spy.newInstance(n2, n2, 2);
		spy.setNext(n2, d0);
		spy.setNext(d0, n1);
		spy.setNext(n1, n2);
		spy.setData(d0, null);
		assertWellFormed(false, self);
	}

	public void testD5() {
		self = spy.newInstance(n2, n2, 2);
		spy.setNext(n2, d0);
		spy.setNext(d0, n1);
		spy.setNext(n1, n2);
		spy.setData(d0, "Dummy");
		assertWellFormed(false, self);
	}

	public void testD6() {
		self = spy.newInstance(n2, n2, 2);
		spy.setNext(n2, d0);
		spy.setNext(d0, n0);
		spy.setNext(n0, n2);
		assertWellFormed(true, self);
	}
	
	public void testD7() {
		self = spy.newInstance(n2, n2, 2);
		spy.setNext(n2, d0);
		spy.setNext(d0, n1);
		spy.setNext(n1, d0);
		assertWellFormed(false, self);
	}

	public void testD8() {
		self = spy.newInstance(n2, n2, 1);
		spy.setNext(n2, d0);
		spy.setNext(d0, n1);
		spy.setNext(n1, n2);
		assertWellFormed(false, self);
	}

	public void testD9() {
		self = spy.newInstance(n2, n2, 3);
		spy.setNext(n2, d0);
		spy.setNext(d0, n1);
		spy.setNext(n1, n2);
		assertWellFormed(false, self);
	}

	public void testE0() {
		self = spy.newInstance(n2, null, 2);
		spy.setNext(n2, d0);
		spy.setNext(d0, n1);
		spy.setNext(n1, n2);
		assertWellFormed(false, self);
	}

	public void testE1() {
		self = spy.newInstance(n2, d0, 2);
		spy.setNext(n2, d0);
		spy.setNext(d0, n1);
		spy.setNext(n1, n2);
		assertWellFormed(true, self);
	}

	public void testE2() {
		self = spy.newInstance(n2, d1, 2);
		spy.setNext(n2, d0);
		spy.setNext(d0, n1);
		spy.setNext(n1, n2);
		spy.setNext(d1, n1);
		assertWellFormed(false, self);
	}

	public void testE3() {
		self = spy.newInstance(n2, n1, 2);
		spy.setNext(n2, d0);
		spy.setNext(d0, n1);
		spy.setNext(n1, n2);
		assertWellFormed(true, self);
	}

	public void testE4() {
		self = spy.newInstance(n2, n1a, 2);
		spy.setNext(n2, d0);
		spy.setNext(d0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n1a, n2);
		assertWellFormed(false, self);
	}

	public void testE5() {
		self = spy.newInstance(n2, n2a, 2);
		spy.setNext(n2, d0);
		spy.setNext(d0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2a, d0);
		assertWellFormed(false, self);
	}

	public void testE6() {
		self = spy.newInstance(n2, n1, 2);
		spy.setNext(n2, d0);
		spy.setNext(d0, n1);
		spy.setNext(n1, n2);
		spy.setData(d0, d1);
		assertWellFormed(false, self);
	}

	public void testF0() {
		self = spy.newInstance(n0, n0, 3);
		spy.setNext(n0, d1);
		spy.setNext(d1, n2);
		spy.setNext(n2, n1);
		spy.setNext(n1, n0);
		assertWellFormed(true, self);
	}

	public void testF1() {
		self = spy.newInstance(n0, n0, 3);
		spy.setNext(n0, d1);
		spy.setNext(d1, n2);
		spy.setNext(n2, n1);
		spy.setNext(n1, null);
		assertWellFormed(false, self);
	}

	public void testF2() {
		self = spy.newInstance(n0, n0, 3);
		spy.setNext(n0, d1);
		spy.setNext(d1, n2);
		spy.setNext(n2, n1);
		spy.setNext(n1, d1);
		assertWellFormed(false, self);
	}

	public void testF3() {
		self = spy.newInstance(n0, n0, 3);
		spy.setNext(n0, d1);
		spy.setNext(d1, n2);
		spy.setNext(n2, n1);
		spy.setNext(n1, n2);
		assertWellFormed(false, self);
	}

	public void testF4() {
		self = spy.newInstance(n0, n0, 3);
		spy.setNext(n0, d1);
		spy.setNext(d1, n2);
		spy.setNext(n2, n1);
		spy.setNext(n1, n1);
		assertWellFormed(false, self);
	}
	
	public void testF5() {
		self = spy.newInstance(n0, d1, 3);
		spy.setNext(n0, d1);
		spy.setNext(d1, n2);
		spy.setNext(n2, n1);
		spy.setNext(n1, n0);
		assertWellFormed(true, self);
	}
	
	public void testF6() {
		self = spy.newInstance(n0, n2, 3);
		spy.setNext(n0, d1);
		spy.setNext(d1, n2);
		spy.setNext(n2, n1);
		spy.setNext(n1, n0);
		assertWellFormed(true, self);
	}
	
	public void testF7() {
		self = spy.newInstance(n0, n1, 3);
		spy.setNext(n0, d1);
		spy.setNext(d1, n2);
		spy.setNext(n2, n1);
		spy.setNext(n1, n0);
		assertWellFormed(true, self);
	}

	public void testF8() {
		self = spy.newInstance(n0, null, 3);
		spy.setNext(n0, d1);
		spy.setNext(d1, n2);
		spy.setNext(n2, n1);
		spy.setNext(n1, n0);
		assertWellFormed(false, self);
	}
	
	public void testF9() {
		self = spy.newInstance(n0, n1, 2);
		spy.setNext(n0, d1);
		spy.setNext(d1, n2);
		spy.setNext(n2, n1);
		spy.setNext(n1, n0);
		assertWellFormed(false, self);
	}

	public void testG0() {
		self = spy.newInstance(n0, n0a, 3);
		spy.setNext(n0, d1);
		spy.setNext(d1, n2);
		spy.setNext(n2, n1);
		spy.setNext(n1, n0);
		spy.setNext(n0a, d1);
		assertWellFormed(false, self);
	}

	public void testG1() {
		self = spy.newInstance(n0, n1a, 3);
		spy.setNext(n0, d1);
		spy.setNext(d1, n2);
		spy.setNext(n2, n1);
		spy.setNext(n1, n0);
		spy.setNext(n1a, n0);
		assertWellFormed(false, self);
	}

	public void testG2() {
		self = spy.newInstance(n0, n2a, 3);
		spy.setNext(n0, d1);
		spy.setNext(d1, n2);
		spy.setNext(n2, n1);
		spy.setNext(n1, n0);
		spy.setNext(n2a, n1);
		assertWellFormed(false, self);
	}

	public void testG3() {
		self = spy.newInstance(n0, d2, 3);
		spy.setNext(n0, d1);
		spy.setNext(d1, n2);
		spy.setNext(n2, n1);
		spy.setNext(n1, n0);
		spy.setNext(d2, n2);
		assertWellFormed(false, self);
	}

	public void testG4() {
		self = spy.newInstance(n0, d2, 3);
		spy.setNext(n0, d1);
		spy.setNext(d1, n2);
		spy.setNext(n2, n1);
		spy.setNext(n1, n0);
		spy.setNext(d2, n2);
		spy.setData(d2, d1);
		assertWellFormed(false, self);
	}

	public void testG5() {
		self = spy.newInstance(n0, d2, 3);
		spy.setNext(n0, d1);
		spy.setNext(d1, n2);
		spy.setNext(n2, n1);
		spy.setNext(n1, n0);
		spy.setNext(d2, n2);
		spy.setData(d2, "two");
		assertWellFormed(false, self);
	}

	public void testH0() {
		self = spy.newInstance(n5, n5, 5);
		spy.setNext(n5, d2);
		spy.setNext(d2, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		spy.setNext(n4, n5);
		assertWellFormed(true, self);
	}

	public void testH1() {
		self = spy.newInstance(n5, n5, 5);
		spy.setNext(n5, d2);
		spy.setNext(d2, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		spy.setNext(n4, null);
		assertWellFormed(false, self);
	}

	public void testH2() {
		self = spy.newInstance(n5, n5, 5);
		spy.setNext(n5, d2);
		spy.setNext(d2, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		spy.setNext(n4, d2);
		assertWellFormed(false, self);
	}

	public void testH3() {
		self = spy.newInstance(n5, n5, 5);
		spy.setNext(n5, d2);
		spy.setNext(d2, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		spy.setNext(n4, n1);
		assertWellFormed(false, self);
	}

	public void testH4() {
		self = spy.newInstance(n5, n5, 5);
		spy.setNext(n5, d2);
		spy.setNext(d2, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		spy.setNext(n4, n2);
		assertWellFormed(false, self);
	}

	public void testH5() {
		self = spy.newInstance(n5, n5, 5);
		spy.setNext(n5, d2);
		spy.setNext(d2, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		spy.setNext(n4, n3);
		assertWellFormed(false, self);
	}

	public void testH6() {
		self = spy.newInstance(n5, n5, 5);
		spy.setNext(n5, d2);
		spy.setNext(d2, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		spy.setNext(n4, n4);
		assertWellFormed(false, self);
	}

	public void testH7() {
		self = spy.newInstance(n5, n5, 4);
		spy.setNext(n5, d2);
		spy.setNext(d2, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		spy.setNext(n4, n5);
		assertWellFormed(false, self);
	}

	public void testH8() {
		self = spy.newInstance(n5, n5, 6);
		spy.setNext(n5, d2);
		spy.setNext(d2, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		spy.setNext(n4, n5);
		assertWellFormed(false, self);
	}

	public void testH9() {
		self = spy.newInstance(n5, n5a, 5);
		spy.setNext(n5, d2);
		spy.setNext(d2, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		spy.setNext(n4, n5);
		spy.setNext(n5a, d2);
		assertWellFormed(false, self);
	}

	public void testI0() {
		self = spy.newInstance(n5, n4, 5);
		spy.setNext(n5, d2);
		spy.setNext(d2, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		spy.setNext(n4, n5);
		spy.setNext(n5a, d2);
		assertWellFormed(true, self);
	}

	public void testI1() {
		self = spy.newInstance(n5, n3, 5);
		spy.setNext(n5, d2);
		spy.setNext(d2, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		spy.setNext(n4, n5);
		spy.setNext(n5a, d2);
		assertWellFormed(true, self);
	}

	public void testI2() {
		self = spy.newInstance(n5, n2, 5);
		spy.setNext(n5, d2);
		spy.setNext(d2, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		spy.setNext(n4, n5);
		spy.setNext(n5a, d2);
		assertWellFormed(true, self);
	}

	public void testI3() {
		self = spy.newInstance(n5, n1, 5);
		spy.setNext(n5, d2);
		spy.setNext(d2, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		spy.setNext(n4, n5);
		spy.setNext(n5a, d2);
		assertWellFormed(true, self);
	}

	public void testI4() {
		self = spy.newInstance(n5, d2, 5);
		spy.setNext(n5, d2);
		spy.setNext(d2, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		spy.setNext(n4, n5);
		spy.setNext(n5a, d2);
		assertWellFormed(true, self);
	}

	public void testI5() {
		self = spy.newInstance(n5, d1, 5);
		spy.setNext(n5, d2);
		spy.setNext(d2, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		spy.setNext(n4, n5);
		spy.setNext(d1, n1);
		assertWellFormed(false, self);
	}

	public void testI6() {
		self = spy.newInstance(n5, n1a, 5);
		spy.setNext(n5, d2);
		spy.setNext(d2, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		spy.setNext(n4, n5);
		spy.setNext(n1a, n2);
		assertWellFormed(false, self);
	}

	public void testI7() {
		self = spy.newInstance(n5, n2a, 5);
		spy.setNext(n5, d2);
		spy.setNext(d2, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		spy.setNext(n4, n5);
		spy.setNext(n2a, n3);
		assertWellFormed(false, self);
	}

	public void testI8() {
		self = spy.newInstance(n5, n3a, 5);
		spy.setNext(n5, d2);
		spy.setNext(d2, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		spy.setNext(n4, n5);
		spy.setNext(n3a, n4);
		assertWellFormed(false, self);
	}

	public void testI9() {
		self = spy.newInstance(n5, n4a, 5);
		spy.setNext(n5, d2);
		spy.setNext(d2, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		spy.setNext(n4, n5);
		spy.setNext(n4a, n5);
		assertWellFormed(false, self);
	}
	
	
	/// model field testing
	
	public void testM0() {
		self = spy.newInstance(d0, d0, 0);
		assertSame(d0, spy.getHead(self));
	}
	
	public void testM1() {
		self = spy.newInstance(n0, d0, 1);
		spy.setNext(d0, n0);
		spy.setNext(n0, d0);
		assertSame(n0, spy.getHead(self));
	}
	
	public void testM2() {
		self = spy.newInstance(n3, n3, 3);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, d1);
		spy.setNext(d1, n1);
		assertSame(n1, spy.getHead(self));
	}
	
	public void testM3() {
		self = spy.newInstance(d0, d0, 0);
		assertSame(d0, spy.getDummy(self));
	}
	
	public void testM4() {
		self = spy.newInstance(n3, n2, 3);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, d1);
		spy.setNext(d1, n1);
		assertSame(d1, spy.getDummy(self));
	}
	
	public void testM5() {
		self = spy.newInstance(d0, d0, 0);
		assertSame(d0, spy.getCursor(self));
	}
	
	public void testM6() {
		self = spy.newInstance(n0, d0, 1);
		spy.setNext(d0, n0);
		spy.setNext(n0, d0);
		assertSame(n0, spy.getCursor(self));
	}
	
	public void testM7() {
		self = spy.newInstance(n0, n0, 1);
		spy.setNext(d0, n0);
		spy.setNext(n0, d0);
		assertSame(d0, spy.getCursor(self));
	}
	
	public void testM8() {
		self = spy.newInstance(n3, d1, 3);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, d1);
		spy.setNext(d1, n1);
		assertSame(n1, spy.getCursor(self));
	}
	
	public void testM9() {
		self = spy.newInstance(n3, d1, 3);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, d1);
		spy.setNext(d1, n1);
		assertSame(n1, spy.getCursor(self));
	}
	
}
