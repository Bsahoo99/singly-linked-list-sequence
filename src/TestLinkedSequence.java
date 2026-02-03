import java.util.function.Supplier;

import edu.uwm.cs351.LinkedSequence;
import junit.framework.TestCase;

public class TestLinkedSequence extends TestCase {
	Integer e1 = 101; 
	Integer e2 = 202;
	Integer e3 = 303;
	Integer e4 = 404;
	Integer e5 = 505;

	LinkedSequence<Integer> s;
	
	protected LinkedSequence<Integer> newSequence() {
		return new LinkedSequence<Integer>();
	}

	@Override
	protected void setUp() {
		s = newSequence();
		try {
			assert 1/((int)e1-101) == 42 : "OK";
			System.err.println("Assertions must be enabled to use this test suite.");
			System.err.println("In Eclipse: add -ea in the VM Arguments box under Run>Run Configurations>Arguments");
			assertFalse("Assertions must be -ea enabled in the Run Configuration>Arguments>VM Arguments",true);
		} catch (ArithmeticException ex) {
			return;
		}
	}

	protected <T> void assertException(Supplier<T> producer, Class<?> excClass) {
		try {
			T result = producer.get();
			assertFalse("Should have thrown an exception, not returned " + result,true);
		} catch (RuntimeException ex) {
			if (!excClass.isInstance(ex)) {
				ex.printStackTrace();
				assertFalse("Wrong kind of exception thrown: "+ ex.getClass().getSimpleName(),true);
			}
		}		
	}

	protected <T> void assertException(Class<?> excClass, Runnable f) {
		try {
			f.run();
			assertFalse("Should have thrown an exception, not returned",true);
		} catch (RuntimeException ex) {
			if (!excClass.isInstance(ex)) {
				ex.printStackTrace();
				assertFalse("Wrong kind of exception thrown: "+ ex.getClass().getSimpleName(),true);
			}
		}		
	}

	/**
	 * Return the Integer as an int
	 * <dl>
	 * <dt>-1<dd><i>(an exception was thrown)
	 * <dt>0<dd>null
	 * <dt>1<dd>e1
	 * <dt>2<dd>e2
	 * <dt>3<dd>e3
	 * <dt>4<dd>e4
	 * <dt>5<dd>e5
	 * </dl>
	 * @return int encoding of Integer supplied
	 */
	protected int asInt(Supplier<Integer> g) {
		try {
			Integer n = g.get();
			if (n == null) return 0;
			return n / 100;
		} catch (RuntimeException ex) {
			return -1;
		}
	}
	
	/// test0n: formerly locked tests
	
	public void test00() {
		// Nothing inserted yet:
		assertEquals(0,s.size());
		assertFalse(s.isCurrent());
		s.start();
		assertFalse(s.isCurrent());
	}
	
	public void test01() {
		// Initially empty.
		// -1 for error, 0 for null, 1 for e1, 2 for e2 ...
		assertEquals(-1, asInt(() -> s.getCurrent()));
		s.insert(e1);
		assertEquals(1,asInt(() -> s.getCurrent()));
		s.start();
		assertEquals(1,asInt(() -> s.getCurrent()));
		s.advance();
		assertEquals(-1,asInt(() -> s.getCurrent()));
	}
	
	public void test02() {
		// Initially empty.
		s.insert(e4);
		s.insert(e5);
		// -1 for error, 0 for null, 1 for e1, 2 for e2 ...
		assertEquals(5,asInt(() -> s.getCurrent()));
		s.advance();
		assertEquals(4,asInt(() -> s.getCurrent()));
		s.advance();
		assertEquals(-1,asInt(() -> s.getCurrent()));
	}
	
	public void test03() {
		// Initially empty
		s.insert(e3);
		s.advance();
		s.insert(e2);
		// -1 for error, 0 for null, 1 for e1, 2 for e2 ...
		assertEquals(2,asInt(() -> s.getCurrent()));
		s.advance();
		assertEquals(-1,asInt(() -> s.getCurrent()));
		s.start();
		assertEquals(3,asInt(() -> s.getCurrent()));
	}
	
