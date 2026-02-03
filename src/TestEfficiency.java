import java.util.Random;

import edu.uwm.cs.junit.EfficiencyTestCase;
import edu.uwm.cs351.LinkedSequence;

public class TestEfficiency extends EfficiencyTestCase {
	
	LinkedSequence<String> s;
	Random r;
	
	@Override
	public void setUp() {
		s = new LinkedSequence<>();
		r = new Random();
		try {
			assert 1/s.size() == 42 : "OK";
			assertTrue(true);
		} catch (ArithmeticException ex) {
			System.err.println("Assertions must NOT be enabled to use this test suite.");
			System.err.println("In Eclipse: remove -ea from the VM Arguments box under Run>Run Configurations>Arguments");
			assertFalse("Assertions must NOT be enabled while running efficiency tests.",true);
		}
		super.setUp();
	}

	private static final int POWER = 20;
	private static final int MAX_LENGTH = 1<<POWER; // must be even
	
	public void test0() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			String si = ""+i;
			s.insert(si);
			assertSame(si,s.getCurrent());
		}
		assertEquals(MAX_LENGTH, s.size());
	}
	
	public void test1() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			String si = ""+i;
			s.insert(si);
			assertSame(si,s.getCurrent());
			s.advance();
		}
		assertEquals(MAX_LENGTH, s.size());
	}
	
	public void test2() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			String si = ""+i;
			s.insert(si);
		}
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertTrue(s.isCurrent());
			int j = MAX_LENGTH-1-i;
			assertEquals(""+j, s.getCurrent());
			s.advance();
		}
		assertFalse(s.isCurrent());
	}

	public void test3() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			String si = ""+i;
			s.insert(si);
			assertSame(si,s.getCurrent());
			s.advance();
		}
		s.start();
		for (int i=0; i < MAX_LENGTH; i +=2) {
			s.removeCurrent();
		}
		assertTrue(s.isCurrent());
		assertEquals(MAX_LENGTH/2, s.size());
		s.start();
		assertEquals(""+(MAX_LENGTH/2),s.getCurrent());
	}
	
	public void test4() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			String si = ""+i;
			s.insert(si);
			assertSame(si,s.getCurrent());
			s.advance();
		}
		s.start();
		for (int i=0; i < MAX_LENGTH; i +=2) {
			s.removeCurrent();
			s.advance();
		}
		assertFalse(s.isCurrent());
		assertEquals(MAX_LENGTH/2, s.size());
		s.start();
		assertEquals("1",s.getCurrent());
	}
	
	public void test5() {
		for (int i=0; i < MAX_LENGTH/2; ++i) {
			String si = ""+i;
			s.insert(si);
			s.advance(); 
			assertFalse(s.isCurrent());
		}
		for (int i=0; i < MAX_LENGTH/2; ++i) {
			String si = ""+i;
			s.insert(si);
			s.removeCurrent();
		}
		assertEquals(MAX_LENGTH/2, s.size());
	}

	public void test6() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			String si = ""+i;
			s.insert(si);
			assertSame(si,s.getCurrent());
			if (i % 2 == 0) s.advance();
		}
		String cur = s.getCurrent();
		LinkedSequence<String> c = s.clone();
		s.removeCurrent();
		assertEquals(MAX_LENGTH, c.size());
		assertSame(cur,c.getCurrent());
	}
	
	public void test7() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			String si = ""+i;
			s.insert(si);
			assertSame(si,s.getCurrent());
			if (i % 2 == 0) s.advance();
		}
		String cur = s.getCurrent();
		LinkedSequence<String> c = s.clone();
		c.removeCurrent();
		assertEquals(MAX_LENGTH, s.size());
		assertSame(cur,s.getCurrent());
	}

	private static final int SMALL = 4; // must be a factor of MAX_LENGTH
	
	public void test8() {
		LinkedSequence<String> other = new LinkedSequence<>();
		for (int i=0; i < SMALL; ++i) {
			other.insert(""+i);
		}
		for (int i=0; i < MAX_LENGTH; i += SMALL) {
			assertFalse(s.isCurrent());
			s.insertAll(other);
		}
		assertEquals(MAX_LENGTH, s.size());
	}

	public void test9() {
		s.insert("0");
		while (s.size() < MAX_LENGTH) {
			s.insertAll(s);
			s.insert(s.size()+"");
			System.out.println("Size is now " + s.size());
		}
		assertEquals((1<<(1+POWER))-1,s.size());
		assertEquals(""+((1<<(1+POWER))-2),s.getCurrent());
	}
}