	public void test05() {
		// Initially empty
		s.insert(null);
		assertEquals(1,s.size());
		assertEquals(true,s.isCurrent());
		// -1 for error, 0 for null, 1 for e1, 2 for e2 ...
		assertEquals(0,asInt(() -> s.getCurrent()));
		s.advance();
		assertEquals(-1,asInt(() -> s.getCurrent()));
	}
	
	public void test06() {
		s.insert(e1);
		s.insert(e2);
		s.start();
		s.advance();
		assertSame(e1,s.getCurrent());
		LinkedSequence<Integer> s2 = newSequence();
		s2.insert(e4);
		s.insertAll(s2);
		assertEquals(3,s.size());
		// -1 for error, 0 for null, 1 for e1, 2 for e2 ...
		assertEquals(1,asInt(() -> s.getCurrent()));
		s.advance();
		assertEquals(-1,asInt(() -> s.getCurrent()));
	}
	
	
	/// test1N: tests of a single element sequence
	
	public void test10() {
		s.insert(e1);
		assertEquals(1,s.size());
	}
	
	public void test11() {
		s.insert(e2);
		assertTrue(s.isCurrent());
	}
	
	/* Duplicated by earlier tests.
	public void test12() {
		s.insert(e3);
		assertSame(e3,s.getCurrent());
	}
	*/
	
	public void test13() {
		s.insert(e4);
		s.start();
		assertSame(e4,s.getCurrent());
	}
	
	public void test14() {
		s.insert(e5);
		s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test15() {
		s.insert(e5);
		s.advance();
		s.start();
		assertTrue(s.isCurrent());
	}
	
	/* duplicated by test05
	public void test16() {
		s.insert(null);
		assertEquals(1,s.size());
	}
	*/
	
	public void test17() {
		s.insert(null);
		assertTrue(s.isCurrent());
		assertNull(s.getCurrent());
	}
	
	public void test18() {
		s.insert(null);
		s.advance();
		assertFalse(s.isCurrent());
	}
	
	/// test2N: more complex tests with small sequences
	
	public void test20() {
		s.insert(e1);
		assertEquals(1,s.size());
		assertTrue(s.isCurrent());
		assertSame(e1,s.getCurrent());
		s.start();
		assertTrue(s.isCurrent());
		assertSame(e1,s.getCurrent());
		assertSame(e1,s.getCurrent());
		s.advance();
		assertEquals(1,s.size());
		assertFalse(s.isCurrent());
		s.start();
		assertTrue(s.isCurrent());
		assertSame(e1,s.getCurrent());
		assertEquals(1,s.size());
	}
	
	public void test21() {
		s.start();
		assertException(IllegalStateException.class, () -> s.getCurrent());
	}

	public void test22() {
		assertException(IllegalStateException.class, () -> s.advance());
	}

	public void test23() {
		s.insert(e2);
		s.start();
		s.advance();
		assertException(IllegalStateException.class, () -> s.advance());
		assertFalse(s.isCurrent());
		assertEquals(1,s.size());
	}

	public void test24() {
		s.insert(e2);
		s.advance();
		assertException(IllegalStateException.class, () -> s.getCurrent());
		assertFalse(s.isCurrent());
		assertEquals(1,s.size());
	}
	
	public void test25() {
		s.insert(e1);
		s.insert(e2);
		assertEquals(2,s.size());
	}
	
	public void test26() {
		s.insert(e3);
		s.insert(e4);
		assertTrue(s.isCurrent());
	}
	
	public void test27() {
		s.insert(e1);
		s.insert(e2);
		assertSame(e2,s.getCurrent());
	}
	
	public void test28() {
		s.insert(e1);
		assertTrue(s.isCurrent());
		assertSame(e1,s.getCurrent());
		s.insert(e2);
		assertTrue(s.isCurrent());
		assertSame(e2,s.getCurrent());
		assertEquals(2,s.size());
		s.start();
		assertTrue(s.isCurrent());
		assertSame(e2,s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(e1,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
		assertEquals(2,s.size());
		s.start();
		assertSame(e2,s.getCurrent());
		s.advance();
		s.start();
		assertSame(e2,s.getCurrent());
	}
	
	public void test29() {
		s.insert(e1);
		assertTrue(s.isCurrent());
		assertSame(e1,s.getCurrent());
		s.advance();
		s.insert(e2);
		assertTrue(s.isCurrent());
		assertSame(e2,s.getCurrent());
		assertEquals(2,s.size());
		s.start();
		assertSame(e1,s.getCurrent());
		s.advance();
		assertSame(e2,s.getCurrent());
		assertTrue(s.isCurrent());
	}
	
	
	/// test3n: tests of larger sequences
	
	public void test30() {
		s.insert(e1);
		s.insert(e2);
		s.insert(e3);
		assertEquals(3,s.size());
		s.start();
		assertTrue(s.isCurrent());
		assertSame(e3,s.getCurrent());
		assertTrue(s.isCurrent());
		assertSame(e3,s.getCurrent());
		s.advance();
		assertSame(e2,s.getCurrent());
		assertTrue(s.isCurrent());
		assertSame(e2,s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(e1,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
		assertEquals(3,s.size());
		s.start();
		assertSame(e3,s.getCurrent());
		s.advance();
		s.start();
		assertSame(e3,s.getCurrent());
	}
	
	public void test31() {
		s.insert(e1);
		s.advance();
		s.insert(e2);
		s.insert(e3);
		assertEquals(3,s.size());
		s.start();
		assertTrue(s.isCurrent());
		assertSame(e1,s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(e3,s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(e2,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
		assertEquals(3,s.size());
		s.start();
		assertSame(e1,s.getCurrent());
	}

	public void test32() {
		s.insert(e2);
		s.advance();
		s.insert(e3);
		s.start();
		s.insert(e1);
		assertEquals(3,s.size());
		s.start();
		assertTrue(s.isCurrent());
		assertSame(e1,s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(e2,s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(e3,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
		assertEquals(3,s.size());
	}
	
	public void test33() {
		s.insert(e1); s.advance();
		s.insert(e2); s.advance();
		s.insert(e3);
		assertSame(e3, s.getCurrent());
		s.start();
		assertEquals(e1,s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(e2,s.getCurrent());
		s.advance();
		assertSame(e3,s.getCurrent());
	}

	public void test34() {
		s.insert(e3);
		s.insert(e1);
		s.advance();
		s.insert(null);
		assertNull(s.getCurrent());
		s.advance();
		assertSame(e3,s.getCurrent());
		s.start();
		assertEquals(3,s.size());
		assertTrue(s.isCurrent());
		assertSame(e1,s.getCurrent());
	}
	
	public void test35() {
		s.insert(e2);
		s.advance();
		s.insert(null);
		s.start();
		s.insert(null);
		// s.start(); // REDUNDANT
		assertNull(s.getCurrent());
		s.advance();
		assertSame(e2,s.getCurrent());
		s.advance();
		assertNull(s.getCurrent());
		s.advance();
		assertEquals(3,s.size());
		assertFalse(s.isCurrent());
		assertException(IllegalStateException.class, () -> s.getCurrent());
		assertException(IllegalStateException.class, () -> s.advance());
	}
 
	public void test38() {
		s.insert(e1);
		s.insert(e2);
		s.insert(e3);
		s.insert(e4);
		s.insert(e5);
		assertSame(e5,s.getCurrent());
		s.insert(e1);
		s.insert(e2);
		s.insert(e3);
		s.insert(e4);
		s.insert(e5);
		assertEquals(10,s.size());
		assertTrue(s.isCurrent());
		s.start();
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertSame(e1,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test39() {
		for (int i=0; i < 39; ++i) {
			s.insert(i);
			s.advance();
		}
		s.start();
		for (int i=0; i < 39; ++i) {
			assertEquals(Integer.valueOf(i),s.getCurrent());
			s.advance();
		}
		assertEquals(39,s.size());
	}

	
	/// test4N: tests of removeCurrent
	
	public void test40() {
		s.insert(e1);
		s.removeCurrent();
		assertFalse(s.isCurrent());
	}
	
	public void test41() {
		s.insert(e2);
		s.removeCurrent();
		assertEquals(0,s.size());
	}
	
	public void test42() {
		s.insert(e3);
		s.advance();
		s.insert(e4);
		s.removeCurrent();
		assertFalse(s.isCurrent());
		assertEquals(1,s.size());
		s.start();
		assertSame(e3,s.getCurrent());
	}
	
	public void test43() {
		s.insert(e4);
		s.insert(e3);
		s.start();
		s.removeCurrent();
		assertTrue(s.isCurrent());
		assertSame(e4,s.getCurrent());
	}
	
	public void test44() {
		s.insert(e4);
		s.advance();
		s.insert(null);
		s.start();
		s.removeCurrent();
		assertTrue(s.isCurrent());
		assertSame(null,s.getCurrent());
	}
	
	public void test45() {
		s.insert(e5);
		s.insert(e4);
		s.insert(e3);
		s.start();
		s.advance();
		s.removeCurrent();
		assertSame(e5,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
		assertEquals(2,s.size());
		s.start();
		assertSame(e3,s.getCurrent());
	}
	
	public void test46() {
		s.insert(e4); s.advance();
		s.insert(e5); s.advance();
		s.insert(null);
		s.removeCurrent();
		assertEquals(2,s.size());
		assertFalse(s.isCurrent());
		s.start();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test47() {
		s.insert(e5);
		s.insert(e4);
		s.insert(e3);
		s.insert(e2);
		s.insert(e1);
		s.start();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); 
		s.removeCurrent();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent());
		s.removeCurrent();
		assertSame(e5,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
		s.start();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test48() {
		s.insert(e5);
		s.insert(e4);
		s.insert(e3);
		s.insert(e2);
		s.insert(e1);
		s.start();
		assertSame(e1,s.getCurrent()); s.removeCurrent();
		assertSame(e2,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.removeCurrent();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); s.removeCurrent();
		assertFalse(s.isCurrent());
		s.start();
		assertSame(e2,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test49() {
		for (int i=0; i < 49; ++i) {
			s.insert(i);
			s.advance();
		}
		s.start();
		for (int i=0; i < 49; ++i) {
			assertSame(Integer.valueOf(i),s.getCurrent());
			if ((i%2) == 0) s.removeCurrent();
			else s.advance();
		}
		s.start();
		for (int i=0; i < 24; ++i) {
			assertSame(Integer.valueOf(i*2+1),s.getCurrent());
			s.removeCurrent();
		}
		assertEquals(0,s.size());
	}
	
	
	/// test5N: errors with removeCurrent
	
	public void test50() {
		assertException(IllegalStateException.class, () -> s.removeCurrent());
	}
	
	public void test51() {
		s.insert(e1);
		s.advance();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
	}
	
	public void test52() {
		s.insert(e1);
		s.removeCurrent();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
	}
	
	public void test53() {
		s.insert(e1); s.advance();
		s.insert(e2);
		s.removeCurrent();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
	}
	
	public void test54() {
		s.insert(e1); s.advance();
		s.insert(e2); s.advance();
		s.insert(e3); s.advance();
		s.insert(e4); s.advance();
		s.insert(e5);
		s.advance();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
	}
	
	public void test55() {
		s.insert(e5);
		s.advance();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
		s.start();
		s.insert(e4);
		s.advance();
		s.advance();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
		s.start();
		s.insert(e3);
		s.advance();
		s.advance();
		s.advance();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
		s.start();
		s.insert(e2);
		s.advance();
		s.advance();
		s.advance();
		s.advance();
		assertException(IllegalStateException.class, () -> s.removeCurrent());
		s.start();
		s.insert(e1);
		assertEquals(5,s.size());
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
	}

	public void test59() {
		s.insert(null);
		s.advance();
		s.insert(e2);
		s.advance();
		s.insert(null);
		
		assertEquals(3,s.size());
		assertTrue(s.isCurrent());
		assertSame(null,s.getCurrent());
		s.start();
		assertTrue(s.isCurrent());
		assertSame(null,s.getCurrent());
		s.removeCurrent();
		assertEquals(2,s.size());
		assertTrue(s.isCurrent());
		assertSame(e2,s.getCurrent());
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(null,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
	}
	
	public void test60() {
		LinkedSequence<Integer> se = newSequence();
		s.insertAll(se);
		assertFalse(s.isCurrent());
		assertEquals(0,s.size());
		s.insert(e1);
		s.insertAll(se);
		assertTrue(s.isCurrent());
		assertEquals(1,s.size());
		assertEquals(0,se.size());
		assertSame(e1,s.getCurrent());
		s.advance();
		s.insertAll(se);
		assertFalse(s.isCurrent());
		assertEquals(1,s.size());
		assertEquals(0,se.size());
		s.insert(e2);
		s.insertAll(se);
		assertTrue(s.isCurrent());
		assertSame(e2,s.getCurrent());
		assertEquals(2,s.size());
		assertEquals(0,se.size());
		s.start();
		s.insertAll(se);
		assertTrue(s.isCurrent());
		assertSame(e1,s.getCurrent());
		assertEquals(2,s.size());
		assertEquals(0,se.size());
	}
	
	public void test61() {
		LinkedSequence<Integer> se = newSequence();
		se.insert(e1);
		s.insertAll(se);
		assertFalse(s.isCurrent());
		assertTrue(se.isCurrent());
		assertEquals(1,s.size());
		assertEquals(1,se.size());
		s.start();
		assertSame(e1,s.getCurrent());
		assertSame(e1,se.getCurrent());
	}
	
	public void test62() {
		LinkedSequence<Integer> se = newSequence();
		se.insert(e1);
		s.insert(e2);
		s.insertAll(se);
		assertTrue(s.isCurrent());
		assertEquals(2,s.size());
		assertEquals(1,se.size());
		assertSame(e1,se.getCurrent());
		assertSame(e2,s.getCurrent());
	}
	
	public void test63() {
		LinkedSequence<Integer> se = newSequence();
		se.insert(e1);
		s.insert(e2);
		s.advance();
		s.insertAll(se);
		assertFalse(s.isCurrent());
		assertEquals(2,s.size());
		assertEquals(1,se.size());
		assertTrue(se.isCurrent());
		assertSame(e1,se.getCurrent());
		s.start();
		assertSame(e2,s.getCurrent());
		s.advance();
		assertSame(e1,s.getCurrent());
	}
	
	public void test64() {
		LinkedSequence<Integer> se = newSequence();
		se.insert(e1);
		se.advance();
		s.insert(e3);
		s.insert(e2);
		s.insertAll(se);
		assertTrue(s.isCurrent());
		assertSame(e2,s.getCurrent());
		assertEquals(3,s.size());
		assertEquals(1,se.size());
		assertFalse(se.isCurrent());
		s.advance();
		assertSame(e3,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());	
		s.start();
		assertSame(e1,s.getCurrent());
	}
	
	public void test65() {
		LinkedSequence<Integer> se = newSequence();
		se.insert(e1);
		s.insert(e2);
		s.advance();
		s.insert(e3);
		s.insertAll(se);
		assertTrue(s.isCurrent());
		assertEquals(3,s.size());
		assertEquals(1,se.size());
		assertSame(e3,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
		s.start();
		assertSame(e2,s.getCurrent());
		s.advance();
		assertSame(e1,s.getCurrent());
	}
	
	public void test66() {
		LinkedSequence<Integer> se = newSequence();
		se.insert(e1);
		s.insert(e2);
		s.advance();
		s.insert(e3);
		s.advance();
		assertFalse(s.isCurrent());
		s.insertAll(se);
		assertFalse(s.isCurrent());
		assertEquals(3,s.size());
		assertEquals(1,se.size());
		assertSame(e1,se.getCurrent());
		s.start();
		assertSame(e2,s.getCurrent());
		s.advance();
		assertSame(e3,s.getCurrent());
		s.advance();
		assertSame(e1,s.getCurrent());
	}

	public void test67() {
		LinkedSequence<Integer> se = newSequence();
		se.insert(e2);
		se.insert(e1);	
		s.insert(e4);
		s.insert(e3);
		s.insertAll(se);
		assertTrue(s.isCurrent());
		assertEquals(4,s.size());
		assertEquals(2,se.size());
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());	
		s.start();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
	}

	public void test68() {
		LinkedSequence<Integer> se = newSequence();
		se.insert(e2);
		se.insert(e1);
		se.advance();
		s.insert(e3);
		s.advance();
		s.insert(e4);
		s.insertAll(se);
		assertTrue(s.isCurrent());
		assertEquals(4,s.size());
		assertEquals(2,se.size());
		assertSame(e2,se.getCurrent()); se.advance();
		assertFalse(se.isCurrent());
		// check s
		assertSame(e4,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());	
		s.start();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
	}

	public void test69() {
		LinkedSequence<Integer> se = newSequence();
		se.insert(e2);
		se.insert(e1);
		se.advance();
		se.advance();
		s.insert(e3);
		s.advance();
		s.insert(e4);
		s.advance();
		assertFalse(s.isCurrent());
		assertFalse(se.isCurrent());
		s.insertAll(se);
		assertFalse(s.isCurrent());
		assertFalse(se.isCurrent());
		assertEquals(4,s.size());
		assertEquals(2,se.size());
		s.start();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());	
	}

	public void test70() {
		LinkedSequence<Integer> se = newSequence();
		se.insert(e3);
		se.insert(e4);
		se.insert(e5);
		se.insert(e3);
		se.insert(e4);
		se.insert(e5);
		se.insert(e3);
		se.insert(e4);
		se.insert(e5);
		se.insert(e3);
		se.insert(e4);
		se.insert(e5);
		se.insert(e3);
		se.insert(e4);
		se.insert(e5);
		se.insert(e3);
		se.insert(e4);
		se.insert(e5);
		se.insert(e3);
		se.insert(e4);
		se.insert(e5);
		se.insert(e3);
		se.insert(e4);
		se.insert(e5);
		// se has 24 elements
		s.insert(e1);
		s.advance();
		s.insert(e2);
		s.insertAll(se);
		assertEquals(26,s.size());
		s.start();
		s.advance();
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();		
		s.insertAll(se);
		assertEquals(50,s.size());
		s.start();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		// interruption
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); s.advance();
		assertSame(e4,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		// done with all 24 copies most recently inserted
		// now back to the original
		assertSame(e3,s.getCurrent()); s.advance();
		assertSame(e5,s.getCurrent()); // etc.
	}
	
	public void test71() {
		s.insertAll(s);
		assertFalse(s.isCurrent());
		assertEquals(0,s.size());
	}
	
	
	public void test72() {
		s.insert(e1);
		s.insertAll(s);
		assertEquals(2,s.size());
		assertTrue(s.isCurrent());
		assertSame(e1,s.getCurrent());
		s.advance();
		assertFalse(s.isCurrent());
		s.start();
		assertTrue(s.isCurrent());
		assertSame(e1,s.getCurrent());
	}
	
	public void test73() {
		s.insert(e1);
		s.advance();
		s.insertAll(s);
		assertEquals(2,s.size());
		assertFalse(s.isCurrent());
		s.start();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e1,s.getCurrent());
	}
	
	public void test74() {
		s.insert(e1);
		s.removeCurrent();
		s.insertAll(s);
		assertEquals(0,s.size());
		assertFalse(s.isCurrent());
	}
	
	public void test75() {
		s.insert(e2);
		s.insert(e1);
		s.insertAll(s);
		assertEquals(4,s.size());
		assertTrue(s.isCurrent());
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());		
		s.start();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
	}
	
	public void test76() {
		s.insert(e1);
		s.advance();
		s.insert(e2);
		s.insertAll(s);
		assertEquals(4,s.size());
		assertTrue(s.isCurrent());
		assertSame(e2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());	
		s.start();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
	}

	public void test77() {
		s.insert(e1);
		s.advance();
		s.insert(e2);
		s.advance();
		assertFalse(s.isCurrent());
		s.insertAll(s);
		assertFalse(s.isCurrent());
		assertEquals(4,s.size());
		s.start();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());		
	}

	public void test78() {
		s.insert(e1);
		s.advance();
		s.insert(e2);
		s.insertAll(s);
		s.removeCurrent();
		s.insert(e3);
		assertSame(e3,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());
		s.start();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e1,s.getCurrent()); s.advance();
		s.insertAll(s);
		assertEquals(8,s.size());
		assertTrue(s.isCurrent());
		assertSame(e2,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
		assertFalse(s.isCurrent());		
		s.start();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e2,s.getCurrent()); s.advance();
		assertSame(e3,s.getCurrent()); s.advance();
	}
	
	public void test79() {
		LinkedSequence<Integer> se = newSequence();
		se.insert(e1);
		se.advance();
		se.insert(e2);	
		s.insert(e3);
		s.advance();
		s.insert(e4);
		s.insertAll(se); // s = e3 e1 e2 * e4
		s.insert(e5); // s = e3 e1 e2 * e5 e4
		s.advance();
		assertTrue(s.isCurrent());
		assertSame(e4,s.getCurrent());
		assertEquals(5,s.size());
		assertEquals(2,se.size());
		assertSame(e2,se.getCurrent());
		se.advance();
		assertFalse(se.isCurrent());
		se.start();
		assertSame(e1,se.getCurrent());
	}
	
	public void test80() {
		LinkedSequence<Integer> c = s.clone();
		assertFalse(c.isCurrent());
		assertEquals(0, c.size());
	}
	
	public void test81() {
		s.insert(e1);
		LinkedSequence<Integer> c = s.clone();
		
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(e1,s.getCurrent()); s.advance();
		assertSame(e1,c.getCurrent()); c.advance();
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
	}
	
	public void test82() {
		s.insert(e1);
		s.advance();
		LinkedSequence<Integer> c = s.clone();
		
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
	}

	public void test83() {
		LinkedSequence<Integer> c = s.clone();
		assertFalse(c.isCurrent());
		
		s.insert(e1);
		c = s.clone();
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(e1,s.getCurrent());
		assertSame(e1,c.getCurrent());
		
		s.advance();
		s.insert(e2);
		c = s.clone();
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(e2,s.getCurrent());
		assertSame(e2,c.getCurrent());
		s.advance();
		c.advance();
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
		
		s.insert(e3);
		assertTrue(s.isCurrent());
		assertFalse(c.isCurrent());
		
		c = s.clone();
		assertSame(e3,s.getCurrent());
		assertSame(e3,c.getCurrent());
		s.advance();
		c.advance();
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
		s.start();
		c.start();
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(e1,s.getCurrent());
		assertSame(e1,c.getCurrent());
		s.advance();
		c.advance();
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(e2,s.getCurrent());
		assertSame(e2,c.getCurrent());
		
		s.start();
		c = s.clone();
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(e1,s.getCurrent());
		assertSame(e1,c.getCurrent());
		s.advance();
		c.advance();
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(e2,s.getCurrent());
		assertSame(e2,c.getCurrent());
		s.advance();
		c.advance();
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		assertSame(e3,s.getCurrent());
		assertSame(e3,c.getCurrent());		
	}
	
	public void test84() {
		s.insert(e1);
		s.advance();
		s.insert(e3);
		s.insert(e2);
		s.removeCurrent();
		
		LinkedSequence<Integer> c = s.clone();
		
		assertEquals(2,c.size());
		
		assertTrue(s.isCurrent());
		assertTrue(c.isCurrent());
		
		assertSame(e3,s.getCurrent());
		assertSame(e3,c.getCurrent());
	}

	public void test85() {
		s.insert(e1);
		s.advance();
		s.insert(e2);
		
		LinkedSequence<Integer> c = s.clone();
		s.insert(e3);
		c.insert(e4);
		
		assertSame(e3,s.getCurrent());
		assertSame(e4,c.getCurrent());
		s.advance();
		c.advance();
		assertSame(e2,s.getCurrent());
		assertSame(e2,c.getCurrent());
		s.advance();
		c.advance();
		assertFalse(s.isCurrent());
		assertFalse(c.isCurrent());
		
		s.start();
		c.start();
		assertSame(e1,s.getCurrent());
		assertSame(e1,c.getCurrent());
		s.advance();
		c.advance();
		assertSame(e3,s.getCurrent());
		assertSame(e4,c.getCurrent());
	}
	
	private static class Foo {
		final String label;
		Foo(String l) { label = l; }
	}
	
	public void test86() {
 		LinkedSequence<Foo> s1 = new LinkedSequence<>();
 		s1.insert(new Foo("hi"));
 		assertEquals("hi",s1.getCurrent().label);
 	}
}
